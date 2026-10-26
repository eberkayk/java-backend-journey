# Hafta 3 · Gün 6 (Gün 18) — Hafta 3 Tekrarı + Mini Sınav

**Hedef:** Gün 13–17'yi (exceptions, `List`, `HashMap`/`HashSet`, generics + sıralama, `Iterator`) kapalı kitap bir sınavla ölçmek. Puan değil, **nerede boşluk olduğu** önemli — Hafta 4'e geçmeden neyi tekrar edeceğimize bununla karar vereceğiz.

**Süre:** ~2.5 saat · Tekrar 45 dk · Mini sınav 85 dk · Kapanış 20 dk

**Kurallar (değişmedi):**
- Sınıf, metot, değişken ve ekran mesajları **İngilizce**
- **Değişiklik → derle → çalıştır → çıktının her satırını oku.**
- **Metot adı/imzası görevde nasılsa öyle.**
- Hesaplayan metot yazdırmaz, **return** eder.

Bugün ısınma yok — dün söylediğin 4 zayıf konuyu (List, HashMap/HashSet, generics + sıralama, Iterator) tekrar ediyoruz.

---

## 1. Tekrar (45 dk) — sınavdan ÖNCE, notlara bakmak serbest

1. Gün 14–17'nin **"Kağıda Yaz" kutularını** baştan oku (4 kutu, ~10 dk).
2. Aşağıdaki 4 konuyu oku. Her birinde kısa bir kod + açıklama var (~30 dk).
3. Takıldığın bir şey varsa **sınavdan önce** chat'e sor (~5 dk). Sınav başladıktan sonra soru yok.

### 1.1 Metotların DÖNÜŞ değerleri (4 gündür kaçan konu)

Bir metodu çağırınca iki şey olur: (1) iş yapılır, (2) bir değer **geri döner**. `println(...)` içine yazarsan ekrana **dönen değer** basılır, yapılan iş değil.

```java
List<String> list = new ArrayList<>(List.of("a", "b"));
System.out.println(list.add("c"));      // true    (eklendi mi?)
System.out.println(list.remove(0));     // a       (silinen ELEMAN)
System.out.println(list.remove("zz"));  // false   (silindi mi?)

Map<String, Integer> m = new HashMap<>();
System.out.println(m.put("k", 1));      // null    (önceki değer: yoktu)
System.out.println(m.put("k", 9));      // 1       (önceki değer)
System.out.println(m.get("yok"));       // null    (exception DEĞİL)

Set<String> s = new HashSet<>();
System.out.println(s.add("x"));         // true    (yeni eklendi)
System.out.println(s.add("x"));         // false   (zaten vardı, eklenmedi)
```

Ezber kuralı:
```
add / remove(obj) / set.add  → boolean  ("oldu mu?")
list.remove(index)           → silinen eleman
map.put                      → ESKİ değer (yoksa null)
map.get                      → değer (yoksa null)
```

Dikkat: `List<Integer>` üzerinde `remove(1)` → **index 1**'i siler. `1` sayısını silmek için `remove(Integer.valueOf(1))`.

### 1.2 `Comparable` vs `Comparator`

```java
// 1) Comparable: class'ın İÇİNDE, "doğal sıra". Tek bir tane olur.
class Book implements Comparable<Book> {
    @Override
    public int compareTo(Book other) {          // metot adı: compareTo, 1 parametre
        return title.compareTo(other.title);
    }
}
books.sort(null);                               // null = "doğal sırayı kullan"

// 2) Comparator: class'ın DIŞINDA, "hakem". İstediğin kadar olur.
Comparator<Book> byAuthor = Comparator.comparing(Book::getAuthor);
books.sort(byAuthor);                           // metot adı: compare, 2 parametre
books.sort(byAuthor.thenComparing(Book::getTitle));   // eşitse başlığa bak
books.sort(byAuthor.reversed());                       // tersten
```

Ne zaman hangisi:
```
Class'ın "normal" sırası tek ve belli    → Comparable (compareTo)
Farklı yerlerde farklı sıralar lazım     → Comparator (compare / comparing)
Class'ı değiştiremiyorsun (String vb.)   → Comparator
```

`compareTo` / `compare` sonucu: **negatif → a önce**, `0` → eşit, **pozitif → a sonra**. Sayılarda `a - b` değil `Integer.compare(a, b)` / `Double.compare(a, b)` (taşma riski).

### 1.3 `List.of` / `List.copyOf` → değiştirilemez

```java
List<Integer> a = List.of(3, 1, 2);
a.add(4);                         // UnsupportedOperationException (fırlatır, sessiz geçmez)
a.sort(null);                     // UnsupportedOperationException (sıralamak da değiştirmek)

List<Integer> b = new ArrayList<>(List.of(3, 1, 2));   // değiştirilebilir KOPYA
b.sort(null);                     // [1, 2, 3]
```

Kütüphanede kullandığın kalıp: "kopyala → sırala → `List.copyOf` ile kilitle → dışarı ver".

### 1.4 for-each'in arkası ve CME

```java
for (String s : list) { ... }
// Java bunu şuna çevirir:
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String s = it.next();
    ...
}
```

- Gezerken `list.remove(...)` → listeyi iterator'ın **arkasından** değiştirdin → bir sonraki `next()`'te `ConcurrentModificationException`.
- Doğru yollar: `it.remove()` (açık `while` döngüsüyle) veya `list.removeIf(x -> ...)`.
- Döngü başına **bir** `next()`; sonucu değişkene al.
- `for (X x : obj)` yazabilmek için `obj`'nin class'ı `Iterable<X>` olmalı → `iterator()` metodu.

---

## 🧪 HAFTA 3 MİNİ SINAV (85 dk) — kapalı kitap

**Kurallar:** Notlara, DERS dosyalarına, eski koduna, internete bakmak **yok**. IntelliJ açık olabilir ama **AI kapalı**. Başlangıç saatini `SINAV.md`'ye yaz, süreyi tut. 85 dk dolduğunda nerede kaldıysan bırak — yarım kod da teslim edilir.

- Teori ve çıktı cevapları → `hafta-03/gun-06/SINAV.md` (sorular hazır, sadece `Cevap:` satırlarını doldur)
- Kodlar → package **`w3d6`** (`hafta-03/gun-06/w3d6/`, Sources Root ayarını unutma)
- **Ben kontrol edeceğim (testleri ben yazıp çalıştıracağım) — kendin "doğru" diye işaretleme.**

**Puanlama:** A teori 30 · B çıktı 20 · C kod 50

### Bölüm A — Teori (20 dk) → `SINAV.md`
### Bölüm B — Çıktı tahmini (15 dk, ÇALIŞTIRMADAN) → `SINAV.md`
Sorular `SINAV.md` içinde.

### Bölüm C — Kod (50 dk)

Her public class kendi dosyasında, package `w3d6`.

**C1 (25 puan) — Depo / Stok sistemi.** Kütüphanenin küçük kardeşi; aynı fikirler, yeni alan.

#### 1. `class InvalidQuantityException extends RuntimeException`
- Constructor: `(String message)` → `super(message)`

#### 2. `class Product implements Comparable<Product>`
- Field'lar: `private final String name`, `private final double price`, `private int stock`
- Constructor `(String name, double price, int stock)`, kontroller bu sırayla:
  - `name` `null` veya boş/sadece boşluk → `IllegalArgumentException("Name must not be blank")`
  - `price < 0` → `InvalidQuantityException("Price must not be negative")`
  - `stock < 0` → `InvalidQuantityException("Stock must not be negative")`
- Getter'lar: `getName()`, `getPrice()`, `getStock()`
- `public void decreaseStock(int qty)` → `stock`'u `qty` kadar azaltır. Kontrol **yapmaz** (kontrol `Inventory.sell`'de).
- `compareTo(Product other)` → **isme göre** (String'in doğal sırası)
- `toString()` → `"Pen | 2.5 | 10"` formatında (isim, fiyat, stok)

#### 3. `class Inventory implements Iterable<Product>`
- Field: `private final Map<String, Product> products = new HashMap<>();` — anahtar = ürün adı
- Constructor: yok (varsayılan yeter)
- `public void add(Product p)`
  - `p == null` → `IllegalArgumentException("Product must not be null")`
  - aynı isimde ürün zaten varsa → `IllegalArgumentException("Product already exists: Pen")` (sondaki isim ürünün adı)
- `public Product find(String name)`
  - yoksa → `IllegalArgumentException("No such product: Glue")`
- `public void sell(String name, int qty)` — kontroller bu sırayla:
  - ürünü `find` ile bul (yoksa `find`'ın exception'ı dışarı çıksın)
  - `qty <= 0` → `InvalidQuantityException("Quantity must be positive")`
  - `qty` stoktan büyükse → `InvalidQuantityException("Not enough stock: Pen")`
  - değilse `decreaseStock(qty)`
- `public List<Product> sortedByName()` → tüm ürünler **isme göre** (doğal sıra), **değiştirilemez** liste
- `public List<Product> sortedByPriceDesc()` → fiyat **büyükten küçüğe**, fiyat eşitse **isme göre**; **değiştirilemez** liste. `Comparator` kullan.
- `public int removeOutOfStock()` → stoğu `0` olan ürünleri map'ten siler, kaç tane sildiğini döner. **`products.values().iterator()` + `it.remove()` zorunlu.**
- `public Iterator<Product> iterator()` → ürünleri **isme göre sıralı** gezsin.

#### 4. `Exam1.java` → `main`, bu sırayla:
1. `Inventory` oluştur, şu 4 ürünü ekle: `Pen 2.5 10` · `Notebook 12.0 3` · `Eraser 1.0 0` · `Ink 12.0 5`
2. Tekrar `Pen` eklemeyi dene → yakala, `getMessage()`'ı yazdır
3. `sell("Notebook", 3)` (başarılı olmalı)
4. Şu üçünü **ayrı ayrı** dene, her birini yakala, mesajı yazdır: `sell("Pen", 20)` · `sell("Glue", 1)` · `sell("Pen", 0)`
5. `sortedByName()` sonucunu yazdır
6. `sortedByPriceDesc()` sonucunu yazdır
7. `"Removed: " + removeOutOfStock()` yazdır
8. `for (Product p : inventory)` ile kalan ürünleri satır satır yazdır

---

**C2 (15 puan) — `Exam2.java`:** `static int[] frequencySort(int[] nums)`
Sayıları **tekrar sayısına göre artan** sırala. Tekrar sayısı eşitse **büyük sayı önce**. Yeni dizi döndür.

```
{1, 1, 2, 2, 2, 3}             → {3, 1, 1, 2, 2, 2}
{2, 3, 1, 3, 2}                → {1, 3, 3, 2, 2}
{-1, 1, -6, 4, 5, -6, 1, 4, 1} → {5, -1, 4, 4, -6, -6, 1, 1, 1}
```
- İpucu: önce `Map` ile say → sayıları `List<Integer>`'a koy → `Comparator` ile sırala → `int[]`'e geri çevir.
- `main`'de yukarıdaki 3 örneği yazdır (`Arrays.toString`).
- Sınavdan sonra: bu **LeetCode 1636**, gönderebilirsin.

---

**C3 (10 puan) — `Exam3.java`:** `static <T> List<T> removeDuplicates(List<T> list)`
Tekrarları at, **ilk görünme sırası** korunsun. Girdi listesi **değişmesin**, yeni liste döndür.

```
[3, 1, 3, 2, 1]   → [3, 1, 2]
["b", "a", "b"]   → [b, a]
[]                → []
```
- `list == null` → `IllegalArgumentException("List must not be null")`
- **O(n) olmalı** — sonuç listesinde `contains` ile aramak O(n²), yasak. (Hangi yapı "var mı?" sorusunu O(1) cevaplıyordu?)
- `main`'de en az 3 test: biri `Integer`, biri `String` listesi (generic çalıştığını göster), biri boş liste. Ayrıca girdi listesini metottan **sonra** yazdır, değişmediğini göster.

---

## 📝 Kağıda Yaz (sınavdan sonra, 5 dk)

```
HAFTA 3 — 5 CÜMLE
1. Hata → exception fırlat; checked olanı ya yakala ya throws yaz.
2. List: sıra/index · Set: tekrar yok, var mı O(1) · Map: anahtarla bul/say.
3. add/remove → boolean · map.put → eski değer · map.get yoksa → null.
4. Comparable = içerideki doğal sıra (compareTo) · Comparator = dışarıdan hakem (compare).
5. for-each = gizli iterator; gezerken silme → it.remove() veya removeIf.
```

---

## 🔧 GIT (10 dk)

```bash
git add hafta-03/gun-06
git status            # sadece .java ve .md (.class yok)
git commit -m "feat: week 3 mini exam"
git push
```
Ben notu verdikten sonra: `ILERLEME.md` → Gün 18 logu + **Haftalık Kontrol tablosunda Hafta 3 satırı** (mini sınav puanı + bir cümle not) → `docs: day 18 progress log`.

---

## ✅ Gün Sonu

- [ ] Tekrar 45 dk (Kağıda Yaz kutuları + 1.1–1.4)
- [ ] Sınav: `SINAV.md` A + B, C1 class'ları + `Exam1`–`Exam3`
- [ ] Not verildikten sonra hataları birlikte düzelt
- [ ] "Kağıda Yaz" kutusu
- [ ] Commit + push, `ILERLEME.md` Gün 18 + Hafta 3 satırı

**Sıradaki:** Hafta 4 — PLAN.md'ye göre devam.
