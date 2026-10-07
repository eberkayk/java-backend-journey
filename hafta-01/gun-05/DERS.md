# Hafta 1 · Gün 5 — String Derinlemesine, `char` İşlemleri, StringBuilder + Hafta 1 Mini Sınav

**Hedef:** Günün sonunda String'in neden **immutable** olduğunu ve bunun sonuçlarını (pool, `==` tuzağı, döngüde `+` maliyeti) açıklayabilmek; sık kullanılan String metotlarını doküman açmadan kullanabilmek; `char` üzerinde aritmetik yapabilmek; giriş testinde yapamadığın **palindrome**'u kağıtta tasarlayıp yazabilmek. Sonra Hafta 1'i kapalı kitap mini sınavla kapatmak.

**Süre:** ~6 saat · Isınma 20 dk · Ders 1.5 sa · Egzersiz 2.5 sa · Mini sınav 1 sa · Kapanış 20 dk

**Kurallar (Gün 1–4'ten):**
- `public static void main(String[] args)`, kısaltma yok
- Sınıf, metot, değişken ve ekran mesajları **İngilizce**
- **Değişiklik → derle → çalıştır → çıktının her satırını oku.**
- **Metot adı/imzası görevde nasılsa öyle.** Önce kağıda imza, sonra kod.
- Hesaplayan metot yazdırmaz, **return** eder.

---

## 0. Isınma (20 dk) — notlara bakmadan (Gün 4 soruları)

1. `int[] b = a; b[0] = 5;` → `a[0]` ne olur? Neden?
2. Metoda verilen dizinin **içini** değiştirmek ile metotta diziye **yeni dizi atamak** arasındaki fark nedir?
3. `ArrayIndexOutOfBoundsException` derleme hatası mı, çalışma anı hatası mı? Neden?
4. for-each ne zaman kullanılmaz?
5. Max bulurken neden `0` ile değil ilk elemanla başlarız?

---

## 1. String bir nesnedir — ve değiştirilemez (immutable) ⭐⭐ (15 dk)

Gün 1'den biliyorsun: `String` primitive değil, bir **class**. Değişken adresi tutar, nesne heap'tedir. Dizilerden farkı şu:

> **Bir String nesnesi oluşturulduktan sonra içeriği ASLA değişmez.**
> "Değiştiriyormuş gibi" görünen her metot **yeni bir String** döndürür.

```java
String name = "enes";
name.toUpperCase();              // yeni "ENES" oluştu... ve kimse onu tutmadı → çöp
System.out.println(name);        // enes  ← değişmedi!

name = name.toUpperCase();       // yeni nesnenin adresini name'e ATA
System.out.println(name);        // ENES
```
```
name.toUpperCase() sonrası:      name = name.toUpperCase() sonrası:
name ──► "enes"                  "enes"   (artık kimse göstermiyor → çöp)
         "ENES"  (sahipsiz)      name ──► "ENES"
```

**Dizi ile karşılaştır (Gün 4):** `arr[0] = 99` dizinin **içini** değiştirir. String'te bunun karşılığı **yok**: `name.charAt(0) = 'E'` derlenmez. String'te tek yol: yeni String oluşturup değişkene atamak.

**Sonucu (Gün 3 bağlantısı):** String'i metoda verirsin, metot içinde `s = s + "!"` yaparsa çağıran görmez (yeniden atama). İçini değiştirmenin de yolu olmadığı için → **String'i metoda verdiğinde, metot onu hiçbir şekilde bozamaz.** Bu yüzden güvenli.

**Neden böyle tasarlanmış?** (mülakat) Güvenlik (dosya yolu, şifre, URL bir kez kontrol edildikten sonra değişemez), thread-safety, `hashCode` önbelleklenebilir (Hafta 3 HashMap'te göreceksin) ve **String pool** mümkün olur ↓

---

## 2. String pool ve `==` tuzağı ⭐⭐ (15 dk) — Gün 2'de "detayı Gün 5'te" dediğimiz yer

Tırnakla yazılan String'ler (**literal**) heap'te özel bir bölgede, **String pool**'da tutulur. Aynı literal ikinci kez yazılırsa **yeni nesne oluşmaz**, havuzdakinin adresi verilir. (İmmutable olduğu için paylaşmak güvenli: kimse içini bozamaz.)

```java
String a = "java";
String b = "java";               // pool'dan aynı nesne
String c = new String("java");   // new → HER ZAMAN yeni nesne (pool dışında)

a == b         // true   ← aynı adres (şans eseri doğru gibi görünür!)
a == c         // false  ← farklı adres
a.equals(c)    // true   ← içerik aynı
```
```
          String pool
a ──┐    ┌──────────┐
    ├──► │  "java"  │
b ──┘    └──────────┘
c ──────► "java"   (ayrı nesne)
```

**Tuzak:** `a == b` true çıkınca "== çalışıyor" sanırsın. Ama `scanner.nextLine()`, `substring`, `+` ile çalışma anında oluşan String'ler pool'dan **gelmez** → `==` false verir. Gün 2'deki `if (answer == "yes")` hatası buydu.

**Kural:** String'leri **her zaman** `equals` ile karşılaştır. `==` sadece "aynı nesne mi?" sorusu için.

```java
"yes".equals(answer)           // answer null olsa bile patlamaz (literal solda)
answer.equals("yes")           // answer null ise → NullPointerException
answer.equalsIgnoreCase("YES") // büyük/küçük harf fark etmez
```

---

## 3. Sık kullanılan String metotları (25 dk) ⭐

İndeksler dizideki gibi: **0 … length() - 1**.

```java
String s = "Hello, World";
//          0123456789...
```

| Metot | Örnek | Sonuç | Not |
|---|---|---|---|
| `length()` | `s.length()` | `12` | **Parantezli!** (dizide `length` parantezsizdi) |
| `charAt(i)` | `s.charAt(0)` | `'H'` | `char` döner. Sınır dışı → `StringIndexOutOfBoundsException` |
| `indexOf(x)` | `s.indexOf('o')` | `4` | İlk geçtiği yer, yoksa `-1` (senin E3'teki `indexOf` gibi!) |
| `lastIndexOf(x)` | `s.lastIndexOf('o')` | `8` | |
| `contains(x)` | `s.contains("World")` | `true` | |
| `substring(b, e)` | `s.substring(0, 5)` | `"Hello"` | **b dahil, e hariç** → uzunluk = e - b |
| `substring(b)` | `s.substring(7)` | `"World"` | b'den sona kadar |
| `toUpperCase()` / `toLowerCase()` | | `"HELLO, WORLD"` | Yeni String döner |
| `trim()` / `strip()` | `"  hi  ".strip()` | `"hi"` | Baş/son boşluk |
| `isEmpty()` / `isBlank()` | `"  ".isEmpty()` / `"  ".isBlank()` | `false` / `true` | blank = sadece boşluk da boş sayılır |
| `startsWith` / `endsWith` | `s.endsWith("ld")` | `true` | |
| `replace(a, b)` | `s.replace('l', 'L')` | `"HeLLo, WorLd"` | Hepsini değiştirir |
| `split(regex)` | `"a,b,c".split(",")` | `{"a","b","c"}` | `String[]` döner (Gün 4!) |
| `toCharArray()` | `"abc".toCharArray()` | `{'a','b','c'}` | `char[]` → diziye çevir, değiştir |
| `String.valueOf(x)` | `String.valueOf(42)` | `"42"` | Sayı → String |
| `Integer.parseInt(s)` | `Integer.parseInt("42")` | `42` | String → sayı. `"4a"` → `NumberFormatException` |
| `compareTo(o)` | `"apple".compareTo("banana")` | negatif | Alfabetik sıra: <0 önce, 0 eşit, >0 sonra |

**`substring` kuralı** (en çok karıştırılan): "başlangıç dahil, bitiş hariç". `s.substring(i, i + 1)` → i'deki tek karakter (String olarak).

**`split` ve boşluk:** `"a  b".split(" ")` → `{"a", "", "b"}` (iki boşluğun arasında boş String!). Birden çok boşluğa karşı: `split("\\s+")`. Şimdilik "regex = desen" de geç, detay yok.

**Hepsinde ortak:** Hiçbiri `s`'yi değiştirmez. Hepsi yeni değer döndürür.

---

## 4. `char` = küçük bir sayı ⭐ (15 dk)

Gün 1: `char` 16 bitlik bir sayıdır (Unicode kodu). Bu yüzden aritmetik yapılabilir.

```java
char c = 'a';
int code = c;                  // 97 (otomatik genişleme)
char next = (char) (c + 1);    // 'b'  (c + 1 → int olur, cast gerekir)
int digit = '7' - '0';         // 7   ← rakam karakterini sayıya çevirmenin klasik yolu
int pos = 'd' - 'a';           // 3   ← alfabedeki sırası (0'dan)

'a' < 'b'                      // true, karşılaştırılabilir
c == 'a'                       // true ← char primitive, == DOĞRU kullanım
```
Bilmen gereken sıralar: `'0'..'9'` ardışık, `'A'..'Z'` ardışık (65–90), `'a'..'z'` ardışık (97–122). Büyük-küçük farkı 32 — ezberleme, `Character` metotlarını kullan:

| Metot | Ne yapar |
|---|---|
| `Character.isDigit(c)` | rakam mı |
| `Character.isLetter(c)` | harf mi |
| `Character.isLetterOrDigit(c)` | harf veya rakam mı |
| `Character.isWhitespace(c)` | boşluk mu |
| `Character.isUpperCase(c)` / `isLowerCase(c)` | |
| `Character.toUpperCase(c)` / `toLowerCase(c)` | `char` döner |

**Dikkat:** `'a'` (char, tek tırnak) ≠ `"a"` (String, çift tırnak). `"a" == 'a'` derlenmez. `"abc".charAt(0) == "a"` derlenmez.

**String'i karakter karakter gezmek** (Gün 4 döngüsü):
```java
for (int i = 0; i < s.length(); i++) {    // length() PARANTEZLİ
    char ch = s.charAt(i);
    ...
}
// veya sadece okuyacaksan:
for (char ch : s.toCharArray()) { ... }
```

---

## 5. Döngüde `+` ve StringBuilder ⭐⭐ (15 dk)

İmmutable olmasının bedeli:

```java
String result = "";
for (int i = 0; i < 10000; i++) {
    result = result + i;       // HER turda: yeni String oluştur, eskiyi kopyala, eskisi çöp
}
```
Tur 1000'de 3000 karakteri kopyalıyorsun, tur 10000'de ~40000. Toplam iş n² ile büyür. (Big-O → Hafta 8; şimdilik: "her turda tüm geçmişi yeniden kopyalıyor" de yeter.)

**Çözüm: `StringBuilder`** — değiştirilebilir (mutable) karakter dizisi. İçini değiştirirsin, kopyalama yok.

```java
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 10000; i++) {
    sb.append(i);              // aynı nesnenin içine ekler
}
String result = sb.toString(); // en sonda bir kez String'e çevir
```

| StringBuilder metodu | Ne yapar |
|---|---|
| `append(x)` | sona ekle (her tip alır) |
| `insert(i, x)` | i. konuma ekle |
| `reverse()` | **yerinde** ters çevir |
| `deleteCharAt(i)` | i. karakteri sil |
| `setCharAt(i, c)` | i. karakteri değiştir |
| `length()`, `charAt(i)` | String'deki gibi |
| `toString()` | String'e çevir |

**Ne zaman hangisi?**
- Tek satırda birkaç parça birleştiriyorsan → `+` (derleyici zaten optimize eder).
- **Döngü içinde** String biriktiriyorsan → `StringBuilder`.

**Tuzak:** `StringBuilder`'da `equals` içeriği karşılaştırmaz (adres karşılaştırır). Karşılaştıracaksan önce `toString()`.

---

## 📝 KAĞIDA YAZ (sadece bunlar)

1. String **immutable**: metotlar yeni String döndürür → `s = s.toUpperCase();` diye **ata**.
2. Literal'ler **String pool**'da paylaşılır; `new String` yeni nesne. Karşılaştırma **her zaman `equals`**, `==` adres.
3. `"yes".equals(x)` → x null olsa da patlamaz.
4. `length()` parantezli (dizide `length` parantezsiz). `charAt(i)` → `char`.
5. `substring(b, e)`: b dahil, e hariç.
6. `char` bir sayı: `'7' - '0'` = 7, `(char)(c + 1)`. `char` için `==` doğru.
7. Döngüde String biriktirme → `StringBuilder` (`append`, `reverse`, `toString`).

---

## 💻 EGZERSİZLER (AI yok, IntelliJ'de AI kapalı)

Klasör: `hafta-01/gun-05/`. **Her metot için önce kağıda imzasını yaz.** Hesaplayan metotlar `System.out` kullanmaz.

### E1 — Immutability deneyi ⭐
`StringBasics.java`:
1. Bölüm 1'deki `name.toUpperCase();` örneğini **atamadan** ve **atayarak** çalıştır. Çalıştırmadan önce tahminini kağıda yaz.
2. Bölüm 2'deki `a`, `b`, `c` örneğini çalıştır, üç karşılaştırmayı yazdır.
3. Scanner ile kullanıcıdan bir kelime al, `"java"` girsin. `input == "java"` ve `input.equals("java")` sonuçlarını yazdır. Pool'daki `a == b` true iken bu neden false? **Bir cümleyle** yaz.
4. `void shout(String s)` metodu yaz: içinde `s = s.toUpperCase() + "!";` yapsın. main'de çağır, sonra main'deki String'i yazdır. Değişti mi? Gün 4'teki `replace(int[] arr)` ile aynı sebep mi?

### E2 — Metot tanıma
`StringMethods.java`: `String s = "  Java Backend Developer  ";` için **önce kağıda tahmin**, sonra çalıştır:
`s.length()`, `s.strip().length()`, `s.strip().charAt(0)`, `s.strip().indexOf("Back")`, `s.strip().substring(5, 12)`, `s.strip().split(" ").length`, `s.toLowerCase().contains("java")`, `s.strip().replace('a', '@')`.
Yanlış tahmin ettiğin her birinin yanına **neden** yanlış olduğunu yaz.

### E3 — Karakter sayma ⭐
`TextStats.java`:
- `countChar(String s, char target)` → int: target kaç kez geçiyor.
- `countVowels(String s)` → int: sesli harf sayısı (`a e i o u`, büyük/küçük fark etmeksizin).
- `countDigits(String s)` → int: `Character.isDigit` ile.
- `countWords(String s)` → int: kelime sayısı. `"  hello   world  "` → 2 vermeli. **Boş String ve sadece boşluk** için ne dönmeli? Karar ver, yorumla yaz.

Test: `"Hello World 2026"`, `""`, `"   "`, `"aEiOu"`.

### E4 — Reverse ve Palindrome ⭐⭐ (trace table zorunlu)
`Palindrome.java`:
1. `reverse(String s)` → String: **StringBuilder kullanmadan**, döngüyle. Sonra `reverseWithBuilder(String s)` → String: **StringBuilder ile**. İkisini aynı girdilerle karşılaştır.
2. `isPalindrome(String s)` → boolean: **iki uç yöntemiyle** (Gün 4 `reverseInPlace`'teki `left`/`right`), ters String oluşturmadan. Farklı karakter bulduğun an `return false`.
   **Kodlamadan önce** `"racecar"` ve `"abca"` için trace table (sütunlar: tur, left, right, charAt(left), charAt(right), sonuç).
3. `isPalindromeIgnoreCase(String s)`: `"Racecar"` → true.
4. **Bonus (zor):** `isSentencePalindrome(String s)`: harf/rakam olmayan karakterleri atla. `"A man, a plan, a canal: Panama"` → true. İpucu: `left`/`right`'ı harf olmayan karakterlerde ilerlet.

**Soru:** `reverse` metodunda `result = result + ch` kullandıysan, 1 milyon karakterlik String'te ne olur? Neden `reverseWithBuilder` daha iyi?

### E5 — `char` aritmetiği: Caesar şifresi ⭐
`Caesar.java`:
- `encrypt(String text, int shift)` → String: her **harfi** alfabede `shift` kadar kaydır, sona gelince başa dön (`'z' + 1` → `'a'`). Büyük harf büyük, küçük harf küçük kalsın. Harf olmayan karakterler (boşluk, rakam, noktalama) **aynen** kalsın.
- `decrypt(String text, int shift)` → String: **`encrypt`'i çağırarak** (tek satır). İpucu: geri kaydırmak = kaç ileri kaydırmak?
- Test: `encrypt("Hello, World!", 3)` → `"Khoor, Zruog!"` · `decrypt` ile geri al · `shift = 26` ve `shift = 29` ne verir?

İpucu: `'x' - 'a'` → 0–25 arası sıra; `% 26` → başa dönme (Gün 1 mod).

### E6 — StringBuilder performans deneyi (kısa)
`Concat.java`: 0'dan 100000'e kadar sayıları önce `+` ile, sonra `StringBuilder` ile birleştir. Her birinin süresini ölç:
```java
long start = System.currentTimeMillis();
// ... iş ...
long elapsed = System.currentTimeMillis() - start;
```
İki sonucu yazdır. Fark ne kadar? Bölüm 5'teki açıklamayla bir cümlede bağla.

---

## 🧪 HAFTA 1 MİNİ SINAV (60 dk) — kapalı kitap

**Kurallar:** Notlara, DERS dosyalarına, eski koduna, internete bakmak **yok**. IntelliJ açık olabilir ama **AI kapalı**. Süreyi tut. Bittiğinde `hafta-01/gun-05/SINAV.md` dosyasına teorik cevapları yaz, kodları `Exam1.java`, `Exam2.java`, `Exam3.java` olarak kaydet. **Ben kontrol edeceğim — kendin "doğru" diye işaretleme.**

### Bölüm A — Teori (20 dk, her biri 1–3 cümle)
1. JDK, JRE, JVM farkı nedir? `javac` ve `java` komutları ne yapar?
2. `int x = 7 / 2;` ve `double y = 7 / 2;` → değerleri ne? `y`'nin 3.5 olması için ne yazmalısın?
3. `&&` kısa devre nedir? `if (arr != null && arr.length > 0)` ifadesinde sıra neden önemli?
4. `while` ile `do-while` farkı nedir? Hangisi en az bir kez çalışır?
5. Java pass-by-value mu? `void change(int[] a) { a[0] = 9; }` ile `void change(int[] a) { a = new int[]{9}; }` farkını açıkla.
6. Overloading nedir? Sadece dönüş tipi farklı iki metot olur mu?
7. `String s1 = "hi"; String s2 = "hi"; String s3 = new String("hi");` → `s1 == s2`, `s1 == s3`, `s1.equals(s3)` ne verir, neden?
8. String neden immutable? En az iki sebep.
9. Ne zaman `StringBuilder` kullanırsın?
10. `git add`, `git commit`, `git push` sırayla ne yapar?

### Bölüm B — Çıktı tahmini (10 dk, çalıştırmadan)
```java
// B1
int i = 5;
int j = i++ + ++i;
System.out.println(i + " " + j);

// B2
System.out.println(1 + 2 + "3" + 4 + 5);

// B3
int[] a = {1, 2, 3};
int[] b = a;
b = new int[]{7, 8, 9};
b[0] = 100;
System.out.println(a[0] + " " + b[0]);

// B4
String s = "abc";
s.concat("def");
System.out.println(s + " " + s.length());

// B5
for (int k = 0; k < 10; k += 3) {
    if (k == 6) continue;
    System.out.print(k + " ");
}
```

### Bölüm C — Kod (30 dk)
Her biri ayrı dosyada, `main`'de en az 3 test (biri kenar durum).
1. **`Exam1.java`** — `secondLargest(int[] arr)` → int: dizideki **en büyük ikinci farklı** değer. `{5, 1, 5, 3}` → 3. Tek elemanlı veya hepsi aynı dizide ne döneceğine karar ver, yorumla yaz.
2. **`Exam2.java`** — `isAnagram(String a, String b)` → boolean: aynı harflerden mi oluşuyor (`"listen"`, `"silent"` → true). Sadece küçük harf `a–z` gelir varsay. İpucu: 26 elemanlı bir `int[]` sayaç dizisi + `ch - 'a'`.
3. **`Exam3.java`** — `compress(String s)` → String: ardışık tekrarları say. `"aaabccdddd"` → `"a3b1c2d4"`. **StringBuilder kullan.** Boş String → `""`.

---

## 🔧 GIT (10 dk)

```bash
git add hafta-01/gun-05
git status            # sadece .java, DERS.md ve SINAV.md olmalı (.class yok)
git commit -m "feat: day 5 - strings and week 1 exam"
git push
```
Sonra `ILERLEME.md`'ye Gün 5 logu + **Haftalık Kontrol** tablosunda Hafta 1 satırını doldur (mini sınav puanını ben verdikten sonra) → commit + push (`docs: day 5 progress log and week 1 review`).

---

## ✅ Gün Sonu

- [ ] Isınma soruları cevaplandı
- [ ] E1–E6 tamam (E4 trace table'ları kağıtta)
- [ ] "Kağıda Yaz" kutusu kağıtta
- [ ] Mini sınav kapalı kitap yapıldı, kontrol ettirildi
- [ ] Commit + push
- [ ] `ILERLEME.md`'ye Gün 5 logu + Hafta 1 satırı (ve commit'i!)

**Yarın oturum başında soracaklarım:**
1. `s.toUpperCase();` yazıp `s`'yi yazdırınca neden değişmemiş görünür?
2. `a == b` iki String literal için neden true dönebilir, Scanner'dan gelen String için neden false?
3. `"hello".substring(1, 3)` ne döner?
4. `'9' - '0'` ne verir, tipi nedir?
5. Döngüde `+` ile String birleştirmek neden kötü?

**Yarın (Hafta 2, Gün 1):** OOP'ye giriş — class ve object, field, constructor, `this`, nesneler bellekte (bugünkü String/dizi reference bilgisinin devamı).
