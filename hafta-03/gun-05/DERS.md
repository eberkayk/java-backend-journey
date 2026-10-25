# Hafta 3 · Gün 5 (Gün 17) — `Iterator` + Hafta 3 tekrar

**Hedef:** 3 haftadır `for (Book b : books)` yazıyorsun. Bugün bu satırın arkasında ne olduğunu görüyorsun: `Iterator`. Gün 14'te gördüğün `ConcurrentModificationException`'ın asıl sebebini ve "gezerken güvenli silme"nin tek doğru yolunu öğreneceksin. Kütüphaneye "bu yazarın kitaplarını sil" özelliği gelecek ve `for (Book b : library)` yazılabilir hale gelecek. Gün sonunda hafta 3'ü toparlayıp yarınki mini sınava hazırlanıyoruz.

**Süre:** ~5 saat · Isınma 15 dk · Ders 1 sa · Egzersiz 1 sa · Proje 1 sa · Algoritma 45 dk · Tekrar + Kapanış 30 dk

**Kurallar (değişmedi):**
- Sınıf, metot, değişken ve ekran mesajları **İngilizce**
- **Değişiklik → derle → çalıştır → çıktının her satırını oku.**
- **Metot adı/imzası görevde nasılsa öyle.**
- Tahminler **çalıştırmadan önce** chat'e.
- Testleri Claude yazıp çalıştırıyor. Sen yazmıyorsun.

---

## 0. Isınma (15 dk) — notlara bakmadan, cevapları chat'e

**S1.** Bu satır derlenir mi? Derlenmezse düzelt.
```java
List<int> nums = new ArrayList<>();
```

**S2.** Bu satır ne yazdırır: negatif bir sayı mı, `0` mı, pozitif bir sayı mı? Bu sonuç sıralamada ne demek?
```java
System.out.println("b".compareTo("a"));
```

**S3.** Bu kod ne yapar?
```java
List<String> list = List.of("b", "a");
Collections.sort(list);
```

**S4.** Bu iki satır aynı sırayı mı verir? Fark ne?
```java
books.sort(null);                                      // Book: compareTo başlığa göre
books.sort(Comparator.comparing(Book::getAuthor));
```

**S5.** `Comparable` ile `Comparator` farkı ne? Aşağıda hangisi hangisi, metot adları ne?
```java
public class Book implements Comparable<Book> { ... }               // (a)
Comparator<Book> byAuthor = Comparator.comparing(Book::getAuthor);  // (b)
```

**S6.** (Dönüş değeri tuzağı — son 3 günün ortak hatası.) Çıktı ne? (Tek satır.)
```java
List<String> list = new ArrayList<>(List.of("a", "b"));
Set<String> set = new HashSet<>(list);
System.out.println(list.remove("z") + " " + set.add("a") + " " + list.size());
```

---

## 1. `for-each` aslında ne? ⭐⭐⭐ (15 dk)

Bunu yazdığında:
```java
for (String name : names) {
    System.out.println(name);
}
```

Derleyici arkada **bunu** üretir:
```java
Iterator<String> it = names.iterator();   // "baştan gezmeye başla"
while (it.hasNext()) {                    // "sırada eleman var mı?"
    String name = it.next();              // "sıradakini ver ve bir adım ilerle"
    System.out.println(name);
}
```

`Iterator<E>` bir **interface** (Gün 10). Üç metodu var:

| Metot               | Ne yapar                                         | Dönüş     |
| ------------------- | ------------------------------------------------ | --------- |
| `boolean hasNext()` | Sırada eleman var mı? (ilerlemez)                | `boolean` |
| `E next()`          | Sıradakini döndürür **ve** bir adım ilerler      | `E`       |
| `void remove()`     | **Az önce `next()`'in döndürdüğü** elemanı siler | yok       |

Bir benzetme: `Iterator` kitap rafında parmağın. `hasNext()` = "parmağımın sağında kitap var mı?" diye bakmak, `next()` = kitabı alıp parmağı bir sağa kaydırmak.

`List`, `Set`, `map.values()`, `map.keySet()`, `map.entrySet()`... hepsinde `iterator()` var. O yüzden hepsi `for-each` ile gezilebiliyor.

---

## 2. Neden `ConcurrentModificationException`? ⭐⭐⭐ (15 dk)

Gün 14'te bunu gördün:
```java
List<String> list = new ArrayList<>(List.of("a", "b", "c"));
for (String s : list) {
    if (s.equals("a")) {
        list.remove(s);     // ❌ ConcurrentModificationException
    }
}
```

Sebebi: `ArrayList` kendi içinde "kaç kere değiştirildim" sayacı tutar. `Iterator` oluşturulurken bu sayıyı not eder. Her `next()`'te kontrol eder: "Ben gezerken biri listeyi **benim haberim olmadan** değiştirmiş mi?" `list.remove(...)` iterator'a sormadan sayacı artırdı → bir sonraki `next()` farkı görür → exception.

Yani sorun "gezerken silmek" değil, **iterator'ın arkasından** silmek.

### Doğru yol 1 — `it.remove()`

```java
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String s = it.next();
    if (s.equals("a")) {
        it.remove();        // ✅ iterator'ın kendisi siliyor, haberi var
    }
}
```

`for-each` içinde iterator'a erişemezsin (gizli). O yüzden silme gerekiyorsa `while` + `Iterator`'ı **açıkça** yazarsın.

### Doğru yol 2 — `removeIf` (kısası)

```java
list.removeIf(s -> s.equals("a"));   // Gün 16'daki lambda: her eleman için "silinsin mi?"
```

Arkada aynı işi yapar. Koşul basitse bunu kullan; silerken başka iş de yapacaksan (sayaç artırmak, yazdırmak) `Iterator` yaz.

### Map'te de aynı

`map.values()` ve `map.keySet()` map'in **kendisine bakan pencerelerdir**, kopya değil. Onların iterator'ı ile silersen map'ten silinir:
```java
Iterator<Book> it = books.values().iterator();
while (it.hasNext()) {
    Book b = it.next();
    if (b.getAuthor().equals("George Orwell")) {
        it.remove();        // map'ten de silindi
    }
}
```

---

## 3. Üç tuzak ⭐⭐ (10 dk)

| Tuzak                                         | Sonuç                           |
| --------------------------------------------- | ------------------------------- |
| Eleman bitmişken `next()`                     | `NoSuchElementException`        |
| `next()` çağırmadan `remove()`                | `IllegalStateException`         |
| Aynı `next()` sonrası iki kere `remove()`     | `IllegalStateException`         |
| `List.copyOf(...)`'un iterator'ı ile `remove` | `UnsupportedOperationException` |

Bir de sessiz tuzak, exception vermez ama yanlış çalışır:
```java
while (it.hasNext()) {
    if (it.next().length() > 3) {
        System.out.println(it.next());   // ❌ ikinci next() → bir SONRAKİ eleman
    }
}
```
Kural: döngü başına **bir** `next()`, sonucu bir değişkene koy.

---

## 4. Kendi class'ını `for-each`'e açmak: `Iterable` ⭐⭐⭐ (15 dk)

`for (X x : something)` yazabilmek için `something`'in tek bir şartı var: `Iterable<X>` interface'ini implement etmesi. `Iterable`'ın tek metodu:
```java
Iterator<T> iterator();
```

Yani iki parça var:

| Parça         | Görevi                                       | Kaç tane                              |
| ------------- | -------------------------------------------- | ------------------------------------- |
| `Iterable<T>` | "Beni gezebilirsin" — yeni iterator üretir   | Bir koleksiyon                        |
| `Iterator<T>` | Gezmenin kendisi — nerede kaldığını hatırlar | Her `for-each` için **yeni** bir tane |

Örnek: içinde zaten bir `List` olan class, iterator'ı o listeden alabilir:
```java
public class Shelf implements Iterable<String> {
    private final List<String> titles = new ArrayList<>();

    @Override
    public Iterator<String> iterator() {
        return titles.iterator();     // hazır bir iterator'ı döndürmek yeterli
    }
}

// main:
for (String t : shelf) { ... }        // artık derlenir
```

Her `for-each` başında `iterator()` **yeniden** çağrılır → aynı nesneyi iki kere gezebilirsin, ikisi birbirini etkilemez.

---

## 📝 Kağıda Yaz

```
for-each = iterator() + while(hasNext()) + next()
next()   = sıradakini ver + bir adım ilerle (bitmişse NoSuchElementException)
Gezerken silme: list.remove ❌ (CME) → it.remove() ✅ veya removeIf ✅
Döngü başına BİR next(), sonucu değişkene
map.values()/keySet() = pencere → iterator ile silersen map'ten silinir
Iterable = "beni gez" (iterator() üretir) · Iterator = gezmenin kendisi
for (X x : obj) için obj → Iterable<X> olmalı
```

---

## 💪 EGZERSİZLER (~1 saat)

Klasör: `hafta-03/gun-05/w3d5/` · Paket: `package w3d5;`

### E1 — `IteratorDemo.java` (tahmin)

Kodu yaz, **çalıştırmadan** (1)–(7) için tahminlerini chat'e yaz, sonra çalıştır.

```java
package w3d5;

import java.util.*;

public class IteratorDemo {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>(List.of("Ali", "Bora", "Can", "Deniz"));
        Iterator<String> it = names.iterator();
        while (it.hasNext()) {
            String n = it.next();
            if (n.length() == 3) {
                it.remove();
            }
        }
        System.out.println(names);                      // (1)

        List<Integer> nums = new ArrayList<>(List.of(1, 2, 3, 4));
        nums.removeIf(x -> x % 2 == 0);
        System.out.println(nums);                       // (2)

        Iterator<Integer> it2 = nums.iterator();
        System.out.println(it2.next());                 // (3)
        System.out.println(it2.next());                 // (4)
        System.out.println(it2.hasNext());              // (5)

        try {
            it2.next();
        } catch (RuntimeException e) {
            System.out.println(e.getClass().getSimpleName());   // (6)
        }

        List<String> letters = new ArrayList<>(List.of("a", "b", "c"));
        try {
            for (String s : letters) {
                if (s.equals("a")) {
                    letters.remove(s);
                }
            }
        } catch (RuntimeException e) {
            System.out.println(e.getClass().getSimpleName());   // (7)
        }
    }
}
```

Çalıştırdıktan sonra: (7)'deki döngüyü `Iterator` + `it.remove()` ile yeniden yaz (aynı dosyada, altına), `letters`'ı yazdır.

### E2 — `Countdown` (kod yaz)

`for (int n : new Countdown(3))` yazınca `3 2 1` çıksın. İki class + bir demo.

**1. `Countdown implements Iterable<Integer>`** (`Countdown.java`)
- Field: `private final int start;`
- Constructor: `public Countdown(int start)` → `start < 0` ise `IllegalArgumentException("Start must not be negative")`
- Metot: `public Iterator<Integer> iterator()` → her çağrıda `new CountdownIterator(start)` döndürür

**2. `CountdownIterator implements Iterator<Integer>`** (`CountdownIterator.java`)
- Field: `private int current;` (sıradaki verilecek sayı)
- Constructor: `public CountdownIterator(int start)`
- `public boolean hasNext()` → verilecek sayı kaldıysa `true` (`1`'den sonra biter, `0` verilmez)
- `public Integer next()` → bitmişse `NoSuchElementException("Countdown finished")`; değilse şu anki sayıyı döndürür ve bir azaltır
- `remove()` yazma (interface'te `default` var, Gün 10 — dokunmazsan `UnsupportedOperationException` atar)
- Import: `java.util.Iterator`, `java.util.NoSuchElementException`

**3. `CountdownDemo.java`** — `main`'de:
- `Countdown c = new Countdown(3);` → `for-each` ile yan yana yazdır: `3 2 1`
- Aynı `c`'yi **ikinci kez** `for-each` ile gez, yine yazdır
- `new Countdown(0)` ile gez → hiçbir şey yazmamalı
- `new Countdown(-1)` → exception mesajını yakalayıp yazdır

**Soru (chat'e, 1-2 cümle):** Aynı `c`'yi ikinci kez gezince neden yine `3 2 1` çıktı, `current` neden sıfırda kalmadı?

### E3 — Sessiz tuzak (tahmin)

`names = ["Ali", "Bora", "Can", "Deniz", "Ece"]` iken çıktı ne? Çalıştırmadan chat'e yaz, sonra `IteratorDemo`'nun sonuna ekleyip dene.
```java
Iterator<String> it3 = names.iterator();
while (it3.hasNext()) {
    if (it3.next().length() > 3) {
        System.out.println(it3.next());
    }
}
```
(`names`'i E1'den bağımsız, yeni bir `List.of(...)` ile oluştur.) Sonra: tek `next()` ile düzelt, "4 harften uzun olanları" (`Bora`, `Deniz`) yazdırsın.

---

## 🏛️ PROJE — Kütüphane v7: güvenli silme + `for (Book b : library)` (~1 saat)

### Adım 0 — Kopyala

`hafta-03/gun-04/w3d4/` içindeki proje dosyalarını (`Book`, `Member`, `StudentMember`, `StaffMember`, `Library`, `LibraryApp`, 6 exception) `hafta-03/gun-05/w3d5/`'e kopyala, paketi `w3d5` yap. `Pair`, `RawListDemo`, `SortDemo`, `Algo*` kopyalanmaz.

### Adım 1 — `Library.removeBooksByAuthor`

```java
public int removeBooksByAuthor(String author)
```
- `author` `null` veya boşsa → `IllegalArgumentException("Author must not be blank")`
- O yazarın **müsait** (ödünç verilmemiş) bütün kitaplarını siler. Ödünçteki kitaplar **kalır**.
- Kaç kitap sildiğini döndürür (hiç yoksa `0`, exception değil)
- **Zorunlu:** `books.values()` üzerinde `Iterator` + `it.remove()` kullan. (`removeIf` de çalışır ama bugün `Iterator`'ı yazmak istiyorum, çünkü silerken sayaç da tutman gerekiyor.)

### Adım 2 — `Library implements Iterable<Book>`

- Class satırı: `public class Library implements Iterable<Book>`
- `public Iterator<Book> iterator()` → kitapları **başlık sırasıyla** gezsin. Sıralı listeyi üreten metodun zaten var; onu yeniden yazma, kullan.
- Import: `java.util.Iterator` (zaten `java.util.*` varsa gerek yok)

### Adım 3 — `LibraryApp`

- **Menü 1:** `getAllBooks()` yerine `for (Book book : library)` ile yazdır. Kitap yoksa `No books`.
- **Yeni menü 10) Remove books by author:** yazar adını sor → `library.removeBooksByAuthor(...)` → `Removed 2 books` yazdır. `IllegalArgumentException` gelirse `Error: <mesaj>`.
- Menü listesine `10) Remove books by author` satırını ekle (`0) Exit`'in üstüne).

Manuel kontrol (istersen): menü 3 ile `Dune`'u ödünç ver → menü 10 `Frank Herbert` → `Removed 1 books` (Dune ödünçte, kalır) → menü 1.

### Proje sonu düşünme soruları (chat'e, 1-2 cümle)

1. `removeBooksByAuthor`'ı şöyle yazsaydın ne olurdu, neden?
   ```java
   for (Book b : books.values()) {
       if (b.getAuthor().equals(author) && b.isAvailable()) {
           books.remove(b.getIsbn());
       }
   }
   ```
2. Biri dışarıdan şunu yazarsa ne olur? Bu kütüphane için iyi bir şey mi, neden?
   ```java
   Iterator<Book> it = library.iterator();
   it.next();
   it.remove();
   ```

---

## 🧩 ALGORİTMA SAATİ (45 dk) — Gezerken silmek / saymak

**A1 — [LeetCode 27: Remove Element](https://leetcode.com/problems/remove-element/)**
`int removeElement(int[] nums, int val)`: dizideki bütün `val`'leri "sil", kalan eleman sayısını `k` döndür. İlk `k` eleman `val` olmayanlar olmalı (sıraları önemli değil).
- Örnek: `nums = [3,2,2,3]`, `val = 3` → `2`, `nums` ilk iki eleman `[2,2,...]`
- Diziden gerçekten silemezsin (boyu sabit, Gün 4). Fikir: bir **yazma index'i** (`k`) tut. Her elemana bak; `val` değilse `nums[k]`'ya yaz, `k`'yı artır.
- Bu Gün 8'deki LC 26'nın (Remove Duplicates) kardeşi; aynı "iki index" kalıbı.
- Önce `nums = [0,1,2,2,3,0,4,2]`, `val = 2` için `k`'nın ve dizinin her adımda nasıl değiştiğini chat'e 3-4 satırla yaz, sonra kodla.

**A2 — [LeetCode 1207: Unique Number of Occurrences](https://leetcode.com/problems/unique-number-of-occurrences/)**
`boolean uniqueOccurrences(int[] arr)`: her sayının kaç kere geçtiği birbirinden farklıysa `true`.
- Örnek: `[1,2,2,1,1,3]` → 1 üç kere, 2 iki kere, 3 bir kere → sayılar `3, 2, 1` hepsi farklı → `true`. `[1,2]` → ikisi de bir kere → `false`
- Hafta 3'ün iki aracı birlikte: önce `Map` ile say (Gün 15 sayma kalıbı), sonra sayıları bir `Set`'e koy. "Hepsi farklı mı?" sorusunu `Set` nasıl cevaplar? (İpucu: `add`'in **dönüş değeri** veya `size()`.)
- Önce planını 3-4 cümleyle chat'e yaz.

---

## 🔁 HAFTA 3 TEKRAR (15 dk) — yarın mini sınav

Yarın (Gün 18) **Hafta 3 mini sınavı**: kapalı kitap, Gün 13–17. Format Hafta 2 ile aynı: teori + çıktı tahmini + kısa kod. Bugün aşağıdaki listeye bak:

| Gün | Konu                  | Bilmen gereken çekirdek                                                        |
| --- | --------------------- | ------------------------------------------------------------------------------ |
| 13  | Exceptions            | checked/unchecked, `try/catch/finally` akışı, catch sırası, kendi exception'ın |
| 14  | `ArrayList` / `List`  | iç yapı (dizi + büyüme), `remove(int)` vs `remove(Object)`, `List.of` değişmez |
| 15  | `HashMap` / `HashSet` | `put`/`get`/`add` dönüş değerleri, `hashCode` + `equals` ilişkisi              |
| 16  | Generics, sıralama    | `List<Integer>` neden, `compareTo` işareti, `Comparable` vs `Comparator`       |
| 17  | `Iterator`            | `for-each`'in arkası, CME sebebi, `it.remove()`, `Iterable`                    |

**Chat'e (1 satır):** En zayıf hissettiğin 2 konu hangisi? Yarın sınavdan önceki 30 dk tekrarı onlara ayıracağız.

---

## 🔧 GIT (10 dk)

```bash
git add hafta-03/gun-05
git status            # sadece .java ve DERS.md (.class yok)
git commit -m "feat: week 3 day 5 - iterator"
git push
```
Sonra `ILERLEME.md`'ye Gün 17 logu → `docs: day 17 progress log`.

---

## ✅ Gün Sonu

- [ ] Isınma (6 soru, chat'e)
- [ ] E1–E3 (tahminler **çalıştırmadan önce** chat'e)
- [ ] Proje Adım 0–3
- [ ] Proje düşünme soruları
- [ ] A1 + A2
- [ ] Hafta 3 tekrar: zayıf 2 konu chat'e
- [ ] "Kağıda Yaz" kutusu kağıtta
- [ ] Commit + push, `ILERLEME.md` Gün 17

**Yarın oturum başında soracaklarım:** Yarın isınma yok, doğrudan **tekrar (30 dk) + Hafta 3 mini sınavı**. `SINAV.md` yarın `hafta-03/gun-06/` içinde hazır olacak.

**Sıradaki:** Hafta 3 Gün 6 (Gün 18): **Hafta 3 mini sınavı**. Sonra Hafta 4.
