# Hafta 7 · Gün 6 (Gün 43) — HAFTA 7 SINAVI (HTTP/REST + JUnit + SOLID)

**Hedef:** Hafta 7'nin tamamını (HTTP → REST → JUnit → SOLID/pattern → Postman/coverage) tek sınavla ölçmek.
Bu hafta ilk kez **kapalı kitap, sıfırdan kod** bölümü var (C): boş klasöre küçük bir proje yazıyorsun.

**Süre:** Tekrar 15 dk · Sınav 140 dk · Mola · Not + düzeltme · LeetCode

**Kurallar:**
- Kapalı kitap: not, DERS dosyaları, eski proje kodu (`todo-server`, `expense-tracker`), internet **yok**. **AI kapalı**.
- IntelliJ'de derlemek, `mvnw test` çalıştırmak **serbest** (derleyici senin yardımcın).
- Başlangıç/bitiş saatini `SINAV.md`'ye yaz. 140 dk dolunca bırak — yarım cevap da teslim edilir.
- `main` yazmana gerek yok. C bölümünü ben kendi testlerimle çalıştırıyorum.

---

## 0. Kurulum (5 dk) — sınav süresine dahil değil

Sınav projesi hazır: `hafta-07/gun-06/exam` (içinde `pom.xml`, JUnit ekli, klasörler boş).

1. IntelliJ → sağdaki **Maven** paneli → **+** → `hafta-07/gun-06/exam/pom.xml` seç.
2. Kontrol: `src/main/java/com/eberkayk/notes` ve `src/test/java/com/eberkayk/notes` klasörleri görünüyor mu?

---

## 1. Tekrar (15 dk) — sınavdan ÖNCE, notlara bakmak serbest

1. Hafta 7 kağıt notlarını oku.
2. **5 kez kaçan konu** — exception'ı sararken eskisini **cause** olarak ver:
   ```java
   catch (NumberFormatException e) {
       throw new IllegalArgumentException("mesaj", e);   // ✔ e = 2. argüman
   }
   //  "mesaj" + e.getMessage()  → ✘ eski exception kaybolur
   ```
3. Tek bakış:
   - `assertEquals(beklenen, gerçek)` — sıra önemli.
   - Boş `ArrayDeque`: `peek()` → `null`, `pop()` → exception.
   - `int / int` → `int` (ondalık atılır).
4. Sorun varsa **şimdi** sor. Sınav başladıktan sonra soru yok.

---

## 🧪 SINAV (140 dk)

| Bölüm             | Nerede          | Puan | Önerilen süre |
| ----------------- | --------------- | ---- | ------------- |
| A — Teori         | `SINAV.md`      | 25   | 25 dk         |
| B — Çıktı tahmini | `SINAV.md`      | 25   | 25 dk         |
| C — Sıfırdan kod  | `exam/` projesi | 50   | 90 dk         |

### Konu bazında puan

| Konu                              | Sorular        | Puan |
| --------------------------------- | -------------- | ---- |
| HTTP / REST / status kodları      | A1–A4, B1      | 18   |
| JUnit / coverage                  | A5, A6, B2, C5 | 21   |
| SOLID / pattern / interface       | A7, A8, C2     | 11   |
| Exception (cause dahil)           | B3, C4b, C4c   | 15   |
| Java temelleri (int bölme, deque) | B4, B5         | 10   |
| Record / Map / sıralama           | C1, C3, C4a    | 25   |
| **Toplam**                        |                | 100  |

---

### Bölüm A + B → `SINAV.md`

Sorular orada hazır.

---

### Bölüm C — Sıfırdan kod (50p) → `exam/src/...`

Küçük bir **not uygulaması**. Paket: `com.eberkayk.notes`. 5 sınıf + 1 test sınıfı.
Her sınıfın alanları, constructor'ı ve metotları **kendi başlığının altında**. Yazmadan önce koşul listesini oku,
bitince her koşulu ✔ diye işaretle (spec ritüeli).

---

#### C1. `Note` (3p) — `src/main/java/com/eberkayk/notes/Note.java`

`record`. Alanlar, bu sırayla:

| Alan       | Tip      |
| ---------- | -------- |
| `id`       | `int`    |
| `title`    | `String` |
| `priority` | `int`    |

Doğrulama **yok** (doğrulama C3'te).

---

#### C2. `NoteRepository` (5p) — `interface`

4 metot imzası:

```
Note           add(String title, int priority)
Optional<Note> findById(int id)
List<Note>     findAll()
boolean        delete(int id)
```

---

#### C3. `InMemoryNoteRepository implements NoteRepository` (14p)

**Alanlar:** bu senin tasarım kararın (bir `Map` + bir sayaç önerilir).
**Constructor:** parametresiz.

**`add(title, priority)`** — kontroller bu sırayla, hepsi `IllegalArgumentException`, mesajlar birebir:

| Koşul                                    | Ne olur                            |
| ---------------------------------------- | ---------------------------------- |
| `title` null ya da blank                 | IAE `Title is blank`               |
| `priority` 1'den küçük ya da 5'ten büyük | IAE `Invalid priority`             |
| yoksa                                    | yeni `Note` oluştur, sakla, döndür |

- İlk eklenen notun `id`'si **1**, sonraki **2**, ...
- Hatalı `add` id **harcamaz** (ör. `add("a",1)` → id 1, `add("",1)` → hata, `add("b",1)` → id **2**).
- Silinen id **tekrar kullanılmaz** (id 1 silindikten sonra eklenen not yine yeni sayıyı alır).

**`findById(id)`** — varsa dolu `Optional`, yoksa boş `Optional`.

**`findAll()`** — bütün notlar, **id küçükten büyüğe**. Dönen listeyi çağıran değiştirirse (ör. `clear()`) depodaki notlar **etkilenmemeli**.

**`delete(id)`** — silindiyse `true`, öyle bir id yoksa `false`.

---

#### C4. `NoteService` (18p)

**Alan:** `NoteRepository repo` (`private final`).
**Constructor:** `NoteService(NoteRepository repo)` — repo **dışarıdan** gelir, içeride `new` **yok**.

##### C4a. `List<Note> topPriority(int n)` (8p)

En önemli `n` not.

| Koşul            | Ne olur                 |
| ---------------- | ----------------------- |
| `n < 0`          | IAE `Invalid n`         |
| `n == 0`         | boş liste               |
| `n` > not sayısı | bütün notlar (hata yok) |

Sıra: `priority` **büyükten küçüğe**, eşitlikte `id` **küçükten büyüğe**.
Bu metot depodaki sırayı **bozmamalı** (sonradan `findAll()` yine id sırasıyla dönmeli).

Örnek: notlar `1:p2, 2:p5, 3:p2, 4:p4` → `topPriority(2)` → id **2, 4** · `topPriority(10)` → id **2, 4, 1, 3**.

##### C4b. `Note require(int id)` (5p)

| Koşul     | Ne olur                                                      |
| --------- | ------------------------------------------------------------ |
| not varsa | o notu döndür                                                |
| yoksa     | `NoteNotFoundException`, mesaj: `Note not found: 7` (7 = id) |

`NoteNotFoundException` → **C4b için ayrı dosyada** kendi exception sınıfın:
- `RuntimeException`'dan türer.
- Constructor: `NoteNotFoundException(int id)` → mesajı yukarıdaki gibi kurar.

##### C4c. `static int parsePriority(String text)` (5p)

| Koşul                                | Ne olur                                                                            |
| ------------------------------------ | ---------------------------------------------------------------------------------- |
| `text` sayıya çevrilebiliyorsa       | o sayıyı döndür (`"3"` → `3`, `"-5"` → `-5`; aralık kontrolü **yok**)              |
| çevrilemiyorsa (`"3a"`, `null`, ...) | IAE, mesaj `Bad priority: 3a` **ve** yakalanan exception **cause** olarak eklenmiş |

---

#### C5. `NoteServiceTest` (10p) — `src/test/java/com/eberkayk/notes/NoteServiceTest.java`

**Sadece 2 test.** Her testten önce yeni bir `InMemoryNoteRepository` + `NoteService` kurulsun.

| Test    | Ne kontrol eder                                                               |
| ------- | ----------------------------------------------------------------------------- |
| T1 (5p) | C4a'daki örnek: 4 not ekle, `topPriority(2)` → ilk notun id'si 2, ikincinin 4 |
| T2 (5p) | `require(99)` (hiç not yokken) → `NoteNotFoundException` fırlatır             |

`mvnw test` → 2 yeşil.

---

## ☕ Bitince

`SINAV.md` + `exam/` kaydet, chat'e **"bitti"** yaz.
Ben: C'yi kendi testlerimle çalıştırıyorum, senin 2 testini çalıştırıyorum, A + B'yi okuyorum → konu bazında not.
Sonra yanlışları birlikte düzeltiyoruz.

---

## 🧩 Sınavdan sonra — LeetCode (30 dk)

| #   | Problem                                                                                                             | Konu                                       |
| --- | ------------------------------------------------------------------------------------------------------------------- | ------------------------------------------ |
| L1  | [232. Implement Queue using Stacks](https://leetcode.com/problems/implement-queue-using-stacks/)                    | iki stack ile kuyruk                       |
| L2  | [1327. List the Products Ordered in a Period](https://leetcode.com/problems/list-the-products-ordered-in-a-period/) | JOIN + tarih aralığı + HAVING (PostgreSQL) |

---

## 🔧 GIT (10 dk)

```bash
git add hafta-07/gun-06
git status            # sadece .md, .java, pom.xml, mvnw
git commit -m "feat: day 43 - week 7 exam"
git push
```

Not verildikten sonra: `ILERLEME.md` → Gün 43 logu + **Haftalık Kontrol tablosunda Hafta 7 satırı** → `docs: day 43 progress log`.
(ILERLEME satırlarını ben hazırlarım.)

---

## ✅ Gün Sonu

- [ ] Kurulum (Maven panelinde `exam` görünüyor)
- [ ] Tekrar 15 dk
- [ ] Sınav: `SINAV.md` A + B, `exam/` C1–C5
- [ ] Not → yanlışları düzelt
- [ ] LeetCode 232 + SQL 1327
- [ ] Commit + push

Sayaç: 91 + 1 (L2) = **92** SQL sorusu.
**Sonraki:** Hafta 8 — Spring Boot'a giriş (izin günün araya girebilir, sen söyle).
