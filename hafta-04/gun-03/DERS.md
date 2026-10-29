# Hafta 4 · Gün 3 (Gün 21) — Optional, enum, record

**Hedef:** Üç küçük ama her Spring projesinde karşına çıkacak araç:
- **Optional** → "sonuç olmayabilir"i `null` yerine tipte söylemek (`findById` bunu döndürür)
- **enum** → sabit, sınırlı seçenekler (`"Food"` String'i yerine `Category.FOOD`)
- **record** → dünkü 40 satırlık `Expense` class'ının tek satırlık hâli (DTO'lar böyle yazılır)

Gider Takip v3'te `Expense` record'a, kategori enum'a dönüşüyor; "bulunamadı" durumları Optional dönüyor.

**Süre:** ~4.5 saat · Isınma 15 dk · Ders 1 sa · Egzersiz 1 sa · Proje 1 sa · Algoritma 45 dk · Kapanış 15 dk

**Kurallar (değişmedi):**
- Sınıf, metot, değişken ve ekran mesajları **İngilizce**
- **Değişiklik → derle → çalıştır → çıktının her satırını oku.**
- **Metot adı/imzası görevde nasılsa öyle.**
- Tahminler **çalıştırmadan önce** chat'e.
- Testleri Claude yazıp çalıştırıyor. Sen yazmıyorsun.

---

## 0. Isınma — dünden 5 soru (chat'e, kısa)

**S1.** Çıktı ne?
```java
System.out.println(List.of(4, 9, 2, 7).stream().filter(n -> n > 3).map(n -> n + 1).toList());
```

**S2.** `Expense`'in `getDescription()` ve `getAmount()` metotları var. Bu kod derlenir mi? Derlenmezse: neden, ve hangi satırı nasıl değiştirirsin?
```java
List<Expense> big = expenses.stream()
        .map(Expense::getAmount)
        .filter(a -> a > 100)
        .toList();
```

**S3.** Çıktı ne? (HashMap sırası bu örnekte küçükten büyüğe çıkıyor.)
```java
System.out.println(List.of("aa", "b", "cc", "d", "eee").stream()
        .collect(Collectors.groupingBy(String::length, Collectors.counting())));
```

**S4.** Derlenir mi? Derlenmezse neden?
```java
int c = List.of(1, 2, 3).stream().count();
```

**S5.** Ne olur?
```java
List<Integer> r = List.of(3, 1, 2).stream().sorted().toList();
r.add(4);
```

---

## 1. Optional — "sonuç olmayabilir" ⭐⭐⭐ (20 dk)

Dünkü A1'de şunu gördün: `.max().orElse(-1)`. `max()` neden direkt sayı döndürmedi? Çünkü **liste boşsa max yok.** Java bunu `null` döndürmek yerine bir kutuyla söylüyor:

```
Optional<T>  =  içinde 0 ya da 1 eleman olan kutu
Optional[Bora]      ← dolu kutu
Optional.empty      ← boş kutu
```

**Neden `null` değil?** `null` döndüren metodu çağıran, kontrol etmeyi unutur → NPE (Exam B6). `Optional<Expense>` dönüş tipi ise çağırana **zorla** "boş olabilir, ne yapacağına karar ver" der.

### Optional nereden gelir?

| Nereden                                      | Ne zaman boş            |
| -------------------------------------------- | ----------------------- |
| `stream.findFirst()`                         | stream boşsa            |
| `stream.max(comparator)` / `min(...)`        | stream boşsa            |
| `Optional.of(x)`                             | asla (`x` null ise NPE) |
| `Optional.ofNullable(x)`                     | `x` null ise            |
| `Optional.empty()`                           | her zaman               |
| Spring'de `repository.findById(id)` (ilerde) | o id'li kayıt yoksa     |

### Kutuyu açmak

| Metot           | Doluysa                      | Boşsa                              |
| --------------- | ---------------------------- | ---------------------------------- |
| `isPresent()`   | `true`                       | `false`                            |
| `isEmpty()`     | `false`                      | `true`                             |
| `orElse(v)`     | içindeki değer               | `v`                                |
| `orElseThrow()` | içindeki değer               | `NoSuchElementException`           |
| `map(f)`        | `Optional` içinde `f(değer)` | yine boş `Optional` (`f` çalışmaz) |
| `ifPresent(c)`  | `c`'yi değerle çalıştırır    | hiçbir şey yapmaz                  |

```java
List<String> names = List.of("Deniz", "Ali", "Bora");

Optional<String> b = names.stream().filter(n -> n.startsWith("B")).findFirst();   // Optional[Bora]
Optional<String> z = names.stream().filter(n -> n.startsWith("Z")).findFirst();   // Optional.empty

b.orElse("none");                        // "Bora"
z.orElse("none");                        // "none"
b.map(String::length).orElse(0);         // 4    ← Optional<String> → Optional<Integer> → 4
z.map(String::length).orElse(0);         // 0    ← map boşta çalışmadı, orElse devreye girdi
```

`map` burada stream'deki `map` ile aynı fikir: **kutunun içindekini dönüştür.** Kutu boşsa dönüştürecek bir şey yok.

### Optional kuralları

1. **Sadece metot dönüş tipinde kullan.** Field ya da parametre olarak `Optional` yazma.
2. **`get()` kullanma.** Boşsa patlar ve neden patladığını söylemez. `orElse` / `orElseThrow` / `map` kullan.
3. **`if (opt.isPresent()) { opt.get() ... }` yazıyorsan** neredeyse her zaman `map(...).orElse(...)` ya da `ifPresent(...)` daha kısa.

---

## 2. enum — sınırlı seçenekler ⭐⭐⭐ (20 dk)

Şu an kategori bir `String`. Şunlar derleniyor ve hepsi hata:

```java
new Expense("Tea", "Fod", 10);      // yazım hatası
new Expense("Tea", "food", 10);     // büyük/küçük harf → isFood bunu kaçırır
```

**enum = olası değerlerin sabit listesi.** Listede olmayan bir değer **derlenmez**.

```java
public enum Category {
    FOOD, HOME, TRANSPORT
}

Category c = Category.FOOD;
Category x = Category.FOD;          // ❌ derleme hatası
```

### enum'da hazır gelenler

| Ne                               | Örnek                      | Sonuç                                        |
| -------------------------------- | -------------------------- | -------------------------------------------- |
| `toString()` / `name()`          | `Category.HOME.toString()` | `"HOME"`                                     |
| `values()`                       | `Category.values()`        | `[FOOD, HOME, TRANSPORT]` (dizi, bu sırayla) |
| `valueOf(String)`                | `Category.valueOf("HOME")` | `Category.HOME`                              |
| `valueOf` yanlış isimle          | `Category.valueOf("Home")` | `IllegalArgumentException`                   |
| Karşılaştırma                    | `c == Category.FOOD`       | `true` / `false`                             |
| Doğal sıra (`sorted`, `TreeMap`) | —                          | **tanımlanma sırası** (alfabe değil)         |

**Neden `==` güvenli?** Her enum sabitinden JVM'de **tek bir nesne** var. `Category.FOOD` her yerde aynı adres. (String'de `==` riskliydi çünkü aynı içerikli iki ayrı String nesnesi olabilir — Gün 5.)

### Alanlı enum

enum aslında bir class: field, constructor, metot alabilir. Constructor'ı sen çağırmazsın; sabitin yanındaki parantez onu çağırır.

```java
public enum Size {
    SMALL("S", 200),
    MEDIUM("M", 300),
    LARGE("L", 400);          // ← sabitlerden sonra noktalı virgül

    private final String code;
    private final int ml;

    Size(String code, int ml) {          // private sayılır, `new Size(...)` yazılamaz
        this.code = code;
        this.ml = ml;
    }

    public String getCode() { return code; }
    public int getMl() { return ml; }
}

Size.LARGE.getMl();      // 400
```

### enum + switch

```java
static String describe(Size s) {
    return switch (s) {
        case SMALL -> "tiny";
        case MEDIUM -> "normal";
        case LARGE -> "huge";
    };
}
```

`switch` ifade olarak (`return switch ...`, `->` ile) yazılınca: `break` yok, her `case` bir değer döndürür. Bütün sabitleri yazdıysan `default` gerekmez; birini unutursan **derlenmez**. (Enum'a yeni sabit eklediğinde unuttuğun yeri derleyici gösterir.)

Bir enum'un içinde `this` = o anki sabit, yani `switch (this)` da yazılabilir.

---

## 3. record — veri taşıyan class'ın kısa yolu ⭐⭐⭐ (15 dk)

Dünkü `Expense`: 3 `private final` field + constructor + 3 getter + `toString`. Aynısı:

```java
public record Expense(String description, String category, double amount) { }
```

Bu tek satır şunları **senin yerine** yazar:

| Ne                        | Nasıl                                                       |
| ------------------------- | ----------------------------------------------------------- |
| `private final` field'lar | `description`, `category`, `amount`                         |
| Constructor               | `new Expense("Tea", "Food", 10)`                            |
| Erişim metotları          | `e.description()`, `e.amount()` — **`get` öneki yok**       |
| `equals` + `hashCode`     | bütün alanlar eşitse eşit (Hafta 3 Gün 3'te elle yazmıştın) |
| `toString`                | `Expense[description=Tea, category=Food, amount=10.0]`      |

**Setter yok.** Record değiştirilemez (immutable); değiştirmek istersen yeni bir record oluşturursun.

### Doğrulama: compact constructor

```java
public record Point(int x, int y) {
    public Point {                                    // ← parantez yok, parametre yok
        if (x < 0) {
            throw new IllegalArgumentException("X must not be negative");
        }
        // this.x = x;  YAZMA — atamayı record kendisi yapar, bu bloktan sonra
    }

    public int sum() {                                // metot eklemek serbest
        return x + y;
    }
}
```

`toString` beğenmediysen `@Override public String toString()` ile kendin yazabilirsin.

**Record ne zaman?** "Sadece veri taşıyor, değişmiyor" dediğin her şey: DTO, API cevabı, koordinat, gider kaydı. **Ne zaman değil?** Durumu değişen nesneler (dünkü `ExpenseTracker`, içindeki listeye ekleme yapılıyor).

---

## 4. Üç tuzak ⭐⭐ (5 dk)

1. **Record'da getter adı alan adıyla aynı:** `e.amount()`, `e.getAmount()` değil. Method reference de değişir: `Expense::amount`.
2. **`valueOf` birebir isim ister:** `Category.valueOf("food")` → `IllegalArgumentException`. Kullanıcıdan gelen metinde önce `.toUpperCase()`.
3. **`orElseThrow()` boşta patlar:** "Kesin doludur" dediğin yerde kullan; emin değilsen `orElse` ya da `map`.

---

## 📝 Kağıda Yaz

```
Optional<T> = 0 ya da 1 elemanlı kutu. findFirst/max/min döndürür.
  orElse(v)  orElseThrow()  map(f)  ifPresent(c)  isPresent/isEmpty
  Sadece DÖNÜŞ tipinde. get() kullanma.
enum: sabit liste. == ile karşılaştır. values() valueOf("AD") name()
  Alanlı: FOOD("Food"); + private final field + constructor
  Sıralama = tanımlanma sırası. switch → bütün case'ler, default yok
record Expense(String description, double amount) { }
  → final field + constructor + description() + equals/hashCode/toString
  Doğrulama: public Expense { if (...) throw ...; }   (atama yok)
```

---

## 💪 EGZERSİZLER (~1 saat)

Klasör: `hafta-04/gun-03/w4d3/` · Paket: `package w4d3;` · Import: `java.util.*`, `java.util.stream.*`

### E1 — `OptionalPredict.java` (tahmin)

`main`'e yaz. **Çalıştırmadan önce** 9 satırın çıktısını chat'e yaz (son satır için: "ne olur?"), sonra çalıştır ve karşılaştır.

```java
List<String> names = List.of("Deniz", "Ali", "Bora", "Ece");
Optional<String> b = names.stream().filter(n -> n.startsWith("B")).findFirst();
Optional<String> z = names.stream().filter(n -> n.startsWith("Z")).findFirst();

System.out.println(b);
System.out.println(z);
System.out.println(b.isPresent() + " " + z.isEmpty());
System.out.println(b.orElse("none") + " " + z.orElse("none"));
System.out.println(b.map(String::length).orElse(0) + " " + z.map(String::length).orElse(0));
b.ifPresent(n -> System.out.println("Found " + n));
z.ifPresent(n -> System.out.println("Found " + n));
System.out.println(names.stream().max(Comparator.comparingInt(String::length)).orElseThrow());
System.out.println(z.orElseThrow());
```

### E2 — `TrafficLight.java` + `LightDemo.java` (kod yaz)

**`TrafficLight`** (enum, kendi dosyasında):
- Sabitler, bu sırayla: `RED` (30 saniye), `YELLOW` (5 saniye), `GREEN` (25 saniye)
- Field: `private final int seconds`
- Constructor: `TrafficLight(int seconds)`
- `public int getSeconds()`
- `public TrafficLight next()`: sıradaki ışık. Kural: `RED → GREEN`, `GREEN → YELLOW`, `YELLOW → RED`. Nasıl yazacağın (switch ya da başka yol) **senin tasarım kararın**.

**`LightDemo`** (`main`):
1. `values()` üzerinde dön, her ışık için: `<ad> <saniye>s -> <sonraki>`
2. `Cycle: ` + bütün ışıkların saniye toplamı + `s` (döngü ya da stream, serbest)
3. `valueOf("GREEN")`'in `next()`'ini yazdır

**Beklenen çıktı:**
```
RED 30s -> GREEN
YELLOW 5s -> RED
GREEN 25s -> YELLOW
Cycle: 60s
YELLOW
```

### E3 — `Point.java` + `PointDemo.java` (kod yaz + tahmin)

**`Point`** (record, kendi dosyasında):
- Alanlar: `int x`, `int y`
- Compact constructor: `x` ya da `y` negatifse → `IllegalArgumentException("Coordinates must not be negative")`
- `public int manhattan(Point other)`: `|x1 - x2| + |y1 - y2|` (`Math.abs`)

**`PointDemo`** (`main`) — önce yaz, **çalıştırmadan önce** 6 satırın çıktısını chat'e yaz:

```java
Point p = new Point(1, 2);
Point q = new Point(1, 2);
Point r = new Point(4, 6);

System.out.println(p);
System.out.println(p.equals(q) + " " + (p == q));
System.out.println(p.manhattan(r));
System.out.println(r.x());
System.out.println(new HashSet<>(List.of(p, r)).contains(q));
```

Sonra `new Point(-1, 0)`'ı try/catch ile dene, `Error: <mesaj>` yazdır (6. satır).

---

## 🏛️ PROJE 0 — Gider Takip v3 (~1 saat)

### Adım 0 — Kopyala

`hafta-04/gun-02/w4d2/` içinden `ExpenseTracker` ve `ExpenseApp`'i `hafta-04/gun-03/w4d3/` içine kopyala, `package w4d3;` yap. `Expense`'i **kopyalama**, sıfırdan record olarak yazacaksın.

### `Category` (enum, yeni dosya)

- Sabitler, bu sırayla: `FOOD("Food")`, `HOME("Home")`, `TRANSPORT("Transport")`
- Field: `private final String label`
- Constructor: `Category(String label)`
- `public String getLabel()`
- `toString`'i **override etme** (`Category.FOOD` ekrana `FOOD` yazsın)

### `Expense` (record, dosyayı sıfırdan yaz)

- Alanlar, bu sırayla: `String description`, `Category category`, `double amount`
- Compact constructor, bu sırayla kontrol:
  - `description` null ya da boş → `IllegalArgumentException("Description must not be blank")`
  - `category` null → `IllegalArgumentException("Category must not be null")`
  - `amount <= 0` → `IllegalArgumentException("Amount must be positive")`
- `toString` override: dünkü format, kategori **label** ile → `Coffee (Food) 45.5`

### `ExpenseTracker` — mevcut metotlar

Record'a geçince `Expense::getAmount` gibi her şey kırılacak. **Derleyici hatalarını tek tek düzelt** (getter adları değişti). Bunun dışında:

| Metot                                                               | Değişiklik                                                    |
| ------------------------------------------------------------------- | ------------------------------------------------------------- |
| `add`, `getAll`, `filter`, `sorted`, `total`, `removeWhere`         | sadece getter adları                                          |
| `top(int n)`, `hasExpenseOver(double limit)`                        | sadece getter adları                                          |
| `categories()`                                                      | dönüş tipi `List<Category>`. Sıra: **enum sırası**, tekrarsız |
| `countByCategory()`                                                 | dönüş tipi `Map<Category, Long>`, `TreeMap` (enum sırası)     |
| `totalsByCategory()`                                                | dönüş tipi `Map<Category, Double>`, `TreeMap` (enum sırası)   |
| `descriptions(String category)` → `descriptions(Category category)` | parametre tipi değişti; karşılaştırma `==` ile                |

### `ExpenseTracker` — yeni metotlar (döngüsüz, stream ile)

| İmza                                                             | Ne döndürür                                                                                                  |
| ---------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------ |
| `public Optional<Expense> mostExpensive()`                       | Tutarı en büyük gider. Tracker boşsa boş `Optional`. (`max(...)`)                                            |
| `public Optional<Expense> findByDescription(String description)` | Açıklaması eşleşen **ilk** gider, **büyük/küçük harf fark etmez** (`equalsIgnoreCase`). Yoksa boş `Optional` |

### `ExpenseApp` (`main`) — baştan yaz

1. Dünkü 7 gideri aynı sırayla ekle, kategoriler artık `Category.FOOD` vb.
2. `Predicate<Expense> isFood` → bu sefer `==` ile
3. `Food:` → `isFood` ile filtrele, her birini ayrı satırda yazdır
4. `Categories: ` + `categories()`
5. `Labels: ` + `Category.values()`'in label'ları, `", "` ile, **tek stream zinciri** (dizi → stream: `Arrays.stream(...)`)
6. `Count by category: ` + `countByCategory()`
7. `Totals by category: ` + `totalsByCategory()`
8. `Food items: ` + `descriptions(Category.FOOD)`
9. `Most expensive: ` + en pahalının **sadece açıklaması**; yoksa `none` (`map` + `orElse`, `if` yok)
10. `Empty tracker most expensive: ` → yeni boş bir `ExpenseTracker` ile 9'un aynısı
11. `Gum price: ` + `findByDescription("gum")` → tutarı; yoksa `0.0` (`map` + `orElse`)
12. `Pizza price: ` + `findByDescription("Pizza")` ile 11'in aynısı
13. `Same gum: ` + yeni oluşturduğun `new Expense("Gum", Category.FOOD, 12.5)` ile `findByDescription("Gum").orElseThrow()` `equals` mı?
14. `Total: ` + bütün giderlerin toplamı (`total(...)`)
15. `new Expense("Tea", null, 10)` dene → yakala → `Error: <mesaj>`
16. `Category.valueOf("Pizza")` dene → yakala → `Error: <mesaj>`

**Beklenen çıktı:**
```
Food:
Coffee (Food) 45.5
Groceries (Food) 640.75
Gum (Food) 12.5
Categories: [FOOD, HOME, TRANSPORT]
Labels: Food, Home, Transport
Count by category: {FOOD=3, HOME=2, TRANSPORT=2}
Totals by category: {FOOD=698.75, HOME=12450.0, TRANSPORT=245.0}
Food items: Coffee, Groceries, Gum
Most expensive: Rent
Empty tracker most expensive: none
Gum price: 12.5
Pizza price: 0.0
Same gum: true
Total: 13393.75
Error: Category must not be null
Error: No enum constant w4d3.Category.Pizza
```

### Proje sonu düşünme soruları (chat'e, 1-2 cümle)

1. 13. satır `true` çıktı. Dünkü `Expense` class'ıyla aynı satırı yazsaydın ne çıkardı, neden?
2. `mostExpensive()` neden `Expense` değil de `Optional<Expense>` döndürüyor? `Expense` döndürseydi, 10. satırda (boş tracker) metodun içinden ne dönerdi ve 9. satırdaki kod nerede patlardı?

---

## 🧩 ALGORİTMA SAATİ (45 dk) — Sayma, devam

**A1 — [LeetCode 1748: Sum of Unique Elements](https://leetcode.com/problems/sum-of-unique-elements/)**
`int sumOfUnique(int[] nums)`: dizide **tam 1 kez** geçen sayıların toplamı.
- Örnekler: `[1,2,3,2]` → `4` · `[1,1,1,1,1]` → `0` · `[1,2,3,4,5]` → `15`
- Kodlamadan önce chat'e: `[1,2,3,2]` için sayma map'i ne? Hangi key'ler toplama girer?
- Yol serbest: dünkü gibi `groupingBy` + `counting()` ya da sayma dizisi (kısıt: `1 <= nums[i] <= 100`).

**A2 — [LeetCode 1512: Number of Good Pairs](https://leetcode.com/problems/number-of-good-pairs/)**
`int numIdenticalPairs(int[] nums)`: `nums[i] == nums[j]` ve `i < j` olan `(i, j)` çiftlerinin sayısı.
- Örnek: `[1,2,3,1,1,3]` → `4` · `[1,1,1,1]` → `6` · `[1,2,3]` → `0`
- Önce iç içe iki döngüyle çöz, gönder.
- Sonra chat'e: `[1,1,1,1]`'de dört tane `1` var ve cevap `6`. Bir sayı `k` kez geçiyorsa kaç çift verir? (`k=2`, `k=3`, `k=4` için say, formülü bul.) Formülü bulunca sayma ile tek geçişte yeniden yaz, tekrar gönder.

---

## 🔧 GIT (10 dk)

```bash
git add hafta-04/gun-03
git status            # sadece .java ve DERS.md (.class yok)
git commit -m "feat: week 4 day 3 - optional, enum, record"
git push
```
Sonra `ILERLEME.md`'ye Gün 21 logu → `docs: day 21 progress log`.

---

## ✅ Gün Sonu

- [ ] Isınma S1–S5
- [ ] Bölüm 1–4 okundu
- [ ] E1 (tahminler **çalıştırmadan önce** chat'e)
- [ ] E2 `TrafficLight`, E3 `Point` (+ tahminler)
- [ ] Proje: `Category` enum, `Expense` record, tracker güncel + 2 Optional metot, `ExpenseApp` çıktısı birebir aynı
- [ ] Proje düşünme soruları
- [ ] A1 + A2 (A2 iki yolla)
- [ ] "Kağıda Yaz" kutusu kağıtta
- [ ] Commit + push, `ILERLEME.md` Gün 21

**Yarın oturum başında soracaklarım:** 4-5 kısa soru (kod örnekli): Optional zinciri çıktısı, record'un ürettiği metotlar, enum `valueOf` / `==`, compact constructor.

**Sıradaki:** Hafta 4 Gün 4 (Gün 22): **Dosya I/O + `java.time`**. Gider Takip giderleri dosyaya kaydedip geri okuyacak, her gidere tarih eklenecek.
