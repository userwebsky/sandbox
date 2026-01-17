package org.example.kryopoc.migrationtest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
// WAŻNE: Wykluczamy selfReference z equals/hashCode/toString, aby uniknąć StackOverflowError
// przy cyklicznych referencjach podczas asercji w testach.
@EqualsAndHashCode(exclude = "selfReference")
@ToString(exclude = "selfReference")
public class ComplexTestObject implements Serializable {

  // 1. Prymitywy
  private int primitiveInt;
  private long primitiveLong;
  private double primitiveDouble;
  private float primitiveFloat;
  private boolean primitiveBoolean;
  private char primitiveChar;
  private byte primitiveByte;
  private short primitiveShort;

  // 2. Wrappery
  private Integer wrapperInt;
  private Long wrapperLong;
  private Double wrapperDouble;
  private Boolean wrapperBoolean;

  // 3. Tekst
  private String text;
  private String emptyText;
  private String nullText;

  // 4. Tablice
  private int[] intArray;
  private String[] stringArray;
  private Object[] objectArray;

  // 5. Kolekcje standardowe
  private List<String> stringList;
  private ArrayList<Integer> arrayListInt;
  private LinkedList<Double> linkedListDouble;
  private Set<String> hashSet;
  private TreeSet<Integer> treeSet;

  // 6. Mapy
  private Map<String, String> hashMap;
  private TreeMap<Integer, String> treeMap;
  private ConcurrentHashMap<String, Integer> concurrentMap;

  // 7. Typy numeryczne precyzyjne
  private BigDecimal bigDecimal;
  private BigInteger bigInteger;

  // 8. Daty
  private Date legacyDate;
  private java.sql.Timestamp sqlTimestamp;
  private LocalDate localDate;
  private LocalDateTime localDateTime;
  private Instant instant;
  private UUID uuid;

  // 9. Enum
  private TestEnum enumValue;

  // 10. Obiekt zagnieżdżony
  private NestedObject nestedObject;

  // 11. Cykliczna referencja
  private ComplexTestObject selfReference;

  public enum TestEnum {
    VALUE_ONE, VALUE_TWO, VALUE_THREE
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode
  public static class NestedObject implements Serializable {
    private String name;
    private int value;
  }

  public static ComplexTestObject createFullObject() {
    ComplexTestObject obj = new ComplexTestObject();

    // 1. Prymitywy
    obj.setPrimitiveInt(42);
    obj.setPrimitiveLong(999999999L);
    obj.setPrimitiveDouble(123.456);
    obj.setPrimitiveFloat(78.9f);
    obj.setPrimitiveBoolean(true);
    obj.setPrimitiveChar('K');
    obj.setPrimitiveByte((byte) 127);
    obj.setPrimitiveShort((short) 32000);

    // 2. Wrappery
    obj.setWrapperInt(100);
    obj.setWrapperLong(null);
    obj.setWrapperDouble(55.55);
    obj.setWrapperBoolean(Boolean.FALSE);

    // 3. Tekst
    obj.setText("Zażółć gęślą jaźń");
    obj.setEmptyText("");
    obj.setNullText(null);

    // 4. Tablice
    obj.setIntArray(new int[]{1, 2, 3, 4, 5});
    obj.setStringArray(new String[]{"A", "B", "C"});
    obj.setObjectArray(new Object[]{1, "Text", 2.5});

    // 5. Kolekcje - WAŻNE: Używamy konkretnych implementacji (new ArrayList),
    // a nie Arrays.asList(), aby uniknąć problemów z deserializacją wewnętrznych klas Arrays$ArrayList
    obj.setStringList(new ArrayList<>(Arrays.asList("ListElement1", "ListElement2")));
    obj.setArrayListInt(new ArrayList<>(Arrays.asList(10, 20, 30)));
    obj.setLinkedListDouble(new LinkedList<>(Arrays.asList(1.1, 2.2)));

    Set<String> set = new HashSet<>();
    set.add("Set1");
    set.add("Set2");
    obj.setHashSet(set);

    TreeSet<Integer> tSet = new TreeSet<>();
    tSet.add(5);
    tSet.add(1);
    obj.setTreeSet(tSet);

    // 6. Mapy
    Map<String, String> map = new HashMap<>();
    map.put("Key1", "Val1");
    map.put("Key2", "Val2");
    obj.setHashMap(map);

    TreeMap<Integer, String> tMap = new TreeMap<>();
    tMap.put(10, "Ten");
    tMap.put(1, "One");
    obj.setTreeMap(tMap);

    ConcurrentHashMap<String, Integer> cMap = new ConcurrentHashMap<>();
    cMap.put("Concurrent", 100);
    obj.setConcurrentMap(cMap);

    // 7. Numeryczne
    obj.setBigDecimal(new BigDecimal("12345.6789"));
    obj.setBigInteger(new BigInteger("98765432109876543210"));

    // 8. Daty
    // Ustawiamy stałe daty dla powtarzalności testów binarnych
    obj.setLegacyDate(new Date(1700000000000L));
    obj.setSqlTimestamp(new java.sql.Timestamp(1700000000000L));
    obj.setLocalDate(LocalDate.of(2023, 11, 14));
    obj.setLocalDateTime(LocalDateTime.of(2023, 11, 14, 12, 30, 45));
    obj.setInstant(Instant.ofEpochMilli(1700000000000L));
    obj.setUuid(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));

    // 9. Enum
    obj.setEnumValue(TestEnum.VALUE_TWO);

    // 10. Nested
    obj.setNestedObject(new NestedObject("Nested", 99));

    // 11. Self reference
    obj.setSelfReference(obj);

    return obj;
  }
}
