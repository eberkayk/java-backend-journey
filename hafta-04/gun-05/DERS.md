# Hafta 4 · Gün 5 (Gün 23) — Maven + README · Proje 0 Teslimi

**Hedef:** 4 gündür yazdığın Gider Takip, `hafta-04/gun-0X/` klasörlerinde dağınık duran bir alıştırma. Bugün onu **gerçek bir proje** yapıyoruz:
- **Maven** → standart klasör yapısı, `pom.xml`, tek komutla derle + paketle → çalışan bir `.jar`
- **Komut satırı uygulaması** → `java -jar expense-tracker-1.0.0.jar add 2026-10-31 Coffee FOOD 45.5` gibi gerçekten kullanılabilir
- **README** → GitHub'da projeye bakan biri (işveren!) 1 dakikada ne olduğunu ve nasıl çalıştırılacağını anlasın

Bugünün sonunda `projects/expense-tracker/` repoda, CV'ye yazılabilir ilk projen olarak duruyor. (Spring Boot projeleri de **birebir bu yapıda** — Hafta 7'de tanıdık gelecek.)

**Süre:** ~4,5 saat · Isınma 15 dk · Ders 40 dk · Maven kurulum 45 dk · Proje v5 1 sa · README 30 dk · Algoritma 45 dk · Kapanış 15 dk

**Kurallar (değişmedi):**
- Sınıf, metot, değişken ve ekran mesajları **İngilizce** (README de İngilizce)
- **Değişiklik → derle → çalıştır → çıktının her satırını oku.**
- **Metot adı/imzası görevde nasılsa öyle.**
- Tahminler **çalıştırmadan önce** chat'e.
- Testleri Claude yazıp çalıştırıyor. Sen yazmıyorsun.

---

## 0. Isınma — dünden 5 soru (chat'e, kısa)

**S1.** Üç satırın çıktısı ne?
```java
Optional<String> o = Optional.of("Cem");
System.out.println(o);
System.out.println(o.orElse("none"));
System.out.println(Optional.empty());
```

**S2.** `enum Category { FOOD, HOME }`. Her satır hangi exception'ı fırlatır?
```java
Category.valueOf("food");               // A
Optional.empty().orElseThrow();         // B
```

**S3.** Çıktı ne?
```java
System.out.println(ChronoUnit.DAYS.between(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31)));
```

**S4.** İki satırın çıktısı ne?
```java
LocalDate d = LocalDate.of(2026, 10, 31);
d.plusDays(1);
System.out.println(d);
d = d.plusMonths(1);
System.out.println(d);
```

**S5.** Son satır ne yazdırır?
```java
Path p = Path.of("x.txt");
Files.write(p, List.of("a", "b"));
Files.write(p, List.of("c"));
System.out.println(Files.readAllLines(p));
```

---

## 1. Maven neden var? ⭐⭐⭐ (10 dk)

Şu ana kadar her şeyi IntelliJ'nin "Run" düğmesi yaptı. Ama:
- IntelliJ'si olmayan biri (işveren, sunucu, CI) projeni **nasıl derleyecek**?
- Spring Boot, PostgreSQL sürücüsü, JUnit gibi **başkalarının yazdığı kütüphaneleri** (jar dosyalarını) projeye nasıl ekleyeceksin? İnternetten indirip elle kopyalayarak mı?

Maven bu iki işi yapar:

| İş                     | Maven'sız                                 | Maven'la                                                     |
| ---------------------- | ----------------------------------------- | ------------------------------------------------------------ |
| Derleme + paketleme    | IntelliJ'ye bağımlı, herkes farklı yapar  | Tek komut: `mvn package` → `target/xxx.jar`                  |
| Kütüphane (dependency) | jar'ı bul, indir, elle ekle, sürüm takibi | `pom.xml`'e 5 satır yaz, Maven indirir (Hafta 6'da JUnit'le) |
| Klasör yapısı          | Herkes kafasına göre                      | **Standart**: her Java projesi aynı yerde aynı şeyi tutar    |

Projenin her şeyi **tek bir dosyada** yazar: **`pom.xml`** (Project Object Model).

> Rakibi **Gradle** (aynı işi yapar, farklı dosya formatı). Türkiye'deki ve dünyadaki Spring ilanlarının çoğunda Maven. Biz Maven'la gidiyoruz.

---

## 2. Standart klasör yapısı ⭐⭐⭐ (5 dk)

```
expense-tracker/                          ← proje kökü
├── pom.xml                               ← projenin kimliği + ayarları
├── README.md
├── src/
│   ├── main/
│   │   └── java/                         ← SENİN KODUN burada başlar
│   │       └── com/eberkayk/expense/     ← paket = klasör
│   │           ├── Category.java
│   │           └── ...
│   └── test/
│       └── java/                         ← testler (Hafta 6)
└── target/                               ← Maven'ın ÇIKTISI (.class, .jar). Git'e girmez.
```

**Paket adı = klasör yolu.** `package com.eberkayk.expense;` yazan bir dosya **mutlaka** `src/main/java/com/eberkayk/expense/` içinde olmalı. (Şimdiye kadar `w4d4` gibi tek kelimelik paketler kullandık. Gerçek projelerde paket adı ters çevrilmiş alan adıyla başlar: `com.firma.proje`. Senin alan adın yok; GitHub kullanıcı adını kullanıyoruz.)

---

## 3. `pom.xml` — satır satır ⭐⭐⭐ (10 dk)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>                <!-- hep 4.0.0, ezberleme -->

    <groupId>com.eberkayk</groupId>                   <!-- KİM: sen / firman -->
    <artifactId>expense-tracker</artifactId>          <!-- NE: projenin adı -->
    <version>1.0.0</version>                          <!-- HANGİ SÜRÜM -->
    <packaging>jar</packaging>                        <!-- çıktı tipi: .jar -->

    <properties>
        <maven.compiler.release>25</maven.compiler.release>               <!-- Java 25 ile derle -->
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <build>
        <plugins>
            <plugin>                                  <!-- jar'ı paketleyen eklenti -->
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>3.4.2</version>
                <configuration>
                    <archive>
                        <manifest>
                            <!-- java -jar ile açılınca HANGİ main çalışsın -->
                            <mainClass>com.eberkayk.expense.ExpenseCli</mainClass>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

**groupId + artifactId + version = GAV.** Dünyadaki her Maven kütüphanesi bu üçlüyle bulunur. Hafta 6'da JUnit'i eklerken şöyle bir blok yazacaksın — gördüğün gibi aynı üçlü:

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.11.0</version>
</dependency>
```

`mainClass` = **paket adı + class adı** (tam ad). Yanlış yazarsan jar oluşur ama `java -jar` çalışmaz.

---

## 4. Lifecycle — Maven komutları ⭐⭐⭐ (5 dk)

Maven'ın aşamaları sırayla çalışır. Birini çağırınca **öncekilerin hepsi** de çalışır:

```
validate → compile → test → package → install
                                  ▲
                    mvn package = hepsi buraya kadar
```

| Komut               | Ne yapar                                                            |
| ------------------- | ------------------------------------------------------------------- |
| `mvn compile`       | `src/main/java` → `target/classes/*.class`                          |
| `mvn test`          | compile + `src/test/java`'daki testleri çalıştırır (bugün test yok) |
| `mvn package`       | test + `target/expense-tracker-1.0.0.jar` oluşturur                 |
| `mvn clean`         | `target/` klasörünü siler (temiz başlangıç)                         |
| `mvn clean package` | önce sil, sonra baştan paketle — **en sık kullanacağın**            |

Jar adı nereden geliyor? `artifactId` + `-` + `version` + `.jar`.

### Maven Wrapper (`mvnw`)

Maven bilgisayarında kurulu değil (IntelliJ'nin içinde gömülü bir tane var). Projeyi indiren birinde de kurulu olmayabilir. **Wrapper** küçük bir script'tir: projeyle birlikte gelir, gereken Maven sürümünü kendisi indirir.

```
mvn clean package          ← Maven kuruluysa
.\mvnw.cmd clean package   ← Windows, wrapper ile (kurulum gerekmez)
./mvnw clean package       ← Linux/Mac, wrapper ile
```

Spring Initializr'dan indirdiğin her Spring projesinde `mvnw` hazır gelir. Bugün kendin oluşturacaksın.

---

## 5. Komut satırı argümanları: `args` ⭐⭐⭐ (5 dk)

23 gündür yazdığın `main(String[] args)`'taki `args` nihayet işe yarıyor:

```
java -jar app.jar add 2026-10-31 "Bus card" TRANSPORT 15
                  │   │          │          │         │
args:           [0] [1]        [2]        [3]       [4]
                "add" "2026-10-31" "Bus card" "TRANSPORT" "15"
```

- Argümanlar **boşlukla** ayrılır. İçinde boşluk olan argüman **tırnak** içinde yazılır (`"Bus card"` tek argüman, tırnaklar `args`'a girmez).
- Hepsi **String**. `"15"` → `Double.parseDouble`, `"2026-10-31"` → `LocalDate.parse`, `"TRANSPORT"` → `Category.valueOf`.
- Hiç argüman yoksa `args.length == 0` (null değil, boş dizi).

String'e göre `switch` (enum'daki arrow `switch`'in aynısı, `case`'lere String yazıyorsun):

```java
switch (args[0]) {
    case "list" -> { ... }
    case "add"  -> { ... }
    default     -> System.out.println("Unknown");
}
```

---

## 6. Bugünün 3 yeni küçük API'si ⭐⭐ (5 dk)

| Kod                                    | Sonuç                                          |
| -------------------------------------- | ---------------------------------------------- |
| `YearMonth.parse("2026-02")`           | `2026-02` (`yyyy-MM` formatı)                  |
| `YearMonth.of(2026, 2).atDay(1)`       | `2026-02-01` (`LocalDate`)                     |
| `YearMonth.of(2026, 2).atEndOfMonth()` | `2026-02-28` (ayın son günü, artık yılı bilir) |
| `Files.exists(path)`                   | dosya var mı → `boolean`                       |

**Hangi parse hatası hangi exception?** `add` komutu kullanıcıdan gelen 3 String'i çeviriyor; yanlış girilirse:

| Çağrı                           | Kötü girdi    | Exception                  | Kimin alt class'ı                           |
| ------------------------------- | ------------- | -------------------------- | ------------------------------------------- |
| `Category.valueOf("food")`      | küçük harf    | `IllegalArgumentException` | —                                           |
| `Double.parseDouble("abc")`     | sayı değil    | `NumberFormatException`    | `IllegalArgumentException`'ın               |
| `LocalDate.parse("30.10.2026")` | yanlış format | `DateTimeParseException`   | `DateTimeException`'ın (**IAE'nin değil!**) |
| `YearMonth.parse("2026-13")`    | 13. ay yok    | `DateTimeParseException`   | `DateTimeException`'ın                      |

Yani `catch (IllegalArgumentException e)` ilk ikisini yakalar ama tarihi **yakalamaz** → program çöker. İkisini birden yakalamak için Hafta 3 Gün 1'deki multi-catch:

```java
} catch (IllegalArgumentException | DateTimeException e) {
```

(`DateTimeException` import'u: `java.time.DateTimeException`.)

---

## 📝 Kağıda Yaz

```
Maven: pom.xml = projenin kimliği.  GAV = groupId:artifactId:version
  src/main/java/com/x/y/ ← paket = klasör   src/test/java ← testler
  target/ ← çıktı, git'e girmez
  mvn clean package → target/<artifactId>-<version>.jar
  compile → test → package (öncekiler de çalışır)
  mvnw = wrapper, Maven kurulu olmadan çalıştırır
  mainClass (jar plugin) → java -jar ile açılan class
args[]: boşlukla ayrılır, "tırnak" = tek argüman, hepsi String
DateTimeParseException ≠ IAE → catch (IAE | DateTimeException e)
```

---

## ⚙️ ADIM 1 — Maven projesini kur (~45 dk)

### 1.1 JAVA_HOME'u düzelt (bir kere, 5 dk)

Senin bilgisayarında `java` komutu **25**, ama `JAVA_HOME` ortam değişkeni hâlâ **jdk-17**'yi gösteriyor. Maven (wrapper dahil) `JAVA_HOME`'a bakar → `release version 25 not supported` hatası verir. Ben denedim, bu hatayı aldım.

1. Windows arama → **"ortam değişkenlerini düzenle"** (Sistem ortam değişkenlerini düzenleyin) → **Ortam Değişkenleri...**
2. `JAVA_HOME`'u bul (Kullanıcı ya da Sistem bölümünde) → **Düzenle** → değeri `C:\Program Files\Java\jdk-25` yap → Tamam
3. IntelliJ'yi **tamamen kapat ve yeniden aç** (terminal eski değeri hatırlar)
4. IntelliJ terminalinde kontrol: `echo $env:JAVA_HOME` → `C:\Program Files\Java\jdk-25`

### 1.2 Klasörleri ve `pom.xml`'i oluştur (15 dk)

1. Repo kökünde (`C:\dev\roadmap backend`) yeni klasör: `projects` → içine `expense-tracker`
2. `expense-tracker/` içine `pom.xml` oluştur → **Bölüm 3'teki pom'u yaz** (yorumları yazmana gerek yok)
3. `expense-tracker/src/main/java/com/eberkayk/expense/` klasörlerini oluştur (IntelliJ'de `src/main/java`'ya sağ tık → New → Package → `com.eberkayk.expense`)
4. IntelliJ'de `pom.xml`'e sağ tık → **"Add as Maven Project"**. Sağ kenarda **Maven** penceresi (m ikonu) çıkacak, içinde `expense-tracker`. `java` klasörü mavi (Sources Root) oldu mu, bak.

### 1.3 Kodu taşı (10 dk)

1. `hafta-04/gun-04/w4d4/` içinden **`Category`, `Expense`, `ExpenseTracker`**'ı `com/eberkayk/expense/` içine **kopyala** (taşıma — eski günler olduğu gibi kalsın)
2. Üç dosyanın ilk satırını `package com.eberkayk.expense;` yap
3. `ExpenseApp`, `FileDemo`, `Algo*`, `DatePredict` **kopyalanmıyor**. Proje sadece ürünün kendisi.

### 1.4 Maven ile derle (10 dk)

1. Maven penceresi → `expense-tracker` → **Lifecycle** → `clean`'e çift tık, sonra `package`'a çift tık
2. Alttaki çıktıda **`BUILD SUCCESS`** görmelisin. İlk sefer internetten eklenti indirdiği için 1-2 dk sürebilir.
3. `target/` klasörü oluştu mu? İçinde `expense-tracker-1.0.0.jar` var mı?

Bu aşamada `ExpenseCli` henüz yok, yani jar oluşur ama `java -jar` ile çalışmaz. Sorun değil.

### 1.5 Wrapper'ı oluştur (5 dk)

1. Maven penceresinin üstündeki **"Execute Maven Goal"** düğmesi (içinde `m` olan ikon) → kutuya `mvn wrapper:wrapper` yaz → Enter
2. Proje kökünde oluşanlar: `mvnw`, `mvnw.cmd`, `.mvn/wrapper/maven-wrapper.properties`
3. IntelliJ terminalinde:
```powershell
cd "C:\dev\roadmap backend\projects\expense-tracker"
.\mvnw.cmd clean package
```
→ `BUILD SUCCESS`. Artık IntelliJ'siz de derleniyor.

**Hata alırsan** çıktının `[ERROR]` ile başlayan **ilk** satırını chat'e yapıştır.

---

## 🏛️ PROJE 0 — Gider Takip v5: komut satırı uygulaması (~1 saat)

Şimdiye kadar `ExpenseApp` her çalışmada aynı 7 gideri kodun içinden ekliyordu. Artık kullanıcı **komutla** ekleyecek, veri `expenses.csv`'de **kalıcı** duracak.

`Category`, `Expense`, `ExpenseTracker`'da değişiklik **yok**.

### `ExpenseCli` (yeni class, `com.eberkayk.expense` paketinde)

**Alanlar** (ikisi de `private static final`), adı, tipi ve değeri tam olarak bu:

```
FILE   : Path   = Path.of("expenses.csv")
USAGE  : String = "Usage: add <yyyy-MM-dd> <description> <CATEGORY> <amount> | list | summary | month <yyyy-MM>"
```

(USAGE'daki `|` işaretleri metnin parçası, olduğu gibi yaz.)

**Metot:** sadece `public static void main(String[] args)` — imzada `throws` **yok**, hatalar içeride yakalanıyor.

**`main` ne yapar, sırayla:**

1. `args.length == 0` → `USAGE` yazdır, `return`
2. Geri kalan her şey **tek bir `try`** içinde:
   - `FILE` varsa (`Files.exists`) → `ExpenseTracker.loadFrom(FILE)`; yoksa → `new ExpenseTracker()` (ilk çalıştırmada dosya yok)
   - `args[0]`'a göre `switch`:

| Komut              | Argüman sayısı (`args.length`)  | Ne yapar                                                                                                                                                |
| ------------------ | ------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `add`              | tam 5, değilse `USAGE` + return | `args[1..4]`'ten `Expense` oluştur (Bölüm 6 tablosu), tracker'a ekle, `saveTo(FILE)`, `Added: <expense>` yazdır                                         |
| `list`             | (kontrol yok)                   | Giderleri **tarihe göre artan** yazdır, her biri ayrı satır (tracker'daki `sorted` metodun var). Hiç gider yoksa `No expenses`                          |
| `summary`          | (kontrol yok)                   | 4 satır: `Count: <sayı>`, `Total: <toplam>`, `By category: <totalsByCategory>`, `By month: <totalsByMonth>`                                             |
| `month`            | tam 2, değilse `USAGE` + return | `YearMonth.parse(args[1])` → o ayın 1'i ile son günü arasındaki giderler (`between` + Bölüm 6), her biri ayrı satır, en sonda `Total: <o ayın toplamı>` |
| başka herhangi bir | —                               | `USAGE` yazdır                                                                                                                                          |

3. `catch` blokları:
   - `IllegalArgumentException | DateTimeException` → `Error: ` + `getMessage()`
   - `IOException` → `File error: ` + `getMessage()`

`Total` için tracker'daki `total(Predicate)` metodunu kullanabilirsin (hepsi için: `e -> true`). `month`'un toplamını nasıl hesaplayacağın **senin tasarım kararın** (stream ya da döngü).

### Çalıştır — terminalden, jar ile

```powershell
cd "C:\dev\roadmap backend\projects\expense-tracker"
.\mvnw.cmd clean package
java -jar target\expense-tracker-1.0.0.jar list
```

Kod her değiştiğinde **önce `mvnw.cmd clean package`**, sonra `java -jar`. (Paketlemezsen eski jar çalışır — "düzelttim ama değişmedi" diye şaşırma.)

**Test senaryosu.** Önce `projects/expense-tracker/expenses.csv` varsa sil. Sonra bu 16 komutu **sırayla** çalıştır (her biri `java -jar target\expense-tracker-1.0.0.jar` + aşağıdaki argümanlar):

```
(argümansız)
list
add 2026-10-12 Coffee FOOD 45.5
add 2026-10-01 Rent HOME 12000
add 2026-09-28 "Bus card" TRANSPORT 15
list
summary
month 2026-10
month 2026-11
add 2026-10-05 Tea food 10
add 2026-10-05 Tea FOOD abc
add 30.10.2026 Tea FOOD 10
add 2026-10-05 Tea FOOD -3
add 2026-10-05 "Tea, milk" FOOD 3
add 2026-10-05 Tea
delete
```

**Beklenen çıktı** (komut komut):
```
Usage: add <yyyy-MM-dd> <description> <CATEGORY> <amount> | list | summary | month <yyyy-MM>
No expenses
Added: 2026-10-12 Coffee (Food) 45.5
Added: 2026-10-01 Rent (Home) 12000.0
Added: 2026-09-28 Bus card (Transport) 15.0
2026-09-28 Bus card (Transport) 15.0
2026-10-01 Rent (Home) 12000.0
2026-10-12 Coffee (Food) 45.5
Count: 3
Total: 12060.5
By category: {FOOD=45.5, HOME=12000.0, TRANSPORT=15.0}
By month: {2026-09=15.0, 2026-10=12045.5}
2026-10-01 Rent (Home) 12000.0
2026-10-12 Coffee (Food) 45.5
Total: 12045.5
Total: 0.0
Error: No enum constant com.eberkayk.expense.Category.food
Error: For input string: "abc"
Error: Text '30.10.2026' could not be parsed at index 0
Error: Amount must be positive
Error: Description must not contain commas
Usage: add <yyyy-MM-dd> <description> <CATEGORY> <amount> | list | summary | month <yyyy-MM>
Usage: add <yyyy-MM-dd> <description> <CATEGORY> <amount> | list | summary | month <yyyy-MM>
```

Sonunda `expenses.csv`'de **sadece 3 satır** olmalı (hatalı `add`'ler kaydedilmedi). Kontrol: `Get-Content expenses.csv`

> Bu çıktıyı ben aynı spesifikasyonla yazdığım referans kodla, gerçek `mvnw` + `java -jar` ile çalıştırıp aldım.

### Proje sonu düşünme soruları (chat'e, 1-2 cümle)

1. `add 2026-10-05 Tea FOOD abc` → `Error: For input string: "abc"`. Hangi satır, hangi exception'ı fırlattı ve neden `catch`'in **IAE** tarafı onu yakaladı?
2. **Tahmin sorusu (bilmemen normal):** `pom.xml`'den `<mainClass>` satırını silip `clean package` yapsan, `java -jar ... list` ne der? (İpucu: jar açılınca hangi class'ın `main`'ini çalıştıracağını nereden biliyordu?) İstersen gerçekten dene, sonra geri koy.

---

## 📄 README (~30 dk)

`projects/expense-tracker/README.md` — **İngilizce**, sen yazıyorsun. GitHub'da projeni açan biri bunu ilk görür. Kısa ve net olsun; her bölüm 1-5 satır.

Bu başlıklar, **bu sırayla**:

| Başlık                 | İçine ne yazacaksın                                                                                        |
| ---------------------- | ---------------------------------------------------------------------------------------------------------- |
| `# Expense Tracker`    | Altına 1 cümle: ne yapan bir uygulama?                                                                     |
| `## Features`          | 4 komutun her biri bir madde (`add`, `list`, `summary`, `month`) + "data is saved to a CSV file"           |
| `## Tech Stack`        | Java 25, Maven, ve kullandığın Java özellikleri (record, enum, Stream API, Optional, java.time, NIO Files) |
| `## Requirements`      | Hangi JDK gerekli                                                                                          |
| `## Build`             | Derleme komutu, kod bloğu içinde (Windows ve Linux/Mac ikisi de)                                           |
| `## Usage`             | Her komut için **bir örnek komut + gerçek çıktısı** (kendi terminalinden kopyala), kod blokları içinde     |
| `## Data Storage`      | Dosya adı, nerede oluşuyor, bir satırın formatı (örnek satırla)                                            |
| `## Project Structure` | `src/main/java/...` altındaki 4 dosya, her birinin yanında ne yaptığı (kısa)                               |

Kod blokları için üç ters tırnak: <code>```bash</code> ... <code>```</code>

Bitince IntelliJ'de README'ye sağ üstten **Preview** ile bak: GitHub'da böyle görünecek.

**Repo kökündeki `README.md`'ye** (Türkçe olan) en alta bir bölüm ekle:
```
## Projeler

| Proje                                        | Hafta | Ne                                          |
| -------------------------------------------- | ----- | ------------------------------------------- |
| [Expense Tracker](projects/expense-tracker/) | 1–4   | Konsol gider takip (Maven, CSV, Stream API) |
```

---

## 🧩 ALGORİTMA SAATİ (45 dk) — Tarih metinleri

**A1 — [LeetCode 1507: Reformat Date](https://leetcode.com/problems/reformat-date/)**
`String reformatDate(String date)`: `"20th Oct 2052"` → `"2052-10-20"`.
- Örnekler: `"20th Oct 2052"` → `"2052-10-20"` · `"6th Jun 1933"` → `"1933-06-06"` · `"26th May 1960"` → `"1960-05-26"`
- `split(" ")` ile 3 parça: gün (`"20th"`), ay (`"Oct"`), yıl.
- Gün: son 2 harfi (`th`/`st`/`nd`/`rd`) at → `substring`. `"6"` → `"06"` olmalı: tek haneliyse başına `"0"`.
- Ay: `"Jan"` → `"01"` ... 12 ay için **senin tasarım kararın** (bir dizi + `indexOf` döngüsü, ya da `Map`, ya da `switch`).
- Kodlamadan önce chat'e: `"6th Jun 1933"` için 3 parça ne, her parça neye dönüşüyor?

**A2 — [LeetCode 1185: Day of the Week](https://leetcode.com/problems/day-of-the-week/)**
`String dayOfTheWeek(int day, int month, int year)`: o tarih haftanın hangi günü?
- Örnekler: `31, 8, 2019` → `"Saturday"` · `18, 7, 1999` → `"Sunday"` · `15, 8, 1993` → `"Sunday"`
- **`java.time` serbest.** Dünkü dersten: `getDayOfWeek()` bir enum döndürür.
- Sorun: enum `SATURDAY` yazdırır, LeetCode `"Saturday"` istiyor. İlk harf büyük, gerisi küçük → `substring` + `toUpperCase`/`toLowerCase` (Gün 5'ten).
- LeetCode'da en üste `import java.time.*;` yazabilirsin.

Klasör: `hafta-04/gun-05/w4d5/` · Paket: `package w4d5;` · `Algo1.java`, `Algo2.java` (dünkü düzen).

---

## 🔧 GIT (10 dk)

`.gitignore`'da `target/` ve `expenses.csv` zaten var (her klasörde geçerli). Ekleme yok.

```bash
git add projects/expense-tracker hafta-04/gun-05 README.md
git status
```
`git status`'ta **olması gerekenler**: `pom.xml`, `mvnw`, `mvnw.cmd`, `.mvn/wrapper/maven-wrapper.properties`, `src/...` altındaki 4 `.java`, proje `README.md`'si, kök `README.md`, `hafta-04/gun-05/` dosyaları.
**Olmaması gerekenler**: `target/`, `expenses.csv`, `.class`, `.iml`.

```bash
git commit -m "feat: project 0 - expense tracker as maven cli app"
git push
```
Sonra `ILERLEME.md`'ye Gün 23 logu → `docs: day 23 progress log`.

Push'tan sonra GitHub'da `projects/expense-tracker`'ı aç: README düzgün görünüyor mu?

---

## ✅ Gün Sonu

- [ ] Isınma S1–S5
- [ ] Bölüm 1–6 okundu
- [ ] JAVA_HOME → jdk-25
- [ ] `projects/expense-tracker` Maven projesi, `mvnw.cmd clean package` → `BUILD SUCCESS`
- [ ] `ExpenseCli` — 16 komutun çıktısı birebir aynı, `expenses.csv`'de 3 satır
- [ ] Proje düşünme soruları
- [ ] Proje README'si + kök README'de Projeler tablosu
- [ ] A1 + A2
- [ ] "Kağıda Yaz" kutusu kağıtta
- [ ] Commit + push, `ILERLEME.md` Gün 23

**Yarın oturum başında soracaklarım:** 4-5 kısa soru (kod örnekli): GAV, `mvn package` hangi aşamaları çalıştırır, jar adı, `args` + tırnak, `DateTimeParseException` neden IAE catch'ine düşmez.

**Sıradaki:** Hafta 4 Gün 6 (Gün 24): Hafta 4 mini sınavı + temel concurrency kavramı (thread nedir — sadece fikir).
