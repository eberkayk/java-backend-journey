# Hafta 6 · Gün 4 (Gün 35) — Subquery, CTE, Normalizasyon, Index, Transaction

**Hedef:** Sorgu içinde sorgu yazmak, uzun sorguları `WITH` ile adım adım kurmak, tabloyu doğru bölmek,
yavaş sorguyu index ile hızlandırmak ve "ya hep ya hiç" çalışan işlemler (transaction) yazmak.

**Süre:** ~4.5 saat
Isınma 15 dk · Ders 70 dk · Görev A 70 dk · Görev B 25 dk · Görev C 15 dk · Görev D 15 dk · Görev E 40 dk

**Kurallar (aynı):**
- SQL anahtar kelimeleri BÜYÜK, tablo/kolon adları küçük.
- Bitince "bitti" de; dosyaları **ben** veritabanında çalıştırıp kontrol ederim.
- Bugün dünkü kütüphane verisini kullanıyoruz (senin `../gun-03/schema.sql` + `../gun-03/seed.sql`).

---

## 0. Isınma (15 dk) — cevapları chat'e yaz

**S1.** (SQL) Dünkü ders verisi: Legal departmanında **kimse yok**, IT'de Ali ve Ayse var.
`employees.dept_id REFERENCES departments(id)` (ON DELETE yazılmamış). İki komut ayrı ayrı çalışıyor:
```sql
DELETE FROM departments WHERE name = 'Legal';   -- (a)
DELETE FROM departments WHERE name = 'IT';      -- (b)
```
Hangisi geçer, hangisi reddedilir? Neden?

**S2.** (SQL) İki sorgu, tek fark `JOIN` / `LEFT JOIN`:
```sql
-- (a)
SELECT e.name, d.name AS dept
FROM employees e
LEFT JOIN departments d ON e.dept_id = d.id
WHERE d.name IS NOT NULL;

-- (b)
SELECT e.name, d.name AS dept
FROM employees e
JOIN departments d ON e.dept_id = d.id;
```
Sonuçları farklı mı aynı mı? (Zeynep'in departmanı NULL.) Aynıysa hangisini yazmak daha doğru?

**S3.** (SQL) "Her ödünç için kaç gün tutulduğu" isteniyor. Bu sorgu **hata** veriyor:
```sql
SELECT l.id, SUM(l.returned_on - l.loaned_on) AS days_kept
FROM loans l;
```
Neden hata verir? Düzeltilmiş halini yaz.

**S4.** (Java) Ne olur?
```java
Map<String, Integer> stock = new HashMap<>();
stock.put("pen", 5);
int n = stock.get("mug");
```

---

## 1. Ders (70 dk)

### 1.1 Subquery — sorgunun içinde sorgu

Parantez içine yazılan `SELECT`'tir. Önce o çalışır, sonucu dıştaki sorguda **değer gibi** kullanılır.

**Tek değer döndüren (scalar) subquery** — `=`, `>`, `<` ile:
```sql
-- Ortalama maaştan fazla kazananlar
SELECT name, salary
FROM employees
WHERE salary > (SELECT AVG(salary) FROM employees);
```
Neden `WHERE salary > AVG(salary)` yazamıyorsun? Dünkü kural: aggregate WHERE'de kullanılamaz
(WHERE satır satır çalışır, ortalama henüz yok). Subquery ortalamayı **önce** hesaplar → `WHERE salary > 41250`.

Scalar subquery **tek satır, tek kolon** döndürmeli. İki satır dönerse:
`ERROR: more than one row returned by a subquery used as an expression`.

**Liste döndüren subquery** — `IN` ile:
```sql
-- Çalışanı olan departmanlar (JOIN'siz)
SELECT name
FROM departments
WHERE id IN (SELECT dept_id FROM employees);
```

**`NOT IN` + NULL tuzağı:**
```sql
-- Çalışanı OLMAYAN departmanlar?
SELECT name FROM departments
WHERE id NOT IN (SELECT dept_id FROM employees);     -- 0 satır!  (Legal bekliyordun)
```
Zeynep'in `dept_id`'si NULL → liste `(1, 1, 2, 3, NULL)`. `4 NOT IN (..., NULL)` demek
`4 <> 1 AND ... AND 4 <> NULL` → son parça UNKNOWN → bütün ifade UNKNOWN → satır atılır.
Listede **tek bir NULL** varsa `NOT IN` hiçbir şey döndürmez. Çözüm → `NOT EXISTS`.

### 1.2 EXISTS / NOT EXISTS ve correlated subquery

```sql
-- Çalışanı OLMAYAN departmanlar — doğru yol
SELECT d.name
FROM departments d
WHERE NOT EXISTS (
    SELECT 1 FROM employees e WHERE e.dept_id = d.id
);
```
- `EXISTS (...)` → içteki sorgu **en az 1 satır** döndürüyorsa true. Ne seçtiğin önemsiz, `SELECT 1` gelenek.
- İçteki sorgu dıştaki `d`'yi kullanıyor (`e.dept_id = d.id`) → buna **correlated subquery** denir:
  dıştaki **her satır için** içteki sorgu o satırın değeriyle yeniden çalışır.
  Java ile düşün: `for (d : departments) { if (!employees.stream().anyMatch(e -> e.deptId == d.id)) ... }`
- NULL tuzağı yok: Zeynep'in satırı `e.dept_id = 4` koşulunu sağlamaz, o kadar.

**Correlated subquery SELECT'te** — her satır için bir değer hesaplar:
```sql
SELECT d.name,
       (SELECT COUNT(*) FROM employees e WHERE e.dept_id = d.id) AS people
FROM departments d;                    -- Legal 0 ile gelir (dünkü LEFT JOIN + COUNT ile aynı sonuç)
```

**Correlated subquery WHERE'de** — "kendi grubuna göre" karşılaştırma:
```sql
-- Kendi departmanının ortalamasından fazla kazananlar
SELECT e.name, e.salary
FROM employees e
WHERE e.salary > (SELECT AVG(e2.salary)
                  FROM employees e2
                  WHERE e2.dept_id = e.dept_id);
```
Aynı tablo iki kez → iki alias (`e` dıştaki satır, `e2` içteki arama). Dünkü self join ile aynı fikir.

### 1.3 CTE — `WITH` ile adım adım

Uzun sorguyu **isimli ara sonuçlara** bölersin. Java'da ara sonucu bir değişkene atamak gibi:
```sql
WITH dept_counts AS (
    SELECT dept_id, COUNT(*) AS people
    FROM employees
    WHERE dept_id IS NOT NULL
    GROUP BY dept_id
)
SELECT d.name, dc.people
FROM dept_counts dc
JOIN departments d ON d.id = dc.dept_id
WHERE dc.people = (SELECT MAX(people) FROM dept_counts);    -- en kalabalık departman(lar): IT 2
```
- `dept_counts` sadece bu sorgu boyunca var olan geçici bir tablo gibi; sonra kaybolur.
- Birden fazla adım: `WITH a AS (...), b AS (... FROM a ...) SELECT ... FROM b;` (aralarda virgül, `WITH` bir kez).
- `FROM (SELECT ...) AS x` (derived table) aynı işi yapar, ama okunması zor. Bugün **WITH** yaz.

**Ne zaman hangisi?**

| İhtiyaç                                 | Araç                                    |
| --------------------------------------- | --------------------------------------- |
| İki tablonun kolonları yan yana         | `JOIN`                                  |
| "Ortalamadan / en büyükten fazla"       | scalar subquery                         |
| "Hiç ... olmayan"                       | `NOT EXISTS` (veya LEFT JOIN + IS NULL) |
| "Kendi grubuna göre" karşılaştırma      | correlated subquery                     |
| Bir sonucu birden fazla yerde kullanmak | `WITH` (CTE)                            |

### 1.4 Normalizasyon — veriyi neden böldük?

Kötü tasarım: her şey tek tabloda.
```
 order_id | customer | customer_email | product | price | qty
----------+----------+----------------+---------+-------+-----
        1 | Ali      | ali@mail.com   | Pen     |    10 |   2
        1 | Ali      | ali@mail.com   | Mug     |    60 |   1
        2 | Ali      | ali@mail.com   | Pen     |    10 |   5
        3 | Ayse     | ayse@mail.com  | Mug     |    60 |   1
```
Sorunlar (**anomali**):
- **Güncelleme:** Ali e-postasını değiştirirse 3 satırı birden güncellemelisin; birini unutursan
  Ali'nin iki farklı e-postası olur.
- **Ekleme:** Hiç sipariş verilmemiş yeni bir ürünü ekleyemezsin (order_id ne olacak?).
- **Silme:** 3 numaralı siparişi silersen Ayse'nin e-postası da sonsuza kadar gider.

Kök neden: **aynı bilgi birden fazla satırda tekrar ediyor.** Normalizasyon = her bilgiyi **tek yerde** tutacak
şekilde tabloları bölmek.

| Kural   | Kısaca                                                           | Bozan örnek                                 |
| ------- | ---------------------------------------------------------------- | ------------------------------------------- |
| **1NF** | Her hücrede **tek** değer; liste yok                             | `phones = '0555..., 0532...'`               |
| **2NF** | Kolon, PK'nın **tamamına** bağlı (PK iki kolonsa yarısına değil) | `(order_id, product)` PK'lı tabloda `price` |
| **3NF** | PK olmayan kolon, başka bir PK-olmayan kolona bağlı değil        | `customer` → `customer_email`               |

Pratik özet (ezberlemen gereken tek cümle): **"Her tablo tek bir şeyi anlatır; bir bilgi tek yerde yazılır;
başka bir şeye ait bilgi oraya id ile bağlanır."** Yukarıdaki tablonun doğrusu dünkü D sorularındaki tasarım:
`customers`, `products`, `orders(customer_id)`, `order_items(order_id, product_id, qty)`.

(Gerçek hayatta bazen bilerek tekrar edilir — örn. sipariş anındaki fiyat `order_items.unit_price`'a kopyalanır,
çünkü ürünün fiyatı sonra değişse de eski siparişin fiyatı değişmemeli. Bu tasarım kararıdır, hata değil.)

### 1.5 Index — kitabın arkasındaki dizin

Index'siz `WHERE user_id = 777` → PostgreSQL **bütün satırları** tek tek okur (**Seq Scan**).
Index = kolonun **sıralı kopyası** + her değerin satırının adresi (kitabın sonundaki "kavram → sayfa no" dizini).
Sıralı olduğu için binary search gibi hızlı bulunur (**Index Scan**).

```sql
CREATE INDEX idx_page_views_user_id ON page_views (user_id);
DROP INDEX idx_page_views_user_id;
```
Planı görmek için sorgunun başına:
```sql
EXPLAIN ANALYZE SELECT * FROM page_views WHERE user_id = 777;
-- Parallel Seq Scan on page_views ...  Execution Time: ~120 ms  (index yokken)
-- Bitmap Index Scan on idx_...        Execution Time: ~0.3 ms  (index varken)
```
- `PRIMARY KEY` ve `UNIQUE` **otomatik** index açar. **FK açmaz** — `loans.member_id` gibi kolonlara
  JOIN'de çok kullanılıyorsa index'i sen eklersin.
- Bedava değil: her INSERT/UPDATE/DELETE index'i de günceller, diskte yer kaplar. Her kolona açılmaz;
  **sık aranan / JOIN'lenen / sıralanan** kolonlara açılır.
- Az satırlı tabloda (bizim 13 ödünç) PostgreSQL index'i kullanmayabilir bile — hepsini okumak zaten hızlı.

### 1.6 Transaction — ya hep ya hiç

Kütüphanede ödünç vermek **iki** komut: kopya sayısını 1 azalt + `loans`'a satır ekle.
Biri çalışıp diğeri hata verirse veri tutarsız kalır (kitap verildi ama stok düşmedi, ya da tersi).

```sql
BEGIN;                                          -- işlem başlar
UPDATE accounts SET balance = balance - 100 WHERE id = 1;
UPDATE accounts SET balance = balance + 100 WHERE id = 2;
COMMIT;                                         -- ikisi birden kalıcı olur
```
- `COMMIT` → değişiklikler kalıcı. `ROLLBACK` → `BEGIN`'den beri yapılan **her şey** geri alınır.
- `BEGIN` içinde bir komut **hata** verirse transaction "bozulur": sonraki komutlar
  `current transaction is aborted, commands ignored until end of transaction block` hatasıyla reddedilir,
  sondaki `COMMIT` da aslında **ROLLBACK** yapar. Yani ya hepsi ya hiçbiri.
- `BEGIN` yazmazsan her komut **kendi başına** bir transaction'dır (autocommit): biri hata verse de
  diğerleri kalıcı olur.

**ACID** (mülakat sorusu, kelimeleri bil):

| Harf | Anlamı      | Bir cümle                                                       |
| ---- | ----------- | --------------------------------------------------------------- |
| A    | Atomicity   | Ya hepsi ya hiçbiri                                             |
| C    | Consistency | Transaction sonunda bütün kısıtlar (FK, CHECK...) hâlâ sağlanır |
| I    | Isolation   | Aynı anda çalışan transaction'lar birbirinin yarım işini görmez |
| D    | Durability  | COMMIT edilen veri elektrik gitse de kaybolmaz                  |

Hafta 9'da Spring'deki `@Transactional` tam olarak bunu yapacak: metodun başında `BEGIN`,
sonunda `COMMIT`, exception fırlarsa `ROLLBACK`.

---

## 📝 Kağıda Yaz

```
Subquery: (SELECT ...) önce çalışır · scalar = tek değer (>, =) · liste = IN
NOT IN + listede NULL → 0 satır!  "Hiç olmayan" → NOT EXISTS
Correlated: içteki sorgu dıştaki satırı kullanır, her satır için yeniden çalışır
WITH ad AS (...) SELECT ... FROM ad  → ara sonuca isim ver
Normalizasyon: bir bilgi tek yerde; tekrar = güncelleme/ekleme/silme anomalisi
Index: Seq Scan → Index Scan · PK/UNIQUE otomatik, FK değil · yazmayı yavaşlatır
BEGIN ... COMMIT / ROLLBACK · içeride hata → hepsi geri alınır
ACID: Atomicity, Consistency, Isolation, Durability
```

---

## 2. Görev A — Subquery & CTE (70 dk)

Önce temiz veri (bu klasördeyken):
```powershell
psql -U postgres -h localhost -d shop -f ../gun-03/schema.sql -f ../gun-03/seed.sql
```
Sorgular `subqueries.sql`'e (bu klasör, sen oluşturuyorsun), üstlerine `-- Q1` yorumu.
**"Araç" kolonundaki yöntemi kullan** — JOIN ile de çözülebilecek olanlar var ama bugün amaç subquery/CTE pratiği.

| #   | İstenen                                                                                      | Araç                            | Kolonlar                  | Sıralama                           | Satır | Kontrol                  |
| --- | -------------------------------------------------------------------------------------------- | ------------------------------- | ------------------------- | ---------------------------------- | ----- | ------------------------ |
| Q1  | Yayın yılı, bütün kitapların ortalama yayın yılından büyük olan kitaplar                     | scalar subquery                 | `title`, `published_year` | `published_year` artan             | 4     | ilk: Benim Adim Kirmizi  |
| Q2  | En çok kopyası olan kitap(lar)                                                               | scalar subquery (`MAX`)         | `title`, `copies`         | —                                  | 1     | 1984 · 3                 |
| Q3  | En az bir kez ödünç verilmiş kitaplar — **JOIN yok**                                         | `IN`                            | `title`                   | `title` A→Z                        | 7     | ilk: 1984                |
| Q4  | Hiç ödünç almamış üyeler                                                                     | `NOT EXISTS`                    | `full_name`               | —                                  | 1     | Elif Arslan              |
| Q5  | Yazarı `Turkey`'den olan **ve** şu an iade edilmemiş bir ödüncü bulunan kitaplar — JOIN yok  | iki `IN`                        | `title`                   | `title` A→Z                        | 3     | ilk: Ask                 |
| Q6  | Her kitap ve kaç kez ödünç verildiği (0'lar dahil) — JOIN/GROUP BY yok                       | correlated subquery `SELECT`'te | `title`, `loan_count`     | `loan_count` azalan, sonra `title` | 10    | son: The Idiot 0         |
| Q7  | En çok ödünç alan üye(ler) ve sayısı                                                         | `WITH` + `MAX`                  | `full_name`, `loan_count` | —                                  | 1     | Ali Yilmaz 4             |
| Q8  | Her yazarın **en yeni** kitabı                                                               | correlated subquery `WHERE`'de  | `title`, `published_year` | `title` A→Z                        | 5     | ilk: 1984 · 1949         |
| Q9  | Kopya sayısı, **kendi yazarının** kitaplarının ortalama kopya sayısından fazla olan kitaplar | correlated subquery `WHERE`'de  | `title`, `copies`         | `title` A→Z                        | 3     | son: Kuyucakli Yusuf · 2 |
| Q10 | Şu an iade edilmemiş kitabı olan yazarlar (her yazar bir kez)                                | `WITH` + JOIN                   | `author`                  | `author` A→Z                       | 3     | ilk: Elif Shafak         |

İpuçları (takılırsan bak):
- Q4: 1.2'deki örnek, tablolar farklı.
- Q7: 1.3'teki örnek. Önce CTE'de `member_id` başına sayı; sonra isim için `members` ile JOIN.
- Q8 / Q9: 1.2'deki "kendi departmanının ortalaması" örneği; `books` iki kez, iki alias.
- Q10: CTE'de iade edilmemiş ödünçlerin `book_id`'leri. Aynı yazar iki kez çıkıyorsa → dünkü `DISTINCT`.

---

## 3. Görev B — Transaction tahmini (25 dk)

`tx.sql` hazır (**değiştirme**). Dört blok var: T1, T2, T3, T4. Başlangıç: **The Idiot** (`id 9`) `copies = 1`,
`loans`'ta **13** satır. Senin şemandaki `copies >= 0` kuralı burada önemli.

**Çalıştırmadan önce** chat'e yaz:
- **B1.** T1 sonunda The Idiot'un `copies` değeri ve `loans` satır sayısı?
- **B2.** T2'de hangi komut hata verir, neden? `INSERT` ne olur? T2 sonunda `copies` ve satır sayısı?
- **B3.** T3 sonunda Ali'nin ödünç kayıtları duruyor mu?
- **B4.** T4 (aynı iki komut, `BEGIN`/`COMMIT` yok) sonunda `copies` ve satır sayısı? Veri **tutarlı** mı —
  yani `copies` ile gerçekte kaç kişide olduğu uyuyor mu?

Sonra çalıştır, script sondaki 3 `SELECT` ile durumu yazdırıyor:
```powershell
psql -U postgres -h localhost -d shop -f ../gun-03/schema.sql -f ../gun-03/seed.sql -f tx.sql
```

**B5 (yaz).** `return.sql` dosyası (bu klasör, sen yazıyorsun): **Zeynep Celik'in Kar'ı iade etmesi** — tek transaction:
- `loans`'taki ilgili kaydın (`book_id = 2`, `member_id = 4`, `returned_on IS NULL`) `returned_on`'u `'2026-11-19'` olsun,
- Kar'ın (`id = 2`) `copies`'i 1 artsın.

Temiz veri + `return.sql` çalışınca Kar `copies = 2`, açık ödünç sayısı 4 → 3 olmalı.

---

## 4. Görev C — Normalizasyon (15 dk) — chat'e yaz

Bir okul tek tablo tutuyor:
```
 student_no | student_name | course_code | course_name | teacher | teacher_room | grade
------------+--------------+-------------+-------------+---------+--------------+-------
        101 | Ali          | CS101       | Java        | Kaya    | B-204        |    85
        101 | Ali          | CS102       | SQL         | Demir   | B-110        |    90
        102 | Ayse         | CS101       | Java        | Kaya    | B-204        |    70
        103 | Can          | CS101       | Java        | Kaya    | B-204        |    95
```
(Her dersin tek öğretmeni var; öğretmenin tek odası var.)

**C1.** Öğretmen Kaya B-301'e taşındı. Bu tabloda kaç satırı güncellemen gerekir? Birini unutursan ne olur?
**C2.** Yeni bir ders `CS103 · Spring · Kaya` açıldı ama henüz öğrencisi yok. Bu tabloya ekleyebilir misin? Neden?
**C3.** Bu tabloyu hangi tablolara bölersin? Her tablo için kolonları yaz, **PK**'yı ve **FK**'ları belirt.
(SQL yazmana gerek yok: `tablo(kolon PK, kolon, kolon FK → tablo)` biçimi yeter.)

---

## 5. Görev D — Index (15 dk)

`big.sql` hazır (**değiştirme**): 500 bin satırlık `page_views` tablosu kurar (~1 dakika sürer, bekle).
```powershell
psql -U postgres -h localhost -d shop -f big.sql
psql -U postgres -h localhost -d shop
```
psql içinde sırayla:
1. `EXPLAIN ANALYZE SELECT * FROM page_views WHERE user_id = 777;` → planda geçen **scan tipi** (`... Scan on ...` yazan satır) ve en alttaki **Execution Time**'ı not et.
2. `user_id` kolonuna index aç (komutu sen yaz, adı `idx_page_views_user_id`).
3. 1. adımdaki sorguyu tekrar çalıştır, aynı iki bilgiyi not et.
4. `EXPLAIN ANALYZE SELECT * FROM page_views WHERE url = '/page/42';` → bu hızlandı mı? Neden?

Chat'e: 1 ve 3'ün scan tipi + süreleri, 4'ün cevabı, ve 2'de yazdığın komut.
Bitince temizlik: `DROP TABLE page_views;`

---

## 6. Görev E — LeetCode SQL (40 dk)

Dil: **PostgreSQL**. Submit = testin.

| #   | Problem                                                                                                                   | Konu                            |
| --- | ------------------------------------------------------------------------------------------------------------------------- | ------------------------------- |
| E1  | [183. Customers Who Never Order](https://leetcode.com/problems/customers-who-never-order/)                                | `NOT EXISTS` / `NOT IN`         |
| E2  | [1978. Employees Whose Manager Left the Company](https://leetcode.com/problems/employees-whose-manager-left-the-company/) | `NOT IN` + NULL'a dikkat        |
| E3  | [176. Second Highest Salary](https://leetcode.com/problems/second-highest-salary/)                                        | scalar subquery (Medium)        |
| E4  | [570. Managers with at Least 5 Direct Reports](https://leetcode.com/problems/managers-with-at-least-5-direct-reports/)    | `IN` + GROUP BY/HAVING (Medium) |

E3 ipucu: "ikinci en büyük" = "en büyükten küçük olanların en büyüğü". Yoksa `null` dönmeli —
aggregate boş kümede ne döndürüyordu?

---

## 7. Kapanış

- A bitince "bitti" → `subqueries.sql`'i ben çalıştırırım.
- B1–B4 tahminleri chat'e (çalıştırmadan önce), B5 bitince "bitti".
- C ve D cevapları chat'e, E için "accepted" yeter.
- ILERLEME satırlarını ben hazırlarım.

Sayaç: 58 + 10 (Q) + 4 (E) = **72** SQL sorusu → hafta hedefi (60+) geçildi.
Yarın (Gün 36): **JDBC** — Java'dan PostgreSQL'e bağlanmak, Gider Takip'i veritabanına taşımak.
