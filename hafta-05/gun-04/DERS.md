# Hafta 5 · Gün 4 (Gün 28) — TEKRAR: Lambda + Functional Interface + Stream

**Hedef:** Yeni konu yok. Hafta 4'teki lambda, method reference ve Stream'i **notsuz yazabilir** hâle getirmek.
En çok kaçan hata: stream'de **`sorted`'dan önce `map`** yapmak. Bugün her adımdan sonra "elimde ne tip var?" diye soracağız.
Kapı sınavında (Gün 31) bugünkü konular ~20 puan.

**Süre:** ~5-6 saat
Isınma 15 dk · Konu özeti 30 dk · Çıktı tahmini 20 dk · Notsuz kod görevi 1.5–2 sa · Algoritma 1 sa · Kapanış 15 dk

**Kurallar (değişmedi):**
- Sınıf, metot, değişken ve mesajlar **İngilizce**
- **Değişiklik → derle → çalıştır → çıktının her satırını oku.**
- **Metot adı/imzası görevde nasılsa öyle.** Exception mesajları **birebir**.
- `main`'e test yazmak yok. Testleri ben yazıp çalıştırıyorum.

**Kurulum:** `hafta-05/gun-04/` klasöründe package **`w5d4`** (`hafta-05/gun-04/w5d4/`). `gun-04`'ü Sources Root yap.

---

## 0. Isınma (15 dk) — cevapları chat'e yaz

**S1.** Çıktı ne?
```java
record City(String name, int pop) {}
System.out.println(List.of(new City("Ankara", 5), new City("Izmir", 4)));
```

**S2.** Çıktı ne? (2 satır)
```java
Map<String, List<Integer>> m = new TreeMap<>();
m.put("b", List.of(2));
m.put("a", List.of(1, 3));
System.out.println(m);
System.out.println(m.values());
```

**S3.** Bu satır derlenir:
```java
record P(String name, int age) {}
List<String> r = ps.stream().sorted(Comparator.comparingInt(P::age)).map(P::name).toList();
```
`sorted` ile `map`'in **yerini değiştirirsen** neden derlenmez? Tek cümle.

---

## 1. Konu Özeti (30 dk) — oku, takıldığın yeri chat'e sor

### 1.1 Lambda = tek metotlu interface'in kısa yazımı

```java
Predicate<String> isLong = s -> s.length() > 3;          // tek parametre: parantez gerekmez
Comparator<String> byLen = (a, b) -> Integer.compare(a.length(), b.length());  // iki parametre: parantez şart
Supplier<String> hi = () -> "hi";                         // parametresiz: ()
Function<Integer, Integer> sq = x -> {                     // birden çok satır: { } + return
    int y = x * x;
    return y;
};
```

**Bilmen gereken 5 interface** — hangi metodu çağırırsın, ne döner:

| Interface          | Metot              | Girdi → Çıktı    | Örnek                          |
| ------------------ | ------------------ | ---------------- | ------------------------------ |
| `Predicate<T>`     | `test(t)`          | T → `boolean`    | `s -> s.isEmpty()`             |
| `Function<T, R>`   | `apply(t)`         | T → R            | `s -> s.length()`              |
| `Consumer<T>`      | `accept(t)`        | T → (void)       | `s -> System.out.println(s)`   |
| `Supplier<T>`      | `get()`            | () → T           | `() -> new ArrayList<>()`      |
| `Comparator<T>`    | `compare(a, b)`    | (T, T) → `int`   | `(a, b) -> a.compareTo(b)`     |

**Method reference** = lambda sadece bir metodu çağırıyorsa kısası:

| Lambda                          | Method reference        |
| ------------------------------- | ----------------------- |
| `s -> s.length()`               | `String::length`        |
| `s -> System.out.println(s)`    | `System.out::println`   |
| `s -> Integer.parseInt(s)`      | `Integer::parseInt`     |
| `o -> o.total()` (record)       | `Order::total`          |

⚠️ **Effectively final:** Lambda içinde kullandığın **local** değişken bir daha değiştirilmemeli.
```java
int base = 10;
Function<Integer, Integer> add = x -> x + base;   // base'i kullanıyor
base = 20;                                         // ❌ derleme hatası (yukarıdaki satırda)
```

Lambda'yı **döndüren** metot da yazılabilir (bugünkü görevde var):
```java
static Predicate<String> longerThan(int n) {
    return s -> s.length() > n;           // n parametre, değişmiyor → serbest
}
longerThan(3).test("tiger")               // true
```

### 1.2 Stream = kaynak → ara adımlar → **tek** bitiş adımı

```java
list.stream()          // kaynak
    .filter(...)       // ara adım (Stream döner)
    .map(...)          // ara adım
    .toList();         // bitiş adımı (sonuç burada üretilir)
```

- **Bitiş adımı yoksa hiçbir şey çalışmaz** (filter içindeki `println` bile).
- Bir stream **bir kez** kullanılır. İkinci bitiş adımı → `IllegalStateException`.
- Stream **kaynak listeyi değiştirmez**.
- `.toList()` **değiştirilemez** liste döner → `add` → `UnsupportedOperationException`.

### 1.3 Her adımdan sonra elinde ne var?

| Adım                                  | Ne yapar                           | Sonrası                  |
| ------------------------------------- | ---------------------------------- | ------------------------ |
| `filter(Predicate)`                   | şartı tutanlar kalır               | `Stream<T>` (aynı tip)   |
| `map(Function)`                       | her elemanı dönüştürür             | `Stream<R>` (yeni tip)   |
| `sorted()` / `sorted(cmp)`            | sıralar                            | `Stream<T>`              |
| `distinct()`                          | tekrarları atar (`equals`)         | `Stream<T>`              |
| `limit(n)`                            | ilk n tane                         | `Stream<T>`              |
| `mapToInt(f)`                         | sayıya çevirir                     | `IntStream`              |
| `toList()`                            | listeye toplar                     | `List<T>`                |
| `count()`                             | kaç tane                           | `long`                   |
| `anyMatch(p)` / `allMatch(p)`         | biri / hepsi tutuyor mu            | `boolean`                |
| `findFirst()`                         | ilk eleman                         | `Optional<T>`            |
| `max(cmp)` / `min(cmp)`               | en büyük / küçük                   | `Optional<T>`            |
| `sum()` (sadece IntStream'de)         | toplam                             | `int`                    |
| `forEach(c)`                          | her biri için çalıştır             | `void`                   |

**`sorted`'dan önce `map` tuzağı** — tip satır satır:
```java
orders.stream()                                        // Stream<Order>
      .sorted(Comparator.comparingInt(Order::total))   // Stream<Order>  ← hâlâ Order, total() var
      .map(Order::id)                                  // Stream<String> ← artık sadece id'ler
      .toList();                                       // List<String>
```
`map(Order::id)`'yi önce yaparsan elinde `String` kalır, `Order::total` ile sıralayamazsın.
**Kural: önce Order'a lazım olan her şeyi yap (filter, sorted, limit), `map` en sona.**

### 1.4 `collect` ile Map'e toplamak — `groupingBy`

```java
// key → o key'deki elemanların LİSTESİ
Map<String, List<Order>> a = orders.stream()
        .collect(Collectors.groupingBy(Order::customer));

// key → kaç tane (Long!)
Map<String, Long> b = orders.stream()
        .collect(Collectors.groupingBy(Order::customer, Collectors.counting()));

// key → toplam (int)
Map<String, Integer> c = orders.stream()
        .collect(Collectors.groupingBy(Order::customer, Collectors.summingInt(Order::total)));

// key'ler sıralı olsun → ortaya TreeMap::new
Map<String, Long> d = orders.stream()
        .collect(Collectors.groupingBy(Order::customer, TreeMap::new, Collectors.counting()));
```
⚠️ `groupingBy`'dan önce `map` **yapma**: key'i de değeri de **Order**'dan alıyorsun.

### 1.5 Döngü ↔ Stream

```java
// döngü                                   // stream
int sum = 0;                               int sum = orders.stream()
for (Order o : orders) {                           .filter(o -> o.qty() > 1)
    if (o.qty() > 1) {                             .mapToInt(Order::total)
        sum += o.total();                          .sum();
    }
}
```
`if` → `filter`, "neyi topluyorum" → `map` / `mapToInt`, `sum +=` → `sum()`.

### ✍️ Kağıda Yaz

```
Predicate test→boolean · Function apply→R · Consumer accept→void · Supplier get()→T
Lambda içindeki local değişken effectively final olmalı
Bitiş adımı yoksa stream ÇALIŞMAZ · stream bir kez kullanılır (2. kez → ISE)
toList() değiştirilemez → add → UOE
filter/sorted/limit ÖNCE, map EN SONA (her satırda tipi sor)
findFirst / max / min → Optional · count → long · counting() → Long
groupingBy(key) → Map<K, List<T>> · (key, TreeMap::new, counting()) → sıralı key
```

---

## 2. Çıktı Tahmini (20 dk) — ÇALIŞTIRMADAN, cevaplar chat'e

Her snippet `main` içinde. Exception çıkarsa **hangi exception** olduğunu yaz, sonrasında yazdırılmayan satırları yazma. Derleme hatası varsa "derlenmez" + nedenini yaz.

**P1.**
```java
Predicate<String> longW = s -> s.length() > 3;
Function<String, Integer> len = String::length;
Supplier<String> sup = () -> "hi";
System.out.println(longW.test("cat"));
System.out.println(len.apply("tiger") + 1);
System.out.println(sup.get() + sup.get());
```

**P2.** Derlenir mi?
```java
int base = 10;
Function<Integer, Integer> add = x -> x + base;
base = 20;
System.out.println(add.apply(5));
```

**P3.**
```java
List<Integer> r = List.of(4, 7, 2, 9, 6).stream()
        .filter(x -> x % 2 == 0)
        .map(x -> x * 10)
        .toList();
System.out.println(r);
```

**P4.**
```java
record Emp(String name, int salary) {}
List<Emp> es = List.of(new Emp("Ada", 300), new Emp("Can", 500), new Emp("Bora", 400));
System.out.println(es.stream()
        .sorted(Comparator.comparingInt(Emp::salary).reversed())
        .limit(2)
        .map(Emp::name)
        .toList());
System.out.println(es.get(0));
```

**P5.** (5 satır)
```java
List<String> w = List.of("kiwi", "fig", "apple", "fig");
System.out.println(w.stream().distinct().count());
System.out.println(w.stream().anyMatch(s -> s.startsWith("a")));
System.out.println(w.stream().filter(s -> s.length() > 4).findFirst());
System.out.println(w.stream().filter(s -> s.length() > 9).findFirst());
System.out.println(w.stream().mapToInt(String::length).sum());
```

**P6.**
```java
List<Integer> xs = List.of(1, 2, 3);
xs.stream().filter(x -> {
    System.out.println("check " + x);
    return x > 1;
});
System.out.println("end");
```

**P7.**
```java
Stream<String> s = Stream.of("a", "b");
System.out.println(s.count());
System.out.println(s.count());
```

**P8.**
```java
List<Integer> r = Stream.of(3, 1, 2).sorted().toList();
System.out.println(r);
r.add(4);
System.out.println(r);
```

**P9.**
```java
List<String> w = List.of("ant", "bee", "cow", "deer", "ox");
Map<Integer, Long> byLen = w.stream()
        .collect(Collectors.groupingBy(String::length, TreeMap::new, Collectors.counting()));
System.out.println(byLen);
Map<Character, List<String>> byFirst = w.stream()
        .collect(Collectors.groupingBy(s -> s.charAt(0)));
System.out.println(byFirst.get('d'));
```

---

## 3. Notsuz Kod Görevi (1.5–2 sa) — Sipariş Raporu

**Kural:** DERS dosyalarına (bu dosyanın özet kısmı dahil), eski koduna, internete bakmak **yok**. AI kapalı. Bu bölümün **görev metni** açık kalabilir. Takılırsan önce 10 dk kendin dene, sonra chat'e sor.

Her public tip kendi dosyasında, package `w5d4`.
**`OrderReport`'ta `for` / `while` YASAK, sadece stream** — tek istisna `totalRevenueLoop()` (orada tersi: stream yasak).

### 3.1 `record Order(String id, String customer, String category, int qty, int unitPrice)` (`Order.java`)

- **Compact constructor**, kontroller bu sırayla:
  - `id == null` ya da `id.isBlank()` → `IllegalArgumentException("Id must not be blank")`
  - `customer == null` ya da `customer.isBlank()` → `IllegalArgumentException("Customer must not be blank")`
  - `category == null` ya da `category.isBlank()` → `IllegalArgumentException("Category must not be blank")`
  - `qty < 1` → `IllegalArgumentException("Bad qty: " + qty)`
  - `unitPrice < 0` → `IllegalArgumentException("Bad price: " + unitPrice)`
- `public int total()` → `qty * unitPrice`

### 3.2 `final class Filters` (`Filters.java`) — lambda **döndüren** static metotlar

**Constructor:** `private Filters() {}`

**Metotlar:**
- `public static Predicate<Order> minTotal(int min)` → `total()` **≥ `min`** olan siparişler için `true`.
- `public static Predicate<Order> byCustomer(String customer)` → müşterisi `customer`'a **eşit** olan siparişler için `true`.
- `public static Function<Order, String> label()` → `"id:customer:total"`.
  Örnek: `new Order("o1", "Ada", "book", 2, 150)` → `"o1:Ada:300"`

### 3.3 `class OrderReport` (`OrderReport.java`)

**Field:**
- `private final List<Order> orders;`

**Constructor:** `public OrderReport(List<Order> orders)`
- `orders == null` → `IllegalArgumentException("Orders must not be null")`
- Gelen listenin **kopyasını** sakla: dışarıdaki liste sonradan değişirse rapor etkilenmemeli (`List.of(...)` da gelebilir).

**Metotlar:**
- `public List<Order> matching(Predicate<Order> condition)` → şartı tutan siparişler, **orijinal sırayla**.
- `public int totalRevenue()` → bütün siparişlerin `total()` toplamı. Sipariş yoksa `0`.
- `public int totalRevenueLoop()` → aynısı, **for-each ile** (stream yasak).
- `public List<String> labelsByTotalDesc()` → her siparişin `Filters.label()` çıktısı; sıra: `total` **büyükten küçüğe**, eşitse `id` alfabetik.
- `public Optional<Order> biggestOrder()` → `total`'ı en büyük sipariş; sipariş yoksa `Optional.empty()`. (Testlerde eşit en büyük yok.)
- `public Map<String, Long> orderCountByCategory()` → kategori → sipariş sayısı, key'ler **alfabetik**.
  Örnek: book, pen, book → `{book=2, pen=1}`
- `public Map<String, Integer> revenueByCustomer()` → müşteri → `total()` toplamı, key'ler **alfabetik**.
- `public List<String> customersWhoBought(String category)` → o kategoriden sipariş vermiş müşteriler, **her biri bir kez**, **alfabetik**. Kimse yoksa boş liste.

**Bitince:** "bitti" yaz, dosyaları okuyup testlerimi çalıştıracağım.

---

## 4. Algoritma (1 sa) — String

**Yöntem:**
1. Örnek girdiyi **elle** çöz, adımları chat'e yaz.
2. Ben "tamam" deyince kodla.
3. LeetCode'a submit et.

### A1 — LeetCode 557: Reverse Words in a String III (Easy)
Cümledeki **her kelimeyi kendi içinde** ters çevir; kelimelerin sırası ve aradaki tek boşluklar aynı kalsın.

**Elle çöz:** `s = "Let's take LeetCode contest"` → `"s'teL ekat edoCteeL tsetnoc"`
1. Cümleyi kelimelere ayır, kelimeleri alt alta yaz.
2. Her kelimenin tersini yanına yaz.
3. Tersleri aralarına boşluk koyarak birleştir.
Soru: Bir `String`'i ters çevirmek için hangi sınıfın hangi metodunu kullanırsın?

### A2 — LeetCode 1859: Sorting the Sentence (Easy)
Her kelimenin sonuna, cümledeki **sırası** (1–9) eklenmiş ve kelimeler karıştırılmış. Orijinal cümleyi geri kur.

**Elle çöz:** `s = "is2 sentence4 This1 a3"` → `"This is a sentence"`
1. Her kelime için `(son karakter, rakamsız kelime)` yaz: `is2` → `(2, "is")`
2. 4 kutulu bir dizi çiz, her kelimeyi doğru kutuya koy.
3. Kutuları boşlukla birleştir.
Soru: `'4'` karakterinden `4` sayısını (int) nasıl elde edersin?

---

## 5. Kapanış (15 dk)

- Isınma + tahmin sonuçlarından **en çok yanlış yaptığın 1 konuyu** chat'e yaz.
- `ILERLEME.md`'ye Gün 28 satırı (satırları ben hazırlayacağım).
- Commit + push:
  ```
  git add hafta-05
  git commit -m "feat: day 28 - review lambda + stream"
  git push
  ```
