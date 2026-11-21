# Hafta 6 · Gün 6 (Gün 37) — HAFTA 6 SINAVI (SQL + JDBC)

**Hedef:** Hafta 6'nın tamamını (SELECT → JOIN → GROUP BY → subquery/CTE → transaction → JDBC) tek sınavla ölçmek.

**Süre:** ~3.5 saat · Kurulum 10 dk · Tekrar 15 dk · Sınav 150 dk · Mola · Not + düzeltme · LeetCode 30 dk

**Kurallar:**
- Kapalı kitap: not, DERS dosyaları, eski `.sql` dosyaları, internet **yok**. IntelliJ + psql açık, **AI kapalı**.
- Sorgularını psql'de çalıştırıp sonucuna bakmak **serbest** (SQL'de "derleyici" o).
- D bölümünde sadece `ExpenseRepository.java` açık olur — aynı dosyadaki kendi eski metotların serbest.
- Başlangıç/bitiş saatini `SINAV.md`'ye yaz. 150 dk dolunca bırak — yarım cevap da teslim edilir.
- Test yazmana gerek yok. D bölümünü ben test veritabanında, kendi testlerimle çalıştırıyorum.

---

## 0. Kurulum (10 dk) — sınav süresine dahil değil

```powershell
psql -U postgres -h localhost -c "CREATE DATABASE w6exam"
cd "C:\dev\roadmap backend\hafta-06\gun-06"
psql -U postgres -h localhost -d w6exam -f schema.sql -f seed.sql
```
Kontrol: `psql -U postgres -h localhost -d w6exam -c "SELECT COUNT(*) FROM order_items"` → `15`.

`schema.sql` ve `seed.sql` hazır, **değiştirme**. Önce `schema.sql`'i oku: 4 tablo, `orders.customer_id` NULL olabilir (misafir sipariş).

---

## 1. Tekrar (15 dk) — sınavdan ÖNCE, notlara bakmak serbest

1. Hafta 6 kağıt notlarını oku. Özellikle **4 kez kaçan konu**: `NOT IN` + alt sorguda `NULL`. Notundan tekrar bak.
2. Tek bakış:
   - Ay filtresi yarı açık aralık: `>= '2026-10-01' AND < '2026-11-01'`.
   - `ORDER BY` yazmazsan satır sırası garanti değil.
   - JDBC tipleri: `DATE` ↔ `setObject` / `getObject(kolon, LocalDate.class)`, `NUMERIC` ↔ `setDouble` / `getDouble`, enum → `name()` / `valueOf`.
3. Sorun varsa **şimdi** sor. Sınav başladıktan sonra soru yok.

---

## 🧪 SINAV (150 dk)

| Bölüm             | Nerede                       | Puan | Önerilen süre |
| ----------------- | ---------------------------- | ---- | ------------- |
| A — Teori         | `SINAV.md`                   | 20   | 20 dk         |
| B — Çıktı tahmini | `SINAV.md`                   | 25   | 30 dk         |
| C — SQL sorguları | `exam.sql`                   | 35   | 60 dk         |
| D — JDBC          | `ExpenseRepository.java`     | 20   | 40 dk         |

### Konu bazında puan (notu ben böyle vereceğim)

| Konu                                  | Sorular                    | Puan |
| ------------------------------------- | -------------------------- | ---- |
| Şema, kısıt, normalizasyon, index     | A1, A4, A5                 | 7    |
| SELECT / WHERE / NULL                 | Q1, Q2                     | 4    |
| GROUP BY / HAVING / aggregate         | A2, B2, Q3, Q7             | 15   |
| JOIN                                  | A3, B1, Q4, Q5             | 14   |
| Subquery / EXISTS / CTE               | B3, Q6, Q8, Q9, Q10        | 22   |
| Transaction                           | A6, B4                     | 8    |
| JDBC                                  | A7, A8, B5, D1–D4          | 30   |
| **Toplam**                            |                            | 100  |

---

### Bölüm A + B → `SINAV.md`

Sorular orada hazır.

---

### Bölüm C — SQL sorguları (35p) → `exam.sql`

Veritabanı `w6exam`. Her sorgu `exam.sql`'de kendi `-- Qn` başlığının altına.
Kolon adları önemli değil, **satırlar ve sıra** önemli. Para değerlerini yuvarlamana gerek yok (`ROUND` sadece Q3'te istenen yerde).

**"Satış" = sadece `status = 'PAID'` olan siparişler.** `CANCELLED` siparişler satış sayılmaz.
Bir kalemin tutarı = `products.price * order_items.quantity`.

| #   | Puan | İstenen                                                                                                                                                            | Sıra                                    |
| --- | ---- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------ | --------------------------------------- |
| Q1  | 2    | Şehri `Istanbul` **veya** `Ankara` olan müşteriler: `name`, `city`                                                                                                 | name A→Z                                |
| Q2  | 2    | Şehri **bilinmeyen** müşterilerin adı                                                                                                                              | —                                       |
| Q3  | 3    | Kategori başına: `category`, ürün sayısı, ortalama fiyat (**2 ondalık**)                                                                                           | ürün sayısı çok→az, sonra category A→Z  |
| Q4  | 3    | **Ekim 2026**'daki siparişlerin her kalemi (iptal dahil): sipariş id, `ordered_on`, ürün adı, `quantity`, kalem tutarı                                             | sipariş id, sonra ürün adı A→Z          |
| Q5  | 4    | **Her** müşteri ve **satış** (PAID) sipariş sayısı. Hiç PAID siparişi olmayan müşteri de **0** ile görünsün                                                        | sayı çok→az, sonra name A→Z             |
| Q6  | 4    | **Hiç** siparişi olmayan müşteriler (iptal dahil hiçbir sipariş yok): `name`                                                                                       | name A→Z                                |
| Q7  | 4    | Ürün başına toplam **satılan** adet; sadece toplamı **≥ 3** olanlar: ürün adı, toplam adet                                                                         | adet çok→az, sonra ad A→Z               |
| Q8  | 4    | Her **satış** siparişinin toplam tutarı; sadece toplamı, tüm satış siparişlerinin **ortalama** toplamından büyük olanlar: sipariş id, toplam. **`WITH` kullan**    | toplam çok→az                           |
| Q9  | 4    | Kasım 2026'dan **önce** en az bir satış siparişi olan, ama **Kasım 2026'da** hiç satış siparişi **olmayan** müşteriler: `name`                                     | name A→Z                                |
| Q10 | 5    | **Her** ürün ve toplam satış tutarı (ciro). Hiç satılmamış ürün **0** ile görünsün (NULL değil)                                                                    | ciro çok→az, sonra ad A→Z               |

Kontrol için satır sayıları: Q1 **4** · Q2 **1** · Q3 **3** · Q4 **7** · Q5 **7** · Q6 **2** · Q7 **4** · Q8 **2** · Q9 **2** · Q10 **7**.
Satır sayın tutmuyorsa sorgunda bir tuzak var — bul.

---

### Bölüm D — JDBC (20p) → `projects/expense-tracker/.../ExpenseRepository.java`

Mevcut `ExpenseRepository` sınıfına **4 yeni public metot** ekle. Mevcut metotlara dokunma.
Hepsi `throws SQLException`. Bütün SQL'ler `PreparedStatement` + `?` ile. `System.out` yok.
Kontroller **SQL'den önce**, bu sırayla; hepsi `IllegalArgumentException`, mesajlar birebir.

Hatırlatma (`schema.sql`): `expenses(id, spent_on DATE, description TEXT, category TEXT, amount NUMERIC(10,2))`.

#### D1. `List<Expense> findByCategory(Category category)` (5p)

| Durum              | Ne olur                           |
| ------------------ | --------------------------------- |
| `category == null` | IAE `Category is null`            |
| eşleşen satır yok  | boş liste                         |
| yoksa              | o kategorideki bütün harcamalar   |

Sıra: `spent_on` eskiden yeniye, eşitlikte `id` küçükten büyüğe.

#### D2. `double totalBetween(LocalDate from, LocalDate to)` (6p)

`from` ile `to` arasındaki (**ikisi de dahil**) harcamaların toplam tutarı.

| Durum                         | Ne olur                |
| ----------------------------- | ---------------------- |
| `from` ya da `to` null        | IAE `Date is null`     |
| `from` tarihi `to`'dan sonra  | IAE `Invalid range`    |
| aralıkta hiç harcama yok      | `0.0` döner            |

Örnek: tabloda `2026-11-01 → 100`, `2026-11-15 → 50`, `2026-11-30 → 25`, `2026-12-01 → 10` varsa
`totalBetween(2026-11-15, 2026-11-30)` → `75.0` · `totalBetween(2026-11-16, 2026-11-29)` → `0.0`.

#### D3. `int deleteByDescription(String description)` (3p)

Açıklaması **birebir** eşit olan bütün satırları siler, **silinen satır sayısını** döndürür.

| Durum                            | Ne olur                       |
| -------------------------------- | ----------------------------- |
| `description` null ya da blank   | IAE `Description is blank`    |
| eşleşen satır yok                | `0` döner                     |

#### D4. `int replaceMonth(YearMonth month, List<Expense> expenses)` (6p)

O aya ait **bütün** satırları siler, sonra listedeki harcamaları ekler. **Tek transaction**:
eklemelerden biri patlarsa **hiçbir şey değişmez** (silinen eski satırlar geri gelir) ve exception çağırana iletilir.
Başarılıysa **eklenen** satır sayısını döndürür.

| Durum                 | Ne olur                    |
| --------------------- | -------------------------- |
| `month == null`       | IAE `Month is null`        |
| `expenses == null`    | IAE `Expenses is null`     |
| boş liste             | o ay silinir, `0` döner    |

Listedeki harcamaların gerçekten o aya ait olup olmadığını kontrol etmen **gerekmiyor**.

---

## ☕ Bitince

`SINAV.md`, `exam.sql`, `ExpenseRepository.java` kaydet, chat'e **"bitti"** yaz.
Ben: `exam.sql`'i `w6exam`'da çalıştırıp referansla karşılaştırıyorum, `ExpenseRepository`'yi test veritabanında
kendi testlerimle deniyorum, A + B'yi okuyorum → konu bazında not. Sonra yanlışları birlikte düzeltiyoruz.

---

## 🧩 Sınavdan sonra — LeetCode SQL (30 dk)

Dil: **PostgreSQL**. Submit = testin.

| #   | Problem                                                                                     | Konu                            |
| --- | ------------------------------------------------------------------------------------------- | ------------------------------- |
| L1  | [1075. Project Employees I](https://leetcode.com/problems/project-employees-i/)             | `JOIN` + `GROUP BY` + `ROUND`   |
| L2  | [1251. Average Selling Price](https://leetcode.com/problems/average-selling-price/)         | tarih aralığına JOIN + `LEFT`   |

Not: PostgreSQL'de `ROUND(x, 2)` sadece `NUMERIC` alır → gerekirse `::numeric`.

---

## 🔧 GIT (10 dk)

```bash
git add hafta-06/gun-06 projects/expense-tracker/src
git status            # sadece .md, .sql, .java
git commit -m "feat: day 37 - week 6 exam"
git push
```

Not verildikten sonra: `ILERLEME.md` → Gün 37 logu + **Haftalık Kontrol tablosunda Hafta 6 satırı** → `docs: day 37 progress log`.
(ILERLEME satırlarını ben hazırlarım.)

---

## ✅ Gün Sonu

- [ ] Kurulum (`w6exam`, 15 satır kontrolü)
- [ ] Tekrar 15 dk
- [ ] Sınav: `SINAV.md` A + B, `exam.sql` Q1–Q10, `ExpenseRepository` D1–D4
- [ ] Not → yanlışları düzelt
- [ ] LeetCode 1075 + 1251
- [ ] Commit + push

Sayaç: 74 + 10 (C) + 2 (LeetCode) = **86** SQL sorusu.
**Yarın (Pazar 22 Kasım): izin.** Pazartesi: **Hafta 7** — HTTP, REST, JUnit 5.
