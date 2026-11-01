# Hafta 4 Mini Sınav — Gün 24

Başlangıç saati:
Bitiş saati:

## Bölüm A — Teori (her biri 1–3 cümle) · 25 puan

1. Her tarif için Java'nın hazır functional interface'lerinden hangisi? (`Predicate` / `Function` / `Consumer` / `Supplier`)
   (a) `String` alır, `boolean` döner
   (b) `String` alır, `Integer` döner
   (c) hiçbir şey almaz, `LocalDate` döner
   (d) `String` alır, hiçbir şey döndürmez
Cevap:a) predicate b) consumer c) supplier d) function

2. Bu kod neden derlenmez? Toplamı almak için doğru stream satırı ne olurdu?
```java
List<Integer> nums = List.of(1, 2, 3);
int total = 0;
nums.forEach(n -> total += n);
```
Cevap:List.of immutable olduğu için derlenmez. nums.stream().mapToInt(n -> n).sum();

3. Ara işlem (intermediate) ile bitiş işlemi (terminal) farkı nedir? Her birinden 2 örnek ver. Aşağıdaki son satırda ne olur?
```java
Stream<String> s = List.of("a", "bb", "ccc").stream().filter(x -> x.length() > 1);
System.out.println(s.count());
System.out.println(s.count());
```
Cevap:ara işlem listedeki elemanlar üzerinde yapılan işlemleri dönüşümleri sıralamaları içerir, bitiş işlemi ise son değeri döndürecek listeye çevirme, toplamı bulma gruplama gibi işlemleri içerir.
ikinci println'de IllegalStateException fırlatılır çünkü bir stream sadece tek bir kere kullanılabilir.

4. Bir metot neden `null` yerine `Optional<T>` döndürür? `orElse(...)` ile `orElseThrow()` arasında hangisini ne zaman seçersin?
Cevap: bir metodun ne döneceğinden tam emin değilsek, null da dönebilir bir değer de dönebilir diyorsak Optional kullanırız. orElse(...)'i eğer istediğimiz değer dönmeyecekse onun yerine dönecek bir değer vermek için, orElseThrow()'u da istediğimiz değer dönmeyecekse hata fırlatması için kullanırız.

5. `record Point(int x, int y) {}` yazınca Java senin yerine neleri üretir (en az 4 şey)? Oluştuktan sonra `x`'i değiştirebilir misin? Compact constructor ne için kullanılır?
Cevap: benim yerime ne üretir hatırlamıyorum. oluşturulduktan sonra x değiştirilemez. eğer değiştirilmek isteniyorsa yeni bir record üretilir. compact constructor hata kontrolü için kullanılır.

6. Kategori için `String` yerine `enum` kullanmanın faydası ne? `Category.valueOf("food")` (enum'da `FOOD` var) ne olur?
Cevap: IllegalArgumentException olur. enum kullanmak kategori isimlerinin sınıfa bakmadan bilinmesini sağlar örneğin Category. yazdığın zaman dropdown menüde hangi kategorilerin olduğu gösterilir. (eminim sorduğun fayda bu değildir ama aklıma bu geldi sadece)

7. Maven: (a) GAV'ın üç harfi ne demek? (b) `mvn clean package` sonunda ne oluşur ve hangi klasörde? (c) `java -jar app.jar` → `no main manifest attribute` hatası neden çıkar?
Cevap: a) groupId artifactId versionId b) target klasörünün içeriği silinir proje yeniden derlenir, target klasöründe dosyalar yeniden oluşturulur. c) mainClass pom.xml dosyasında tanıtılmamıştır.

## Bölüm B — Çıktı tahmini (ÇALIŞTIRMADAN) · 25 puan

Her satırın çıktısını yaz. Exception fırlatan satır için exception'ın **adını** yaz.

**B1.**
```java
Optional<String> a = Optional.of("Ada");
Optional<String> b = Optional.empty();
System.out.println(a);
System.out.println(a.orElse("none"));
System.out.println(b.orElse("none"));
System.out.println(b);
System.out.println(b.map(String::length).orElse(-1));
```
Cevap: [Optional]Ada
Ada
NosuchElementException
[Optional]Empty
-1

**B2.**
```java
List<String> names = List.of("Ali", "Bora", "Cem", "Deniz", "Ece");
System.out.println(names.stream().filter(n -> n.length() == 3).map(String::toUpperCase).toList());
System.out.println(names.stream().mapToInt(String::length).sum());
System.out.println(names.stream().anyMatch(n -> n.startsWith("D")));
System.out.println(names.stream().sorted(Comparator.comparing(String::length).reversed()).findFirst().orElse("?"));
System.out.println(names);
```
Cevap:ALİ, CEM, ECE
18
true
Deniz
Ali, Bora, Cem, Deniz, Ece

**B3.**
```java
List<String> words = List.of("kiwi", "fig", "apple", "pear", "plum", "date");
Map<Integer, Long> m = words.stream()
        .collect(Collectors.groupingBy(String::length, Collectors.counting()));
System.out.println(new TreeMap<>(m));
```
Cevap:4=3, 3=1, 5=1

**B4.**
```java
enum Size { S, M, L }
record Shirt(String color, Size size) {}

Shirt x = new Shirt("red", Size.M);
Shirt y = new Shirt("red", Size.M);
System.out.println(x);
System.out.println(x == y);
System.out.println(x.equals(y));
System.out.println(Size.L.ordinal());
System.out.println(Size.valueOf("m"));
```
Cevap: red, M
false
true
ordinal ne demek bilmiyorum
IllegalArgumentException

**B5.**
```java
LocalDate d = LocalDate.of(2026, 1, 31);
d.plusDays(1);
System.out.println(d);
System.out.println(d.plusMonths(1));
System.out.println(ChronoUnit.DAYS.between(d, LocalDate.of(2026, 2, 3)));
System.out.println(LocalDate.parse("2026-02-30"));
```
Cevap: 31.01.2026
28.02.2026
3
2026, 02, 30

**B6.**
```java
Path p = Path.of("b6.txt");
Files.write(p, List.of("x", "y"));
Files.write(p, List.of("z"), StandardOpenOption.APPEND);
System.out.println(Files.readAllLines(p));
Files.write(p, List.of("w"));
System.out.println(Files.readAllLines(p));
```
Cevap:x, y, z
w
