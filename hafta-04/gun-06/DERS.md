# Hafta 4 · Gün 6 (Gün 24) — Hafta 4 Mini Sınav + Concurrency'ye Giriş

**Hedef:** İki parça:
1. Gün 19–23'ü (lambda, stream, Optional, enum, record, java.time, dosya I/O, Maven) **kapalı kitap** bir sınavla ölçmek. Puan değil, **nerede boşluk olduğu** önemli.
2. Sınavdan sonra kısa bir konu: **concurrency (eşzamanlılık)**. Spring'de her HTTP isteği ayrı bir thread'de çalışır — iki thread aynı nesneye aynı anda dokununca ne olduğunu bugün kendi gözünle göreceksin.

**Süre:** ~3 saat · Tekrar 30 dk · Mini sınav 90 dk · Mola 10 dk · Concurrency 40 dk · Kapanış 15 dk

**Kurallar (değişmedi):**
- Sınıf, metot, değişken ve ekran mesajları **İngilizce**
- **Değişiklik → derle → çalıştır → çıktının her satırını oku.**
- **Metot adı/imzası görevde nasılsa öyle.**
- Testleri Claude yazıp çalıştırıyor. Sen yazmıyorsun.

Bugün ısınma yok — onun yerine son 4 günde **birden fazla kez kaçan** konuları tekrar ediyoruz.

**Kurulum:** `hafta-04/gun-06/` klasöründe package **`w4d6`** (`hafta-04/gun-06/w4d6/`). `gun-06`'yı Sources Root yap.

---

## 1. Tekrar (30 dk) — sınavdan ÖNCE, notlara bakmak serbest

1. Gün 19–23'ün **"Kağıda Yaz" kutularını** baştan oku (5 kutu, ~10 dk).
2. Aşağıdaki 4 maddeyi oku (~15 dk). Hepsi son günlerde en az 2 kez kaçtı.
3. Takıldığın bir şey varsa **sınavdan önce** chat'e sor. Sınav başladıktan sonra soru yok.

### 1.1 Optional'ı yazdırmak (3 kez kaçtı)

**Kutuyu yazdırırsan kutu görünür. Açarsan içi görünür.**

```java
Optional<String> o = Optional.of("Cem");
System.out.println(o);                    // Optional[Cem]   ← kutu
System.out.println(o.orElse("none"));     // Cem             ← açıldı
System.out.println(Optional.empty());     // Optional.empty  ← boş kutu
System.out.println("Top: " + o);          // Top: Optional[Cem]  (+ ile birleştirmek de açmaz)
```

Açan metotlar: `orElse`, `orElseThrow`, `get`. **`map` açmaz** — yine `Optional` döner.

### 1.2 Hangi satır hangi exception'ı fırlatır (2 kez kaçtı)

```
Category.valueOf("food")         → IllegalArgumentException    (isim birebir tutmadı)
Optional.empty().orElseThrow()   → NoSuchElementException      (kutu boş)
LocalDate.parse("30.10.2026")    → DateTimeParseException      (format tutmadı)
Integer.parseInt("abc")          → NumberFormatException
Files.readAllLines(...)          → IOException (CHECKED → throws ya da try/catch zorunlu)
stream'i ikinci kez kullanmak    → IllegalStateException
```

İpucu: `NumberFormatException` aslında `IllegalArgumentException`'ın alt sınıfı — o yüzden `catch (IllegalArgumentException e)` ikisini de yakalar.

### 1.3 Tarih tuzakları

```java
LocalDate d = LocalDate.of(2026, 10, 31);
d.plusDays(1);                     // hiçbir şey olmadı! d hâlâ 2026-10-31
d = d.plusDays(1);                 // şimdi 2026-11-01

LocalDate.of(2026, 10, 31).plusMonths(1);   // 2026-11-30  (31 Kasım yok → ayın son gününe kırpılır)
ChronoUnit.DAYS.between(1 Ekim, 31 Ekim);   // 30          (= bitiş - başlangıç, başlangıç günü sayılmaz)
```

### 1.4 Maven tek bakışta

```
GAV = groupId (şirket/kişi: com.eberkayk) · artifactId (proje adı: expense-tracker) · version (1.0.0)
mvn clean package → target/ siler, derler, paketler → target/expense-tracker-1.0.0.jar
                                                        (artifactId-version.jar)
java -jar x.jar  → jar'ın içinde "Main-Class" yazmalı; yoksa "no main manifest attribute"
                   (bunu pom.xml'deki maven-jar-plugin → mainClass ayarlıyor)
Boşluklu argüman → tırnak:  add 2026-10-31 "Coffee beans" FOOD 45.5
```

---

## 🧪 HAFTA 4 MİNİ SINAV (90 dk) — kapalı kitap

**Kurallar:** Notlara, DERS dosyalarına, eski koduna, internete bakmak **yok**. IntelliJ açık olabilir ama **AI kapalı**. Başlangıç saatini `SINAV.md`'ye yaz, süreyi tut. 90 dk dolduğunda nerede kaldıysan bırak — yarım kod da teslim edilir.

- Teori ve çıktı cevapları → `hafta-04/gun-06/SINAV.md` (sorular hazır, sadece `Cevap:` satırlarını doldur)
- Kodlar → package **`w4d6`**
- **Ben kontrol edeceğim (testleri ben yazıp çalıştıracağım) — kendin "doğru" diye işaretleme.**

**Puanlama:** A teori 25 · B çıktı 25 · C kod 50
**Önerilen süre:** A 15 dk · B 15 dk · C 60 dk

### Bölüm A — Teori → `SINAV.md`
### Bölüm B — Çıktı tahmini (ÇALIŞTIRMADAN) → `SINAV.md`

Sorular `SINAV.md` içinde.

### Bölüm C — Kod

Her public tip kendi dosyasında, package `w4d6`. Döngü yasak denen yerde stream kullan.

**C1 (25 puan) — Antrenman Günlüğü.** Gider Takip'in kardeşi; aynı fikirler, yeni alan.

#### 1. `enum Sport` (`Sport.java`)
- Sabitler, bu sırayla: `RUN`, `BIKE`, `SWIM`

#### 2. `record Workout(LocalDate date, Sport sport, int minutes)` (`Workout.java`)
- **Compact constructor**, kontroller bu sırayla:
  - `date == null` → `IllegalArgumentException("Date must not be null")`
  - `sport == null` → `IllegalArgumentException("Sport must not be null")`
  - `minutes <= 0` → `IllegalArgumentException("Minutes must be positive")`
- `public String toCsv()` → `"2026-10-26,RUN,30"` formatında (tarih, spor, dakika; virgülle)
- `public static Workout fromCsv(String line)` → `toCsv`'nin tersi. Bilinmeyen spor gelirse `valueOf`'un kendi exception'ı dışarı çıksın (yakalama).

#### 3. `class WorkoutLog` (`WorkoutLog.java`)
- Field: `private final List<Workout> workouts = new ArrayList<>();`
- Constructor: yok (varsayılan yeter)
- `public void add(Workout w)` → `w == null` ise `IllegalArgumentException("Workout must not be null")`, değilse listeye ekle
- `public int size()` → kaç antrenman var
- `public int totalMinutes()` → tüm dakikaların toplamı. **Döngü yasak.**
- `public List<Workout> bySport(Sport sport)` → o spordaki antrenmanlar, **tarihe göre eskiden yeniye**. **Döngü yasak.**
- `public Optional<Workout> longest()` → dakikası en büyük antrenman; liste boşsa boş `Optional`. **Döngü yasak.**
- `public Map<Sport, Integer> minutesBySport()` → spor → o spordaki toplam dakika. Key'ler **enum sırasıyla** (`RUN`, `BIKE`, `SWIM`) gelsin. **Döngü yasak.**
  - İpucu: `Collectors.summingDouble`'ın `int` hâli `Collectors.summingInt`. Sıra için Gün 20'deki TreeMap kalıbı (enum'ların doğal sırası = tanımlandıkları sıra).
- `public long activeDays(LocalDate from, LocalDate to)` → `from` ile `to` arasında (**ikisi de dahil**) antrenman yapılmış **farklı gün** sayısı. Aynı gün 2 antrenman varsa 1 gün sayılır. **Döngü yasak.**
  - `from`, `to`'dan sonraysa → `IllegalArgumentException("From must not be after to")`
- `public void saveTo(Path path) throws IOException` → her antrenman bir satır (`toCsv`)
- `public static WorkoutLog loadFrom(Path path) throws IOException` → dosyayı okuyup yeni bir `WorkoutLog` döndürür (döngü serbest)

#### 4. `Exam1.java` → `main`, bu sırayla:
1. `WorkoutLog` oluştur, şu 5 antrenmanı ekle:
   ```
   2026-10-26 RUN  30
   2026-10-26 SWIM 45
   2026-10-28 BIKE 60
   2026-10-30 RUN  40
   2026-11-01 BIKE 50
   ```
2. `new Workout(LocalDate.of(2026, 10, 31), Sport.RUN, 0)` dene → yakala, `getMessage()`'ı yazdır
3. `Workout.fromCsv("2026-10-31,YOGA,20")` dene → yakala, `getMessage()`'ı yazdır
4. `"Total: " + totalMinutes()` yazdır
5. `bySport(Sport.RUN)` sonucunu **satır satır** yazdır
6. `"Longest: " + longest()` yazdır
7. `"Longest in empty log: " + new WorkoutLog().longest()` yazdır
8. `minutesBySport()` yazdır
9. `"Active days: " + activeDays(2026-10-26, 2026-10-30)` yazdır
10. `"Active days: " + activeDays(2026-10-27, 2026-10-31)` yazdır
11. `activeDays(2026-11-01, 2026-10-01)` dene → yakala, mesajı yazdır
12. `saveTo(Path.of("workouts.csv"))`, sonra `"Saved " + size() + " workouts"` yazdır
13. `WorkoutLog.loadFrom(...)` ile aynı dosyadan yeni bir log oku, `"Loaded 5 workouts, total 225"` formatında yazdır (sayılar yeni log'dan gelsin)

---

**C2 (15 puan) — `Exam2.java`:** `static List<String> topKFrequent(String[] words, int k)`
En çok geçen `k` kelimeyi döndür. Sıra: **tekrar sayısı büyükten küçüğe**; tekrar sayısı eşitse **alfabetik**.

```
["i","love","leetcode","i","love","coding"], k=2                     → [i, love]
["the","day","is","sunny","the","the","the","sunny","is","is"], k=4  → [the, is, sunny, day]
["b","a","c","a","b","c"], k=2                                        → [a, b]
```
- İpucu: say (`groupingBy` + `counting`, ya da elle `Map`) → kelimeleri bir `List`'e al → `Comparator` ile sırala → ilk `k` tanesini al.
- `main`'de yukarıdaki 3 örneği yazdır.
- Sınavdan sonra: bu **LeetCode 692** (Medium), gönderebilirsin.

---

**C3 (10 puan) — `Exam3.java`:** `static int countWeekendDays(LocalDate from, LocalDate to)`
`from` ile `to` arasındaki (**ikisi de dahil**) Cumartesi + Pazar sayısı.

```
2026-10-26 → 2026-11-01   → 2
2026-11-01 → 2026-11-01   → 1
2026-10-01 → 2026-10-31   → 9
2026-11-02 → 2026-11-06   → 0
```
- `from`, `to`'dan sonraysa → `IllegalArgumentException("From must not be after to")`
- Döngü serbest.
- `main`'de 4 örneği + exception durumunu (yakala, mesajı yazdır) göster.

---

## ☕ Mola (10 dk)

Sınav bitince `SINAV.md` + kodları kaydet ve chat'e "bitti" yaz. Ben kontrol ederken sen concurrency'ye geç.

---

## 2. Concurrency'ye giriş (40 dk)

### 2.1 Neden? ⭐⭐⭐

Şimdiye kadar yazdığın her program **tek işçi** ile çalıştı: `main` satır satır gider, biri bitmeden diğeri başlamaz. Bu işçinin adı **thread**.

Bir web sunucusunda (Spring Boot = içinde Tomcat) 100 kullanıcı aynı anda istek atar. Sunucu her isteği **ayrı bir thread**'de çalıştırır. Yani Hafta 7'den itibaren yazacağın her `@Service` aynı anda birden fazla thread tarafından çağrılabilir. Bugünün tek sorusu: **iki thread aynı nesneye aynı anda dokunursa ne olur?**

### 2.2 Thread başlatmak ⭐⭐⭐

```java
Runnable task = () -> System.out.println("Hello from " + Thread.currentThread().getName());

Thread t = new Thread(task, "worker");   // 2. argüman = thread'in adı
t.start();                               // YENİ thread açar, task orada çalışır
t.join();                                // main bekler: "worker bitene kadar ilerleme"
System.out.println("Done");
```

- `Runnable` = **functional interface** (Gün 19): parametre almaz, bir şey döndürmez → `() -> { ... }`
- **`start()`** → yeni thread açar, `run()`'ı **orada** çağırır.
- **`run()`**'ı kendin çağırırsan → yeni thread açılmaz, normal metot çağrısı gibi **main**'de çalışır. (Klasik mülakat sorusu.)
- **`join()`** → "bu thread bitene kadar bekle". `InterruptedException` (checked) fırlatabilir → `main`'e `throws InterruptedException` yaz.
- `join` yoksa main beklemez; iki thread'in satırları **hangi sırayla** basılır, garanti yok.

### 2.3 Race condition: `count++` aslında 3 adım ⭐⭐⭐

```
count++  =  1) count'u oku (5)   2) 1 ekle (6)   3) count'a yaz (6)
```

İki thread aynı anda:
```
Thread A: oku → 5
Thread B: oku → 5
Thread A: yaz → 6
Thread B: yaz → 6        ← iki artış oldu, sonuç sadece 1 arttı. Bir artış KAYBOLDU.
```

Buna **race condition** denir: sonuç, thread'lerin hangi sırayla koştuğuna bağlı. Her çalıştırmada farklı, yanlış bir sayı çıkar ve hata vermez — bu yüzden çok tehlikeli.

### 2.4 İki çözüm ⭐⭐

```java
// 1) synchronized: "bu metoda aynı anda sadece BİR thread girebilir"
public synchronized void increment() { count++; }

// 2) AtomicInteger: oku-ekle-yaz'ı tek, bölünmez adımda yapan sayaç
private final AtomicInteger count = new AtomicInteger();
public void increment() { count.incrementAndGet(); }
public int getCount() { return count.get(); }
```

- `synchronized` genel çözüm (birden fazla satırı korur), ama thread'ler sırayla bekler → yavaşlar.
- Tek bir sayaç için `AtomicInteger` daha basit ve hızlı.
- **En iyi çözüm çoğu zaman paylaşılan değiştirilebilir veri hiç olmaması:** record + immutable nesneler (Hafta 4'te bu yüzden `List.copyOf`, record, `LocalDate` gördük). Değişmeyen nesneyi 100 thread aynı anda okusa da sorun yok.

### 2.5 Gerçek hayatta (sadece bil) ⭐

Elle `new Thread` pek yazılmaz. Java'da **thread havuzu** (`ExecutorService`) var, Spring de bunu senin yerine yönetir. Java 21+ ile gelen **virtual thread**'ler, binlerce isteği ucuza aynı anda çalıştırmayı sağlıyor. İkisini de Spring tarafında tekrar göreceğiz; bugünlük kavramlar yeter.

---

## 📝 Kağıda Yaz

```
HAFTA 4 + CONCURRENCY
1. Lambda = tek metotluk interface'i kısa yazmak. Predicate→boolean, Function→dönüştür,
   Consumer→al-tüket, Supplier→üret. Dışarıdaki değişkeni değiştiremez.
2. Stream: filter/map/sorted (ara) → toList/count/sum/collect (bitiş). Tek kullanımlık.
3. Optional'ı yazdırırsan kutu görünür; orElse/orElseThrow açar.
4. valueOf yanlış isim → IAE · orElseThrow boş → NoSuchElement · parse → DateTimeParse
5. thread.start() = yeni thread · run() = aynı thread · join() = bitmesini bekle
6. count++ = oku+ekle+yaz → iki thread → artış kaybolur (race condition)
   Çözüm: synchronized · AtomicInteger · en iyisi: paylaşılan değişen veri olmasın
```

---

## 💪 EGZERSİZLER (concurrency, ~25 dk)

### E1 — `ThreadDemo.java` (tahmin)

`main`'e yaz (`main`'e `throws InterruptedException` ekle). **Çalıştırmadan önce** 3 satırın çıktısını chat'e yaz, sonra çalıştır.

```java
Runnable task = () -> System.out.println("Hello from " + Thread.currentThread().getName());
Thread t = new Thread(task, "worker");

t.run();
t.start();
t.join();
System.out.println("Done in " + Thread.currentThread().getName());
```

Soru (chat'e, 1 cümle): İlk satırda neden `worker` yazmadı?

### E2 — `RaceDemo.java` (kod yaz)

**`Counter`** (aynı dosyada, `RaceDemo`'nun içinde `static class Counter` olabilir — bu senin tasarım kararın)
- Field: `private int count = 0;`
- `public void increment()` → `count++`
- `public int getCount()`

**`RaceDemo.main`** (`throws InterruptedException`):
1. Bir `Counter` oluştur
2. Bir `Runnable` yaz: döngüyle `increment()`'i **1_000_000** kez çağırsın
3. Bu `Runnable` ile **iki** `Thread` oluştur, ikisini de `start()` et, ikisini de `join()` et
4. `"Count: " + counter.getCount()` yazdır

Önce tahmin: kaç yazar? Sonra programı **3 kez** çalıştır, 3 sayıyı chat'e yaz.

Sonra:
- **(b)** `increment()`'e `synchronized` ekle, tekrar 3 kez çalıştır. Ne değişti?
- **(c)** `synchronized`'ı geri al; `count`'u `AtomicInteger` yap (`incrementAndGet()` / `get()`). Tekrar çalıştır.

Soru (chat'e, 1-2 cümle): (a)'da sayı neden 2_000_000'dan küçük, ve neden her seferinde farklı?

---

## 🔧 GIT (10 dk)

```bash
git add hafta-04/gun-06
git status            # sadece .java ve .md (workouts.csv, b6.txt, .class yok)
git commit -m "feat: week 4 mini exam + concurrency intro"
git push
```

`workouts.csv` `git status`'ta görünürse `.gitignore`'a ekle.

Ben notu verdikten sonra: `ILERLEME.md` → Gün 24 logu + **Haftalık Kontrol tablosunda Hafta 4 satırı** (mini sınav puanı + bir cümle not) → `docs: day 24 progress log`.

---

## ✅ Gün Sonu

- [ ] Tekrar 30 dk (Kağıda Yaz kutuları + 1.1–1.4)
- [ ] Sınav: `SINAV.md` A + B, C1 (`Sport`, `Workout`, `WorkoutLog`, `Exam1`) + `Exam2` + `Exam3`
- [ ] Not verildikten sonra hataları birlikte düzelt
- [ ] Concurrency 2.1–2.5 + E1 + E2 (a, b, c)
- [ ] "Kağıda Yaz" kutusu
- [ ] Commit + push, `ILERLEME.md` Gün 24 + Hafta 4 satırı

**Sıradaki:** Hafta 5 — SQL + PostgreSQL. Proje 0 bitti; yarından itibaren veriler dosyada değil veritabanında.
