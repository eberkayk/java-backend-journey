# Hafta 7 · Gün 1 (Gün 38) — HTTP + JSON: Java'dan bir API'ye istek atmak

**Hedef:** Bir HTTP isteğini ve cevabını parça parça okuyabilmek (metod, URL, status, header, body),
JSON'u Java nesnesine çevirmek ve **Java'dan gerçek bir API'ye GET/POST atan bir client** yazmak.
Spring'de yazacağın her controller bu isteklerin **öbür ucu** olacak.

**Kurallar:**
- Kodu sen yazarsın; testleri **ben** yazar, senin sınıfına karşı çalıştırırım. Bitince "bitti" de.
- Yeni kural (spec ritüeli): Görev B'deki koşul listesini koda başlamadan oku, bitince her birini ✔ diye işaretle.
- Takılırsan "1 ipucu ver" de — çözüm değil, sıradaki küçük adımı veririm.
- curl komutlarını **Git Bash**'te çalıştır (PowerShell JSON tırnaklarını bozabiliyor).

---

## 0. Isınma — cevapları chat'e yaz

**S1.** (JDBC) `category` kolonu `VARCHAR`. Bu kodda **2 hata** var. Hangileri, düzeltilmiş hali ne?
```java
PreparedStatement ps = conn.prepareStatement("DELETE FROM expenses WHERE category = ?");
ps.setObject(1, Category.FOOD);
ResultSet rs = ps.executeQuery();
```
Doğru metod ne döndürür?

**S2.** (SQL) "Ortalamanın üstündeki siparişler" için yazılmış:
```sql
SELECT id, total FROM orders WHERE total > AVG(total);
```
Ne olur? Nasıl düzeltirsin?

**S3.** (Java) Veritabanı bağlantısı koptu, `executeUpdate` `SQLException` fırlattı:
```java
int deleteAll() {
    try {
        return ps.executeUpdate();
    } catch (SQLException e) {
        System.out.println("hata");
    }
    return 0;
}
```
Çağıran kod ne görür, exception mı yoksa bir sayı mı? Bu neden tehlikeli? catch'e ne eklemelisin?

**S4.** (SQL) Veri:
```
customers        orders
 id | name        id | customer_id
----+------      ----+------------
  1 | Ali          10 |          1
  2 | Can          11 |          1
```
```sql
SELECT c.name, COUNT(*) AS a, COUNT(o.id) AS b
FROM customers c LEFT JOIN orders o ON o.customer_id = c.id
GROUP BY c.name;
```
Ali ve Can için `a` ve `b` kaç?

---

## 1. Ders

### 1.1 HTTP = metin olarak giden bir istek, metin olarak gelen bir cevap

Tarayıcı, Postman, curl veya Java kodu fark etmez. Sunucuya giden şey düz metindir:

```
GET /todos/1 HTTP/1.1                    ← istek satırı: METOD  YOL  VERSİYON
Host: jsonplaceholder.typicode.com       ← header'lar (Ad: değer)
Accept: application/json
                                         ← boş satır = header'lar bitti
                                         ← body (GET'te genelde yok)
```

Gelen cevap:

```
HTTP/1.1 200 OK                          ← status satırı: VERSİYON  KOD  AÇIKLAMA
Content-Type: application/json           ← header'lar
Content-Length: 83

{ "userId": 1, "id": 1, "title": "delectus aut autem", "completed": false }   ← body
```

URL'nin parçaları:

```
https://jsonplaceholder.typicode.com/todos?userId=1&completed=false
└─┬─┘   └────────────┬────────────┘└─┬──┘└───────────┬──────────┘
 şema               host            path       query parametreleri (? ile başlar, & ile ayrılır)
```

### 1.2 Metodlar

| Metod    | Ne yapar                        | Body var mı | Güvenli (veri değiştirmez) | Idempotent |
| -------- | ------------------------------- | ----------- | -------------------------- | ---------- |
| `GET`    | Oku                             | Hayır       | Evet                       | Evet       |
| `POST`   | Yeni kayıt oluştur              | Evet        | Hayır                      | **Hayır**  |
| `PUT`    | Kaydın **tamamını** değiştir    | Evet        | Hayır                      | Evet       |
| `PATCH`  | Kaydın **bir kısmını** değiştir | Evet        | Hayır                      | Genelde    |
| `DELETE` | Sil                             | Genelde yok | Hayır                      | Evet       |

**Idempotent** = aynı isteği 1 kez de atsan 5 kez de atsan sunucudaki son durum aynı.
- `DELETE /todos/5` → 5 kez at, 5 numara yine silinmiş durumda. Idempotent.
- `POST /todos` → 5 kez at, **5 yeni kayıt** oluşur. Idempotent değil.

Mülakatta çok sorulur: "PUT ile POST farkı ne?", "Hangi metodlar idempotent?"

### 1.3 Status kodları — ilk rakam kategoriyi söyler

| Kod   | Anlamı                 | Ne zaman                                                    |
| ----- | ---------------------- | ----------------------------------------------------------- |
| `200` | OK                     | Başarılı GET/PUT/PATCH                                      |
| `201` | Created                | POST yeni kayıt oluşturdu (+ `Location` header'ı)           |
| `204` | No Content             | Başarılı ama body yok (çoğu DELETE)                         |
| `301` | Moved Permanently      | Adres kalıcı değişti                                        |
| `304` | Not Modified           | "Sendeki kopya hâlâ güncel" (önbellek)                      |
| `400` | Bad Request            | İstek bozuk: eksik alan, yanlış format                      |
| `401` | Unauthorized           | **Kim olduğunu bilmiyorum** (giriş yok / token yok)         |
| `403` | Forbidden              | **Kim olduğunu biliyorum ama izin yok**                     |
| `404` | Not Found              | Kayıt/yol yok                                               |
| `405` | Method Not Allowed     | Bu yol bu metodu desteklemiyor (örn. `/login`'e `DELETE`)   |
| `409` | Conflict               | Çakışma (örn. aynı e-posta zaten kayıtlı)                   |
| `500` | Internal Server Error  | Sunucu kodunda hata (yakalanmamış exception)                |
| `503` | Service Unavailable    | Sunucu şu an hizmet veremiyor (bakım/aşırı yük)             |

Kural: **4xx = istemcinin hatası**, **5xx = sunucunun hatası**.
Spring'de yakalanmamış bir exception → kullanıcı `500` görür. İleride bunu `404`/`400`'e çevirmeyi öğreneceğiz.

### 1.4 Önemli header'lar

| Header          | Yön        | Anlamı                                                         |
| --------------- | ---------- | -------------------------------------------------------------- |
| `Content-Type`  | iki yön de | "Gönderdiğim body'nin formatı bu" → `application/json`         |
| `Accept`        | istek      | "Ben bu formatta cevap istiyorum"                              |
| `Authorization` | istek      | Kimlik bilgisi → `Bearer eyJhbGc...` (JWT, Hafta 10)           |
| `Location`      | cevap      | `201`'de yeni kaydın adresi → `/todos/201`                     |

`Content-Type` yanlışsa sunucu body'yi yanlış okur. Görev A5'te bunu kendi gözünle göreceksin.

### 1.5 JSON → Java

JSON'da 6 tip var: string `"a"`, sayı `3` / `2.5`, boolean `true`, `null`, nesne `{...}`, dizi `[...]`.

```json
{ "id": 3, "name": "Ali", "active": true, "tags": ["a", "b"], "manager": null }
```

Java karşılığı, alan adları JSON anahtarlarıyla **birebir aynı** olmalı:

```java
record User(int id, String name, boolean active, List<String> tags, String manager) {}
```

Bu çeviriyi **Jackson** kütüphanesi yapar (Spring de arka planda Jackson kullanır):

```java
ObjectMapper mapper = new ObjectMapper();

User u = mapper.readValue(jsonText, User.class);                                // JSON nesne → record
List<User> list = mapper.readValue(jsonText, new TypeReference<List<User>>() {}); // JSON dizi → List
String json = mapper.writeValueAsString(u);                                       // record → JSON metni
```

- `readValue` ve `writeValueAsString`, `JsonProcessingException` fırlatabilir. Bu bir `IOException` alt sınıfıdır, yani checked.
- `TypeReference` sadece **liste/generic** okurken lazım, çünkü `List<User>.class` diye bir şey yazamazsın.
- JSON'da olup record'da olmayan bir alan varsa Jackson varsayılan olarak **hata fırlatır** (`UnrecognizedPropertyException`).

### 1.6 Java'dan istek atmak: `HttpClient` (Java 11+, kütüphane gerekmez)

```java
HttpClient http = HttpClient.newHttpClient();

// GET
HttpRequest get = HttpRequest.newBuilder(URI.create("https://example.com/users/3"))
        .header("Accept", "application/json")
        .GET()
        .build();

HttpResponse<String> res = http.send(get, HttpResponse.BodyHandlers.ofString());
int code = res.statusCode();   // 200, 404, ...
String body = res.body();      // cevabın body'si, String olarak

// POST
HttpRequest post = HttpRequest.newBuilder(URI.create("https://example.com/users"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(jsonText))
        .build();
```

- `send(...)` → `IOException` (ağ hatası) ve `InterruptedException` fırlatabilir, ikisi de checked.
- **`send` 404'te exception FIRLATMAZ.** 404 de bir cevaptır. Status'u **sen** kontrol edersin.

### 1.7 curl kopya kağıdı (Git Bash)

```bash
curl -i URL                                   # -i: status satırı + header'ları da göster
curl -i -X DELETE URL                         # -X: metod
curl -i -X POST URL -H "Content-Type: application/json" -d '{"a":1}'   # -H header, -d body
curl -i -u kullanici:sifre URL                # Basic auth (Authorization header'ını curl üretir)
```

> **Kağıda Yaz**
> 1. İstek = metod + URL + header'lar + (body). Cevap = status + header'lar + (body).
> 2. GET oku, POST oluştur, PUT tamamını değiştir, PATCH bir kısmını, DELETE sil.
> 3. Idempotent = 5 kez atsan da son durum aynı. POST değil, diğerleri evet.
> 4. 2xx başarı · 4xx istemci hatası · 5xx sunucu hatası. 201 Created, 204 No Content.
> 5. 401 = kim olduğunu bilmiyorum, 403 = biliyorum ama izin yok.
> 6. Content-Type = gönderdiğimin formatı, Accept = istediğim format.
> 7. Jackson: readValue (JSON→Java), writeValueAsString (Java→JSON), liste için TypeReference.
> 8. HttpClient.send 404'te exception atmaz, status'u ben kontrol ederim.

---

## 2. Görev A — curl ile istek at

Her madde için: **önce tahminini yaz**, sonra komutu çalıştır, sonra gerçek sonucu yaz.
Chat'e madde başına tek satır yeter: `A1: tahmin 200 / gerçek 200 / Content-Type: ...`

Base URL: `https://jsonplaceholder.typicode.com` (sahte bir test API'si, yazma işlemleri gerçekte kaydedilmez)

**A1.** `curl -i https://jsonplaceholder.typicode.com/todos/1`
→ Status kodu? `Content-Type` header'ının değeri?

**A2.** `curl -i https://jsonplaceholder.typicode.com/todos/99999`
→ Status kodu? Body ne?

**A3.** `curl -s "https://jsonplaceholder.typicode.com/todos?userId=1&completed=false"`
→ Body nesne mi dizi mi? Kaç kayıt var? (saymak için sonuna `| grep -c '"id"'` ekleyebilirsin)

**A4.**
```bash
curl -i -X POST https://jsonplaceholder.typicode.com/todos -H "Content-Type: application/json" -d '{"title":"HTTP calis","userId":1,"completed":false}'
```
→ Status kodu? `Location` header'ı ne? Body'deki `id` kaç?

**A5.** A4'ün aynısı, sadece **`-H "Content-Type: application/json"` kısmını sil**.
→ Body nasıl değişti? Neden? (ipucu: curl `-d` ile header vermezsen varsayılan olarak `application/x-www-form-urlencoded` gönderir)

**A6.** `curl -i -X DELETE https://jsonplaceholder.typicode.com/todos/1`
→ Status kodu? Aynı komutu 3 kez çalıştırırsan ne olmalı? DELETE idempotent mi?

**A7.**
```bash
curl -i https://httpbin.org/basic-auth/enes/1234
curl -i -u enes:1234 https://httpbin.org/basic-auth/enes/1234
```
→ İki status kodu? Birinciye `401` mi `403` mü dönmeli, neden?

---

## 3. Görev B — `TodoClient`

Proje: `projects/todo-client` (pom.xml'i ben hazırladım, Jackson ekli). IntelliJ'de **Open** ile aç.
Paket: `com.eberkayk.todo` → klasör: `src/main/java/com/eberkayk/todo/`

### B1. `record Todo` — `Todo.java`

**Alanlar** (JSON anahtarlarıyla birebir aynı):
- `int userId`
- `int id`
- `String title`
- `boolean completed`

### B2. `class TodoNotFoundException` — `TodoNotFoundException.java`

- `RuntimeException`'dan türer.
- **Constructor:** `TodoNotFoundException(int id)` → mesaj: `"Todo not found: " + id`

### B3. `class TodoClient` — `TodoClient.java`

**Alanlar:**
- `private final String baseUrl`
- `private final HttpClient http` → `HttpClient.newHttpClient()`
- `private final ObjectMapper mapper` → `new ObjectMapper()`

**Constructor:** `TodoClient(String baseUrl)`
- Örnek kullanım: `new TodoClient("https://jsonplaceholder.typicode.com")`
- Ben testte kendi sahte sunucumun adresini vereceğim. O yüzden URL'yi **asla** sınıfın içine sabit yazma, hep `baseUrl + ...` kullan.

**Üç metodun hepsi:** `throws IOException, InterruptedException`

---

#### Metod 1: `Todo findById(int id)`

İstek: `GET {baseUrl}/todos/{id}`

Koşullar (kodlamadan önce oku, bitince işaretle):
- [ ] 1. `id <= 0` → `IllegalArgumentException("Invalid id")`, **istek atılmaz**
- [ ] 2. status `200` → body'yi `Todo`'ya çevir, döndür
- [ ] 3. status `404` → `TodoNotFoundException(id)` fırlat
- [ ] 4. başka her status → `IllegalStateException("Unexpected status: " + kod)`

---

#### Metod 2: `List<Todo> findByUser(int userId)`

İstek: `GET {baseUrl}/todos?userId={userId}`

Koşullar:
- [ ] 1. `userId <= 0` → `IllegalArgumentException("Invalid userId")`, **istek atılmaz**
- [ ] 2. status `200` → body'yi `List<Todo>`'ya çevir, döndür
- [ ] 3. hiç todo yoksa sunucu `[]` döner → **boş liste** döndür (`null` değil, exception değil)
- [ ] 4. `200` dışındaki her status → `IllegalStateException("Unexpected status: " + kod)`

---

#### Metod 3: `Todo create(String title, int userId)`

İstek: `POST {baseUrl}/todos`
- Header: `Content-Type: application/json`
- Body: `{"title": ..., "userId": ..., "completed": false}`

Koşullar:
- [ ] 1. `title` `null` veya boş/sadece boşluk → `IllegalArgumentException("Title is blank")`, **istek atılmaz**
- [ ] 2. `userId <= 0` → `IllegalArgumentException("Invalid userId")`, **istek atılmaz**
- [ ] 3. body'deki JSON'u **elle string birleştirerek yazma**, Jackson ile üret (`writeValueAsString`)
- [ ] 4. status `201` → cevap body'sini `Todo`'ya çevir, döndür
- [ ] 5. `201` dışındaki her status → `IllegalStateException("Unexpected status: " + kod)`

Bu senin tasarım kararın: `writeValueAsString`'e ne vereceğin. Ayrı bir record (`NewTodo` gibi) de olur, `Map.of(...)` da olur.

---

**Çalıştırmak istersen (isteğe bağlı):** 3-4 satırlık bir `Main` ile gerçek API'ye karşı `findById(1)` deneyebilirsin. Zorunlu değil, testleri ben çalıştıracağım.

---

## 4. Görev C — LeetCode

**C1. LeetCode 20 — Valid Parentheses** (Easy, Java)
`"()[]{}"` → true, `"(]"` → false, `"([)]"` → false, `"{[]}"` → true.
Mülakatların en klasik sorusu. Hangi veri yapısı lazım, önce onu düşün: en son açılan parantez ilk kapanmalı.

**C2. LeetCode 1280 — Students and Examinations** (Easy, SQL)
Her öğrenci × her ders için kaç kez sınava girdiğini sayan sorgu, **0 olanlar da görünecek**.
Dün öğrendiğin iki şey burada lazım: LEFT JOIN'in koşulu nereye yazılır, ve `COUNT(*)` mı `COUNT(kolon)` mu.

---

Bitince: "bitti" de. Görev B'yi kendi harness'ımla test edip sonucu söyleyeceğim.
