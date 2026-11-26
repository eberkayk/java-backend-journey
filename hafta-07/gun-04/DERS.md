# Hafta 7 · Gün 4 (Gün 41) — SOLID + 4 temel design pattern

**Hedef:** Şimdiye kadar "çalışan kod" yazdın. Bugün "**değiştirmesi kolay** kod" ne demek, onu öğreniyorsun.
Mülakatta neredeyse kesin gelen iki soru: "SOLID nedir?" ve "Hangi design pattern'leri biliyorsun?".
İkisini de **kendi kodundan örnekle** cevaplayabilir hale geleceksin.

İyi haber: bugünkü kavramların çoğunu **zaten kullandın**, sadece adlarını bilmiyordun.

**Kurallar:**
- Ders chat'te adım adım ilerler. Bu dosya **referans**.
- Ezber yok: her kavram için "hangi problemi çözüyor?" sorusunun cevabını bil, yeter.
- Takılırsan "1 ipucu ver" de.

---

## 0. Isınma — cevapları chat'e yaz

**S1.** (JUnit, dün) `repo` boş. Bu test kırmızı olur. JUnit **tam olarak** hangi mesajı yazar?
```java
Todo t = repo.create("A", 1);      // t.id() = 1
assertEquals(t.id(), 2);
```

**S2.** (Java, dünkü LC 682) Ne yazdırır?
```java
Deque<Integer> st = new ArrayDeque<>();
st.push(5);
st.push(2);
int a = st.peek();
int b = st.pop();
System.out.println(a + " " + b + " " + st.size());
```

**S3.** (Java) `p` ve `q` kaç olur?
```java
int poor = 1, total = 3;
double p = poor / total * 100;
double q = poor * 100.0 / total;
```

**S4.** (SQL, dünkü LC 1211) `Queries` tablosunda `rating` sütunu: `5`, `2`, `1` (3 satır). Sonuç?
```sql
SELECT SUM(CASE WHEN rating < 3 THEN 1 ELSE 0 END) AS poor,
       COUNT(*)                                     AS total
FROM Queries;
```

---

## 1. Ders

### 1.1 Problem: kod değişir

Yazdığın kod bir kere yazılıp bitmez. Patron gelir: "Todo'lar artık bellekte değil, PostgreSQL'de dursun."
"Sıralama başlığa göre değil, tarihe göre olsun." "Bir de e-posta gönder."

Kötü yazılmış kodda **küçük bir istek = 10 dosyada değişiklik** + bir sürü yeni bug.
İyi yazılmış kodda **küçük istek = 1 yeni sınıf, 1 satır değişiklik**.

**SOLID** = "değiştirmesi kolay kod" için 5 kural. **Design pattern** = sık karşılaşılan bir probleme
herkesin bildiği, adı konmuş hazır çözüm (ustaların "bunun bir kalıbı var" dediği şey).

### 1.2 S — Single Responsibility (Tek Sorumluluk)

> Bir sınıfın **değişmek için tek bir sebebi** olmalı.

Benzetme: Restoranda aşçı yemek yapar, garson servis yapar, kasiyer hesap alır. Kasa sistemi
değişince aşçıyı yeniden eğitmezsin.

Kendi kodun: todo-server'da **3 ayrı sınıf** yazdın:

| Sınıf            | İşi                                   | Ne olursa değişir?              |
|------------------|---------------------------------------|---------------------------------|
| `Todo`           | veriyi taşır                          | todo'ya yeni alan eklenirse     |
| `InMemoryTodoRepository` | veriyi saklar / bulur                 | saklama yeri değişirse (DB)     |
| `TodoHandler`    | HTTP isteğini okur, cevap yazar       | URL / status kodu değişirse     |

Hepsini tek sınıfa yazsaydın, DB'ye geçerken HTTP kodunun arasında kaybolurdun. **Bu SRP.**

Kontrol sorusu (kendine): "Bu sınıf **neden** değişebilir?" — iki farklı cevap çıkıyorsa, iki sınıf olmalı.

### 1.3 D — Dependency Inversion (Bağımlılığı Tersine Çevirme)

> Üst seviye kod (Handler) **somut sınıfa** değil, **interface'e** bağlı olsun.

Benzetme: Duvardaki priz. Lambanın fişi "220V priz" standardına uyar; arkada hangi santral var,
lamba bilmez. Santral değişir, lamba değişmez. **Priz = interface.**

Şu an kodun:
```java
class TodoHandler {
    private final TodoRepository repo;          // TodoRepository = SOMUT sınıf (TreeMap'li)
    public TodoHandler(TodoRepository repo) { this.repo = repo; }
}
```
Yarın `JdbcTodoRepository` (PostgreSQL) yazdın. Handler onu **kabul edemez** — çünkü tipi
"tam olarak TreeMap'li o sınıf".

Çözüm:
```java
interface TodoRepository {                    // PRİZ: sadece "ne yapılabilir" listesi
    List<Todo> findAll();
    Optional<Todo> findById(int id);
    ...
}
class InMemoryTodoRepository implements TodoRepository { ... }   // TreeMap'li (bugünkü)
class JdbcTodoRepository     implements TodoRepository { ... }   // PostgreSQL'li (ileride)
```
Handler `InMemoryTodoRepository` (interface) ister → ikisini de kabul eder. Hangisinin verileceğine
**tek bir yerde** (`TodoServerApp.main`) karar verilir:
```java
TodoRepository repo = new InMemoryTodoRepository();   // DB'ye geçince sadece bu satır değişir
server.createContext("/todos", new TodoHandler(repo));
```

Dışarıdan constructor ile vermeye **dependency injection** (bağımlılık enjeksiyonu) denir.
**Spring'in kalbi tam olarak bu.** Hafta 8'de Spring bu `new` satırını senin yerine yazacak.

Bonus: test yazarken gerçek DB yerine sahte (fake) bir repo verebilirsin — interface sayesinde.

### 1.4 O, L, I — kısaca

- **O — Open/Closed:** Yeni özellik = **yeni sınıf ekle**, eskisini değiştirme.
  `JdbcTodoRepository` eklemek handler'ı değiştirmedi → bu O.
- **L — Liskov Substitution:** Interface'i uygulayan her sınıf, onun yerine **sürprizsiz** konabilmeli.
  `findById` bulamazsa `InMemory` boş `Optional` dönüyorsa, `Jdbc` de öyle yapmalı (exception fırlatmamalı).
- **I — Interface Segregation:** Dev bir interface yerine küçük interface'ler. Sadece okuyan bir sınıfı
  `delete` metodunu uygulamaya zorlama.

Mülakat için: S ve D'yi kendi örneğinle anlat, O-L-I'yı birer cümleyle söyle. Yeter.

### 1.5 Pattern 1 — Strategy

> Algoritmanın **değişen kısmını** dışarıdan parametre olarak ver.

Bunu Hafta 3'ten beri yapıyorsun:
```java
todos.sort(Comparator.comparing(Todo::title));               // başlığa göre
todos.sort(Comparator.comparing(Todo::id).reversed());       // id'ye göre, ters
```
`sort` metodunun kodu **hiç değişmedi**. Değişen sadece verdiğin `Comparator`. `Comparator` = strateji.

### 1.6 Pattern 2 — Builder

> Çok parçalı bir nesneyi **adım adım** kur, sonunda `build()`.

Gün 38'de kullandın:
```java
HttpRequest request = HttpRequest.newBuilder(URI.create(url))
        .header("Content-Type", "application/json")
        .POST(BodyPublishers.ofString(json))
        .build();
```
Alternatifi 6 parametreli, yarısı `null` olan bir constructor olurdu: `new HttpRequest(url, null, "POST", json, null, null)`.
Hangisi hangi parametre? Okunmaz. Builder bunu çözer.

### 1.7 Pattern 3 — Factory

> Nesneyi `new` ile değil, **onu senin yerine üreten bir metodla** al.

Bunları da kullandın:
```java
List<Integer> list = List.of(1, 2, 3);
Optional<Todo> o   = Optional.ofNullable(todo);
HttpClient http    = HttpClient.newHttpClient();
```
Hangi somut sınıfın üretildiğini bilmiyorsun ve **bilmen gerekmiyor**. Üretim detayını değiştirmek
isteyen sadece fabrikayı değiştirir.

### 1.8 Pattern 4 — Singleton

> Bir sınıftan uygulamada **tek bir nesne** olsun, herkes onu paylaşsın.

Senin `main`'in: `new InMemoryTodoRepository()` **bir kere** çağrılıyor, aynı nesne handler'a veriliyor.
Her istekte yeni repo oluştursaydın, POST ile eklediğin todo GET'te kaybolurdu.

Klasik ders kitabı yazımı (private constructor + `static getInstance()`) var ama gerçek projede bunu
elle nadiren yazarsın: **Spring, oluşturduğu nesneleri varsayılan olarak singleton yapar.**

### Kağıda Yaz
```
SOLID = değiştirmesi kolay kod
S: bir sınıf, değişmek için tek sebep (Todo / Repository / Handler)
D: somut sınıfa değil interface'e bağlan; nesneyi constructor ile dışarıdan ver (= DI, Spring'in kalbi)
O: yeni özellik = yeni sınıf   L: yerine konan sürpriz yapmasın   I: küçük interface'ler
Strategy  = değişen kısmı parametre ver      (Comparator)
Builder   = adım adım kur, build()           (HttpRequest.newBuilder)
Factory   = new yerine üreten metod          (List.of, Optional.of)
Singleton = tek nesne, herkes paylaşır       (main'deki tek repo, Spring bean'leri)
```

---

## 2. Görev A — Hangisi? (chat'te, tek tek)

**A1.** Bu sınıfın **kaç farklı** değişme sebebi var? Hangileri?
```java
class ExpenseReport {
    double total(List<Expense> list) { ... }                 // toplamı hesaplar
    String toText(double total)      { ... }                 // "Toplam: 120 TL" metni üretir
    void saveToFile(String text)     { ... }                 // dosyaya yazar
    void sendMail(String text)       { ... }                 // e-posta atar
}
```

**A2.** Yarın `JdbcTodoRepository` yazılacak. Aşağıdaki iki versiyonda, PostgreSQL'e geçmek için
`TodoHandler` içinde kaç satır değişir?
```java
// Versiyon 1
class TodoHandler {
    private final InMemoryTodoRepository repo = new InMemoryTodoRepository();
}
// Versiyon 2
class TodoHandler {
    private final TodoRepository repo;                       // interface
    TodoHandler(TodoRepository repo) { this.repo = repo; }
}
```

**A3.** Her satır hangi pattern? (Strategy / Builder / Factory / Singleton)
```java
a) HttpClient client = HttpClient.newHttpClient();
b) names.sort(Comparator.reverseOrder());
c) HttpRequest r = HttpRequest.newBuilder(uri).GET().build();
d) // main'de tek repo oluşturulup bütün handler'lara aynısı veriliyor
```

**A4.** (L — Liskov) `InMemoryTodoRepository.findById(99)` → boş `Optional`.
Arkadaşın `JdbcTodoRepository.findById(99)` için bulamayınca `throw new RuntimeException("yok")` yazmış.
`TodoHandler.handleGetOne` hiç değişmeden `GET /todos/99` isteğinde ne olur? Neden bu bir sorun?

---

## 3. Görev B — todo-server'ı interface'e çevir

Proje: `projects/todo-server`. Amaç: Handler artık **somut sınıfa değil interface'e** bağlı olsun.
Davranış **hiç** değişmeyecek: sonunda testlerin yine 4/4 yeşil.

### B1. Sınıfı yeniden adlandır
`InMemoryTodoRepository` sınıfının adını `InMemoryTodoRepository` yap.
IntelliJ: sınıf adının üstüne gel → **Shift+F6** → yeni adı yaz → Enter.
(Dosya adı ve kullanıldığı **her yer** otomatik değişir: Handler, App, Test.)

### B2. Interface'i yaz
Yeni dosya: `TodoRepository.java` (aynı paket). İçinde `public interface TodoRepository`.
İçine `InMemoryTodoRepository`'deki **5 public metodun sadece imzası** (gövde yok, `;` ile biter):
`findAll`, `findByUser`, `findById`, `create`, `deleteById`.

### B3. Bağla
- `InMemoryTodoRepository` → `implements TodoRepository`. 5 metodun üstüne `@Override`.
- `TodoHandler` → alan ve constructor parametresinin tipi **`InMemoryTodoRepository`** (interface) olsun.
  (B1'deki rename bunu `InMemoryTodoRepository` yaptı — geri çevir.)
- `TodoServerApp` → `TodoRepository repo = new InMemoryTodoRepository();`
- `InMemoryTodoRepositoryTest` → alan tipi `InMemoryTodoRepository`, `@BeforeEach`'te `new InMemoryTodoRepository()`.

### B4. Kontrol
`./mvnw test` → 4/4 yeşil. Sunucu da eskisi gibi çalışmalı (ben harness ile kontrol edeceğim).

Soru (chat'e): `TodoHandler`'da `repo.` yazınca IntelliJ'in önerdiği metod listesi neden
`TreeMap`'in metodlarını (`put`, `get`...) **göstermiyor**?

---

## 4. Görev C — LeetCode

**C1. LeetCode 844 — Backspace String Compare** (Easy, Java)
İki string, `#` = bir önceki karakteri sil. Silmeler uygulandıktan sonra ikisi eşit mi?
Örnek: `"ab#c"` → `"ac"`. Gün 38–40'ın yapısı yine: hep **en son eklenene** bakıyorsun.
Boş yığında `#` gelirse? → hiçbir şey olmaz (dikkat: Gün 39'daki boş `peek` hatası).

**C2. LeetCode 1934 — Confirmation Rate** (Medium, SQL)
Her kullanıcı için: `confirmed` mesaj sayısı / toplam mesaj sayısı, 2 haneye yuvarlanmış.
Hiç mesajı olmayan kullanıcı da listede (oran `0.00`). Dün öğrendiğin iki şey + Gün 38'deki LEFT JOIN.

---

Bitince: "bitti" de. `./mvnw test`'i ve sunucu harness'ını ben çalıştırıp sonucu söyleyeceğim.
