# Hafta 2 · Gün 7 (Gün 12) — `equals` / `hashCode` + Proje: Kütüphane Sistemi

**Hedef:** İki nesnenin "aynı" olduğunu Java'ya **senin** söylemen (`equals`), bunun neden tek başına yetmediğini görmek (`hashCode`) ve Hafta 2'de öğrendiğin her şeyi (encapsulation, abstract class, polymorphism, `equals`) tek bir küçük projede birleştirmek: **Kütüphane Sistemi**. Haftaya bu projeyi Collections + exception ile yeniden yazacağız, yani bugünkü tasarım kararların haftaya işine yarayacak.

**Süre:** ~6.5 saat · Isınma 20 dk · Ders 1 sa · Egzersiz 1 sa · Proje 3 sa · Algoritma 45 dk · Kapanış 20 dk
Proje bugün bitmezse **Parça 2 (konsol menüsü)** yarın ilk iş. Parça 1 bugün bitmeli.

**Kurallar (değişmedi):**
- Sınıf, metot, değişken ve ekran mesajları **İngilizce**
- **Değişiklik → derle → çalıştır → çıktının her satırını oku.**
- **Metot adı/imzası görevde nasılsa öyle.** (Sınavda `totalPrice` → `getPrice` oldu, puan gitti.)
- Hesaplayan metot yazdırmaz, **return** eder.
- **Test tablosu kodlamadan önce:** `girdi | beklenen | neyi kontrol ediyor`. Her false testi **farklı bir sebepten** false olsun.

---

## 0. Isınma (20 dk) — notlara bakmadan (sınavda kaçanlar, cevapları chat'e)

1. `final` üç yerde kullanılır: field, metot, class. Her biri neyi yasaklar? (Birer cümle.)
2. `class X { int n; String s; }` → `new X()` sonrası `n` ve `s` ne? Bir metodun içinde `int n; System.out.println(n);` yazarsan ne olur?
3. ```java
   Animal a = new Dog();
   Cat c = (Cat) a;
   System.out.println("done");
   ```
   Derlenir mi? Çalışınca ne olur? **Hangi satırda** olur? `"done"` yazılır mı?
4. Sınavda `totalInsurance` içinde `instanceof CarRental` yazdın. Yarın `MotorRental` da `Insurable` olursa ne olur? `instanceof Insurable` bu sorunu neden çözer?
5. `mergeSorted`'da `while (i < a.length || j < b.length)` neden `ArrayIndexOutOfBoundsException` attı? `&&` yaparsan döngüden sonra ne yapman gerekir?
6. `count` bir sayma dizisi, `i` en çok tekrar eden harfin index'i. `(char) ('a' + i)` ile `(char) ('a' + count[i])` farkı ne? Hangisi harfi verir?

---

## 1. Sorun: `==` nesnelerde yetmez ⭐⭐ (10 dk)

Gün 5'ten hatırla: String'leri `==` ile değil `equals` ile karşılaştırıyorduk, çünkü `==` **adresleri** karşılaştırır. Kendi class'ların için de aynısı geçerli:

```java
Book b1 = new Book("978-0441", "Dune", "Frank Herbert");
Book b2 = new Book("978-0441", "Dune", "Frank Herbert");

b1 == b2        // false — heap'te iki ayrı nesne, iki ayrı adres
b1.equals(b2)   // ??? 
```

`equals` metodunu sen yazmadın. Ama derleniyor, demek ki biri yazmış. Kim? → **`Object`** (Gün 8: her class gizlice `extends Object`). `Object`'in `equals`'ı şöyle:

```java
public boolean equals(Object obj) {
    return this == obj;      // yani yine adres karşılaştırması!
}
```

Yani override etmezsen `equals` = `==`. Kütüphanede aynı ISBN'li kitabı iki kere eklemeyi engellemek, "bu kitabı bu üye mi ödünç aldı?" diye sormak için Java'ya **"bu class'ta iki nesne ne zaman aynıdır"** bilgisini sen vermelisin.

---

## 2. `equals`'ı override etmek ⭐⭐⭐ (25 dk)

Önce karar: **iki nesneyi aynı yapan ne?** Bu bir **tasarım kararıdır**, Java bilemez.
- Bir kitap: aynı **ISBN** → aynı kitap. Başlık aynı olabilir (iki farklı "Dune" baskısı) ama ISBN tektir.
- Bir öğrenci: aynı **öğrenci numarası** → aynı kişi. İsim aynı olabilir (iki "Ayşe Yılmaz").

Bu alanlara **iş anahtarı** (business key) denir. Spring/veritabanı haftalarında aynı soru tekrar gelecek.

Şablon (örnek `Student` üzerinden, `Book`'u sen yazacaksın):

```java
public class Student {
    private final String studentNo;
    private String name;
    ...

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;                              // (1) aynı adres → kesin eşit
        if (o == null || getClass() != o.getClass()) return false; // (2) null veya farklı class
        Student other = (Student) o;                             // (3) artık güvenli downcast
        return studentNo.equals(other.studentNo);                // (4) iş anahtarını karşılaştır
    }
}
```

Satır satır:
1. **Kısayol.** Aynı nesneyse alanlara bakmaya gerek yok.
2. **`null` kontrolü + tip kontrolü.** `b1.equals(null)` → `false` dönmeli, **NPE atmamalı**. Bir `Book`'u `String` ile karşılaştırınca da `false`. `getClass()` nesnenin gerçek tipini verir (Gün 9: runtime tipi).
3. **Downcast.** Parametre `Object` olduğu için `other.studentNo` diyemezsin (Gün 9: referans tipine göre derlenir). Ama (2)'den geçtiysek tip kesin `Student`, cast `ClassCastException` atamaz.
4. **Alanlar.** Nesne alanları (`String`) → `equals` ile. Primitif alanlar (`int`, `double`) → `==` ile.
   - Alan `null` olabiliyorsa `Objects.equals(a, b)` kullan (`java.util.Objects`), ikisi de null ise true, biri null ise false döner, NPE atmaz.
   - Birden fazla alan varsa `&&` ile bağla.

`private` alana `other.studentNo` diye nasıl eriştik? → Gün 7: `private` **class seviyesindedir**, aynı class'ın başka nesnesinin private alanı görülür.

### ⚠️ Klasik tuzak: parametre `Object` olmak zorunda

```java
public boolean equals(Student other) { ... }    // Object değil, Student!
```
Bu **override değil, overload**'dur: `Object.equals(Object)` hâlâ duruyor, yanına ikinci bir metot eklemiş olursun. Gün 9'da öğrendin: **overload referans tipine göre** seçilir. Yani kod bazen senin metodunu, bazen `Object`'inkini çağırır. E2'de bunu kendi gözünle göreceksin. `@Override` yazarsan derleyici seni hemen uyarır, bu yüzden `@Override`'ı **her zaman** yaz.

### `equals`'ın sözleşmesi (mülakatta sorulur, 5 kural)
- **Yansıma:** `a.equals(a)` → `true`
- **Simetri:** `a.equals(b)` ise `b.equals(a)`
- **Geçişlilik:** `a.equals(b)` ve `b.equals(c)` ise `a.equals(c)`
- **Tutarlılık:** Nesneler değişmedikçe kaç kere çağırırsan çağır aynı cevap
- **null:** `a.equals(null)` → `false` (exception değil)

---

## 3. `hashCode`: equals'ın zorunlu ikizi ⭐⭐⭐ (20 dk)

`hashCode()` da `Object`'ten gelir: nesne için bir `int` döndürür. Ne işe yarar? Haftaya göreceğin `HashSet` ve `HashMap` gibi yapılar onu **hızlı arama** için kullanır.

Basit model: `HashSet`'in içinde 16 kutu olduğunu düşün.
1. Bir nesne eklenirken önce `hashCode()` çağrılır → sayıya göre **bir kutu** seçilir.
2. Sadece **o kutunun içindekilerle** `equals` ile karşılaştırılır. Eşit olan varsa eklenmez.

Bu sayede 1 milyon eleman arasında arama yapmak için 1 milyon `equals` çağırmazsın, sadece bir kutuya bakarsın.

**Sorun:** `equals`'ı override edip `hashCode`'u etmezsen, `Object`'in `hashCode`'u her nesneye **farklı** sayı verir (adrese benzer). İki "eşit" kitap **farklı kutulara** düşer, `equals` hiç çağrılmaz → set'te aynı kitaptan **iki tane** olur. E3'te bunu göreceksin.

### Kural (ezberle, mülakatta birebir sorulur)
> **`a.equals(b)` true ise, `a.hashCode() == b.hashCode()` olmak ZORUNDA.**
> Tersi şart değil: hashCode'ları aynı olan iki nesne eşit olmayabilir (aynı kutuya düşerler, `equals` ayırır, buna **collision** denir).

Pratik sonuç: **`equals`'ta kullandığın alanları `hashCode`'ta da kullan, fazlasını kullanma.**

```java
@Override
public int hashCode() {
    return Objects.hash(studentNo);    // equals'ta hangi alanlar varsa onlar
}
```
`Objects.hash(...)` verdiğin alanlardan bir sayı üretir, birden fazla alan alabilir: `Objects.hash(a, b, c)`.

> **`equals`'ı override ettiysen `hashCode`'u da override et. Biri varsa öbürü de olmalı.**

### `toString` ile bağlantı (Gün 7'deki gizem)
Gün 7'de `Product@1b6d3586` gibi bir şey basmıştın. `Object.toString()` aslında şu:
```java
getClass().getName() + "@" + Integer.toHexString(hashCode())
```
`@`'den sonraki o sayı nesnenin `hashCode`'unun 16'lık tabandaki hâliydi. E3'te bunu kontrol edeceksin.

### IntelliJ bunları yazabilir, peki neden elle yazıyoruz?
IntelliJ `Alt+Insert → equals() and hashCode()` ile üretir, gerçek işte öyle yaparsın. Ama mülakatta "equals'ı tahtaya yaz" diyorlar ve "hashCode'u neden override ettin?" diye soruyorlar. **Bugün elle yaz**, haftaya IntelliJ'e bırakabilirsin.

---

## 📝 KAĞIDA YAZ (sadece bunlar)

1. `Object.equals` = `==` (adres). Mantıksal eşitlik için override et.
2. İmza: `public boolean equals(Object o)`. `Object` değilse **overload** olur, `@Override` her zaman yazılır.
3. Şablon: aynı adres → true · null / farklı class → false · cast · iş anahtarını karşılaştır.
4. Nesne alanı `equals` / `Objects.equals`, primitif `==`.
5. Kural: **equals true ⇒ hashCode aynı.** Tersi şart değil (collision).
6. `equals`'ı override eden `hashCode`'u da override eder, ikisinde aynı alanlar kullanılır.

---

## 💻 EGZERSİZLER (AI yok, IntelliJ'de AI kapalı)

### E0 — Kurulum (5 dk)
`hafta-02/gun-07/` → **Sources Root** → package **`w2d7`**.

### E1 — Önce bozuk, sonra düzgün eşitlik ⭐⭐
`w2d7/Book.java`: `private final String isbn, title, author;` + constructor + getter'lar. **Henüz `equals`, `hashCode`, `toString` yazma.**

`w2d7/EqualityDemo.java`:
```java
Book b1 = new Book("978-0441", "Dune", "Frank Herbert");
Book b2 = new Book("978-0441", "Dune", "Frank Herbert");
Book b3 = b1;
Book b4 = new Book("978-0441", "Dune (2nd ed.)", "Frank Herbert");
```
Şunları yazdır: `b1 == b2`, `b1.equals(b2)`, `b1 == b3`, `b1.equals(b3)`, `b1.equals(b4)`.
1. **Çalıştırmadan önce** 5 cevabı chat'e yaz, sonra çalıştır.
2. `Book`'a `equals`'ı yaz (iş anahtarı: **sadece ISBN**). Aynı 5 satırı tekrar tahmin et, çalıştır. Hangileri değişti?
3. Şu iki satırı da ekle, önce tahmin et: `b1.equals(null)`, `b1.equals("978-0441")`. İkisi de `false` olmalı ve **exception olmamalı**. Olmuyorsa şablonun hangi satırı eksik?

### E2 — Overload tuzağı ⭐⭐⭐ (Gün 9'un tekrarı)
`w2d7/BadBook.java`: `Book`'un kopyası, ama `equals`'ı şöyle (bunu bilerek yanlış yazıyoruz):
```java
public boolean equals(BadBook other) {     // @Override YOK, parametre BadBook
    return isbn.equals(other.isbn);
}
```
`EqualityDemo`'ya:
```java
BadBook x = new BadBook("111", "A", "B");
BadBook y = new BadBook("111", "A", "B");
Object  z = y;
```
`x.equals(y)` ve `x.equals(z)`: **önce tahmin et**, sonra çalıştır. `y` ve `z` aynı nesne, neden farklı cevap? (Bir cümle: "referans tipi" kelimesi geçsin.)
Sonra `BadBook.equals`'ın üstüne `@Override` ekle → derle → hata mesajını chat'e yapıştır.

### E3 — `hashCode`'u unutursan ⭐⭐⭐
1. `Book`'ta **henüz `toString` ve `hashCode` yok** iken:
   ```java
   System.out.println(b1);
   System.out.println(Integer.toHexString(b1.hashCode()));
   ```
   İki satırı karşılaştır. `@`'den sonraki kısım ne?
2. `HashSet` teaser (Collections haftaya, bugün sadece bu üç satırı kullan):
   ```java
   java.util.Set<Book> set = new java.util.HashSet<>();
   set.add(b1);
   set.add(b2);
   System.out.println(set.size());
   ```
   `b1.equals(b2)` artık `true`. **Tahmin et:** `size()` kaç? Çalıştır.
3. `Book`'a `hashCode`'u yaz (§3: equals'taki alanlar). Aynı kodu tekrar çalıştır. `size()` kaç oldu? Neden değişti? (İki cümle: "kutu" ve "equals hiç çağrılmadı" geçsin.)
4. Son olarak `toString` yaz: `Book[978-0441, Dune by Frank Herbert]`.

### E4 — Kurala uyuyor mu? ⭐⭐ (chat'e, çalıştırmadan, 10 dk)
Her biri için **uyar / uymaz** + bir cümle sebep:

1. `equals` ISBN'e bakıyor, `hashCode` → `return 42;`
2. `equals` ISBN **ve** başlığa bakıyor, `hashCode` → `Objects.hash(isbn)`
3. `equals` **sadece** ISBN'e bakıyor, `hashCode` → `Objects.hash(isbn, title)`
4. `equals` override edilmiş, `hashCode` hiç yazılmamış.
5. (Tasarım) Kütüphane üyesinin (`Member`) iş anahtarı ne olmalı: `id` mi, `name` mi, ikisi mi? Neden?

> 1'e "uyar ama…" diyorsan "ama" kısmını da yaz.

---

## 🏛️ PROJE — Kütüphane Sistemi (Hafta 2 projesi, ~3 saat)

**Kısıtlar:** Sadece bu hafta öğrendiklerin. **Collections yok** (`ArrayList`, `HashMap` yasak, E3'teki `HashSet` sadece deneydi), **dizi + sayaç** kullan. Hata durumunda `boolean false` / `null` dön (exception'ları haftaya öğreneceğiz).

Tüm dosyalar `w2d7` içinde. `Book`'u E1–E3'ten devam ettir.

### Parça 1 — Model + Library (bugün bitmeli)

**1. `Book`** (E1–E3'ten devam)
- Yeni alan: `private Member borrower;`. `null` ise kitap rafta.
- `public boolean isAvailable()`
- `public Member getBorrower()`
- `public void setBorrower(Member member)`. Sadece `Library` çağıracak; `null` vermek "iade edildi" demek.
- `equals` / `hashCode` **sadece ISBN** (değiştirme).
- `toString` → rafta: `Book[978-0441, Dune by Frank Herbert, available]`, ödünçte: `Book[978-0441, Dune by Frank Herbert, borrowed by M1]`

**2. `abstract class Member`**
- `private final String id; private final String name; private int borrowedCount;`
- `public abstract int maxBooks();`
- `public boolean canBorrow()` → limit dolmadıysa true
- `public void onBorrow()` / `public void onReturn()` → sayacı artır / azalt
- `equals` / `hashCode` → E4-5'te verdiğin karara göre
- `toString` → `StudentMember[M1, Ayse, 1/2 books]` (class adı alt class'tan gelsin: `getClass().getSimpleName()` kullanabilirsin)

**3. `StudentMember`** → `maxBooks()` = 2 · **`StaffMember`** → `maxBooks()` = 5

**4. `Library`**

Field'lar (4 tane, hepsi `private`): bir `Book[]` dizisi + kaç kitap eklendiğini tutan `int` sayaç, bir `Member[]` dizisi + kaç üye eklendiğini tutan `int` sayaç. Diziler constructor'da gelen kapasiteyle oluşturulur. Döngüler `length`'e değil **sayaca** kadar gider.

```java
public Library(int bookCapacity, int memberCapacity)

public boolean addBook(Book book)        // null, dolu veya aynı kitap zaten var → false
public boolean addMember(Member member)  // null, dolu veya aynı üye zaten var → false
public Book findBook(String isbn)        // yoksa null
public Member findMember(String id)      // yoksa null
public boolean borrowBook(String memberId, String isbn)
public boolean returnBook(String memberId, String isbn)
public int availableCount()
public Book[] booksByAuthor(String author)
```
Kurallar:
- `addBook` / `addMember`: "aynısı var mı" kontrolünü **`equals` ile** yap (bugünün konusu).
- `borrowBook` → false: üye yok, kitap yok, kitap zaten ödünçte, üyenin limiti dolu. Başarılıysa kitabın `borrower`'ı ve üyenin sayacı **ikisi birden** güncellenir.
- `returnBook` → false: üye yok, kitap yok, kitap **bu üyede değil** (başkasında ya da rafta). Hangi üyede olduğunu `equals` ile kontrol et.
- `booksByAuthor` → **tam boyutlu** dizi döndür: 3 sonuç varsa uzunluğu 3, sonuç yoksa uzunluğu **0** (null değil). İpucu: iki tur (önce say, sonra doldur).
- Hiçbir metot `println` yapmaz.

**5. `LibraryDemo` (test senaryosu)**
- **Kodlamadan önce chat'e en az 10 satırlık test tablosu:** `işlem | beklenen | neyi kontrol ediyor`. En az şunları kapsasın: aynı ISBN'i tekrar ekleme, kapasite dolu, var olmayan üye, var olmayan kitap, ödünçteki kitabı tekrar ödünç alma, öğrenci 3. kitap, yanlış üyenin iade etmesi, iade sonrası başka üyenin ödünç alması, `booksByAuthor` sonuç yok (uzunluk 0), `booksByAuthor` 2+ sonuç.
- `main` tabloyu sırayla çalıştırsın, her satır için `beklenen` ve `gerçek` yazdırsın.

### Parça 2 — Konsol menüsü (bitmezse yarın ilk iş)
`LibraryApp.java`, `Scanner` ile (Gün 1'de kullanmıştın):
```
=== LIBRARY ===
1) List all books
2) Add book
3) Borrow book
4) Return book
5) Search by author
0) Exit
```
**`Library`'ye eklenecek metot:** `public Book[] getAllBooks()` → eklenmiş kitapları **tam boyutlu** yeni bir dizi olarak döndürür (uzunluğu `bookCount`). İç diziyi (`books`) doğrudan döndürme, sonunda `null`'lar var ve dışarıdan değiştirilebilir.

**`LibraryApp`** (`main` içeren class):
- Başta 4-5 kitap ve 2-3 üye (en az bir `StudentMember`, bir `StaffMember`) koddan eklenmiş olsun.
- Tek bir `Scanner`, sadece `nextLine()` kullan, menü seçimini sayıya çevirmek için `Integer.parseInt(...)`. (`nextInt` + `nextLine` karışınca satır atlama bug'ı çıkar.)
- Döngü + `switch`. Her turda menüyü yazdır, seçimi oku.

| Seçim | Kullanıcıdan istenenler | Yazdırılan |
|---|---|---|
| `1` | — | `getAllBooks()`'taki her kitabın `toString`'i, alt alta |
| `2` | ISBN, başlık, yazar (3 ayrı satır) | `"Book added"` / `"Add failed"` |
| `3` | Üye id, ISBN | `"Borrowed"` / `"Borrow failed"` |
| `4` | Üye id, ISBN | `"Returned"` / `"Return failed"` |
| `5` | Yazar | Bulunan kitaplar alt alta, hiç yoksa `"No books found"` |
| `0` | — | `"Bye"` ve program biter |
| başka sayı | — | `"Invalid choice"` |

Bu tabloda olmayan bir şey eklemek serbest.

### Proje sonu düşünme soruları (chat'e, 2-3 cümle)
1. `borrowBook` `false` döndü. Kullanıcıya **neden** başarısız olduğunu söyleyebiliyor musun? 4 farklı sebep var, elinde tek bir `boolean` var. → Haftanın ilk konusu (exceptions) tam bu sorunu çözüyor.
2. Kütüphane dolunca ne oluyor? Kapasiteyi baştan bilmek zorunda olmak sence gerçek bir sistemde mantıklı mı? → Collections.
3. `Book`'ta `setBorrower` public. Biri `Library`'yi atlayıp `book.setBorrower(member)` çağırırsa ne bozulur? (Üyenin sayacına bak.)

---

## 🧩 ALGORİTMA SAATİ (45 dk, bugün tek soru)

**Yöntem (değişmedi):** imza kağıda → 3-5 Türkçe cümle → test tablosu → `w2d7/Algo1.java` → LeetCode.

**A1 — [LeetCode 217: Contains Duplicate](https://leetcode.com/problems/contains-duplicate/)**
`boolean containsDuplicate(int[] nums)`: herhangi bir değer en az iki kere geçiyorsa `true`.
Üç yol, sırayla:
1. **İç içe döngü**, O(n²). Döngü sınırlarına dikkat: bir eleman kendisiyle karşılaştırılmamalı.
2. **Sırala + komşulara bak**, O(n log n). `Arrays.sort` serbest, **ama** girdi dizisini bozma (Hafta 1 sınavında `Exam1` girdiyi değiştirmişti). Önce kopyala: `nums.clone()`.
3. **`HashSet`**, O(n). E3'teki üç satırı hatırla. Bir ipucu: `set.add(x)` bir `boolean` döndürür. Ne zaman `false` döner?
- Test tablosu (en az 4): tekrar yok, tekrar en başta, tekrar en sonda (sıralamadan önce birbirinden uzak), tek eleman.
- Bonus soru (chat'e): 3. yol bugünkü `hashCode` konusuyla nasıl bağlantılı? `Integer` için `equals`/`hashCode`'u kim yazmış?

---

## 🔧 GIT (10 dk)

```bash
git add hafta-02/gun-07
git status            # sadece .java ve DERS.md (.class yok)
git commit -m "feat: week 2 day 7 - equals/hashCode and library system"
git push
```
Sonra `ILERLEME.md` Gün 12 logu → `docs: day 12 progress log`.
⚠️ Dün "Ne öğrendim" `-` kaldı. Bugün 3 madde yaz; tek kelime değil, birer kısa cümle.

---

## ✅ Gün Sonu

- [ ] Isınma (6 soru, chat'e)
- [ ] E0–E4 (E1/E2/E3 tahminler **çalıştırmadan önce** chat'e)
- [ ] Proje Parça 1 + LibraryDemo (test tablosu önce)
- [ ] Proje Parça 2 (bitmezse yarın)
- [ ] Proje düşünme soruları
- [ ] A1 üç yol
- [ ] "Kağıda Yaz" kutusu kağıtta
- [ ] Commit + push, `ILERLEME.md` Gün 12

**Yarın oturum başında soracaklarım:**
1. `Object`'in `equals`'ı neyi karşılaştırır? Neden override ederiz?
2. `equals(Book other)` yazarsan ne olur? `@Override` seni nasıl korur?
3. equals–hashCode kuralı nedir? Tersi neden şart değil?
4. `equals`'ı override edip `hashCode`'u etmezsen `HashSet`'te ne olur? Kutu modeliyle anlat.
5. `Book`'un iş anahtarı neden başlık değil ISBN?

**Sıradaki:** Proje Parça 2 bitmediyse onu bitir, sonra **Hafta 3**: Exceptions (projedeki "neden false?" sorusunun cevabı).
