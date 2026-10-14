# Hafta 2 · Gün 2 (Gün 7) — Encapsulation (`private`, getter/setter) ve `static`

**Hedef:** Dünkü `account.balance = -100000;` sorununu **derleyiciye engelletmek**; bir nesnenin hiçbir zaman geçersiz duruma düşmemesini sağlamak; `static` bir alanın/metodun nesneye değil **class'a** ait olduğunu açıklayabilmek. Algoritma saatinde sayaç dizisini bir adım öteye taşımak ve **iki pointer** kalıbına giriş.

**Süre:** ~6 saat · Isınma 20 dk · Ders 1.5 sa · Egzersiz 2.5 sa · Algoritma 1 sa · Kapanış 20 dk

**Kurallar (değişmedi):**
- Sınıf, metot, değişken ve ekran mesajları **İngilizce**
- **Değişiklik → derle → çalıştır → çıktının her satırını oku.**
- **Metot adı/imzası görevde nasılsa öyle.** Önce kağıda imza, sonra kod.
- Hesaplayan metot yazdırmaz, **return** eder.

---

## 0. Isınma (20 dk) — notlara bakmadan (Gün 6 soruları)

1. Class ile object arasındaki fark nedir? Bir örnekle.
2. `Student s;` field olarak tanımlıysa değeri ne? Metot içinde local olarak tanımlıysa?
3. Constructor yazınca `new Student()` neden derlenmeyebilir?
4. `this.name = name;` satırında iki `name` neyi gösterir?
5. `Student b = a; b.age = 30;` → `a.age` ne olur? Neden?

---

## 1. Dünkü sorun ⭐ (10 dk)

```java
account.withdraw(500);          // false — kontrol çalıştı, güzel
account.balance = -100000;      // ...ama kapıyı kullanmadan duvardan girdin
```

`deposit` ve `withdraw` içindeki bütün kontroller, **field'a doğrudan erişilebildiği sürece** işe yaramaz. Kural koymak yetmez; kuralı **atlamanın imkânsız** olması gerekir.

> **Encapsulation (kapsülleme):** Nesnenin verisini dışarıya kapatıp, ona sadece nesnenin kendi metotları üzerinden erişilmesine izin vermek. Böylece nesne kendi kurallarını (**invariant** — "bakiye asla negatif değildir" gibi her zaman doğru kalması gereken şart) kendisi korur.

---

## 2. Erişim belirleyiciler (access modifiers) ⭐⭐ (15 dk)

| Belirleyici | Kim erişebilir? |
|---|---|
| `private` | **Sadece aynı class'ın içi** |
| *(hiçbir şey yazmamak)* — "package-private" | Aynı **package** içindeki class'lar |
| `protected` | Package + alt class'lar (Gün 3 inheritance'ta) |
| `public` | Herkes |

Dün hiçbir şey yazmadın → field'lar package-private'tı → `BankDemo` aynı package'ta (`w2d1`) olduğu için direkt erişebildi.

```java
public class BankAccount {
    private String owner;
    private double balance;
    ...
}
```
Artık `BankDemo`'da `account.balance = -100000;` → **derleme hatası**. Hata çalışma anında değil, kod yazarken yakalanıyor. Bu en iyi hata türüdür.

**Varsayılan alışkanlık (mülakatta söylenecek):** Field'lar **her zaman `private`**. Dışarıya ne açılacağına bilinçli karar verilir.

⚠️ **İnce ama önemli (mülakat):** `private` **class seviyesindedir, nesne seviyesinde değil.** `BankAccount` içindeki bir metot, **başka** bir `BankAccount` nesnesinin `private` field'ına erişebilir:
```java
boolean transfer(BankAccount to, double amount) {
    ...
    to.balance += amount;     // derlenir! aynı class'ın içindeyiz
}
```
(Yine de başka nesnenin field'ına doğrudan yazmak yerine onun metodunu — `to.deposit(amount)` — çağırmak genelde daha iyidir: o nesnenin kendi kontrolleri de çalışır.)

---

## 3. Getter ve setter ⭐⭐ (20 dk)

Field kapalı. Okumak/değiştirmek gerekiyorsa **metot** açarsın:

```java
public class Student {
    private String name;
    private double gpa;

    public double getGpa() {            // getter: get + FieldAdı (boolean ise genelde isXxx)
        return gpa;
    }

    public boolean setGpa(double gpa) { // setter: kontrol EDEREK değiştirir
        if (gpa < 0.0 || gpa > 4.0) {
            return false;               // geçersiz → eski değer korunur
        }
        this.gpa = gpa;
        return true;
    }
}
```

**"Her field'a getter + setter yaz" ≠ encapsulation.** Kontrolsüz bir setter, field'ı `public` yapmakla aynıdır:
```java
public void setBalance(double balance) { this.balance = balance; }   // -100000'i yine kabul eder!
```
Sorulacak soru şudur: **"Bu değer dışarıdan değiştirilmeli mi? Değiştirilecekse hangi kurala göre?"**
- `balance` → dışarıdan **set edilmemeli**. Sadece `deposit`/`withdraw` ile değişir. → getter var, **setter yok**.
- `owner` → hesap açıldıktan sonra değişmiyorsa → getter var, setter yok.
- `gpa` → değişebilir ama 0.0–4.0 arasında → **kontrollü** setter.

> Not: Gerçek projelerde geçersiz değer gelince `false` dönmek yerine **exception** fırlatılır (Hafta 3). Şimdilik `boolean` dönüyoruz, `deposit`/`withdraw` ile aynı fikir.

**Constructor da bir kapıdır:** `new Student("Ali", 20, 9.7)` → setter'da yakaladığın gpa'yı constructor'da da yakalamalısın, yoksa geçersiz nesne doğar. Constructor'dan `false` dönemezsin; bugünlük çözüm: constructor içinde setter'ı çağır ve geçersizse güvenli bir varsayılan bırak (ör. 0.0). Hafta 3'te bunu exception ile düzelteceğiz.

---

## 4. `final` field ⭐ (10 dk)

```java
private final String owner;
```
`final` field **bir kez** atanır (tanımlandığı satırda ya da constructor'da) ve sonra **asla** değişmez. Constructor'da atamazsan derlenmez. Class'ın içinden bile yanlışlıkla `owner = "x";` yazılamaz → derleyici korur.

Değişmemesi gereken her field'ı `final` yap: niyetini koda yazmış olursun.

---

## 5. `static` field — class'a ait veri ⭐⭐ (20 dk)

Soru: Şimdiye kadar kaç `Student` nesnesi oluşturuldu? Bu bilgi **hangi öğrenciye** ait? Hiçbirine — **class'a** ait.

```java
public class Student {
    private static int count = 0;   // TEK kopya, bütün Student'lar paylaşır
    private String name;            // HER nesnenin kendi kopyası

    public Student(String name) {
        this.name = name;
        count++;                    // her new'de ortak sayaç artar
    }
}
```

```
HEAP
 Student { name="Ali"  }      Student { name="Ayşe" }      Student { name="Can" }
        \                            |                           /
         └──────────── Student.count = 3  (class'ın kendi alanı, tek) ─┘
```

**Kurallar:**
- `static` field nesne oluşturulmadan da vardır (class yüklendiğinde oluşur).
- Erişim: **`ClassAdı.alan`** (`Student.count`). `s1.count` de derlenir ama yanıltıcıdır — okuyan kişi "s1'e ait" sanır.
- `static` field'ın da varsayılan değeri vardır (0, false, null).

**Tipik kullanım — her nesneye benzersiz id:**
```java
private static int nextId = 1;
private final int id;

public Student(String name) {
    this.id = nextId;     // bu nesnenin id'si
    nextId++;             // bir sonraki nesne için ortak sayacı artır
    ...
}
```

---

## 6. `static` metot ⭐⭐ (15 dk)

```java
public static int getCount() {
    return count;          // OK — static field
    // return name;        // ❌ derlenmez: HANGİ öğrencinin name'i?
    // this.name           // ❌ static metotta this yok, çünkü nesne yok
}
```
Çağrı: `Student.getCount()` — nesne gerekmez.

| | instance (static değil) | static |
|---|---|---|
| Kime ait? | Nesneye | Class'a |
| Çağrı | `s1.getName()` | `Student.getCount()` |
| `this` var mı? | Var | **Yok** |
| Instance field'a erişir mi? | Evet | **Hayır** (doğrudan) |
| Static field'a erişir mi? | Evet | Evet |

**Neden `main` static?** Program başlarken hiç nesne yok. JVM'in `main`'i çağırabilmesi için nesneye ihtiyaç duymaması gerekir.
**Neden Hafta 1'de her metodun `static` idi?** Çünkü `main` (static) onları nesnesiz çağırıyordu. Static bir metot, aynı class'taki static olmayan bir metodu nesne olmadan çağıramaz — yarın bunu hatada göreceksin.

---

## 7. Sabitler ve utility class (10 dk)

```java
public static final double TAX_RATE = 0.20;    // sabit: class'a ait + değişmez
```
- İsimlendirme: `UPPER_SNAKE_CASE`.
- `Math.PI`, `Integer.MAX_VALUE` → hepsi `public static final`.
- Kod içine `0.20` gömmek yerine (magic number) isim verilmiş sabit kullanılır.

**Utility class:** Sadece static metotlardan oluşan, nesnesi üretilmeyen class (`Math` gibi). Nesne üretilmesini engellemek için constructor `private` yapılır:
```java
public class MathUtils {
    private MathUtils() { }               // kimse new MathUtils() diyemez
    public static int max(int[] arr) { ... }
}
```

---

## 📝 KAĞIDA YAZ (sadece bunlar)

1. **Encapsulation** = field `private`, erişim sadece kontrollü metotlarla. Amaç: nesne asla geçersiz duruma düşmesin.
2. `private` → sadece aynı **class** (nesne değil!). Hiçbir şey yazmamak → aynı package.
3. Kontrolsüz setter = public field. Her field'a setter yazılmaz; "dışarıdan değişmeli mi?" diye sor.
4. `final` field → bir kez atanır (tanımda veya constructor'da), sonra değişmez.
5. `static` field → class'a ait **tek kopya**. `static` metot → nesnesiz çağrılır, `this` yok, instance field'a erişemez.
6. Sabit: `public static final TYPE NAME_IN_CAPS`.

---

## 💻 EGZERSİZLER (AI yok, IntelliJ'de AI kapalı)

### E0 — Kurulum (5 dk)
`hafta-02/gun-02/` → **Sources Root** → içinde package **`w2d2`**. Bugünkü bütün class'lar burada. (Dünkü class'lara dokunma; gerekenleri **bugünkü package'a yeniden yazacaksın** — kopyala-yapıştır değil, elle yaz, tekrar iyidir.)

### E1 — `BankAccount` v2: kapıyı kapat ⭐⭐
`w2d2/BankAccount.java`:
- `owner` → `private final`. `balance` → `private`.
- Constructor `BankAccount(String owner)`.
- `deposit`, `withdraw`, `getBalance` dünkü gibi. `getOwner()` ekle. **`setBalance` yazma.**
- Yeni: `boolean transfer(BankAccount to, double amount)` → bu hesaptan `to` hesabına para aktarır. Geçersizse (`amount <= 0`, bakiye yetmiyor, **`to` null**, **`to` kendisi** — `this`) `false` döner ve **iki hesapta da hiçbir şey değişmez**.
  - İpucu: Kendi yazdığın `withdraw`/`deposit`'u kullanabilir misin? Kullanırsan kontrolleri tekrar yazman gerekir mi?

`w2d2/BankDemo.java`:
1. Dünkü `account.balance = -100000;` satırını yaz → **hata mesajını oku**, kağıda bir cümleyle yaz, sonra satırı yoruma al.
2. `account.owner = "Hacker";` yaz → hata mesajı ne? (Dikkat: `private` olmasaydı bile derlenir miydi? `final` olduğu için?) Yoruma al.
3. `transfer` için **en az 5 test**, her biri beklenen sonuçla ve **iki hesabın bakiyesiyle** yazdırılsın. Kenar durumlar: başarılı, yetersiz bakiye, negatif tutar, `null` hesap, kendine transfer.

### E2 — `Student` v2: kontrollü setter ⭐⭐
`w2d2/Student.java`:
- `private` field'lar: `name` (String), `age` (int), `gpa` (double).
- Constructor `Student(String name, int age, double gpa)`.
- Getter'lar: hepsi. Setter'lar: `boolean setAge(int age)` (0–150 arası geçerli), `boolean setGpa(double gpa)` (0.0–4.0). `name` için setter **yok**.
- **Constructor geçersiz değerle çağrılırsa** (`new Student("Ali", -5, 9.7)`) nesne geçersiz doğmamalı. Constructor'da kontrolü **tekrar yazmadan** nasıl yaparsın? (İpucu: §3 son paragraf.)

`w2d2/StudentDemo.java`: Önce kağıda tahmin, sonra çalıştır:
1. `setGpa(3.5)`, `setGpa(4.5)`, `setGpa(-1)` → her birinin dönüşü ve sonra `getGpa()`.
2. `new Student("Ali", -5, 9.7)` → `getAge()` ve `getGpa()` ne?
3. Bir cümle: `name` için neden setter yazmadık? Yazsaydık hangi kontrol mantıklı olurdu?

### E3 — `static` sayaç ve id ⭐⭐
E2'deki `Student`'a ekle:
- `private static int count` → kaç öğrenci oluşturuldu. `public static int getCount()`.
- `private final int id` → her öğrenciye 1'den başlayan benzersiz id (§5'teki `nextId` kalıbı). `getId()`.

`w2d2/StaticDemo.java`:
1. Hiç öğrenci oluşturmadan `Student.getCount()` yazdır. **Önce tahmin.**
2. 3 öğrenci oluştur, her birinin id'sini ve `Student.getCount()`'u yazdır.

**Deneyler (önce tahmin, sonra çalıştır, sonra geri al):**
- (a) `count`'un önündeki `static`'i sil. Ne oluyor? Hata nerede çıkıyor ve **neden orada**? (Bir cümle.) `getCount()`'tan da `static`'i silersen bu sefer hata nerede çıkar?
- (b) `getCount()` (static) içinde `return name.length();` yaz. Hata mesajını oku, kağıda yaz.
- (c) `s1.getCount()` yaz. Derleniyor mu? IntelliJ ne uyarısı veriyor? Neden kötü bir yazım? (Bir cümle.)

### E4 — Tahmin tablosu: static vs instance ⭐⭐ (kağıt zorunlu, **önce çalıştırma**)
Aşağıdaki kodu **kağıtta** izle, her `println`'ın çıktısını yaz. Sonra `w2d2/Counter.java` + `w2d2/CounterDemo.java` olarak yaz, çalıştır, yanlışlarının yanına neden yanlış olduğunu yaz.

```java
public class Counter {
    private static int total = 0;
    private int mine = 0;

    public void click() {
        mine++;
        total++;
    }
    public int getMine() { return mine; }
    public static int getTotal() { return total; }
}
```
```java
Counter a = new Counter();
Counter b = new Counter();
a.click(); a.click(); b.click();
System.out.println(a.getMine() + " " + b.getMine() + " " + Counter.getTotal());   // 1
Counter c = a;
c.click();
System.out.println(a.getMine() + " " + c.getMine() + " " + Counter.getTotal());   // 2
Counter d = new Counter();
System.out.println(d.getMine() + " " + Counter.getTotal());                       // 3
```

### E5 — `Product` + sabit + utility ⭐⭐
`w2d2/Product.java`:
- `public static final double TAX_RATE = 0.20;`
- `private final String name`, `private double price`.
- Constructor `Product(String name, double price)` — price negatifse 0.0 olsun (E2'deki gibi setter üzerinden).
- `boolean setPrice(double price)` (negatif → false), `getName()`, `getPrice()`.
- `double priceWithTax()` → vergili fiyat. **`0.20` yazma, sabiti kullan.**

`w2d2/PriceUtils.java` — utility class:
- `private` constructor.
- `static double totalWithTax(Product[] products)` → hepsinin vergili toplamı. Boş dizi → 0.0.
- `static Product cheapest(Product[] products)` → en ucuz ürünün **kendisi**; boş dizi → `null`. (Dünkü E5'teki max kalıbı — başlangıç değerini neye eşitliyorsun?)

`w2d2/ShopDemo.java`: 3 ürünlük dizi, normal + boş dizi testleri. Son olarak `new PriceUtils()` yaz → hata ne? Yoruma al.

---

## 🧩 ALGORİTMA SAATİ (1 sa)

**Yöntem (değişmedi):** imza kağıda → 3-5 Türkçe cümle → bir örnekte elle durum çiz → `w2d2/Algo1.java`, `Algo2.java` içinde kendi testlerinle yaz → LeetCode'a gönder. 20 dk ilerleme yoksa dur, cümlelerini bana göster.

**A1 — [LeetCode 1189: Maximum Number of Balloons](https://leetcode.com/problems/maximum-number-of-balloons/)** — sayaç dizisi, bir adım ileri
`maxNumberOfBalloons(String text)` → int: `text`'teki harflerle (her biri bir kez) en fazla kaç tane `"balloon"` kelimesi yazılabilir? `"nlaebolko"` → 1, `"loonbalxballpoon"` → 2, `"leetcode"` → 0.
İpucu: Saymayı biliyorsun. Yeni olan kısım: `"balloon"`'da bazı harfler **iki kez** geçiyor. Her harf için "bu harf kaç balloon'a yeter?" diye düşün; cevabı hangi harf belirler?

**A2 — [LeetCode 344: Reverse String](https://leetcode.com/problems/reverse-string/)** — iki pointer'a giriş
`reverseString(char[] s)` → void: diziyi **yerinde** (yeni dizi oluşturmadan) ters çevir. `['h','e','l','l','o']` → `['o','l','l','e','h']`.
İpucu: Biri baştan, biri sondan başlayan iki index. Her adımda ne yaparlar, ne zaman dururlar? **Döngü koşulunu kağıtta 4 ve 5 elemanlı iki dizi için izle** (off-by-one listende).
Not: Burada dizinin içini değiştirmek **istenen şey** — Exam1'deki "girdiyi bozma" hatasıyla karıştırma; fark, soru bunu istiyor mu istemiyor mu.

---

## 🔧 GIT (10 dk)

```bash
git add hafta-02/gun-02
git status            # sadece .java ve DERS.md (.class yok)
git commit -m "feat: week 2 day 2 - encapsulation and static"
git push
```
Sonra `ILERLEME.md` Gün 7 logu → commit + push (`docs: day 7 progress log`). **"Ne takıldı" alanını `-` bırakma** — dün A1'in ikinci döngüsünde takıldın ama log `-` dedi. Takıldığın şeyi yazmak, haftalık tekrarda neye bakacağını belirler.

---

## ✅ Gün Sonu

- [ ] Isınma soruları cevaplandı
- [ ] E0 package `w2d2`
- [ ] E1–E5 tamam (E4 tahmin tablosu kağıtta, **çalıştırmadan önce**)
- [ ] A1–A2 (önce kağıtta 3-5 cümle)
- [ ] "Kağıda Yaz" kutusu kağıtta
- [ ] Commit + push, `ILERLEME.md` Gün 7 logu (ve commit'i!)

**Yarın oturum başında soracaklarım:**
1. Encapsulation nedir, ne işe yarar? Dünkü `-100000` örneğiyle anlat.
2. Her field'a getter ve setter yazmak neden otomatik olarak encapsulation sayılmaz?
3. `private` field'a aynı class'ın **başka bir nesnesinden** erişilebilir mi?
4. Static metot içinden instance field'a neden erişilemez?
5. `final` field ne zaman atanabilir?

**Yarın (Hafta 2, Gün 3):** Inheritance (`extends`, `super`, method overriding, `protected`) — `BankAccount`'tan `SavingsAccount` türeteceğiz.
