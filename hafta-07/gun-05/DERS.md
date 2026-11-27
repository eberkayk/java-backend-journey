# Hafta 7 · Gün 5 (Gün 42) — Postman + Proje 0'a unit testler

**Hedef:** İki şey.
1. **Postman:** API'ye istek atmanın "elle test" aracı. Her backend işinde her gün açık durur.
2. **Proje 0 (expense-tracker) testleri:** PLAN'daki Hafta 7 teslimi → "unit testler, %70+ coverage".
   Bugün "coverage" ne demek, nasıl ölçülür, onu da göreceksin.

**Kurallar:**
- Ders chat'te adım adım ilerler. Bu dosya **referans**.
- Takılırsan "1 ipucu ver" de.
- Yarın (Gün 43) Hafta 7 sınavı.

---

## 0. Isınma — cevapları chat'e yaz

**S1.** (Exception, 4. kez geliyor) `catch` içine **tek satır** yaz: `IllegalStateException` fırlat,
mesajı `"DB hatası"` olsun, **asıl sebep (`e`) kaybolmasın**.
```java
try {
    repo.save(expense);
} catch (SQLException e) {
    // ← buraya
}
```

**S2.** (JDBC) Tabloda `description = 'Kahve'` olan **2 satır** var. `n` kaç olur?
```java
PreparedStatement ps = conn.prepareStatement("DELETE FROM expenses WHERE description = ?");
ps.setString(1, "Kahve");
int n = ps.executeUpdate();
```

**S3.** (Java) Ne yazdırır?
```java
int a = 7, b = 2;
double r = a / b;
System.out.println(r);
```

**S4.** (Java, LC 844) İki satır ayrı ayrı çalışsa, her biri ne olur?
```java
Deque<Character> st = new ArrayDeque<>();   // boş
st.peek();   // (a) ?
st.pop();    // (b) ?
```

---

## 1. Ders — Postman

### 1.1 Problem: tarayıcı sadece GET atar

Tarayıcının adres çubuğuna `localhost:8080/todos` yazınca → **GET** isteği gider, JSON'u görürsün.
Peki **POST** (yeni todo) veya **DELETE** nasıl atacaksın? Adres çubuğunda method seçemezsin, body yazamazsın.

Şimdiye kadar bunun için `curl` kullandın. Çalışıyor ama:
- Uzun komut, her seferinde baştan yaz.
- Git Bash'te tırnak / `;` derdi (Gün 41'de yaşadın).
- Cevabın status'unu, header'larını görmek için ek flag lazım.

**Postman** = "her şeyi elle seçebildiğin bir tarayıcı". Ekranda 4 şey seçersin:
**method** (GET/POST/...), **URL**, **header'lar**, **body** → **Send**.
Altta cevap gelir: **status** (200, 404...), **süre**, **header'lar**, **body** (JSON, renkli).

Benzetme: curl = telefonla sipariş vermek (her şeyi ağzınla söylersin).
Postman = sipariş formu (kutucuklar hazır, doldur, gönder, form da saklanır).

### 1.2 Collection — istekleri kaydetmek

Her gün aynı 6 isteği atacaksın. Postman'de bir isteği **kaydedersin**, kayıtlı istekler bir
klasörde durur: buna **Collection** denir. Ekipte bu klasör paylaşılır → yeni gelen geliştirici
"API'yi nasıl çağırıyorum?" diye sormaz, collection'ı açar.

### 1.3 Variable — `{{baseUrl}}`

Her istekte `http://localhost:8080` yazıyorsun. Yarın sunucu başka adrese taşınınca 20 isteği tek tek
mi düzelteceksin? Hayır: bir **değişken** tanımlarsın, `baseUrl = http://localhost:8080`, isteklerde
`{{baseUrl}}/todos` yazarsın. Adres değişince tek yeri değiştirirsin. (Tanıdık mı? Gün 41'deki
"değişiklik tek yerde olsun" fikri.)

### Kağıda Yaz
```
Postman = method + URL + header + body seç → Send → status/header/body gör
Tarayıcı adres çubuğu = sadece GET
Collection = kayıtlı istekler klasörü (ekiple paylaşılır)
{{baseUrl}} = değişken, adres değişince tek yerden düzelt
POST body: Body → raw → JSON  (Content-Type: application/json otomatik eklenir)
```

---

## 2. Görev A — Postman ile todo-server (chat'te, tek tek)

Önce: **todo-server'ı çalıştır** (IntelliJ'de `TodoServerApp` → Run). Sunucu açılışta 3 todo ekliyor (id 1, 2, 3).

**A1.** Postman'i kur: postman.com/downloads → Windows. Hesap isterse ücretsiz hesap aç (veya "skip" varsa geç).
Yeni istek: `GET http://localhost:8080/todos` → Send.
`A1: status ___ , kaç todo geldi ___`

**A2.** Aynı istekte **Params** sekmesi → key `userId`, value `1`. URL'nin kendiliğinden değiştiğini gör.
`A2: URL ne oldu ___ , kaç todo ___`

**A3.** `POST http://localhost:8080/todos` → **Body → raw → JSON**:
```json
{"title": "Postman ile", "userId": 1}
```
`A3: status ___ , gelen id ___ , Headers sekmesinde Location ___`

**A4.** Aynı POST, `"title": ""` yap → Send.
`A4: status ___ , body ___`

**A5.** `DELETE http://localhost:8080/todos/{A3'teki id}` → Send. Sonra **aynısını tekrar** Send.
`A5: 1. seferde status ___ , 2. seferde status ___`

**A6.** Collection oluştur: adı `todo-server`. A1–A5 isteklerini içine kaydet (Ctrl+S).
Collection'a variable ekle: `baseUrl = http://localhost:8080`. İsteklerdeki adresi `{{baseUrl}}` ile değiştir.
`A6: {{baseUrl}}/todos ile Send → status ___`

---

## 3. Ders — Coverage + 2 yeni JUnit aracı

### 3.1 Coverage nedir?

Testleri çalıştırırken bir araç (**JaCoCo**) kodunun **hangi satırlarının en az bir kez çalıştığını** sayar.
- Test hiç çalıştırmadıysa satır **kırmızı** → o satır hiç test edilmedi.
- Çalıştıysa **yeşil**. `if`'in sadece bir kolu çalıştıysa **sarı**.
- **Coverage %** = çalışan / toplam.

Neden önemli? "Testim var" demek yetmez; `top(n)` metodunu hiç çağırmadıysan, orada bug olsa da testler yeşil kalır.
Coverage raporu sana **"şuraya hiç bakmadın"** der.

Dikkat: %100 coverage = bug yok **demek değil**. Satır çalıştı ama `assert` yazmadıysan hiçbir şey kontrol etmedin.
Coverage "neyi test etmediğini" gösterir, "testlerin iyi mi" sorusunu değil.

`pom.xml`'e JaCoCo'yu ben ekledim. `./mvnw test` → rapor: `target/site/jacoco/index.html` (tarayıcıda aç).
`ExpenseRepository` (DB ister → Hafta 10'da Testcontainers) ve `ExpenseCli` (main) hesaba katılmıyor.
**Hedef: Total ≥ %70.**

### 3.2 `double` karşılaştırma — 3. parametre

```java
assertEquals(0.3, 0.1 + 0.2);          // KIRMIZI! 0.1+0.2 = 0.30000000000000004
assertEquals(0.3, 0.1 + 0.2, 0.001);   // yeşil: "aradaki fark 0.001'den küçükse eşit say"
```
`double` bilgisayarda tam saklanamaz (küçük yuvarlama hataları). `total()` gibi `double` dönen her şeyde
3. parametreyi (**delta**) ver.

### 3.3 `@TempDir` — test için geçici klasör

`saveTo(path)` dosyaya yazıyor. Testte gerçek `expenses.csv`'ni ezmek istemezsin.
```java
@Test
void saveAndLoad(@TempDir Path dir) throws IOException {
    Path file = dir.resolve("test.csv");   // dir = JUnit'in açtığı boş klasör
    ...
}
```
JUnit test başlamadan boş bir klasör açar, parametre olarak verir, test bitince **siler**.

### Kağıda Yaz
```
Coverage = testler kodun yüzde kaçını ÇALIŞTIRDI (kırmızı = hiç çalışmadı)
%100 coverage ≠ bug yok (assert yoksa hiçbir şey kontrol edilmedi)
double → assertEquals(beklenen, gerçek, 0.001)
@TempDir Path dir → geçici klasör, test bitince silinir
Rapor: ./mvnw test → target/site/jacoco/index.html
```

---

## 4. Görev B — expense-tracker testleri

Klasör hazır: `projects/expense-tracker/src/test/java/com/eberkayk/expense/`
IntelliJ: Maven panelinde expense-tracker → **Reload** (yeni bağımlılık için).

### B1. `ExpenseTest` — record'un kontrolleri
- `amount = 0` olan `Expense` → `IllegalArgumentException` fırlatmalı.
- Açıklamasında virgül olan (`"Su, ekmek"`) → `IllegalArgumentException`.

### B2. `ExpenseTest` — CSV gidiş-dönüş
Bir `Expense` oluştur → `toCsv()` → çıkan string'i `fromCsv()`'ye ver → ilk `Expense`'e **eşit** olmalı.
(Record → `equals` otomatik, Hafta 4.)

### B3–B6. `ExpenseTrackerTest` — `@BeforeEach`'te şu 3 harcamayı ekle:

```
2026-11-01  Market  FOOD       300
2026-11-15  Kira    HOME       5000
2026-12-02  Otobus  TRANSPORT  40
```

- **B3.** `total(e -> e.category() == Category.FOOD)` → `300`. `total(e -> true)` → `5340`. (delta!)
- **B4.** `top(2)` → 1. eleman `Kira`, 2. eleman `Market`. `top(0)` → exception.
- **B5.** `between(2026-11-01, 2026-11-15)` → kaç eleman? (Sınır günleri dahil mi? Koda bak, karar ver.)
- **B6.** `removeWhere(e -> e.amount() < 100)` → kaç döner? Sonra `getAll()` kaç eleman?

### B7. `ExpenseTrackerTest` — dosyaya kaydet / geri oku
`@TempDir` ile: `saveTo(file)` → `ExpenseTracker.loadFrom(file)` → yüklenenin `getAll()`'u
orijinalin `getAll()`'una eşit olmalı.

### B8. Coverage'ı oku
`./mvnw test` → `target/site/jacoco/index.html` aç → `com.eberkayk.expense` → `ExpenseTracker`.
`B8: Total % ___ , kırmızı kalan metodlardan 3 tanesi ___`
%70'in altındaysa: kırmızı metodlardan **seç**, her birine kısa bir test (genelde 1 assert yeter) → %70'i geç.

---

## 5. Görev C — LeetCode

**C1. LeetCode 933 — Number of Recent Calls** (Easy, Java)
`ping(t)` her çağrıldığında: son 3000 ms içindeki (`[t-3000, t]`) ping sayısını döndür. `t` hep artar.
Yeni yapı: **Queue** (kuyruk). Stack'te "en son giren önce çıkar"dı; kuyrukta **"ilk giren ilk çıkar"** —
market kasası sırası. Eskiyen ping'ler hep **en baştakiler**. `Queue<Integer> q = new ArrayDeque<>();`
metodlar: `offer` (sona ekle), `peek` (baştakine bak), `poll` (baştakini çıkar), `size`.

**C2. LeetCode 1193 — Monthly Transactions I** (Medium, SQL)
Her **ay + ülke** için: toplam işlem sayısı, onaylı işlem sayısı, toplam tutar, onaylı tutar.
Gün 40'taki `SUM(CASE WHEN ...)` + tarihten ay çıkarma: `TO_CHAR(trans_date, 'YYYY-MM')`.
(Senin `totalsByMonth()`'un SQL versiyonu.)

---

Bitince: "bitti" de. `./mvnw test` + coverage'ı ben de çalıştırıp söyleyeceğim.
