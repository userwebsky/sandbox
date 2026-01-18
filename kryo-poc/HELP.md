# **Poradnik Migracji Kryo 4 (Java 8\) do Kryo 5 (Java 17\)**

Ten dokument opisuje kroki niezbędne do przeprowadzenia migracji aplikacji korzystającej z biblioteki Kryo w wersji 4.x (na Java 8\) do wersji 5.x (na Java 17), przy zachowaniu kompatybilności wstecznej dla zserializowanych danych.

## **1\. Główne różnice i problemy**

Podczas migracji napotkasz trzy główne kategorie problemów:

1. **Zmiana domyślnych ustawień:** Kryo 5 jest "secure by default" (wymaga rejestracji klas), podczas gdy Kryo 4 było bardzo permisywne.
2. **Zmiana formatu binarnego:**
  * **Prymitywy i Wrappery:** Kryo 4 używało Fixed Big Endian dla Float/Double oraz VarInt (z optimizePositive=true) dla Integer/Long. Kryo 5 może używać Little Endian lub innych optymalizacji.
  * **Tablice i Kolekcje:** Różnice w zapisie długości (nullability) oraz formacie elementów (ZigZag vs Fixed).
  * **String:** Kryo 4 dodawało prefiks 0x01 (SOH) dla stringów ASCII w niektórych kontekstach.
3. **Dostęp do pól (Java 17):** Domyślny FieldSerializer z Kryo 4 używa refleksji do pól prywatnych (setAccessible(true)), co w Java 17 jest blokowane przez system modułów dla klas JDK (np. java.time.\*, java.util.\*).

## **2\. Konfiguracja KryoService (Most Kompatybilności)**

Aby odczytać dane zapisane przez Kryo 4, musisz skonfigurować instancję Kryo 5 w specyficzny sposób.

### **2.1. Ustawienia globalne**

Kryo kryo \= new Kryo();

// 1\. Wyłącz wymóg rejestracji (Kryo 4 domyślnie pozwalało na niezarejestrowane klasy)  
kryo.setRegistrationRequired(false);

// 2\. Włącz referencje (Kryo 4 domyślnie obsługiwało grafy cykliczne)  
kryo.setReferences(true);

// 3\. Użyj StdInstantiatorStrategy (do tworzenia obiektów bez konstruktora bezargumentowego)  
kryo.setInstantiatorStrategy(new org.objenesis.strategy.StdInstantiatorStrategy());

### **2.2. Strategia deserializacji (Manual vs readClassAndObject)**

Kluczowym problemem jest obsługa nagłówka referencji (NOT\_NULL / RefID) dla obiektu głównego (korzenia).

* **Kryo 4:** Dla wielu typów (np. ArrayList niezarejestrowana) **nie zapisywało** nagłówka referencji dla korzenia.
* **Kryo 5 (readClassAndObject):** Zawsze oczekuje nagłówka referencji, jeśli klasa ma włączone referencje.

**Rozwiązanie:** Używaj kryo.readClassAndObject(input) **ALE** musisz wyłączyć referencje dla typów, które w V4 ich nie miały (wrappery, String) oraz upewnić się, że kolekcje/tablice są obsługiwane przez poprawione serializery.

## **3\. Implementacja Legacy Serializerów**

Musisz zaimplementować własne serializery, które emulują zachowanie Kryo 4\.

### **3.1. LegacyAdapters (Klucz do sukcesu)**

Stwórz klasę narzędziową z metodami do czytania/pisania w formacie V4.

* **Int/Long:** Używaj VarInt (zmienna długość).
* **Float/Double/Short/Char:** Używaj Fixed Big Endian.
* **ZigZag:** Pamiętaj, że elementy tablic int\[\]/long\[\] w V4 były zapisywane z optimizePositive=false (ZigZag).

### **3.2. Serializery dla Typów Prostych**

Zarejestruj je z setReferences(false), aby readClassAndObject nie szukało dla nich ID w strumieniu.

// W KryoService  
kryo.register(Integer.class, new LegacyIntSerializer()).setReferences(false);  
kryo.register(Long.class, new LegacyLongSerializer()).setReferences(false);  
kryo.register(Float.class, new LegacyAdapters.LegacyFloatSerializer()).setReferences(false);  
// ... i tak dalej dla Double, Boolean, Byte, Char, Short, String

### **3.3. Serializery dla Tablic**

Standardowe serializery tablic w V5 nie pasują do formatu V4 (długość \+ 1, ZigZag elementów).  
Napisz LegacyIntArraySerializer, LegacyStringArraySerializer itd., które używają LegacyAdapters.readVarIntLegacy.  
Ważne: Używaj input.readByte() w pętlach, aby poprawnie aktualizować pozycję bufora\!

### **3.4. Serializery dla Kolekcji i Map**

Standardowe serializery V5 mogą rzucać ArrayIndexOutOfBounds przy czytaniu rozmiaru zapisanego jako VarInt.  
Użyj LegacyCollectionSerializer i LegacyMapSerializer, które czytają rozmiar przez LegacyAdapters.

### **3.5. Serializery dla Dat (Java 8 Time)**

Kryo 4 używało FieldSerializer do LocalDate, Instant itd.  
W Java 17 FieldSerializer rzuci InaccessibleObjectException przy próbie dostępu do prywatnych pól (np. year).  
Rozwiązanie: Napisz serializery (np. LegacyLocalDateSerializer), które czytają bajty w kolejności alfabetycznej pól (jak robił to FieldSerializer), ale tworzą obiekt przez publiczne API (np. LocalDate.of()).  
**Format pól w V4:**

* int \-\> VarInt (optimizePositive=true).
* long \-\> VarLong (optimizePositive=false \- ZigZag\!).
* short/byte \-\> Fixed.

## **4\. Rejestracja Klas i ID (Krytyczne dla TreeSet/TreeMap)**

Kryo 4 miało wbudowane domyślne rejestracje z konkretnymi ID, np.:

* int\[\] \-\> ID 10
* String\[\] \-\> ID 11
* TreeSet \-\> ID 36
* TreeMap \-\> ID 38

Kryo 5 ma inną kolejność. Jeśli nie wymusisz tych ID, Kryo 5 spróbuje odczytać dane TreeSet (ID 36\) używając serializera przypisanego do ID 36 w V5 (którym może być cokolwiek), co skończy się błędem lub pustą kolekcją.

**Wymagana konfiguracja w KryoService:**

// Wymuś ID zgodne z Kryo 4  
kryo.register(int\[\].class, new LegacyIntArraySerializer(), 10);  
kryo.register(String\[\].class, new LegacyStringArraySerializer(), 11);  
// ...  
kryo.register(TreeSet.class, new LegacyCollectionSerializer(), 36);  
kryo.register(TreeMap.class, new LegacyMapSerializer(), 38);

## **5\. Podsumowanie listy kontrolnej**

1. \[ \] Ustaw registrationRequired \= false i references \= true.
2. \[ \] Zaimplementuj LegacyAdapters (VarInt, BigEndian).
3. \[ \] Zaimplementuj LegacyIntSerializer / LegacyLongSerializer (obsługa ZigZag).
4. \[ \] Zaimplementuj LegacyStringSerializer (obsługa SOH 0x01).
5. \[ \] Zaimplementuj serializery dla tablic (LegacyIntArraySerializer...).
6. \[ \] Zaimplementuj serializery dla dat (LegacyLocalDateSerializer...) omijające refleksję.
7. \[ \] W KryoService: Zarejestruj wrappery i String z setReferences(false).
8. \[ \] W KryoService: Zarejestruj kolekcje/mapy/tablice z odpowiednimi ID z Kryo 4\.
9. \[ \] Używaj kryo.readClassAndObject(input) do deserializacji.

Powodzenia w migracji\!
