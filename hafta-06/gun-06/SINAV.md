# Hafta 6 Sınavı — Gün 37 (SQL + JDBC)

Başlangıç saati:
Bitiş saati:

Kapalı kitap. Not, eski kod, internet, AI yok. Sadece `Cevap:` satırlarını doldur.
Sorgu sonucu sorulan yerde satırları tablo gibi yaz (`Ali | 2 | 2` yeterli). Boş sonuç → `0 satır`.

---

## Bölüm A — Teori (her biri 1–3 cümle) · 20 puan

**A1. (2p)** `PRIMARY KEY`, `UNIQUE`, `FOREIGN KEY` — her biri neyi garanti eder (tek cümle)?
Cevap: PRIMARY KEY: değerlerin benzersiz ve zorunlu olmasını garanti eder UNIQUE: değerlerin benzersiz olmasını garanti eder FOREIGN KEY: değerlerin başka bir tablodan alındığını o tabloda olmayan değerlerin olmayacağını garanti eder.

**A2. (3p)** Bu sorgu neden hata verir? Nasıl düzeltirsin? `FROM, WHERE, GROUP BY, HAVING, SELECT, ORDER BY` hangi sırayla çalışır?
```sql
SELECT category, COUNT(*) AS cnt
FROM products
WHERE COUNT(*) >= 2
GROUP BY category;
```
Cevap: WHERE'de aggregate olmaz bu yüzden hata verir. WHERE silinip HAVING COUNT(*) >= 2 ile düzeltilir

**A3. (2p)** `JOIN` (INNER) ile `LEFT JOIN` farkı ne? `LEFT JOIN` yazıp `WHERE` içinde **sağ** tablonun bir kolonuna koşul koyarsan (ör. `WHERE o.status = 'PAID'`) ne olur?
Cevap: JOIN bir tabloyu kendi üzerine eklemektir. LEFT JOIN başka bir tabloyu bir tablonun üzerine eklemektir, soldaki tablodaki tüm değerlerin gelmesini sağlar. LEFT JOIN'de WHERE içine sağ tablonun bir kolonuna koşul koyarsan hata verir.

**A4. (3p)** Tek tablo:
```
orders_flat
 order_id | customer_name | customer_city | product_name | product_price | quantity
----------+---------------+---------------+--------------+---------------+---------
        1 | Ali           | Istanbul      | Mouse        |           500 |        2
        1 | Ali           | Istanbul      | Lamp         |           750 |        1
        2 | Ali           | Istanbul      | Mouse        |           500 |        1
```
Bu tasarımın 2 sorununu yaz (Ali başka şehre taşınırsa / Mouse'un fiyatı değişirse ne olur?). Hangi tablolara bölersin (sadece tablo adları + kolonları)?
Cevap: bir üründe veya bir müşteride değişiklik yapmak için birden fazla satır güncellenmesi gerekiyor. Customer(name, city) Product(name, price) Order(id, customer_id, product_id, qty)

**A5. (2p)** Index neyi hızlandırır? Her kolona index koymak neden iyi fikir değil?
Cevap: aramayı hızlandırır. çünkü index'in bir bedeli vardır. her yere koymak arama dışındaki diğer işlemleri yavaşlatır.

**A6. (3p)** Transaction'da "atomicity" ne demek? `BEGIN` yazmadan çalıştırdığın bir `UPDATE`, ardından gelen `ROLLBACK` ile geri alınır mı, neden?
Cevap: atomicity ya hep ya hiç demektir. begin yazmadan bir işlem rollback ile geri alınamaz çünkü rollback yapılacak bir nokta belirlenmemiştir.

**A7. (3p)** Bu metotta ne tehlikeli? `name` olarak `x' OR '1'='1` gelirse PostgreSQL'e giden SQL tam olarak ne olur? Düzeltmek için ne kullanırsın?
```java
String sql = "SELECT * FROM customers WHERE name = '" + name + "'";
```
Cevap: dışarıdan gelecek bir değişkenin sorguya string olarak eklenmesi tehlikelidir. name x' or '1'='1 gelirse 1=1 her zaman true olacağı için tüm satırlara işlem yapılır düzeltmek için dışarıdan gelen verileri string olarak değil ? olarak sorguya yazarız bu da SQLInjection'un önüne geçer.

**A8. (2p)** `executeQuery()` ve `executeUpdate()` ne döndürür, hangisi hangi SQL için? `Connection`'ı neden `try (...)` içinde açarız?
Cevap:executeQuery ResultSet döndürür, executeUpdate int döndürür. executeQuery sql içindir. Connection'u SQLException fırlatabileceği için try içinde açarız.

---

## Bölüm B — Çıktı tahmini (ÇALIŞTIRMADAN) · 25 puan (her blok 5p)

B1–B3 bu veriyle:
```
students        enrollments                      club_members
 id | name       student_id | course | grade      student_id | club
----+------      -----------+--------+------      -----------+------
  1 | Ali                 1 | SQL    |    80               1 | Chess
  2 | Ayse                1 | Java   |    60            NULL | Chess
  3 | Can                 2 | SQL    |  NULL               2 | Music
                          2 | Java   |    90
```

**B1. (JOIN + COUNT)**
```sql
SELECT s.name, COUNT(*) AS c1, COUNT(e.grade) AS c2
FROM students s
LEFT JOIN enrollments e ON e.student_id = s.id
GROUP BY s.name
ORDER BY s.name;
```
Cevap: Ali | 2 | 2
Ayse | 2 | 1
Can | 0 | 0

**B2. (GROUP BY / HAVING / boş aggregate)**
```sql
-- (a)
SELECT student_id, AVG(grade) AS avg_grade
FROM enrollments
GROUP BY student_id
HAVING AVG(grade) > 70;

-- (b)
SELECT student_id, AVG(grade) AS avg_grade
FROM enrollments
WHERE grade > 70
GROUP BY student_id;

-- (c)  tabloda Python dersi yok
SELECT COUNT(*), SUM(grade) FROM enrollments WHERE course = 'Python';
```
Cevap (a): 2 | 90
Cevap (b): 1 | 80
2 | 90
Cevap (c): 0 | 0

**B3. (subquery)**
```sql
-- (a)
SELECT name FROM students
WHERE id NOT IN (SELECT student_id FROM club_members);

-- (b)
SELECT name FROM students s
WHERE NOT EXISTS (SELECT 1 FROM club_members m WHERE m.student_id = s.id);
```
Cevap (a): -
Cevap (b): Can
(a) ile (b) neden farklı (ya da aynı)? Cevap: NULL değer olduğu için

**B4. (transaction, psql)** Başlangıç: `accounts` → `(1, 100)`, `(2, 50)`. Tabloda `CHECK (balance >= 0)` var.
```sql
BEGIN;
UPDATE accounts SET balance = balance - 30 WHERE id = 1;   -- (1)
UPDATE accounts SET balance = balance - 80 WHERE id = 2;   -- (2)
UPDATE accounts SET balance = balance + 30 WHERE id = 2;   -- (3)
COMMIT;                                                    -- (4)
UPDATE accounts SET balance = balance + 5  WHERE id = 1;   -- (5)
ROLLBACK;                                                  -- (6)
SELECT * FROM accounts;
```
(2), (3), (4) satırlarında ne olur? Son `SELECT` ne yazdırır?
Cevap (2): geçmez
Cevap (3): üst satır geçmediği için buna gelinmez
Cevap (4): patlar
Cevap (SELECT): 1 | 100
2 | 50

**B5. (JDBC)** Tablo boş. Listede 4 harcama var; **3.** INSERT patlıyor (tabloda bir CHECK kısıtını ihlal ediyor).
```java
public int saveAll(List<Expense> expenses) throws SQLException {
    int count = 0;
    try (Connection conn = connect()) {
        conn.setAutoCommit(false);
        try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            for (Expense e : expenses) {
                // ... set'ler ...
                count += ps.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
        }
    }
    return count;
}
```
(a) Metot bittiğinde tabloda kaç satır var? (b) Metot exception fırlatır mı, ne döndürür? (c) Bu kodun hatası ne, hangi **tek satırı** eklersin?
Cevap (a): 0 
Cevap (b): fırlatır, 0 döndürür
Cevap (c): catch'de hata yok sayılmış, e.getMessage(); yazılması lazımdı
