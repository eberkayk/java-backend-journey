# Hafta 6 · Gün 5 (Gün 36) — JDBC: Java'dan PostgreSQL'e

**Hedef:** Java kodundan veritabanına bağlanmak, güvenli sorgu çalıştırmak (`PreparedStatement`),
satırları Java nesnesine çevirmek, transaction'ı Java'dan yönetmek ve **Gider Takip'i CSV'den PostgreSQL'e taşımak**.

**Süre:** ~5 saat
Isınma 15 dk · Ders 75 dk · Görev A 15 dk · Görev B 100 dk · Görev C 30 dk · Görev D 10 dk · Görev E 30 dk

**Kurallar:**
- Kodu sen yazarsın; test kodunu **ben** yazar, senin sınıfına karşı çalıştırırım. Bitince "bitti" de.
- Bugün `projects/expense-tracker` projesinde çalışıyoruz (paket: `com.eberkayk.expense`).

---

## 0. Isınma (15 dk) — cevapları chat'e yaz

**S1.** (SQL) Veri:
```
employees                     departments
 id | name   | dept_id         id | name
----+--------+---------       ----+------
  1 | Ali    |       1          1 | IT
  2 | Ayse   |       1          2 | HR
  3 | Zeynep |    NULL          3 | Legal
```
```sql
SELECT e.name, d.name AS dept
FROM employees e
JOIN departments d ON e.dept_id = d.id;
```
Kaç satır döner, hangileri? Zeynep sonuçta var mı? `JOIN` → `LEFT JOIN` yapınca ne değişir?

**S2.** (SQL) Aynı veri. "Çalışanı olmayan departmanlar" için yazılmış:
```sql
SELECT name FROM departments
WHERE id NOT IN (SELECT dept_id FROM employees);
```
HR ve Legal beklenirken **0 satır** dönüyor. Neden? Nasıl düzeltirsin?

**S3.** (SQL) psql'de, `BEGIN` yazmadan:
```sql
UPDATE books SET copies = copies - 1 WHERE id = 1;   -- copies 3 idi
ROLLBACK;
```
`ROLLBACK`'ten sonra `copies` kaç?

**S4.** (Java)
```java
Map<String, Integer> m = new HashMap<>();
Integer a = m.get("x");
int b = m.get("x");
```
`a`'nın değeri ne? `b` satırında ne olur? `b` satırını nasıl güvenli yazarsın?

---

## 1. Ders (75 dk)

### 1.1 JDBC nedir

Şu ana kadar SQL'i `psql`'e sen yazdın. Uygulamada sorguyu **Java kodu** gönderir.
JDBC = Java'nın veritabanı API'si (`java.sql` paketi). Zincir:

```
Senin kodun  →  java.sql (Connection, PreparedStatement, ResultSet)  →  PostgreSQL driver (.jar)  →  PostgreSQL
```

`java.sql` sadece **arayüz**dür; PostgreSQL ile nasıl konuşulacağını **driver** bilir. Driver'ı Maven ekler.

Bugünkü 4 parça:

| Parça               | Ne işe yarar                                        |
| ------------------- | --------------------------------------------------- |
| `Connection`        | Veritabanına açık bağlantı (psql'i açmak gibi)      |
| `PreparedStatement` | Gönderilecek SQL, `?` yerlerine değer konur         |
| `ResultSet`         | `SELECT` sonucu, satır satır okunur                 |
| `SQLException`      | Her şey bunu fırlatabilir — **checked** exception   |

### 1.2 Driver'ı projeye eklemek (hazır — kopyala)

`projects/expense-tracker/pom.xml`'de `<build>` bloğunun **tamamını** aşağıdakiyle değiştir ve
`</properties>` satırından hemen sonra `<dependencies>` bloğunu ekle:

```xml
    <dependencies>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.7.7</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.6.0</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals><goal>shade</goal></goals>
                        <configuration>
                            <createDependencyReducedPom>false</createDependencyReducedPom>
                            <transformers>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>com.eberkayk.expense.ExpenseCli</mainClass>
                                </transformer>
                            </transformers>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

Neden shade? Eski `maven-jar-plugin` sadece **senin** sınıflarını jar'a koyar; `java -jar` çalışınca
driver'ı bulamaz (`No suitable driver found`). Shade, driver'ı da jar'ın içine koyar ("fat jar").
IntelliJ'de sağ üstte çıkan **Load Maven Changes** butonuna bas.

### 1.3 Bağlanmak + şifreyi koddan uzak tutmak

```java
String url = "jdbc:postgresql://localhost:5432/shop";
//            protokol  driver     host     port  veritabanı

try (Connection conn = DriverManager.getConnection(url, "postgres", password)) {
    // conn ile iş yap
}   // blok bitince conn otomatik kapanır (try-with-resources)
```

- Bağlantı **kaynaktır** (dosya gibi): kapatmazsan PostgreSQL'de açık kalır, bir süre sonra
  `too many connections`. O yüzden hep `try (...)` içinde aç.
- Şifre **asla** koda yazılmaz (GitHub'a gider). Ortam değişkeninden okunur:
  ```java
  String password = System.getenv("DB_PASSWORD");   // yoksa null döner
  ```
  Bir kere PowerShell'de: `setx DB_PASSWORD "postgres_sifren"` → **IntelliJ'i ve terminali kapatıp aç**
  (setx yeni açılan pencerelere etki eder).

### 1.4 SELECT → ResultSet

```java
String sql = "SELECT id, title, published_year FROM books WHERE author_id = ? ORDER BY title";

try (Connection conn = DriverManager.getConnection(url, user, password);
     PreparedStatement ps = conn.prepareStatement(sql)) {
    ps.setInt(1, 3);                                  // 1. '?' yerine 3
    try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {                           // sıradaki satıra geç; satır yoksa false
            int id = rs.getInt("id");
            String title = rs.getString("title");
            int year = rs.getInt("published_year");
            System.out.println(id + " " + title + " " + year);
        }
    }
}
```

- `ResultSet` bir **imleç**tir; başta **ilk satırdan önce** durur. `rs.next()` çağırmadan
  `rs.getString(...)` → `SQLException`. Tek satır bekliyorsan da `if (rs.next())` yazarsın.
- Kolonu **adıyla** oku (`"title"`) — sıra numarasıyla okumak, SELECT değişince sessizce bozulur.
- `?` numaraları **1'den** başlar (dizilerin aksine). `setInt(0, ...)` → `SQLException`.

Tip eşlemesi:

| PostgreSQL     | Okuma                                  | Yazma                         |
| -------------- | -------------------------------------- | ----------------------------- |
| `INTEGER`      | `rs.getInt("c")`                       | `ps.setInt(i, v)`             |
| `TEXT`         | `rs.getString("c")`                    | `ps.setString(i, v)`          |
| `NUMERIC`      | `rs.getDouble("c")`                    | `ps.setDouble(i, v)`          |
| `DATE`         | `rs.getObject("c", LocalDate.class)`   | `ps.setObject(i, localDate)`  |
| enum (`TEXT`)  | `Category.valueOf(rs.getString("c"))`  | `ps.setString(i, cat.name())` |

(Ek bilgi: gerçek projelerde para için `double` değil `BigDecimal` kullanılır — Gider Takip zaten `double`, bugün öyle kalsın.)

### 1.5 Neden `PreparedStatement` — SQL injection

Yanlış yol: kullanıcı girdisini SQL'e **string olarak yapıştırmak**:
```java
String sql = "SELECT * FROM members WHERE email = '" + email + "'";
Statement st = conn.createStatement();
ResultSet rs = st.executeQuery(sql);
```
Kullanıcı `email` olarak `x' OR '1'='1` yazarsa oluşan SQL:
```sql
SELECT * FROM members WHERE email = 'x' OR '1'='1'
```
`'1'='1'` her satırda TRUE → **bütün üyeler** döner. `'; DROP TABLE members; --` ile tablo bile silinebilir.

Doğru yol: SQL'de `?`, değer ayrı gönderilir:
```java
PreparedStatement ps = conn.prepareStatement("SELECT * FROM members WHERE email = ?");
ps.setString(1, email);
```
Driver değeri **her zaman veri** olarak gönderir, asla SQL kodu olarak değil. `x' OR '1'='1`
sadece "böyle garip bir email arıyorum" olur → 0 satır.
**Kural: kullanıcıdan gelen hiçbir değer SQL string'ine `+` ile girmez. Hep `?`.**

### 1.6 INSERT / UPDATE / DELETE → `executeUpdate`

```java
String sql = "INSERT INTO members (full_name, email, joined_on) VALUES (?, ?, ?)";
try (Connection conn = DriverManager.getConnection(url, user, password);
     PreparedStatement ps = conn.prepareStatement(sql)) {
    ps.setString(1, "Deniz Ak");
    ps.setString(2, "deniz@mail.com");
    ps.setObject(3, LocalDate.of(2026, 11, 20));
    int rows = ps.executeUpdate();        // etkilenen satır sayısı → burada 1
}
```
- `SELECT` → `executeQuery()` (ResultSet döner). Değiştirenler → `executeUpdate()` (int döner).
- `id` kolonunu yazmıyorsun; IDENTITY kendisi üretir.
- Aynı `ps`'i döngüde tekrar kullanabilirsin: `set...` → `executeUpdate()` → tekrar `set...` → ...

### 1.7 Transaction Java'dan

Dün gördün: `BEGIN` yazmazsan **her komut kendi başına hemen kalıcı** (autocommit).
JDBC'de de varsayılan **autocommit = true**. "Ya hep ya hiç" için:

```java
try (Connection conn = DriverManager.getConnection(url, user, password)) {
    conn.setAutoCommit(false);                 // = BEGIN
    try (PreparedStatement ps1 = conn.prepareStatement("UPDATE books SET copies = copies - 1 WHERE id = ?");
         PreparedStatement ps2 = conn.prepareStatement("INSERT INTO loans (book_id, member_id, loaned_on) VALUES (?, ?, ?)")) {
        ps1.setInt(1, bookId);
        ps1.executeUpdate();
        ps2.setInt(1, bookId);
        ps2.setInt(2, memberId);
        ps2.setObject(3, LocalDate.now());
        ps2.executeUpdate();
        conn.commit();                         // = COMMIT
    } catch (SQLException e) {
        conn.rollback();                       // = ROLLBACK, ikisi de geri alınır
        throw e;                               // hatayı yutma, yukarı ilet
    }
}
```
- `commit` **son** satırdır; ondan önce bir şey patlarsa `catch`'e düşer ve hiçbir değişiklik kalmaz.
- `rollback`'ten sonra `throw e;` — yoksa çağıran kod "başarılı oldu" sanır.

### 1.8 `SQLException` checked — nereye gider

`SQLException` **checked** exception: ya `catch` edersin ya da metoda `throws SQLException` yazarsın.
Veri katmanı (repository) genelde **yakalamaz**, `throws` ile yukarı iletir; kullanıcıya mesajı
en üstteki katman (CLI) basar. Expense Tracker'da `IOException` ile `saveTo`'da yaptığının aynısı.

### 1.9 Repository fikri

Veritabanı kodunu tek bir sınıfta toplarsın: dışarıdan bakan sadece `save(expense)`, `findAll()` görür;
içeride SQL, `Connection`, `ResultSet` var. CLI SQL bilmez. Yarın CSV'ye geri dönmek istesen
sadece bu sınıf değişir. (Spring'de `@Repository` aynen bu.)

> **Kağıda Yaz**
> 1. `DriverManager.getConnection(url, user, pass)` → `try (...)` içinde, kapanır.
> 2. Şifre koda değil: `System.getenv("DB_PASSWORD")`.
> 3. `SELECT` → `executeQuery` → `while (rs.next())`. Değiştiren → `executeUpdate` → int.
> 4. `?` numaraları 1'den başlar. Kullanıcı girdisi SQL'e `+` ile ASLA girmez.
> 5. DATE ↔ `getObject(c, LocalDate.class)` / `setObject(i, date)`.
> 6. Tx: `setAutoCommit(false)` → işler → `commit()`; `catch` → `rollback()` + `throw e`.
> 7. `SQLException` checked → repository `throws`, CLI yakalar.

---

## 2. Görev A — Veritabanı + tablo (15 dk)

**A1.** Veritabanını oluştur (bir kere, PowerShell):
```powershell
psql -U postgres -h localhost -c "CREATE DATABASE expense_tracker;"
```

**A2.** `projects/expense-tracker/schema.sql` dosyasını yaz. İlk satırı `DROP TABLE IF EXISTS expenses;` olsun.
Tablo **`expenses`**, kolonlar:

| Kolon         | Tip             | Kural                                                    |
| ------------- | --------------- | -------------------------------------------------------- |
| `id`          | `INTEGER`       | otomatik artan PK (Gün 34'teki gibi)                     |
| `spent_on`    | `DATE`          | boş olamaz                                               |
| `description` | `TEXT`          | boş olamaz                                               |
| `category`    | `TEXT`          | boş olamaz, sadece `'FOOD'`, `'HOME'`, `'TRANSPORT'`     |
| `amount`      | `NUMERIC(10,2)` | boş olamaz, 0'dan büyük                                  |

Çalıştır:
```powershell
psql -U postgres -h localhost -d expense_tracker -f projects/expense-tracker/schema.sql
```

---

## 3. Görev B — `ExpenseRepository` (100 dk)

Yeni dosya: `src/main/java/com/eberkayk/expense/ExpenseRepository.java`.
**Diğer sınıflara dokunma** (Expense, Category, ExpenseTracker aynen kalır).

### `ExpenseRepository` sınıfı

**Alanlar:**
- `private final String url;`
- `private final String user;`
- `private final String password;`

**Constructor:**
- `public ExpenseRepository(String url, String user, String password)`
  - `url` veya `user` null/blank → `IllegalArgumentException`.

**Metodlar** (hepsi `throws SQLException`):

1. `private Connection connect()`
   — `DriverManager.getConnection(url, user, password)` döndürür.

2. `public void save(Expense expense)`
   — `expense` null → `IllegalArgumentException`.
   — `expenses` tablosuna tek satır INSERT (`id` hariç 4 kolon).

3. `public List<Expense> findAll()`
   — Bütün satırlar, `spent_on` artan, eşitse `id` artan.
   — Tablo boşsa boş liste (null değil).

4. `public List<Expense> findByMonth(YearMonth month)`
   — `month` null → `IllegalArgumentException`.
   — O ayın harcamaları, sıralama `findAll` ile aynı.
   — Koşul SQL'de olsun: `spent_on >= ? AND spent_on < ?`
     (1. `?` = ayın ilk günü: `month.atDay(1)`; 2. `?` = **sonraki** ayın ilk günü: `month.plusMonths(1).atDay(1)`).

5. `public Map<Category, Double> totalsByCategory()`
   — Kategori başına toplam tutar. Toplama **SQL'de** (`GROUP BY`) yapılsın, Java stream'iyle değil.
   — `TreeMap` döndür. Hiç harcaması olmayan kategori map'te **yok**.

6. `public int saveAll(List<Expense> expenses)`
   — `expenses` null → `IllegalArgumentException`.
   — Hepsini **tek transaction**'da INSERT et: biri bile patlarsa **hiçbiri** kalmaz (rollback + `throw e`).
   — Başarılıysa eklenen satır sayısını döndür.
   — Tek `Connection`, tek `PreparedStatement`, döngüde `set...` + `executeUpdate()`.

**Senin tasarım kararın:** SQL string'lerini `private static final` sabit yapmak, ResultSet satırını
`Expense`'e çeviren `private Expense mapRow(ResultSet rs) throws SQLException` yardımcısı yazmak
(3 metodda aynı çeviri var — tavsiye ederim).

**Ben ne test edeceğim:** Ayrı bir test veritabanında senin sınıfınla: boş tablo, save + findAll sırası,
findByMonth ay sınırları (31 Ekim / 1 Kasım / 30 Kasım / 1 Aralık), totalsByCategory, saveAll'ın
ortasında bir satır patlayınca tablonun boş kalması, null/blank girdiler.

---

## 4. Görev C — CLI'yi veritabanına bağla (30 dk)

`ExpenseCli.java`'da **sadece** şunlar değişiyor:

1. **Sabitler:** `DB_URL = "jdbc:postgresql://localhost:5432/expense_tracker"`, `DB_USER = "postgres"`.
   `main`'in başında `System.getenv("DB_PASSWORD")` oku; null ise `DB_PASSWORD is not set` yaz ve `return`.
2. **Başlangıçtaki CSV yüklemesi** (`if (Files.exists(FILE)) ... loadFrom ...`) **kalkar**;
   yerine bir `ExpenseRepository` oluşturulur.
3. `add` → `tracker.add` + `saveTo` yerine `repository.save(expense)`. Çıktı aynı (`Added: ...`).
4. `list` → `repository.findAll()`. Boşsa yine `No expenses`.
5. `month` → `repository.findByMonth(ym)`; `Total:` satırı aynı kalır.
6. `summary` → `Count`, `Total`, `By month` için `findAll()` sonucunu bir `ExpenseTracker`'a doldurup
   eski metodları kullan; `By category` satırı `repository.totalsByCategory()`'den gelsin.
7. **Yeni komut `import`:** `expenses.csv`'yi (`ExpenseTracker.loadFrom(FILE)`) okuyup
   `repository.saveAll(...)` ile veritabanına yaz, `Imported: N` bas. `USAGE`'a `| import` ekle.
8. `catch`'lere `SQLException` ekle → `Database error: ` + mesaj.

Deneme (proje klasöründe):
```powershell
.\mvnw.cmd clean package
java -jar target\expense-tracker-1.0.0.jar import
java -jar target\expense-tracker-1.0.0.jar list
java -jar target\expense-tracker-1.0.0.jar add 2026-11-20 Lunch FOOD 180
java -jar target\expense-tracker-1.0.0.jar month 2026-11
java -jar target\expense-tracker-1.0.0.jar summary
```
Sonra psql'den gör: `SELECT * FROM expenses ORDER BY id;` — CLI'den eklediğin satır orada.
`import`'u **bir kere** çalıştır (iki kere çalışırsa kayıtlar çift olur — normal, birazdan D2'de konuşacağız).

---

## 5. Görev D — chat'e yaz (10 dk)

**D1.** Biri `ExpenseRepository`'ye şu metodu eklemiş:
```java
public List<Expense> findByDescription(String text) throws SQLException {
    String sql = "SELECT * FROM expenses WHERE description = '" + text + "'";
    ...
}
```
Kullanıcı `text` olarak `x' OR '1'='1` girerse PostgreSQL'e giden SQL tam olarak ne olur, kaç satır döner?
Metodu düzeltmek için **hangi iki satır** değişir?

**D2.** `saveAll`'a 5 harcama veriliyor, **3.** INSERT patlıyor. Tabloda kaç yeni satır kalır:
(a) senin yazdığın haliyle, (b) biri `conn.setAutoCommit(false);` satırını silerse?

**D3.** `import`'u iki kere çalıştırınca kayıtlar çiftleniyor. Tabloya hangi kısıtı eklersen
ikinci `import` **tamamen** reddedilir (tek satır bile eklenmez)? Neden "tek satır bile" eklenmez?

---

## 6. Görev E — LeetCode SQL (30 dk)

Dil: **PostgreSQL**. Submit = testin.

| #   | Problem                                                                                                                  | Konu                       |
| --- | ------------------------------------------------------------------------------------------------------------------------ | -------------------------- |
| E1  | [577. Employee Bonus](https://leetcode.com/problems/employee-bonus/)                                                     | `LEFT JOIN` + NULL koşulu  |
| E2  | [1661. Average Time of Process per Machine](https://leetcode.com/problems/average-time-of-process-per-machine/)          | self join + `AVG`, `ROUND` |

E2 notu: PostgreSQL'de `ROUND(x, 3)` sadece `NUMERIC` alır; `AVG` sonucunu `::numeric` ile çevirmen gerekebilir.

---

## 7. Kapanış

- A, B, C bitince "bitti" → schema'yı ve `ExpenseRepository`'yi ben test veritabanında çalıştırırım, CLI'yi denerim.
- D cevapları chat'e, E için "accepted" yeter.
- ILERLEME satırlarını ben hazırlarım.

Sayaç: 72 + 2 (E) = **74** SQL sorusu.
Yarın (Gün 37): **Hafta 6 sınavı** (SQL + JDBC). Pazar (22 Kasım) izin.
