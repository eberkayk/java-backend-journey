# Hafta 4 · Gün 4 (Gün 22) — java.time + Dosya I/O

**Hedef:** Gider Takip'in iki eksiği kapanıyor:
- **java.time** → her gidere **tarih** (`LocalDate`). "Ekimde ne harcadım?" sorusu artık sorulabilir.
- **Dosya I/O** → program kapanınca giderler **kaybolmasın**: dosyaya yaz, sonra geri oku.

Bugünün sonunda Gider Takip giderleri `expenses.csv`'ye kaydedip geri yüklüyor. (Spring'de bu işi veritabanı yapacak; mantık aynı: nesne → satır → nesne.)

**Süre:** ~4 saat · Isınma 15 dk · Ders 50 dk · Egzersiz 45 dk · Proje 1 sa · Algoritma 45 dk · Kapanış 15 dk

**Kurallar (değişmedi):**
- Sınıf, metot, değişken ve ekran mesajları **İngilizce**
- **Değişiklik → derle → çalıştır → çıktının her satırını oku.**
- **Metot adı/imzası görevde nasılsa öyle.**
- Tahminler **çalıştırmadan önce** chat'e.
- Testleri Claude yazıp çalıştırıyor. Sen yazmıyorsun.

---

## 0. Isınma — dünden 5 soru (chat'e, kısa)

**S1.** İki satırın çıktısı ne?
```java
Optional<String> o = List.of("Ali", "Bora", "Cem").stream().filter(n -> n.length() > 3).findFirst();
System.out.println(o);
System.out.println(o.map(String::length).orElse(0));
```

**S2.** Çıktı ne?
```java
record P(int x) { }

P a = new P(1);
P b = new P(1);
System.out.println((a == b) + " " + a.equals(b));
```

**S3.** `record Point(int x, int y)` için hangisi **doğru** compact constructor? Diğer ikisi neden yanlış?
```java
// A
public Point() { if (x < 0) throw new IllegalArgumentException("neg"); }
// B
public Point { if (x < 0) throw new IllegalArgumentException("neg"); }
// C
public Point { this.x = x; }
```

**S4.** `enum Category { FOOD, HOME }` için ne olur?
```java
Category c = Category.valueOf("food");
```

**S5.** `findByDescription` bir `Optional<Expense>` döndürüyor. Hangisi derlenir, diğeri neden derlenmez?
```java
// A
boolean x = tracker.findByDescription("Gum").equals(gum).orElseThrow();
// B
boolean y = tracker.findByDescription("Gum").orElseThrow().equals(gum);
```

---

## 1. java.time — tarih ⭐⭐⭐ (25 dk)

Tarihi `String` (`"30.10.2026"`) ya da `int` olarak tutarsan: "3 gün sonrası?", "hangisi önce?", "Şubat 30 var mı?" sorularının hepsini sen hesaplarsın. `java.time` bunları hazır verir.

| Tip             | Ne tutar       | Örnek değer (`toString`) |
| --------------- | -------------- | ------------------------ |
| `LocalDate`     | gün (saat yok) | `2026-10-30`             |
| `LocalTime`     | saat (gün yok) | `14:05:30`               |
| `LocalDateTime` | gün + saat     | `2026-10-30T14:05:30`    |
| `YearMonth`     | yıl + ay       | `2026-10`                |

Bugün çoğunlukla `LocalDate` kullanacağız.

### Oluşturma

```java
LocalDate a = LocalDate.of(2026, 10, 30);      // yıl, ay (1-12), gün
LocalDate b = LocalDate.parse("2026-12-31");   // SADECE yyyy-MM-dd formatı
LocalDate c = LocalDate.now();                 // bugünün tarihi (her gün farklı!)
LocalDate.of(2026, 2, 30);                     // ❌ DateTimeException — Şubat 30 yok
```

### Kullanma

| Metot                              | Örnek (`a = 2026-10-30`)               | Sonuç             |
| ---------------------------------- | -------------------------------------- | ----------------- |
| `plusDays(n)` / `minusDays(n)`     | `a.plusDays(3)`                        | `2026-11-02`      |
| `plusMonths(n)` / `minusMonths(n)` | `a.plusMonths(1)`                      | `2026-11-30`      |
| `getYear()` / `getMonthValue()`    | `a.getMonthValue()`                    | `10` (int)        |
| `getMonth()`                       | `a.getMonth()`                         | `OCTOBER` (enum!) |
| `getDayOfWeek()`                   | `a.getDayOfWeek()`                     | `FRIDAY` (enum!)  |
| `isBefore(x)` / `isAfter(x)`       | `a.isBefore(b)`                        | `true`            |
| `equals(x)`                        | `a.equals(LocalDate.of(2026, 10, 30))` | `true`            |
| `ChronoUnit.DAYS.between(x, y)`    | `ChronoUnit.DAYS.between(a, b)`        | `62` (long)       |
| `YearMonth.from(x)`                | `YearMonth.from(a)`                    | `2026-10`         |

`ChronoUnit.DAYS.between(x, y)` = `y - x`. Sırayı ters verirsen sonuç **negatif** olur.

### ⚠️ En büyük tuzak: LocalDate değişmez (immutable)

`LocalDate` dünkü record'un mantığında: **kendini değiştirmez, yeni bir tarih döndürür.**

```java
LocalDate d = LocalDate.of(2026, 10, 30);
d.plusDays(1);                 // yeni tarih oluştu... ve çöpe gitti
System.out.println(d);         // 2026-10-30  ← değişmedi!

d = d.plusDays(1);             // ✅ dönen değeri sakla
System.out.println(d);         // 2026-10-31
```

(Gün 5'teki `s.toUpperCase();` tuzağının aynısı — String de değişmez.)

### Kendi formatın: DateTimeFormatter

`toString` hep `yyyy-MM-dd` verir. Ekranda `30.10.2026` istersen:

```java
DateTimeFormatter tr = DateTimeFormatter.ofPattern("dd.MM.yyyy");
a.format(tr);                              // "30.10.2026"
LocalDate.parse("30.10.2026", tr);         // 2026-10-30   ← formatter'ı ver
LocalDate.parse("30.10.2026");             // ❌ DateTimeParseException — formatter yok, yyyy-MM-dd bekler
```

`MM` = ay, `mm` = dakika. Karıştırma.

---

## 2. Dosya I/O — yaz ve oku ⭐⭐⭐ (20 dk)

Modern Java'da dosya işi iki class ile: **`Path`** (dosyanın adresi) ve **`Files`** (işi yapan static metotlar). Import: `java.nio.file.*` ve `java.io.IOException`.

```java
Path p = Path.of("notes.txt");    // sadece adres. Dosya oluşturmaz, var mı bakmaz.
```

| Metot                                                | Ne yapar                                                                |
| ---------------------------------------------------- | ----------------------------------------------------------------------- |
| `Files.write(p, List<String> lines)`                 | Her elemanı bir satır yazar. Dosya varsa **içini siler**, baştan yazar. |
| `Files.write(p, lines, StandardOpenOption.APPEND)`   | Silmez, **sonuna ekler**.                                               |
| `Files.readAllLines(p)`                              | Bütün satırları `List<String>` olarak döndürür (satır sonları yok).     |
| `Files.writeString(p, text)` / `Files.readString(p)` | Tek bir `String` olarak yaz / oku.                                      |
| `Files.exists(p)`                                    | Dosya var mı → `boolean`.                                               |

### IOException — derleyici seni zorlar

Hafta 3 Gün 1'i hatırla: `IOException` **checked**. Disk dolu, dosya yok, izin yok... Java "bu olabilir, karar ver" der. İki yol:

```java
// 1) Yukarı fırlat: "bu metot dosya işi yapıyor, çağıran ilgilensin"
public void saveTo(Path path) throws IOException {
    Files.write(path, ...);
}

// 2) Yakala: "burada ne yapacağıma ben karar veriyorum"
try {
    List<String> lines = Files.readAllLines(Path.of("missing.txt"));
} catch (IOException e) {
    System.out.println("Error: " + e.getMessage());     // Error: missing.txt
}
```

Dosya yoksa gelen exception `NoSuchFileException` (`IOException`'ın alt class'ı). Mesajı sadece dosya adı.

### Dosya nerede oluşuyor?

`Path.of("notes.txt")` gibi **göreli** bir yol, programın çalıştığı klasöre göre çözülür. IntelliJ'de bu varsayılan olarak **proje kökü** (`C:\dev\roadmap backend`). Emin olmak için bir kez şunu yazdırabilirsin:

```java
System.out.println(p.toAbsolutePath());
```

> **Ek bilgi (bugün kullanmıyoruz):** Çok büyük dosyalarda hepsini birden okumak yerine satır satır okunur: `try (BufferedReader r = Files.newBufferedReader(p)) { ... }`. Bu `try (...)` yazımına **try-with-resources** denir; blok bitince dosyayı otomatik kapatır. JDBC'de (veritabanı) tekrar karşına çıkacak.

---

## 3. Nesne ↔ satır: CSV ⭐⭐⭐ (5 dk)

Dosya sadece metin tutar. `Expense` nesnesini kaydetmek için onu **tek satır metne** çevirip, okurken **geri nesneye** çevirmemiz gerekiyor. En basit format: virgülle ayrılmış alanlar (CSV).

```
nesne                                                         satır
Expense[2026-10-12, Coffee, FOOD, 45.5]   ── toCsv() ──▶   2026-10-12,Coffee,FOOD,45.5
                                          ◀── fromCsv() ──
```

Geri çevirirken her parça `String`'den kendi tipine:

| Parça          | String → tip              |
| -------------- | ------------------------- |
| `"2026-10-12"` | `LocalDate.parse(...)`    |
| `"FOOD"`       | `Category.valueOf(...)`   |
| `"45.5"`       | `Double.parseDouble(...)` |

**Neden `FOOD` yazıyoruz, `Food` (label) değil?** Çünkü geri okurken `valueOf` birebir **sabit adını** ister (dünkü tuzak 2). `category.name()` → `"FOOD"`.

**Virgül sorunu:** Açıklama `"Tea, milk"` olursa satır 5 parçaya bölünür ve okuma bozulur. Bugünkü çözüm: açıklamada virgüle **izin verme** (compact constructor'da kontrol).

---

## 4. Üç tuzak ⭐⭐ (5 dk)

1. **`d.plusDays(1);` tek başına hiçbir şey yapmaz.** `d = d.plusDays(1);`
2. **`LocalDate.parse` formatter'sız sadece `yyyy-MM-dd` okur.** `"30.10.2026"` → `DateTimeParseException`.
3. **`Files.write` dosyanın içini siler.** Eklemek istiyorsan `StandardOpenOption.APPEND`.

---

## 📝 Kağıda Yaz

```
LocalDate.of(2026, 10, 30)  .parse("2026-10-30")  .now()
  plusDays/minusMonths → YENİ tarih döndürür (d = d.plusDays(1))
  isBefore/isAfter  getMonth() getDayOfWeek() → enum
  ChronoUnit.DAYS.between(a, b) = b - a     YearMonth.from(d)
  DateTimeFormatter.ofPattern("dd.MM.yyyy")  MM=ay mm=dakika
Path p = Path.of("x.txt");  Files.write(p, lines)  ← içini siler
  Files.readAllLines(p) → List<String>     APPEND = sonuna ekle
  IOException CHECKED → throws ya da try/catch
CSV: toCsv() nesne→satır, fromCsv() satır→nesne (split(","))
```

---

## 💪 EGZERSİZLER (~45 dk)

Klasör: `hafta-04/gun-04/w4d4/` · Paket: `package w4d4;` · Import: `java.time.*`, `java.time.temporal.ChronoUnit`, `java.time.format.*`, `java.nio.file.*`, `java.io.IOException`, `java.util.*`

### E1 — `DatePredict.java` (tahmin)

`main`'e yaz. **Çalıştırmadan önce** 10 satırın çıktısını chat'e yaz, sonra çalıştır ve karşılaştır.

```java
LocalDate d = LocalDate.of(2026, 10, 30);
System.out.println(d);                                                  // 1
System.out.println(d.plusDays(3));                                      // 2
d.plusDays(10);
System.out.println(d);                                                  // 3
System.out.println(d.getDayOfWeek());                                   // 4  (ipucu: bugün 30 Ekim 2026)
System.out.println(d.minusMonths(8));                                   // 5  (tahmin sorusu — 30 Şubat yok, Java ne yapar?)

LocalDate e = LocalDate.parse("2026-12-31");
System.out.println(d.isBefore(e) + " " + e.isBefore(d));               // 6
System.out.println(ChronoUnit.DAYS.between(d, e));                      // 7
System.out.println(ChronoUnit.DAYS.between(e, d));                      // 8
System.out.println(d.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));// 9

try {
    LocalDate.parse("30.10.2026");
    System.out.println("Parsed");
} catch (DateTimeParseException ex) {
    System.out.println(ex.getClass().getSimpleName());                  // 10
}
```

### E2 — `FileDemo.java` (kod yaz)

`main`'in imzasına `throws IOException` ekle (sadece bu demo için; en sondaki "missing" kısmı yine try/catch ile).

1. `Path p = Path.of("notes.txt");`
2. `Files.write` ile `"first"`, `"second"`, `"third"` satırlarını yaz
3. `Files.readAllLines` ile geri oku → `Lines: <satır sayısı>`
4. Her satırı numarasıyla yazdır: `1: first` ... (döngü serbest)
5. `"fourth"`'u **sonuna ekle** (`APPEND`), sonra `After append: ` + `readAllLines` sonucu
6. `Files.write` ile **sadece** `"only"` yaz (APPEND yok), sonra `After overwrite: ` + `readAllLines` sonucu
7. `Path.of("missing.txt")`'i oku → `IOException` yakala → `Error: ` + `e.getClass().getSimpleName()` + `" "` + `e.getMessage()`

**Beklenen çıktı:**
```
Lines: 3
1: first
2: second
3: third
After append: [first, second, third, fourth]
After overwrite: [only]
Error: NoSuchFileException missing.txt
```

Çalıştırdıktan sonra `notes.txt`'i IntelliJ'de aç, içinde sadece `only` olduğunu gör.

---

## 🏛️ PROJE 0 — Gider Takip v4 (~1 saat)

### Adım 0 — Kopyala

`hafta-04/gun-03/w4d3/` içinden `Category`, `Expense`, `ExpenseTracker`'ı `hafta-04/gun-04/w4d4/` içine kopyala, `package w4d4;` yap. `ExpenseApp`'i **kopyalama**, `w4d4`'te yeni dosya olarak yazacaksın.

`Category`'de değişiklik **yok**.

### `Expense` (record) — değişiklikler

- Alanlar, **bu sırayla**: `LocalDate date`, `String description`, `Category category`, `double amount` (`date` en başa geldi)
- Compact constructor, **bu sırayla** kontrol:
  - `date` null → `IllegalArgumentException("Date must not be null")`
  - `description` null ya da boş → `"Description must not be blank"` (dünkü)
  - `description` virgül içeriyor → `"Description must not contain commas"` (`contains(",")`)
  - `category` null → `"Category must not be null"` (dünkü)
  - `amount <= 0` → `"Amount must be positive"` (dünkü)
- `toString` override: başa tarih geldi → `2026-10-12 Coffee (Food) 45.5`
- **Yeni:** `public String toCsv()` → `2026-10-12,Coffee,FOOD,45.5` (kategori `name()` ile, label değil)
- **Yeni:** `public static Expense fromCsv(String line)`:
  - `line.split(",")` ile böl
  - Parça sayısı 4 değilse → `IllegalArgumentException("Invalid line: " + line)`
  - Her parçayı kendi tipine çevir (Bölüm 3 tablosu), `new Expense(...)` döndür

### `ExpenseTracker` — mevcut metotlar

`Expense`'e alan eklendiği için bir şey kırılmaz (getter adları aynı). **Dokunma.** Sadece derlendiğini kontrol et.

### `ExpenseTracker` — yeni metotlar

| İmza                                                                  | Ne yapar                                                                                                                               |
| --------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------- |
| `public void saveTo(Path path) throws IOException`                    | Her gideri `toCsv()` ile satıra çevirip dosyaya yazar (stream + `Files.write`).                                                        |
| `public static ExpenseTracker loadFrom(Path path) throws IOException` | Dosyayı okur, **yeni** bir `ExpenseTracker` oluşturur, her satırı `fromCsv` ile çevirip `add` eder, tracker'ı döndürür. Döngü serbest. |
| `public List<Expense> between(LocalDate from, LocalDate to)`          | Tarihi `from` ile `to` arasında olan giderler, **iki uç dahil**, **tarihe göre artan** sıralı. Stream ile.                             |
| `public Map<YearMonth, Double> totalsByMonth()`                       | Her ay (`YearMonth.from(date)`) için toplam tutar, `TreeMap` (aylar sıralı). Dünkü `totalsByCategory`'nin ay versiyonu.                |

İpucu (`between`, "iki uç dahil"): `10-01` ile `10-15` arası istendiğinde `10-01` de `10-15` de gelmeli. `isAfter`/`isBefore` uçları **dahil etmez**; "`from`'dan önce **değil** ve `to`'dan sonra **değil**" diye düşün.

`loadFrom` neden `static`? Çünkü elinde henüz bir tracker yok; metot tracker'ı **üretiyor**. Çağrısı: `ExpenseTracker.loadFrom(path)`.

### `ExpenseApp` (`main`, yeni dosya)

`main`'in imzasına `throws IOException` ekle.

1. Bu 7 gideri **bu sırayla** ekle (dünküler, tarihli; sıra tarihe göre **değil**, bilerek):

| Tarih        | Açıklama  | Kategori    | Tutar  |
| ------------ | --------- | ----------- | ------ |
| `2026-10-12` | Coffee    | `FOOD`      | 45.5   |
| `2026-10-01` | Rent      | `HOME`      | 12000  |
| `2026-09-28` | Bus card  | `TRANSPORT` | 15     |
| `2026-10-05` | Groceries | `FOOD`      | 640.75 |
| `2026-10-15` | Gum       | `FOOD`      | 12.5   |
| `2026-10-03` | Internet  | `HOME`      | 450    |
| `2026-10-20` | Taxi      | `TRANSPORT` | 230    |

2. `Path file = Path.of("expenses.csv");` → `saveTo(file)` → `Saved <sayı> expenses`
3. `File:` + dosyanın satırlarını **olduğu gibi** yazdır (`Files.readAllLines`, her satır ayrı)
4. `ExpenseTracker.loadFrom(file)` ile **yeni** bir tracker yükle → `Loaded: <sayı>`
5. `Same data: ` + yüklenen tracker'ın `getAll()`'u ile ilk tracker'ın `getAll()`'u `equals` mı?
6. `Oct 1-15:` + yüklenen tracker'da `between(2026-10-01, 2026-10-15)`, her biri ayrı satır
7. `Totals by month: ` + `totalsByMonth()`
8. `Span: <ilk tarih> -> <son tarih> (<gün> days)` → en erken ve en geç tarihi stream ile bul (`map` + `min`/`max` + `orElseThrow`), aradaki günü `ChronoUnit` ile. Tarihler için comparator: `Comparator.naturalOrder()`.
9. `new Expense(2026-10-01, "Tea, milk", FOOD, 10)` dene → yakala → `Error: <mesaj>`
10. `Expense.fromCsv("2026-10-01,Tea")` dene → yakala → `Error: <mesaj>`
11. `ExpenseTracker.loadFrom(Path.of("missing.csv"))` dene → `IOException` yakala → `Load error: ` + `getSimpleName()` + `" "` + `getMessage()`

**Beklenen çıktı:**
```
Saved 7 expenses
File:
2026-10-12,Coffee,FOOD,45.5
2026-10-01,Rent,HOME,12000.0
2026-09-28,Bus card,TRANSPORT,15.0
2026-10-05,Groceries,FOOD,640.75
2026-10-15,Gum,FOOD,12.5
2026-10-03,Internet,HOME,450.0
2026-10-20,Taxi,TRANSPORT,230.0
Loaded: 7
Same data: true
Oct 1-15:
2026-10-01 Rent (Home) 12000.0
2026-10-03 Internet (Home) 450.0
2026-10-05 Groceries (Food) 640.75
2026-10-12 Coffee (Food) 45.5
2026-10-15 Gum (Food) 12.5
Totals by month: {2026-09=15.0, 2026-10=13378.75}
Span: 2026-09-28 -> 2026-10-20 (22 days)
Error: Description must not contain commas
Error: Invalid line: 2026-10-01,Tea
Load error: NoSuchFileException missing.csv
```

### Proje sonu düşünme soruları (chat'e, 1-2 cümle)

1. 5. satır `true` çıktı. Yüklenen giderler dosyadan **yeni** oluşturulan nesneler, yani adresleri farklı. Neden yine de `true`? (İpucu: `List.equals` her elemanı neyle karşılaştırır, ve `Expense` ne?)
2. `toCsv()`'de kategoriyi `getLabel()` ile (`Food`) yazsaydık, `loadFrom` hangi satırda, hangi exception ile patlardı?

---

## 🧩 ALGORİTMA SAATİ (45 dk) — Tarihler

**A1 — [LeetCode 1360: Number of Days Between Two Dates](https://leetcode.com/problems/number-of-days-between-two-dates/)**
`int daysBetweenDates(String date1, String date2)`: iki tarih (`yyyy-MM-dd`) arasındaki gün sayısı (her zaman pozitif).
- Örnekler: `"2019-06-29", "2019-06-30"` → `1` · `"2020-01-15", "2019-12-31"` → `15`
- Bugünkü dersle çöz: `java.time`. LeetCode'da en üste `import java.time.*;` ve `import java.time.temporal.ChronoUnit;` yazabilirsin.
- İkinci örnekte sıra ters: `between` negatif döner. Ne yapman lazım?
- `between` `long` döndürür, metot `int` istiyor.

**A2 — [LeetCode 1154: Day of the Year](https://leetcode.com/problems/day-of-the-year/)**
`int dayOfYear(String date)`: tarih (`yyyy-MM-dd`) yılın kaçıncı günü?
- Örnekler: `"2019-01-09"` → `9` · `"2019-02-10"` → `41` · `"2004-03-01"` → `61`
- **Bu sefer `java.time` YASAK** — elle çöz (mülakatta kütüphane yoksa diye).
- Adımlar: (1) String'den yıl, ay, gün sayılarını çıkar (`substring` + `Integer.parseInt`, ya da `split("-")`). (2) Her ayın gün sayısını bir diziye yaz: `{31, 28, 31, 30, ...}`. (3) Önceki ayların günlerini topla + gün.
- Kodlamadan önce chat'e: `"2019-02-10"` için hangi ay(lar)ın günleri toplanır, sonuç neden 41?
- **Artık yıl** kuralı (Şubat 29 gün): yıl 4'e bölünüyorsa artık yıl, **ama** 100'e bölünüyorsa değil, **ama** 400'e bölünüyorsa yine artık yıl. (`2004` ✔, `1900` ✘, `2000` ✔)
- Gönderip kabul aldıktan sonra (isteğe bağlı): aynı soruyu tek satırda `LocalDate.parse(date).getDayOfYear()` ile yazıp gönder.

---

## 🔧 GIT (10 dk)

`notes.txt` ve `expenses.csv` program çıktısı, repoya girmesin. `.gitignore`'un sonuna iki satır ekle:
```
notes.txt
expenses.csv
```

```bash
git add .gitignore hafta-04/gun-04
git status            # sadece .gitignore, .java ve DERS.md (.class, .txt, .csv yok)
git commit -m "feat: week 4 day 4 - java.time, file io"
git push
```
Sonra `ILERLEME.md`'ye Gün 22 logu → `docs: day 22 progress log`.

---

## ✅ Gün Sonu

- [ ] Isınma S1–S5
- [ ] Bölüm 1–4 okundu
- [ ] E1 (tahminler **çalıştırmadan önce** chat'e)
- [ ] E2 `FileDemo` çıktısı birebir aynı
- [ ] Proje: tarihli `Expense` + `toCsv`/`fromCsv`, tracker'da 4 yeni metot, `ExpenseApp` çıktısı birebir aynı
- [ ] Proje düşünme soruları
- [ ] A1 + A2 (A2 elle)
- [ ] "Kağıda Yaz" kutusu kağıtta
- [ ] Commit + push, `ILERLEME.md` Gün 22

**Yarın oturum başında soracaklarım:** 4-5 kısa soru (kod örnekli): `plusDays` atamasız çağrı, `ChronoUnit` işareti, `Files.write` silme/ekleme, `IOException` checked, `fromCsv` parça çevirme.

**Sıradaki:** Hafta 4 Gün 5 (Gün 23): **Maven** — Gider Takip'i Maven projesine taşıyıp README yazıyoruz (Proje 0 teslimi).
