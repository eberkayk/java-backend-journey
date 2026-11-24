# Hafta 7 · Gün 2 (Gün 39) — REST tasarımı + kendi küçük sunucun

**Hedef:** Dün **istemciydin** (soru soran taraf). Bugün **sunucu** tarafına geçiyorsun (cevap veren taraf).
Bir API'nin URL'lerini, metodlarını ve status kodlarını **neden öyle** seçtiğimizi anlamak, sonra bunu
Java'nın kendi içindeki küçük sunucuyla yazmak. Hafta 8'de Spring aynı işi senin yerine yapacak;
bugün perdenin arkasını görüyorsun.

**Kurallar:**
- Ders chat'te adım adım ilerler. Bu dosya **referans**: unutursan buraya bakarsın.
- Kodu sen yazarsın; testleri **ben** yazar, senin sunucuna karşı çalıştırırım. Bitince "bitti" de.
- Spec ritüeli: Görev B'deki koşul listelerini koda başlamadan oku, bitince her birini ✔ diye işaretle.
- Takılırsan "1 ipucu ver" de.

---

## 0. Isınma — cevapları chat'e yaz

**S1.** (Java) Veritabanı bağlantısı koptu, `executeQuery` `SQLException` fırlattı:
```java
int countExpenses() {
    try {
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1);
    } catch (SQLException e) {
        System.out.println("DB error: " + e.getMessage());
        return 0;
    }
}

// çağıran kod:
if (countExpenses() == 0) {
    System.out.println("Hiç harcamanız yok!");
}
```
a) Ekranda hangi satır(lar) yazar?
b) Kullanıcı açısından bu neden yanlış?
c) `catch`'in içine `return 0` yerine ne yazarsan çağıran kod hatayı fark eder?

**S2.** (SQL) Veri:
```
departments        employees
 id | name          id | dept_id
----+-------       ----+---------
  1 | IT             1 |       1
  2 | HR             2 |       1
  3 | Legal          3 |       2
```
```sql
SELECT d.name, COUNT(*) AS a, COUNT(e.id) AS b
FROM departments d LEFT JOIN employees e ON e.dept_id = d.id
GROUP BY d.name;
```
IT, HR ve Legal için `a` ve `b` kaç?

**S3.** (HTTP, dün) Sunucu `404` ve body olarak `{}` döndürüyor:
```java
HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
System.out.println("A");
Todo t = mapper.readValue(res.body(), Todo.class);
System.out.println(t.title());
```
a) `send` exception fırlatır mı? `"A"` yazılır mı?
b) Son satır ne yazar?

**S4.** (JDBC) `expenses` tablosunda `category = 'FOOD'` olan 3 satır var:
```java
PreparedStatement ps = conn.prepareStatement(
        "UPDATE expenses SET amount = amount * 2 WHERE category = ?");
ps.setString(1, "FOOD");
int n = ps.executeUpdate();
```
a) `n` kaç?
b) `executeUpdate` yerine `executeQuery` yazsaydın ne olurdu?

---

## 1. Ders

### 1.1 Sunucu ne demek?

Dün `curl https://jsonplaceholder.typicode.com/todos/1` dediğinde, dünyanın bir yerinde bir program
**bekliyordu**. Senin isteğin geldi, okudu, cevabı hazırladı, geri yolladı, tekrar beklemeye geçti.
İşte o program **sunucu**.

Restoran benzetmesi: sen müşterisin (istemci), mutfak sunucu, garsonun aldığı sipariş fişi HTTP isteği.
Mutfak hiçbir zaman kendiliğinden yemek yollamaz; **hep bir sipariş gelmesini bekler**.

Bir sunucu bir **port** dinler. Port, aynı bilgisayardaki programları ayıran kapı numarasıdır.
Bugün yazacağın sunucu `localhost:8080`'i dinleyecek: "bu bilgisayar, 8080 numaralı kapı".

### 1.2 Kural olmasa ne olurdu? (REST'in çözdüğü problem)

Üç farklı ekibin yazdığı üç API düşün, hepsi "5 numaralı todo'yu sil" işini yapıyor:

```
Ekip 1:  GET  /deleteTodo?id=5            → 200, body: "silindi"
Ekip 2:  POST /todo/remove   body: {"id":5} → 200, body: {"ok": true}
Ekip 3:  POST /api           body: {"action":"delete","id":5} → 200, body: {"error":"yok"}
```

Her birini kullanmak için ayrı ayrı dokümantasyon okuman lazım. Daha kötüsü:
- Ekip 1'de silme işi `GET` ile yapılıyor. Tarayıcılar ve arama motorları `GET` linklerini
  "zararsız okuma" sanıp kendiliğinden açar → veriler kendi kendine silinir.
- Ekip 3 hata olduğunda bile `200` dönüyor. Dün yazdığın `TodoClient` gibi status koduna bakan
  bir istemci "her şey yolunda" sanır.

**REST** bu kaosa karşı bir **anlaşmadır** (kütüphane değil, kod değil, bir yazım kuralı):

1. **URL = NE** üzerinde çalışıyorsun (bir isim: todo'lar, kitaplar, üyeler)
2. **Metod = NE YAPIYORSUN** (GET oku, POST ekle, PUT/PATCH güncelle, DELETE sil)
3. **Status kodu = NASIL GİTTİ** (200, 201, 404, 400 ...)

Bu üçünü herkes aynı şekilde kullanınca, hiç görmediğin bir API'yi bile tahmin edebilirsin.

### 1.3 URL'ler: kaynak (resource) isimleri

Sunucudaki her "şey" bir **kaynaktır**: bir todo, bir kitap, bir üye.
URL o kaynağın **adresidir**, eylem değil. O yüzden URL'de **fiil olmaz, çoğul isim olur**.

```
/todos              → todo'ların tamamı (koleksiyon)
/todos/5            → id'si 5 olan TEK todo
/users/3/todos      → 3 numaralı kullanıcının todo'ları (iç içe ilişki)
/todos?userId=3     → koleksiyonu filtrele (? sonrası = query parametresi)
```

Fiil nereye gitti? **Metoda**. "Sil" demek için URL'ye `delete` yazmıyorsun, metodu `DELETE` yapıyorsun:

```
YANLIŞ:  GET /deleteTodo?id=5        DOĞRU:  DELETE /todos/5
YANLIŞ:  POST /createTodo            DOĞRU:  POST   /todos
YANLIŞ:  GET /getTodo/5              DOĞRU:  GET    /todos/5
```

### 1.4 Metodlar: aynı URL, farklı iş

Aynı `/todos/5` adresine farklı metodlarla gidersen farklı iş olur. Dün GET, POST, DELETE'i gördün.
Bugün iki yeni tane: **PUT** ve **PATCH**, ikisi de güncelleme.

- **PUT** = "bu kaydın **tamamını** şununla değiştir". Body'de bütün alanlar gelir.
- **PATCH** = "sadece **şu alanı** değiştir". Body'de sadece değişen alan gelir.

Örnek: 5 numaralı todo'yu "tamamlandı" yapmak istiyorsun.
- `PATCH /todos/5` body `{"completed": true}` → sadece completed değişir.
- `PUT /todos/5` body `{"completed": true}` → başlık da gönderilmediği için başlık **boşalır**. PUT'ta her şeyi göndermen gerekir.

Özet (yukarıdakileri okuduktan sonra bak):

| Metod  | `/todos` (koleksiyon)   | `/todos/5` (tek kayıt)        |
| ------ | ----------------------- | ----------------------------- |
| GET    | hepsini listele         | 5'i getir                     |
| POST   | yeni todo ekle          | (kullanılmaz)                 |
| PUT    | (kullanılmaz)           | 5'in tamamını değiştir        |
| PATCH  | (kullanılmaz)           | 5'in bazı alanlarını değiştir |
| DELETE | (genelde izin verilmez) | 5'i sil                       |

### 1.5 Status kodları: sonucu doğru söylemek

Dün status kodlarını **okudun**. Bugün sunucu sensin, **sen seçeceksin**. Kural basit:
istemci status koduna bakıp ne olduğunu body'yi okumadan anlayabilmeli.

Bir isteğin başına gelebilecekler, sırasıyla:

1. **İstemci saçma bir şey gönderdi** (id yerine `abc`, başlık boş, JSON bozuk) → **400 Bad Request**.
   "Hata sende, isteğini düzelt."
2. **İstek düzgün ama aranan kayıt yok** (`/todos/999`) → **404 Not Found**.
3. **Bu adreste bu metod yok** (`PUT /todos/5` ama sen PUT yazmadın) → **405 Method Not Allowed**.
4. **Her şey yolunda:**
   - okuma → **200 OK** + veri
   - ekleme → **201 Created** + oluşan kayıt + `Location: /todos/6` header'ı ("yeni kaydın adresi bu")
   - silme → **204 No Content** (body yok, söylenecek bir şey yok)
5. **Senin kodun patladı** (NullPointerException vs.) → **500 Internal Server Error**.
   Hata istemcide değil sende.

**Dikkat — en sık hata:** `GET /todos?userId=99` ve 99'un hiç todo'su yok. Bu **404 değil**, `200` + `[]`.
Koleksiyon var, sadece boş. 404 "bu adres/kayıt yok" demek; `/todos` adresi var.

Hata durumunda body'ye ne yazılır? Düz metin değil, istemcinin okuyabileceği JSON:
```json
{"error": "Todo not found: 999"}
```

### 1.6 Stateless: sunucu seni hatırlamaz

Her istek **kendi başına** eksiksiz olmalı. Sunucu "bu kullanıcı az önce şunu sormuştu" diye
hatırlamaz. Giriş yapmış olman bile her istekte header'la tekrar gönderilir (dün A7'de `-u enes:1234`
her istekte vardı). Bu sayede aynı uygulamadan 10 kopya çalıştırıp istekleri aralarında dağıtabilirsin.

### 1.7 Java'nın kendi küçük sunucusu: `HttpServer`

JDK'nın içinde basit bir sunucu var (`com.sun.net.httpserver`). Bir isteğin yolculuğu:

```
curl GET localhost:8080/todos/5
        │
        ▼
HttpServer (8080'i dinliyor)
        │  "/todos ile başlayan her şey TodoHandler'a"
        ▼
TodoHandler.handle(exchange)       ← exchange = bu tek istek + onun cevabı
        │  router: metod GET, yol 3 parça → handleGetOne(ex, "5")
        ▼
handleGetOne                        ← SENİN YAZACAĞIN KISIM
        │  "5" → 5, repo'da ara, bulduysan 200 + JSON, bulamadıysan 404
        ▼
cevap istemciye gider
```

`HttpExchange` (kısaca `ex`) içinden lazım olanlar — çoğunu hazır yardımcılar zaten yapıyor:

| Ne lazım              | Nasıl alınır                                          |
| --------------------- | ----------------------------------------------------- |
| metod                 | `ex.getRequestMethod()` → `"GET"`                     |
| yol                   | `ex.getRequestURI().getPath()` → `"/todos/5"`         |
| query parametresi     | yardımcı: `queryParam(ex, "userId")` → `"3"` / `null` |
| istek body'si (JSON)  | yardımcı: `readBody(ex, Tip.class)`                   |
| cevaba header eklemek | `ex.getResponseHeaders().set("Location", "...")`      |
| JSON cevap göndermek  | yardımcı: `sendJson(ex, 200, nesne)`                  |
| hata cevabı göndermek | yardımcı: `sendError(ex, 404, "mesaj")`               |
| 204 göndermek         | yardımcı: `sendNoContent(ex)`                         |

**Önemli kural:** Bir istek için **tek bir cevap** gönderebilirsin. `sendError(...)` dedikten sonra
metoddan çıkmazsan kod aşağı akar, ikinci bir `sendJson` dener ve patlar. Her hata durumundan sonra `return;`.

> **Kağıda Yaz**
> - REST: URL = ne (çoğul isim), metod = ne yapıyorsun, status = nasıl gitti
> - URL'de fiil yok: `DELETE /todos/5`, `GET /deleteTodo?id=5` değil
> - PUT = tamamını değiştir, PATCH = sadece gelen alanları değiştir
> - 400 istemcinin hatası, 404 kayıt yok, 405 metod yok, 500 benim hatam
> - POST → 201 + Location, DELETE → 204, boş liste → 200 `[]` (404 değil)
> - Bir istek = tek cevap; hata gönderdikten sonra `return;`

---

## 2. Görev A — REST tasarımı (chat'te, madde madde)

### A1. Bu endpoint'leri REST'e çevir

Bir kütüphane API'si. Her biri için doğru **metod + URL** yaz. Cevap formatı: `A1.1: DELETE /...`

1. `GET /getAllBooks`
2. `GET /deleteBook?id=7`
3. `POST /books/7/update` (body: kitabın yeni başlığı, sadece başlık değişiyor)
4. `GET /book/7`
5. `POST /createMember`
6. `GET /members/3/getLoans` (3 numaralı üyenin ödünç aldığı kitaplar)

### A2. Hangi status kodu?

Cevap formatı: `A2.1: 404`

1. `GET /books/99` — 99 numaralı kitap yok.
2. `POST /books` — body'de `title` boş string.
3. `POST /books` — kitap eklendi, id'si 12 oldu. (Kodu ve hangi header'ı ekleyeceğini yaz.)
4. `DELETE /books/7` — silindi.
5. `GET /books?author=Orwell` — Orwell'in hiç kitabı yok.
6. `DELETE /books` — sunucu "bütün kitapları sil" işlemini desteklemiyor.
7. `GET /books/5` — kitap var ama senin kodunda `NullPointerException` çıktı.

---

## 3. Görev B — `todo-server`

Proje: `projects/todo-server` (pom, `TodoServerApp` ve `TodoHandler` iskeleti hazır). IntelliJ'de **Open** ile aç.
Paket: `com.eberkayk.todoserver` → klasör: `src/main/java/com/eberkayk/todoserver/`

**Sıra önemli:** Proje, B1–B3'teki dosyalar yazılana kadar derlenmez (hazır dosyalar onları kullanıyor).

### B1. `record Todo` — `Todo.java`

Dünkü `Todo` ile birebir aynı alanlar, sadece paket farklı:
- `int userId`, `int id`, `String title`, `boolean completed`

### B2. `record CreateTodoRequest` — `CreateTodoRequest.java`

POST isteğinin body'sini okumak için. **Alanlar:**
- `String title`
- `int userId`

(İstemci `completed` de gönderse sorun yok, `TodoHandler`'daki mapper bilinmeyen alanları yok sayacak şekilde ayarlı.)

### B3. `class TodoRepository` — `TodoRepository.java`

Todo'ları bellekte tutan sınıf (veritabanı yerine). **Doğrulama yapmaz**, doğrulama `TodoHandler`'ın işi.

**Alanlar:**
- `private final Map<Integer, Todo> todos = new TreeMap<>();` → anahtar id. `TreeMap` anahtarları sıralı tutar, liste hep id sırasıyla gelir.
- `private int nextId = 1;`

**Constructor:** yok (varsayılan yeter).

**Metodlar (hepsi `public`):**

| İmza                                    | Ne yapar                                                                               |
| --------------------------------------- | -------------------------------------------------------------------------------------- |
| `List<Todo> findAll()`                  | bütün todo'ları id sırasıyla **yeni bir liste** olarak döndürür                        |
| `List<Todo> findByUser(int userId)`     | sadece o kullanıcının todo'ları; yoksa boş liste                                       |
| `Optional<Todo> findById(int id)`       | varsa dolu `Optional`, yoksa `Optional.empty()`                                        |
| `Todo create(String title, int userId)` | id = `nextId`, `completed = false`; map'e koyar, `nextId`'yi artırır, todo'yu döndürür |
| `boolean deleteById(int id)`            | silindiyse `true`, öyle bir id yoksa `false`                                           |

### B4. `TodoHandler` — 5 metodu doldur

Dosyada `// SENİN KISMIN` bölümündeki 5 metodun içi `throw new UnsupportedOperationException("TODO")`.
O satırı silip yerine kodunu yaz. Router, yardımcılar ve diğer her şey hazır; onlara dokunma.

---

#### Metod 1: `int parsePositiveInt(String text)`

Diğer metodlar bunu kullanacak, o yüzden ilk bunu yaz.

- [ ] 1. `text` pozitif bir tam sayıysa (`"5"`) → o sayıyı döndür
- [ ] 2. `null`, `""`, `"abc"`, `"0"`, `"-3"` → `-1` döndür
- [ ] 3. exception **fırlatmaz** (ipucu: `Integer.parseInt(null)` ve `Integer.parseInt("abc")` ikisi de `NumberFormatException` fırlatır)

---

#### Metod 2: `void handleList(HttpExchange ex)` — `GET /todos` ve `GET /todos?userId=3`

- [ ] 1. `userId` parametresi **yok** → `200`, bütün todo'lar
- [ ] 2. `userId` var ama pozitif tam sayı değil (`abc`, `0`, boş) → `400`, `"Invalid userId"`
- [ ] 3. geçerli → `200`, o kullanıcının todo'ları (hiç yoksa `[]`)

---

#### Metod 3: `void handleGetOne(HttpExchange ex, String idText)` — `GET /todos/{id}`

`idText` = URL'deki id kısmı, metin olarak (`"5"`, `"abc"` ...).

- [ ] 1. `idText` pozitif tam sayı değil → `400`, `"Invalid id"`
- [ ] 2. o id'de todo yok → `404`, `"Todo not found: " + id`
- [ ] 3. var → `200`, todo'nun kendisi

---

#### Metod 4: `void handleCreate(HttpExchange ex)` — `POST /todos`

Body örneği: `{"title": "Spring ogren", "userId": 1}`

- [ ] 1. body `CreateTodoRequest`'e çevrilemiyorsa (bozuk/boş JSON) → `400`, `"Invalid JSON"`
      (ipucu: `readBody` bu durumda `JsonProcessingException` fırlatır; onu **yakala**)
- [ ] 2. `title` `null` veya boş/sadece boşluk → `400`, `"Title is blank"`
- [ ] 3. `userId <= 0` → `400`, `"Invalid userId"`
- [ ] 4. geçerli → repo'ya ekle, `Location` header'ına `"/todos/" + yeniId` koy, `201` + oluşan todo

---

#### Metod 5: `void handleDelete(HttpExchange ex, String idText)` — `DELETE /todos/{id}`

- [ ] 1. `idText` pozitif tam sayı değil → `400`, `"Invalid id"`
- [ ] 2. o id'de todo yok → `404`, `"Todo not found: " + id`
- [ ] 3. silindi → `204` (body yok)

---

### B5. Kendi sunucunla konuş (isteğe bağlı ama keyifli)

`TodoServerApp`'i IntelliJ'den çalıştır ("Sunucu calisiyor" yazmalı), sonra Git Bash'te:

```bash
curl -i localhost:8080/todos
curl -i localhost:8080/todos/2
curl -i localhost:8080/todos/abc
curl -i -X POST localhost:8080/todos -H "Content-Type: application/json" -d '{"title":"kendi sunucum","userId":1}'
curl -i -X DELETE localhost:8080/todos/1
curl -i -X PUT localhost:8080/todos/2
```

Bonus: dünkü `TodoClient`'ı `new TodoClient("http://localhost:8080")` ile kendi sunucuna bağla.
Dün yazdığın istemci, bugün yazdığın sunucuyla konuşur.

---

## 4. Görev C — LeetCode

**C1. LeetCode 1047 — Remove All Adjacent Duplicates In String** (Easy, Java)
`"abbaca"` → `"ca"` (önce `bb` silinir → `"aaca"`, sonra `aa` silinir → `"ca"`).
Dün LC 20'de kullandığın yapı burada da işe yarıyor: yeni gelen harf, **en son kalan** harfle aynıysa ikisi birden gider.

**C2. LeetCode 1633 — Percentage of Users Attended a Contest** (Easy, SQL)
Her yarışma için, **bütün kullanıcıların** yüzde kaçı katılmış. Bölen (toplam kullanıcı sayısı)
başka bir tablodan geliyor: Gün 37'de `AVG`'yi `WHERE`'e koyamayıp alt sorguya aldığın gibi, burada da
toplam kullanıcı sayısını bir alt sorguyla al.

---

Bitince: "bitti" de. Görev B'yi kendi test sunucumla (gerçek HTTP istekleriyle) deneyip sonucu söyleyeceğim.
