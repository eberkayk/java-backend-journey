# Hafta 6 · Gün 2 (Gün 33) — Aggregate, GROUP BY, HAVING, UPDATE/DELETE

**Hedef:** Satırları tek tek değil **gruplar halinde** özetlemek (kaç tane, toplam, ortalama),
grupları filtrelemek ve tablodaki veriyi değiştirmek/silmek.

**Süre:** ~3.5 saat
Isınma 15 dk · Ders 50 dk · Görev A 75 dk · Görev B 30 dk · Görev C 30 dk · Kapanış 10 dk

**Kurallar (dünküyle aynı):**
- SQL anahtar kelimeleri BÜYÜK, tablo/kolon adları küçük.
- Bitince "bitti" de; dosyaları **ben** veritabanında çalıştırıp kontrol ederim.

---

## 0. Isınma (15 dk) — cevapları chat'e yaz

**S1.** (Java) Derlenir mi? Derlenmiyorsa **neden**, ve **tek satır ekleyerek** nasıl düzelir?
```java
String prefix = "ID-";
if (args.length > 0) prefix = "X-";
List.of("a", "b").forEach(s -> System.out.println(prefix + s));
```

**S2.** (Java) `words = ["a", "b", "a"]` iken ilk turda ne olur? Satırı **tek bir metot çağrısıyla** düzelt.
```java
Map<String, Integer> counts = new HashMap<>();
for (String w : words) {
    counts.put(w, counts.get(w) + 1);
}
```

**S3.** (SQL) Bu tabloda sorgu **hangi isimleri** döndürür? Ali neden gelmez/gelir?
```
 name  | supplier
-------+----------
 Ali   | Acme
 Veli  | NULL
 Ayse  | Ikea
```
```sql
SELECT name FROM t WHERE supplier <> 'Acme';
```

**S4.** (SQL) Hata verir. Neden, ve WHERE satırını nasıl yazarsın?
```sql
SELECT name, price * stock AS stock_value FROM products WHERE stock_value > 5000;
```

---

## 1. Ders (50 dk)

### 1.1 Aggregate fonksiyonlar — çok satır gir, tek değer çık

| Fonksiyon               | Ne döndürür                            | NULL'a ne yapar                 |
| ----------------------- | -------------------------------------- | ------------------------------- |
| `COUNT(*)`              | satır sayısı                           | **sayar** (satır satırdır)      |
| `COUNT(kolon)`          | o kolonu NULL **olmayan** satır sayısı | **atlar**                       |
| `COUNT(DISTINCT kolon)` | o kolondaki farklı değer sayısı        | atlar                           |
| `SUM(kolon)`            | toplam                                 | atlar                           |
| `AVG(kolon)`            | ortalama                               | atlar (NULL'lar paydaya girmez) |
| `MIN(kolon)`            | en küçük                               | atlar                           |
| `MAX(kolon)`            | en büyük                               | atlar                           |

Java karşılığı: `list.size()`, `stream().mapToInt(...).sum()`, `.average()`, `.min()`, `.max()`.

Dünkü `employees` tablosuyla (5 kişi, 2'sinin `manager_id`'si NULL):
```sql
SELECT COUNT(*)          AS people,        -- 5
       COUNT(manager_id) AS with_manager,  -- 3   (NULL'lar sayılmadı)
       COUNT(DISTINCT dept) AS depts,      -- 3   (IT, HR, Sales)
       SUM(salary)       AS total,         -- 186000.00
       ROUND(AVG(salary), 2) AS avg_sal,   -- 37200.00
       MAX(salary)       AS top            -- 51000.00
FROM employees;
```
`ROUND(x, 2)` → virgülden sonra 2 hane. AVG genelde uzun ondalık verir, onu yuvarlamak için.

**AVG ve NULL tuzağı:** puanlar `5, 3, NULL` ise `AVG` = (5+3)/**2** = 4, (5+3+0)/3 değil.
NULL "puan verilmedi" demek, "0 puan" değil.

### 1.2 GROUP BY — her grup için ayrı özet

Aggregate tek başına **tüm tabloyu** tek grup sayar. `GROUP BY dept` → her departman ayrı grup,
her grup için **bir** satır çıkar.

```sql
SELECT dept, COUNT(*) AS people, MAX(salary) AS top
FROM employees
GROUP BY dept
ORDER BY dept;
```
```
 dept  | people |   top
-------+--------+----------
 HR    |      1 | 30000.00
 IT    |      3 | 51000.00
 Sales |      1 | 28000.00
```
Java karşılığı: `Collectors.groupingBy(Employee::dept, Collectors.counting())`
— ya da elle yazdığın `Map<String, Integer>` sayaç döngüsü (bugünkü S2).

**Altın kural:** GROUP BY varsa, SELECT'teki her kolon ya **GROUP BY'da yazılı** olmalı ya da
**aggregate'in içinde** olmalı.
```sql
SELECT dept, name, COUNT(*) FROM employees GROUP BY dept;
-- HATA: column "employees.name" must appear in the GROUP BY clause or be used in an aggregate function
```
Neden? IT grubunda 3 kişi var — tek satırlık özete **hangi** `name` yazılsın? Veritabanı tahmin etmez.

**Birden fazla kolonla gruplama:** `GROUP BY city, category` → her (şehir, kategori) **çifti** bir grup.

### 1.3 HAVING — grupları filtrele

```sql
SELECT dept, COUNT(*) AS people
FROM employees
GROUP BY dept
HAVING COUNT(*) >= 2;          -- sadece IT (3 kişi)
```
`WHERE` ile `HAVING` farkı:

|          | Ne zaman çalışır      | Neyi eler             | Aggregate kullanabilir mi?              |
| -------- | --------------------- | --------------------- | --------------------------------------- |
| `WHERE`  | gruplamadan **önce**  | tek tek **satırları** | **hayır** (`WHERE COUNT(*) > 2` → hata) |
| `HAVING` | gruplamadan **sonra** | bütün **grupları**    | evet                                    |

İkisi aynı sorguda olabilir:
```sql
SELECT dept, ROUND(AVG(salary), 2) AS avg_sal
FROM employees
WHERE active = true            -- 1) önce pasif çalışanları at
GROUP BY dept                  -- 2) kalanları departmana göre grupla
HAVING AVG(salary) > 30000     -- 3) ortalaması 30000'i geçmeyen grupları at
ORDER BY avg_sal DESC;         -- 4) sırala
```

**Güncel çalışma sırası** (dünkünün genişlemiş hali):
```
FROM → WHERE → GROUP BY → HAVING → SELECT → DISTINCT → ORDER BY → LIMIT
```
HAVING de SELECT'ten **önce** çalışır → SELECT'teki alias'ı HAVING'de kullanma, ifadeyi tekrar yaz
(`HAVING AVG(salary) > 30000`, `HAVING avg_sal > 30000` değil). ORDER BY'da alias serbest.

### 1.4 Tarihten parça almak

```sql
SELECT EXTRACT(MONTH FROM DATE '2026-11-17');   -- 11
SELECT EXTRACT(YEAR  FROM DATE '2026-11-17');   -- 2026
```
Ayına göre gruplamak: `GROUP BY EXTRACT(MONTH FROM sold_on)`.

Bir ayın satırlarını almak için en temiz WHERE:
`sold_on >= '2026-11-01' AND sold_on < '2026-12-01'` (ay sonunun 30 mu 31 mi olduğunu düşünmezsin).

### 1.5 UPDATE ve DELETE — veriyi değiştirmek

```sql
UPDATE employees
SET salary = salary * 1.10, active = true      -- birden fazla kolon: virgülle
WHERE dept = 'IT';                             -- psql cevabı: UPDATE 3  (3 satır değişti)

DELETE FROM employees
WHERE active = false;                          -- psql cevabı: DELETE 0
```
- `SET salary = salary * 1.10` → sağdaki `salary` **eski** değer. Java'daki `x = x * 1.10` gibi.
- **WHERE'siz UPDATE/DELETE bütün tabloyu etkiler.** `DELETE FROM employees;` → herkes silindi, geri alma yok
  (geri almayı Gün 35'te transaction ile göreceğiz).
- **Güvenli alışkanlık:** önce aynı WHERE ile `SELECT` yaz, doğru satırları getirdiğini gör,
  sonra `SELECT ...` kısmını `UPDATE ... SET ...` / `DELETE` ile değiştir.
- Komutlar **sırayla** çalışır: önceki UPDATE'in değiştirdiği değeri sonraki komut görür.

---

## 📝 Kağıda Yaz

```
Sıra: FROM → WHERE → GROUP BY → HAVING → SELECT → ORDER BY → LIMIT
COUNT(*) satır sayar · COUNT(kolon) NULL'ları atlar · AVG NULL'u paydaya katmaz
GROUP BY varsa SELECT'teki her kolon ya GROUP BY'da ya aggregate içinde
WHERE = satır eler (aggregate YOK) · HAVING = grup eler (aggregate VAR)
Alias: ORDER BY'da var, WHERE ve HAVING'de yok
UPDATE t SET a = ..., b = ... WHERE ... · WHERE'siz = bütün tablo
Önce SELECT ile aynı WHERE'i dene, sonra UPDATE/DELETE
```

---

## 2. Görev A — Satış Raporları (75 dk)

Bugün tablo hazır: `sales.sql` (20 satış, **değiştirme**). Önce yükle:
```powershell
psql -U postgres -h localhost -d shop -f sales.sql
```
Sorgular `queries.sql`'e, her birinin üstünde `-- Q1` yorumu.

**Tablo:** `sales`

| Kolon        | Tip              | Not                                    |
| ------------ | ---------------- | -------------------------------------- |
| `id`         | `INTEGER`        | otomatik                               |
| `sold_on`    | `DATE`           | satış tarihi (Eylül–Kasım 2026)        |
| `customer`   | `TEXT`           | müşteri adı                            |
| `city`       | `TEXT`           | `Istanbul`, `Ankara`, `Izmir`          |
| `product`    | `TEXT`           | ürün adı                               |
| `category`   | `TEXT`           | `electronics`, `books`, `toys`, `home` |
| `quantity`   | `INTEGER`        | kaç adet alındı                        |
| `unit_price` | `NUMERIC(10, 2)` | bir adedin fiyatı                      |
| `rating`     | `INTEGER`        | 1–5, puan verilmediyse **NULL**        |

Bir satışın tutarı = `quantity * unit_price`. **Ciro** = tutarların toplamı.

"Kontrol" sütunu: sonucun içinde görmen gereken bir değer (kendini kontrol etmen için).

| #   | İstenen                                                                                    | Kolonlar (alias)                      | Sıralama                            | Satır | Kontrol             |
| --- | ------------------------------------------------------------------------------------------ | ------------------------------------- | ----------------------------------- | ----- | ------------------- |
| Q1  | Toplam kaç satış var                                                                       | `sale_count`                          | —                                   | 1     | 20                  |
| Q2  | Kaç satışa puan verilmiş, kaçına verilmemiş                                                | `rated`, `not_rated`                  | —                                   | 1     | `not_rated` = 6     |
| Q3  | Ortalama puan, 2 haneye yuvarlanmış                                                        | `avg_rating`                          | —                                   | 1     | 4.00                |
| Q4  | Toplam ciro                                                                                | `revenue`                             | —                                   | 1     | 13405.70            |
| Q5  | En düşük ve en yüksek birim fiyat                                                          | `cheapest`, `most_expensive`          | —                                   | 1     | 45.00               |
| Q6  | Kaç **farklı** müşteri alışveriş yapmış                                                    | `customer_count`                      | —                                   | 1     | 6                   |
| Q7  | Her şehirdeki satış sayısı                                                                 | `city`, `sale_count`                  | `sale_count` azalan, sonra city     | 3     | Istanbul 8          |
| Q8  | Her kategorinin cirosu                                                                     | `category`, `revenue`                 | `revenue` azalan                    | 4     | electronics 6262.70 |
| Q9  | Her müşterinin toplam harcaması                                                            | `customer`, `total_spent`             | `total_spent` azalan                | 6     | Mehmet en üstte     |
| Q10 | Toplam harcaması 2000'den fazla olan müşteriler                                            | `customer`, `total_spent`             | `total_spent` azalan                | 3     | Ali 2702.30         |
| Q11 | **Kasım 2026** satışlarında kategori başına satılan toplam adet; sadece toplamı 3+ olanlar | `category`, `units`                   | `units` azalan                      | 2     | home 19             |
| Q12 | Her (şehir, kategori) çifti için satış sayısı                                              | `city`, `category`, `sale_count`      | city A→Z, sonra category A→Z        | 11    | Izmir · toys 2      |
| Q13 | Her ayın cirosu (ay numarasıyla)                                                           | `month`, `revenue`                    | `month` artan                       | 3     | 11 → 6652.00        |
| Q14 | En az 2 kez satılmış ürünler: kaç kez satıldığı ve ortalama puanı (1 haneye yuvarlanmış)   | `product`, `times_sold`, `avg_rating` | `product` A→Z                       | 8     | Notebook 5.0        |
| Q15 | Her müşterinin **puan vermediği** satış sayısı (hiç puansız satışı olmayan müşteri çıkmaz) | `customer`, `sale_count`              | `sale_count` azalan, sonra customer | 5     | Ali 2               |

İpuçları (takılırsan bak):
- Q2: `not_rated` için yeni bir fonksiyon gerekmez — iki COUNT'u birbirinden çıkar.
- Q14: Notebook'un puanları `5, NULL` → ortalama neden 5.0? (1.1'deki AVG tuzağı)
- Q15: WHERE mi HAVING mi? "puansız satış" bir **satırın** özelliği mi, bir **grubun** mu?

---

## 3. Görev B — UPDATE / DELETE (30 dk)

Dünkü `products` tablosu üzerinde. Önce sıfırla (dünkü klasörden):
```powershell
psql -U postgres -h localhost -d shop -f ..\gun-01\schema.sql -f ..\gun-01\seed.sql
```
Komutları `dml.sql`'e **bu sırayla** yaz (üstlerine `-- D1` yorumu). Sağdaki sayı, psql'in
yazması gereken cevap.

| #   | İstenen                                                                                                                     | psql cevabı |
| --- | --------------------------------------------------------------------------------------------------------------------------- | ----------- |
| D1  | `toys` kategorisindeki tüm ürünlerin fiyatını %10 artır                                                                     | `UPDATE 3`  |
| D2  | `SQL Basics`'in stoğunu 30 yap                                                                                              | `UPDATE 1`  |
| D3  | Tedarikçisi bilinmeyen ürünlerin tedarikçisini `Generic` yap                                                                | `UPDATE 4`  |
| D4  | Satıştan kalkmış (`discontinued`) ürünleri sil                                                                              | `DELETE 2`  |
| D5  | `home` kategorisinde fiyatı 100'den az olan ürünlerin stoğunu 50 artır **ve** tedarikçisini `Ikea` yap — **tek** UPDATE ile | `UPDATE 2`  |
| D6  | 1 Ekim 2025'ten **önce** eklenmiş **ve** stoğu 20'den az olan ürünleri sil                                                  | `DELETE 1`  |

Hepsini sıfırlamadan sonra tek seferde çalıştır:
```powershell
psql -U postgres -h localhost -d shop -f ..\gun-01\schema.sql -f ..\gun-01\seed.sql -f dml.sql
```
Son durumda `SELECT COUNT(*) FROM products;` → `11`.

D6'yı yazınca şunu düşün (chat'e yaz): **Coffee Mug** 2025'in başında eklendi ve tabloyu sıfırladığında
stoğu 80. Neden D6'da silinmiyor?

---

## 4. Görev C — LeetCode SQL (30 dk)

Dil: **PostgreSQL**. Submit = testin.

| #   | Problem                                                                                                                                   | Konu                       |
| --- | ----------------------------------------------------------------------------------------------------------------------------------------- | -------------------------- |
| C1  | [2356. Number of Unique Subjects Taught by Each Teacher](https://leetcode.com/problems/number-of-unique-subjects-taught-by-each-teacher/) | GROUP BY + COUNT(DISTINCT) |
| C2  | [1741. Find Total Time Spent by Each Employee](https://leetcode.com/problems/find-total-time-spent-by-each-employee/)                     | iki kolonla GROUP BY + SUM |
| C3  | [596. Classes More Than 5 Students](https://leetcode.com/problems/classes-more-than-5-students/)                                          | HAVING                     |
| C4  | [1890. The Latest Login in 2020](https://leetcode.com/problems/the-latest-login-in-2020/)                                                 | WHERE + GROUP BY + MAX     |

---

## 5. Kapanış (10 dk)

- A ve B bitince "bitti" yaz → `queries.sql` ve `dml.sql`'i ben çalıştırıp kontrol ederim.
- C1–C4 için "accepted" yazman yeter.
- ILERLEME satırlarını ben hazırlarım.

Sayaç: dün 17 + bugün 15 + 6 + 4 = **42** SQL sorusu (hafta hedefi 60+).
