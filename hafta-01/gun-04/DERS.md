# Hafta 1 · Gün 4 — Diziler (Array), Dizilerde Döngü, Metotlara Dizi Geçirme

**Hedef:** Günün sonunda dizi oluşturup döngüyle gezebilmek, `ArrayIndexOutOfBoundsException`'ı neden aldığını açıklayabilmek, diziyi metoda verip "içi değişir, yeniden atama değişmez" kuralını **kodla göstermek**, temel dizi algoritmalarını (toplam, max/min, arama, ters çevirme) kağıtta tasarlayıp yazabilmek.

**Süre:** ~6 saat · Isınma 20 dk · Ders 1.5 sa · Egzersiz 3.5 sa · Kapanış 20 dk

**Kurallar (Gün 1–3'ten):**
- `public static void main(String[] args)`, kısaltma yok
- Sınıf, metot, değişken ve ekran mesajları **İngilizce**
- **Değişiklik → derle → çalıştır → çıktıyı oku.** Eski çıktıya güvenme.
- **Görev metnini kodla yan yana kontrol et.** Metot adı/imzası görevde nasılsa öyle (Gün 3'te `findMax`, `digitCunt` oldu).
- Hesaplayan metot yazdırmaz, **return** eder.

---

## 0. Isınma (20 dk) — notlara bakmadan

1. Java pass-by-value mu, pass-by-reference mı? Primitive ve reference için ayrı açıkla.
2. `swap(int a, int b)` neden çalışmaz?
3. Overloading nedir? Sadece dönüş tipi farklı iki metot olur mu?
4. "missing return statement" hatası ne zaman çıkar?
5. Hesaplayan metot neden yazdırmamalı?

---

## 1. Dizi nedir, neden var? (10 dk)

30 öğrencinin notunu tutmak için `int grade1, grade2, ... grade30` mı yazacaksın? Ortalamayı nasıl alacaksın, döngü değişken isimlerini gezemez.

**Dizi = aynı tipte, sabit sayıda elemanı yan yana tutan yapı.** Elemanlara **indeks** ile ulaşılır.

```
indeks:   0    1    2    3    4
        [ 70 | 85 | 40 | 95 | 60 ]      length = 5
```
- İlk indeks **0**, son indeks **length - 1**.
- Boyut oluşturulurken belirlenir, **sonradan değişmez**. (Büyüyebilen liste → Hafta 3, `ArrayList`.)

---

## 2. Oluşturma, okuma, yazma (20 dk)

```java
int[] grades = new int[5];              // 5 elemanlı, hepsi 0
int[] primes = {2, 3, 5, 7, 11};        // değerleri biliyorsan
String[] days = {"Mon", "Tue", "Wed"};

grades[0] = 70;                          // yazma
int first = primes[0];                   // okuma → 2
int last = primes[primes.length - 1];    // son eleman → 11
System.out.println(primes.length);       // 5  (parantez YOK: length bir alan, metot değil)
```

### Varsayılan değerler (`new` ile oluşturunca)
| Tip | Varsayılan |
|---|---|
| `int`, `long`, `double` | `0` / `0.0` |
| `boolean` | `false` |
| `char` | `'\u0000'` (boş karakter) |
| `String` ve diğer reference tipler | `null` |

### Sınır dışı erişim
```java
int[] a = new int[3];      // geçerli indeksler: 0, 1, 2
a[3] = 10;                 // ❌ çalışma anında: ArrayIndexOutOfBoundsException: Index 3 out of bounds for length 3
```
Bu **derleme** hatası değil, **çalışma anı** hatası (exception). Derleyici indeksin değerini bilemez. Mesajı oku: hangi indeks, length kaç → hatayı direkt söylüyor.

---

## 3. Dizide döngü (20 dk) ⭐

### Klasik for — indekse ihtiyacın varsa
```java
for (int i = 0; i < grades.length; i++) {
    System.out.println(i + ": " + grades[i]);
}
```
**Sınır kuralı:** `i = 0` ile başla, `i < length` ile bitir. `i <= length` → son turda patlar. (Gün 2'deki off-by-one'ın dizi versiyonu, en çok yapılan hata.)

### for-each — sadece değerleri okuyacaksan
```java
for (int grade : grades) {        // "grades'deki her grade için"
    System.out.println(grade);
}
```
- Daha kısa, indeks hatası yapamazsın.
- **Ama:** indeksi bilmezsin ve `grade = 100;` yazmak **diziyi değiştirmez** (`grade` elemanın kopyası, Gün 3 mantığı).
- Kural: okuyorsan for-each, indeks lazımsa veya yazıyorsan klasik for.

### Diziyi yazdırma
```java
System.out.println(primes);                    // [I@1b6d3586  ← adres gibi bir şey, içerik değil!
System.out.println(Arrays.toString(primes));   // [2, 3, 5, 7, 11]
```
`import java.util.Arrays;` gerekir (Scanner'daki gibi).

---

## 4. Dizi bellekte: reference tip ⭐⭐ (Gün 3'ün devamı)

Dizi bir **nesnedir**, heap'te durur. Değişken sadece **adresi** tutar.

```java
int[] a = {1, 2, 3};
int[] b = a;          // diziyi KOPYALAMAZ, adresi kopyalar
b[0] = 99;
System.out.println(a[0]);   // ?
```
```
a ──┐
    ├──► [ 99 | 2 | 3 ]     tek dizi, iki isim
b ──┘
```

### Gerçek kopya
```java
int[] c = Arrays.copyOf(a, a.length);   // yeni dizi, yeni adres
```

### `==` ile karşılaştırma
```java
int[] x = {1, 2};
int[] y = {1, 2};
x == y;                  // false → adresleri karşılaştırır (Gün 2'deki String == ile aynı tuzak)
Arrays.equals(x, y);     // true  → içeriği karşılaştırır
```

---

## 5. Metotlara dizi geçirme (20 dk) ⭐⭐

Kural Gün 3'teki gibi: **adresin kopyası** gider.

```java
public static void doubleAll(int[] arr) {
    for (int i = 0; i < arr.length; i++) {
        arr[i] = arr[i] * 2;      // aynı nesnenin İÇİNİ değiştiriyor → çağıran GÖRÜR
    }
}

public static void replace(int[] arr) {
    arr = new int[]{0, 0, 0};     // kopya adrese YENİ nesne atanıyor → çağıran GÖRMEZ
}
```
```
doubleAll çağrısı:                  replace çağrısı (atamadan sonra):
main: nums ──┐                      main: nums ──► [ 1 | 2 | 3 ]
             ├──► [ 1 | 2 | 3 ]     method: arr ─► [ 0 | 0 | 0 ]   (yeni, metot bitince çöp)
method: arr ─┘
```
Ev/adres benzetmesi: adresi bir kağıda yazıp birine verdin. O kişi **o eve gidip** mobilyaları değiştirirse sen de görürsün. Kağıttaki adresi silip **başka adres** yazarsa senin evine bir şey olmaz.

### Metottan dizi döndürme
```java
public static int[] createRange(int n) {     // dönüş tipi int[]
    int[] result = new int[n];
    for (int i = 0; i < n; i++) {
        result[i] = i + 1;
    }
    return result;      // adres döner; nesne heap'te olduğu için metot bitince silinmez
}
```

---

## 6. Temel dizi algoritmaları (20 dk) ⭐

Kodlarını **sen** yazacaksın (egzersizlerde). Burada sadece fikirler:

| Algoritma | Fikir | Dikkat |
|---|---|---|
| Toplam / ortalama | Akümülatör `sum`, döngüde ekle | Ortalama `double`: `int / int` tuzağı (Gün 1) |
| Max / min | **İlk elemanı** başlangıç kabul et, kalanlarla karşılaştır | `max = 0` ile başlarsan tüm elemanlar negatifken yanlış |
| Doğrusal arama | Baştan sona bak, bulunca indeksi **return** et | Bulunamazsa `-1` (sentinel, Gün 3'teki lcm kararın gibi) |
| Sayma | Koşulu sağlayanlar için sayaç | — |
| Yerinde ters çevirme | İki uç: `left = 0`, `right = length - 1`, yer değiştir, ortada buluş | Döngü ne zaman durmalı? Sonuna kadar gidersen ne olur? |

**Boş dizi** (`new int[0]`, length 0) ayrı bir durumdur. Max'ı neyle başlatacaksın? Karar ver, yorumla yaz.

---

## 7. 2D dizi — kısa bakış (10 dk)

```java
int[][] matrix = {
    {1, 2, 3},
    {4, 5, 6}
};
matrix[1][2];            // 6 → satır 1, sütun 2
matrix.length;           // 2 → satır sayısı
matrix[0].length;        // 3 → ilk satırın sütun sayısı
```
Gezmek için iç içe for (Gün 2'deki çarpım tablosu gibi). 2D = "dizilerin dizisi".

---

## 📝 KAĞIDA YAZ (sadece bunlar)

1. Dizi: aynı tip, sabit boyut. İndeks `0` … `length - 1`. Döngü: `i < arr.length`.
2. `length` parantezsiz. Sınır dışı → `ArrayIndexOutOfBoundsException` (çalışma anı).
3. `new` ile varsayılanlar: sayı `0`, boolean `false`, reference `null`.
4. for-each = sadece okumak. Yazmak / indeks lazım → klasik for.
5. `b = a` kopyalamaz, adresi paylaşır. Kopya: `Arrays.copyOf`. Karşılaştırma: `Arrays.equals`, `==` değil.
6. Metoda dizi: adres kopyası → **içini değiştirmek görünür, yeniden atama görünmez.**
7. Yazdırma: `Arrays.toString(arr)`.

---

## 💻 EGZERSİZLER (AI yok, IntelliJ'de AI kapalı)

Klasör: `hafta-01/gun-04/`. **Her metot için önce kağıda imzasını yaz.** Hiçbir metot (main ve `print...` olanlar hariç) `System.out` kullanmamalı.

### E1 — İlk dizi + sınır hatası
`ArrayBasics.java`:
1. 5 elemanlı `int[]` oluştur (`new` ile), hiçbir şey atamadan klasik for ile yazdır. Ne gördün, neden?
2. Aynısını `String[]` ve `boolean[]` için yap.
3. Döngüyü bilerek `i <= arr.length` yap, çalıştır. **Hata mesajının tamamını** kağıda yaz ve her parçasını açıkla.
4. Aynı diziyi for-each ile ve `Arrays.toString` ile yazdır.

### E2 — Array statistics ⭐
`ArrayStats.java`, şu metotlar:
- `sum(int[] arr)` → int
- `average(int[] arr)` → double
- `max(int[] arr)` → int
- `min(int[] arr)` → int
- `countGreaterThan(int[] arr, int limit)` → int

`main`'de **en az şu üç diziyle** test et: `{3, 8, 1, 9, 4}`, `{-5, -2, -9}`, `{7}`.
**Soru:** `max`'ı `int max = 0;` ile başlatsaydın hangi test dizisi yanlış sonuç verirdi?
**Karar:** boş dizi gelirse `average` ve `max` ne yapmalı? Yorum satırıyla yaz.

### E3 — Linear search
`indexOf(int[] arr, int target)` → int. Bulursa ilk indeksi, bulamazsa `-1`.
`contains(int[] arr, int target)` → boolean, **`indexOf`'u çağırarak** (tek satır).
Test: ilk eleman, son eleman, olmayan eleman, iki kere geçen eleman.

### E4 — Reverse ⭐ (trace table zorunlu)
1. `reversedCopy(int[] arr)` → `int[]`: **yeni** dizi döndürür, orijinal değişmez.
2. `reverseInPlace(int[] arr)` → `void`: **aynı** diziyi ters çevirir (iki uç yöntemi, Bölüm 6).

Kodu yazmadan önce `{1, 2, 3, 4, 5}` için `reverseInPlace` **trace table** yap (sütunlar: tur, left, right, dizi). Sonra 4 elemanlı bir dizi için de tekrarla, döngü koşulun ikisinde de doğru mu?
**Soru:** `reverseInPlace` neden `void` olabiliyor da `reversedCopy` olamıyor? (Gün 3'teki `swap`'la karşılaştır.)

### E5 — Pass-by-value deneyi, dizi versiyonu ⭐⭐
`ArrayPassByValue.java`:
1. Bölüm 5'teki `doubleAll` ve `replace` metotlarını yaz.
2. Her birini çağırmadan önce `main`'de diziyi yazdır, çağır, tekrar yazdır. **Çalıştırmadan önce tahminini kağıda yaz.**
3. `replace`'i, `main`'deki diziyi gerçekten `{0, 0, 0}` yapacak şekilde iki farklı yolla değiştir:
   - (a) yeni dizi döndürerek (dönüş tipi değişir),
   - (b) dönüş tipi `void` kalarak.
4. Bölüm 4'teki `b = a` örneğini çalıştır, sonra `Arrays.copyOf` ile tekrarla. Farkı bir cümleyle yaz.

### E6 — Equals tuzağı
İçeriği aynı iki dizi oluştur. `==` ve `Arrays.equals` sonuçlarını yazdır. Sonra `int[] c = a;` yap, `a == c` ne verir? Üç sonucu da **adres** kavramıyla açıkla.

### E7 — Grades (Scanner + dizi + metotlar)
`GradeBook.java`:
- Kullanıcıdan önce öğrenci sayısını al, sonra o kadar notu (0–100) diziye oku. Geçersiz not girilirse **aynı öğrenci için** tekrar sor.
- E2'deki metotlarla (kopyala veya aynı sınıfa yaz) ortalama, en yüksek, en düşük notu yazdır.
- Gün 3'teki `gradeFor`'u kullanarak her öğrencinin harf notunu yazdır: `Student 1: 85 → BA`.
- Ortalamanın üstünde kaç öğrenci var?

### E8 — Matrix (2D, kısa)
`Matrix.java`: 3×4 bir `int[][]` oluştur (değerleri sen seç).
- `printMatrix(int[][] m)` → void: satır satır, düzgün hizalı (`printf`, Gün 2).
- `rowSums(int[][] m)` → `int[]`: her satırın toplamı.
- Döngü sınırlarında `m.length` ve `m[i].length`'i doğru yerde kullandığından emin ol. **Hangisi satır, hangisi sütun?** Yorum satırıyla yaz.

---

## 🔧 GIT (10 dk)

```bash
git add hafta-01/gun-04
git status            # sadece .java ve DERS.md olmalı
git commit -m "feat: day 4 - arrays"
git push
```
Sonra `ILERLEME.md`'ye Gün 4 logu → **onu da commit + push** (`docs: day 4 progress log`).

---

## ✅ Gün Sonu

- [ ] Isınma soruları cevaplandı
- [ ] E1–E8 tamam (E4 trace table'ları kağıtta)
- [ ] "Kağıda Yaz" kutusu kağıtta
- [ ] Commit + push
- [ ] `ILERLEME.md`'ye Gün 4 logu (ve commit'i!)

**Yarın oturum başında soracaklarım:**
1. `int[] b = a; b[0] = 5;` → `a[0]` ne olur? Neden?
2. Metoda verilen dizinin içini değiştirmek ile metotta diziye yeni dizi atamak arasındaki fark nedir?
3. `ArrayIndexOutOfBoundsException` derleme hatası mı, çalışma anı hatası mı? Neden?
4. for-each ne zaman kullanılmaz?
5. Max bulurken neden `0` ile değil ilk elemanla başlarız?

**Yarın (Gün 5):** String derinlemesine (immutability, String pool, `equals`, sık metotlar, `StringBuilder`), `char` işlemleri, Hafta 1 tekrar + mini sınav.
