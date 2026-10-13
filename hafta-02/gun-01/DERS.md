# Hafta 2 · Gün 1 (Gün 6) — OOP'ye Giriş: Class, Object, Constructor, `this`, Nesneler Bellekte

**Hedef:** Günün sonunda kendi class'ını yazıp ondan nesne üretebilmek; constructor ve `this`'in neden var olduğunu açıklayabilmek; iki değişkenin **aynı nesneyi** gösterdiği durumu (Gün 4 dizileri, Gün 5 String'leri) kendi nesnelerinde tanıyabilmek; `NullPointerException`'ın nereden geldiğini bilmek. Algoritma saatinde sınavda zorlandığın **sayaç dizisi** kalıbını iki yeni soruda tekrar kurmak.

**Süre:** ~6 saat · Isınma 20 dk · Ders 1.5 sa · Egzersiz 2.5 sa · Algoritma 1 sa · Kapanış 20 dk

**Kurallar (Hafta 1'den):**
- Sınıf, metot, değişken ve ekran mesajları **İngilizce**
- **Değişiklik → derle → çalıştır → çıktının her satırını oku.**
- **Metot adı/imzası görevde nasılsa öyle.** Önce kağıda imza, sonra kod.
- Hesaplayan metot yazdırmaz, **return** eder.

---

## 0. Isınma (20 dk) — notlara bakmadan (Gün 5 soruları)

1. `s.toUpperCase();` yazıp `s`'yi yazdırınca neden değişmemiş görünür?
2. `a == b` iki String literal için neden true dönebilir, Scanner'dan gelen String için neden false?
3. `"hello".substring(1, 3)` ne döner?
4. `'9' - '0'` ne verir, tipi nedir?
5. Döngüde `+` ile String birleştirmek neden kötü?

---

## 1. Neden class? ⭐ (10 dk)

Hafta 1'de her şeyi tek tek değişkenlerle tuttun. 3 öğrenciyi tutmak istesen:

```java
String name1 = "Ali";  int age1 = 20;  double gpa1 = 3.1;
String name2 = "Ayşe"; int age2 = 22;  double gpa2 = 3.6;
// 100 öğrenci? Paralel diziler? names[i], ages[i], gpas[i] ... birini kaydırırsan hepsi bozulur.
```

Sorun: bu değişkenler **birbirine ait** ama Java bunu bilmiyor. **Class**, birbirine ait verileri (ve o verilerle çalışan metotları) tek bir **yeni tip** altında toplar.

> **Class = kalıp / plan** (bir kez yazılır). **Object (nesne) = o kalıptan üretilmiş somut şey** (istediğin kadar).
> `String` de bir class'tı; `"enes"` onun bir nesnesiydi. Bugün **kendi** tiplerini yazıyorsun.

---

## 2. İlk class: field ve `new` ⭐⭐ (20 dk)

Ders örneği `Car` (egzersizlerde başka class'lar yazacaksın):

```java
// Car.java
public class Car {
    String brand;     // field (alan) — her nesnenin KENDİ kopyası var
    int year;
    double km;
}
```

```java
// CarDemo.java
public class CarDemo {
    public static void main(String[] args) {
        Car c1 = new Car();      // heap'te yeni bir Car nesnesi, adresi c1'e
        c1.brand = "Fiat";       // nokta ile nesnenin field'ına eriş
        c1.year = 2015;

        Car c2 = new Car();      // AYRI bir nesne
        c2.brand = "Toyota";

        System.out.println(c1.brand + " " + c2.brand);   // Fiat Toyota
        System.out.println(c2.year);                      // 0   ← hiç atamadık!
    }
}
```

**Field'ların varsayılan değeri vardır** (mülakat sorusu):

| Tip | Varsayılan |
|---|---|
| `int`, `long`, `double` ... | `0` / `0.0` |
| `boolean` | `false` |
| `char` | `'\u0000'` |
| **Her reference tip** (`String`, dizi, `Car`...) | **`null`** |

⚠️ **Local değişkenlerin (metot içindekiler) varsayılanı YOKTUR** — atamadan kullanırsan derlenmez ("might not have been initialized"). Field'larda Java senin yerine doldurur. Gün 4'te `new int[5]`'in içinin 0 olması da aynı kuraldı.

**Dosya kuralı:** `public class Car` → dosya adı **`Car.java`** olmak zorunda. Her public class kendi dosyasında.

---

## 3. Bellekte ne oluyor? ⭐⭐ (15 dk) — Gün 3-4-5'in devamı

```
STACK (main'in değişkenleri)        HEAP (nesneler)
c1 ──────────────────────────────► Car { brand="Fiat",   year=2015, km=0.0 }
c2 ──────────────────────────────► Car { brand="Toyota", year=0,    km=0.0 }
```

`c1` bir **Car değildir** — bir Car'ın **adresini** tutar. Dizide ve String'te gördüğünün aynısı. Yani:

```java
Car c3 = c1;          // YENİ NESNE YOK. c3 de aynı adresi tutar.
c3.km = 50000;
System.out.println(c1.km);   // 50000.0  ← c1 ile c3 aynı arabaya bakıyor
System.out.println(c1 == c3);                 // true  (aynı adres)
System.out.println(c1 == new Car());          // false (farklı adres — içi ne olursa olsun)
```

`new` görmüyorsan **yeni nesne yoktur**. (Gün 4: `int[] b = a;` → aynı dizi. Bugün de aynı kural.)

**`null` ve `NullPointerException`:**
```java
Car c4 = null;           // c4 hiçbir nesneyi göstermiyor
System.out.println(c4.brand);   // 💥 NullPointerException (çalışma anı hatası)
```
Adresi olmayan bir şeyin field'ına gitmeye çalıştın. Java'da en sık görülen hata budur — stack trace'te **satır numarasına** bak, o satırda noktadan önceki hangi değişken null, onu bul.

---

## 4. Instance metot: nesnenin kendi davranışı ⭐⭐ (15 dk)

Hafta 1'de bütün metotların `static` idi. Şimdi **`static` olmayan** metot yazacağız:

```java
public class Car {
    String brand;
    int year;
    double km;

    int age(int currentYear) {          // static YOK
        return currentYear - year;       // year → BU nesnenin year'ı
    }

    void drive(double distance) {
        km = km + distance;              // BU nesnenin km'sini değiştirir
    }
}
```
```java
c1.drive(120);      // c1'in km'si artar, c2'ninki değil
c2.age(2026);       // c2'nin yılına göre hesaplar
```

**Fark:**
- **static olmayan (instance) metot** → bir nesne üzerinden çağrılır (`c1.drive(...)`), o nesnenin field'larını görür.
- **static metot** → class'a aittir, nesne gerektirmez (`Math.max(...)`, senin Hafta 1 metotların). Field'lara doğrudan erişemez çünkü "hangi nesnenin?" sorusunun cevabı yok.

Bugünlük kural: **nesnenin verisiyle çalışan metot → static değil.** Demo class'ında main'in yardımcısı olan metotlar → static kalabilir. `static`'in detayı Gün 2'de.

---

## 5. Constructor ⭐⭐ (20 dk)

Sorun: `new Car()` sonrası brand `null`, year `0`. Bir arabanın markasız olması anlamsız — **nesne doğduğu anda geçerli olmalı.** Constructor, nesne oluşturulurken çalışan özel metottur:

```java
public class Car {
    String brand;
    int year;
    double km;

    Car(String brand, int year) {    // dönüş tipi YOK, adı class adıyla AYNI
        this.brand = brand;
        this.year = year;
        // km yazmadık → 0.0 kalır
    }
}
```
```java
Car c1 = new Car("Fiat", 2015);   // new → yer ayır → field'lar varsayılan → constructor çalışır
```

**Kurallar (mülakat):**
1. Adı class adıyla aynı, **dönüş tipi yok** (`void` bile yok — `void Car(...)` yazarsan normal bir metot olur, constructor olmaz!).
2. Hiç constructor yazmazsan Java gizlice **parametresiz boş bir tane** ekler (default constructor). O yüzden `new Car()` çalışıyordu.
3. **Bir tane bile yazarsan** o gizli constructor **kaybolur** → `new Car()` artık derlenmez. İstiyorsan kendin yazmalısın.
4. Overloading (Gün 3) constructor'da da geçerli: farklı parametre listeleriyle birden fazla constructor olabilir.

---

## 6. `this` ⭐⭐ (15 dk)

```java
Car(String brand, int year) {
    brand = brand;     // ⚠️ parametreyi kendisine atar! field null kalır.
}
```
Parametre adı field adıyla aynı olunca, metot içinde `brand` **en yakındakini** (parametreyi) gösterir — field gölgelenir (shadowing). `this` = **"şu an üzerinde çalıştığım nesne"**. `this.brand` → nesnenin field'ı, `brand` → parametre.

`this`'in ikinci kullanımı — bir constructor'dan **diğerini** çağırmak (kod tekrarı yok):
```java
Car(String brand) {
    this(brand, 2026);     // diğer constructor'ı çağır — constructor'ın İLK satırı olmak zorunda
}
```

---

## 7. Nesneyi yazdırmak ve metoda vermek (10 dk)

```java
System.out.println(c1);    // Car@1b6d3586 gibi bir şey
```
Java nesneyi nasıl yazdıracağını bilmiyor; class adı + `@` + hash yazar. Düzgün yazdırmayı (`toString`) bu hafta Gün 5'te öğreneceksin. Şimdilik field'ları kendin yazdır.

**Metoda nesne vermek** — Gün 3'teki pass-by-value aynen geçerli: metot **adresin kopyasını** alır.
- Metot `car.km = 0` yaparsa → çağıran görür (aynı nesne).
- Metot `car = new Car(...)` yaparsa → çağıran görmez (kopyayı başka yere çevirdin).

Gün 4 diziler ile **birebir aynı**. Bunu E3'te kendin kanıtlayacaksın.

---

## 📝 KAĞIDA YAZ (sadece bunlar)

1. **Class** = kalıp, **object** = `new` ile üretilen somut nesne. Değişken nesneyi değil **adresini** tutar.
2. Field varsayılanları: sayı → 0, boolean → false, reference → **null**. Local değişkenin varsayılanı yok.
3. `new` yoksa yeni nesne yok: `Car b = a;` → aynı nesne.
4. null bir değişkende `.` kullanmak → **NullPointerException**.
5. Constructor: class adıyla aynı, dönüş tipi yok. Bir tane yazarsan gizli boş constructor kaybolur.
6. `this.x = x;` → field = parametre. `this(...)` → başka constructor'ı çağır (ilk satır).
7. Nesnenin verisiyle çalışan metot **static değil**.

---

## 💻 EGZERSİZLER (AI yok, IntelliJ'de AI kapalı)

### E0 — Kurulum: package (10 dk) ⭐ — neden gerekli?

Dünkü sorunu hatırla: IntelliJ projedeki **bütün** dosyaları birlikte derliyor. Bu hafta `Book`, `Student` gibi class adları farklı günlerde tekrar gelecek → aynı adlı iki class = **"duplicate class"** hatası. Çözüm: her günün class'larını bir **package** (paket = isim alanı, klasör) altına koymak.

1. `hafta-02/gun-01/` klasörünü oluştur → sağ tık → **Mark Directory as → Sources Root** (Hafta 1'deki gibi).
2. İçinde sağ tık → **New → Package** → adı: `w2d1` (week 2 day 1 — tire yok, rakamla başlamaz, küçük harf).
3. Bugünkü bütün class'ları `w2d1` içinde oluştur. IntelliJ her dosyanın en üstüne `package w2d1;` satırını koyar — **silme**.

Aynı package içindeki class'lar birbirini import'suz görür. Tam adı `w2d1.Student`'tır; yarınki `w2d2.Student` ile çakışmaz.

### E1 — İlk class ve varsayılan değerler ⭐
`Student.java`: field'lar `name` (String), `age` (int), `gpa` (double), `graduated` (boolean). **Constructor yazma.**
`StudentDemo.java` (main burada):
1. **Önce kağıda tahmin:** `new Student()` sonrası dört field'ı yazdırınca ne çıkar? Sonra çalıştır.
2. İki öğrenci oluştur, field'larını ata, ikisini de yazdır — birini değiştirmek diğerini etkiliyor mu?
3. `System.out.println(s1);` → ne çıktı? Bir cümle: neden böyle?
4. main içinde `int x; System.out.println(x);` yaz → hata mesajını oku. Field'da hata yokken burada neden var? Bir cümle. Sonra o satırları sil.

### E2 — Constructor, `this`, instance metot ⭐⭐
`Rectangle.java`:
- Field'lar: `width`, `height` (double).
- Constructor `Rectangle(double width, double height)`.
- Constructor `Rectangle(double side)` → kare; **`this(...)` ile** diğerini çağırsın.
- Instance metotlar: `area()` → double, `perimeter()` → double, `isSquare()` → boolean, `scale(double factor)` → void (en ve boyu factor ile çarpar).

`RectangleDemo.java`: en az 3 dikdörtgen (biri kare constructor'ıyla). Her biri için alan/çevre/kare mi yazdır; birini `scale(2)` yap ve tekrar yazdır.

**Deneyler (önce tahmin, sonra çalıştır):**
- (a) İki parametreli constructor'da `this.` kısmını sil (`width = width;`). `area()` ne verdi? Neden? Sonra geri düzelt.
- (b) main'de `new Rectangle()` yaz. Hata mesajı ne? E1'de `new Student()` çalışırken burada neden çalışmıyor?

### E3 — Referanslar, null, metoda nesne vermek ⭐⭐ (tahmin tablosu zorunlu)
`ReferenceDemo.java` — `Student` class'ını kullan (E1'deki; aynı package'ta olduğu için direkt görürsün).
**Her adımdan önce çıktıyı kağıda yaz**, sonra çalıştır, yanlış tahminlerin yanına neden yanlış olduğunu yaz:
1. `Student a = new Student(); a.name = "Ali"; Student b = a; b.name = "Veli";` → `a.name`?
2. `Student c = new Student(); c.name = "Veli";` → `b == c`? `b.name.equals(c.name)`? (İkisinin farkını bir cümleyle yaz.)
3. `static void rename(Student s)` → içinde `s.name = "Renamed";` · `static void replace(Student s)` → içinde `s = new Student(); s.name = "New";`. İkisini `a` ile çağır, her seferinden sonra `a.name` yazdır. Gün 4'teki dizi deneyinle aynı sonuç mu?
4. `Student d = null; System.out.println(d.name);` → çalıştır, **stack trace'i oku**: hangi satır, hangi değişken null? Sonra satırı yoruma al.

### E4 — `BankAccount` ⭐⭐
`BankAccount.java`:
- Field'lar: `owner` (String), `balance` (double).
- Constructor `BankAccount(String owner)` → bakiye 0 ile başlar.
- `deposit(double amount)` → boolean: `amount <= 0` ise reddet (`false`), değilse ekle (`true`).
- `withdraw(double amount)` → boolean: `amount <= 0` veya bakiye yetmiyorsa `false`, değilse düş ve `true`.
- `getBalance()` → double.

`BankDemo.java`: **en az 6 test**, her biri beklenen sonuçla birlikte yazdırılsın (`"deposit 100 -> true, balance 100.0"` gibi). Kenar durumlar: negatif yatırma, 0 çekme, bakiyeden fazla çekme, tam bakiyeyi çekme.

**Son adım (yarının kancası):** main'in en sonunda `account.balance = -1000000;` yaz ve bakiyeyi yazdır. Ne oldu? `withdraw`'daki bütün kontrollerin ne işe yaradı? **Bir cümleyle** sorunu yaz — yarın Gün 2'de (encapsulation) bunu çözeceğiz.

### E5 — Nesne dizisi ⭐⭐
`Classroom.java` (main burada):
1. `Student[] students = new Student[3];` → hemen ardından `students[0]` yazdır. **Önce tahmin.** Neden? (Tablo 2'ye bak.)
2. Diziyi doldur — `Student`'a artık bir constructor ekle: `Student(String name, int age, double gpa)`. **Dikkat:** constructor ekledin → E1/E3'teki `new Student()` satırları ne oldu? Derle, gör, düzelt (nasıl düzelteceğine sen karar ver — iki yol var, birini seç ve yorumla yaz).
3. `static Student topStudent(Student[] arr)` → **en yüksek gpa'lı öğrenciyi** döndürsün (adını değil, **nesnenin kendisini**). Gün 4'teki max kalıbı. Boş dizi gelirse `null` dönsün.
4. `static double averageGpa(Student[] arr)` → double.
5. Test: normal dizi, tek elemanlı dizi, boş dizi (`new Student[0]`). Boş dizide `topStudent(...)`'ın sonucunun `.name`'ine erişirsen ne olur? Bunu main'de nasıl önlersin?

---

## 🧩 ALGORİTMA SAATİ (1 sa) — sayaç dizisi kalıbı

Hafta 1 sınavında en çok zorlandığın kalıp: **`int[26]` sayaç + `ch - 'a'`** (Exam2 isAnagram). Bugün aynı kalıbı iki yeni soruda kuracaksın.

**Yöntem (her soru için, kod yazmadan önce):**
1. İmzayı kağıda yaz.
2. Çözümü **3-5 Türkçe cümleyle** kağıda yaz ("önce ... sayarım, sonra ...").
3. Bir örnek için elle sayaç dizisinin durumunu çiz (sadece değişen harfler yeter).
4. Sonra IntelliJ'de kendi `main` testlerinle yaz (`w2d1` içinde `Algo1.java`, `Algo2.java`). En son LeetCode'a gönder.

**A1 — [LeetCode 387: First Unique Character in a String](https://leetcode.com/problems/first-unique-character-in-a-string/)**
`firstUniqChar(String s)` → int: tekrar etmeyen **ilk** karakterin index'i, yoksa -1. `"leetcode"` → 0, `"loveleetcode"` → 2, `"aabb"` → -1.
İpucu: kaç tur döngü gerekiyor? Tek turda "ilk"i bilebilir misin?

**A2 — [LeetCode 383: Ransom Note](https://leetcode.com/problems/ransom-note/)**
`canConstruct(String ransomNote, String magazine)` → boolean: magazine'deki harflerle (her biri bir kez kullanılabilir) ransomNote yazılabilir mi? `("a","b")` → false, `("aa","ab")` → false, `("aa","aab")` → true.
İpucu: Exam2'deki isAnagram'a çok benziyor — farkı ne? Önce farkı bir cümleyle yaz.

20 dakikada bir soruda hiç ilerleme yoksa dur, kağıttaki 3-5 cümleni bana göster.

---

## 🔧 GIT (10 dk)

```bash
git add hafta-02/gun-01
git status            # sadece .java ve DERS.md olmalı (.class yok)
git commit -m "feat: week 2 day 1 - classes and objects"
git push
```
Sonra `ILERLEME.md`'ye Gün 6 logu → commit + push (`docs: day 6 progress log`). **ILERLEME commit'ini unutma** (Hafta 1'de bir kez kaldı).

---

## ✅ Gün Sonu

- [ ] Isınma soruları cevaplandı
- [ ] E0 package kurulumu
- [ ] E1–E5 tamam (E3 tahmin tablosu kağıtta)
- [ ] A1–A2 (önce kağıtta 3-5 cümle)
- [ ] "Kağıda Yaz" kutusu kağıtta
- [ ] Commit + push, `ILERLEME.md` Gün 6 logu (ve commit'i!)

**Yarın oturum başında soracaklarım:**
1. Class ile object arasındaki fark nedir? Bir örnekle.
2. `Student s;` field olarak tanımlıysa değeri ne? Metot içinde local olarak tanımlıysa?
3. Constructor yazınca `new Student()` neden derlenmeyebilir?
4. `this.name = name;` satırında iki `name` neyi gösterir?
5. `Student b = a; b.age = 30;` → `a.age` ne olur? Neden?

**Yarın (Hafta 2, Gün 2):** Encapsulation (`private`, getter/setter, geçersiz durumu engellemek — E4'teki `-1000000` sorunu) ve `static` (class'a ait alan/metot, sayaç örneği).
