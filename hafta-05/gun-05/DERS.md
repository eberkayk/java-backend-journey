# Hafta 5 · Gün 5 (Gün 29) — TEKRAR: Optional + record + enum + java.time + Dosya I/O + Exception isimleri

**Hedef:** Yeni konu yok. Hafta 4'ün "küçük araçlarını" **notsuz** kullanabilmek ve **çıktılarını doğru yazabilmek**.
En çok kaçanlar: `Optional`'ı yazdırmak (`Optional[Ada]`), record `toString`'i (`Item[name=pen, qty=2]`), `LocalDate`'in sıfırlı formatı (`2026-01-05`) ve "bu satır hangi exception'ı fırlatır?".
Kapı sınavında (Gün 31) bugünkü konular ~20 puan.

**Süre:** ~5-6 saat
Isınma 15 dk · Konu özeti 30 dk · Çıktı tahmini 25 dk · Notsuz kod görevi 1.5–2 sa · Algoritma 1 sa · Kapanış 15 dk

**Kurallar (değişmedi):**
- Sınıf, metot, değişken ve mesajlar **İngilizce**
- **Değişiklik → derle → çalıştır → çıktının her satırını oku.**
- **Metot adı/imzası görevde nasılsa öyle.** Exception mesajları **birebir**.
- `main`'e test yazmak yok. Testleri ben yazıp çalıştırıyorum.

**Kurulum:** `hafta-05/gun-05/` klasöründe package **`w5d5`** (`hafta-05/gun-05/w5d5/`). `gun-05`'i Sources Root yap.

---

## 0. Isınma (15 dk) — cevapları chat'e yaz

Dünün kaçanları: List yazdırma, stream'in kaynağı değiştirmemesi, bitiş adımı olmadan stream'in çalışmaması.

**S1.** Çıktı ne? (2 satır)
```java
record Pt(int x, int y) {}
List<Pt> ps = new ArrayList<>(List.of(new Pt(1, 2), new Pt(3, 4)));
List<Integer> xs = ps.stream().map(Pt::x).toList();
System.out.println(ps);
System.out.println(xs);
```

**S2.** Çıktı ne? (Kaç satır olduğunu da düşün.)
```java
List<String> w = List.of("bob", "al", "bob");
w.stream().map(s -> {
    System.out.println("map " + s);
    return s.length();
});
System.out.println(w.stream().map(String::length).distinct().toList());
```

---

## 1. Konu Özeti (30 dk) — oku, takıldığın yeri chat'e sor

### 1.1 Optional — "sonuç olmayabilir"

```java
Optional<String> a = Optional.of("Ada");        // içinde değer var
Optional<String> b = Optional.empty();          // boş
Optional<String> c = Optional.ofNullable(x);    // x null ise boş, değilse dolu
Optional.of(null);                              // ❌ NullPointerException
```

**Yazdırma (5 kez kaçtı, ezberle):**

| Kod                        | Çıktı              |
| -------------------------- | ------------------ |
| `println(a)`               | `Optional[Ada]`    |
| `println(b)`               | `Optional.empty`   |
| `println(a.get())`         | `Ada`              |
| `println(a.orElse("x"))`   | `Ada`              |
| `println(b.orElse("x"))`   | `x`                |

**Metotlar — ne döner:**

| Metot                          | Dolu ise             | Boş ise                          |
| ------------------------------ | -------------------- | -------------------------------- |
| `isPresent()` / `isEmpty()`    | `true` / `false`     | `false` / `true`                 |
| `get()` / `orElseThrow()`      | değer                | `NoSuchElementException`         |
| `orElse(v)`                    | değer                | `v`                              |
| `orElseThrow(() -> new X(..))` | değer                | senin exception'ın (`X`)         |
| `map(f)`                       | `Optional[f(değer)]` | `Optional.empty`                 |
| `ifPresent(c)`                 | `c` çalışır          | hiçbir şey olmaz                 |

⚠️ `map` sonucu **yine Optional**: `Optional.of("Ada").map(String::length)` → `Optional[3]`, `3` değil.

### 1.2 record — veri taşıyan değişmez class

```java
record Item(String name, int qty) {
    Item {                                            // compact constructor: parantez YOK
        if (qty < 0) throw new IllegalArgumentException("Bad qty: " + qty);
    }
    int doubled() { return qty * 2; }                 // kendi metodunu ekleyebilirsin
}
```

Derleyici senin için yazar:
- `private final` field'lar (setter **yok**)
- erişim metotları: `name()`, `qty()` — **`getName()` değil**
- `equals` / `hashCode` → **değerlere** bakar (iki `new Item("pen", 2)` → `equals` true, `==` false)
- `toString` → **`Item[name=pen, qty=2]`** — köşeli parantez + `alan=değer` + virgül-boşluk

### 1.3 enum — sınırlı, sabit seçenekler

```java
enum Size {
    SMALL(10), LARGE(30);                 // sabitler EN ÜSTTE, sonunda ;
    private final int price;
    Size(int price) { this.price = price; }   // constructor'ı sabitin parantezi çağırır
    int price() { return price; }
}
```

| Kod                          | Sonuç                                      |
| ---------------------------- | ------------------------------------------ |
| `println(Size.LARGE)`        | `LARGE` (toString = adı)                   |
| `Size.LARGE.name()`          | `"LARGE"`                                  |
| `Size.LARGE.ordinal()`       | `1` (sıra, 0'dan başlar)                   |
| `Size.values()`              | `[SMALL, LARGE]` dizisi (tanım sırası)     |
| `Size.valueOf("SMALL")`      | `Size.SMALL`                               |
| `Size.valueOf("small")`      | `IllegalArgumentException` (büyük-küçük)   |
| `a == b`                     | güvenli (her sabitten tek nesne var)       |
| `TreeMap<Size, ..>` sırası   | **tanım sırası** (alfabetik değil)         |

`switch` ifadesi — bütün sabitleri yazarsan `default` gerekmez:
```java
String s = switch (size) {
    case SMALL -> "s";
    case LARGE -> "l";
};
```

### 1.4 java.time — LocalDate

```java
LocalDate d = LocalDate.of(2026, 1, 5);
System.out.println(d);                  // 2026-01-05   ← yıl-ay-gün, SIFIRLI
LocalDate.parse("2026-01-05");          // String → LocalDate (format birebir bu olmalı)
```

| Kod                                       | Sonuç                                   |
| ----------------------------------------- | --------------------------------------- |
| `d.plusDays(3)`                           | **yeni** tarih döner, `d` değişmez      |
| `d.plusMonths(1)` (31 Ocak'tan)           | `2026-02-28` (ayın son gününe yuvarlar) |
| `a.isBefore(b)` / `a.isAfter(b)`          | `boolean`                               |
| `ChronoUnit.DAYS.between(a, b)`           | `b - a` gün (`long`), ters → negatif    |
| `d.getDayOfWeek()`                        | `MONDAY` gibi bir enum                  |
| `d.format(ofPattern("dd.MM.yyyy"))`       | `05.01.2026`                            |
| `LocalDate.parse("2026-1-5")`             | `DateTimeParseException`                |
| `LocalDate.of(2026, 2, 30)`               | `DateTimeException`                     |

⚠️ `d.plusDays(1);` tek başına bir satırsa **hiçbir şey yapmaz** (sonucu atmadın). `d = d.plusDays(1);` yazmalısın.

"`day` `start` ile `end` arasında mı (ikisi dahil)?" kalıbı:
```java
!day.isBefore(start) && !day.isAfter(end)
```

### 1.5 Dosya I/O — Path + Files

```java
Path p = Path.of("data.txt");
Files.write(p, List.of("a", "b"));       // dosyayı SİLİP baştan yazar (her eleman bir satır)
Files.writeString(p, "c\n", StandardOpenOption.APPEND);   // sonuna ekler
List<String> lines = Files.readAllLines(p);               // [a, b, c]
```

- `IOException` **checked** → metoda `throws IOException` yaz ya da `try/catch` yap. Yazmazsan **derlenmez**.
- Dosya yoksa: `NoSuchFileException` (`IOException`'ın alt class'ı).
- CSV kalıbı: kaydederken `String.join(";", ...)` ya da `+ ";" +`, okurken `line.split(";")`.

### 1.6 Exception isimleri — hangisi ne zaman

| Kod                                        | Exception                          |
| ------------------------------------------ | ---------------------------------- |
| `String s = null; s.length();`             | `NullPointerException`             |
| `int[] a = new int[2]; a[2] = 1;`          | `ArrayIndexOutOfBoundsException`   |
| `"abc".charAt(3)`                          | `StringIndexOutOfBoundsException`  |
| `new ArrayList<>().get(0)`                 | `IndexOutOfBoundsException`        |
| `Integer.parseInt("12a")`                  | `NumberFormatException`            |
| `5 / 0` (int)                              | `ArithmeticException`              |
| `(Integer) (Object) "hi"`                  | `ClassCastException`               |
| `List.of(1).add(2)`                        | `UnsupportedOperationException`    |
| for-each içinde `list.remove(x)`           | `ConcurrentModificationException`  |
| boş Optional'da `get()`, bitmiş `next()`   | `NoSuchElementException`           |
| aynı stream'e ikinci bitiş adımı           | `IllegalStateException`            |
| `Enum.valueOf("yanlis")`                   | `IllegalArgumentException`         |
| olmayan dosyayı okumak                     | `NoSuchFileException` (checked)    |

**Checked olan tek grup:** `IOException` ve alt class'ları (+ `extends Exception` yazdığın kendi exception'ların). Tablodaki diğerleri **unchecked** (`RuntimeException`'dan gelir) → `throws` yazmak zorunda değilsin.

### ✍️ Kağıda Yaz

```
Optional[Ada] · Optional.empty · map → yine Optional · boşta get() → NoSuchElementException
record: Item[name=pen, qty=2] · erişim name() · equals değere bakar, == adrese
enum: println → ad · ordinal 0'dan · valueOf büyük-küçük harf duyarlı → IAE · TreeMap = tanım sırası
LocalDate: 2026-01-05 (sıfırlı) · plusDays YENİ tarih döner · DAYS.between(a, b) = b - a
Files.write SİLİP yazar · readAllLines → List<String> · IOException CHECKED
parseInt → NumberFormat · charAt → StringIndex · get(i) → IndexOutOfBounds · List.of add → UOE
```

---

## 2. Çıktı Tahmini (25 dk) — ÇALIŞTIRMADAN, cevaplar chat'e

Her snippet `main` içinde (`main` → `throws IOException`). Exception çıkarsa **hangi exception** olduğunu yaz, sonrasında yazdırılmayan satırları yazma. Derleme hatası varsa "derlenmez" + nedenini yaz.

**P1.** (5 satır)
```java
Optional<String> a = Optional.of("Ada");
Optional<String> b = Optional.empty();
System.out.println(a);
System.out.println(b);
System.out.println(a.map(String::length));
System.out.println(b.orElse("none"));
System.out.println(a.get() + b.isPresent());
```

**P2.**
```java
Optional<String> x = Optional.ofNullable(null);
System.out.println(x.isEmpty());
System.out.println(x.orElseThrow());
System.out.println("end");
```

**P3.** (4 satır)
```java
record Item(String name, int qty) {}
Item a = new Item("pen", 2);
Item b = new Item("pen", 2);
System.out.println(a);
System.out.println(a == b);
System.out.println(a.equals(b));
System.out.println(a.qty() + b.qty());
```

**P4.** (3 satır)
```java
record Age(int value) {
    Age {
        if (value < 0) throw new IllegalArgumentException("Negative: " + value);
    }
}
System.out.println(new Age(5));
try {
    new Age(-1);
} catch (IllegalArgumentException e) {
    System.out.println(e.getMessage());
}
System.out.println(new Age(7).value() * 2);
```

**P5.**
```java
enum Level { LOW, MID, HIGH }
System.out.println(Level.MID);
System.out.println(Level.MID.ordinal());
System.out.println(Arrays.toString(Level.values()));
System.out.println(Level.valueOf("HIGH") == Level.HIGH);
System.out.println(Level.LOW.compareTo(Level.HIGH) < 0);
System.out.println(Level.valueOf("high"));
```

**P6.** (2 satır)
```java
enum Coin {
    SMALL(5), BIG(25);
    private final int cents;
    Coin(int cents) { this.cents = cents; }
    int cents() { return cents; }
}
int total = 0;
for (Coin c : Coin.values()) {
    total += c.cents();
}
System.out.println(total);
Coin c = Coin.BIG;
String s = switch (c) {
    case SMALL -> "s";
    case BIG -> "b";
};
System.out.println(s + c.name().length());
```

**P7.** (6 satır) — 13 Kasım 2026 **Cuma**.
```java
LocalDate d = LocalDate.of(2026, 1, 31);
d.plusDays(1);
System.out.println(d);
LocalDate e = d.plusMonths(1);
System.out.println(e);
System.out.println(ChronoUnit.DAYS.between(d, e));
System.out.println(e.isBefore(d));
System.out.println(LocalDate.of(2026, 11, 13).getDayOfWeek());
System.out.println(d.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
```

**P8.**
```java
Path p = Path.of("p8.txt");
Files.write(p, List.of("x", "y"));
Files.write(p, List.of("z"));
Files.writeString(p, "w\n", StandardOpenOption.APPEND);
List<String> lines = Files.readAllLines(p);
System.out.println(lines);
System.out.println(lines.size());
Files.readAllLines(Path.of("nope.txt"));
System.out.println("end");
```

**P9.** Her satır **tek başına** çalışsa hangi exception fırlar? Sadece ismini yaz (1–8).
```java
Integer.parseInt("12a");                                   // 1
"abc".charAt(3);                                           // 2
new ArrayList<Integer>().get(0);                           // 3
List.of(1).add(2);                                         // 4
LocalDate.parse("2026-1-5");                               // 5
LocalDate.of(2026, 2, 30);                                 // 6
new ArrayList<String>().iterator().next();                 // 7
Optional.of(null);                                         // 8
```
Ek soru: 1–8'den hangileri **checked**?

---

## 3. Notsuz Kod Görevi (1.5–2 sa) — Bisiklet Kiralama

**Kural:** DERS dosyalarına (bu dosyanın özet kısmı dahil), eski koduna, internete bakmak **yok**. AI kapalı. Bu bölümün **görev metni** açık kalabilir. Takılırsan önce 10 dk kendin dene, sonra chat'e sor.

Her public tip kendi dosyasında, package `w5d5`. Döngü ya da stream **serbest** (senin tasarım kararın).

### 3.1 `enum BikeType` (`BikeType.java`)

**Sabitler (bu sırayla):** `CITY(50)`, `MOUNTAIN(80)`, `ELECTRIC(120)` — parantezdeki sayı günlük fiyat.

**Field:** `private final int dailyPrice;`

**Constructor:** `BikeType(int dailyPrice)`

**Metot:** `public int dailyPrice()` → günlük fiyat.

### 3.2 `record Rental(String id, String customer, BikeType type, LocalDate start, LocalDate end)` (`Rental.java`)

**Compact constructor**, kontroller bu sırayla:
- `id == null` ya da `id.isBlank()` → `IllegalArgumentException("Id must not be blank")`
- `customer == null` ya da `customer.isBlank()` → `IllegalArgumentException("Customer must not be blank")`
- `type == null` → `IllegalArgumentException("Type must not be null")`
- `start == null` ya da `end == null` → `IllegalArgumentException("Dates must not be null")`
- `end`, `start`'tan **önce** → `IllegalArgumentException("End before start")`

**Metotlar:**
- `public long days()` → kiralama gün sayısı, **iki uç dahil**. Aynı gün alıp verirse `1`.
  Örnek: start `2026-11-01`, end `2026-11-03` → `3`
- `public long price()` → `days() * type.dailyPrice()`.
  Örnek: yukarıdaki + `MOUNTAIN` → `240`
- `public String toCsv()` → alanlar `;` ile, bu sırayla: `"r1;Ada;CITY;2026-11-01;2026-11-03"`
- `public static Rental fromCsv(String line)` → `toCsv`'nin tersi.
  - `;` ile bölünce **tam 5 parça** çıkmazsa → `IllegalArgumentException("Bad line: " + line)`
  - Tip ya da tarih bozuksa kendi exception'ını yazma: Java'nınki (`valueOf` / `parse` ne fırlatıyorsa) olduğu gibi fırlasın.

### 3.3 `class RentalShop` (`RentalShop.java`)

**Field:**
- `private final List<Rental> rentals = new ArrayList<>();`

**Constructor:** yok (default constructor yeterli).

**Metotlar:**
- `public void add(Rental rental)`
  - `rental == null` → `IllegalArgumentException("Rental must not be null")`
  - Aynı **id**'li kiralama zaten varsa → `IllegalStateException("Duplicate id: " + id)`
- `public Optional<Rental> findById(String id)` → bulursa dolu, bulamazsa `Optional.empty()`.
- `public long priceOf(String id)` → o kiralamanın `price()`'ı. Yoksa → `NoSuchElementException("No rental: " + id)`. **`findById`'yi kullan.**
- `public List<Rental> activeOn(LocalDate day)` → `day`, `start` ile `end` arasında (**ikisi dahil**) olan kiralamalar, **eklenme sırasıyla**.
- `public Map<BikeType, Long> revenueByType()` → tip → o tipteki kiralamaların `price()` toplamı. Key sırası **enum sırası** (CITY, MOUNTAIN, ELECTRIC). Hiç kiralanmamış tip map'te **olmasın**.
- `public Optional<Rental> longest()` → `days()`'i en büyük kiralama; kiralama yoksa `Optional.empty()`. (Testlerde eşit en uzun yok.)
- `public void saveTo(Path path) throws IOException` → her kiralamayı `toCsv()` ile bir satır yap, eklenme sırasıyla dosyaya yaz (dosya varsa **üzerine yaz**).
- `public static RentalShop loadFrom(Path path) throws IOException` → dosyayı oku, **yeni** bir `RentalShop` oluştur, her satırı `fromCsv` ile çevirip `add` et, döndür. **Boş satırları** (`isBlank()`) atla.

**Bitince:** "bitti" yaz, dosyaları okuyup testlerimi çalıştıracağım.

---

## 4. Algoritma (1 sa) — Karışık

**Yöntem:**
1. Örnek girdiyi **elle** çöz, adımları chat'e yaz.
2. Ben "tamam" deyince kodla.
3. LeetCode'a submit et.

### A1 — LeetCode 1436: Destination City (Easy)
`paths[i] = [cityA, cityB]` → A'dan B'ye yol var. Yollar tek bir zincir oluşturuyor. **Hiçbir yere yolu olmayan** şehri (varış noktası) döndür.

**Elle çöz:** `paths = [["London","New York"],["New York","Lima"],["Lima","Sao Paulo"]]` → `"Sao Paulo"`
1. Her yolun **başlangıç** şehrini bir listeye yaz.
2. Her yolun **bitiş** şehrine bak: hangisi 1. listede **yok**?
Soru: 1. listeyi hangi koleksiyonda tutarsan "içinde var mı?" sorusu hızlı olur?

### A2 — LeetCode 1903: Largest Odd Number in String (Easy)
`num` bir sayıyı temsil eden String. `num`'un **baştan başlayan** alt stringlerinden (`num.substring(0, k)`), **tek** sayı olan en büyüğünü döndür. Yoksa `""`.

**Elle çöz:**
- `num = "52"` → `"5"`
- `num = "4206"` → `""`
- `num = "35427"` → `"35427"`

1. `"4206"` ve `"52"` için: hangi karakter bir sayının tek olup olmadığını belirler?
2. En büyük alt stringi istiyorsan, aramaya **baştan mı sondan mı** başlarsın? Neden?
Soru: Neden `Integer.parseInt(num)` kullanamazsın? (İpucu: `num` 10^5 karakter olabilir.)

---

## 5. Kapanış (15 dk)

- Isınma + tahmin sonuçlarından **en çok yanlış yaptığın 1 konuyu** chat'e yaz.
- `ILERLEME.md`'ye Gün 29 satırı (satırları ben hazırlayacağım).
- Commit + push:
  ```
  git add hafta-05
  git commit -m "feat: day 29 - review optional + record + enum + time + io"
  git push
  ```

**Yarın (Gün 30):** Notsuz mini proje — Maven ile sıfırdan, 3-4 saat. Bugünkü her şey içinde olacak.
