# Hafta 6 · Gün 3 (Gün 34) — Kısıtlar (PK/FK/UNIQUE/CHECK), JOIN, ER

**Hedef:** Birbirine bağlı tablolar kurmak, veritabanının **yanlış veriyi reddetmesini** sağlamak
ve birden fazla tabloyu **JOIN** ile tek sonuçta birleştirmek.

**Süre:** ~4 saat
Isınma 15 dk · Ders 60 dk · Görev A 40 dk · Görev B 20 dk · Görev C 75 dk · Görev D 10 dk · Görev E 30 dk

**Kurallar (aynı):**
- SQL anahtar kelimeleri BÜYÜK, tablo/kolon adları küçük.
- Bitince "bitti" de; dosyaları **ben** veritabanında çalıştırıp kontrol ederim.

---

## 0. Isınma (15 dk) — cevapları chat'e yaz

**S1.** (Java) Derlenmez. Neden? Lambda satırından **önce tek satır ekleyerek** (ve lambda'da tek kelime
değiştirerek) nasıl derletirsin? O satırı yaz.
```java
int limit = 10;
if (vip) limit = 50;
orders.removeIf(o -> o.total() > limit);
```

**S2.** (SQL) Tablo `t`:
```
 name | price
------+-------
 Pen  |    40
 Mug  |    60
 Lamp |   120
```
İki script, aynı iki komut, **sıraları farklı**. Her script sonunda tabloda **hangi isimler** kalır?
```sql
-- Script A
UPDATE t SET price = price * 2 WHERE price < 100;
DELETE FROM t WHERE price > 100;

-- Script B
DELETE FROM t WHERE price > 100;
UPDATE t SET price = price * 2 WHERE price < 100;
```

**S3.** (tahmin sorusu — bilmemen normal) Java'da kitabın yazarını şöyle tutarsın:
```java
class Book {
    String title;
    Author author;   // nesnenin kendisi
}
```
Bir SQL tablosunun hücresine nesne koyamazsın, sadece sayı/metin/tarih gibi değerler koyarsın.
O zaman `books` tablosunda "bu kitabın yazarı kim" bilgisini **hangi değerle** tutardın?

---

## 1. Ders (60 dk)

### 1.1 Kısıtlar (constraints) — veritabanının bekçileri

Java'da constructor'da `if (price < 0) throw ...` yazıyordun. SQL'de bu kontrolü **tablonun kendisine**
koyarsın; kural bozan INSERT/UPDATE/DELETE **reddedilir**, tablo hiç değişmez.

| Kısıt         | Ne garanti eder                                        | Örnek                                        |
| ------------- | ------------------------------------------------------ | -------------------------------------------- |
| `NOT NULL`    | kolon boş kalamaz                                      | `name TEXT NOT NULL`                         |
| `UNIQUE`      | aynı değer iki satırda olamaz                          | `email TEXT NOT NULL UNIQUE`                 |
| `PRIMARY KEY` | satırın kimliği: `NOT NULL` + `UNIQUE`                 | `id INTEGER GENERATED ... PRIMARY KEY`       |
| `CHECK (...)` | koşul sağlanmalı                                       | `price NUMERIC CHECK (price >= 0)`           |
| `DEFAULT x`   | (kısıt değil) değer verilmezse x yazılır               | `active BOOLEAN NOT NULL DEFAULT true`       |
| `REFERENCES`  | **FOREIGN KEY** — başka tablodaki bir id'yi göstermeli | `dept_id INTEGER REFERENCES departments(id)` |

```sql
CREATE TABLE departments (
    id   INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

CREATE TABLE employees (
    id      INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name    TEXT           NOT NULL,
    salary  NUMERIC(10, 2) NOT NULL CHECK (salary > 0),
    hired   DATE           NOT NULL DEFAULT CURRENT_DATE,   -- verilmezse bugünün tarihi
    dept_id INTEGER        REFERENCES departments(id)       -- NULL olabilir: departmansız çalışan
);
```

**İki kolonu karşılaştıran CHECK** kolonun yanına değil, kolonların **altına** ayrı satır olarak yazılır:
```sql
CREATE TABLE trips (
    id        INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    starts_on DATE NOT NULL,
    ends_on   DATE,
    CHECK (ends_on >= starts_on)
);
```
**CHECK ve NULL:** `ends_on` NULL ise `NULL >= tarih` → **UNKNOWN**. WHERE'de UNKNOWN satırı **atardı**,
CHECK ise UNKNOWN'u **geçirir** (sadece kesin `false` reddedilir). Yani yukarıdaki CHECK, bitiş tarihi
henüz belli olmayan seyahatlere izin verir — ekstra `IS NULL` yazmana gerek yok.

Hata mesajları kısıtı **adıyla** söyler, okumayı öğren:
```
ERROR:  new row for relation "employees" violates check constraint "employees_salary_check"
ERROR:  duplicate key value violates unique constraint "departments_name_key"
ERROR:  null value in column "name" of relation "employees" violates not-null constraint
```

### 1.2 FOREIGN KEY — tablolar arası bağ

S3'ün cevabı: nesneyi değil, **yazarın id'sini** tutarsın. `employees.dept_id = 1` → "bu çalışan id'si 1
olan departmanda". `REFERENCES departments(id)` şunu garanti eder:

- **Olmayan id yazamazsın:** `dept_id = 99` ama 99 numaralı departman yok → INSERT reddedilir.
- **Kullanılan satırı silemezsin:** IT departmanında çalışan varken `DELETE FROM departments WHERE name = 'IT'`
  → reddedilir (yoksa çalışanlar "var olmayan departmanı" gösterirdi).
  Kimsenin göstermediği departmanı silmek serbest.
- Bu yüzden **oluşturma sırası:** önce gösterilen (`departments`), sonra gösteren (`employees`).
  **Silme (DROP) sırası tam tersi:** `DROP TABLE IF EXISTS employees, departments;`

Silmeyi reddetmek varsayılan davranış. İstersen değiştirirsin (bugün kullanmayacağız, bil yeter):
`REFERENCES departments(id) ON DELETE CASCADE` → departman silinince çalışanları da silinir;
`ON DELETE SET NULL` → çalışanların `dept_id`'si NULL olur.

**IDENTITY notu:** reddedilen bir INSERT de sıradaki id'yi "harcar". Sorun değil; id'ler boşluklu olabilir,
id'nin tek görevi benzersiz olmak.

### 1.3 JOIN — iki tabloyu yan yana koymak

Ders boyunca bu veri (Legal'de kimse yok, Zeynep'in departmanı yok):
```
departments              employees
 id | name                id | name   | dept_id
----+-------             ----+--------+---------
  1 | IT                   1 | Ali    |       1
  2 | HR                   2 | Ayse   |       1
  3 | Sales                3 | Veli   |       2
  4 | Legal                4 | Can    |       3
                           5 | Zeynep |    NULL
```

**INNER JOIN** (kısaca `JOIN`) — sadece **eşi bulunan** satırlar:
```sql
SELECT e.name, d.name AS dept
FROM employees e
JOIN departments d ON e.dept_id = d.id
ORDER BY e.name;
```
```
 name | dept
------+-------
 Ali  | IT
 Ayse | IT
 Can  | Sales
 Veli | HR
```
- `employees e` → tabloya kısa ad (alias). İki tabloda da `name` ve `id` var; `e.name` / `d.name` diye
  hangisini kastettiğini **söylemek zorundasın** (yoksa `column reference "name" is ambiguous`).
- `ON e.dept_id = d.id` → eşleşme kuralı: FK = PK.
- Zeynep (dept NULL) ve Legal (kimse yok) **çıkmadı** — eşleri yok.

Java ile düşün: her çalışan için departman listesinde `e.deptId == d.id` olanı arayan iç içe döngü.
Eşleşen her (e, d) çifti bir sonuç satırı.

**LEFT JOIN** — soldaki tablonun **her satırı** gelir; eşi yoksa sağ taraf NULL:
```sql
SELECT e.name, d.name AS dept
FROM employees e
LEFT JOIN departments d ON e.dept_id = d.id
ORDER BY e.name;
```
```
  name  | dept
--------+-------
 Ali    | IT
 Ayse   | IT
 Can    | Sales
 Veli   | HR
 Zeynep | NULL        ← eşi yok ama sol tarafta olduğu için geldi
```
"Sol" = `FROM`'da yazılan, "sağ" = `JOIN`'den sonra yazılan. Yönü sen seçersin:
`FROM departments d LEFT JOIN employees e ...` → her departman gelir, Legal'in çalışan kolonları NULL.

(`RIGHT JOIN` = tabloların yerini değiştirilmiş LEFT JOIN; pratikte herkes LEFT yazar.
`FULL JOIN` = iki taraftan da eşsizler gelir; nadiren gerekir.)

### 1.4 LEFT JOIN kalıpları

**Kalıp 1 — "hiç ... olmayanlar":** LEFT JOIN + sağ tarafın PK'sı `IS NULL`.
```sql
-- Hiç çalışanı olmayan departmanlar
SELECT d.name
FROM departments d
LEFT JOIN employees e ON e.dept_id = d.id
WHERE e.id IS NULL;            -- Legal
```

**Kalıp 2 — "her biri için sayı, sıfırlar dahil":** LEFT JOIN + `COUNT(sağ.id)`.
```sql
SELECT d.name, COUNT(e.id) AS people
FROM departments d
LEFT JOIN employees e ON e.dept_id = d.id
GROUP BY d.id, d.name
ORDER BY d.name;
```
```
 name  | people
-------+--------
 HR    |      1
 IT    |      2
 Legal |      0
 Sales |      1
```
**Tuzak:** `COUNT(*)` yazsaydın Legal **1** çıkardı — Legal satırı (çalışan kolonları NULL) yine de bir satır.
`COUNT(e.id)` NULL'ı atlar → 0. (Dünkü kural: `COUNT(*)` satır sayar, `COUNT(kolon)` NULL atlar.)

`GROUP BY d.id, d.name` → iki departmanın adı aynı olsa bile ayrı gruplar olsun diye id ile gruplarız.

**Kalıp 3 — sağ tabloya koşul: ON mu WHERE mu?**
"Her departman ve maaşı 40000'den yüksek çalışan sayısı (sıfırlar dahil)":
```sql
-- YANLIŞ: WHERE, JOIN'den SONRA çalışır, NULL'lı satırları da atar → 0'lı departmanlar kaybolur
... LEFT JOIN employees e ON e.dept_id = d.id
WHERE e.salary > 40000

-- DOĞRU: koşul ON'da → eşleşme sırasında elenir, departman satırı yine gelir
... LEFT JOIN employees e ON e.dept_id = d.id AND e.salary > 40000
```
Kural: LEFT JOIN'de **sağ** tablonun koşulu → `ON`'a. Sol tablonun koşulu → `WHERE`'e.

### 1.5 Üç tablo ve kendine JOIN

**Zincir:** JOIN'leri art arda yazarsın, her biri bir öncekinin sonucuna eklenir.
```sql
SELECT ...
FROM loans l
JOIN members m ON m.id = l.member_id
JOIN books   b ON b.id = l.book_id;
```

**Self join** — tablo kendini gösteriyorsa (çalışanın yöneticisi de bir çalışan, `manager_id` → `employees.id`),
aynı tabloyu **iki farklı alias'la** iki kez yazarsın:
```sql
SELECT e.name AS employee, m.name AS manager
FROM employees e
JOIN employees m ON e.manager_id = m.id;     -- e = çalışan rolü, m = yönetici rolü
```

### 1.6 Tarih farkı

PostgreSQL'de `DATE - DATE` → **gün sayısı** (tam sayı):
```sql
SELECT DATE '2026-11-18' - DATE '2026-11-01';   -- 17
```

### 1.7 ER diyagramı — tabloları kağıtta tasarlamak

Kod yazmadan önce "hangi tablolar, aralarında nasıl bağ" çizilir. **Entity** = tablo, **Relationship** = bağ.

| İlişki  | Örnek                                         | FK nereye                                           |
| ------- | --------------------------------------------- | --------------------------------------------------- |
| **1–N** | bir departmanda çok çalışan, çalışanın 1 dep. | **N** tarafına: `employees.dept_id`                 |
| **N–N** | bir öğrenci çok ders, bir derste çok öğrenci  | **ara tablo**: `enrollments(student_id, course_id)` |
| **1–1** | bir kullanıcının bir profili                  | birine FK + `UNIQUE` (`profiles.user_id UNIQUE`)    |

Neden N–N'de ara tablo? `students` tablosuna `course_id` koysan öğrenci tek ders alabilir;
`courses`'a `student_id` koysan derste tek öğrenci olur. Bir hücreye tek değer girer → ilişkiyi ayrı satırlarda tut.
Ara tabloya ilişkinin **kendi bilgisi** de konur (kayıt tarihi, not...).

Basit çizim:
```
departments 1 ────< N employees            students 1 ──< enrollments >── 1 courses
                                                        (student_id, course_id, grade)
```
`──<` = "çok" tarafı.

---

## 📝 Kağıda Yaz

```
PK = NOT NULL + UNIQUE · FK = REFERENCES t(id): olmayan id yazılamaz, kullanılan satır silinemez
CHECK UNKNOWN'u (NULL) geçirir, sadece false reddeder
CREATE: önce gösterilen · DROP: önce gösteren
JOIN = eşi olanlar · LEFT JOIN = soldakilerin hepsi, eşi yoksa sağ NULL
"Hiç olmayan": LEFT JOIN + WHERE sağ.id IS NULL
"Sıfırlar dahil sayı": LEFT JOIN + COUNT(sağ.id)   (COUNT(*) değil!)
LEFT JOIN'de sağ tablonun koşulu ON'a, WHERE'e değil
1–N: FK çok tarafta · N–N: ara tablo
```

---

## 2. Görev A — Kütüphane şeması (40 dk)

Dosya: `schema.sql` (bu klasörde, **sen** yazıyorsun). İlk satırı tam olarak şu:
```sql
DROP TABLE IF EXISTS loans, books, members, authors;
```
Sonra 4 tablo. **Kolon adları ve tipleri birebir** böyle olmalı (hazır veri bunlara göre yazıldı).
Hangi kuralı hangi kısıtla sağlayacağın **senin kararın**. Tüm `id`'ler: otomatik artan PK
(`INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY`). Tabloları hangi sırayla oluşturacağını da sen düşün.

### `authors`

| Kolon     | Tip       | Kural        |
| --------- | --------- | ------------ |
| `id`      | `INTEGER` | otomatik PK  |
| `name`    | `TEXT`    | zorunlu      |
| `country` | `TEXT`    | boş olabilir |

### `books`

| Kolon            | Tip       | Kural                                         |
| ---------------- | --------- | --------------------------------------------- |
| `id`             | `INTEGER` | otomatik PK                                   |
| `title`          | `TEXT`    | zorunlu                                       |
| `isbn`           | `TEXT`    | zorunlu, iki kitabın ISBN'i aynı olamaz       |
| `author_id`      | `INTEGER` | zorunlu, var olan bir yazarı göstermeli       |
| `published_year` | `INTEGER` | zorunlu, 1450 ile 2100 arasında (ikisi dahil) |
| `copies`         | `INTEGER` | zorunlu, verilmezse 1, negatif olamaz         |

### `members`

| Kolon       | Tip       | Kural                                     |
| ----------- | --------- | ----------------------------------------- |
| `id`        | `INTEGER` | otomatik PK                               |
| `full_name` | `TEXT`    | zorunlu                                   |
| `email`     | `TEXT`    | zorunlu, iki üyenin e-postası aynı olamaz |
| `joined_on` | `DATE`    | zorunlu, verilmezse bugünün tarihi        |

### `loans` (ödünç kayıtları)

| Kolon         | Tip       | Kural                                                         |
| ------------- | --------- | ------------------------------------------------------------- |
| `id`          | `INTEGER` | otomatik PK                                                   |
| `book_id`     | `INTEGER` | zorunlu, var olan bir kitabı göstermeli                       |
| `member_id`   | `INTEGER` | zorunlu, var olan bir üyeyi göstermeli                        |
| `loaned_on`   | `DATE`    | zorunlu                                                       |
| `returned_on` | `DATE`    | boş olabilir (NULL = hâlâ üyede); `loaned_on`'dan önce olamaz |

Hazır veriyi yükle (`seed.sql` hazır, **değiştirme**):
```powershell
psql -U postgres -h localhost -d shop -f schema.sql -f seed.sql
```
Hatasız biterse: `SELECT COUNT(*) FROM loans;` → `13`.

---

## 3. Görev B — Kısıtları kır (20 dk)

`broken.sql` hazır, 8 komut (K1–K8). **Çalıştırmadan önce** her biri için chat'e yaz:
**geçer mi**, geçmezse **hangi kural / hangi kısıt** durdurur. (Biri geçer.)

Sonra çalıştır ve tahminlerinle karşılaştır:
```powershell
psql -U postgres -h localhost -d shop -f schema.sql -f seed.sql -f broken.sql
```
Tahmin ettiğin bir komut **geçtiyse**, şemanda o kısıt eksik demektir → `schema.sql`'i düzelt, tekrar çalıştır.

---

## 4. Görev C — JOIN sorguları (75 dk)

Önce temiz veri:
```powershell
psql -U postgres -h localhost -d shop -f schema.sql -f seed.sql
```
Sorgular `joins.sql`'e, üstlerine `-- J1` yorumu. Alias'lar tablodaki gibi olsun.

| #   | İstenen                                                                                        | Kolonlar (alias)               | Sıralama                              | Satır | Kontrol                 |
| --- | ---------------------------------------------------------------------------------------------- | ------------------------------ | ------------------------------------- | ----- | ----------------------- |
| J1  | Her kitap ve yazarının adı                                                                     | `title`, `author`              | `title` A→Z                           | 10    | Kar · Orhan Pamuk       |
| J2  | Ülkesi `Turkey` olan yazarların kitapları                                                      | `title`, `published_year`      | `published_year` artan                | 6     | ilk: Kuyucakli Yusuf    |
| J3  | Her yazar ve kitap sayısı — **kitabı olmayan yazarlar 0 ile** gelsin                           | `author`, `book_count`         | `book_count` azalan, sonra author     | 7     | Yasar Kemal 0           |
| J4  | Hiç kitabı olmayan yazarlar                                                                    | `author`                       | `author` A→Z                          | 2     | Jane Doe                |
| J5  | Tüm ödünçler: kim, hangi kitap, ne zaman                                                       | `member`, `title`, `loaned_on` | `loaned_on` artan                     | 13    | ilk: Ali Yilmaz · 1984  |
| J6  | Hâlâ iade edilmemiş ödünçler                                                                   | `member`, `title`, `loaned_on` | `loaned_on` artan                     | 4     | ilk: Zeynep Celik · Kar |
| J7  | Hiç kitap ödünç almamış üyeler                                                                 | `member`                       | —                                     | 1     | Elif Arslan             |
| J8  | Hiç ödünç verilmemiş kitaplar                                                                  | `title`                        | `title` A→Z                           | 3     | The Idiot               |
| J9  | En az 3 kez ödünç almış üyeler ve ödünç sayıları                                               | `member`, `loan_count`         | `loan_count` azalan, sonra member     | 3     | Ali Yilmaz 4            |
| J10 | Her yazarın kitapları toplam kaç kez ödünç verilmiş (hiç verilmemiş yazar çıkmasın)            | `author`, `times_loaned`       | `times_loaned` azalan, sonra author   | 5     | George Orwell 5         |
| J11 | İade edilmiş ve 14 günden **uzun** tutulmuş ödünçler: kim, hangi kitap, kaç gün                | `member`, `title`, `days_kept` | `days_kept` azalan                    | 2     | Crime and Punishment 38 |
| J12 | Her üye ve **Kasım 2026'da** aldığı ödünç sayısı — **o ay hiç almayan üyeler de 0 ile** gelsin | `member`, `november_loans`     | `november_loans` azalan, sonra member | 6     | Zeynep Celik 0          |

İpuçları (takılırsan bak):
- J3, J12: 1.4 Kalıp 2'deki tuzak.
- J10: dört tablodan kaçına gerçekten ihtiyacın var? `members` gerekli mi?
- J12: 1.4 Kalıp 3. 3 satır geliyorsa koşulun yanlış yerde.

---

## 5. Görev D — ER soruları (10 dk) — chat'e yaz

Senaryo: bir e-ticaret sitesi. **Müşteri** sipariş verir, **sipariş** içinde birden çok **ürün** olur
(her ürün için adet bilgisiyle), bir ürün de birçok siparişte yer alır.

**D1.** `customers` ile `orders` arasındaki ilişki 1–1, 1–N, N–N'den hangisi? FK hangi tabloya, adı ne olur?
**D2.** `orders` ile `products` arasındaki ilişki hangisi? Bunun için hangi tabloyu eklersin;
kolonlarını yaz (adet bilgisi nereye gider?).
**D3.** Bugünkü kütüphanede `members` ile `books` arasındaki ilişki ne, ve ara tablo hangisi?

---

## 6. Görev E — LeetCode SQL (30 dk)

Dil: **PostgreSQL**. Submit = testin.

| #   | Problem                                                                                                                                               | Konu                           |
| --- | ----------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------ |
| E1  | [175. Combine Two Tables](https://leetcode.com/problems/combine-two-tables/)                                                                          | LEFT JOIN                      |
| E2  | [1378. Replace Employee ID With The Unique Identifier](https://leetcode.com/problems/replace-employee-id-with-the-unique-identifier/)                 | LEFT JOIN                      |
| E3  | [1581. Customer Who Visited but Did Not Make Any Transactions](https://leetcode.com/problems/customer-who-visited-but-did-not-make-any-transactions/) | LEFT JOIN + IS NULL + GROUP BY |
| E4  | [181. Employees Earning More Than Their Managers](https://leetcode.com/problems/employees-earning-more-than-their-managers/)                          | self join                      |

---

## 7. Kapanış

- A + B bitince "bitti" yaz (B tahminlerinle birlikte) → `schema.sql`'i ben çalıştırıp kontrol ederim.
- C bitince "bitti" → `joins.sql`'i ben çalıştırırım.
- D cevapları chat'e, E için "accepted" yeter.
- ILERLEME satırlarını ben hazırlarım.

Sayaç: 42 + 12 (J) + 4 (E) = **58** SQL sorusu (hafta hedefi 60+, yarın geçiyoruz).
