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
    kryo.setReferences(true);
    kryo.setInstantiatorStrategy(new org.objenesis.strategy.StdInstantiatorStrategy());

    // --- KONFIGURACJA DLA KRYO 4 ---

    // 1. PRYMITYWY i WRAPPERY
    // Int: VarInt (optimizePositive=true). Long: VarLong (ZigZag).
    // Wyłączamy referencje dla wrapperów, aby readClassAndObject nie szukało RefID w danych.
    kryo.register(int.class, new LegacyIntSerializer());
    kryo.register(Integer.class, new LegacyIntSerializer()); // Domyślnie immutable=true w serializerze

    kryo.register(long.class, new LegacyLongSerializer());
    kryo.register(Long.class, new LegacyLongSerializer());

    // Float/Double/Short/Char/Boolean - Fixed Big Endian
    kryo.register(Double.class, new LegacyAdapters.LegacyDoubleSerializer());
    kryo.register(Float.class, new LegacyAdapters.LegacyFloatSerializer());
    kryo.register(Short.class, new LegacyAdapters.LegacyShortSerializer());
    kryo.register(Character.class, new LegacyAdapters.LegacyCharSerializer());
    kryo.register(Boolean.class, new LegacyAdapters.LegacyBooleanSerializer());

    // 2. TABLICE (Naprawiają błąd "Expected 5 Actual 0")
    // Twoja implementacja LegacyIntArraySerializer jest poprawna dla Kryo 4 (length+1, VarInt elements).
    kryo.register(int[].class, new LegacyIntArraySerializer());
    kryo.register(String[].class, new LegacyStringArraySerializer());
    // kryo.register(Object[].class, new LegacyObjectArraySerializer()); // Warto dodać dla array_mixed_objects

    // 3. STRING
    kryo.register(String.class, new LegacyStringSerializer());

    // 4. KOLEKCJE i MAPY
    kryo.register(ArrayList.class, new LegacyCollectionSerializer());
    kryo.register(LinkedList.class, new LegacyCollectionSerializer());
    kryo.register(HashSet.class, new LegacyCollectionSerializer());
    kryo.register(TreeSet.class, new LegacyCollectionSerializer());

    kryo.register(HashMap.class, new LegacyMapSerializer());
    kryo.register(TreeMap.class, new LegacyMapSerializer());
    kryo.register(ConcurrentHashMap.class, new LegacyMapSerializer());

    // 5. DATY (Pola wewnętrzne muszą używać odpowiednio LegacyIntSerializer/LegacyLongSerializer)
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
      // WRACAMY DO STANDARDOWEGO ODCZYTU
      // Dzięki temu Kryo obsłuży ReferenceID dla tablic/kolekcji.
      // Dla Wrapperów/Stringów (immutable) ReferenceID nie ma, ale Kryo 5 o tym wie (z konfiguracji serializerów).
      return kryo.readClassAndObject(input);
    } finally {
      input.close();
    }
  }
}
