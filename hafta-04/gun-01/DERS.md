# Hafta 4 · Gün 1 (Gün 19) — Lambda, functional interface, method reference

**Hedef:** Hafta 3'te `(a, b) -> ...`, `Book::getAuthor`, `removeIf(...)` yazdın ama bunların ne olduğunu hiç konuşmadık. Bugün konuşuyoruz: lambda nedir, nereye yazılabilir, `Predicate` / `Function` / `Consumer` / `Supplier` ne işe yarar. Yarın gelecek Stream API tamamen bunun üstüne kurulu. Proje tarafında Hafta 4'ün projesi **Gider Takip**'e başlıyorsun.

**Süre:** ~4.5 saat · Ders 1 sa · Egzersiz 1 sa · Proje 1 sa · Algoritma 45 dk · Kapanış 15 dk

**Kurallar (değişmedi):**
- Sınıf, metot, değişken ve ekran mesajları **İngilizce**
- **Değişiklik → derle → çalıştır → çıktının her satırını oku.**
- **Metot adı/imzası görevde nasılsa öyle.**
- Tahminler **çalıştırmadan önce** chat'e.
- Testleri Claude yazıp çalıştırıyor. Sen yazmıyorsun.

---

## 0. Isınma — bugün yok

Oturum başında `Integer.compare` / `(a, b)` tekrarını chat'te yaptık. Bugün bunun devamı.

---

## 1. Lambda = "tek metotluk bir nesneyi kısa yazmak" ⭐⭐⭐ (15 dk)

Hafta 3'te uzunluğa göre sıralamak için tam bir class yazabilirdin. Java 8'den önce herkes bunu "isimsiz class" (anonymous class) ile yazardı:

```java
Comparator<String> byLength = new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return Integer.compare(a.length(), b.length());
    }
};
```

Bu 6 satırın asıl işi tek satır: `Integer.compare(a.length(), b.length())`. Gerisi tören. Lambda bu töreni siler:

```java
Comparator<String> byLength = (a, b) -> Integer.compare(a.length(), b.length());
```

İkisi **aynı şey**: `compare` metodu olan bir `Comparator` nesnesi. `names.sort(byLength)` ikisiyle de aynı çalışır.

**Lambda'nın yazılış biçimleri:**

| Yazılış                                   | Ne zaman                                           |
| ----------------------------------------- | -------------------------------------------------- |
| `() -> 42`                                | parametre yok                                      |
| `x -> x * 2`                              | tek parametre (parantez opsiyonel)                 |
| `(a, b) -> a + b`                         | iki parametre                                      |
| `(a, b) -> { int s = a + b; return s; }`  | süslü parantez varsa `return` **zorunlu**          |

Parametrelerin tipini (`String a`) yazmana gerek yok. Java, lambda'yı hangi değişkene / parametreye verdiğine bakıp anlıyor.

---

## 2. Functional interface: lambda nereye yazılabilir? ⭐⭐⭐ (10 dk)

Lambda her yere yazılamaz. Sadece **tek abstract metodu olan bir interface** beklenen yere yazılır. Bunlara **functional interface** denir.

`Comparator<T>`'nin tek abstract metodu `compare(T a, T b)`. Lambda yazdığında Java şunu yapıyor: "Bu lambda, o tek metodun gövdesi."

Kendin de yazabilirsin:

```java
@FunctionalInterface              // opsiyonel: 2. abstract metot eklenirse derleme hatası verir
interface Discount {
    double apply(double price);
}

Discount tenPercent = price -> price * 0.9;
Discount fixed50    = price -> price - 50;

System.out.println(tenPercent.apply(200));   // 180.0
System.out.println(fixed50.apply(200));      // 150.0
```

Dikkat: lambda **çalışmaz**, sadece **saklanır**. `tenPercent` bir değişken; içinde "fiyatı 0.9 ile çarp" tarifi var. Tarif ancak `apply(...)` çağrıldığında çalışır. `list.sort(lambda)` dediğinde de sort, lambda'yı içeride defalarca çağırıyor (sabahki `(a, b)` anlatımı).

---

## 3. Java'nın hazır functional interface'leri ⭐⭐⭐ (15 dk)

Her seferinde `Discount` gibi interface yazmayasın diye `java.util.function` paketinde hazırları var. Bugün 4'ünü bilmen yeterli:

| Interface        | Metodu              | Ne alır → ne döner | Gördüğün yer                         |
| ---------------- | ------------------- | ------------------ | ------------------------------------ |
| `Predicate<T>`   | `boolean test(T)`   | T → boolean        | `list.removeIf(...)`                 |
| `Function<T, R>` | `R apply(T)`        | T → R              | `Comparator.comparing(...)` içine    |
| `Consumer<T>`    | `void accept(T)`    | T → (hiçbir şey)   | `list.forEach(...)`                  |
| `Supplier<T>`    | `T get()`           | () → T             | (yarın Optional'da)                  |

```java
import java.util.function.*;

Predicate<Integer> isPositive = n -> n > 0;
Function<String, Integer> length = s -> s.length();
Consumer<String> printer = s -> System.out.println(">> " + s);
Supplier<String> greeting = () -> "Hello";

System.out.println(isPositive.test(-3));   // false
System.out.println(length.apply("Java"));  // 4
printer.accept("hi");                      // >> hi
System.out.println(greeting.get());        // Hello
```

`Function<T, R>`'deki **R** = "Result", dönüş tipi. `Function<String, Integer>` = "String alır, Integer döner".

Hafta 3'te bilmeden kullandıkların artık anlam kazanıyor:

```java
books.removeIf(b -> b.getYear() < 1950);        // removeIf bir Predicate<Book> istiyor
names.forEach(n -> System.out.println(n));      // forEach bir Consumer<String> istiyor
```

---

## 4. Method reference: `::` ⭐⭐ (10 dk)

Lambda **sadece** var olan tek bir metodu çağırıyorsa daha da kısaltabilirsin:

| Lambda                            | Method reference          |
| --------------------------------- | ------------------------- |
| `s -> s.length()`                 | `String::length`          |
| `b -> b.getAuthor()`              | `Book::getAuthor`         |
| `x -> System.out.println(x)`      | `System.out::println`     |
| `() -> new ArrayList<>()`         | `ArrayList::new`          |

Hafta 3'teki `Comparator.comparing(Book::getAuthor)` = `Comparator.comparing(b -> b.getAuthor())`. `comparing` bir `Function<Book, String>` istiyor: "bana kitabı ver, sana yazarını vereyim, ben de yazarları karşılaştırayım."

Lambda bir şey **daha** yapıyorsa (`s -> s.length() * 2`) method reference yazamazsın. O zaman lambda kalır.

---

## 5. Kural: dışarıdaki değişkeni değiştiremezsin ⭐⭐ (5 dk)

Lambda, kendi dışındaki local değişkenleri **okuyabilir**:

```java
Map<Integer, Integer> repeat = ...;                    // Exam2'deki gibi
integers.sort((a, b) -> Integer.compare(repeat.get(a), repeat.get(b)));   // ✅ okuyor
```

Ama o değişken **bir daha hiç atanmamalı**. Buna *effectively final* denir: `final` yazmasan da değeri hiç değişmeyen değişken. Lambda içinden `count++` yapmak da, lambda'dan sonra değişkene yeni değer atamak da derleme hatası verir. (Neden: lambda daha sonra, başka bir yerde çalışabilir; Java hangi değeri göreceğini garanti edemez.)

---

## 6. Comparator kısayolları + dünkü tuzak ⭐⭐ (5 dk)

```java
Comparator.comparing(Book::getTitle)                 // String alanına göre
Comparator.comparingInt(Book::getYear)               // int alanına göre
Comparator.comparingDouble(Expense::getAmount)       // double alanına göre
    .thenComparing(Book::getTitle)                   // eşitse ikinci kural
    .reversed()                                      // ŞİMDİYE KADAR yazılan HER ŞEYİ ters çevirir
```

Sınav C1'deki hata: `comparing(price).thenComparing(name).reversed()` → isim sırası da tersine döndü. Sadece fiyatı ters çevirmek istiyorsan `reversed()`'ı fiyattan hemen sonra yaz: `comparingDouble(price).reversed().thenComparing(name)`.

---

## 📝 Kağıda Yaz

```
Lambda = tek metotluk interface'in kısa yazılışı (çalışmaz, saklanır)
Functional interface = tek abstract metot → oraya lambda yazılır
Predicate test T→boolean · Function apply T→R
Consumer accept T→void   · Supplier get ()→T
removeIf(Predicate) · forEach(Consumer) · comparing(Function)
x -> x.foo()  ==  X::foo  (sadece tek metot çağırıyorsa)
Lambda dışarıdaki değişkeni okur, DEĞİŞTİREMEZ (effectively final)
.reversed() kendinden önceki zincirin tamamını ters çevirir
```

---

## 💪 EGZERSİZLER (~1 saat)

Klasör: `hafta-04/gun-01/w4d1/` · Paket: `package w4d1;` · Import: `java.util.*` ve `java.util.function.*`

### E1 — `FunctionDemo.java` (tahmin)

Aşağıdaki kodu `main`'e yaz. **Çalıştırmadan önce** her `println`'ın çıktısını chat'e yaz (toplam 6 satır), sonra çalıştır ve karşılaştır.

```java
Predicate<String> isLong = s -> s.length() > 3;
Function<String, Integer> len = String::length;
Consumer<String> shout = s -> System.out.println(s.toUpperCase() + "!");
Supplier<List<String>> maker = () -> new ArrayList<>();

System.out.println(isLong.test("Ali") + " " + isLong.test("Deniz"));
System.out.println(len.apply("Java") * 2);
shout.accept("hey");
List.of("a", "bc").forEach(shout);
List<String> a = maker.get();
List<String> b = maker.get();
a.add("x");
System.out.println(a.size() + " " + b.size() + " " + (a == b));
```

Ek soru (chat'e, 1 cümle): `List.of("a", "bc").forEach(shout)` satırı neden çalışıyor? `forEach` ne istiyor, `shout` ne?

### E2 — `LambdaConvert.java` (kod yaz)

`main`'e aşağıdaki 3 isimsiz class'ı **lambda** olarak yeniden yaz. Değişken adları ve tipleri aynı kalsın; sadece `new ... { ... }` kısmı lambda olacak.

```java
Comparator<String> byLength = new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return Integer.compare(a.length(), b.length());
    }
};

Runnable hello = new Runnable() {
    @Override
    public void run() {
        System.out.println("Hello");
    }
};

Predicate<Integer> isEven = new Predicate<Integer>() {
    @Override
    public boolean test(Integer n) {
        return n % 2 == 0;
    }
};
```

(`Runnable` da functional interface: tek metodu `void run()`, parametre almaz, bir şey döndürmez.)

Sonra `main`'in sonuna şunu ekle. **Çalıştırmadan önce** 4 satırlık çıktıyı chat'e tahmin et:

```java
List<String> names = new ArrayList<>(List.of("Deniz", "Ali", "Bora", "Ce"));
names.sort(byLength);
System.out.println(names);
names.sort(byLength.reversed());
System.out.println(names);
hello.run();
System.out.println(isEven.test(4) + " " + isEven.test(7));
```

Son adım: `byLength`'i bir kez daha, bu sefer `Comparator.comparingInt(...)` + method reference ile yaz (yeni değişken: `byLength2`). Çıktı aynı olmalı.

### E3 — `ListUtils.java` (kod yaz)

İki generic static metot. İkisi de **girdi listesini değiştirmez** ve **yeni bir `ArrayList`** döndürür.

```java
public static <T> List<T> filter(List<T> list, Predicate<T> condition)
```
- `list`'teki elemanlardan `condition.test(...)` `true` olanları, aynı sırayla, yeni listeye koyar.

```java
public static <T, R> List<R> map(List<T> list, Function<T, R> mapper)
```
- `list`'teki her elemana `mapper.apply(...)` uygular, sonuçları aynı sırayla yeni listeye koyar.
- `<T, R>`: iki tip parametresi. Girdi elemanı T, çıktı elemanı R (Gün 16'daki `<T>`'nin iki tanelisi).

`main`'de bu 3 satırı çalıştır. Beklenen çıktı yorumlarda:

```java
System.out.println(filter(List.of(3, 8, 1, 10, 6), n -> n > 5));                 // [8, 10, 6]
System.out.println(map(List.of("Ali", "Deniz"), String::length));                 // [3, 5]
System.out.println(map(filter(List.of(3, 8, 1, 10, 6), n -> n > 5), n -> n * 10)); // [80, 100, 60]
```

Yarın göreceğin Stream API'nin `filter` ve `map`'i tam olarak bunu yapıyor. Bugün içini sen yazıyorsun.

### E4 — İki derleme hatası (tahmin)

Her biri için chat'e yaz: **derlenir mi?** Derlenmiyorsa hangi satır, neden? (Bölüm 5.)

```java
// (a)
int count = 0;
List<String> names = List.of("Ali", "Bora");
names.forEach(n -> count++);
```

```java
// (b)
List<String> names = List.of("Ali", "Bora");
String prefix = "Mr. ";
names.forEach(n -> System.out.println(prefix + n));
prefix = "Ms. ";
```

Sonra (a)'yı derlenir hâle getir: isimleri saymanın lambda'sız bir yolunu yaz (tek satır yeter).

---

## 🏛️ PROJE 0 — Gider Takip v1 (~1 saat)

Hafta 4'ün projesi: konsol **Gider Takip** uygulaması. Hafta sonuna kadar büyüyecek: stream, dosyaya kaydetme, tarih, Maven. Bugün çekirdeği yazıyorsun. Bütün filtreleme ve sıralama **dışarıdan lambda alarak** yapılıyor.

Klasör/paket: `hafta-04/gun-01/w4d1/` (egzersizlerle aynı yer).

### `Expense`

**Alanlar** (hepsi `private final`):
- `String description`
- `String category`
- `double amount`

**Constructor:** `public Expense(String description, String category, double amount)`
- `description` `null` veya boş → `IllegalArgumentException("Description must not be blank")`
- `category` `null` veya boş → `IllegalArgumentException("Category must not be blank")`
- `amount <= 0` → `IllegalArgumentException("Amount must be positive")`

**Metotlar:**
- `getDescription()`, `getCategory()`, `getAmount()`
- `toString()` → `Coffee (Food) 45.5` biçimi: `description + " (" + category + ") " + amount`

Setter yok (alanlar `final`).

### `ExpenseTracker`

**Alan:** `private final List<Expense> expenses = new ArrayList<>();`

**Metotlar:**

| İmza                                                   | Ne yapar                                                                                       |
| ------------------------------------------------------ | ---------------------------------------------------------------------------------------------- |
| `public void add(Expense expense)`                     | `null` → `IllegalArgumentException("Expense must not be null")`, değilse ekler                 |
| `public List<Expense> getAll()`                        | değiştirilemez kopya (`List.copyOf`)                                                           |
| `public List<Expense> filter(Predicate<Expense> c)`    | `c`'ye uyanları yeni listede döndürür, `expenses`'a dokunmaz                                   |
| `public List<Expense> sorted(Comparator<Expense> o)`   | `expenses`'ın **kopyasını** `o`'ya göre sıralayıp döndürür, `expenses`'ın sırası değişmez      |
| `public double total(Predicate<Expense> c)`            | `c`'ye uyanların `amount` toplamı                                                              |
| `public int removeWhere(Predicate<Expense> c)`         | `c`'ye uyanları `expenses`'tan siler, kaç tane sildiğini döndürür                              |

`removeWhere`'ı `Iterator` ile mi, `removeIf` ile mi yazacağın **senin tasarım kararın**. Dikkat: `removeIf` silinen sayıyı değil `boolean` döndürür.

### `ExpenseApp` (`main`)

Bugün menü yok, sadece sırayla çalışan bir demo.

1. Tracker'a şu 6 gideri bu sırayla ekle:

   | description | category  | amount   |
   | ----------- | --------- | -------- |
   | Coffee      | Food      | 45.5     |
   | Rent        | Home      | 12000    |
   | Bus card    | Transport | 15       |
   | Groceries   | Food      | 640.75   |
   | Gum         | Food      | 12.5     |
   | Internet    | Home      | 450      |

2. `Predicate<Expense> isFood` değişkeni tanımla (kategori `"Food"` mi?). Bunu 4. ve 8. adımda **tekrar kullanacaksın**.
3. `All:` yaz, sonra hepsini yazdır. **Zorunlu:** `getAll().forEach(...)` + method reference.
4. `Food:` → `isFood` ile filtrele, yazdır.
5. `Over 100:` → tutarı 100'den büyükleri yazdır (lambda'yı doğrudan argüman olarak yaz).
6. `By amount desc:` → tutara göre büyükten küçüğe.
7. `By category, then amount:` → önce kategori (A→Z), aynı kategoride tutar küçükten büyüğe.
8. `Food total: ` + yemek toplamı
9. `Removed: ` + tutarı 20'den küçükleri sil, silinen sayı
10. `Left: ` + kalan gider sayısı
11. `new Expense("Test", "Food", 0)` dene → yakala → `Error: <mesaj>`

**Beklenen çıktı:**
```
All:
Coffee (Food) 45.5
Rent (Home) 12000.0
Bus card (Transport) 15.0
Groceries (Food) 640.75
Gum (Food) 12.5
Internet (Home) 450.0
Food:
Coffee (Food) 45.5
Groceries (Food) 640.75
Gum (Food) 12.5
Over 100:
Rent (Home) 12000.0
Groceries (Food) 640.75
Internet (Home) 450.0
By amount desc:
Rent (Home) 12000.0
Groceries (Food) 640.75
Internet (Home) 450.0
Coffee (Food) 45.5
Bus card (Transport) 15.0
Gum (Food) 12.5
By category, then amount:
Gum (Food) 12.5
Coffee (Food) 45.5
Groceries (Food) 640.75
Internet (Home) 450.0
Rent (Home) 12000.0
Bus card (Transport) 15.0
Food total: 698.75
Removed: 2
Left: 4
Error: Amount must be positive
```

### Proje sonu düşünme soruları (chat'e, 1-2 cümle)

1. `ExpenseTracker`'da `filterFood()`, `filterOver100()`, `filterHome()` diye ayrı ayrı metotlar yazmak yerine tek bir `filter(Predicate<Expense>)` yazdık. Yarın biri "Transport giderlerini göster" derse hangi dosyaya dokunman gerekir, hangisine gerekmez?
2. `sorted(...)` metodunda kopyayı değil doğrudan `expenses`'ı sıralasaydın, 6. ve 7. adımdan sonra `getAll()` ne gösterirdi? Bu neden kötü?

---

## 🧩 ALGORİTMA SAATİ (45 dk) — Sıralama + sayma

**A1 — [LeetCode 1365: How Many Numbers Are Smaller Than the Current Number](https://leetcode.com/problems/how-many-numbers-are-smaller-than-the-current-number/)**
`int[] smallerNumbersThanCurrent(int[] nums)`: her `nums[i]` için dizide ondan **küçük** kaç sayı olduğunu döndür.
- Örnek: `[8,1,2,2,3]` → `[4,0,1,1,3]` (8'den küçük 4 sayı var, 1'den küçük yok…)
- Önce iki iç içe `for` ile (O(n²)) çöz ve submit et.
- Sonra daha hızlı yol (ipucu): dizinin **sıralanmış kopyasında** bir sayının **ilk** göründüğü index = ondan küçük sayıların adedi. Bunu bir `Map<Integer, Integer>`'a koy. `[1,2,2,3,8]` kopyasına bakarak 2 için cevabın neden 1 olduğunu chat'e yaz, sonra kodla.

**A2 — [LeetCode 1122: Relative Sort Array](https://leetcode.com/problems/relative-sort-array/)**
`int[] relativeSortArray(int[] arr1, int[] arr2)`: `arr1`'i, `arr2`'deki sıraya göre sırala. `arr2`'de olmayanlar sona, küçükten büyüğe.
- Örnek: `arr1 = [2,3,1,3,2,4,6,7,9,2,19]`, `arr2 = [2,1,4,3,9,6]` → `[2,2,2,1,4,3,3,9,6,7,19]`
- Bu Exam2'nin (LC 1636) kardeşi: `Map` + `List<Integer>` + `sort((a, b) -> ...)`. Map'te bu sefer **sayma yok**: her sayının `arr2`'deki **index**'ini tut.
- Comparator'ı yazmadan önce chat'e 3 durumu yaz: (1) a da b de `arr2`'de, (2) sadece biri `arr2`'de, (3) ikisi de değil. Her durumda kim önce gelmeli, lambda ne döndürmeli?

---

## 🔧 GIT (10 dk)

```bash
git add hafta-04/gun-01
git status            # sadece .java ve DERS.md (.class yok)
git commit -m "feat: week 4 day 1 - lambda"
git push
```
Sonra `ILERLEME.md`'ye Gün 19 logu → `docs: day 19 progress log`.

---

## ✅ Gün Sonu

- [ ] Bölüm 1–6 okundu
- [ ] E1, E2, E4 (tahminler **çalıştırmadan önce** chat'e)
- [ ] E3 `ListUtils`
- [ ] Proje: `Expense`, `ExpenseTracker`, `ExpenseApp` (çıktı birebir aynı)
- [ ] Proje düşünme soruları
- [ ] A1 + A2
- [ ] "Kağıda Yaz" kutusu kağıtta
- [ ] Commit + push, `ILERLEME.md` Gün 19

**Yarın oturum başında soracaklarım:** 4-5 kısa soru (kod örnekli): hangi functional interface hangi metodu taşır, lambda ↔ method reference çevirme, effectively final.

**Sıradaki:** Hafta 4 Gün 2 (Gün 20): **Stream API**. Bugünkü `filter`/`map`'in hazırı ve çok daha fazlası.
