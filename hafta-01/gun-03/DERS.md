# Hafta 1 · Gün 3 — Metotlar (parametre, return, overloading, scope) ve Pass-by-Value

**Hedef:** Günün sonunda tekrar eden kodu metotlara bölebilmek, her metodun **ne aldığını ve ne döndürdüğünü** kağıtta tasarlayabilmek, "Java pass-by-value mu, pass-by-reference mı?" sorusunu cevaplayabilmek.

**Süre:** ~6 saat · Isınma 20 dk · IntelliJ kurulumu 20 dk · Ders 1.5 sa · Egzersiz 3.5 sa · Kapanış 20 dk

**Kurallar (Gün 1–2'den):**
- `public static void main(String[] args)`, kısaltma yok
- Sınıf, metot, değişken ve ekran mesajları **İngilizce**
- **Değişiklik → derle → çalıştır → çıktıyı oku.** Eski çıktıya güvenme.
- **Görev metnini kodla yan yana kontrol et.** "Çalışıyor" ≠ "istenen yapılmış".

---

## 0. Isınma (20 dk) — notlara bakmadan

1. `&&` kısa devre nedir, neden işe yarar? Bir örnek ver.
2. Klasik switch'te fall-through nedir?
3. while ile do-while farkı nedir, do-while nerede kullanılır?
4. `"yes" == input` neden yanlış?
5. E4'te faktöriyelde ne oldu, neden?
6. (Gün 2'den) `break` nerelerde kullanılabilir, nerede kullanılamaz?

---

## 1. IntelliJ IDEA kurulumu (20 dk)

1. **IntelliJ IDEA**'yı (ücretsiz sürüm) jetbrains.com'dan indir, kur.
2. **AI'ı kapat (zorunlu):**
   - Settings → Plugins → *AI Assistant* varsa **Disable**
   - Settings → Editor → General → Code Completion → **Full Line Code Completion** kutusunu kaldır
   - Normal otomatik tamamlama (`sout` → `System.out.println`) serbest.
3. New Project → Java → JDK olarak kurulu JDK'yı seç → proje klasörü: `hafta-01/gun-03`
   - Build system: **IntelliJ** (Maven'ı Hafta 4'te öğreneceğiz)
4. Bir sınıf oluştur, `main` yaz, yeşil ▶ ile çalıştır.

**Unutma:** ▶ tuşu arka planda Gün 1'de elle yaptığın şeyi yapıyor: `javac` → `.class` → `java`. Kırmızı alt çizgi = derleyici hatası. **Fareyi üstüne getir ve mesajı oku**, tahmin etme.

`.gitignore`'a eklenecekler: `.idea/`, `out/`, `*.iml` (IntelliJ'nin kendi dosyaları, repoya girmez).

---

## 2. Metot nedir, neden var? (15 dk)

Gün 2'de `isPrime` mantığını, `reverse` mantığını `main`'in içine yazdın. Başka bir programda lazım olsa? Kopyala-yapıştır → bir yerde hatayı düzeltirsin, diğerinde unutursun (MultiplicationTable'daki `print`/`printf` olayı).

**Metot = isim verilmiş, tekrar kullanılabilir kod bloğu.**

```java
public static int square(int x) {
    return x * x;
}
```

| Parça | Anlamı |
|---|---|
| `public` | erişim belirleyici (Hafta 2'de detay) |
| `static` | nesne oluşturmadan çağrılır; `main` static olduğu için şimdilik bütün metotlar static (Hafta 2'de detay) |
| `int` | **dönüş tipi**: metot ne geri veriyor? Hiçbir şey vermiyorsa `void` |
| `square` | metot adı: **fiil** ile başlar, camelCase (`calculateTotal`, `isPrime`, `printMenu`) |
| `(int x)` | **parametre listesi**: metot ne alıyor? |
| `return x * x;` | sonucu çağırana geri ver |

Çağırma:
```java
int result = square(5);          // 5 → argüman, x → parametre
System.out.println(square(3));   // dönen değer direkt kullanılabilir
```

- **Parametre:** tanımdaki değişken (`x`). **Argüman:** çağırırken verilen değer (`5`).
- `boolean` döndüren metotlar `is`/`has`/`can` ile başlar: `isPrime(7)`, `hasDivisor(...)`.

---

## 3. return (20 dk) ⭐

### void vs değer döndüren
```java
public static void printLine(int length) {    // bir İŞ yapar, değer vermez
    for (int i = 0; i < length; i++) {
        System.out.print("-");
    }
    System.out.println();
}

public static int sumTo(int n) {               // bir DEĞER hesaplar
    int sum = 0;
    for (int i = 1; i <= n; i++) {
        sum += i;
    }
    return sum;
}
```

**Altın kural:** Hesaplayan metot **yazdırmaz**, sonucu `return` eder. Yazdırmak çağıranın işi. Neden? `sumTo` sonucu ekrana basarsa, sonucu başka bir hesapta kullanamazsın.

### return metodu ANINDA bitirir
```java
public static String gradeFor(int score) {
    if (score < 0 || score > 100) {
        return "Invalid";          // guard clause: burada çıkılır, alt satırlar çalışmaz
    }
    if (score >= 90) {
        return "AA";
    }
    // ...
    return "FF";
}
```
Gün 2'de ReverseNumber'da `break` ile yapmaya çalıştığın "burada dur" işi bu: **`return`**. `else`'e gerek kalmaz, kod düzleşir.

### Her yol return etmeli
```java
public static String sign(int n) {
    if (n > 0) {
        return "positive";
    } else if (n < 0) {
        return "negative";
    }
}   // ❌ derleme hatası: missing return statement  (n == 0 ise ne döner?)
```
Derleyici **her olası yolun** bir değer döndürdüğünü kontrol eder. (Modern switch expression'daki `default` zorunluluğuyla aynı mantık.)

---

## 4. Scope — değişken nerede yaşar? (15 dk)

```java
public static void main(String[] args) {
    int total = 10;
    addFive(total);
    System.out.println(total);      // ?
}

public static void addFive(int total) {
    total = total + 5;              // bu `total` main'deki `total` DEĞİL
}
```

- Her metodun değişkenleri **kendine aittir**. Parametreler de metodun yerel (local) değişkenidir.
- Aynı isim (`total`) iki metotta = **iki ayrı değişken**. Bir metot diğerinin yerel değişkenini göremez.
- `{ }` bloğu içinde tanımlanan değişken blok bitince yok olur (Gün 2: döngü değişkeni).
- Metot bitince yerel değişkenleri stack'ten silinir (Gün 1'deki stack!).

Metotlar arası veri taşımanın **tek** yolu: **parametre ile ver, return ile al.**

---

## 5. Pass-by-Value ⭐⭐ (Mülakat klasiği)

> **Java her zaman pass-by-value'dur.** Metoda değişkenin kendisi değil, **değerinin kopyası** gider.

### Primitive
```java
public static void swap(int a, int b) {
    int temp = a;
    a = b;
    b = temp;
}

int x = 1, y = 2;
swap(x, y);
System.out.println(x + " " + y);   // ?
```
`swap`'a `x` ve `y`'nin **kopyaları** gider. Metot kopyaları değiştirir, metot bitince kopyalar silinir. `x` ve `y` etkilenmez.

### Reference (ön bakış — diziler yarın)
Reference tipte değişkenin değeri **adrestir** (Gün 1). Metoda **adresin kopyası** gider:
- Kopya adres de **aynı nesneyi** gösterir → nesnenin **içini** değiştirirsen çağıran da görür.
- Ama kopya adrese **yeni nesne atarsan** sadece kopya değişir → çağıran etkilenmez.

```
main:    arr ──┐
               ├──► [ 1, 2, 3 ]   (heap'teki tek nesne)
method:  copy ─┘
```
Mülakatta "Java'da nesneler pass-by-reference" dersen **yanlış** kabul edilir. Doğru cümle: *"Java pass-by-value'dur; reference tiplerde kopyalanan değer, nesnenin adresidir."*

---

## 6. Overloading (15 dk)

Aynı isimde, **parametre listesi farklı** birden çok metot:
```java
public static int max(int a, int b) { ... }
public static int max(int a, int b, int c) { ... }
public static double max(double a, double b) { ... }
```
- Fark **parametre sayısı veya tipi** olmalı.
- **Sadece dönüş tipi farklıysa overloading olmaz** → derleme hatası.
- Hangi metodun çağrılacağına **derleme anında**, argümanların tipine bakarak karar verilir.
- Bildiğin örnek: `System.out.println(int)`, `println(String)`, `println(double)`... hepsi ayrı metot.

---

## 📝 KAĞIDA YAZ (sadece bunlar)

1. Metot: `dönüşTipi isim(parametreler)`. Hesaplayan metot yazdırmaz, **return** eder.
2. `return` metodu anında bitirir. Değer döndüren metotta **her yol** return etmeli.
3. Parametre = metodun yerel değişkeni. Veri taşıma: **parametre ile ver, return ile al.**
4. **Java her zaman pass-by-value.** Primitive → değer kopyalanır. Reference → adres kopyalanır (içi değişir, yeniden atama değişmez).
5. Overloading: aynı isim, farklı parametre listesi. Sadece dönüş tipi farkı yetmez.
6. `boolean` metot: `isX` / `hasX`. Metot adı fiil, camelCase.

---

## 💻 EGZERSİZLER (AI yok, IntelliJ'de AI kapalı)

Klasör: `hafta-01/gun-03/`. **Her metot için önce kağıda imzasını yaz:** ne alıyor, ne döndürüyor, adı ne?

### E1 — Gün 2'yi metotlara böl ⭐
Tek bir `NumberUtils.java` dosyasında şu metotları yaz (Gün 2'deki kodlarını kullan, ama artık metot olarak):
- `isPrime(int n)` → boolean (bölen bulunca **return** ile çık, `break` ve flag'e gerek kalmamalı)
- `reverse(int n)` → int
- `isPalindrome(int n)` → boolean (**`reverse`'i çağırarak**, tekrar yazmadan)
- `factorial(int n)` → long
- `sumTo(int n)` → int

`main`'de her birini birkaç değerle çağırıp sonuçları yazdır. Hiçbir metot (main hariç) `System.out` kullanmamalı.
**Soru:** `isPrime` artık neden flag değişkenine ihtiyaç duymuyor?

### E2 — Grade with return
`gradeFor(int score)` → String. Guard clause'u `return` ile yap, `else` kullanma. `main`'den kullanıcı girdisiyle çağır.
Önce bilerek son `return "FF";` satırını sil, derleyicinin ne dediğini yaz.

### E3 — Pass-by-value deneyi ⭐
1. Yukarıdaki `swap(int a, int b)` metodunu yaz. Çalıştırmadan önce çıktıyı **tahmin et ve kağıda yaz**, sonra çalıştır.
2. Bölüm 4'teki `addFive` örneğini çalıştır, çıktıyı açıkla.
3. `addFive`'ı, `main`'deki `total`'ı **gerçekten** 15 yapacak şekilde değiştir. (İpucu: Bölüm 4'ün son cümlesi.)

### E4 — Overloading
`max` metodunun üç versiyonunu yaz: `(int, int)`, `(int, int, int)`, `(double, double)`.
- 3 parametreli versiyonu, 2 parametreliyi **çağırarak** yaz (kodu tekrar yazma).
- **Tahmin sorusu (bilmemen normal):** `max(3, 4.5)` hangi versiyonu çağırır? Neden? (İpucu: Gün 1'deki tip yükseltme, `int` → `double`.) Tahmin et, sonra dene.
- Dönüş tipi `long` olan bir `max(int a, int b)` daha eklemeyi dene. Derleyici ne diyor?

### E5 — Digits
- `digitCount(int n)` → kaç basamaklı? (`0` → 1 basamak, unutma!)
- `digitSum(int n)` → rakamlar toplamı
- Negatif sayılar için ne yapacağına karar ver, yorum satırıyla yaz.

### E6 — GCD (EBOB) ⭐ (Öklid algoritması)
`gcd(int a, int b)`: `b` 0 olana kadar → `(a, b)` yerine `(b, a % b)` koy. Sonuç `a`.
Kodu yazmadan önce `gcd(48, 18)` için **trace table** yap (sütunlar: tur, a, b, a % b).
Sonra `lcm(int a, int b)` (EKOK) yaz: **gcd'yi kullanarak**. (İpucu: `a × b = gcd × lcm`. Taşma riskine dikkat: önce çarpmak mı, önce bölmek mi?)

### E7 — Primes up to n
`printPrimesUpTo(int n)` → void. 2'den n'e kadar asal sayıları yan yana yazdırır. **E1'deki `isPrime`'ı kullan.**
Bu metot neden `void` olabilir de E1'dekiler olamaz? Bir cümle.

### E8 — Menu calculator (do-while + switch + metotlar)
Menü:
```
1. Add
2. Subtract
3. Multiply
4. Divide
0. Exit
```
- Menüyü yazdırma işi `printMenu()` metodunda.
- Her işlem ayrı metot (`add`, `subtract`, ...), değer döndürür.
- Seçim modern `switch` ile.
- `0` girilene kadar devam eder (hangi döngü?).
- Sıfıra bölmede program çökmemeli, uyarı vermeli.

---

## 🔧 GIT (10 dk)

`.gitignore`'a `.idea/`, `out/`, `*.iml` ekle, sonra:
```bash
git add hafta-01/gun-03 .gitignore
git status            # .java, DERS.md, .gitignore olmalı; .idea/out/.class olmamalı
git commit -m "feat: day 3 - methods and pass-by-value"
git push
```
Commit mesajı formatı: `tip: açıklama` (feat, fix, docs, refactor...). Gün 1'de öğrendin.

---

## ✅ Gün Sonu

- [ ] Isınma soruları cevaplandı
- [ ] IntelliJ kuruldu, AI kapalı
- [ ] E1–E8 tamam
- [ ] "Kağıda Yaz" kutusu kağıtta
- [ ] Commit + push
- [ ] `ILERLEME.md`'ye Gün 3 logu (ve commit'i!)

**Yarın oturum başında soracaklarım:**
1. Java pass-by-value mu, pass-by-reference mı? Primitive ve reference için ayrı açıkla.
2. `swap(int a, int b)` neden çalışmaz?
3. Overloading nedir? Sadece dönüş tipi farklı iki metot olur mu?
4. "missing return statement" hatası ne zaman çıkar?
5. Hesaplayan metot neden yazdırmamalı?

**Yarın (Gün 4):** Diziler (array), dizilerde döngü, metotlara dizi geçirme (pass-by-value'nun reference tarafı), temel algoritmalar (max, min, arama, ters çevirme).
