# Hafta 7 · Gün 3 (Gün 40) — JUnit 5: kodunu otomatik kontrol eden kod

**Hedef:** Şimdiye kadar kodunun doğru çalıştığını iki yolla anladın: `main`'de `println` ile baktın,
ya da ben kendi test programımı (harness) senin koduna karşı çalıştırdım. Bugün o harness'ın
**sektördeki standart halini** öğreniyorsun: **JUnit 5**. İş ilanlarında "JUnit" neredeyse her Java
ilanında yazar; mülakatta "unit test nedir, nasıl yazarsın?" sorusu gelir.

**Kurallar:**
- Ders chat'te adım adım ilerler. Bu dosya **referans**.
- Bugün test yazmak **angarya değil, işin kendisi** (öğrendiğin beceri bu). Az ve kısa: Görev B'de 4 test.
- Takılırsan "1 ipucu ver" de.

---

## 0. Isınma — cevapları chat'e yaz

**S1.** (Java) Boşluğa **tek satır** yaz: çağıran kod hatayı fark etsin, sebebi (`e`) de kaybolmasın.
```java
int countExpenses() {
    try {
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1);
    } catch (SQLException e) {
        ______________________________________
    }
}
```

**S2.** (Java, dünkü `parsePositiveInt`) Her satır için: değer ne olur, ya da exception mı fırlar?
```java
int a = Integer.parseInt("12");
int b = Integer.parseInt("-5");
int c = Integer.parseInt("12a");
```

**S3.** (REST, dün) Sunucunda 7 numaralı kullanıcının **hiç** todo'su yok, 7 numaralı todo da yok.
a) `GET /todos?userId=7` → status kodu ve body?
b) `GET /todos/7` → status kodu?

**S4.** (SQL, PostgreSQL — dünkü LC 1633) İki sütun ne çıkar?
```sql
SELECT 2 / 3 * 100      AS x,
       2 * 100.0 / 3    AS y;
```

---

## 1. Ders

### 1.1 Neden otomatik test?

Dün `TodoRepository` yazdın. Diyelim yarın `create`'i değiştirdin. Hâlâ doğru çalışıyor mu?
Bunu anlamak için ne yaparsın? `main` açarsın, 3 todo eklersin, `println` ile bakarsın, gözünle
kontrol edersin. Ertesi gün başka bir şeyi değiştirdin → **aynı şeyi baştan yaparsın**.

Gerçek bir projede yüzlerce metod var. Her değişiklikten sonra hepsini elle kontrol etmek imkânsız.
Çözüm: kontrolü de **koda yazmak**. Bir kere yazarsın, sonra bir tuşla saniyeler içinde hepsi
tekrar çalışır ve sana "hepsi doğru" ya da "şu bozuldu" der.

Benzetme: araba muayenesi. Usta her seferinde "frene bastım, durdu mu? farı yaktım, yandı mı?"
diye aynı listeyi kontrol eder. Unit test = o liste, ama bilgisayar yapıyor.

Aslında bunu zaten gördün: Gün 38–39'da "26/26 geçti" dediğim harness tam olarak buydu.
JUnit, bu işi herkesin aynı şekilde yazması için yapılmış hazır bir kütüphane.

**Unit test** = **tek bir küçük parçayı** (bir metodu, bir sınıfı) tek başına test eden kod.
"Unit" = birim. `TodoRepository` bir birim; sunucuyu, HTTP'yi, veritabanını işe karıştırmıyoruz.

### 1.2 Test nereye yazılır?

Maven'da iki ayrı klasör var:
```
src/main/java/com/eberkayk/todoserver/TodoRepository.java      ← asıl kod
src/test/java/com/eberkayk/todoserver/TodoRepositoryTest.java  ← onun testi
```
- Test sınıfı **aynı paket adında**, adı `<SınıfAdı>Test`.
- `src/test` içindeki kod uygulamanın içine girmez; sadece test çalıştırırken kullanılır.
- JUnit'i `pom.xml`'e ben ekledim (`<scope>test</scope>` = "sadece testlerde lazım").

### 1.3 İlk test — anatomi

```java
package com.eberkayk.todoserver;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TodoRepositoryTest {

    @Test
    void create_firstTodo_getsId1() {
        // 1) Hazırla (Arrange)
        TodoRepository repo = new TodoRepository();

        // 2) Çalıştır (Act)
        Todo todo = repo.create("Spring ogren", 3);

        // 3) Kontrol et (Assert)
        assertEquals(1, todo.id());
    }
}
```

Satır satır:
- `@Test` → JUnit'e "bu metod bir test, çalıştır" der. `main` yok; JUnit kendisi çağırır.
- Metod `void`, parametresiz. Adı **ne test ettiğini** anlatır (`create_firstTodo_getsId1`).
- Her test 3 adım: **hazırla → çalıştır → kontrol et**.
- `assertEquals(beklenen, gerçek)` → ikisi eşitse sessizce geçer. Eşit değilse test **kırmızı** olur ve
  şunu yazar:
  ```
  expected: <1> but was: <2>
  ```
  **Sıra önemli:** önce *beklediğin* değer, sonra *kodun ürettiği*. Ters yazarsan mesaj ters okunur.

Sonuç iki renk: **yeşil** = geçti, **kırmızı** = kırıldı.

### 1.4 Diğer kontrol metodları

| Metod                                       | Ne zaman geçer                            |
|---------------------------------------------|-------------------------------------------|
| `assertEquals(beklenen, gerçek)`            | ikisi `equals` ile eşitse                 |
| `assertTrue(koşul)`                         | koşul `true` ise                          |
| `assertFalse(koşul)`                        | koşul `false` ise                         |
| `assertThrows(XException.class, () -> ...)` | içindeki kod **o exception'ı fırlatırsa** |

`Optional` için: `assertTrue(repo.findById(99).isEmpty());`

`assertThrows` örneği (lambda'yı Hafta 4'ten biliyorsun — "bu kodu sen çalıştır" diye JUnit'e veriyoruz):
```java
@Test
void parseInt_letters_throws() {
    assertThrows(NumberFormatException.class, () -> Integer.parseInt("12a"));
}
```
Kod exception fırlatırsa → yeşil. **Fırlatmazsa** → kırmızı.

### 1.5 `@BeforeEach` — her test temiz başlasın

Her testte `new TodoRepository()` yazmak tekrar. Ortak hazırlığı bir kere yaz:
```java
class TodoRepositoryTest {
    private TodoRepository repo;

    @BeforeEach
    void setUp() {
        repo = new TodoRepository();
    }

    @Test
    void ...() { repo.create(...); ... }
}
```
`@BeforeEach` = "**her** `@Test`'ten hemen önce bunu çalıştır". Yani her test **boş, yepyeni** bir repo alır.

Neden önemli: testler **birbirinden bağımsız** olmalı. Bir test 3 todo ekledi diye diğer testin
id'leri 4'ten başlamamalı. Testlerin çalışma sırası garanti değil.

### 1.6 Çalıştırmak

- **IntelliJ:** test sınıfının/metodun solundaki yeşil ▶ → *Run*. Altta yeşil ✔ / kırmızı ✘ listesi.
  (pom değişti: sağdaki Maven panelinden 🔄 *Reload* yapman gerekebilir.)
- **Terminal:** proje klasöründe `./mvnw test` → sonunda `Tests run: 4, Failures: 0`.

### 1.7 Test gerçekten çalışıyor mu? Kasten boz.

Hiç kırmızı olmayan test, belki hiçbir şeyi kontrol etmiyordur. Profesyonel alışkanlık:
asıl kodu **bilerek** boz → test kırmızı olmalı → kodu geri al → yeşil. (Görev B5.)

### Kağıda Yaz
```
Unit test = tek bir metodu/sınıfı otomatik kontrol eden kod
Yer: src/test/java, aynı paket, adı XxxTest
@Test → JUnit çağırır, main yok
Hazırla → Çalıştır → Kontrol et
assertEquals(BEKLENEN, GERÇEK)   sıra önemli
assertTrue / assertFalse / assertThrows(X.class, () -> ...)
@BeforeEach → her testten önce, temiz başlangıç
Kırmızı görmediğin teste güvenme: kodu boz, kırmızıyı gör
```

---

## 2. Görev A — Yeşil mi kırmızı mı? (chat'te, tek tek)

Her soruda: **yeşil** mi **kırmızı** mı? Kırmızıysa JUnit ne mesaj yazar?
(Hepsinde `repo` = `@BeforeEach`'te oluşan yepyeni `TodoRepository`.)

**A1.**
```java
Todo t = repo.create("A", 5);
assertEquals(5, t.id());
```

**A2.**
```java
repo.create("A", 1);
repo.create("B", 2);
assertEquals(1, repo.findByUser(2).size());
```

**A3.**
```java
assertThrows(NumberFormatException.class, () -> Integer.parseInt("-5"));
```

**A4.**
```java
Todo t = repo.create("A", 1);
repo.deleteById(t.id());
assertTrue(repo.deleteById(t.id()));
```

---

## 3. Görev B — `TodoRepositoryTest`

Dosya: `projects/todo-server/src/test/java/com/eberkayk/todoserver/TodoRepositoryTest.java`
(klasörü ben açtım, dosya senin.)

Önce iskelet: `@BeforeEach` ile her testten önce yeni `repo`. Sonra 4 test.
Her testin **ne hazırlayacağı, ne çağıracağı, neyi kontrol edeceği** aşağıda yazıyor.

### B1. `create_assignsIncreasingIds`
- Hazırla: —
- Çalıştır: `"A"` (user 1) ve `"B"` (user 1) oluştur.
- Kontrol: ilkinin id'si `1`, ikincinin `2`, ilkinin `completed`'ı `false`.

### B2. `findById_missingId_returnsEmpty`
- Hazırla: — (repo boş)
- Çalıştır: `findById(99)`
- Kontrol: sonuç boş `Optional`.

### B3. `findByUser_returnsOnlyThatUsersTodos`
- Hazırla: `"A"` user 1, `"B"` user 2, `"C"` user 1 oluştur.
- Çalıştır: `findByUser(1)`
- Kontrol: listenin boyutu `2`; 0. elemanın title'ı `"A"`, 1. elemanın `"C"`.

### B4. `deleteById_existing_removesIt`
- Hazırla: `"A"` user 1 oluştur.
- Çalıştır: onun id'siyle `deleteById`.
- Kontrol: dönen değer `true`; sonra `findById` aynı id için boş.

### B5. Kasten boz
1. `./mvnw test` (ya da IntelliJ) → 4/4 yeşil.
2. `TodoRepository.create` içindeki `nextId++` satırını yorum satırı yap (`//`).
3. Tekrar çalıştır → **hangi** test(ler) kırmızı oldu, mesaj ne? Chat'e yaz.
4. `//`'yi geri al → tekrar 4/4 yeşil.

---

## 4. Görev C — LeetCode

**C1. LeetCode 682 — Baseball Game** (Easy, Java)
Komut listesi: sayı → kaydet, `"+"` → son iki skorun toplamını kaydet, `"D"` → son skorun 2 katını kaydet,
`"C"` → son skoru sil. Sonunda kalan skorların toplamı. Gün 38–39'daki yapı yine işe yarıyor:
hep **en son eklenene** bakıyorsun.

**C2. LeetCode 1211 — Queries Quality and Percentage** (Easy, SQL)
Her `query_name` için iki şey: `quality` = `rating / position` ortalaması, `poor_query_percentage` =
`rating < 3` olan satırların yüzdesi. İkisi de 2 haneye yuvarlanır. Isınma S4'teki tam sayı bölmesine dikkat.

---

Bitince: "bitti" de. Testlerini `./mvnw test` ile ben de çalıştırıp sonucu söyleyeceğim.
