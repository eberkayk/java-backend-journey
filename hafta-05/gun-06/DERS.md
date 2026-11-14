# Hafta 5 · Gün 6 (Gün 30) — NOTSUZ MİNİ PROJE: Stok Takip CLI (Maven, sıfırdan)

**Hedef:** Hafta 3–4'te öğrendiğin her şeyi **tek projede, notsuz** kullanmak:
collections + custom exception + stream + record/enum + java.time + dosya I/O + Maven jar.
Yarın (Gün 31) kapı sınavı var; bugün "nerede takılıyorum?" listesini çıkarıyoruz.

**Süre:** ~4 saat
Isınma 15 dk · Proje 3–3.5 sa · Kapanış 15 dk · (algoritma yok)

**Kurallar:**
- **Notsuz:** eski DERS.md'lere ve `expense-tracker` koduna **bakmak yok**. Takılırsan bana sor (bir soru = bir adım ipucu).
- Sınıf, metot, değişken ve mesajlar **İngilizce**. Metot adı/imzası ve exception mesajları **birebir**.
- **Değişiklik → derle → çalıştır → çıktının her satırını oku.**
- `main`'e test yazmak yok. Her sınıf bitince "bitti" de, testleri ben yazıp çalıştırırım.

---

## 0. Isınma (15 dk) — cevapları chat'e yaz

Dünün kaçanları: bitiş adımı olmayan stream (2 gün üst üste), `Optional.map` sonucu, olmayan dosyanın exception'ı.

**S1.** Çıktı ne? (Kaç satır olduğunu da düşün.)
```java
List<Integer> n = new ArrayList<>(List.of(3, 1, 2));
n.stream().sorted().peek(x -> System.out.println("p " + x));
List<Integer> s = n.stream().sorted().toList();
System.out.println(n + " " + s);
```

**S2.** Çıktı ne? (3 satır)
```java
Optional<String> o = Optional.of("java");
System.out.println(o.map(String::toUpperCase));
System.out.println(o.filter(x -> x.length() > 5));
System.out.println(o.map(String::length).orElse(0) + 1);
```

**S3.** Her satır **hangi exception'ı** fırlatır? (`enum Level { LOW, HIGH }`)
```java
Files.readAllLines(Path.of("yok.txt"));   // a
String x = "a;b".split(";")[2];           // b
Level.valueOf("low");                     // c
LocalDate.parse("2026-11-31");            // d
Integer.parseInt(" 5");                   // e
```

---

## 1. Proje Kurulumu (20 dk)

**Klasör:** `projects/inventory-cli/` (IntelliJ → New Project → **Maven**, JDK 25).

| Alan                  | Değer                                 |
| --------------------- | ------------------------------------- |
| groupId               | `com.eberkayk`                        |
| artifactId            | `inventory-cli`                       |
| version               | `1.0.0`                               |
| package               | `com.eberkayk.inventory`              |
| main class (manifest) | `com.eberkayk.inventory.InventoryCli` |

- `pom.xml`: Java 25 + UTF-8 + `java -jar` ile çalışan jar (manifest'te main class). Ezbere bilmen beklenmiyor; **önce kendin dene**, takılırsan sor.
- Maven wrapper (`mvnw`, `mvnw.cmd`, `.mvn/`) için `expense-tracker`'dan bu 3 şeyi **kopyalamak serbest** (kod değil, araç).
- `inventory.csv`'yi `.gitignore`'a ekle (veri dosyası repoya girmez).

---

## 2. Görev — Stok Takip

Bir depoda ürün girişleri (`IN`) ve çıkışları (`OUT`) kaydediliyor. Stok, hareketlerden **hesaplanıyor** (ayrı bir stok sayacı tutmak yok).

**Önerilen sıra:** 2.1 → 2.2 → 2.3 → 2.4 → 2.5. Her biri bitince "bitti" yaz.

### 2.1 `enum MovementType` (`MovementType.java`)

| Öğe         | Tanım                                                       |
| ----------- | ----------------------------------------------------------- |
| Sabitler    | `IN(1)`, `OUT(-1)`                                          |
| Field       | `private final int sign`                                    |
| Constructor | `MovementType(int sign)`                                    |
| Metot       | `int signed(int qty)` → `sign * qty` (IN 5 → 5, OUT 5 → -5) |

### 2.2 `record Movement(String sku, MovementType type, int qty, LocalDate date)` (`Movement.java`)

**Compact constructor** — bu sırayla kontrol et, hepsi `IllegalArgumentException`:

| Durum                     | Mesaj                               |
| ------------------------- | ----------------------------------- |
| `sku` null veya boş/blank | `SKU is blank`                      |
| `type` null               | `Type is null`                      |
| `qty <= 0`                | `Quantity must be positive: ` + qty |
| `date` null               | `Date is null`                      |

**Metotlar:**

| İmza                                   | Ne yapar                                                                                                                                                    |
| -------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `int signedQty()`                      | `type`'ın `signed` metodunu kullanır (OUT 3 → -3)                                                                                                           |
| `String toCsv()`                       | `A1;IN;5;2026-11-14` (sırası: sku;type;qty;date)                                                                                                            |
| `static Movement fromCsv(String line)` | `;` ile böl; parça sayısı 4 değilse IAE `Bad line: ` + line; sonra çevir (parseInt / valueOf / LocalDate.parse — onların exception'ları olduğu gibi çıksın) |

### 2.3 `class InsufficientStockException` (`InsufficientStockException.java`)

| Öğe         | Tanım                                                        |
| ----------- | ------------------------------------------------------------ |
| Tür         | **checked** (`extends Exception`)                            |
| Constructor | `InsufficientStockException(String sku, int have, int need)` |
| Mesaj       | `Not enough stock for A1: have 3, need 5`                    |

### 2.4 `class Inventory` (`Inventory.java`)

| Öğe   | Tanım                                                         |
| ----- | ------------------------------------------------------------- |
| Field | `private final List<Movement> movements = new ArrayList<>();` |
| Ctor  | yazmana gerek yok (varsayılan yeterli)                        |

**Metotlar:**

| İmza                                                      | Ne yapar                                                                                                                                                                                  |
| --------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `void add(Movement m) throws InsufficientStockException`  | `m` null → IAE `Movement is null`. `OUT` ve mevcut stok < `qty` → `InsufficientStockException(sku, stok, qty)`. Yoksa listeye ekle.                                                       |
| `int stockOf(String sku)`                                 | O sku'nun `signedQty` toplamı. Hiç hareketi yoksa `0`.                                                                                                                                    |
| `Map<String, Integer> stockAll()`                         | `TreeMap`: sku → stok. Stoğu 0 olan sku da görünür.                                                                                                                                       |
| `List<Movement> history(String sku)`                      | O sku'nun hareketleri, **tarihe göre artan**. Aynı tarihliler eklenme sırasında kalır. Yoksa boş liste.                                                                                   |
| `List<String> lowStock(int threshold)`                    | Stoğu `threshold`'dan **küçük** olan sku'lar, alfabetik.                                                                                                                                  |
| `Optional<String> topSeller()`                            | Toplam `OUT` miktarı en büyük sku. Eşitlikte alfabetik ilk. Hiç `OUT` yoksa `Optional.empty()`.                                                                                           |
| `int totalOut(LocalDate from, LocalDate to)`              | `from`–`to` arasındaki (**iki uç dahil**) `OUT` miktarlarının toplamı. `from` `to`'dan sonraysa IAE `Bad range: 2026-11-20 > 2026-11-10`.                                                 |
| `List<Movement> all()`                                    | Tüm hareketler, eklenme sırasında, **değiştirilemez kopya**.                                                                                                                              |
| `void saveTo(Path file) throws IOException`               | Her hareket bir satır (`toCsv`), eklenme sırasında.                                                                                                                                       |
| `static Inventory loadFrom(Path file) throws IOException` | Dosya **yoksa** boş `Inventory` döner. Boş/blank satırları atla. Satırları `fromCsv` ile okuyup **doğrudan listeye** ekle (stok kontrolü yok — dosya zaten geçerli hareketlerden oluştu). |

`Inventory` içinde `System.out` yok.

### 2.5 `class InventoryCli` (`InventoryCli.java`) — `main`

Veri dosyası: çalışılan klasörde `inventory.csv`. Program her çalışmada dosyayı yükler; `in`/`out` başarılıysa dosyaya kaydeder.

| Komut                                  | Çıktı                                                                                       |
| -------------------------------------- | ------------------------------------------------------------------------------------------- |
| `in A1 10 2026-11-14`                  | `OK: A1 stock = 10`                                                                         |
| `out A1 3 2026-11-15`                  | `OK: A1 stock = 7`                                                                          |
| `stock`                                | her sku bir satır: `A1: 7` (alfabetik). Hiç yoksa `No stock`                                |
| `stock A1`                             | `A1: 7` (bilinmeyen sku → `A1: 0`)                                                          |
| `history A1`                           | her hareket bir satır: `2026-11-14 IN 10`. Yoksa `No history for A1`                        |
| `low 5`                                | her sku bir satır. Yoksa `Nothing below 5`                                                  |
| `top`                                  | `Top seller: A1`. Hiç satış yoksa `No sales yet`                                            |
| argüman yok / eksik / bilinmeyen komut | `Usage: in\|out <sku> <qty> <yyyy-MM-dd> \| stock [sku] \| history <sku> \| low <n> \| top` |

**Hata çıktıları** (stack trace **asla** görünmez):
- `IOException` → `File error: ` + mesaj
- Diğer her şey (IAE, NFE, tarih hatası, `InsufficientStockException`) → `Error: ` + mesaj

**Örnek:** `out A1 50 2026-11-16` (stok 7) → `Error: Not enough stock for A1: have 7, need 50`

### 2.6 Jar + README (15 dk)

- `mvnw.cmd clean package` → `java -jar target\inventory-cli-1.0.0.jar stock` çalışmalı.
- `README.md`: ne yapar, komutlar, build, kullanım örneği (kısa).

---

### ✍️ Kağıda Yaz (proje bitince, 5 satır)
Bugün projede **takıldığın** 3 şey + her birinin doğru hali tek satır. Yarın sınavdan önce bunlara bakacaksın.

---

## 3. Kapanış (15 dk)

- `ILERLEME.md` → Gün 30 (Ne öğrendim / Ne takıldı satırlarını ben taslak veririm).
- Commit + push.
- Yarın: **Kapı sınavı** (Hafta 1–4, 100 puan, ≥ 80 → SQL).
