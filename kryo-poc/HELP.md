# **Migracja Kryo 4.x (Java 8\) do Kryo 5.x (Java 17\)**

## **Wprowadzenie**

Ten dokument opisuje proces migracji aplikacji korzystającej z biblioteki serializacji Kryo w wersji 4.x (uruchomionej na Java 8\) do wersji 5.x (uruchomionej na Java 17). Celem jest zachowanie pełnej kompatybilności wstecznej, czyli umożliwienie odczytu danych (blobów) zapisanych przez starą wersję aplikacji w nowej wersji.

Migracja ta jest nietrywialna z powodu licznych zmian w formacie binarnym Kryo, zmian w domyślnej konfiguracji oraz restrykcji wprowadzonych w nowszych wersjach Javy (Project Jigsaw).

## **1\. Główne Zmiany i Problemy**

### **1.1. Zmiany w Kryo 5**

* **Bezpieczeństwo (registrationRequired):** W Kryo 4 domyślnie można było serializować każdą klasę. W Kryo 5 domyślnie wymagana jest rejestracja każdej klasy (registrationRequired \= true), co powoduje błędy IllegalArgumentException: Class is not registered przy próbie odczytu starych danych.
* **Format Liczb (VarInt vs Fixed):**
  * W Kryo 4 (domyślnie) typy proste w polach obiektów (int, long) były zapisywane jako **VarInt** (zmienna długość) z optymalizacją dla liczb dodatnich (optimizePositive=true).
  * Wrappery (Integer, Long) oraz elementy tablic (int\[\], long\[\]) były zapisywane jako **VarInt z ZigZag encoding** (optimizePositive=false).
  * Kryo 5 w wielu miejscach zmieniło te domyślne zachowania, co prowadzi do błędów KryoBufferUnderflowException lub odczytu błędnych wartości (np. 1999999998 zamiast 999999999).
* **Format Stringów:** Kryo 4 dla stringów ASCII potrafiło dodawać prefiks 0x01 (SOH). Kryo 5 czyta to jako część stringa, co powoduje "śmieci" na początku tekstu (np. \\u0001Hello).
* **Obsługa Referencji:** Sposób zapisu nagłówka referencji dla głównego obiektu (korzenia) różni się. W Kryo 4 korzeń często nie miał ID referencji, podczas gdy w Kryo 5 metoda readClassAndObject zawsze go oczekuje.
* **Kolejność ID Klas:** Kryo 4 miało wbudowane domyślne rejestracje z konkretnymi ID (np. TreeSet \= 36). Kryo 5 ma inną kolejność, co powoduje, że odczyt po ID z pliku V4 prowadzi do użycia niewłaściwego serializera.

### **1.2. Zmiany w Java 17**

* **Enkapsulacja (Jigsaw):** Domyślny FieldSerializer w Kryo 4 używa refleksji (setAccessible(true)) do prywatnych pól. W Java 17 jest to zablokowane dla klas z pakietów JDK, co kończy się InaccessibleObjectException.

## **2\. Rozwiązanie \- Warstwa Kompatybilności**

Aby obsłużyć stary format danych, został stworzony zestaw klas i konfiguracji.

### **2.1. Konfiguracja KryoService**

Kryo kryo \= new Kryo();

// 1\. Przywróć zachowanie V4: pozwól na niezarejestrowane klasy  
kryo.setRegistrationRequired(false);

// 2\. Włącz referencje (domyślne w V4)  
kryo.setReferences(true);

// 3\. Strategia instancjonowania (dla klas bez pustego konstruktora)  
kryo.setInstantiatorStrategy(new org.objenesis.strategy.StdInstantiatorStrategy());

### **2.2. Wymuszenie ID klas**

Musimy zarejestrować kluczowe typy pod tymi samymi ID, co w Kryo 4, aby deserializacja po ID działała poprawnie.

// Tablice  
kryo.register(int\[\].class, new LegacyIntArraySerializer(), 10);  
kryo.register(String\[\].class, new LegacyStringArraySerializer(), 11);  
// ...

// Kolekcje specjalne  
kryo.register(TreeSet.class, new LegacyTreeSetSerializer(), 36);  
kryo.register(TreeMap.class, new LegacyTreeMapSerializer(), 38);

### **2.3. Wyłączenie referencji dla typów prostych**

W Kryo 4 wrappery i Stringi (jako obiekty niemutowalne) nie były zapisywane z nagłówkiem referencji. Musimy o tym poinformować Kryo 5\.

kryo.register(Integer.class, new LegacyIntSerializer()).setReferences(false);  
kryo.register(String.class, new LegacyStringSerializer()).setReferences(false);  
// ... i tak dalej dla wszystkich wrapperów

### **2.4. Strategia Deserializacji**

Używamy metody kryo.readClassAndObject(input), która potrafi obsłużyć nagłówek referencji (jeśli występuje) oraz jego brak (dla typów z setReferences(false)).

## **3\. Implementacja Serializerów (Kod)**

Stworzyliśmy zestaw klas ...Serializer w pakiecie serializers.

### **3.1. KryOldAdapters**

Klasa narzędziowa zawierająca logikę czytania/pisania w starym formacie.

* **VarInt Legacy:** Obsługuje zmienną długość (1-5 bajtów) z opcjonalnym kodowaniem ZigZag. Kluczowe jest użycie input.readByte(), aby poprawnie przesuwać kursor w strumieniu.
* **Big Endian Fixed:** Obsługuje zapis stałobajtowy (4 bajty dla int/float, 8 dla long/double) w kolejności Big Endian (Kryo 5 często używa Little Endian).

### **3.2. Przykłady Serializerów**

* **IntSerializer:** Dla wrapperów Integer. Wymusza ZigZag (optimizePositive=false).
* **IntArraySerializer:** Czyta długość jako VarInt(true), a elementy jako ZigZag (VarInt(false)).
* **StringSerializer:** Wykrywa i pomija bajt 0x01 (SOH) na początku stringa.
* **TreeSetSerializer / TreeMapSerializer:** Kluczowa różnica względem standardowych kolekcji – te serializery w V4 najpierw zapisywały **Comparator**, a dopiero potem dane. Nasze serializery muszą go najpierw odczytać.
* **LocalDateSerializer (i inne daty):** Zamiast używać FieldSerializer (refleksja na java.time w Java 17 \= błąd), czytamy bajty ręcznie (zgodnie z układem pól w klasie) i tworzymy obiekt przez LocalDate.of(). Używamy helperów z Adapters.

## **4\. Słowniczek Pojęć i Wprowadzenie do Operacji Bitowych**

### **Pojęcia**

* **Serializacja:** Proces zamiany obiektu w pamięci na ciąg bajtów (aby zapisać go w pliku lub wysłać przez sieć).
* **Deserializacja:** Proces odwrotny – odtworzenie obiektu z bajtów.
* **VarInt (Variable Length Integer):** Sposób zapisu liczby całkowitej używający od 1 do 5 bajtów. Mniejsze liczby zajmują mniej miejsca. Najstarszy bit każdego bajtu mówi, czy to już koniec liczby.
* **ZigZag Encoding:** Sposób kodowania liczb ujemnych w VarInt. Liczby ujemne (np. \-1) w standardowym kodowaniu są bardzo duże (dużo jedynek w U2), więc zajmowałyby 5 bajtów. ZigZag mapuje: 0-\>0, \-1-\>1, 1-\>2, \-2-\>3 itd., dzięki czemu małe liczby ujemne też zajmują mało miejsca.
* **Endianness (Big Endian vs Little Endian):** Kolejność bajtów w liczbie wielobajtowej.
  * **Big Endian (BE):** "Najważniejszy" bajt (najstarszy) jest pierwszy. Jak w normalnym zapisie liczby (tysiące, setki, dziesiątki...). To format sieciowy i domyślny w Javie (DataOutputStream).
  * **Little Endian (LE):** "Najmniej ważny" bajt jest pierwszy. Format natywny procesorów x86. Kryo 5 często go używa dla wydajności.

### **Operacje Bitowe w Java**

W naszych serializerach (np. LegacyAdapters) używamy operatorów bitowych do "składania" bajtów w liczby.

* & 0xFF (AND): W Javie byte jest ze znakiem (-128 do 127). Jeśli mamy bajt 11111111, Java widzi to jako \-1. Operacja & 0xFF (czyli & 00000000 00000000 00000000 11111111\) zamienia to na int o wartości 255 (bez znaku).
  * Przykład: input.readByte() & 0xFF pozwala traktować bajt jako liczbę 0-255.
* \<\< (Przesunięcie w lewo): Mnoży liczbę przez 2^n. Służy do przesuwania bajtu na właściwą pozycję.
  * Przykład przy czytaniu int (Big Endian):
    1. Czytamy 1\. bajt. Przesuwamy go o 24 bity w lewo (\<\< 24).
    2. Czytamy 2\. bajt. Przesuwamy o 16 bitów.
    3. Czytamy 3\. bajt. Przesuwamy o 8 bitów.
    4. Czytamy 4\. bajt. Nie przesuwamy.
* | (OR): Sumuje bity. Używamy go do sklejenia przesuniętych bajtów w jedną całość.
  * wynik \= (b1 \<\< 24\) | (b2 \<\< 16\) | ...
* \>\>\> (Przesunięcie w prawo bez znaku): Służy do "wyciągnięcia" bajtów z liczby przy zapisie. value \>\>\> 24 przesuwa bity w prawo, tak że najstarsze 8 bitów ląduje na pozycji najmłodszych (gotowe do zapisu jako byte).

## **5\. Podsumowanie Wymaganych Plików**

Do poprawnego działania w projekcie muszą znaleźć się:

1. **LegacyAdapters.java** \- Narzędzia IO.
2. **LegacyIntSerializer.java** \- Obsługa Integer i VarInt.
3. **LegacyLongSerializer.java** \- Obsługa Long i VarLong.
4. **LegacyStringSerializer.java** \- Obsługa String (SOH).
5. **LegacyIntArraySerializer.java** (i inne tablice) \- Obsługa tablic.
6. **LegacyCollectionSerializer.java** \- Obsługa standardowych kolekcji.
7. **LegacyMapSerializer.java** \- Obsługa standardowych map.
8. **LegacyTreeSetSerializer.java** \- Specjalna obsługa TreeSet (komparator).
9. **LegacyTreeMapSerializer.java** \- Specjalna obsługa TreeMap (komparator).
10. **LegacyLocalDateSerializer.java** (i inne daty) \- Obsługa Java Time API.
11. **KryoService.java** \- Spinająca to wszystko konfiguracja z rejestracją ID i metodą deserialize używającą readClassAndObject.
