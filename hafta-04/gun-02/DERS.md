# Hafta 4 · Gün 2 (Gün 20) — Stream API

**Hedef:** Dün `ListUtils.filter` / `map`'i elle yazdın: döngü + yeni liste + `test` / `apply`. Stream API bunun Java'daki hazır hâli: `list.stream().filter(...).map(...).toList()`. Bugün en çok kullanılan stream işlemlerini öğreniyorsun ve Gider Takip'in içindeki döngüleri stream'e çeviriyorsun.

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
Function<String, Integer> f = s -> s.length() * 2;
System.out.println(f.apply("hey"));
```

**S2.** `names.forEach(x -> System.out.println(x));` → `forEach` hangi functional interface'i istiyor, o interface'in metodunun adı ne?

**S3.** Hangisi method reference'a çevrilebilir? Çevrilebilene yaz, çevrilemeyene "olmaz" yaz.
```java
b -> b.getTitle()
s -> s.length() + 1
```

**S4.** Derlenir mi? Derlenmezse hangi satır?
```java
int total = 0;
List<Integer> nums = List.of(1, 2, 3);
nums.forEach(n -> total += n);
```

**S5.** Çıktı ne?
```java
Predicate<Integer> p = n -> n > 10;
System.out.println(p.test(5));
```

---

## 1. Stream = "listenin üstünden geçen bir boru hattı" ⭐⭐⭐ (10 dk)

Dünkü `ListUtils` ile "5'ten büyükleri al, 10 ile çarp":

```java
map(filter(List.of(3, 8, 1, 10, 6), n -> n > 5), n -> n * 10)   // [80, 100, 60]
```

Aynısı stream ile:

```java
List.of(3, 8, 1, 10, 6).stream()
        .filter(n -> n > 5)
        .map(n -> n * 10)
        .toList();                                               // [80, 100, 60]
```

Okuma sırası = çalışma sırası: **listeden akıt → süz → dönüştür → listeye topla.** İçeride yine döngü var, ama onu sen yazmıyorsun.

Her stream üç parçadan oluşur:

| Parça                | Örnek                       | Ne döner                     |
| -------------------- | --------------------------- | ---------------------------- |
| **Kaynak**           | `list.stream()`             | `Stream<T>`                  |
| **Ara işlem** (0..n) | `.filter(...)`, `.map(...)` | yine `Stream` (zincir sürer) |
| **Bitiş işlemi** (1) | `.toList()`, `.count()`     | sonuç (liste, sayı, …)       |

Bitiş işlemi yoksa **hiçbir şey çalışmaz**. Ara işlemler sadece tarif yazar (dünkü "lambda çalışmaz, saklanır" ile aynı fikir).

---

## 2. Ara işlemler ⭐⭐⭐ (15 dk)

| İşlem        | Ne alır          | Ne yapar                                  |
| ------------ | ---------------- | ----------------------------------------- |
| `filter(p)`  | `Predicate<T>`   | `p.test(x)` true olanlar geçer            |
| `map(f)`     | `Function<T, R>` | her elemanı `f.apply(x)` ile değiştirir   |
| `sorted()`   | —                | doğal sıra (sayı küçük→büyük, String A→Z) |
| `sorted(c)`  | `Comparator<T>`  | `c`'ye göre sıralar                       |
| `distinct()` | —                | tekrarları atar (`equals` ile)            |
| `limit(n)`   | `long`           | ilk `n` elemanı geçirir                   |

```java
List<String> names = List.of("Deniz", "Ali", "Bora", "Ce", "Ali");

names.stream().filter(n -> n.length() > 2).map(String::toUpperCase).toList();
// [DENIZ, ALI, BORA, ALI]

names.stream().distinct().sorted().toList();
// [Ali, Bora, Ce, Deniz]

names.stream().map(String::length).toList();
// [5, 3, 4, 2, 3]       ← map tipi değiştirebilir: Stream<String> → Stream<Integer>
```

Büyükten küçüğe: `sorted(Comparator.reverseOrder())`. Nesnelerde dünkü gibi: `sorted(Comparator.comparingDouble(Expense::getAmount).reversed())`.

---

## 3. Bitiş işlemleri ⭐⭐⭐ (15 dk)

| İşlem                               | Ne döner  | Not                                         |
| ----------------------------------- | --------- | ------------------------------------------- |
| `toList()`                          | `List<T>` | **değiştirilemez** liste (`add` → UOE)      |
| `count()`                           | `long`    | `int` değil, `long`                         |
| `forEach(c)`                        | `void`    | `Consumer<T>` alır                          |
| `anyMatch(p)`                       | `boolean` | **en az biri** uyuyor mu                    |
| `allMatch(p)`                       | `boolean` | **hepsi** uyuyor mu                         |
| `noneMatch(p)`                      | `boolean` | **hiçbiri** uymuyor mu                      |
| `collect(Collectors.joining(", "))` | `String`  | String'leri araya ayraç koyarak birleştirir |

**Toplam almak:** `Stream<Integer>`'ın `sum()`'ı yok. Önce sayı stream'ine çevir:

```java
List.of(5, 3, 8).stream().mapToInt(n -> n).sum();             // 16
expenses.stream().mapToDouble(Expense::getAmount).sum();      // double toplam
```

`mapToInt` / `mapToDouble` = "her elemandan bir sayı çıkar". Sonuç `IntStream` / `DoubleStream`; bunlarda `sum()` var.

```java
names.stream().collect(Collectors.joining(", "));   // "Deniz, Ali, Bora, Ce, Ali"
```

`joining` sadece String stream'inde çalışır. Nesne varsa önce `map(...)` ile String'e çevir.

---

## 4. `groupingBy`: say, topla, grupla ⭐⭐ (10 dk)

Exam2 ve LC1207'de elle yazdığın "sayma map'i"nin hazırı:

```java
import java.util.stream.Collectors;

Map<Integer, Long> byLength = names.stream()
        .collect(Collectors.groupingBy(String::length, Collectors.counting()));
// uzunluk → kaç isim:  {2=1, 3=2, 4=1, 5=1}
```

`groupingBy(anahtar, ne yapılsın)`:
- **1. argüman** (`Function`): her elemandan anahtarı çıkarır → map'in key'i
- **2. argüman**: aynı anahtara düşenlerle ne yapılacağı → map'in value'su

| 2. argüman                                     | Value                           |
| ---------------------------------------------- | ------------------------------- |
| *(yok)* `groupingBy(f)`                        | `List<T>`: o gruptaki elemanlar |
| `Collectors.counting()`                        | `Long`: kaç tane                |
| `Collectors.summingDouble(Expense::getAmount)` | `Double`: tutarların toplamı    |

**Sıra tuzağı:** `groupingBy` bir `HashMap` döndürür, key sırası garanti değil. Ekrana sıralı basmak için `TreeMap`'e sar:

```java
Map<Integer, Long> sortedMap = new TreeMap<>(byLength);   // key'ler küçükten büyüğe
```

---

## 5. Üç tuzak ⭐⭐ (5 dk)

1. **Stream kaynağı değiştirmez.** `names.stream().filter(...).toList()` yeni liste verir; `names` aynı kalır. (Dün `ExpenseTracker`'da kopya sildiğinde asıl liste değişmemişti, burada da aynı.)
2. **Stream tek kullanımlık.** Bir stream'de bitiş işlemi çalıştıktan sonra aynı stream'i tekrar kullanırsan → `IllegalStateException`. Her seferinde `list.stream()` ile yenisini aç.
3. **Stream içinden dışarıdaki değişkeni değiştirme.** `forEach(n -> total += n)` derlenmez (dünkü effectively final). Toplam lazımsa `mapToInt(...).sum()`.

---

## 6. Döngü mü stream mi? (5 dk)

Stream, "süz / dönüştür / topla / say" işleri için. Şu durumlarda döngü daha uygun:
- Index lazımsa (`nums[i]`, `nums[i-1]` gibi komşu elemanlar)
- Erken `return` / karmaşık `break` mantığı varsa
- Listeden **silmek** gerekiyorsa (`removeIf` veya `Iterator`)

Mülakatta "stream mi döngü mü?" sorulursa: **okunurluk**. Performans çoğu zaman aynı.

---

## 📝 Kağıda Yaz

```
list.stream() → ara işlemler → BİR bitiş işlemi (yoksa hiçbir şey çalışmaz)
Ara: filter(Predicate) map(Function) sorted() distinct() limit(n)
Bitiş: toList() count()=long forEach anyMatch/allMatch/noneMatch
Toplam: mapToInt(..).sum() / mapToDouble(..).sum()
String birleştir: collect(Collectors.joining(", "))
Sayma: collect(groupingBy(key, counting()))  → sıralı için new TreeMap<>(...)
Stream kaynağı DEĞİŞTİRMEZ · tek kullanımlık · toList() değiştirilemez
```

---

## 💪 EGZERSİZLER (~1 saat)

Klasör: `hafta-04/gun-02/w4d2/` · Paket: `package w4d2;` · Import: `java.util.*`, `java.util.function.*`, `java.util.stream.*`

### E1 — `StreamPredict.java` (tahmin)

`main`'e yaz. **Çalıştırmadan önce** 8 satırın çıktısını chat'e yaz, sonra çalıştır ve karşılaştır.

```java
List<Integer> nums = List.of(5, 3, 8, 3, 10, 1);

System.out.println(nums.stream().filter(n -> n > 3).toList());
System.out.println(nums.stream().map(n -> n * 2).toList());
System.out.println(nums.stream().distinct().sorted().toList());
System.out.println(nums.stream().filter(n -> n % 2 == 1).count());
System.out.println(nums.stream().mapToInt(n -> n).sum());
System.out.println(nums.stream().sorted(Comparator.reverseOrder()).limit(2).toList());
System.out.println(nums.stream().anyMatch(n -> n > 9) + " " + nums.stream().allMatch(n -> n > 1));
System.out.println(nums);
```

Sonra şu 3 satırı ekle. **Çalıştırmadan önce** chat'e: 1. `println` ne basar, 2. `println`'da ne olur?

```java
Stream<Integer> once = nums.stream();
System.out.println(once.count());
System.out.println(once.count());
```

### E2 — `LoopToStream.java` (kod yaz)

Aşağıda döngüyle yazılmış 3 metot var. Her birini **aynı class'a**, adının sonuna `Stream` ekleyerek, **döngüsüz** (sadece stream) yeniden yaz. Döngülü hâllerini de dosyada tut.

```java
static List<String> longNamesUpper(List<String> names) {
    List<String> result = new ArrayList<>();
    for (String n : names) {
        if (n.length() > 3) {
            result.add(n.toUpperCase());
        }
    }
    return result;
}

static int sumOfEvenSquares(List<Integer> nums) {
    int sum = 0;
    for (int n : nums) {
        if (n % 2 == 0) {
            sum += n * n;
        }
    }
    return sum;
}

static long countStartingWith(List<String> names, String prefix) {
    long count = 0;
    for (String n : names) {
        if (n.startsWith(prefix)) {
            count++;
        }
    }
    return count;
}
```

Yazacağın imzalar:
- `static List<String> longNamesUpperStream(List<String> names)`
- `static int sumOfEvenSquaresStream(List<Integer> nums)`
- `static long countStartingWithStream(List<String> names, String prefix)`

`main`'de **stream** hâllerini çalıştır. Beklenen çıktı yorumlarda:

```java
List<String> people = List.of("Ali", "Deniz", "Bora", "Ece", "Burak");
System.out.println(longNamesUpperStream(people));                     // [DENIZ, BORA, BURAK]
System.out.println(sumOfEvenSquaresStream(List.of(1, 2, 3, 4, 5, 6)));  // 56
System.out.println(countStartingWithStream(people, "B"));             // 2
System.out.println(countStartingWithStream(people, "Z"));             // 0
```

### E3 — `GroupingDemo.java` (kod yaz)

`main`'de bu listeyle 4 satır üret. Her biri **tek bir stream zinciri** olsun (map'leri yazdırırken `new TreeMap<>(...)` ile sar).

```java
List<String> words = List.of("apple", "avocado", "banana", "blueberry", "cherry", "apricot", "beet");
```

| # | Ne                                                   | Beklenen çıktı                                                                   |
| - | ---------------------------------------------------- | -------------------------------------------------------------------------------- |
| 1 | İlk harfe göre kaç kelime (`w.charAt(0)` anahtar)    | `{a=3, b=3, c=1}`                                                                |
| 2 | Uzunluğa göre kelimeler (2. argüman yok)             | `{4=[beet], 5=[apple], 6=[banana, cherry], 7=[avocado, apricot], 9=[blueberry]}` |
| 3 | `b` ile başlayanlar, dik çizgi ayraçlı (aşağıya bak) | aşağıya bak                                                                      |
| 4 | Bütün kelimelerin toplam harf sayısı                 | `44`                                                                             |

3. satır: ayraç `" | "` (boşluk + dik çizgi + boşluk, ters bölü YOK). Beklenen çıktı:

```
banana | blueberry | beet
```

Ek soru (chat'e, 1 cümle): 2. satırda `avocado` neden `apricot`'tan önce?

---

## 🏛️ PROJE 0 — Gider Takip v2 (~1 saat)

Dün bütün metotları döngüyle yazdın. Bugün: (1) mevcut metotların içini stream'e çevir, (2) stream'le yeni raporlar ekle.

### Adım 0 — Kopyala

`hafta-04/gun-01/w4d1/` içinden `Expense`, `ExpenseTracker`, `ExpenseApp`'i `hafta-04/gun-02/w4d2/` içine kopyala. Her dosyada `package w4d1;` → `package w4d2;`.

### `Expense`

Değişiklik **yok**.

### `ExpenseTracker` — mevcut metotlar

İmzalar ve davranış **aynı kalıyor**, sadece içleri değişiyor:

| Metot                               | Bugünkü kural                                                                    |
| ----------------------------------- | -------------------------------------------------------------------------------- |
| `add(Expense)`                      | değişmiyor                                                                       |
| `getAll()`                          | tek satır: `List.copyOf(expenses)` (aradaki kopya döngüsüne gerek yok)           |
| `filter(Predicate<Expense> c)`      | **döngü yok**, tek stream zinciri                                                |
| `sorted(Comparator<Expense> o)`     | **döngü yok**, tek stream zinciri (stream kaynağı değiştirmez → kopya derdi yok) |
| `total(Predicate<Expense> c)`       | **döngü yok**, tek stream zinciri (`mapToDouble`)                                |
| `removeWhere(Predicate<Expense> c)` | değişmiyor (silme = stream işi değil, Bölüm 6)                                   |

### `ExpenseTracker` — yeni metotlar (hepsi **döngüsüz**, stream ile)

| İmza                                            | Ne döndürür                                                                                             |
| ----------------------------------------------- | ------------------------------------------------------------------------------------------------------- |
| `public List<String> categories()`              | Kategoriler, tekrarsız, A→Z. Örn: `[Food, Home, Transport]`                                             |
| `public Map<String, Long> countByCategory()`    | Kategori → gider sayısı. Key'ler A→Z sıralı olsun (`TreeMap`)                                           |
| `public Map<String, Double> totalsByCategory()` | Kategori → tutar toplamı. Key'ler A→Z sıralı olsun (`TreeMap`)                                          |
| `public List<Expense> top(int n)`               | Tutarı en büyük `n` gider, büyükten küçüğe. `n <= 0` → `IllegalArgumentException("N must be positive")` |
| `public boolean hasExpenseOver(double limit)`   | Tutarı `limit`'ten büyük **en az bir** gider var mı                                                     |
| `public String descriptions(String category)`   | O kategorideki giderlerin açıklamaları, eklenme sırasıyla, `", "` ile. Hiç yoksa `""`                   |

### `ExpenseApp` (`main`) — baştan yaz

1. Dünkü 6 gideri aynı sırayla ekle, **sonuna** 7. olarak: `Taxi`, `Transport`, `230`
2. `Predicate<Expense> isFood` (dünkü gibi)
3. `Food:` → `isFood` ile filtrele, her birini ayrı satırda yazdır
4. `Categories: ` + `categories()`
5. `Count by category: ` + `countByCategory()`
6. `Totals by category: ` + `totalsByCategory()`
7. `Top 3:` → `top(3)`, her birini ayrı satırda yazdır
8. `Food items: ` + `descriptions("Food")`
9. `Any over 10000: ` + `hasExpenseOver(10000)`
10. `Any over 20000: ` + `hasExpenseOver(20000)`
11. `Total: ` + bütün giderlerin toplamı (`total(...)` ile; "hepsi" diyen bir lambda yaz)
12. `top(0)` dene → yakala → `Error: <mesaj>`

**Beklenen çıktı:**
```
Food:
Coffee (Food) 45.5
Groceries (Food) 640.75
Gum (Food) 12.5
Categories: [Food, Home, Transport]
Count by category: {Food=3, Home=2, Transport=2}
Totals by category: {Food=698.75, Home=12450.0, Transport=245.0}
Top 3:
Rent (Home) 12000.0
Groceries (Food) 640.75
Internet (Home) 450.0
Food items: Coffee, Groceries, Gum
Any over 10000: true
Any over 20000: false
Total: 13393.75
Error: N must be positive
```

### Proje sonu düşünme soruları (chat'e, 1-2 cümle)

1. Dünkü `sorted(...)`'da önce kopya alman gerekiyordu. Bugünkü stream'li `sorted(...)`'da neden gerekmedi?
2. `top(3)` yazarken `.limit(3)`'ü `.sorted(...)`'dan **önce** yazsaydın çıktı ne olurdu? (Somut: ilk eklenen 3 gider hangileri?)

---

## 🧩 ALGORİTMA SAATİ (45 dk) — Sayma, bu sefer stream ile

İkisinde de sayma map'ini **`groupingBy` + `counting()`** ile kur. `int[]`'i stream'e çevirmek için: `Arrays.stream(arr).boxed()` (`boxed` = `int` → `Integer`, çünkü `groupingBy` nesne ister). `String[]` için sadece `Arrays.stream(arr)`.

**A1 — [LeetCode 1394: Find Lucky Integer in an Array](https://leetcode.com/problems/find-lucky-integer-in-an-array/)**
`int findLucky(int[] arr)`: bir sayı dizide **kendi değeri kadar** geçiyorsa "şanslı". En büyük şanslı sayıyı döndür, yoksa `-1`.
- Örnekler: `[2,2,3,4]` → `2` · `[1,2,2,3,3,3]` → `3` · `[2,2,2,3,3]` → `-1`
- Kodlamadan önce chat'e: `[1,2,2,3,3,3]` için sayma map'i ne? Hangi key'ler şanslı?
- Map'i kurduktan sonra cevabı döngüyle bulman serbest. Stream ile istersen kalıp: `.mapToInt(...).max().orElse(-1)` (`orElse` = "hiç eleman yoksa bunu ver"; yarın Optional'da açılacak).

**A2 — [LeetCode 2085: Count Common Words With One Occurrence](https://leetcode.com/problems/count-common-words-with-one-occurrence/)**
`int countWords(String[] words1, String[] words2)`: **iki dizide de tam 1 kez** geçen kelimelerin sayısı.
- Örnek: `words1 = ["leetcode","is","amazing","as","is"]`, `words2 = ["amazing","leetcode","is"]` → `2`
- Kodlamadan önce chat'e: örnekte `"is"` neden sayılmıyor, `"as"` neden sayılmıyor?
- İki ayrı sayma map'i kur. `words2`'de olmayan kelime için `get` → `null` (Exam B6'daki NPE!). `getOrDefault(k, 0L)` kullan.
- Dönüş tipi `int`, `count()` `long` döner → `(int)` ile çevir.

---

## 🔧 GIT (10 dk)

```bash
git add hafta-04/gun-02
git status            # sadece .java ve DERS.md (.class yok)
git commit -m "feat: week 4 day 2 - stream api"
git push
```
Sonra `ILERLEME.md`'ye Gün 20 logu → `docs: day 20 progress log`.

---

## ✅ Gün Sonu

- [ ] Isınma S1–S5
- [ ] Bölüm 1–6 okundu
- [ ] E1 (tahminler **çalıştırmadan önce** chat'e)
- [ ] E2 `LoopToStream`, E3 `GroupingDemo`
- [ ] Proje: `ExpenseTracker` stream'e çevrildi + 6 yeni metot, `ExpenseApp` çıktısı birebir aynı
- [ ] Proje düşünme soruları
- [ ] A1 + A2
- [ ] "Kağıda Yaz" kutusu kağıtta
- [ ] Commit + push, `ILERLEME.md` Gün 20

**Yarın oturum başında soracaklarım:** 4-5 kısa soru (kod örnekli): stream zinciri çıktısı, ara/bitiş işlemi ayrımı, `groupingBy` sonucu, döngüyü stream'e çevirme.

**Sıradaki:** Hafta 4 Gün 3 (Gün 21): **Optional, record, enum**. Gider Takip'te `Expense` record'a, kategori enum'a dönüşüyor.
