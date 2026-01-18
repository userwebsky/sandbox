package org.example.kryopoc.services;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.example.kryopoc.serializers.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class KryoService {

  private Kryo createKryoInstance() {
    Kryo kryo = new Kryo();

    kryo.setRegistrationRequired(false);
    kryo.setReferences(true); // Globalnie włączone
    kryo.setInstantiatorStrategy(new org.objenesis.strategy.StdInstantiatorStrategy());

    // --- KONFIGURACJA DLA KRYO 4 ---

    // 1. WRAPPERY (BEZ REFERENCJI, ZigZag dla Int/Long, FixedBE dla reszty)
    kryo.register(Integer.class, new LegacyIntSerializer());
    kryo.register(Long.class, new LegacyLongSerializer());
    kryo.register(Float.class, new LegacyAdapters.LegacyFloatSerializer());
    kryo.register(Double.class, new LegacyAdapters.LegacyDoubleSerializer());
    kryo.register(Boolean.class, new LegacyAdapters.LegacyBooleanSerializer());
    kryo.register(Byte.class, new LegacyAdapters.LegacyByteSerializer()); // Trzeba dodać klasę ByteSerializer do LegacyAdapters
    kryo.register(Character.class, new LegacyAdapters.LegacyCharSerializer());
    kryo.register(Short.class, new LegacyAdapters.LegacyShortSerializer());

    // String (BEZ REFERENCJI)
    kryo.register(String.class, new LegacyStringSerializer());

    // Prymitywy (int.class itp.) - tutaj referencje nie mają znaczenia (nie są obiektami),
    // ale rejestrujemy serializer dla "Top Level" testów.
    kryo.register(int.class, new LegacyIntSerializer());
    kryo.register(long.class, new LegacyLongSerializer());

    // 2. TABLICE (MAJĄ REFERENCJE - domyślnie)
    kryo.register(int[].class, new LegacyIntArraySerializer());
    kryo.register(String[].class, new LegacyStringArraySerializer());
    // ... Object[] ...

    // 3. KOLEKCJE i MAPY (MAJĄ REFERENCJE)
    kryo.register(ArrayList.class, new LegacyCollectionSerializer());
    kryo.register(LinkedList.class, new LegacyCollectionSerializer());
    kryo.register(HashSet.class, new LegacyCollectionSerializer());
    kryo.register(TreeSet.class, new LegacyCollectionSerializer());
    kryo.register(HashMap.class, new LegacyMapSerializer());
    kryo.register(TreeMap.class, new LegacyMapSerializer());
    kryo.register(ConcurrentHashMap.class, new LegacyMapSerializer());

    // 4. DATY (MAJĄ REFERENCJE, ale można wyłączyć dla małych obiektów,
    // jednak w V4 często miały, więc zostawmy domyślne true)
    kryo.register(LocalDate.class, new LegacyLocalDateSerializer());
    kryo.register(LocalTime.class, new LegacyLocalTimeSerializer());
    kryo.register(LocalDateTime.class, new LegacyLocalDateTimeSerializer());
    kryo.register(Instant.class, new LegacyInstantSerializer());
    kryo.register(UUID.class, new LegacyUUIDSerializer());

    return kryo;
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
      // Używamy readClassAndObject - to obsłuży ReferenceID dla Tablic/Kolekcji,
      // ale pominie je dla Wrapperów/Stringów dzięki .setReferences(false)
      return kryo.readClassAndObject(input);
    } finally {
      input.close();
    }
  }
}
