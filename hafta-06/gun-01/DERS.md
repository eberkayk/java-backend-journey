# Hafta 6 · Gün 1 (Gün 32) — SQL'e Giriş: PostgreSQL Kurulumu + SELECT Temelleri

**Hedef:** PostgreSQL'i kurmak, ilk tabloyu kendin oluşturmak ve tek tablodan veri çekmenin
her parçasını (WHERE, ORDER BY, LIMIT, DISTINCT, NULL, LIKE, IN, BETWEEN) kullanmak.

**Süre:** ~3.5 saat
Isınma 15 dk · Kurulum 30 dk · Ders 45 dk · Görev A 90 dk · Görev B 30 dk · Kapanış 10 dk

**Bu haftanın yolu (6 gün, Pazar izin):**

| Gün | Tarih  | Konu                                                       |
| --- | ------ | ---------------------------------------------------------- |
| 32  | 16 Kas | Kurulum, CREATE/INSERT, SELECT + WHERE + ORDER BY + NULL   |
| 33  | 17 Kas | Aggregate (COUNT/SUM/AVG), GROUP BY, HAVING, UPDATE/DELETE |
| 34  | 18 Kas | Constraint'ler (PK/FK/UNIQUE/CHECK), JOIN, ER diyagram     |
| 35  | 19 Kas | Subquery, CTE, normalizasyon, index, transaction           |
| 36  | 20 Kas | JDBC: Java'dan PostgreSQL'e bağlanmak (Gider Takip)        |
| 37  | 21 Kas | Haftalık tekrar + mini sınav                               |

**Kurallar:**
- SQL anahtar kelimeleri BÜYÜK (`SELECT`, `WHERE`), tablo/kolon adları küçük ve İngilizce.
- Sorguları `queries.sql`'e yaz. Bitince "bitti" de; dosyayı **ben** veritabanında çalıştırıp kontrol ederim.

---

## 0. Isınma (15 dk) — cevapları chat'e yaz

Kapı sınavında kaçanlar. SQL'e geçmeden son bir Java turu.

**S1.** Bu kod çalışınca ne olur? Hatayı **tek satır ekleyerek** düzelt (hangi satırın önüne ekleyeceğini de söyle).
```java
Map<String, List<String>> courses = new HashMap<>();
courses.get("ali").add("CS101");
System.out.println(courses);
```

**S2.** Bu kod derlenmez, hata `total += x` satırında. **Neden?** (Sebep `List.of` değil.)
```java
int total = 0;
List.of(1, 2, 3).forEach(x -> total += x);
System.out.println(total);
```

**S3.** `enroll` metodu derlenir mi? Derlenmiyorsa **iki farklı** düzeltme yolu yaz (birer satır yeter).
```java
class CourseFullException extends Exception {
    CourseFullException(String msg) { super(msg); }
}

void enroll(String code) {
    throw new CourseFullException("Course full: " + code);
}
```

---

## 1. Kurulum (30 dk)

### 1.1 PostgreSQL 18'i kur

PowerShell'de:
```powershell
winget install PostgreSQL.PostgreSQL.18 --interactive
```
Kurulum sihirbazında:

| Ekran      | Seçim                                                                      |
| ---------- | -------------------------------------------------------------------------- |
| Components | PostgreSQL Server, pgAdmin 4, Command Line Tools (Stack Builder boş)       |
| Password   | `postgres` kullanıcısının şifresi — **unutma**, bir yere yaz               |
| Port       | `5432` (varsayılan)                                                        |
| Locale     | `English, United States` (Türkçe İ/ı sıralama sürprizlerinden kaçmak için) |

### 1.2 `psql`'i PATH'e ekle

PowerShell'de (kendi kullanıcı PATH'ine ekler, sistem PATH'ine dokunmaz):
```powershell
$u = [Environment]::GetEnvironmentVariable("Path", "User")
[Environment]::SetEnvironmentVariable("Path", "$u;C:\Program Files\PostgreSQL\18\bin", "User")
```
Terminali **kapat-aç**, sonra `psql --version` → `psql (PostgreSQL) 18.x` görmelisin.

### 1.3 Şifreyi her seferinde sormasın (ve ben de sorgularını çalıştırabileyim)

`%APPDATA%\postgresql\pgpass.conf` dosyasını oluştur (klasör yoksa aç), içine tek satır:
```
localhost:5432:*:postgres:SENIN_SIFREN
```
Bu sadece bu bilgisayardaki yerel veritabanı için; şifre bu dosyada düz yazı durur, o yüzden
gerçek bir sunucunun şifresini **asla** buraya koyma.

### 1.4 `shop` veritabanını aç

```powershell
psql -U postgres -h localhost
```
`postgres=#` istemi gelince:
```sql
CREATE DATABASE shop;
\c shop
```
İstem `shop=#` olmalı. Çıkmak için `\q`.

**psql komutları** (`\` ile başlayanlar SQL değil, psql'in kendi komutları; sonlarına `;` gerekmez):

| Komut          | Ne yapar                                   |
| -------------- | ------------------------------------------ |
| `\l`           | veritabanlarını listeler                   |
| `\c shop`      | `shop` veritabanına geçer                  |
| `\dt`          | tabloları listeler                         |
| `\d products`  | tablonun kolonlarını ve tiplerini gösterir |
| `\i dosya.sql` | dosyadaki SQL'i çalıştırır                 |
| `\q`           | çıkış                                      |

**Dikkat:** SQL komutu `;` görene kadar bitmez. `;` unutursan istem `shop-#` olur → `;` yazıp Enter.

---

## 2. Ders (45 dk)

### 2.1 Tablo = sınıf, satır = nesne

| Java                   | SQL                            |
| ---------------------- | ------------------------------ |
| `record Employee(...)` | `CREATE TABLE employees (...)` |
| field (`String name`)  | kolon (`name TEXT`)            |
| bir nesne              | bir satır (row)                |
| `List<Employee>`       | tablonun tamamı                |
| `.filter(e -> ...)`    | `WHERE`                        |
| `.map(e -> e.name())`  | `SELECT name`                  |
| `.sorted(...)`         | `ORDER BY`                     |
| `.limit(3)`            | `LIMIT 3`                      |
| `.distinct()`          | `DISTINCT`                     |

Fark: SQL'de **nasıl** bulunacağını yazmazsın, **ne** istediğini yazarsın. Döngü yok.

### 2.2 Tipler

| Tip              | Ne için                                     | Java karşılığı  |
| ---------------- | ------------------------------------------- | --------------- |
| `INTEGER`        | tam sayı                                    | `int`           |
| `BIGINT`         | büyük tam sayı                              | `long`          |
| `NUMERIC(10, 2)` | para (toplam 10 hane, 2'si virgülden sonra) | `BigDecimal`    |
| `TEXT`           | metin (uzunluk sınırı yok)                  | `String`        |
| `VARCHAR(50)`    | en fazla 50 karakter metin                  | `String`        |
| `BOOLEAN`        | `true` / `false`                            | `boolean`       |
| `DATE`           | tarih (`'2026-11-16'`)                      | `LocalDate`     |
| `TIMESTAMP`      | tarih + saat                                | `LocalDateTime` |

Para için **asla** `REAL`/`DOUBLE PRECISION` kullanma (Java'daki `double` yuvarlama derdinin aynısı).

### 2.3 CREATE TABLE ve INSERT

```sql
DROP TABLE IF EXISTS employees;          -- varsa sil (dosyayı tekrar tekrar çalıştırabilmek için)

CREATE TABLE employees (
    id         INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,  -- 1, 2, 3... otomatik
    name       TEXT    NOT NULL,                                  -- boş bırakılamaz
    dept       TEXT    NOT NULL,
    salary     NUMERIC(10, 2) NOT NULL,
    active     BOOLEAN NOT NULL DEFAULT true,                     -- yazılmazsa true olur
    manager_id INTEGER                                            -- NOT NULL yok → NULL olabilir
);

INSERT INTO employees (name, dept, salary, manager_id) VALUES
    ('Ayse',   'IT',    42000, NULL),
    ('Mehmet', 'IT',    35000, 1),
    ('Zeynep', 'HR',    30000, NULL),
    ('Can',    'Sales', 28000, 3),
    ('Elif',   'IT',    51000, 1);
```
- `PRIMARY KEY`: her satırı benzersiz tanımlayan kolon (tekrar edemez, NULL olamaz).
- `GENERATED ALWAYS AS IDENTITY`: `id`'yi veritabanı verir, sen INSERT'te **yazmazsın**.
- INSERT'teki kolon listesinde **olmayan** kolon → `DEFAULT`'u varsa o, yoksa `NULL` olur. Yukarıda `active` yazılmadı → hepsi `true`.
- Metin ve tarih **tek tırnak**: `'IT'`, `'2026-11-16'`. Çift tırnak (`"name"`) kolon/tablo adı demektir, metin değil.

### 2.4 SELECT ... FROM ... WHERE ... ORDER BY ... LIMIT

```sql
SELECT name, salary
FROM employees
WHERE dept = 'IT'
ORDER BY salary DESC;
```
```
  name  |  salary
--------+----------
 Elif   | 51000.00
 Ayse   | 42000.00
 Mehmet | 35000.00
```

**WHERE operatörleri:**

| Yazım                            | Anlamı                                                 |
| -------------------------------- | ------------------------------------------------------ |
| `=`, `<>`, `<`, `<=`, `>`, `>=`  | eşit (tek `=`!), eşit değil, küçük...                  |
| `AND`, `OR`, `NOT`, `( )`        | Java'daki `&&`, `||`, `!` — parantezle grupla          |
| `dept IN ('IT', 'HR')`           | `dept = 'IT' OR dept = 'HR'`'nin kısası                |
| `salary BETWEEN 30000 AND 42000` | `>= 30000 AND <= 42000` — **iki uç da dahil**          |
| `name LIKE 'A%'`                 | `A` ile başlayan. `%` = 0+ karakter                    |
| `name LIKE '_e%'`                | 2. harfi `e` olan. `_` = **tam 1** karakter            |
| `name ILIKE '%ay%'`              | büyük/küçük harf **duyarsız** LIKE (PostgreSQL'e özel) |

**ORDER BY:** `ASC` (varsayılan, küçükten büyüğe) / `DESC`. Birden fazla kolon:
`ORDER BY dept ASC, salary DESC` → önce departmana göre, aynı departmanda maaşa göre azalan.

**LIMIT / OFFSET:** `LIMIT 2` → ilk 2 satır. `LIMIT 2 OFFSET 2` → ilk 2'yi atla, sonraki 2'yi ver (sayfalama: sayfa `n` = `OFFSET (n-1) * boyut`).
ORDER BY olmadan LIMIT'in hangi satırları vereceği **garanti değildir** — her zaman ikisini birlikte kullan.

**DISTINCT:** tekrarları atar.
```sql
SELECT DISTINCT dept FROM employees ORDER BY dept;   -- HR, IT, Sales (3 satır, 5 değil)
```

### 2.5 NULL — SQL'in en çok tuzak kuran yeri

`NULL` = "bilinmiyor". Bilinmeyen bir şeyle yapılan her karşılaştırmanın sonucu da **bilinmiyor**'dur
(ne `true` ne `false`), ve WHERE sadece `true` olan satırları alır.

```sql
SELECT name FROM employees WHERE manager_id = NULL;      -- 0 satır! (= NULL asla true olmaz)
SELECT name FROM employees WHERE manager_id IS NULL;     -- Ayse, Zeynep
SELECT name FROM employees WHERE manager_id <> 1;        -- sadece Can!
```
Son sorguda Ayse ve Zeynep **gelmez**: `NULL <> 1` → bilinmiyor → alınmaz.
"Müdürü 1 olmayan herkes (müdürü olmayanlar dahil)" istiyorsan NULL'u açıkça eklemelisin:
`WHERE manager_id <> 1 OR manager_id IS NULL`.

`COALESCE(a, b)`: `a` NULL değilse `a`, NULL ise `b`. Java'daki `Optional.ofNullable(a).orElse(b)`.
```sql
SELECT name, COALESCE(manager_id, 0) AS manager FROM employees;   -- NULL yerine 0 yazar
```

### 2.6 Hesaplanan kolon, AS ve çalışma sırası

```sql
SELECT name, salary * 12 AS yearly FROM employees ORDER BY yearly DESC;   -- çalışır
SELECT name, salary * 12 AS yearly FROM employees WHERE yearly > 400000;  -- HATA: column "yearly" does not exist
```
Neden? SQL yazıldığı sırayla **çalışmaz**:

```
FROM → WHERE → SELECT → DISTINCT → ORDER BY → LIMIT
```
WHERE çalışırken SELECT henüz çalışmadı, `yearly` diye bir şey yok. ORDER BY ise SELECT'ten sonra, onu görür.
Çözüm: WHERE'de ifadeyi tekrar yaz → `WHERE salary * 12 > 400000`.

---

## 📝 Kağıda Yaz

```
Çalışma sırası: FROM → WHERE → SELECT → DISTINCT → ORDER BY → LIMIT
SELECT'teki alias WHERE'de YOK, ORDER BY'da VAR
= NULL asla true olmaz → IS NULL / IS NOT NULL
col <> 'x' → NULL satırları da DIŞARIDA kalır (istersen OR col IS NULL)
'metin' tek tırnak · "kolon_adi" çift tırnak
BETWEEN iki ucu dahil · LIKE: % = 0+ karakter, _ = tam 1 · ILIKE = harf duyarsız
LIMIT'i ORDER BY'sız kullanma · sayfa n → OFFSET (n-1)*boyut
Para = NUMERIC, asla REAL/DOUBLE
```

---

## 3. Görev A — Mağaza Veritabanı (90 dk)

Dosyalar `hafta-06/gun-01/` içinde:

| Dosya         | Kimin     | İçerik                                            |
| ------------- | --------- | ------------------------------------------------- |
| `schema.sql`  | **senin** | `DROP TABLE` + `CREATE TABLE products` + 2 INSERT |
| `seed.sql`    | hazır     | 12 ürün (değiştirme)                              |
| `queries.sql` | **senin** | Q1–Q13 sorguları                                  |

Veritabanını sıfırlamak için (istediğin kadar tekrar çalıştırabilirsin):
```powershell
psql -U postgres -h localhost -d shop -f schema.sql -f seed.sql
```
Sorguları çalıştırmak için:
```powershell
psql -U postgres -h localhost -d shop -f queries.sql
```

### A1. `schema.sql` — tabloyu oluştur

Dosya `DROP TABLE IF EXISTS products;` ile başlasın. Sonra tablo:

**Tablo:** `products`

| Kolon          | Tip              | Kural                                          |
| -------------- | ---------------- | ---------------------------------------------- |
| `id`           | `INTEGER`        | otomatik artan, primary key                    |
| `name`         | `TEXT`           | boş olamaz                                     |
| `category`     | `TEXT`           | boş olamaz                                     |
| `price`        | `NUMERIC(10, 2)` | boş olamaz                                     |
| `stock`        | `INTEGER`        | boş olamaz                                     |
| `discontinued` | `BOOLEAN`        | boş olamaz, yazılmazsa `false`                 |
| `added_on`     | `DATE`           | boş olamaz                                     |
| `supplier`     | `TEXT`           | **boş olabilir** (tedarikçisi bilinmeyen ürün) |

Kolon adları **birebir** bu olmalı, yoksa `seed.sql` çalışmaz.

### A2. `schema.sql` — kendi 2 ürününü ekle

Aynı dosyanın sonuna **tek** bir `INSERT` ile bu 2 ürünü ekle:

| name                 | category      | price  | stock | added_on     | supplier |
| -------------------- | ------------- | ------ | ----- | ------------ | -------- |
| `Gaming Headset PRO` | `electronics` | 999.00 | 9     | `2026-11-01` | `Logi`   |
| `Notebook`           | `home`        | 45.00  | 200   | `2026-11-16` | (yok)    |

Şartlar:
- `id` ve `discontinued` kolonlarını INSERT'te **yazma** (biri otomatik, diğeri DEFAULT'tan gelsin).
- `Notebook`'un tedarikçisi yok → NULL olacak. Bunu `NULL` yazarak yap.

Kontrol: sıfırlama komutunu çalıştır, sonra psql'de `SELECT COUNT(*) FROM products;` → `14`.

### A3. `queries.sql` — Q1–Q13

Her sorgunun üstüne `-- Q1` gibi bir yorum satırı koy. **Kolonlar ve sıralama tam istendiği gibi** olsun.
Sağdaki sayı, doğru sorgunun döndürmesi gereken satır sayısı (kendini kontrol etmen için).

| #   | İstenen                                                                                | Kolonlar                      | Sıralama                                | Satır |
| --- | -------------------------------------------------------------------------------------- | ----------------------------- | --------------------------------------- | ----- |
| Q1  | Tüm ürünler                                                                            | `name`, `price`               | fiyat pahalıdan ucuza                   | 14    |
| Q2  | `electronics` kategorisinde olup satıştan **kalkmamış** (`discontinued` false) ürünler | `name`, `price`               | `name` A→Z                              | 4     |
| Q3  | Fiyatı 100 ile 500 arasında (ikisi de dahil) ürünler — `BETWEEN` kullan                | `name`, `price`               | fiyat ucuzdan pahalıya                  | 7     |
| Q4  | Adında `pro` geçen ürünler, harf büyüklüğü fark etmez (`Pro`, `PRO`, `pro` hepsi)      | `name`                        | `name` A→Z                              | 3     |
| Q5  | Kategorisi `books` ya da `toys` olanlar — `IN` kullan                                  | `name`, `category`            | önce `category` A→Z, sonra fiyat azalan | 6     |
| Q6  | Tedarikçisi bilinmeyen ürünler                                                         | `name`                        | `name` A→Z                              | 4     |
| Q7  | Tedarikçisi `Acme` **olmayan** ürünler — tedarikçisi bilinmeyenler de **dahil**        | `name`, `supplier`            | `name` A→Z                              | 10    |
| Q8  | Hangi kategoriler var? Her biri bir kez                                                | `category`                    | A→Z                                     | 4     |
| Q9  | En pahalı 3 ürün                                                                       | `name`, `price`               | fiyat azalan                            | 3     |
| Q10 | Stok değeri (`price * stock`) 5000'den büyük olan ürünler                              | `name`, `stock_value` (alias) | `stock_value` azalan                    | 8     |
| Q11 | 2026 yılı içinde eklenmiş ürünler                                                      | `name`, `added_on`            | `added_on` eskiden yeniye               | 8     |
| Q12 | Tüm ürünler; tedarikçisi bilinmiyorsa `unknown` yazsın                                 | `name`, `supplier` (alias)    | `name` A→Z                              | 14    |
| Q13 | Ürünler fiyata göre ucuzdan pahalıya dizilmiş; sayfa boyutu 4 → **2. sayfa**           | `name`, `price`               | fiyat ucuzdan pahalıya                  | 4     |

---

## 4. Görev B — LeetCode SQL (30 dk)

LeetCode → problemin sayfasında dil olarak **PostgreSQL** seç. Submit = testin.

| #   | Problem                                                                                                 | Konu                        |
| --- | ------------------------------------------------------------------------------------------------------- | --------------------------- |
| B1  | [1757. Recyclable and Low Fat Products](https://leetcode.com/problems/recyclable-and-low-fat-products/) | WHERE + AND                 |
| B2  | [595. Big Countries](https://leetcode.com/problems/big-countries/)                                      | WHERE + OR                  |
| B3  | [584. Find Customer Referee](https://leetcode.com/problems/find-customer-referee/)                      | NULL tuzağı                 |
| B4  | [1148. Article Views I](https://leetcode.com/problems/article-views-i/)                                 | DISTINCT + alias + ORDER BY |

---

## 5. Kapanış (10 dk)

- Görev A bitince "bitti" yaz → `queries.sql`'i veritabanında ben çalıştırıp her sorguyu kontrol ederim.
- B1–B4 için "accepted" yazman yeter.
- ILERLEME satırlarını ben hazırlarım, sen yapıştırırsın.

Bugünkü sayaç: **13 + 4 = 17** SQL sorusu (hafta hedefi 60+).
