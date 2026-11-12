# Hafta 5 · Gün 3 (Gün 27) — TEKRAR: Generics + Comparable / Comparator + Iterator

**Hedef:** Yeni konu yok. Hafta 3'teki generics, sıralama ve `Iterator` / `ConcurrentModificationException` konularını **notsuz yazabilir** hâle getirmek.
Kapı sınavında (Gün 31) bugünkü konular ~15 puan.

**Süre:** ~5-6 saat
Isınma 15 dk · Konu özeti 30 dk · Çıktı tahmini 20 dk · Notsuz kod görevi 1.5–2 sa · Algoritma 1 sa · Kapanış 15 dk

**Kurallar (değişmedi):**
- Sınıf, metot, değişken ve mesajlar **İngilizce**
- **Değişiklik → derle → çalıştır → çıktının her satırını oku.**
- **Metot adı/imzası görevde nasılsa öyle.** Exception mesajları **birebir**.
- `main`'e test yazmak yok. Testleri ben yazıp çalıştırıyorum.

**Kurulum:** `hafta-05/gun-03/` klasöründe package **`w5d3`** (`hafta-05/gun-03/w5d3/`). `gun-03`'ü Sources Root yap.

---

## 0. Isınma (15 dk) — çıktıyı chat'e yaz

Bir haftalık aradan sonra en çok kaçan 3 yazdırma kuralı, yeni değerlerle.

**S1.**
```java
record Book(String title, int year) {}
System.out.println(new Book("Dune", 1965));
```

**S2.**
```java
Map<String, Integer> m = new TreeMap<>();
m.put("pear", 3);
m.put("apple", 5);
System.out.println(m);
System.out.println(m.keySet());
```

**S3.**
```java
Optional<Integer> a = Optional.of(4);
System.out.println(a);
System.out.println(a.map(x -> x * 10));
System.out.println(LocalDate.of(2026, 1, 9));
```

---

## 1. Konu Özeti (30 dk) — oku, takıldığın yeri chat'e sor

### 1.1 Generics — `<T>` = "tipi sonra söyleyeceğim"

```java
class Box<T> {                       // T: kutuyu kullanan söyler
    private final T item;
    Box(T item) { this.item = item; }
    T get() { return item; }
}
Box<String> b = new Box<>("hi");     // T = String
String s = b.get();                  // cast yok
```

- Generic **metot**: `<T>` dönüş tipinin **önüne** yazılır:
  `public static <T> T firstOf(List<T> list)`
- İki tip parametresi: `class Pair<A, B>`, `static <A, B> Pair<B, A> swap(Pair<A, B> p)`
- `T` yerine **primitive yazılmaz**: `List<int>` ❌ → `List<Integer>` ✅

### 1.2 `compareTo` / `compare` → sadece bir **sayı** döndürür

Sıralama bu sayının **işaretine** bakar, "a ile b'yi karşılaştır" diye okunur:

| Dönüş       | Anlamı                   |
| ----------- | ------------------------ |
| **negatif** | a, b'den **önce** gelir  |
| **0**       | eşit                     |
| **pozitif** | a, b'den **sonra** gelir |

```java
Integer.compare(2, 9)      // -1  → 2 önce (küçükten büyüğe)
Integer.compare(9, 2)      //  1
"ant".compareTo("bee")     // negatif → "ant" önce
```

Kendi sınıfına **doğal sıra** vermek = `Comparable`:
```java
record Item(String name, int price) implements Comparable<Item> {
    @Override
    public int compareTo(Item other) {
        return Integer.compare(this.price, other.price);   // ucuz → pahalı
    }
}
```
`Integer.compare(other.price, this.price)` yazarsan **ters** olur (pahalı → ucuz).

### 1.3 `Comparator` zinciri

```java
Comparator.comparing(Item::name)            // String / Comparable alan
Comparator.comparingInt(Item::price)        // int alan
    .thenComparing(Item::name)              // eşitse ikinci kural
Comparator.naturalOrder()                   // compareTo'ya göre
Comparator.reverseOrder()                   // compareTo'nun tersi
```

⚠️ **`reversed()` kendinden ÖNCEKİ zincirin tamamını ters çevirir:**

| Yazdığın                                                    | Sonuç                              |
| ----------------------------------------------------------- | ---------------------------------- |
| `comparingInt(price).reversed().thenComparing(name)`        | fiyat **büyük → küçük**, isim A→Z  |
| `comparingInt(price).thenComparing(name).reversed()`        | fiyat büyük → küçük, isim **Z→A**  |

### 1.4 Nerede kullanılır

```java
list.sort(cmp);                       // listeyi YERİNDE sıralar, void
Collections.sort(list);               // doğal sıra (compareTo), void
List<Item> copy = new ArrayList<>(list);
copy.sort(cmp);                       // orijinale dokunmadan sıralı kopya
new TreeSet<>(cmp)                    // sıralı set
new TreeMap<>(Comparator.reverseOrder())  // key'ler büyükten küçüğe
```

- `List.of(...).sort(...)` → **UnsupportedOperationException** (değiştirilemez liste).
- ⚠️ **TreeSet / TreeMap eşitliğe `equals` ile değil, `compare == 0` ile bakar.** Comparator'a göre "eşit" olan ikinci eleman **eklenmez**.
  ```java
  Set<String> s = new TreeSet<>(Comparator.comparingInt(String::length));
  s.add("ab"); s.add("cd");     // ikisi de uzunluk 2 → "cd" eklenmez
  System.out.println(s);        // [ab]
  ```

### 1.5 `Iterator` ve CME

`for-each` arka planda `Iterator` kullanır. Gezerken listeyi **listenin kendi metoduyla** (`list.remove`, `list.add`) değiştirirsen → **`ConcurrentModificationException`**.

```java
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String x = it.next();            // next() her turda TAM 1 kez
    if (x.isEmpty()) {
        it.remove();                 // ✅ iterator'ün kendi remove'u
    }
}
list.removeIf(x -> x.isEmpty());     // ✅ kısası; boolean döner (silindi mi?)
```

### ✍️ Kağıda Yaz

```
<T> metotta dönüş tipinin önüne: static <T> T firstOf(List<T> l)
compare(a, b): negatif → a önce · 0 → eşit · pozitif → a sonra
Integer.compare(this.x, o.x) = küçükten büyüğe · (o.x, this.x) = ters
reversed() → kendinden ÖNCEKİ zincirin hepsini çevirir
TreeSet/TreeMap: compare == 0 → aynı eleman sayılır, eklenmez
list.sort / Collections.sort → void, yerinde · List.of(...).sort → UOE
for-each içinde list.remove → CME · it.remove() ya da removeIf
```

---

## 2. Çıktı Tahmini (20 dk) — ÇALIŞTIRMADAN, cevaplar chat'e

Her snippet `main` içinde. Exception çıkarsa **hangi exception** olduğunu yaz, sonrasında yazdırılmayan satırları yazma.

**P1.**
```java
class Box<T> {
    private final T item;
    Box(T item) { this.item = item; }
    T get() { return item; }
}
Box<String> b = new Box<>("hello");
Box<Integer> n = new Box<>(7);
System.out.println(b.get().length() + n.get());
```

**P2.**
```java
System.out.println(Integer.compare(3, 8));
System.out.println(Integer.compare(8, 3));
System.out.println("apple".compareTo("banana") < 0);
System.out.println("b".compareTo("b"));
```

**P3.**
```java
List<String> w = new ArrayList<>(List.of("kiwi", "fig", "banana", "pear"));
w.sort(Comparator.comparingInt(String::length));
System.out.println(w);
```
(İpucu: uzunluğu eşit olanlar **eski sıralarını korur**.)

**P4.**
```java
record P(String name, int age) {}
List<P> ps = new ArrayList<>(List.of(new P("Can", 30), new P("Ada", 25), new P("Bora", 30)));
ps.sort(Comparator.comparingInt(P::age).reversed().thenComparing(P::name));
System.out.println(ps.get(0).name() + " " + ps.get(1).name() + " " + ps.get(2).name());
ps.sort(Comparator.comparingInt(P::age).thenComparing(P::name).reversed());
System.out.println(ps.get(0).name() + " " + ps.get(1).name() + " " + ps.get(2).name());
```

**P5.**
```java
Set<String> s = new TreeSet<>(Comparator.comparingInt(String::length));
s.add("cat");
s.add("dog");
s.add("horse");
s.add("ox");
System.out.println(s);
System.out.println(s.size());
```

**P6.**
```java
List<Integer> xs = new ArrayList<>(List.of(1, 2, 3, 4));
for (Integer x : xs) {
    if (x == 2) {
        xs.remove(x);
    }
}
System.out.println(xs);
```

**P7.**
```java
List<String> names = new ArrayList<>(List.of("Ada", "Bob", "Ali", "Can"));
Iterator<String> it = names.iterator();
while (it.hasNext()) {
    if (it.next().startsWith("A")) {
        it.remove();
    }
}
System.out.println(names);
System.out.println(names.removeIf(n -> n.length() > 5));
```

**P8.**
```java
List<Integer> nums = List.of(5, 1, 4);
List<Integer> copy = new ArrayList<>(nums);
copy.sort(Comparator.reverseOrder());
System.out.println(copy);
System.out.println(nums);
nums.sort(Comparator.naturalOrder());
System.out.println(nums);
```

**P9.**
```java
record Score(String name, int pts) implements Comparable<Score> {
    public int compareTo(Score o) {
        return Integer.compare(o.pts, pts);
    }
}
List<Score> sc = new ArrayList<>(List.of(new Score("a", 5), new Score("b", 9), new Score("c", 7)));
Collections.sort(sc);
System.out.println(sc.get(0));
```

---

## 3. Notsuz Kod Görevi (1.5–2 sa) — Görev Panosu

**Kural:** DERS dosyalarına (bu dosyanın özet kısmı dahil), eski koduna, internete bakmak **yok**. AI kapalı. Bu bölümün **görev metni** açık kalabilir. Takılırsan önce 10 dk kendin dene, sonra chat'e sor.

Her public tip kendi dosyasında, package `w5d3`. Döngü de stream de serbest (aksi yazmıyorsa).

### 3.1 `record Task(String title, int priority, LocalDate due) implements Comparable<Task>` (`Task.java`)

- **Compact constructor**, kontroller bu sırayla:
  - `title == null` ya da `title.isBlank()` → `IllegalArgumentException("Title must not be blank")`
  - `priority < 1` ya da `priority > 5` → `IllegalArgumentException("Bad priority: " + priority)`
  - `due == null` → `IllegalArgumentException("Due must not be null")`
- `public int compareTo(Task other)` → **doğal sıra:** `due` erken olan önce; `due` eşitse `title` alfabetik.
  (`LocalDate` da `compareTo` bilir: `a.compareTo(b)`.)

### 3.2 `final class Pair<A, B>` (`Pair.java`) — record DEĞİL, elle yaz

**Field'lar:**
- `private final A first;`
- `private final B second;`

**Constructor:** `public Pair(A first, B second)` (kontrol yok, `null` serbest).

**Metotlar:**
- `public A getFirst()`, `public B getSecond()`
- `toString` → `(first, second)`: `new Pair<>("x", 3)` → `(x, 3)`

### 3.3 `final class ListUtils` (`ListUtils.java`) — sadece `static` generic metotlar

**Constructor:** `private ListUtils() {}` (nesne oluşturulmasın).

**Metotlar:**
- `public static <T> List<T> topN(List<T> list, Comparator<T> order, int n)`
  - `n < 0` → `IllegalArgumentException("Bad n: " + n)`
  - `order`'a göre sıralanmış **yeni** listenin ilk `n` elemanı. `n` liste boyundan büyükse hepsi.
  - **Gelen liste değişmemeli** (`List.of(...)` da gelebilir).
- `public static <T> Optional<T> firstMatch(List<T> list, Predicate<T> condition)` → şartı sağlayan **ilk** eleman; yoksa `Optional.empty()`.
- `public static <A, B> Pair<B, A> swap(Pair<A, B> pair)` → elemanları yer değiştirmiş **yeni** `Pair`: `(x, 3)` → `(3, x)`

### 3.4 `class TaskBoard` (`TaskBoard.java`)

**Field:**
- `private final List<Task> tasks = new ArrayList<>();` (eklenme sırasını tutar)

**Constructor:** yok (varsayılan yeterli).

**Metotlar:**
- `public void add(Task task)`
  - `task == null` → `IllegalArgumentException("Task must not be null")`
  - Aynı `title`'a sahip görev zaten varsa → `IllegalStateException("Duplicate title: " + title)`
  - Değilse sona ekle.
- `public int size()`
- `public List<Task> sortedNatural()` → doğal sıraya (`compareTo`) göre sıralı **yeni** liste. `tasks`'ın eklenme sırası **bozulmamalı**.
- `public List<Task> byPriority()` → **yeni** liste: `priority` **büyükten küçüğe** (5 önce); eşitse `due` erken olan önce; o da eşitse `title` alfabetik.
- `public List<String> titlesSortedBy(Comparator<Task> order)` → verilen sıraya göre görevlerin **başlıkları**, yeni liste.
- `public int removeOverdue(LocalDate today)` → `due`'su `today`'den **önce** olan görevleri sil, kaç tane silindiğini döndür. `today` ile aynı gün olan **silinmez**.
  - **`Iterator` ile yaz** (`it.remove()`). `removeIf` ve stream **yasak**, bugünkü amaç bu.
- `public Optional<Pair<Task, Task>> earliestAndLatest()` → doğal sıraya göre **(ilk, son)** görev. Hiç görev yoksa `Optional.empty()`. Tek görev varsa `(o, o)`.
- `public Map<Integer, List<String>> titlesByPriority()` → priority → o priority'deki başlıklar. Key'ler **büyükten küçüğe** (5 önce), her listedeki başlıklar **alfabetik**.
  - Örnek: `"b"` 3, `"a"` 3, `"c"` 5 → `{5=[c], 3=[a, b]}`

**Bitince:** "bitti" yaz, dosyaları okuyup testlerimi çalıştıracağım.

---

## 4. Algoritma (1 sa) — Sıralama ile çözmek

**Yöntem:**
1. Örnek girdiyi **elle** çöz, adımları chat'e yaz.
2. Ben "tamam" deyince kodla.
3. LeetCode'a submit et.

### A1 — LeetCode 1051: Height Checker (Easy)
`heights` dizisi öğrencilerin şu anki sırası. Boydan (küçükten büyüğe) sıralanmış hâliyle karşılaştır: **kaç index'te** farklı?

**Elle çöz:** `heights = [1, 1, 4, 2, 1, 3]` → cevap `3`.
Sıralı kopyayı yaz, sonra iki diziyi alt alta koyup farklı olan index'leri say.
Soru: Sıralı kopyayı nasıl alırsın ki `heights` bozulmasın?

### A2 — LeetCode 1337: The K Weakest Rows in a Matrix (Easy)
`mat` satırlarında 1'ler (asker) hep solda, 0'lar sağda. Bir satır, **daha az 1'i varsa** daha zayıftır. 1 sayısı eşitse **index'i küçük olan** daha zayıftır. En zayıf `k` satırın index'lerini döndür.

**Elle çöz:**
```
mat = [[1,1,0,0,0],
       [1,1,1,1,0],
       [1,0,0,0,0],
       [1,1,0,0,0],
       [1,1,1,1,1]],   k = 3
```
1. Her satır için `(index, 1 sayısı)` çiftlerini yaz.
2. Bu çiftleri "1 sayısı az olan önce, eşitse index küçük olan önce" kuralıyla sırala, sıralı hâli yaz.
3. İlk 3'ün index'leri cevap (`[2, 0, 3]` çıkmalı).

---

## 5. Kapanış (15 dk)

- Isınma + tahmin sonuçlarından **en çok yanlış yaptığın 1 konuyu** chat'e yaz.
- `ILERLEME.md`'ye Gün 27 satırı (satırları ben hazırlayacağım).
- Commit + push:
  ```
  git add hafta-05 PLAN.md
  git commit -m "feat: day 27 - review generics + sorting + iterator"
  git push
  ```
