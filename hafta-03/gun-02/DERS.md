# Hafta 3 · Gün 2 (Gün 14) — Collections 1: `List` ve `ArrayList`

**Hedef:** Kütüphanedeki `Book[]` + `bookCount` ikilisini atıp yerine `List<Book>` koymak. Bugün sonunda "Library is full" hatası tamamen yok olacak, `booksByAuthor` iki tur yerine tek turda bitecek.

**Süre:** ~6 saat · Isınma 15 dk · Ders 1.5 sa · Egzersiz 1 sa · Proje 2 sa · Algoritma 45 dk · Kapanış 20 dk

**Kurallar (değişmedi):**
- Sınıf, metot, değişken ve ekran mesajları **İngilizce**
- **Değişiklik → derle → çalıştır → çıktının her satırını oku.**
- **Metot adı/imzası görevde nasılsa öyle.**
- Tahminler **çalıştırmadan önce** chat'e.
- Testleri Claude yazıp çalıştırıyor. Sen yazmıyorsun.

---

## 0. Isınma (15 dk) — notlara bakmadan, cevapları chat'e

1. Checked ve unchecked exception farkı ne? Birer örnek ver.
2. `throw` ile `throws` farkı?
3. `try` içinde 2. satır exception fırlattı, hiçbir `catch` uymadı. `finally` çalışır mı? `try-catch`'in altındaki satır çalışır mı?
4. `Library` neden kendi exception'larını kendisi yakalamıyor?
5. (Tekrar soru) `x.equals(z)` çağrısında Java hangi `equals`'ı çalıştıracağına karar verirken **`x`'in** gerçek tipine mi bakar, **`z`'nin** gerçek tipine mi? (Bir cümle.)

---

## 1. Sorun: dizinin boyu sabit ⭐⭐ (5 dk)

Dünkü `Library`'de bu üç şey vardı:

```java
private Book[] books;          // boyu constructor'da sabitleniyor
private int bookCount;         // kaç tanesi dolu, elle takip
...
if (bookCount >= bookCapacity) throw new IllegalStateException("Library is full");
```

Ve `booksByAuthor` içinde **iki tur** döndün: önce say (dizinin boyunu bilmek için), sonra doldur.

Gerçek bir kütüphane "10 kitaptan sonra dolu" demez. İstediğimiz şey: **gerektikçe büyüyen** ve sayacı **kendisi tutan** bir dizi. Java'da bunun adı `ArrayList`.

---

## 2. `ArrayList` ile tanışma ⭐⭐⭐ (20 dk)

```java
import java.util.ArrayList;
import java.util.List;

List<String> names = new ArrayList<>();
names.add("Ali");
names.add("Ayse");
System.out.println(names);         // [Ali, Ayse]   ← toString'i hazır
System.out.println(names.size());  // 2
System.out.println(names.get(0));  // Ali
```

Satır satır:
- `List<String>`: "içinde **sadece** `String` olan bir liste". `<...>` içine yazılan tipe **generic tip** denir (Hafta 3'te ayrıca göreceğiz). `names.add(5)` derlenmez.
- `new ArrayList<>()`: sağdaki `<>` boş bırakılır (diamond), Java soldan anlar.
- Neden solda `List`, sağda `ArrayList`? `List` bir **interface**, `ArrayList` onu implement eden bir class (Gün 10: `Payable p = new Invoice(...)`). Değişkeni interface tipinde tutarsan yarın `ArrayList` yerine başka bir `List` koyduğunda başka hiçbir satır değişmez. **Kural:** field, parametre ve dönüş tipi `List`; sadece `new` yazarken `ArrayList`.

### En çok kullanacağın metotlar

| Metot              | Ne yapar                                                     | Dizideki karşılığı          |
| ------------------ | ------------------------------------------------------------ | --------------------------- |
| `add(x)`           | Sona ekler                                                   | `arr[count] = x; count++;`  |
| `add(i, x)`        | `i`'ye ekler, sonrakileri bir sağa kaydırır                  | elle kaydırma döngüsü       |
| `get(i)`           | `i`'deki eleman                                              | `arr[i]`                    |
| `set(i, x)`        | `i`'deki elemanı değiştirir                                  | `arr[i] = x`                |
| `size()`           | Eleman sayısı                                                | `count` (`length` değil!)   |
| `isEmpty()`        | `size() == 0`                                                | `count == 0`                |
| `contains(x)`      | Var mı? **`equals` ile** karşılaştırır                       | döngü + `equals`            |
| `indexOf(x)`       | İlk eşleşenin index'i, yoksa `-1` (**`equals` ile**)         | döngü + `equals`            |
| `remove(int i)`    | `i`'deki elemanı siler, **onu döner**, sağdakiler sola kayar | elle kaydırma döngüsü       |
| `remove(Object x)` | `equals`'ı tutan **ilk** elemanı siler, `true`/`false` döner | döngü + `equals` + kaydırma |
| `clear()`          | Hepsini siler                                                | `count = 0`                 |

**Dikkat:** `contains`, `indexOf`, `remove(Object)` hep **`equals`** kullanır. Gün 12'de `Book.equals`'ı ISBN'e göre yazdın; şimdi karşılığını alıyorsun: başka başlıklı ama aynı ISBN'li bir `Book` için `contains` → `true`.

### Liste üzerinde dolaşmak

```java
for (String name : names) {          // for-each, index gerekmiyorsa bunu kullan
    System.out.println(name);
}

for (int i = 0; i < names.size(); i++) {   // index lazımsa
    System.out.println(i + ": " + names.get(i));
}
```

### Hatalı index

```java
names.get(5);   // IndexOutOfBoundsException: Index 5 out of bounds for length 2
```
Dizideki `ArrayIndexOutOfBoundsException`'ın listedeki kardeşi. İkisi de `RuntimeException` (dünkü ağaç).

---

## 3. `int` listeye giremez: wrapper class'lar ⭐⭐ (10 dk)

```java
List<int> nums = new ArrayList<>();       // DERLENMEZ: generic tip bir class olmalı
List<Integer> nums = new ArrayList<>();   // doğru
nums.add(5);                              // Java 5'i otomatik Integer'a sarar (autoboxing)
int x = nums.get(0);                      // ve geri açar (unboxing)
```

| Primitive | Wrapper     |
| --------- | ----------- |
| `int`     | `Integer`   |
| `double`  | `Double`    |
| `char`    | `Character` |
| `boolean` | `Boolean`   |
| `long`    | `Long`      |

İki tuzak:
1. `Integer` bir nesne. İki `Integer`'ı `==` ile karşılaştırma, **`equals`** kullan (String'deki kuralın aynısı, Gün 5).
2. `List<Integer>`'da `remove(1)` ne siler: **1. index'teki** elemanı mı, **değeri 1 olan** elemanı mı? (E2'de göreceksin.)

---

## 4. İçeride ne var? ⭐⭐⭐ (15 dk)

`ArrayList` sihir değil. İçinde **senin dün yazdığın şey** var:

```java
// ArrayList'in içi (sadeleştirilmiş)
private Object[] elementData;   // asıl dizi
private int size;               // kaç tanesi dolu
```

`add` çağırdığında dizi doluysa:
1. **1.5 kat** büyük yeni bir dizi yaratır (10 → 15 → 22 → 33 ...)
2. Eski elemanları yeni diziye kopyalar
3. Eskisini bırakır (garbage collector toplar)

Yani "Library is full" yerine "dolduysa büyüt". Kopyalama pahalı ama nadiren olur; ortalamada `add` hızlı kalır.

**Hangi işlem ne kadar sürer (n = eleman sayısı):**

| İşlem                       | Süre          | Neden                                    |
| --------------------------- | ------------- | ---------------------------------------- |
| `get(i)`, `set(i, x)`       | O(1)          | Dizide index'e direkt erişim             |
| `add(x)` (sona)             | O(1) ortalama | Arada bir büyütme/kopyalama              |
| `add(0, x)`, `remove(0)`    | O(n)          | Herkesi bir kaydırmak gerekiyor          |
| `contains(x)`, `indexOf(x)` | O(n)          | Baştan sona `equals` ile tek tek bakıyor |

Mülakat sorusu: **"ArrayList'te ortadan eleman silmek neden yavaş?"** Cevabı bu tablo.

`contains` O(n). 1 milyon kitapta ISBN aramak için bu yavaş. Yarın `HashMap` ile bunu O(1)'e indireceğiz (Gün 12'deki kutu modeli).

---

## 5. Döngü içinde silme yasak ⭐⭐ (10 dk)

```java
for (String name : names) {
    if (name.startsWith("A")) {
        names.remove(name);   // ConcurrentModificationException
    }
}
```

for-each, listeyi dolaşırken listenin **değişmediğini** varsayar. İçeride silersen sonraki adımda bunu fark eder ve exception fırlatır (E3).

**Doğru yol** (şimdilik bu satırı kalıp olarak kullan):
```java
names.removeIf(name -> name.startsWith("A"));
```
`name -> name.startsWith("A")` bir **lambda**: "her eleman için bu koşulu sor, `true` olanları sil". Lambda'yı Hafta 4'te detaylı göreceğiz; bugün sadece bu kalıp.

---

## 6. Listeyi dışarı vermek ⭐⭐⭐ (15 dk)

```java
List<String> fixed = List.of("a", "b", "c");   // hızlı liste yaratma
fixed.add("d");                                // UnsupportedOperationException
```

`List.of(...)` **değiştirilemez** (immutable) bir liste döner. `add`, `remove`, `set` hepsi exception.

Bu neden işe yarar? Şu kodu düşün:

```java
public class Library {
    private final List<Book> books = new ArrayList<>();

    public List<Book> getAllBooks() {
        return books;            // ⚠️ kendi listemizin ADRESİNİ veriyoruz
    }
}

// LibraryApp'te:
library.getAllBooks().clear();   // Library'nin içindeki tüm kitaplar silindi!
```

`books` `private` ama adresini verdin, dışarıdaki herkes içini değiştirebilir (Gün 3 ev/adres benzetmesi). `private` alanı korumanın anlamı kalmadı.

Çözüm: **kopya** ver.
```java
public List<Book> getAllBooks() {
    return List.copyOf(books);   // yeni, değiştirilemez bir liste
}
```
Artık dışarıdaki `clear()` → `UnsupportedOperationException`, `Library`'nin listesi güvende. (Dün `getAllBooks`'ta yeni dizi yaratıp kopyalamıştın, aynı fikir.)

**`final` notu:** `private final List<Book> books` → `books` değişkeni **başka bir listeyi** gösteremez. Ama listenin **içine** `add` yapılabilir. `final` adresi kilitler, içeriği değil (Gün 7).

---

## 7. Liste içinde liste ⭐ (5 dk) — sadece A2 için

```java
List<List<Integer>> rows = new ArrayList<>();
List<Integer> row = new ArrayList<>();
row.add(1);
rows.add(row);
System.out.println(rows);              // [[1]]
System.out.println(rows.get(0).get(0)); // 1
```
İki boyutlu dizinin (`int[][]`, Gün 4) listedeki karşılığı.

---

## 📝 Kağıda Yaz

```
List<Book> books = new ArrayList<>();   solda List, sağda ArrayList
size()  (length değil)  ·  get(i)  ·  add(x)  ·  remove(i) / remove(obj)
contains / indexOf / remove(obj) → equals kullanır
List<int> olmaz → List<Integer>  ·  Integer'lar equals ile
ArrayList içi = Object[] + size, doluysa 1.5x büyür
get O(1) · add sona O(1) · ortadan sil/ekle O(n) · contains O(n)
for-each içinde remove → ConcurrentModificationException → removeIf
Dışarı liste verirken: List.copyOf(...)
```

---

## 💪 EGZERSİZLER (~1 saat)

Paket: `w3d2`. Klasör: `hafta-03/gun-02/w3d2/`.

### E1 — `ListBasics.java` (tahmin)

Kodu yaz, **çalıştırmadan önce** her `println`'ın çıktısını chat'e yaz.

```java
List<String> names = new ArrayList<>();
names.add("Ali");
names.add("Ayse");
names.add("Can");
names.add(1, "Zeynep");
System.out.println(names);                      // 1
System.out.println(names.size());               // 2
System.out.println(names.get(2));               // 3
names.set(0, "Veli");
System.out.println(names);                      // 4
System.out.println(names.remove("Ayse"));       // 5
System.out.println(names.remove("Mert"));       // 6
System.out.println(names.indexOf("Can"));       // 7
System.out.println(names.contains("Zeynep"));   // 8
System.out.println(names.get(3));               // 9
```

### E2 — `RemoveTrap.java` (tahmin)

```java
List<Integer> nums = new ArrayList<>(List.of(10, 20, 30, 40));
nums.remove(1);
System.out.println(nums);                 // 1
nums.remove(Integer.valueOf(40));
System.out.println(nums);                 // 2
nums.remove(30);
System.out.println(nums);                 // 3
```
(`new ArrayList<>(List.of(...))`: değiştirilemez listeden **değiştirilebilir** bir kopya yaratır.)

Çalıştırdıktan sonra chat'e bir cümle: `nums.remove(30)` neden "değeri 30 olanı sil" anlamına gelmedi? (İpucu: `remove(int)` ve `remove(Object)` iki **overload**. Java `30` için hangisini seçer? Gün 3 overload.)

### E3 — `ModifyWhileLoop.java` (tahmin + düzelt)

```java
List<String> names = new ArrayList<>(List.of("Ali", "Ayse", "Can", "Aras"));
for (String name : names) {
    if (name.startsWith("A")) {
        names.remove(name);
    }
}
System.out.println(names);
```
1. Ne olur? Chat'e tahmin, sonra çalıştır.
2. Döngüyü sil, yerine bölüm 5'teki `removeIf` satırını koy. Çıktı?

### E4 — `ContainsDemo.java` (tahmin)

`w3d1`'deki `Book`'u `w3d2`'ye kopyala (paket satırını değiştir).

```java
List<Book> books = new ArrayList<>();
books.add(new Book("111", "Dune", "Frank Herbert"));
System.out.println(books.contains(new Book("111", "Another Title", "Someone")));  // 1
System.out.println(books.indexOf(new Book("222", "Dune", "Frank Herbert")));      // 2

List<Book> view = List.copyOf(books);
view.add(new Book("333", "Emma", "Jane Austen"));                                 // 3: ne olur?
```
Sonra `Book`'taki `equals` metodunu geçici olarak yorum satırı yap, sadece 1. satırı tekrar tahmin et ve çalıştır. Bitince `equals`'ı geri aç.

---

## 🏛️ PROJE — Kütüphane v4: `List` ile (~2 saat)

### Adım 0 — Kopyala
`hafta-03/gun-01/w3d1/` içinden şunları `hafta-03/gun-02/w3d2/` içine kopyala, her dosyada `package w3d1;` → `package w3d2;`:
`Book` (E4'te zaten kopyaladın), `Member`, `StudentMember`, `StaffMember`, `Library`, `LibraryApp` ve **6 exception class'ı**.
Derle, çalıştığından emin ol. `Book`, `Member`, alt class'lar ve exception'lar bugün **değişmiyor**.

### Adım 1 — `Library`'nin yeni hâli

**Field'lar** (eski 6 field'ın **hepsini sil**, yerine sadece bunlar):
```java
private final List<Book> books = new ArrayList<>();
private final List<Member> members = new ArrayList<>();
```
`bookCapacity`, `memberCapacity`, `bookCount`, `memberCount` yok. Sayacı liste tutuyor.

**Constructor:** `public Library()`, parametresiz. İçi boş (ya da hiç yazma, Java parametresiz olanı kendisi verir; bu senin tasarım kararın).

**Metotlar** (tablodaki imzalar aynen):

| Metot                                                  | Değişiklik                                                                 |
| ------------------------------------------------------ | -------------------------------------------------------------------------- |
| `public void addBook(Book book)`                       | "Library is full" kontrolü **silinir**. Duplicate kontrolü `contains` ile. |
| `public void addMember(Member member)`                 | Aynısı: "Member list is full" silinir, duplicate `contains` ile.           |
| `public Book findBook(String isbn)`                    | for-each ile. Bulamazsa yine `BookNotFoundException`.                      |
| `public Member findMember(String id)`                  | for-each ile. Bulamazsa yine `MemberNotFoundException`.                    |
| `public void borrowBook(String memberId, String isbn)` | **Değişmez.**                                                              |
| `public void returnBook(String memberId, String isbn)` | **Değişmez.**                                                              |
| `public int availableCount()`                          | for-each ile.                                                              |
| `public List<Book> booksByAuthor(String author)`       | Dönüş tipi `Book[]` → `List<Book>`. **Tek döngü**, sayma turu yok.         |
| `public List<Book> getAllBooks()`                      | Dönüş tipi `Book[]` → `List<Book>`. `List.copyOf(books)` döner.            |
| `public void removeBook(String isbn)`                  | **YENİ** (aşağıda)                                                         |
| `public List<Book> booksBorrowedBy(String memberId)`   | **YENİ** (aşağıda)                                                         |

**`addBook(Book book)`**, kontroller bu sırayla:
1. `book == null` → `IllegalArgumentException("Book must not be null")`
2. `books.contains(book)` → `IllegalArgumentException("Duplicate ISBN: " + book.getIsbn())`
3. Ekle.

**`addMember(Member member)`**: aynı yapı. Mesajlar: `"Member must not be null"`, `"Duplicate member id: " + member.getId()`.

**YENİ `removeBook(String isbn)`**, kontroller bu sırayla:
1. Kitap yok → `BookNotFoundException` (`findBook` zaten fırlatıyor)
2. Kitap ödünçte → `IllegalStateException("Book is borrowed: " + isbn)`
3. Listeden sil. `remove(int)` mi `remove(Object)` mi kullanacağın senin tasarım kararın (E2'yi hatırla).

**YENİ `booksBorrowedBy(String memberId)`**:
1. Üye yok → `MemberNotFoundException` (`findMember` fırlatıyor; listede hiç kitabı olmayan **var olan** üye için boş liste dönmeli, olmayan üye için exception)
2. Ödünç alanı bu üye olan kitapları yeni bir `List<Book>`'a topla ve dön.

**Yasak (değişmedi):** `Library` içinde `try`/`catch` yok.

### Adım 2 — `LibraryApp` güncelle

1. `new Library(10, 10)` → `new Library()`.
2. Seçenek 1 (List all) ve 5 (Search by author): `Book[]` → `List<Book>`. Döngüleri for-each yap. Seçenek 5'te `found.length == 0` yerine `isEmpty()`.
3. Seçenek 2 (Add book): multi-catch'te `IllegalStateException`'a artık gerek var mı? Chat'e bir cümle, gerekmiyorsa sil.
4. **Yeni seçenek 6) Remove book**: ISBN sor → `removeBook`. Başarılı → `"Book removed"`. Hata → `"Error: " + e.getMessage()`. Hangi exception tiplerini yakalaman gerektiğine sen karar ver (`removeBook` hangilerini fırlatabiliyor?).
5. **Yeni seçenek 7) Books of member**: üye id sor → `booksBorrowedBy`. Boşsa `"No books borrowed"`, değilse her kitabı ayrı satıra yazdır. Hata → `"Error: " + e.getMessage()`.
6. Menü metnine 6 ve 7'yi ekle.

Bitince haber ver; testleri ben yazıp çalıştıracağım, sonucu söyleyeceğim.

### Proje sonu düşünme soruları (chat'e, 1-2 cümle)
1. `getAllBooks` `books`'u değil `List.copyOf(books)`'u dönüyor. `LibraryApp`'te `library.getAllBooks().clear();` yazsan, iki durumda ayrı ayrı ne olurdu?
2. `addBook`'taki duplicate kontrolü `books.contains(book)`. `Book.equals`'ı silsen bu kontrol hâlâ çalışır mıydı? Neden? (E4'te gördün.)

---

## 🧩 ALGORİTMA SAATİ (45 dk) — `List` dönen problemler

**Yöntem (değişmedi):** imza kağıda → 3-5 Türkçe cümle → `w3d2/Algo1.java`, `Algo2.java` → LeetCode.

**A1 — [LeetCode 448: Find All Numbers Disappeared in an Array](https://leetcode.com/problems/find-all-numbers-disappeared-in-an-array/)**
`List<Integer> findDisappearedNumbers(int[] nums)`: `nums`'ın boyu `n`, içindeki her sayı `1` ile `n` arasında, bazıları tekrarlıyor. `1..n` arasında dizide **hiç geçmeyen** sayıları liste olarak dön.
- Örnek: `[4,3,2,7,8,2,3,1]` → `[5,6]`
- İpucu: Gün 6'daki **sayma dizisi** kalıbı. Kaç elemanlı bir sayma dizisi lazım? Index'i ne olacak?
- Dikkat: dönen değer **sayı**, index değil. (Senin bilinen tuzağın: index/değer karışması.)

**A2 — [LeetCode 118: Pascal's Triangle](https://leetcode.com/problems/pascals-triangle/)**
`List<List<Integer>> generate(int numRows)`: Pascal üçgeninin ilk `numRows` satırı.
- Örnek: `numRows = 5` → `[[1],[1,1],[1,2,1],[1,3,3,1],[1,4,6,4,1]]`
- Kural: her satırın ilk ve son elemanı `1`. Aradaki her eleman = **bir üst satırda** solundaki + tam üstündeki.
- Önce kağıda: 4. satırı (`[1,3,3,1]`) 3. satırdan nasıl ürettiğini index'lerle yaz (`row[1] = prev[0] + prev[1]` gibi). Sonra kodla.
- Bölüm 7'ye bak.

---

## 🔧 GIT (10 dk)

```bash
git add hafta-03/gun-02
git status            # sadece .java ve DERS.md (.class yok)
git commit -m "feat: week 3 day 2 - lists"
git push
```
Sonra `ILERLEME.md`'ye Gün 14 logu → `docs: day 14 progress log`.

---

## ✅ Gün Sonu

- [ ] Isınma (5 soru, chat'e)
- [ ] E1–E4 (tahminler **çalıştırmadan önce** chat'e)
- [ ] Proje Adım 0–2
- [ ] Proje düşünme soruları
- [ ] A1 + A2
- [ ] "Kağıda Yaz" kutusu kağıtta
- [ ] Commit + push, `ILERLEME.md` Gün 14

**Yarın oturum başında soracaklarım:**
1. `ArrayList`'in içinde ne var? Dolunca ne yapar?
2. `ArrayList`'te `get(i)` ve `remove(0)` neden farklı hızda?
3. `List<Integer>`'da `remove(1)` neyi siler?
4. `contains` iki nesnenin eşit olduğuna nasıl karar verir?
5. Bir `private` listeyi getter'dan olduğu gibi dönmek neden tehlikeli?

**Sıradaki:** Hafta 3 Gün 3: **`HashMap` ve `HashSet`**. `findBook` şu an O(n) döngü; yarın ISBN → `Book` haritasıyla O(1). Gün 12'deki kutu modeli geri geliyor.
