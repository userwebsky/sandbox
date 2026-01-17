package org.example.kryopoc.migrationtest;

import org.example.kryopoc.services.KryoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
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

  // Dostarcza WSZYSTKIE typy danych z ComplexTestObject jako osobne przypadki testowe
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
      // Wrapper Boolean.FALSE jest specyficzny w Kryo (czasem zapisywany jako stała)
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

      // --- 6. Mapy (POPRAWKA: Używamy metod pomocniczych zamiast {{ }} double brace init) ---
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
      Arguments.of("nested_object", new ComplexTestObject.NestedObject("Nested", 99)),

      // --- 11. Pełny, Złożony Obiekt (Integration Test) ---
      Arguments.of("complex_object_full", ComplexTestObject.createFullObject())
    );
  }

  // --- Metody pomocnicze do tworzenia Map (unikamy anonimowych podklas) ---
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

  @ParameterizedTest(name = "Serializacja: {0}")
  @MethodSource("provideTestObjects")
  @Order(1)
  void testSerializationAndDumpToFile(String testName, Object data) throws IOException {
    System.out.println("--- ZAPIS: " + testName + " ---");

    String serializedData = kryoService.serialize(data);
    Assertions.assertNotNull(serializedData);

    String fileName = "kryo_v4_" + testName + ".b64";
    Path path = Paths.get(fileName);
    Files.write(path, serializedData.getBytes());

    System.out.println("Utworzono plik: " + fileName + " (" + serializedData.length() + " znaków)");
  }

  @ParameterizedTest(name = "Weryfikacja: {0}")
  @MethodSource("provideTestObjects")
  @Order(2)
  void testDeserializationVerification(String testName, Object expectedData) throws IOException {
    String fileName = "kryo_v4_" + testName + ".b64";
    Path path = Paths.get(fileName);

    if (!Files.exists(path)) {
      Assertions.fail("Brak pliku: " + fileName);
    }

    String readData = Files.lines(path).collect(Collectors.joining());
    Object actualData = kryoService.deserialize(readData);

    Assertions.assertNotNull(actualData, "Wynik deserializacji jest null: " + testName);

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

    // Specjalne sprawdzenie dla cyklu w complex_object_full
    if (actualData instanceof ComplexTestObject) {
      ComplexTestObject obj = (ComplexTestObject) actualData;
      Assertions.assertSame(obj, obj.getSelfReference(), "Utracono cykliczną referencję w " + testName);
    }

    System.out.println("ODCZYT OK: " + testName);
  }
}
