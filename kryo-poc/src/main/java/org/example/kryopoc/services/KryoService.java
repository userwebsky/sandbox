package org.example.kryopoc.services;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.example.kryopoc.serializers.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class KryoService {

  private Kryo createKryoInstance() {
    Kryo kryo = new Kryo();

    kryo.setRegistrationRequired(false);
    kryo.setReferences(true);
    kryo.setInstantiatorStrategy(new org.objenesis.strategy.StdInstantiatorStrategy());

    // --- NADPISYWANIE REJESTRACJI (ABY ZACHOWAĆ ID z KRYO 4) ---

    // 1. WRAPPERY (Istniejące ID, podmiana serializera, wyłączenie referencji)
    registerLegacy(kryo, Integer.class, new LegacyIntSerializer(), false);
    registerLegacy(kryo, Long.class, new LegacyLongSerializer(), false);
    registerLegacy(kryo, Float.class, new LegacyAdapters.LegacyFloatSerializer(), false);
    registerLegacy(kryo, Double.class, new LegacyAdapters.LegacyDoubleSerializer(), false);
    registerLegacy(kryo, Boolean.class, new LegacyAdapters.LegacyBooleanSerializer(), false);
    registerLegacy(kryo, Byte.class, new LegacyAdapters.LegacyByteSerializer(), false);
    registerLegacy(kryo, Character.class, new LegacyAdapters.LegacyCharSerializer(), false);
    registerLegacy(kryo, Short.class, new LegacyAdapters.LegacyShortSerializer(), false);

    registerLegacy(kryo, String.class, new LegacyStringSerializer(), false);

    // Prymitywy (int.class) - rzadziej używane jako root, ale warto
    kryo.register(int.class, new LegacyIntSerializer());
    kryo.register(long.class, new LegacyLongSerializer());

    // 2. TABLICE (Istniejące ID, podmiana serializera)
    // int[] ma domyślne ID w Kryo. Musimy je nadpisać.
    registerLegacy(kryo, int[].class, new LegacyIntArraySerializer(), true);
    registerLegacy(kryo, String[].class, new LegacyStringArraySerializer(), true);

    // 3. KOLEKCJE (Istniejące ID?? Nie, ArrayList w V4 ma ID 10/11 zależnie od wersji, w V5 inne)
    // Tu bezpieczniej użyć addDefaultSerializer, bo ID kolekcji w pliku V4 są konkretne.
    // Jeśli V4 używało domyślnych ID dla ArrayList (10), to musimy się wstrzelić w to ID.
    // Sprawdźmy: ArrayList w Kryo 4 ma ID 10. W Kryo 5 ma ID 9.
    // Błąd "Encountered unregistered class ID: 38" sugeruje, że to NIE są standardowe ID.
    // 38 to dość wysoki numer. Może `ArrayList` nie była domyślnie rejestrowana w V4 w tym kontekście?
    // Ale `complex_object_full` ma pole `arrayListInt`. FieldSerializer zapisuje ID klasy pola tylko jeśli typ pola jest niejednoznaczny (np. List interface).
    // Tu jest `ArrayList`. Kryo powinno zapisać ID 10 (V4).
    // Jeśli czytamy ID 38, to znaczy, że czytamy śmieci (przesunięcie w strumieniu).

    // Zarejestrujmy nasze serializery jako domyślne dla typów
    kryo.addDefaultSerializer(ArrayList.class, new LegacyCollectionSerializer());
    kryo.addDefaultSerializer(LinkedList.class, new LegacyCollectionSerializer());
    kryo.addDefaultSerializer(HashSet.class, new LegacyCollectionSerializer());
    kryo.addDefaultSerializer(TreeSet.class, new LegacyCollectionSerializer());
    kryo.addDefaultSerializer(HashMap.class, new LegacyMapSerializer());
    kryo.addDefaultSerializer(TreeMap.class, new LegacyMapSerializer());
    kryo.addDefaultSerializer(ConcurrentHashMap.class, new LegacyMapSerializer());

    // 4. DATY
    //kryo.register(LocalDate.class, new LegacyLocalDateSerializer());
    //kryo.register(LocalTime.class, new LegacyLocalTimeSerializer());
    //kryo.register(LocalDateTime.class, new LegacyLocalDateTimeSerializer());
    //kryo.register(Instant.class, new LegacyInstantSerializer());
    kryo.register(UUID.class, new LegacyUUIDSerializer());

    return kryo;
  }

  // Helper do bezpiecznego nadpisywania rejestracji
  private void registerLegacy(Kryo kryo, Class<?> type, Serializer<?> serializer, boolean references) {
    // Sprawdzamy, czy klasa jest już zarejestrowana (np. domyślne rejestracje Kryo)
    // Jeśli tak, pobieramy istniejącą rejestrację i podmieniamy serializer.
    // Jeśli nie, rejestrujemy nową.
    Registration registration = kryo.getRegistration(type);

    if (registration != null) {
      registration.setSerializer(serializer);
      // W Kryo 5 Registration nie ma setReferences(bool).
      // Konfigurację referencji (dla konkretnego typu) robi się zazwyczaj przez serializer
      // (jeśli serializer to obsługuje) lub globalnie.
      // Jednak, jeśli chcemy wyłączyć referencje dla konkretnej klasy,
      // możemy spróbować sztuczki z nowym Registration, ale w Kryo 5 to może być trudne bez podmiany ID.

      // W Kryo 5, aby wyłączyć referencje dla konkretnej klasy,
      // najlepiej użyć serializera, który ignoruje referencje (np. Immutable).
      // Niestety, standardowe serializery Kryo 5 (np. DefaultSerializers.IntSerializer)
      // są "immutable" z definicji, więc Kryo nie pisze dla nich referencji.

      // Nasz LegacyIntSerializer ma { setImmutable(true); }, co mówi Kryo: "nie pisz referencji".
      // Więc parametr 'references' w tej metodzie helpera jest de facto realizowany przez:
      // 1. setImmutable(true) wewnątrz serializera (dla false)
      // 2. Globalne kryo.setReferences(true) (dla true)

      // Dla pewności, jeśli serializer dziedziczy po Serializer, możemy ustawić:
      serializer.setImmutable(!references);
    } else {
      // Nowa rejestracja
      registration = kryo.register(type, serializer);
      serializer.setImmutable(!references);
    }
  }

  public String serialize(Object object) {
    if (object == null) return null;
    Kryo kryo = createKryoInstance();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Output output = new Output(outputStream);
    try {
      kryo.writeClassAndObject(output, object);
      output.flush();
      byte[] bytes = outputStream.toByteArray();
      return Base64.getEncoder().encodeToString(bytes);
    } finally {
      output.close();
    }
  }

  public Object deserialize(String base64Data) {
    if (base64Data == null) return null;
    Kryo kryo = createKryoInstance();
    byte[] bytes = Base64.getDecoder().decode(base64Data);
    ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
    Input input = new Input(inputStream);
    try {
      return kryo.readClassAndObject(input);
    } finally {
      input.close();
    }
  }
}
