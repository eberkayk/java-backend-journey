# Hafta 1 · Gün 2 — Koşullar (if/else, switch) ve Döngüler

**Hedef:** Günün sonunda bir problemi "koşul + döngü" adımlarına bölüp kağıtta tasarlayabilmek, sonra Java'ya çevirebilmek.

**Süre:** ~6 saat · Isınma 20 dk · Ders 1.5 sa · Egzersiz 3.5 sa · Kapanış 20 dk

**Dünden kalan iki kural:**
- Her zaman `public static void main(String[] args)` (kısaltma yok)
- Sınıf, değişken ve ekran mesajları **İngilizce**

---

## 0. Isınma (20 dk) — notlara bakmadan

Claude'a sözlü/yazılı cevapla:
1. JDK, JRE, JVM farkı?
2. `7 / 2` neden 3?
3. Primitive ve reference farkı?
4. E2'de hangi tahminlerin yanlıştı, neden?
5. `.gitignore`'a satır ekledin ama dosya hâlâ `git status`'ta görünüyor. İki olası sebep?

---

## 1. if / else if / else (20 dk)

```java
int score = 72;

if (score >= 85) {
    System.out.println("AA");
} else if (score >= 70) {
    System.out.println("BB");
} else {
    System.out.println("FF");
}
```

- Koşullar **yukarıdan aşağı** kontrol edilir, **ilk doğru olan** çalışır, gerisine bakılmaz. Sıra önemlidir.
- Koşulun içi mutlaka `boolean` olmalı. `if (score)` Java'da derlenmez (C'den farklı).
- Tek satır olsa bile süslü parantez `{}` kullan. Şirketlerde kural budur.

### Ternary operatörü
```java
String result = score >= 50 ? "PASS" : "FAIL";   // koşul ? doğruysa : yanlışsa
```
Sadece basit, tek değer seçen durumlarda kullan. İç içe ternary okunmaz.

### Kısa devre (short-circuit) ⭐ Mülakat
- `a && b` → `a` false ise `b`'ye **hiç bakılmaz**.
- `a || b` → `a` true ise `b`'ye **hiç bakılmaz**.

```java
if (count != 0 && total / count > 10) { ... }   // count 0 ise bölme hiç yapılmaz → hata yok
```

### Tuzak: String karşılaştırma
```java
String a = scanner.nextLine();
if (a == "yes") { ... }        // ❌ adresleri karşılaştırır, çoğu zaman false
if (a.equals("yes")) { ... }   // ✅ içeriği karşılaştırır
```
Sebebi dünkü primitive/reference farkı. Detayı Gün 5'te.

---

## 2. switch (20 dk)

### Klasik switch
```java
switch (day) {
    case 1:
        System.out.println("Monday");
        break;
    case 2:
        System.out.println("Tuesday");
        break;
    default:
        System.out.println("Unknown");
}
```
⚠️ `break` unutulursa alttaki case'ler de çalışır (**fall-through**). Klasik bir bug kaynağıdır.

### Modern switch (Java 14+) — yeni kodda bunu kullan
```java
String name = switch (day) {
    case 1, 2, 3, 4, 5 -> "Weekday";
    case 6, 7 -> "Weekend";
    default -> "Unknown";
};
```
- Ok (`->`) kullanınca fall-through **olmaz**, `break` gerekmez.
- Değer döndürebilir (switch expression).
- `int`, `char`, `String`, `enum` ile çalışır. `double` ve `boolean` ile çalışmaz.

---

## 3. Döngüler (35 dk) ⭐

### for — kaç kez döneceğin belliyse
```java
for (int i = 0; i < 5; i++) {
    System.out.println(i);     // 0 1 2 3 4
}
```
`for (başlangıç; koşul; her turdan sonra)`. Koşul **her turun başında** kontrol edilir.

### while — ne zaman biteceği belli değilse
```java
int n = 472;
while (n > 0) {
    System.out.println(n % 10);   // son rakam
    n = n / 10;                   // son rakamı at
}
```
Dünkü E6'yı hatırla: `%10` ve `/10` ile rakam ayırmak. Artık bunu **her uzunlukta** sayı için yapabilirsin.

### do-while — en az bir kez çalışması gerekiyorsa
```java
int choice;
do {
    System.out.print("Enter 1-3: ");
    choice = scanner.nextInt();
} while (choice < 1 || choice > 3);
```
Kullanıcı girdisi doğrulamada (menüler) kullanılır.

### break ve continue
- `break` → döngüden **tamamen çık**.
- `continue` → bu turun kalanını atla, **sonraki tura geç**.

### İç içe döngü
```java
for (int row = 1; row <= 3; row++) {
    for (int col = 1; col <= row; col++) {
        System.out.print("*");
    }
    System.out.println();
}
```
Dış döngünün **her** turunda iç döngü baştan sona çalışır.

### Döngü tuzakları
- **Sonsuz döngü:** koşulu değiştiren satırı unutmak (`n = n / 10` olmazsa?). Terminalde `Ctrl + C` ile durdur.
- **Off-by-one:** `i < 5` mi `i <= 5` mi? 0'dan mı 1'den mi başlıyorsun? Kodu yazmadan önce ilk ve son turu kağıtta kontrol et.
- Döngü içinde tanımlanan değişken döngü bitince **yok olur** (scope).

---

## 4. Kağıtta Çalıştırma (Trace Table) (15 dk) ⭐

Döngülü kodu kafanda çalıştırmanın yolu: her tur için bir satır, her değişken için bir sütun.

```java
int sum = 0;
for (int i = 1; i <= 4; i++) {
    sum += i;
}
```

| tur | i | koşul `i <= 4` | sum |
|---|---|---|---|
| başlangıç | 1 | true | 0 → 1 |
| 2 | 2 | true | 3 |
| 3 | 3 | true | 6 |
| 4 | 4 | true | 10 |
| — | 5 | **false** → çık | 10 |

Mülakatta "bu kodun çıktısı ne?" sorusunu böyle çözersin. **Bugünkü her egzersizde, kodu yazmadan önce küçük bir örnekle trace table yap.**

---

## 📝 KAĞIDA YAZ (sadece bunlar)

1. if-else if: **ilk doğru** koşul çalışır, gerisine bakılmaz.
2. `&&` / `||` kısa devre: sol taraf sonucu belirliyorsa sağa bakılmaz.
3. String karşılaştırma `==` ile değil `.equals()` ile.
4. Klasik switch'te `break` yoksa fall-through. Modern `->` switch'te yok.
5. for: sayı belli · while: sayı belli değil · do-while: en az 1 kez.
6. `break` = döngüden çık · `continue` = sonraki tura geç.
7. Döngüyü yazmadan önce: ilk tur, son tur, bitiş koşulu → trace table.

---

## 💻 EGZERSİZLER (AI yok, önce kağıtta tasarla)

Klasör: `hafta-01/gun-02/`. Her egzersiz ayrı dosya, İngilizce isim.

### E1 — Grade calculator (if/else)
Kullanıcıdan 0-100 arası not al, harf notu yazdır: 90+ AA, 80+ BA, 70+ BB, 60+ CB, 50+ CC, altı FF. 0-100 dışındaysa `"Invalid score"`.
**Soru:** Koşulları ters sırada (önce `>= 50`) yazsaydın ne olurdu? Kağıtta açıkla.

### E2 — Day name (switch)
1-7 arası sayı al, gün adını yazdır. Önce **klasik** switch ile yaz, sonra **modern** `->` switch ile yaz. Klasik olanda bir `break`'i bilerek sil, 1 gir, ne olduğunu gözlemle.

### E3 — FizzBuzz ⭐ (klasik mülakat ısınma sorusu)
1'den 100'e kadar yazdır. 3'e bölünenlerde `Fizz`, 5'e bölünenlerde `Buzz`, ikisine de bölünenlerde `FizzBuzz`, diğerlerinde sayının kendisi.
**Dikkat:** Koşulların sırası. 15 için ne yazıyor, kontrol et.

### E4 — Sum and factorial
Kullanıcıdan `n` al. 1'den n'e kadar toplamı ve `n!` (faktöriyel) yazdır.
- Önce `int` ile dene: `n = 13` için faktöriyel doğru mu? (`13! = 6227020800`)
- Sorunu açıkla (dünkü bir konu), düzelt. `long` ile kaça kadar doğru çalışır? Bul.

### E5 — Multiplication table (iç içe döngü)
1'den 10'a çarpım tablosu yazdır. Sütunlar hizalı olsun (`printf("%4d", ...)` araştır).

### E6 — Reverse number ⭐ (Gün 5'teki palindromun hazırlığı)
Kullanıcıdan pozitif bir tam sayı al (ör. `12345`), tersini **sayı olarak** üret (`54321`). String'e çevirmek yasak, sadece `%`, `/`, `*`, `+`.
Kodu yazmadan önce `123` için trace table yap.
**Bonus:** Sayı kendi tersine eşitse `"Palindrome"` yazdır (`12321`).

### E7 — Prime check
Kullanıcıdan bir sayı al, asal mı değil mi yazdır. Bölen bulduğun anda döngüyü bitir (`break`).
**Düşün:** 2'den n-1'e kadar denemek gerekir mi? Daha erken nerede durabilirsin? (İpucu: `n = 36`'nın bölenlerini çiftler halinde yaz.)

### E8 — Number guessing game (while + do-while)
Program 1-100 arası rastgele bir sayı tutar. Kullanıcı tahmin eder, program `"Higher"` / `"Lower"` der. Bulunca deneme sayısını yazdır.
Rastgele sayı için `java.util.Random` sınıfını **dokümandan** araştır (`nextInt(bound)` metodu sınırları nasıl kullanıyor? 0 dahil mi, bound dahil mi?).

### E9 (bonus) — Pattern
Kullanıcıdan `n` al, n=4 için:
```
   *
  ***
 *****
*******
```
Önce kağıtta: satır `i` için kaç boşluk, kaç yıldız? Formülü bul, sonra kodla.

---

## 🔧 GIT (10 dk)

Gün sonunda:
```bash
git add hafta-01/gun-02
git status            # sadece .java ve DERS.md olmalı, .class olmamalı
git commit -m "feat: day 2 - conditionals and loops"
git push
```

---

## ✅ Gün Sonu

- [ ] Isınma soruları cevaplandı
- [ ] E1–E8 tamam (E9 bonus)
- [ ] "Kağıda Yaz" kutusu kâğıtta
- [ ] Commit + push
- [ ] `ILERLEME.md`'ye Gün 2 logu

**Yarın oturum başında soracaklarım:**
1. `&&` kısa devre nedir, neden işe yarar? Bir örnek ver.
2. Klasik switch'te fall-through nedir?
3. while ile do-while farkı nedir, do-while nerede kullanılır?
4. `"yes" == input` neden yanlış?
5. E4'te faktöriyelde ne oldu, neden?

**Yarın (Gün 3):** Metotlar (parametre, return, overloading, scope) ve pass-by-value.
