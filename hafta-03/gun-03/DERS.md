# Hafta 3 · Gün 3 (Gün 15) — Collections 2: `HashMap` ve `HashSet`

**Hedef:** `findBook` şu an bütün listeyi baştan sona geziyor (O(n)). Bugün sonunda ISBN'i verince kitabı **tek adımda** (O(1)) bulacak. Ayrıca "hangi yazardan kaç kitap var" sorusunu tek turda cevaplayacaksın.

**Süre:** ~6 saat · Isınma 15 dk · Ders 1.5 sa · Egzersiz 1 sa · Proje 2 sa · Algoritma 45 dk · Kapanış 20 dk

**Kurallar (değişmedi):**
- Sınıf, metot, değişken ve ekran mesajları **İngilizce**
- **Değişiklik → derle → çalıştır → çıktının her satırını oku.**
- **Metot adı/imzası görevde nasılsa öyle.**
- Tahminler **çalıştırmadan önce** chat'e.
- Testleri Claude yazıp çalıştırıyor. Sen yazmıyorsun.

---

## 0. Isınma (15 dk) — notlara bakmadan, cevapları chat'e

**S1.** Bu kodda 11. `add` çağrıldığında `ArrayList`'in içinde ne olur? (İçinde ne tür bir yapı var, dolunca ne yapıyor?)
```java
List<String> list = new ArrayList<>();   // içi 10 kişilik başlıyor
for (int i = 0; i < 11; i++) {
    list.add("x" + i);
}
```

**S2.** `list`'te 1 milyon eleman var. Hangisi yavaş, neden? (Bir cümle.)
```java
list.get(500000);
list.remove(0);
```

**S3.** Çıktı ne?
```java
List<Integer> nums = new ArrayList<>(List.of(5, 1, 7));
nums.remove(1);
System.out.println(nums);
```

**S4.** `Book.equals` sadece ISBN'e bakıyor. Çıktı ne, neden?
```java
List<Book> books = new ArrayList<>();
books.add(new Book("111", "Dune", "Frank Herbert"));
System.out.println(books.contains(new Book("111", "Emma", "Jane Austen")));
```

**S5.** `Library`'de getter böyle yazılmış olsaydı, `LibraryApp`'teki ikinci satır ne yapardı?
```java
public List<Book> getAllBooks() { return books; }      // Library içinde

library.getAllBooks().clear();                          // LibraryApp içinde
```

**S6.** (Tekrar soru, bu sefer kodlu) Ekrana ne yazar? Java hangi `equals`'ı seçti, neye bakarak?
```java
class A { public boolean equals(Object o) { System.out.println("A.equals"); return false; } }
class B extends A { public boolean equals(Object o) { System.out.println("B.equals"); return false; } }

Object x = new A();
Object z = new B();
x.equals(z);
```

---

## 1. Sorun: aramak hep O(n) ⭐⭐ (5 dk)

Dünkü `findBook`:

```java
for (Book book : books) {
    if (book.getIsbn().equals(isbn)) {
        return book;
    }
}
throw new BookNotFoundException(isbn);
```

1 milyon kitapta aradığın kitap sondaysa 1 milyon `equals`. İstediğimiz şey: "ISBN'i ver, kitabı **direkt** getir." Dizide `arr[i]` direkt geliyordu çünkü elinde **index** vardı. ISBN bir index değil, bir `String`.

Çözüm: `String`'i bir sayıya çevirip (hashCode) o sayıyı index gibi kullanmak. Gün 12'deki **kutu modeli**. Java'da adı `HashMap`.

---

## 2. `HashMap` ile tanışma ⭐⭐⭐ (25 dk)

`Map` = **anahtar → değer** eşlemesi (sözlük gibi: kelime → anlamı).

```java
import java.util.HashMap;
import java.util.Map;

Map<String, Integer> ages = new HashMap<>();
ages.put("Ali", 25);
ages.put("Ayse", 30);
System.out.println(ages.get("Ali"));      // 25
System.out.println(ages);                 // {Ayse=30, Ali=25}   ← sıra garanti DEĞİL
```

- `Map<String, Integer>`: anahtar tipi `String`, değer tipi `Integer`. İki generic tip var.
- Solda `Map`, sağda `HashMap` (dünkü `List`/`ArrayList` kuralının aynısı).
- Bir anahtar **en fazla bir kere** bulunur. Aynı anahtara tekrar `put` → eski değerin **üstüne yazar**.

### En çok kullanacağın metotlar

| Metot                 | Ne yapar                                                                      |
| --------------------- | ----------------------------------------------------------------------------- |
| `put(k, v)`           | Ekler. Anahtar zaten varsa üstüne yazar. **Eski değeri döner** (yoksa `null`) |
| `get(k)`              | Değeri döner. **Anahtar yoksa `null`** (exception değil!)                     |
| `getOrDefault(k, d)`  | Anahtar yoksa `null` yerine `d` döner                                         |
| `containsKey(k)`      | Anahtar var mı? O(1)                                                          |
| `containsValue(v)`    | Değer var mı? **O(n)**, bütün değerlere tek tek bakar                         |
| `remove(k)`           | Siler, **silinen değeri döner** (yoksa `null`)                                |
| `size()`, `isEmpty()` | Kaç anahtar var / boş mu                                                      |
| `keySet()`            | Bütün anahtarlar (`Set<K>`)                                                   |
| `values()`            | Bütün değerler (`Collection<V>`, for-each ile gezilir)                        |
| `entrySet()`          | Bütün (anahtar, değer) çiftleri                                               |

### Üç şekilde gezmek

```java
for (String name : ages.keySet()) {               // sadece anahtarlar
    System.out.println(name);
}

for (Integer age : ages.values()) {               // sadece değerler
    System.out.println(age);
}

for (Map.Entry<String, Integer> e : ages.entrySet()) {   // ikisi birden
    System.out.println(e.getKey() + " -> " + e.getValue());
}
```
`Map.Entry<String, Integer>` = "bir anahtar + bir değer" kutusu. `getKey()`, `getValue()`. İkisi birden lazımsa `entrySet` kullan; `keySet` gezip her seferinde `get` çağırmak gereksiz iş.

### ⚠️ `null` tuzağı

```java
Integer a = ages.get("Mert");   // null, sorun yok
int b = ages.get("Mert");       // NullPointerException!
```
İkinci satırda Java `null`'ı `int`'e açmaya (unboxing, dün) çalışıyor; `null`'ın içinde sayı yok → NPE. Anahtardan emin değilsen `getOrDefault` veya önce `null` kontrolü.

---

## 3. İçeride ne var? (Gün 12'deki kutular) ⭐⭐⭐ (15 dk)

```
put("Ali", 25):
  1) "Ali".hashCode()  → bir sayı
  2) sayı % kutuSayısı → hangi kutu (bucket)
  3) o kutuda equals ile "Ali" var mı? varsa değerini değiştir, yoksa ekle

get("Ali"):
  1) aynı hashCode → aynı kutu
  2) sadece O kutudaki birkaç elemana equals ile bak
```

Bütün listeyi değil, **tek bir kutuyu** geziyor → ortalama **O(1)**.

Buradan çıkan kurallar:
- Anahtar olarak kullandığın class'ın **`equals` ve `hashCode`'u birlikte doğru** olmalı (Gün 12 sözleşmesi). `String`, `Integer` zaten doğru. Kendi class'ını anahtar yaparsan ikisini de yazmalısın.
- `hashCode` bozuksa (hep aynı sayı) her şey tek kutuya düşer → yine O(n). Çalışır ama yavaş (Gün 12 E4, madde 1).
- Kutu fazla dolunca `HashMap` de `ArrayList` gibi **büyür** (kutu sayısını ikiye katlar, herkesi yeniden dağıtır).
- **Sıra yok.** Elemanlar hashCode'a göre kutulara dağılıyor, ekleme sırasına göre değil.

| İşlem                                   | `ArrayList` | `HashMap`     |
| --------------------------------------- | ----------- | ------------- |
| Index/anahtar ile getir                 | O(1)        | O(1) ortalama |
| "Bu var mı?" (`contains`/`containsKey`) | O(n)        | O(1) ortalama |
| Sil                                     | O(n)        | O(1) ortalama |
| Ekleme sırası korunur mu?               | Evet        | **Hayır**     |

**Ek bilgi (bugün kullanmayacaksın):** ekleme sırası lazımsa `LinkedHashMap`, anahtara göre sıralı lazımsa `TreeMap`. İkisi de `Map`; sadece `new` satırı değişir.

---

## 4. Sayma kalıbı: `int[26]`'nın büyüğü ⭐⭐⭐ (15 dk)

Gün 6'dan beri harf saymak için `int[26]` kullanıyorsun: `count[ch - 'a']++`. Ama ya kelime saymak istersen? `"the"`'yi index'e çeviremezsin. `Map` ile:

```java
Map<Character, Integer> count = new HashMap<>();
for (char ch : "banana".toCharArray()) {
    count.put(ch, count.getOrDefault(ch, 0) + 1);
}
System.out.println(count);   // {a=3, b=1, n=2}  (sıra farklı olabilir)
```

Satır satır: "şu ana kadarki sayıyı al (yoksa 0), 1 ekle, geri koy."

| `int[26]`                 | `Map<K, Integer>`                                 |
| ------------------------- | ------------------------------------------------- |
| `count[ch - 'a']++`       | `count.put(k, count.getOrDefault(k, 0) + 1)`      |
| Sadece küçük harf (0..25) | **Her şey** anahtar olabilir: kelime, yazar, sayı |
| Hiç görülmeyen = `0`      | Hiç görülmeyen = **anahtar yok** (`get` → `null`) |

Mülakatta "bir dizide en çok tekrar eden eleman", "iki kelime anagram mı", "ilk tekrar etmeyen karakter" sorularının hepsi bu kalıp.

---

## 5. `HashSet`: sadece anahtarlar ⭐⭐ (10 dk)

Gün 12'de LC217'de `HashSet` kullandın. Şimdi ne olduğu: **değeri olmayan bir `HashMap`**. Sadece "bu var mı?" sorusu için.

```java
import java.util.HashSet;
import java.util.Set;

Set<String> seen = new HashSet<>();
System.out.println(seen.add("java"));     // true  → eklendi
System.out.println(seen.add("java"));     // false → zaten vardı, eklenmedi
System.out.println(seen.contains("java")); // true, O(1)
System.out.println(seen.size());          // 1
```

- **Tekrar yok:** aynı eleman (equals'a göre) ikinci kez girmez. `add` bunu `false` dönerek söyler.
- **Index yok:** `seen.get(0)` diye bir şey yok. Sıra da yok. Gezmek için for-each.
- `contains` O(1). `List.contains` O(n) idi. "Bunu daha önce gördüm mü?" sorusu = `HashSet`.

| Ne lazım?                               | Hangisi              |
| --------------------------------------- | -------------------- |
| Sıralı eleman listesi, index ile erişim | `List` / `ArrayList` |
| "Bu var mı?" + tekrar olmasın           | `Set` / `HashSet`    |
| Anahtar ile değer bulma, sayma          | `Map` / `HashMap`    |

---

## 6. Anahtarı değiştirme ⭐ (5 dk)

```java
Map<Book, Integer> copies = new HashMap<>();
copies.put(book, 3);
// book'un ISBN'i bir şekilde değişseydi → hashCode değişir → artık YANLIŞ kutuya bakılır
copies.get(book);   // null!
```
Kural: **Map anahtarı (ve Set elemanı) içerdeyken, `equals`/`hashCode`'da kullanılan alanlar değişmemeli.** Senin `Book`'ta `isbn` `final`, `Member`'da `id` `final`, yani güvendesin. Bu yüzden anahtar olarak çoğu zaman `String`/`Integer` gibi değişmeyen tipler kullanılır. Bugün projede anahtar `String` (ISBN).

---

## 📝 Kağıda Yaz

```
Map<String, Book> m = new HashMap<>();   anahtar → değer
put: varsa üstüne yazar, eski değeri döner · get: yoksa null (exception değil)
int x = map.get(yok) → NPE (null unboxing) → getOrDefault
HashMap içi: hashCode → kutu → equals ile ara → ortalama O(1), SIRA YOK
Anahtar class'ı: equals + hashCode birlikte · anahtar içerdeyken değişmesin
Sayma: map.put(k, map.getOrDefault(k, 0) + 1)
Set = sadece anahtarlar: add tekrar ise false · contains O(1)
List: sıra/index · Set: var mı/tekrar yok · Map: anahtarla bul/say
```

---

## 💪 EGZERSİZLER (~1 saat)

Paket: `w3d3`. Klasör: `hafta-03/gun-03/w3d3/`.

### E1 — `MapBasics.java` (tahmin)

Kodu yaz, **çalıştırmadan önce** her satırın çıktısını chat'e yaz.

```java
Map<String, Integer> stock = new HashMap<>();
stock.put("apple", 5);
stock.put("pear", 3);
System.out.println(stock.get("apple"));            // 1
System.out.println(stock.get("banana"));           // 2
System.out.println(stock.put("apple", 10));        // 3
System.out.println(stock.get("apple"));            // 4
System.out.println(stock.size());                  // 5
System.out.println(stock.containsKey("pear"));     // 6
System.out.println(stock.containsValue(3));        // 7
System.out.println(stock.remove("pear"));          // 8
System.out.println(stock.remove("pear"));          // 9
System.out.println(stock.getOrDefault("kiwi", 0)); // 10
int kiwi = stock.get("kiwi");                      // 11: ne olur?
System.out.println(kiwi);
```

### E2 — `WordCount.java` (kod yaz)

**Metot:** `public static Map<String, Integer> countWords(String text)`
- `text`'i boşluklara göre böl (`text.split(" ")`, Gün 5).
- Her kelimenin kaç kere geçtiğini bölüm 4'teki kalıpla say, map'i dön.

**`main`:**
1. `countWords("the cat and the hat and the bat")` çağır, dönen map'i `System.out.println` ile yazdır.
2. Aynı map'i `entrySet` ile gez, her satıra `the: 3` formatında yazdır.

Çalıştırdıktan sonra chat'e bir cümle: çıktıdaki kelime sırası cümledeki sırayla aynı mı? Neden?

### E3 — `SetDemo.java` (tahmin + deney)

`w3d2`'deki `Book`'u `w3d3`'e kopyala (paket satırını değiştir). Çalıştırmadan önce tahminleri chat'e.

```java
Set<String> tags = new HashSet<>();
System.out.println(tags.add("java"));       // 1
System.out.println(tags.add("spring"));     // 2
System.out.println(tags.add("java"));       // 3
System.out.println(tags.size());            // 4
tags.remove("java");
System.out.println(tags);                   // 5

Set<Book> books = new HashSet<>();
books.add(new Book("111", "Dune", "Frank Herbert"));
System.out.println(books.add(new Book("111", "Emma", "Jane Austen")));  // 6
System.out.println(books.size());                                       // 7
```

**Deney:** `Book`'taki **sadece `hashCode`** metodunu yorum satırı yap (`equals` kalsın). 6 ve 7'yi tekrar tahmin et, çalıştır. Sonucu bölüm 3'teki kutu modeliyle bir cümleyle açıkla. Bitince `hashCode`'u geri aç.

---

## 🏛️ PROJE — Kütüphane v5: `Map` ile (~2 saat)

### Adım 0 — Kopyala
`hafta-03/gun-02/w3d2/` içinden şunları `hafta-03/gun-03/w3d3/` içine kopyala, her dosyada `package w3d2;` → `package w3d3;`:
`Member`, `StudentMember`, `StaffMember`, `Library`, `LibraryApp` ve **6 exception class'ı** (`Book`'u E3'te zaten kopyaladın).
Derle, çalıştığından emin ol. `Book`, `Member`, alt class'lar ve exception'lar bugün **değişmiyor**.

### Adım 1 — `Library`'nin yeni hâli

**Field'lar** (iki `List` field'ını **sil**, yerine sadece bunlar):
```java
private final Map<String, Book> books = new HashMap<>();       // anahtar: ISBN
private final Map<String, Member> members = new HashMap<>();   // anahtar: üye id
```

**Constructor:** `public Library()`, gövdesi boş (değişmedi).

**Metotlar** (imzalar dünküyle **aynı**, sadece gövdeler değişiyor; atılan exception'lar ve mesajlar da aynı):

| Metot                                           | Ne değişiyor                                                                                                                       |
| ----------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------- |
| `void addBook(Book book)`                       | `null` kontrolü aynı. Duplicate kontrolü artık `contains` değil, **anahtar var mı** sorusu. Ekleme: anahtar = ISBN, değer = kitap. |
| `void addMember(Member member)`                 | Aynı mantık, anahtar = üye id.                                                                                                     |
| `Book findBook(String isbn)`                    | **Döngü yok.** Map'ten tek adımda al. Yoksa (`get` ne döner? E1 satır 2) → `BookNotFoundException`.                                |
| `Member findMember(String id)`                  | Aynı mantık → `MemberNotFoundException`.                                                                                           |
| `void borrowBook(String memberId, String isbn)` | **Değişmiyor** (zaten `find*` çağırıyor).                                                                                          |
| `void returnBook(String memberId, String isbn)` | **Değişmiyor.**                                                                                                                    |
| `int availableCount()`                          | Döngü artık map'in **değerleri** üzerinde.                                                                                         |
| `List<Book> booksByAuthor(String author)`       | Döngü map'in değerleri üzerinde. Dönüş tipi hâlâ `List<Book>`.                                                                     |
| `List<Book> getAllBooks()`                      | Map'in değerlerinden değiştirilemez bir **liste** dön (`List.copyOf` bir `Collection` da kabul eder).                              |
| `void removeBook(String isbn)`                  | `findBook` + ödünç kontrolü aynı. Silme artık **anahtar ile**.                                                                     |
| `List<Book> booksBorrowedBy(String memberId)`   | Döngü map'in değerleri üzerinde.                                                                                                   |

### Adım 2 — İki yeni metot (`Library` içine)

**`public Map<String, Integer> countByAuthor()`**
- Yazar → o yazarın kitap sayısı. Bütün kitaplar sayılır (ödünçte olanlar dahil).
- Bölüm 4'teki sayma kalıbı.
- Dönerken kendi map'ini değil, değiştirilemez kopyasını dön: `Map.copyOf(...)` (`List.copyOf`'un Map'teki karşılığı).
- Kütüphane boşsa boş map.

**`public Set<String> authors()`**
- Kütüphanedeki farklı yazarların kümesi (aynı yazar bir kere).
- Bir `HashSet` doldur; dönerken `Set.copyOf(...)`.

### Adım 3 — `LibraryApp`

1. Kod tarafında başka bir şey değişmemeli (imzalar aynı). Derle, 1–7 menülerini birer kez dene.
2. **Yeni seçenek 8) Author stats**: `countByAuthor()` sonucunu `entrySet` ile gez, her satıra `Frank Herbert: 2` formatında yaz. Boşsa `"No books"`.
3. Menü metnine 8'i ekle.

Bitince haber ver; testleri ben yazıp çalıştıracağım, sonucu söyleyeceğim.

### Proje sonu düşünme soruları (chat'e, 1-2 cümle)
1. `findBook` artık O(1). `booksByAuthor` hâlâ O(n). Neden? `booksByAuthor`'ı da O(1) yapmak isteseydin `Library`'de **ek olarak** hangi tipte bir field tutardın? (Sadece tipini yaz, kodlama.)
2. Menüde 1'i seç: kitaplar `addBook` sırasıyla mı listeleniyor? Neden? Sırayı korumak isteseydin hangi satırı değiştirirdin? (Bölüm 3 ek bilgi.)

---

## 🧩 ALGORİTMA SAATİ (45 dk) — `HashSet` ve `HashMap`

**Yöntem (değişmedi):** imza kağıda → 3-5 Türkçe cümle → `w3d3/Algo1.java`, `Algo2.java` → LeetCode.

**A1 — [LeetCode 349: Intersection of Two Arrays](https://leetcode.com/problems/intersection-of-two-arrays/)**
`int[] intersection(int[] nums1, int[] nums2)`: iki dizide de geçen sayılar, **her biri bir kere**. Sıra önemsiz.
- Örnek: `[1,2,2,1]`, `[2,2]` → `[2]`. `[4,9,5]`, `[9,4,9,8,4]` → `[9,4]`
- İpucu: "Bu sayı `nums1`'de var mı?" sorusunu O(1)'de sormak için ne kullanırsın? Sonuçta tekrar olmaması için ne kullanırsın?
- Dönüş `int[]`: elindeki `Set<Integer>`'ın `size()`'ı kadar dizi aç, for-each ile doldur (index'i sen tut).

**A2 — [LeetCode 1: Two Sum](https://leetcode.com/problems/two-sum/)**
`int[] twoSum(int[] nums, int target)`: toplamı `target` eden iki elemanın **index'lerini** dön. Tam bir cevap olduğu garanti, aynı elemanı iki kere kullanamazsın.
- Örnek: `[2,7,11,15]`, `target = 9` → `[0,1]`. `[3,2,4]`, `6` → `[1,2]`. `[3,3]`, `6` → `[0,1]`
- Önce iki iç içe döngüyle O(n²) çöz, LeetCode'a gönder.
- Sonra O(n): `nums[i]`'deyken aradığın sayı `target - nums[i]`. "Bu sayıyı **daha önce** gördüm mü, gördüysem **hangi index'te**?" sorusunu O(1)'de cevaplamak için map'te ne tutarsın? (Anahtar ne, değer ne? Önce bunu chat'e yaz.)
- Dikkat: `[3,3]` testi. Kendini bulmamak için map'e **ne zaman** koyuyorsun: kontrol etmeden önce mi, sonra mı?
- Dikkat 2: dönen değer **index**, sayı değil. (Bilinen tuzağın: index/değer karışması.)

---

## 🔧 GIT (10 dk)

```bash
git add hafta-03/gun-03
git status            # sadece .java ve DERS.md (.class yok)
git commit -m "feat: week 3 day 3 - maps and sets"
git push
```
Sonra `ILERLEME.md`'ye Gün 15 logu → `docs: day 15 progress log`.

---

## ✅ Gün Sonu

- [ ] Isınma (6 soru, chat'e)
- [ ] E1–E3 (tahminler **çalıştırmadan önce** chat'e)
- [ ] Proje Adım 0–3
- [ ] Proje düşünme soruları
- [ ] A1 + A2 (A2 iki yöntem)
- [ ] "Kağıda Yaz" kutusu kağıtta
- [ ] Commit + push, `ILERLEME.md` Gün 15

**Yarın oturum başında soracaklarım:**

1. Çıktı ne?
   ```java
   Map<String, Integer> m = new HashMap<>();
   m.put("a", 1);
   System.out.println(m.put("a", 2));
   System.out.println(m.get("b"));
   ```
2. Bu satır ne yapar? `int x = m.get("b");`
3. `HashMap.get` neden bütün elemanlara bakmak zorunda kalmıyor? (hashCode ve equals'ın rolü)
4. Çıktı ne?
   ```java
   Set<String> s = new HashSet<>();
   s.add("x");
   System.out.println(s.add("x") + " " + s.size());
   ```
5. "1 milyon ISBN içinden bir ISBN var mı?" sorusu için `List` mi `Set` mi? Neden?

**Sıradaki:** Hafta 3 Gün 4: **Generics + `Comparable` / `Comparator`**. `getAllBooks()` sırası bugün karışık çıkacak; yarın kitapları başlığa ve yazara göre **sıralayacağız**.
