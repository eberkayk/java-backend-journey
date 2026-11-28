# Hafta 7 Sınavı — Gün 43 (HTTP/REST + JUnit + SOLID)

Başlangıç saati:
Bitiş saati:

Kapalı kitap. Not, eski kod, internet, AI yok. Sadece `Cevap:` satırlarını doldur.

---

## Bölüm A — Teori (her biri 1–3 cümle) · 25 puan

**A1. (3p)** Bir HTTP **isteği** hangi 4 parçadan oluşur? Bir HTTP **cevabı** hangi 3 parçadan?
Cevap: istek: metot, body, header, location 
cevap: status code, body, header

**A2. (4p)** Bir todo API'si için tabloyu doldur: hangi **metod**, hangi **URL**, başarılı olunca hangi **status**?

| İş                      | Metod | URL | Başarılı status |
| ----------------------- | ----- | --- | --------------- |
| bütün todo'ları listele |  GET  |  /todos/   |  200 OK               |
| id'si 3 olanı getir     |   GET    | /todos/3/    | 200 OK                |
| yeni todo oluştur       |  POST     |  /todos/   |   201 Created              |
| id'si 3 olanı sil       |   DELETE    |   /todos/3/  |  204 No Content               |

Yeni todo oluşturunca cevaba hangi **header** eklenir, içinde ne yazar?
Cevap: application-json, eklenen todo yazar

**A3. (3p)** `400`, `404`, `500` — her biri ne zaman döner? Hangisinde hata **istemcinin**, hangisinde **sunucunun**?
Cevap: 400 bad request, 404 not found, 500 internal server error, 400 ve 404 istemcinin, 500 sunucunun

**A4. (3p)** "Idempotent" ne demek? Aynı `DELETE /todos/3` isteği iki kez atılınca önce `204`, sonra `404` geliyor. `DELETE` yine de idempotent sayılır mı, neden?
Cevap: idempotent bir isteğin birden fazla kez atıldığında sonucu değiştirmemesine denir. önce 204 sonra 404 gelmesine rağmen DELETE yine de idempotent'tir, çünkü sonu. değişmez kod değişse de, bir şey silinmişse silinmiştir.

**A5. (3p)** Unit test nedir (1 cümle)? `@BeforeEach` ne işe yarar, neden her test **kendi** yeni nesnesiyle başlamalı?
Cevap: kodlarımızın bir birimini yani en küçük parçalarını metotlarını doğru çalışıp çalışmadığını görmemiz için yazılan testlerdir. @BeforeEach bir kaç testte ortak kullanılacak olan bir yapıyı, önceden kurar ve her testte yeniden kurulmasına gerek kalmaz.
her test kendi yeni nesnesiyle başlamalıdır çünkü bir testin sonucunda değişen durumlar diğer testleri etkileyebilir.

**A6. (3p)** Bir projede coverage **%70**. Bu sayı neyi ölçer? Coverage **%100** olsaydı "kodda hiç bug yok" diyebilir miydik, neden?
Cevap: testlerde projedeki kodların %70'inin çalıştırıldığını gösterir. %100 olsaydı dda hiç bug yok diyemezdik çünkü, kodu çalıştırıp bir durumda doğru sonuca ulaştırabiliriz ama başka durumlarda doğru sonuca ulaşamayabilir, o yüzden coverage'a göre değil dahil ettiğimiz durumlara göre bug olup olmama ihtimalini söyleyebiliriz.

**A7. (3p)** Bu sınıf SOLID'in hangi harfini çiğniyor? Nasıl düzeltirsin (alan tipi + constructor)?
```java
public class ReportService {
    private final PostgresExpenseRepository repo = new PostgresExpenseRepository();

    public double monthlyTotal(YearMonth m) { ... repo kullanır ... }
}
```
Cevap: D'y, çiğniyor. somut sınıfa değil interface'e bağlan nesneyi constructor ile dışarıdan ver.

**A8. (3p)** Her satır hangi design pattern? (Builder / Strategy / Factory / Singleton)
```java
// a
HttpRequest req = HttpRequest.newBuilder().uri(uri).GET().build();
// b
list.sort(Comparator.comparing(Todo::title));
// c
List<Integer> nums = List.of(1, 2, 3);
```
Cevap: a builder · b strategy · c factory

---

## Bölüm B — Çıktı tahmini (ÇALIŞTIRMADAN) · 25 puan (her blok 5p)

**B1.** Senin `todo-server`'ın **yeni açıldı**: içinde id **1, 2, 3** var. Komutlar bu sırayla atılıyor.
Her satır için status kodunu yaz. Dolu cevap dönen POST'larda dönen `id`'yi de yaz.

```
1) GET    /todos/2
2) POST   /todos      {"title": "Süt al", "userId": 1}
3) POST   /todos      {"title": "   ",    "userId": 1}
4) DELETE /todos/4
5) DELETE /todos/4
6) POST   /todos      {"title": "Ekmek",  "userId": 2}
7) GET    /todos/abc
8) PUT    /todos/1
```
Cevap:
1) 200 OK id: 2
2) 201 Created id: 4
3) 400 Bad Request (title'ı boş olan todo kabul edilmiyordu diye hatırlıyorum ona göre cevap verdim)
4) 204 No Content
5) 404 Not Found
6) 201 Created id: 4
7) 400 Bad Request
8) 400 Bad Request

**B2.** Her test için `PASS` ya da `FAIL` yaz. `FAIL` olanların sebebini kısaca yaz; `t2` için JUnit'in mesajını (`expected: <?> but was: <?>`) tam yaz.
```java
class Counter {
    private int n;
    void inc() { n++; }
    int get()  { return n; }
}

class CounterTest {
    private Counter c;

    @BeforeEach
    void setUp() { c = new Counter(); }

    @Test void t1() { c.inc(); c.inc(); assertEquals(2, c.get()); }
    @Test void t2() { c.inc();          assertEquals(c.get(), 2); }
    @Test void t3() { assertThrows(IllegalStateException.class, () -> c.get()); }
}
```
Cevap:
t1: PASS
t2: FAIL expected: <1> but was: <2>
t3: FAIL c.get() IllegalStateException fırlatmaz, n döndürür.

**B3.** Çıktı ne? (4 satır)
```java
static int parse(String s) {
    try {
        return Integer.parseInt(s);
    } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Bad: " + s, e);
    }
}

// main içinde:
System.out.println(parse("-5"));
try {
    parse("12a");
} catch (IllegalArgumentException e) {
    System.out.println(e.getMessage());
    System.out.println(e.getCause().getClass().getSimpleName());
    System.out.println(e.getCause() == null);
}
```
Cevap: -5
Bad: 12a
getCause ne bilmiyorum ilk defa gördüm.

**B4.** Çıktı ne? (tek satır)
```java
int a = 7, b = 2;
double x = a / b;
double y = a / 2.0;
int p1 = 3 * 100 / 7;
int p2 = 3 / 7 * 100;
System.out.println(x + " " + y + " " + p1 + " " + p2);
```
Cevap: 3 3.5 42 0

**B5.** Çıktı ne? Exception olan satırda exception'ın **adını** yaz.
```java
ArrayDeque<Integer> st = new ArrayDeque<>();
st.push(1); st.push(2); st.push(3);
System.out.println(st.pop() + " " + st.peek());     // satır 1

Queue<Integer> q = new ArrayDeque<>();
q.add(1); q.add(2); q.add(3);
System.out.println(q.poll() + " " + q.peek());      // satır 2

ArrayDeque<Integer> e = new ArrayDeque<>();
System.out.println(e.peek());                       // satır 3
System.out.println(e.pop());                        // satır 4
```
Cevap:
satır 1: 3 2
satır 2: 1 2
satır 3: NPE
satır 4: NPE

---

## Bölüm C — Sıfırdan kod · 50 puan

Spec `DERS.md`'de. Kod `exam/` projesinde. Burada sadece spec ritüeli:

```
C3 add:       [ ] blank title   [ ] priority aralığı   [ ] id 1'den başlar   [ ] hata id harcamaz
C3 findAll:   [ ] id sırası     [ ] kopya liste
C3 delete:    [ ] true / false
C4 ctor:      [ ] repo dışarıdan
C4a top:      [ ] n<0           [ ] n==0               [ ] n>sayı            [ ] sıra   [ ] depo bozulmaz
C4b require:  [ ] bulunursa     [ ] NoteNotFoundException + mesaj
C4c parse:    [ ] sayı          [ ] mesaj              [ ] cause
C5 test:      [ ] @BeforeEach   [ ] T1                 [ ] T2                [ ] mvnw test yeşil
```
