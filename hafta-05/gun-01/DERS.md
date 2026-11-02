# Hafta 5 · Gün 1 (Gün 25) — TEKRAR: Exceptions + List

**Hedef:** Yeni konu yok. Hafta 3'ün ilk yarısını (exceptions + `List`) **notsuz yazabilir** hâle getirmek.
Bu haftanın sonunda kapı sınavı var (Gün 31, ≥ 80 → SQL). Bugünkü konular orada ~15 puan.

**Süre:** ~5-6 saat
Isınma 15 dk · Konu özeti 30 dk · Çıktı tahmini 20 dk · Notsuz kod görevi 1.5–2 sa · Algoritma 1 sa · Kapanış 15 dk

**Kurallar (değişmedi):**
- Sınıf, metot, değişken ve mesajlar **İngilizce**
- **Değişiklik → derle → çalıştır → çıktının her satırını oku.**
- **Metot adı/imzası görevde nasılsa öyle.** Exception mesajları **birebir** (testlerim mesajı da kontrol ediyor).
- `main`'e test yazmak yok. Testleri ben yazıp çalıştırıyorum.

**Kurulum:** `hafta-05/gun-01/` klasöründe package **`w5d1`** (`hafta-05/gun-01/w5d1/`). `gun-01`'i Sources Root yap.

---

## 0. Isınma (15 dk) — notsuz, cevaplar chat'e

Hepsi son günlerde en az bir kez kaçtı. Her sorunun çıktısını yaz.

**S1.**
```java
Optional<String> o = Optional.of("Ada");
System.out.println(o);
System.out.println(o.map(String::length));
System.out.println(o.orElse("none"));
```

**S2.**
```java
System.out.println(LocalDate.of(2026, 3, 5));
```

**S3.**
```java
record Point(int x, int y) {}
System.out.println(new Point(1, 2));
```

**S4.** Aşağıdaki iki lambda'dan hangisi `Consumer<String>`, hangisi `Function<String, Integer>` tipine atanabilir? Neden?
```java
s -> System.out.println(s)
s -> s.length()
```

**S5.**
```java
Thread t = new Thread(() -> System.out.println(Thread.currentThread().getName()), "worker");
t.run();
t.start();
```

---

## 1. Konu Özeti (30 dk) — oku, takıldığın yeri chat'e sor

### 1.1 Exception ağacı

```
Throwable
├── Error                         (JVM sorunları: OutOfMemoryError — yakalamıyoruz)
└── Exception                     ← CHECKED
    ├── IOException               ← CHECKED
    ├── (senin extends Exception sınıfların) ← CHECKED
    └── RuntimeException          ← UNCHECKED
        ├── IllegalArgumentException
        │   └── NumberFormatException
        ├── IllegalStateException
        ├── IndexOutOfBoundsException
        │   └── ArrayIndexOutOfBoundsException
        ├── NullPointerException
        ├── UnsupportedOperationException
        ├── ConcurrentModificationException
        └── (senin extends RuntimeException sınıfların) ← UNCHECKED
```

**Tek kural:** `RuntimeException` soyundan geliyorsa unchecked, değilse (`Exception`'dan geliyorsa) checked.

### 1.2 Checked / unchecked = derleyicinin sorusu

Derleyici her checked exception için sorar: **"Bu ne olacak?"** İki cevap var:
1. `try/catch` ile yakala, ya da
2. metot imzasına `throws X` yaz → "ben ilgilenmiyorum, beni çağıran ilgilensin".

İkisini de yapmazsan **derlenmez**. Unchecked'te bu soru hiç sorulmaz.

```java
static void read() throws IOException { ... }

public static void main(String[] args) {
    read();                // DERLENMEZ: IOException ne olacak?
}
```

### 1.3 `throw` vs `throws`

```java
throw new IllegalArgumentException("Bad");     // fiil: ŞİMDİ fırlat (metodun içinde)
void load() throws IOException { ... }         // uyarı etiketi: "fırlatabilirim" (imzada)
```

### 1.4 try / catch / finally akışı

- `try` içinde exception olursa **o satırdan sonrası çalışmaz**, uygun `catch`'e atlar.
- `catch`'ler **yukarıdan aşağı** denenir, **ilk uyan** çalışır, gerisi atlanır.
- Alt sınıf üstte olmalı. `catch (RuntimeException e)` üstte, `catch (IllegalArgumentException e)` altta → **derlenmez** (alttakine asla sıra gelmez).
- `finally` **her durumda** çalışır: exception olsa da, olmasa da, `try` içinde `return` olsa da.
- Hiçbir `catch` uymazsa: `finally` çalışır, sonra exception yukarı (çağırana) gider.
- Boş `catch {}` yasak — en azından mesajı yazdır ya da yeniden fırlat.

### 1.5 Kendi exception'ın

```java
public class PaymentException extends RuntimeException {   // unchecked
    public PaymentException(String message) {
        super(message);                                     // mesajı Exception'a ilet → getMessage()
    }
}
```

`extends Exception` yazsaydın checked olurdu → onu fırlatan her metot `throws PaymentException` yazmak zorunda kalırdı.

### 1.6 List — dönüş değerleri (en çok kaçan yer)

```
list.add(x)          → boolean (ArrayList'te hep true)
list.add(i, x)       → void    (i: 0..size arası serbest; i == size → sona ekler)
list.get(i)          → eleman
list.set(i, x)       → ESKİ eleman (yenisi değil!)
list.remove(int i)   → silinen eleman
list.remove(Object)  → boolean (bulup sildiyse true, yoksa false — exception YOK)
list.contains(x)     → boolean  (equals kullanır)
list.indexOf(x)      → int      (yoksa -1; equals kullanır)
list.removeIf(...)   → boolean  (en az bir şey sildiyse true)
```

### 1.7 `remove(int)` tuzağı

```java
List<Integer> nums = new ArrayList<>(List.of(10, 20, 30));
nums.remove(1);                    // INDEX 1'i siler → [10, 30]
nums.remove(Integer.valueOf(30));  // 30 DEĞERİNİ siler → [10]
```

Java önce birebir uyan metodu arar: `1` bir `int`, `remove(int index)` birebir uyuyor → index olarak alınır. Değer olarak silmek için kutula: `Integer.valueOf(...)`.

### 1.8 Değiştirilemez listeler

```java
List.of("a", "b")            // değiştirilemez → add/remove/set → UnsupportedOperationException
List.copyOf(list)            // değiştirilemez KOPYA
new ArrayList<>(List.of(...))  // değiştirilebilir kopya ← "listeyle başlayıp üstüne ekleyeceğim" kalıbı
```

Sınıfının iç listesini dışarı verirken `List.copyOf(...)` döndür → dışarıdaki kod senin listeni bozamaz.

### 1.9 Dönerken silme → ConcurrentModificationException

```java
for (String s : list) {           // for-each gizlice Iterator kullanır
    if (...) list.remove(s);      // liste iterator'ın arkasından değişti → CME
}

list.removeIf(s -> s.isBlank());  // doğru yol
```

### ✍️ Kağıda Yaz

```
1. extends RuntimeException → unchecked · extends Exception → checked (throws ya da catch zorunlu)
2. throw = şimdi fırlat (gövdede) · throws = fırlatabilirim (imzada)
3. catch: yukarıdan aşağı, ilk uyan; alt sınıf üstte. finally her zaman çalışır.
4. set → ESKİ eleman · remove(int) → silinen eleman · remove(Object) → boolean
5. remove(1) = index 1 · remove(Integer.valueOf(1)) = değer 1
6. List.of / List.copyOf → değiştirilemez → UnsupportedOperationException
7. for-each içinde silme → CME → removeIf
```

---

## 2. Çıktı Tahmini (20 dk) — ÇALIŞTIRMADAN, cevaplar chat'e

Her snippet `main` içinde çalışıyor. Exception çıkarsa **hangi exception** olduğunu yaz, sonrasında yazdırılmayan satırları yazma.

**P1.**
```java
static int f() {
    try {
        System.out.println("try");
        return 1;
    } finally {
        System.out.println("finally");
    }
}
// main:
System.out.println(f());
```

**P2.**
```java
try {
    int[] a = new int[2];
    a[2] = 5;
    System.out.println("A");
} catch (IllegalArgumentException e) {
    System.out.println("B");
} catch (RuntimeException e) {
    System.out.println("C " + e.getClass().getSimpleName());
} finally {
    System.out.println("D");
}
System.out.println("E");
```

**P3.**
```java
try {
    Integer.parseInt("12a");
} catch (IllegalArgumentException e) {
    System.out.println("caught " + e.getClass().getSimpleName());
}
```

**P4.**
```java
List<String> list = new ArrayList<>(List.of("a", "b", "c"));
System.out.println(list.add("d"));
System.out.println(list.set(0, "z"));
System.out.println(list.remove("x"));
System.out.println(list.remove(1));
System.out.println(list);
```

**P5.**
```java
List<Integer> nums = new ArrayList<>(List.of(5, 1, 7));
nums.remove(1);
System.out.println(nums);
nums.remove(Integer.valueOf(7));
System.out.println(nums);
nums.remove(5);
System.out.println(nums);
```

**P6.**
```java
List<String> a = List.of("x", "y");
List<String> b = new ArrayList<>(a);
b.add("z");
System.out.println(a + " " + b);
a.add("w");
System.out.println(a);
```

**P7.**
```java
List<String> s = new ArrayList<>();
s.add(0, "b");
s.add(0, "a");
s.add(2, "c");
System.out.println(s);
s.add(4, "d");
System.out.println(s);
```

**P8.** İki ayrı soru: (a) aşağıdaki döngü ne olur? (b) altındaki iki satır ne yazdırır?
```java
List<Integer> n = new ArrayList<>(List.of(1, 2, 3, 4));
for (int x : n) {
    if (x % 2 == 0) n.remove(Integer.valueOf(x));
}
```
```java
List<Integer> m = new ArrayList<>(List.of(1, 2, 3, 4));
System.out.println(m.removeIf(x -> x % 2 == 0));
System.out.println(m);
```

**P9.**
```java
try {
    System.out.println("1");
    throw new IllegalStateException("boom");
} catch (IllegalStateException e) {
    System.out.println("2 " + e.getMessage());
    throw new RuntimeException("again");
} finally {
    System.out.println("3");
}
System.out.println("4");
```

---

## 3. Notsuz Kod Görevi (1.5–2 sa) — Çalma Listesi

**Kural:** Bu bölümde DERS dosyalarına, eski koduna, internete bakmak **yok**. AI kapalı. Bu dosyadaki **görev metni** açık kalabilir (yukarıdaki özet kısmına bakma). Takılırsan önce 10 dk kendin dene, sonra chat'e sor.

Her public tip kendi dosyasında, package `w5d1`. Döngü serbest (stream de serbest ama şart değil).

### 3.1 `class InvalidSongException` (`InvalidSongException.java`)
- **Unchecked** olmalı.
- Constructor: `public InvalidSongException(String message)` → mesajı üst sınıfa iletir.

### 3.2 `class SongNotFoundException` (`SongNotFoundException.java`)
- **Checked** olmalı.
- Constructor: `public SongNotFoundException(String message)` → mesajı üst sınıfa iletir.

### 3.3 `record Song(String title, int seconds)` (`Song.java`)
- **Compact constructor**, kontroller bu sırayla:
  - `title == null` ya da `title.isBlank()` → `InvalidSongException("Title must not be blank")`
  - `seconds <= 0` → `InvalidSongException("Seconds must be positive")`
- Başka metot yok.

### 3.4 `class Playlist` (`Playlist.java`)

**Field'lar:**
- `private final String name;`
- `private final List<Song> songs = new ArrayList<>();`

**Constructor:**
- `public Playlist(String name)` → `name == null` ya da boşsa `IllegalArgumentException("Name must not be blank")`, değilse field'a ata.

**Metotlar:**
- `public String getName()`
- `public int size()` → kaç şarkı var
- `public void add(Song song)`
  - `song == null` → `IllegalArgumentException("Song must not be null")`
  - Aynı şarkı (aynı title **ve** aynı seconds) zaten listede → `IllegalStateException("Already in playlist: " + song.title())`
  - Değilse sona ekle.
- `public void insertAt(int index, Song song)`
  - `song == null` → `IllegalArgumentException("Song must not be null")`
  - `index < 0` ya da `index > size()` → `IndexOutOfBoundsException("Index: " + index + ", size: " + size())` (kendi kontrolün, ArrayList'e bırakma)
  - Değilse o index'e ekle (`index == size()` geçerli → sona ekler).
- `public Song removeAt(int index)` → o index'teki şarkıyı siler ve **silinen şarkıyı döndürür**. Geçersiz index'te kendi kontrolünü yazma — ArrayList'in fırlattığı exception dışarı çıksın.
- `public void remove(String title) throws SongNotFoundException` → başlığı `title` olan **ilk** şarkıyı siler. Yoksa → `SongNotFoundException("Song not found: " + title)`.
- `public int indexOf(String title)` → başlığı `title` olan ilk şarkının index'i, yoksa `-1`.
- `public void moveUp(int index)` → o index'teki şarkıyı bir üstteki (index - 1) şarkıyla yer değiştirir.
  - `index <= 0` ya da `index >= size()` → `IllegalArgumentException("Cannot move up: " + index)`
- `public int totalSeconds()` → tüm şarkıların süre toplamı.
- `public List<Song> longerThan(int seconds)` → süresi `seconds`'tan **büyük** şarkılar, **listedeki sırayla**, yeni bir listede.
- `public int removeShorterThan(int seconds)` → süresi `seconds`'tan **küçük** bütün şarkıları siler, **kaç tane sildiğini** döndürür.
- `public List<Song> getSongs()` → şarkıların **değiştirilemez** kopyası (dışarıdaki kod `add` çağırırsa exception almalı).

### 3.5 `class PlaylistTools` (`PlaylistTools.java`)

Sadece `static` metotlar, field yok.

- `public static boolean tryRemove(Playlist playlist, String title)`
  - `playlist.remove(title)` çağırır.
  - Sildiyse `true` döndürür; `SongNotFoundException` gelirse **yakalar** ve `false` döndürür.
- `public static int parseDuration(String text)` → `"3:25"` gibi `dakika:saniye` metnini toplam saniyeye çevirir (`"3:25"` → `205`).
  - `text == null` → `InvalidSongException("Bad duration: null")`
  - `:` ile böldüğünde tam 2 parça çıkmıyorsa → `InvalidSongException("Bad duration: " + text)`
  - Parçalardan biri sayı değilse (`parseInt` patlarsa) → onu **yakala**, yerine `InvalidSongException("Bad duration: " + text)` fırlat.
  - Saniye kısmı `0..59` dışındaysa → `InvalidSongException("Bad duration: " + text)`
- `public static Song parseSong(String line)` → `"Numb;3:05"` formatındaki satırdan `Song` üretir (`;` ile böl: başlık, süre).
  - `;` ile böldüğünde tam 2 parça çıkmıyorsa → `InvalidSongException("Bad line: " + line)`
  - Süre için `parseDuration` kullan (onun exception'ı olduğu gibi dışarı çıksın).

**Bitince:** "bitti" yaz, dosyaları okuyup testlerimi çalıştıracağım.

---

## 4. Algoritma (1 sa) — Dizi + iki pointer

**Yöntem (bugünden itibaren her problemde):**
1. Örnek girdiyi al, **elle** çöz. Pointer'ların her adımdaki değerini chat'e yaz.
2. Ben "tamam" deyince kodla.
3. LeetCode'a submit et.

### A1 — LeetCode 905: Sort Array By Parity (Easy)
Çiftleri başa, tekleri sona topla (kendi içlerindeki sıra önemli değil).

**Elle çöz:** `[3, 1, 2, 4]` için iki pointer kullan: `left = 0`, `right = 3`. Her adımda `left`, `right` ve dizinin hâlini yaz.
İpucu (sadece fikir): `left` soldan "yanlış yerde duran" bir tek sayı arar, `right` sağdan "yanlış yerde duran" bir çift sayı arar; ikisi de bulunca yer değiştirir. Ne zaman durursun?

### A2 — LeetCode 88: Merge Sorted Array (Easy)
`nums1`'in sonunda `n` tane boş yer (`0`) var. `nums2`'yi `nums1`'in içine, sıralı olacak şekilde yerleştir. Yeni dizi oluşturma.

**Elle çöz:** `nums1 = [1, 2, 3, 0, 0, 0], m = 3`, `nums2 = [2, 5, 6], n = 3`.
İpucu (sadece fikir): baştan doldurmaya çalışırsan `nums1`'in henüz okumadığın elemanlarının üstüne yazarsın. **Sondan** doldurursan ne olur? Üç pointer: `i` (nums1'in son gerçek elemanı), `j` (nums2'nin sonu), `k` (yazılacak yer). Her adımda `i, j, k` ve `nums1`'i yaz.

---

## 5. Kapanış (15 dk)

- Isınma + tahmin sonuçlarından **en çok yanlış yaptığın 1 konuyu** chat'e yaz.
- `ILERLEME.md`'ye Gün 25 satırı (satırları ben hazırlayacağım).
- Commit + push:
  ```
  git add hafta-05
  git commit -m "feat: day 25 - review exceptions + list"
  git push
  ```
