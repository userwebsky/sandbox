package org.example.kryopoc.migrationtest;

import org.example.kryopoc.services.KryoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class KryoMigrationTest {

  @Autowired
  private KryoService kryoService;

  // Dostarcza dane (te same co w Etapie 1, aby zweryfikować poprawność odczytu)
  static Stream<Arguments> provideTestObjects() {
    return Stream.of(
      // --- 1. Prymitywy ---
      Arguments.of("primitive_int", 42),
      Arguments.of("primitive_long", 999999999L),
      Arguments.of("primitive_double", 123.456),
      Arguments.of("primitive_float", 78.9f),
      Arguments.of("primitive_boolean", true),
      Arguments.of("primitive_char", 'K'),
      Arguments.of("primitive_byte", (byte) 127),
      Arguments.of("primitive_short", (short) 32000),

      // --- 2. Wrappery ---
      Arguments.of("wrapper_integer", Integer.valueOf(100)),
      Arguments.of("wrapper_double", Double.valueOf(55.55)),
      Arguments.of("wrapper_boolean", Boolean.FALSE),

      // --- 3. Tekst ---
      Arguments.of("string_basic", "Hello World"),
      Arguments.of("string_polish", "Zażółć gęślą jaźń"),
      Arguments.of("string_empty", ""),

      // --- 4. Tablice ---
      Arguments.of("array_int", new int[]{1, 2, 3, 4, 5}),
      Arguments.of("array_string", new String[]{"A", "B", "C"}),
      Arguments.of("array_mixed_objects", new Object[]{1, "Text", 2.5}),

      // --- 5. Kolekcje ---
      Arguments.of("collection_arraylist_string", new ArrayList<>(Arrays.asList("ListElement1", "ListElement2"))),
      Arguments.of("collection_linkedlist_double", new LinkedList<>(Arrays.asList(1.1, 2.2))),
      Arguments.of("collection_hashset", new HashSet<>(Arrays.asList("Set1", "Set2"))),
      Arguments.of("collection_treeset", new TreeSet<>(Arrays.asList(1, 5))),

      // --- 6. Mapy ---
      Arguments.of("map_hashmap", createHashMap()),
      Arguments.of("map_treemap", createTreeMap()),
      Arguments.of("map_concurrent", createConcurrentMap()),

      // --- 7. Numeryczne Big ---
      Arguments.of("big_decimal", new BigDecimal("12345.6789")),
      Arguments.of("big_integer", new BigInteger("98765432109876543210")),

      // --- 8. Daty ---
      Arguments.of("date_legacy_java_util", new Date(1700000000000L)),
      Arguments.of("date_sql_timestamp", new java.sql.Timestamp(1700000000000L)),
      Arguments.of("date_local_date", LocalDate.of(2023, 11, 14)),
      Arguments.of("date_local_date_time", LocalDateTime.of(2023, 11, 14, 12, 30, 45)),
      Arguments.of("date_instant", Instant.ofEpochMilli(1700000000000L)),
      Arguments.of("date_uuid", UUID.fromString("550e8400-e29b-41d4-a716-446655440000")),

      // --- 9. Enum ---
      Arguments.of("enum_value", ComplexTestObject.TestEnum.VALUE_TWO),

      // --- 10. Nested Object ---
      Arguments.of("nested_object", new ComplexTestObject.NestedObject("Nested", 99))
    );
  }

  // Metody pomocnicze (identyczne jak w Etapie 1)
  private static HashMap<String, String> createHashMap() {
    HashMap<String, String> map = new HashMap<>();
    map.put("Key1", "Val1");
    map.put("Key2", "Val2");
    return map;
  }
  private static TreeMap<Integer, String> createTreeMap() {
    TreeMap<Integer, String> map = new TreeMap<>();
    map.put(1, "One");
    map.put(10, "Ten");
    return map;
  }
  private static ConcurrentHashMap<String, Integer> createConcurrentMap() {
    ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
    map.put("Concurrent", 100);
    return map;
  }

  // --- ETAP 1 (WYŁĄCZONY) ---
  // Nie chcemy nadpisywać plików wzorcowych nowym Kryo!
  @Disabled("Wyłączone w Etapie 2 - nie nadpisujemy plików wzorcowych")
  @ParameterizedTest
  @MethodSource("provideTestObjects")
  @Order(1)
  void testSerializationAndDumpToFile(String testName, Object data) {
    // ... (kod wyłączony)
  }

  // --- ETAP 2 (URUCHAMIAMY) ---
  // Próbujemy czytać pliki z Etapu 1 nową wersją Kryo i Javy
  @ParameterizedTest(name = "Próba deserializacji Legacy: {0}")
  @MethodSource("provideTestObjects")
  @Order(2)
  void testDeserializationVerification(String testName, Object expectedData) throws IOException {
    String fileName = "kryo_v4_" + testName + ".b64";
    Path path = Paths.get(fileName);

    if (!Files.exists(path)) {
      // Jeśli pliku nie ma, to znaczy że Etap 1 nie został poprawnie zakończony
      Assertions.fail("Brak pliku wzorcowego: " + fileName);
    }

    String readData = Files.lines(path).collect(Collectors.joining());

    System.out.println("Próba deserializacji: " + testName);

    try {
      Object actualData = kryoService.deserialize(readData);

      // Jeśli deserializacja się udała (mało prawdopodobne przy default config), sprawdzamy dane
      Assertions.assertNotNull(actualData);

      // Logika porównania
      if (expectedData.getClass().isArray()) {
        if (expectedData instanceof int[]) Assertions.assertArrayEquals((int[]) expectedData, (int[]) actualData);
        else if (expectedData instanceof long[]) Assertions.assertArrayEquals((long[]) expectedData, (long[]) actualData);
        else if (expectedData instanceof double[]) Assertions.assertArrayEquals((double[]) expectedData, (double[]) actualData);
        else if (expectedData instanceof float[]) Assertions.assertArrayEquals((float[]) expectedData, (float[]) actualData);
        else if (expectedData instanceof byte[]) Assertions.assertArrayEquals((byte[]) expectedData, (byte[]) actualData);
        else if (expectedData instanceof char[]) Assertions.assertArrayEquals((char[]) expectedData, (char[]) actualData);
        else if (expectedData instanceof boolean[]) Assertions.assertArrayEquals((boolean[]) expectedData, (boolean[]) actualData);
        else if (expectedData instanceof short[]) Assertions.assertArrayEquals((short[]) expectedData, (short[]) actualData);
        else Assertions.assertArrayEquals((Object[]) expectedData, (Object[]) actualData);
      } else {
        Assertions.assertEquals(expectedData, actualData, "Błąd danych dla: " + testName);
      }

      if (actualData instanceof ComplexTestObject) {
        ComplexTestObject obj = (ComplexTestObject) actualData;
        Assertions.assertSame(obj, obj.getSelfReference());
      }

      System.out.println("SUKCES (Niespodziewany!): " + testName);

    } catch (Exception e) {
      // W Etapie 2 SPODZIEWAMY SIĘ BŁĘDÓW.
      // Wypisujemy je, abyś mógł je przeanalizować.
      System.err.println("OCZEKIWANY BŁĄD dla " + testName + ": " + e.getClass().getSimpleName() + " -> " + e.getMessage());
      // Rzucamy dalej, aby test zaświecił się na czerwono (chyba że wolisz, żeby przeszedł, jeśli złapie błąd?)
      // Skoro to POC, niech testy failują, żebyśmy widzieli co trzeba naprawić w Etapie 3.
      throw e;
    }
  }
}
