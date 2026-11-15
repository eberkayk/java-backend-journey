# Hafta 5 · Gün 7 (Gün 31) — KAPI SINAVI (Hafta 1–4)

**Hedef:** Hafta 1–4'ün tamamını tek sınavla ölçmek. **≥ 80 → yarın SQL.** < 80 → Hafta 5b (sadece %80 altı kalan konular, 1 hafta, sonra ikinci kapı sınavı).

**Süre:** ~3.5 saat · Tekrar 20 dk · Sınav 150 dk · Mola · Not + düzeltme · Kapanış 15 dk

**Kurallar:**
- Kapalı kitap: not, DERS dosyaları, eski kod, internet **yok**. IntelliJ açık, **AI kapalı**.
- Başlangıç/bitiş saatini `SINAV.md`'ye yaz. 150 dk dolunca bırak — yarım kod da teslim edilir.
- Sınıf, metot, mesajlar **İngilizce**. Metot adı/imzası ve mesajlar **birebir** buradaki gibi.
- **`main` yazmana gerek yok.** Testleri ben yazıp çalıştırıyorum. Kendin denemek istersen serbest, ama zorunlu değil.

**Kurulum:** `hafta-05/gun-07/w5d7/` (package **`w5d7`**). `gun-07`'yi Sources Root yap.

---

## 1. Tekrar (20 dk) — sınavdan ÖNCE, notlara bakmak serbest

1. Kağıt notlarını oku — özellikle dünkü "takıldığın 3 şey".
2. Son 3 günde kaçanlar (tek bakış):
   - `Optional` yazdırılırsa kutu görünür: `Optional[5]`. `map` açmaz, `orElse` açar.
   - Terminal işlem (`toList`, `count`, `forEach`...) yoksa stream **hiç çalışmaz** — hata da vermez.
   - Koleksiyon metotlarının **dönüş değeri**: `add` → boolean, `remove(int)` → silinen eleman, `put` → eski değer ya da `null`.
   - Eşitlikte "alfabetik ilk": karşılaştırmada `>` (büyük-eşit değil) + alfabetik sırayla dönen bir yapı (`TreeMap`) ya da `Comparator` zinciri.
3. Sorun varsa **şimdi** sor. Sınav başladıktan sonra soru yok.

---

## 🧪 KAPI SINAVI (150 dk)

| Bölüm             | Nerede         | Puan | Önerilen süre |
| ----------------- | -------------- | ---- | ------------- |
| A — Teori         | `SINAV.md`     | 20   | 20 dk         |
| B — Çıktı tahmini | `SINAV.md`     | 30   | 30 dk         |
| C — Kod           | package `w5d7` | 30   | 60 dk         |
| D — Algoritma     | package `w5d7` | 20   | 40 dk         |

### Konu bazında puan (notu ben böyle vereceğim)

| Konu                                            | Sorular        | Puan |
| ----------------------------------------------- | -------------- | ---- |
| Temeller (tipler, operatör, dizi, String)       | A1, A2, B1     | 9    |
| OOP                                             | A3, A4, B2     | 11   |
| Exceptions                                      | A5, B3, C2     | 11   |
| Collections + Comparator + Iterator             | A6, A7, B4, B5 | 15   |
| Lambda + Stream + Optional                      | A8, B6         | 7    |
| Kod görevi (record/enum/collections/stream/I/O) | C1, C3         | 27   |
| Algoritma                                       | D1, D2         | 20   |
| **Toplam**                                      |                | 100  |

---

### Bölüm A + B → `SINAV.md`

Sorular orada hazır.

---

### Bölüm C — Kod: Kurs Kayıt Sistemi (30p)

Her public tip kendi dosyasında, package `w5d7`.

#### C1. `enum Level` + `record Course` (5p)

`Level.java` — sabitler, bu sırayla: `BEGINNER`, `INTERMEDIATE`, `ADVANCED`

`Course.java` — `record Course(String code, String title, Level level, int capacity)`

**Compact constructor**, kontroller bu sırayla (hepsi `IllegalArgumentException`):

| Durum                    | Mesaj                                    |
| ------------------------ | ---------------------------------------- |
| `code` null ya da blank  | `Code is blank`                          |
| `title` null ya da blank | `Title is blank`                         |
| `level == null`          | `Level is null`                          |
| `capacity <= 0`          | `Capacity must be positive: ` + capacity |

#### C2. `CourseFullException` (3p)

`CourseFullException.java` — **checked** exception.
- Constructor: `CourseFullException(String code, int capacity)`
- Mesaj: `Course full: CS101 (capacity 2)`
- Getter'lar: `getCode()`, `getCapacity()`

#### C3. `class School` (22p)

İçeride hangi koleksiyonları tutacağın **senin kararın**. `School` içinde `System.out` yok.

- **`void addCourse(Course c)`**
  - `c == null` → IAE `Course is null`
  - aynı `code` zaten varsa → `IllegalStateException` `Duplicate course: CS101`
- **`boolean enroll(String student, String code) throws CourseFullException`** — kontroller bu sırayla:
  1. `student` null/blank → IAE `Student is blank`
  2. kurs yok → `NoSuchElementException` `No course: XX1`
  3. öğrenci o kursa zaten kayıtlı → `false` döner (hata yok)
  4. kurs dolu → `CourseFullException`
  5. yoksa kaydet, `true` döner
- **`List<String> studentsOf(String code)`** → o kursun öğrencileri, **alfabetik**. Kurs yoksa ya da kimse yoksa boş liste. Dönen liste **değiştirilemez**.
- **`List<String> coursesOf(String student)`** → öğrencinin kayıtlı olduğu kurs kodları, **alfabetik**. Hiç yoksa boş liste. **Döngü yasak.**
- **`Optional<Course> mostPopular()`** → en çok öğrencisi olan kurs. Eşitlikte **kodu alfabetik ilk** olan. Hiçbir kursta öğrenci yoksa (ya da hiç kurs yoksa) `Optional.empty()`.
- **`Map<Level, Long> countByLevel()`** → seviye → o seviyedeki **kurs** sayısı (öğrenci değil). Key'ler **enum sırasıyla**. Kursu olmayan seviye map'te yok. **Döngü yasak.**
- **`int freeSeats()`** → tüm kurslardaki boş yer toplamı (`capacity - kayıtlı öğrenci`). **Döngü yasak.**
- **`void saveTo(Path file) throws IOException`** → her kayıt bir satır: `CS101;Ada`. Sıra: önce kurs kodu alfabetik, sonra öğrenci alfabetik. Dosya varsa **üzerine yazılır** (eklenmez).

**Örnek senaryo** (sadece anlamak için — `main`'e yazman gerekmiyor):
```
addCourse(CS101, "Java", BEGINNER, 2)
addCourse(DB201, "SQL", INTERMEDIATE, 3)
addCourse(AL301, "Algorithms", ADVANCED, 1)

enroll("Cem", "CS101")   → true
enroll("Ada", "CS101")   → true
enroll("Ada", "CS101")   → false
enroll("Bora", "CS101")  → CourseFullException: Course full: CS101 (capacity 2)
enroll("Ada", "DB201")   → true
enroll("Ada", "XX1")     → NoSuchElementException: No course: XX1

studentsOf("CS101")  → [Ada, Cem]
coursesOf("Ada")     → [CS101, DB201]
mostPopular()        → Optional[Course[code=CS101, title=Java, level=BEGINNER, capacity=2]]
countByLevel()       → {BEGINNER=1, INTERMEDIATE=1, ADVANCED=1}
freeSeats()          → 3        (CS101: 0, DB201: 2, AL301: 1)
saveTo(...)          → CS101;Ada
                       CS101;Cem
                       DB201;Ada
```

---

### Bölüm D — Algoritma (20p) → `Exam2.java`

İkisi de **static** metot, `Exam2` sınıfında. Kâğıtta önce örnekle elle çözmek serbest (önerilir). `main` zorunlu değil.

**D1 (8p) — `static int[] replaceElements(int[] arr)`**
Her elemanı, **sağındaki elemanların en büyüğü** ile değiştir. Son eleman `-1` olur. Yeni dizi döndürebilir ya da `arr`'ı değiştirip onu döndürebilirsin.
```
[17, 18, 5, 4, 6, 1]  → [18, 6, 6, 6, 1, -1]
[400]                 → [-1]
```
- Sınavdan sonra: **LeetCode 1299**.

**D2 (12p) — `static List<List<Integer>> findDifference(int[] nums1, int[] nums2)`**
İki liste döndür:
- `[0]` → `nums1`'de olup `nums2`'de **olmayan** farklı sayılar
- `[1]` → `nums2`'de olup `nums1`'de **olmayan** farklı sayılar

Her listenin içindeki sıra önemsiz; aynı sayı iki kez olmasın.
```
[1, 2, 3],    [2, 4, 6]      → [[1, 3], [4, 6]]
[1, 2, 3, 3], [1, 1, 2, 2]   → [[3], []]
```
- Sınavdan sonra: **LeetCode 2215**.

---

## ☕ Bitince

`SINAV.md` + kodları kaydet, chat'e **"bitti"** yaz. Ben testleri çalıştırıp konu bazında not veriyorum; sonra yanlışları birlikte düzeltiyoruz (önce LeetCode 1299 + 2215'i gönder).

---

## 🔧 GIT (10 dk)

```bash
git add hafta-05/gun-07
git status            # sadece .java ve .md
git commit -m "feat: day 31 - gate exam"
git push
```

Not verildikten sonra: `ILERLEME.md` → Gün 31 logu + **Haftalık Kontrol tablosunda Hafta 5 satırı** (kapı sınavı puanı) → `docs: day 31 progress log`.

---

## ✅ Gün Sonu

- [ ] Tekrar 20 dk
- [ ] Sınav: `SINAV.md` A + B, C (`Level`, `Course`, `CourseFullException`, `School`), D (`Exam2`)
- [ ] Not → yanlışları düzelt, LeetCode 1299 + 2215 gönder
- [ ] Commit + push, `ILERLEME.md` Gün 31 + Hafta 5 satırı

**Sıradaki:** ≥ 80 → Hafta 6, **SQL + PostgreSQL**. < 80 → Hafta 5b.
