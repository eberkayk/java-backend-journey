# Hafta 3 · Gün 4 (Gün 16) — Generics + `Comparable` / `Comparator`

**Hedef:** Dün menü 1'de kitaplar karışık sırayla çıkıyordu (HashMap'te sıra yok). Bugün sonunda kitaplar **başlığa göre**, istersen **yazara göre** sıralı listelenecek. Bunun için iki şey öğreniyorsun: `<T>` işaretinin ne olduğu (generics) ve Java'ya "iki kitaptan hangisi önce gelir" demenin iki yolu.

**Süre:** ~6 saat · Isınma 15 dk · Ders 1.5 sa · Egzersiz 1 sa · Proje 1.5 sa · Algoritma 45 dk · Kapanış 20 dk

**Kurallar (değişmedi):**
- Sınıf, metot, değişken ve ekran mesajları **İngilizce**
- **Değişiklik → derle → çalıştır → çıktının her satırını oku.**
- **Metot adı/imzası görevde nasılsa öyle.**
- Tahminler **çalıştırmadan önce** chat'e.
- Testleri Claude yazıp çalıştırıyor. Sen yazmıyorsun.

---

## 0. Isınma (15 dk) — notlara bakmadan, cevapları chat'e

**S1.** Çıktı ne? (İki satır.)
```java
Map<String, Integer> m = new HashMap<>();
m.put("a", 1);
System.out.println(m.put("a", 2));
System.out.println(m.get("b"));
```

**S2.** Aynı `m` ile bu satır ne yapar? Neden?
```java
int x = m.get("b");
```

**S3.** `HashMap.get("Ali")` neden 1 milyon elemanın hepsine bakmak zorunda kalmıyor? `hashCode` ne işe yarıyor, `equals` ne işe yarıyor? (İki cümle.)

**S4.** Çıktı ne?
```java
Set<String> s = new HashSet<>();
s.add("x");
System.out.println(s.add("x") + " " + s.size());
```

**S5.** "1 milyon ISBN içinden şu ISBN var mı?" sorusu için hangisini seçersin, neden?
```java
List<String> isbnList = ...;   // isbnList.contains("978-0441")
Set<String>  isbnSet  = ...;   // isbnSet.contains("978-0441")
```

**S6.** (Dünkü tuzak, index/değer) `nums = {7, 2, 9}`. Bu döngü ne yazdırır?
```java
Map<Integer, Integer> seen = new HashMap<>();
for (int i = 0; i < nums.length; i++) {
    seen.put(nums[i], i);
}
System.out.println(seen.get(9) + " " + seen.get(2));
```

---

## 1. `<T>` aslında ne? ⭐⭐⭐ (20 dk)

3 haftadır `List<String>`, `Map<String, Book>` yazıyorsun. Köşeli parantezin içindeki şey **tip parametresi**. Önce o olmasaydı ne olurdu, onu gör.

### Generics'ten önce (Java 1.4, 2004 öncesi)

```java
List list = new ArrayList();          // tip yok → içi Object
list.add("Dune");
list.add(42);                         // derleyici ses çıkarmaz, Integer da bir Object
String title = (String) list.get(1);  // ClassCastException → program ÇALIŞIRKEN patlar
```

- İçeri her şey girer (`Object`, Gün 10).
- Çıkarırken **cast** etmek zorundasın (Gün 11 downcast).
- Hata **çalışırken** ortaya çıkar, belki müşterinin bilgisayarında.

### Generics ile

```java
List<String> list = new ArrayList<>();
list.add("Dune");
list.add(42);                 // DERLEME HATASI: int, String'e verilemez
String title = list.get(0);   // cast yok
```

Kazanç: hata **derlerken** yakalanır. Derleme hatası > çalışma zamanı hatası. Bu kadar.

### Kurallar

| Kural                                 | Örnek                                                            |
| ------------------------------------- | ---------------------------------------------------------------- |
| Tip parametresi **sadece class** olur | `List<int>` ❌ → `List<Integer>` ✅ (wrapper, Gün 14 autoboxing) |
| Sağ taraf `<>` (diamond) yeter        | `Map<String, Book> m = new HashMap<>();`                         |
| İsimler tek büyük harf (gelenek)      | `T` = type, `E` = element, `K` = key, `V` = value                |

---

## 2. Kendi generic class'ın ⭐⭐⭐ (20 dk)

`ArrayList`'i birileri bir kere `ArrayList<E>` diye yazdı; sen `String` de koysan `Book` da koysan aynı kod çalışıyor. Sen de yazabilirsin:

```java
public class Box<T> {
    private final T item;

    public Box(T item) {
        this.item = item;
    }

    public T getItem() {
        return item;
    }
}
```

```java
Box<String> b1 = new Box<>("Dune");
String s = b1.getItem();            // cast yok, T burada String

Box<Book> b2 = new Box<>(new Book("111", "Dune", "Frank Herbert"));
Book book = b2.getItem();           // T burada Book
```

`T` bir **yer tutucu**: class'ı kullanan kişi `<...>` içine ne yazarsa, `T` yazan her yer o olur. Birden fazla da olabilir: `Map<K, V>` gibi `Pair<A, B>` (E2'de yazacaksın).

### Generic metot

Sadece **bir metot** generic olabilir. `<T>` dönüş tipinden **önce** yazılır:

```java
public static <T> T firstOf(List<T> list) {
    return list.get(0);
}

String a = firstOf(List.of("x", "y"));   // T = String (Java kendisi anlar)
Integer b = firstOf(List.of(5, 6));      // T = Integer
```

`<T>` = "bu metotta T diye bir tip kullanacağım, ne olduğunu çağıran belirler."

---

## 3. `Comparable`: "benim doğal sıram bu" ⭐⭐⭐ (25 dk)

`List<Integer>` ve `List<String>`'i sıralamak kolay:

```java
List<Integer> nums = new ArrayList<>(List.of(5, 1, 7));
Collections.sort(nums);
System.out.println(nums);       // [1, 5, 7]

List<String> names = new ArrayList<>(List.of("Can", "Ali", "Zeynep"));
Collections.sort(names);
System.out.println(names);      // [Ali, Can, Zeynep]
```

Peki `List<Book>`? Java iki kitaptan hangisinin önce geleceğini **bilmiyor**. Başlık mı, yazar mı, ISBN mi? Sen söylemelisin.

### `compareTo` sözleşmesi

`Integer` ve `String` bunu biliyor çünkü `Comparable` interface'ini (Gün 11) implement ediyorlar. İçinde tek metot var:

```java
public int compareTo(T other)
```

| `a.compareTo(b)` dönüşü | Anlamı                       |
| ----------------------- | ---------------------------- |
| **negatif** (< 0)       | `a`, `b`'den **önce** gelir  |
| **0**                   | Sıralama açısından eşit      |
| **pozitif** (> 0)       | `a`, `b`'den **sonra** gelir |

Sadece **işaret** önemli; `-1` mi `-37` mi fark etmez.

```java
"apple".compareTo("banana")   // negatif  (a < b, sözlük sırası)
"cat".compareTo("cat")        // 0
Integer.compare(9, 3)         // pozitif  (9, 3'ten sonra gelir)
"Zeynep".compareTo("ali")     // NEGATİF! Büyük harfler küçük harflerden önce (Unicode, Gün 5 char kodları)
```

### `Book`'a doğal sıra vermek

```java
public class Book implements Comparable<Book> {
    ...
    @Override
    public int compareTo(Book other) {
        return title.compareTo(other.title);   // başlığa göre
    }
}
```

Artık `Collections.sort(bookList)` çalışır. `implements Comparable<Book>` olmadan bu satır **derlenmez**.

### İki sıralama yolu, aynı iş

```java
Collections.sort(list);   // eski, statik yardımcı
list.sort(null);          // List'in kendi metodu; null = "doğal sırayı kullan"
```

### ⚠️ İki tuzak

**1. Değiştirilemez listeyi sıralamak:**
```java
List<Book> all = List.copyOf(books.values());
Collections.sort(all);              // UnsupportedOperationException (Gün 14 E4)
```
Çözüm: önce değiştirilebilir bir kopya, onu sırala, sonra istersen `List.copyOf`:
```java
List<Book> all = new ArrayList<>(books.values());
Collections.sort(all);
return List.copyOf(all);
```

**2. Sayıları çıkararak karşılaştırmak:**
```java
return this.year - other.year;               // çoğu zaman çalışır, AMA
                                             // çok büyük/küçük sayılarda int taşar → yanlış işaret
return Integer.compare(this.year, other.year); // her zaman doğru → bunu kullan
```

---

## 4. `Comparator`: "bu sefer başka sıra istiyorum" ⭐⭐⭐ (25 dk)

`Comparable` class'ın **tek** doğal sırası. Ama bazen başlığa, bazen yazara göre sıralamak istersin. `compareTo`'yu her seferinde değiştiremezsin. Çözüm: sıralama kuralını **ayrı bir nesne** olarak ver. Bu nesne `Comparator<T>`.

### Yol 1 — Ayrı class (uzun ama her şey açık)

```java
public class AuthorComparator implements Comparator<Book> {
    @Override
    public int compare(Book a, Book b) {
        return a.getAuthor().compareTo(b.getAuthor());
    }
}

list.sort(new AuthorComparator());
```

`compareTo(other)` bir kitabın **kendini** başkasıyla karşılaştırması; `compare(a, b)` dışarıdan bir hakemin **iki kitabı** karşılaştırması. Dönüş kuralı aynı tablo.

### Yol 2 — Lambda (aynı şeyin kısası)

`Comparator`'da tek bir metot var. Java'ya "o tek metodun gövdesi şu" demenin kısa yolu **lambda**:

```java
list.sort((a, b) -> a.getAuthor().compareTo(b.getAuthor()));
```

Okunuşu: "`a` ve `b` verilince, `a.getAuthor().compareTo(b.getAuthor())` döndür." Yol 1'deki class'ın birebir aynısı, sadece isimsiz. (Lambda'ları Hafta 4'te detaylı göreceksin; bugün sadece bu kalıp.)

### Yol 3 — `Comparator.comparing` (en okunaklı)

```java
list.sort(Comparator.comparing(Book::getAuthor));
```

`Book::getAuthor` = "her kitap için `getAuthor()`'u çağır" (metot referansı). `comparing` o değerleri alıp kendi aralarında karşılaştırır.

Zincirleme:
```java
Comparator<Book> byAuthorThenTitle =
        Comparator.comparing(Book::getAuthor)
                  .thenComparing(Book::getTitle);    // yazar aynıysa başlığa bak

list.sort(byAuthorThenTitle);
list.sort(Comparator.comparing(Book::getTitle).reversed());   // Z → A
```

| İstediğin                | Yaz                                                 |
| ------------------------ | --------------------------------------------------- |
| Doğal sıra (`compareTo`) | `list.sort(null)` veya `Collections.sort(list)`     |
| Başka bir alana göre     | `Comparator.comparing(Book::getAuthor)`             |
| Eşitse ikinci alana bak  | `.thenComparing(Book::getTitle)`                    |
| Ters sıra                | `.reversed()`                                       |
| Diziler için             | `Arrays.sort(arr)` / `Arrays.sort(arr, comparator)` |

### `Comparable` mı `Comparator` mı?

| `Comparable<T>`                     | `Comparator<T>`                       |
| ----------------------------------- | ------------------------------------- |
| Class'ın **içinde**, `compareTo(o)` | Class'ın **dışında**, `compare(a, b)` |
| Tek bir doğal sıra                  | İstediğin kadar farklı sıra           |
| `list.sort(null)`                   | `list.sort(comparator)`               |

**Ek bilgi:** Dün "anahtara göre sıralı lazımsa `TreeMap`" demiştik. `TreeMap` de `TreeSet` de anahtarları tam olarak bu `compareTo` ile sıralıyor.

---

## 📝 Kağıda Yaz

```
Generics: hata derlerken yakalanır, cast yok · List<int> ❌ List<Integer> ✅
class Box<T> { T item; } · generic metot: static <T> T firstOf(List<T> l)
a.compareTo(b): negatif → a önce · 0 → eşit · pozitif → a sonra
class Book implements Comparable<Book> → doğal sıra → list.sort(null)
Comparator = dışarıdan hakem: compare(a, b)
Comparator.comparing(Book::getAuthor).thenComparing(Book::getTitle).reversed()
List.copyOf ile gelen liste sıralanamaz → new ArrayList<>(...) kopyala, sırala
Sayı karşılaştır: Integer.compare(a, b), a - b DEĞİL
```

---

## 💪 EGZERSİZLER (~1 saat)

Paket: `w3d4`. Klasör: `hafta-03/gun-04/w3d4/`.

### E1 — `RawListDemo.java` (tahmin)

Çalıştırmadan önce iki soruyu chat'e cevapla.

```java
List raw = new ArrayList();      // bilerek tipsiz
raw.add("Dune");
raw.add(42);
for (Object o : raw) {
    String s = (String) o;
    System.out.println(s.toUpperCase());
}
```
1. Bu kod **derlenir** mi? (IntelliJ sarı uyarı gösterebilir, uyarı hata değildir.)
2. Çalışınca ekranda ne görürsün, hangi satırda ne olur?

Sonra ilk satırı `List<String> raw = new ArrayList<>();` yap. 3. soru: şimdi **hangi satırda** ne olur? Derle, kontrol et, o satırı sil.

### E2 — `Pair.java` + `PairDemo.java` (kod yaz)

**`Pair<A, B>`** (yeni class, generic)
- **Field'lar:** `private final A first;`, `private final B second;`
- **Constructor:** `public Pair(A first, B second)`
- **Metotlar:** `public A getFirst()`, `public B getSecond()`
- **`toString`:** `(Dune, 1965)` formatı

**`PairDemo`** — `main` içinde:
1. `Pair<String, Integer> p = new Pair<>("Dune", 1965);` oluştur, yazdır.
2. `String title = p.getFirst();` ve `int year = p.getSecond();` yaz, ikisini yazdır. (İkinci satırda cast gerekiyor mu? Neden gerekmiyor? Chat'e bir cümle.)
3. `PairDemo` içine generic metot yaz: **`public static <T> T lastOf(List<T> list)`** → listenin son elemanını döner. Liste boşsa `IllegalArgumentException("List is empty")`.
4. `lastOf(List.of("a", "b", "c"))` ve `lastOf(List.of(3, 1, 4))` sonuçlarını yazdır.

### E3 — `SortDemo.java` (tahmin)

Çalıştırmadan önce numaralı her satırın çıktısını chat'e yaz.

```java
System.out.println("apple".compareTo("banana") < 0);        // 1
System.out.println("cat".compareTo("cat"));                 // 2
System.out.println(Integer.compare(3, 9) > 0);              // 3

List<String> names = new ArrayList<>(List.of("can", "Zeynep", "ali", "Bora"));
Collections.sort(names);
System.out.println(names);                                  // 4

names.sort(Comparator.comparing(String::length));
System.out.println(names);                                  // 5  (ipucu: eşit uzunluktakiler sırasını korur)

names.sort(Comparator.comparing(String::length).reversed());
System.out.println(names);                                  // 6

List<Integer> fixed = List.of(3, 1, 2);
Collections.sort(fixed);                                    // 7: ne olur?
```

---

## 🏛️ PROJE — Kütüphane v6: sıralı listeler (~1.5 saat)

### Adım 0 — Kopyala
`hafta-03/gun-03/w3d3/` içinden `Book`, `Member`, `StudentMember`, `StaffMember`, `Library`, `LibraryApp` ve **6 exception class'ını** `hafta-03/gun-04/w3d4/` içine kopyala, her dosyada `package w3d3;` → `package w3d4;`. Derle, çalıştığından emin ol.
`Member`, alt class'lar ve exception'lar bugün **değişmiyor**.

### Adım 1 — `Book`: doğal sıra

- Class satırı: `public class Book implements Comparable<Book>`
- Yeni metot: **`public int compareTo(Book other)`**
  - Önce **başlığa** göre karşılaştır.
  - Başlıklar aynıysa (`0` çıktıysa) **ISBN'e** göre karşılaştır. (Neden? Proje sonu soru 1.)
- Field'lar, constructor, `equals`, `hashCode`, `toString` **değişmiyor**.

### Adım 2 — `Library`

Field'lar ve constructor **değişmiyor** (iki `Map`).

| Metot                                                     | Ne değişiyor                                                                                                                          |
| --------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------- |
| `List<Book> getAllBooks()`                                | Artık **başlığa göre sıralı** (doğal sıra) dönüyor. Hâlâ değiştirilemez liste. (Bölüm 3 tuzak 1'e dikkat.)                            |
| `List<Book> booksByAuthor(String author)`                 | Dönen liste **başlığa göre sıralı**.                                                                                                  |
| **YENİ** `List<Book> getAllBooks(Comparator<Book> order)` | Bütün kitaplar, `order`'a göre sıralı, değiştirilemez liste. `order` `null` ise `IllegalArgumentException("Order must not be null")`. |
| Diğer bütün metotlar                                      | **Değişmiyor.**                                                                                                                       |

`getAllBooks()` ve `getAllBooks(Comparator<Book>)` **overload** (Gün 3). Kopyala-yapıştır yerine biri diğerini çağırabilir mi? Bu senin tasarım kararın.

### Adım 3 — `LibraryApp`

1. Menü 1 kodunda değişiklik gerekmemeli; çalıştır, başlığa göre sıralı geldiğini gör.
2. Başlangıç kitaplarını sıralamayı gösterecek şekilde değiştir (eski `title1..title5` sıralı olduğu için fark görünmüyor). Bu 5 kitabı ekle:
   ```
   "123", "Dune",              "Frank Herbert"
   "124", "Emma",              "Jane Austen"
   "125", "Children of Dune",  "Frank Herbert"
   "126", "Animal Farm",       "George Orwell"
   "127", "1984",              "George Orwell"
   ```
3. **Yeni seçenek 9) List by author**: `getAllBooks(...)`'a **yazara, yazar aynıysa başlığa** göre sıralayan bir `Comparator` ver, sonucu her satıra bir kitap yazdır. Boşsa `"No books"`.
4. Menü metnine 9'u ekle.

Bitince haber ver; testleri ben yazıp çalıştıracağım.

### Proje sonu düşünme soruları (chat'e, 1-2 cümle)
1. `compareTo` sadece başlığa baksaydı, aynı başlıklı iki farklı kitap (`"111", "Dune", ...` ve `"222", "Dune", ...`) için `compareTo` ne dönerdi? `equals` ne dönerdi? Bu ikisinin farklı cevap vermesi neden kafa karıştırıcı?
2. Menü 1 artık sıralı. `getAllBooks()` her çağrıldığında ne kadar iş yapıyor (O(?))? Kitaplar zaten sıralı dursun isteseydin `books` field'ının tipini ne yapardın? (Sadece tipi; bölüm 4 ek bilgi.)

---

## 🧩 ALGORİTMA SAATİ (45 dk) — Sıralama ile çözmek

**Yöntem (değişmedi):** imza kağıda → 3-5 Türkçe cümle → `w3d4/Algo1.java`, `Algo2.java` → LeetCode.

**A1 — [LeetCode 242: Valid Anagram](https://leetcode.com/problems/valid-anagram/)**
`boolean isAnagram(String s, String t)`: `t`, `s`'in harflerinin yer değiştirmiş hâli mi?
- Örnek: `"anagram"`, `"nagaram"` → `true`. `"rat"`, `"car"` → `false`
- **Yol 1 (sıralama):** iki string'i `char[]`'a çevir (`toCharArray`, Gün 5), ikisini de `Arrays.sort` ile sırala, `Arrays.equals` ile karşılaştır. Göndermeden önce chat'e: bu yöntem O(?)?
- **Yol 2 (sayma):** Dünkü sayma kalıbı veya `int[26]`. O(n). İkisini de gönder.

**A2 — [LeetCode 2418: Sort the People](https://leetcode.com/problems/sort-the-people/)**
`String[] sortPeople(String[] names, int[] heights)`: isimleri boylara göre **büyükten küçüğe** sırala. Boylar hepsi farklı.
- Örnek: `names = ["Mary","John","Emma"]`, `heights = [180,165,170]` → `["Mary","Emma","John"]`
- Sorun: `names`'i sıralarsan `heights` ile bağlantı kopar. İki yol, birini seç:
  - **Yol A:** her kişi için bir `Pair<Integer, String>` (E2'deki class'ını LeetCode'a sınıf olarak ekleyebilirsin) → `List`'e koy → boya göre **ters** `Comparator` ile sırala → isimleri diziye aktar.
  - **Yol B:** `Map<Integer, String>` boy → isim (boylar farklı olduğu için olur), `heights`'ı sırala, **sondan başa** gezip map'ten isimleri al.
- Önce hangi yolu seçtiğini ve 3-5 cümlelik planını chat'e yaz.
- Dikkat: dün olduğu gibi index/değer karışmasın. Map'te **anahtar** ne, **değer** ne? `get`'e verdiğin = elindeki.

---

## 🔧 GIT (10 dk)

```bash
git add hafta-03/gun-04
git status            # sadece .java ve DERS.md (.class yok)
git commit -m "feat: week 3 day 4 - generics and sorting"
git push
```
Sonra `ILERLEME.md`'ye Gün 16 logu → `docs: day 16 progress log`.

---

## ✅ Gün Sonu

- [ ] Isınma (6 soru, chat'e)
- [ ] E1–E3 (tahminler **çalıştırmadan önce** chat'e)
- [ ] Proje Adım 0–3
- [ ] Proje düşünme soruları
- [ ] A1 (iki yol) + A2
- [ ] "Kağıda Yaz" kutusu kağıtta
- [ ] Commit + push, `ILERLEME.md` Gün 16

**Yarın oturum başında soracaklarım:**

1. Bu satır derlenir mi? Derlenmezse düzelt.
   ```java
   List<int> nums = new ArrayList<>();
   ```
2. `"b".compareTo("a")` negatif mi, 0 mı, pozitif mi? Bu sonuç sıralamada ne demek?
3. Bu kod ne yapar?
   ```java
   List<String> list = List.of("b", "a");
   Collections.sort(list);
   ```
4. Bu iki satır aynı sırayı mı verir? Fark ne?
   ```java
   books.sort(null);                                      // Book: compareTo başlığa göre
   books.sort(Comparator.comparing(Book::getAuthor));
   ```
5. `Comparable` ile `Comparator` arasındaki fark ne? (Biri class'ın içinde, biri ...)

**Sıradaki:** Hafta 3 Gün 5: **Iterator + Hafta 3 tekrar**. `for-each`'in arkasında ne olduğunu göreceksin (Gün 14'teki `ConcurrentModificationException`'ın asıl sebebi) ve hafta sonu mini sınavına hazırlanacağız.
