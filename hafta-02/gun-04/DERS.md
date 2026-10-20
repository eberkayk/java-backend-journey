# Hafta 2 · Gün 4 (Gün 9) — Polymorphism (üst tip referans, dinamik metot seçimi, `instanceof`, cast)

**Hedef:** `BankAccount acc = new CheckingAccount(...)` satırında **hangi metotların çağrılabileceğine** derleyicinin, **hangi versiyonun çalışacağına** JVM'in karar verdiğini açıklayabilmek; farklı türleri tek bir dizi/metotla işlemek; downcast'i güvenli yapmak. Dünkü "transfer neden CheckingAccount'ın withdraw'ını çağırdı?" sorusunun tam cevabı bugün. Algoritma saatinde: dünkü yavaş/hızlı pointer'ın tekrarı + sayma dizisinin tekrarı.

**Süre:** ~6 saat · Isınma 20 dk · Ders 1.5 sa · Egzersiz 2.5 sa · Algoritma 1 sa · Kapanış 20 dk

**Kurallar (değişmedi):**
- Sınıf, metot, değişken ve ekran mesajları **İngilizce**
- **Değişiklik → derle → çalıştır → çıktının her satırını oku.**
- **Metot adı/imzası görevde nasılsa öyle.**
- Hesaplayan metot yazdırmaz, **return** eder.
- ⚠️ Dünün dersi: görevde "birden fazla test / ilk k elemanı yazdır" diyorsa **onu da** yap — LeetCode'un kontrol ettiği şey o.

---

## 0. Isınma (20 dk) — notlara bakmadan (Gün 8 soruları)

1. Inheritance ne zaman kullanılır? IS-A / HAS-A farkını bir örnekle anlat.
2. Alt class, üst class'ın `private` field'ına erişebilir mi? O field alt class nesnesinde var mı?
3. `super(...)` ne zaman ve nerede yazılır? Yazmazsan ne olur?
4. Overloading ile overriding arasındaki fark?
5. `@Override` yazmasan da kod derlenir; neden yine de yazarsın?
6. (Dünden kalan) `BankAccount` içinde `boolean isRicherThan(BankAccount other) { return this.balance > other.balance; }` derlenir mi? `other.balance` `private` — sorun var mı?
7. (Dünden kalan) Metot içinde `int x; System.out.println(x);` neden derlenmez? (İpucu: aynı satırı **field** için yazınca derleniyor — fark ne?)

---

## 1. Sorun: her hesap türü için ayrı metot mu? ⭐ (10 dk)

Banka ay sonunda bütün hesapların toplam bakiyesini istiyor. Elinde `BankAccount`, `SavingsAccount`, `CheckingAccount` var. Kötü çözüm:

```java
static double total(BankAccount[] a, SavingsAccount[] s, CheckingAccount[] c) { ... }   // ❌ 4. tür gelince imza değişir
```

İyi çözüm: **hepsi bir `BankAccount`'tır** (IS-A) → hepsi tek bir `BankAccount[]` dizisine girer:

```java
BankAccount[] accounts = {
    new BankAccount("Ali"),
    new SavingsAccount("Ayşe", 0.05),
    new CheckingAccount("Can")
};
```

> **Polymorphism (çok biçimlilik):** Bir üst tip referansın, farklı alt tip nesneleri gösterebilmesi ve aynı metot çağrısının **nesnenin gerçek türüne göre** farklı davranması.

Aslında bunu dün **farkında olmadan** kullandın: `transfer(BankAccount to, double amount)` parametresine `SavingsAccount` da `CheckingAccount` da verebiliyordun.

---

## 2. İki tip: referansın tipi ve nesnenin tipi ⭐⭐⭐ (25 dk)

```java
BankAccount acc = new CheckingAccount("Can");
//  ↑ referans tipi (static/declared type)   ↑ nesne tipi (runtime/actual type)
```

Bu satır derlenir çünkü "CheckingAccount **bir** BankAccount'tır". Tersi derlenmez:
```java
CheckingAccount c = new BankAccount("Ali");   // ❌ incompatible types — her hesap checking değildir
```

**Altın kural (mülakatın kalbi):**

| Soru                                 | Kim karar verir?            | Neye bakar?           |
| ------------------------------------ | --------------------------- | --------------------- |
| Bu metodu **çağırabilir miyim**?     | **Derleyici** (derleme anı) | **Referansın** tipine |
| Metodun **hangi versiyonu** çalışır? | **JVM** (çalışma anı)       | **Nesnenin** tipine   |

```java
BankAccount acc = new SavingsAccount("Ayşe", 0.05);
acc.deposit(1000);      // ✅ BankAccount'ta deposit var
acc.addInterest();      // ❌ cannot find symbol — derleyici "acc bir BankAccount" diye bakar, BankAccount'ta addInterest yok
                        //    (nesne gerçekte SavingsAccount olsa bile!)

BankAccount acc2 = new CheckingAccount("Can");
acc2.deposit(100);
acc2.withdraw(50);      // derleyici: BankAccount'ta withdraw var ✅
                        // JVM: nesne CheckingAccount → CheckingAccount.withdraw çalışır → 51 düşer
System.out.println(acc2.getBalance());   // 49.0
```

Derleyici **güvenli tarafta** kalır: `acc` değişkenine programın başka bir yerinde `new BankAccount(...)` atanmış olabilir; o zaman `addInterest()` çağrısı patlardı. Derleyici sadece **kesin olarak var olan** metotlara izin verir.

### Dünün cevabı: dynamic dispatch (dinamik metot seçimi)

```java
// BankAccount.transfer içinde:
if (this.withdraw(amount)) { ... }
```
`checking.transfer(savings, 100)` çağrıldığında `this` = **CheckingAccount nesnesi**. `this.withdraw(...)` yazısı `BankAccount.java` dosyasında olsa da JVM "bu nesnenin gerçek türü ne?" diye bakar → `CheckingAccount` → onun `withdraw`'ı çalışır → ücret kesilir.

> Override edilmiş bir metodun hangi versiyonunun çalışacağı, **kodun hangi dosyada yazıldığına göre değil, nesnenin gerçek türüne göre** çalışma anında seçilir.

---

## 3. Polimorfik dizi ve parametre ⭐⭐ (15 dk)

```java
for (BankAccount acc : accounts) {
    acc.withdraw(10);     // her nesne kendi withdraw'ını çalıştırır
}
```
Bu döngü yazıldığında `CheckingAccount` diye bir şeyi **bilmesine gerek yok**. Yarın `StudentAccount` eklersen bu döngü **hiç değişmeden** onu da doğru işler. Bu, polymorphism'in asıl faydası: **yeni tür eklemek eski kodu değiştirmeyi gerektirmez.**

Aynısı parametre ve dönüş tipi için:
```java
static double totalBalance(BankAccount[] accounts) { ... }    // her türü kabul eder
static BankAccount richest(BankAccount[] accounts) { ... }    // her türü döndürebilir
```

---

## 4. Downcast ve `instanceof` ⭐⭐⭐ (20 dk)

Bazen gerçekten alt class'a özel bir metot lazım (ay sonu: sadece vadeli hesaplara faiz):

```java
BankAccount acc = accounts[1];
acc.addInterest();                          // ❌ derlenmez (§2)
((SavingsAccount) acc).addInterest();       // ✅ derlenir — "derleyici, bana güven, bu bir SavingsAccount"
```

**Cast nesneyi değiştirmez**, sadece derleyiciye referansı başka tipten görmesini söyler. Yalan söylersen JVM yakalar:
```java
BankAccount acc = new CheckingAccount("Can");
SavingsAccount s = (SavingsAccount) acc;    // derlenir! ama çalışınca:
// Exception in thread "main" java.lang.ClassCastException: class w2d4.CheckingAccount cannot be cast to class w2d4.SavingsAccount
```

**Güvenli yol — önce sor:** `instanceof`
```java
if (acc instanceof SavingsAccount) {
    SavingsAccount s = (SavingsAccount) acc;
    s.addInterest();
}
```
**Modern Java (16+, pattern matching)** — kontrol + cast + değişken tek satırda:
```java
if (acc instanceof SavingsAccount s) {
    s.addInterest();     // s sadece bu if bloğunun içinde var
}
```

| Cast yönü                | Örnek                      | Yazmak gerekir mi? | Patlayabilir mi?            |
| ------------------------ | -------------------------- | ------------------ | --------------------------- |
| **Upcast** (alt → üst)   | `BankAccount b = savings;` | Hayır, otomatik    | Hayır                       |
| **Downcast** (üst → alt) | `(SavingsAccount) b`       | Evet, açıkça       | Evet → `ClassCastException` |

⚠️ **Tasarım uyarısı:** Kodunda `if (x instanceof A) ... else if (x instanceof B) ... else if (x instanceof C)` zinciri görürsen, genelde **override etmen gereken bir metot** eksiktir. Her tür kendi davranışını kendi metodunda taşımalı; dışarıdan türünü sorup ona göre davranmak polymorphism'in tersidir. `instanceof` istisna olmalı, kural değil.

---

## 5. Polimorfik OLMAYAN şeyler ⭐⭐ (10 dk)

Dinamik seçim **sadece override edilmiş instance metotlar** için geçerli. Şunlar derleme anında, **referans tipine** göre seçilir:

- **Overload seçimi:** Aynı isimli `print(BankAccount a)` ve `print(SavingsAccount s)` varsa, `print(acc)` çağrısında hangisinin seçileceğine derleyici `acc`'nin **referans tipine** bakarak karar verir (nesneye değil). → E4'te göreceksin.
- **`static` metotlar:** override edilmez (alt class'ta aynı imzayla yazılırsa üsttekini sadece "gizler").
- **Field'lar:** override edilmez. (Zaten `private` tutuyoruz, sorun yaşamazsın.)

**Mülakat cümlesi:** "Java'da iki tür polymorphism var: **compile-time** (overloading — derleyici parametre tiplerine bakıp seçer) ve **runtime** (overriding — JVM nesnenin gerçek tipine bakıp seçer). Genelde 'polymorphism' denince runtime olan kastedilir."

---

## 📝 KAĞIDA YAZ (sadece bunlar)

1. `Üst ref = new Alt(...)` ✅ (Alt bir Üst'tür). Tersi ❌.
2. **Çağırabilir miyim?** → derleyici, **referans** tipine bakar.
3. **Hangisi çalışır?** → JVM, **nesne** tipine bakar (override). = dynamic dispatch.
4. Upcast otomatik ve güvenli. Downcast `(Alt) ref` açık yazılır, yanlışsa `ClassCastException`.
5. Downcast'ten önce `if (ref instanceof Alt a)`. Ama `instanceof` zinciri = eksik override.
6. Overload ve `static` → derleme anı (referans tipi). Override → çalışma anı (nesne tipi).

---

## 💻 EGZERSİZLER (AI yok, IntelliJ'de AI kapalı)

### E0 — Kurulum (5 dk)
`hafta-02/gun-04/` → **Sources Root** → package **`w2d4`**.
Dünkü `w2d3`'ten `BankAccount`, `SavingsAccount`, `CheckingAccount`'ı **kopyala**, `package w2d4;` yap. Alt class'ların constructor'ları ve `SavingsAccount`'ın `getInterestRate`/`addInterest` metotları dün package-private kalmıştı → **`public`** yap.

### E1 — Derlenir mi, ne yazar? ⭐⭐⭐ (kağıt önce, sonra kod)
Her satır için **önce kağıda**: (a) derlenir mi? (b) derleniyorsa ne yazar / bakiye ne olur? Bir cümle sebep (§2 tablosu). **Sonra** `w2d4/TypeDemo.java`'ya yaz, derlenmeyenleri tek tek dene, hata mesajını kağıdına ekle, sonra yoruma al.

```java
BankAccount a = new CheckingAccount("Can");
a.deposit(100);
a.withdraw(20);
System.out.println(a.getBalance());              // 1

BankAccount b = new SavingsAccount("Ayşe", 0.10);
b.deposit(1000);
b.addInterest();                                 // 2
System.out.println(b.getInterestRate());         // 3

SavingsAccount c = new BankAccount("Ali");       // 4
CheckingAccount d = new CheckingAccount("Deniz");
BankAccount e = d;                               // 5
System.out.println(e == d);                      // 6
```

Sonra 2 ve 3'ü **cast ile** düzelt (dikkat: `((SavingsAccount) b).addInterest()` — parantezler neden iki kat?) ve `b`'nin bakiyesini yazdır. Beklenen değeri önce kağıda yaz.

### E2 — Polimorfik dizi: ay sonu ⭐⭐⭐
`w2d4/Bank.java` — `main` dışında **static** metotlar:

- `static double totalBalance(BankAccount[] accounts)` → toplam bakiye.
- `static BankAccount richest(BankAccount[] accounts)` → en yüksek bakiyeli hesap. (Başlangıç değeri: Gün 4 dersini hatırla — `-1` değil.)
- `static void monthEnd(BankAccount[] accounts)` → **sadece** `SavingsAccount` olanlara `addInterest()` uygular (pattern matching `instanceof` ile).

`main`'de: 2 normal, 2 vadeli (farklı faiz), 2 vadesiz hesaplık **tek** bir `BankAccount[]`. Her birine farklı miktar yatır. Sonra **aynı döngüde** hepsinden `withdraw(100)`.
- Her hesabın sahibini ve bakiyesini yazdır → vadesizlerde 101 düştüğünü **çıktıdan** göster.
- `totalBalance` → `monthEnd` → tekrar `totalBalance`. Farkı **elle hesapla**, çıktıyla karşılaştır.
- `richest` sonucunun sahibini yazdır.
- Soru (kağıda, 1-2 cümle): `withdraw(100)` döngüsünü yazarken `instanceof` kullandın mı? `monthEnd`'de neden kullanmak zorunda kaldın? Fark ne?

### E3 — Şekiller ⭐⭐
`w2d4/Shape.java`, `Circle.java`, `Rectangle.java`, `Triangle.java`:
- `Shape`: `public double area()` → `0` döndürür. `public String getName()` → `"Shape"`.
- `Circle(double radius)`, `Rectangle(double width, double height)`, `Triangle(double base, double height)` → hepsi `extends Shape`, `area()` ve `getName()`'i **override** eder. Field'lar `private final`.
- Her birinde `toString()` override: ör. `Circle[r=2.0, area=12.57]` (yuvarlamak için `String.format("%.2f", x)`).

`w2d4/ShapeDemo.java`:
- 5 elemanlı `Shape[]` (en az 1'er tane her tür), hepsini **direkt `println`** ile yazdır.
- `static Shape largest(Shape[] shapes)` ve `static double totalArea(Shape[] shapes)`.
- **Tahmin sorusu — bilmemen normal (yarının konusu):** `new Shape()` yazıp `area()`'sını çağırmak **mantıklı mı**? "Şekil" diye bir nesnenin alanı 0 mı? Bir alt class `area()`'yı override etmeyi **unutursa** ne olur — derleyici seni uyarır mı? Kağıda 2-3 cümle. (İpucu: Gün 5'te `abstract`.)

### E4 — Overload mu, override mı? Çıktı tahmini ⭐⭐⭐ (kağıt — çalıştırmadan)
Aşağıdaki kodun **tam çıktısını** kağıda yaz, her satırın yanına "override → nesne tipi" veya "overload → referans tipi" diye not düş. Sonra `w2d4/DispatchDemo.java`'ya yazıp çalıştır, farkları işaretle.

```java
class Animal {
    public String sound() { return "..."; }
}
class Dog extends Animal {
    @Override
    public String sound() { return "Woof"; }
}
class Cat extends Animal {
    @Override
    public String sound() { return "Meow"; }
}

public class DispatchDemo {
    static String describe(Animal a) { return "animal says " + a.sound(); }
    static String describe(Dog d)    { return "dog says " + d.sound(); }

    public static void main(String[] args) {
        Animal a1 = new Dog();
        Dog d1 = new Dog();
        Animal a2 = new Cat();

        System.out.println(a1.sound());
        System.out.println(describe(a1));
        System.out.println(describe(d1));
        System.out.println(describe(a2));
        System.out.println(describe((Dog) a1));
        System.out.println(describe(new Animal()));
    }
}
```
(Bir dosyada birden fazla class: sadece **biri** `public` olabilir ve dosya adı onunla aynı olmalı. Bu egzersiz için böyle bırak.)

Son soru: `describe((Dog) a2)` satırını eklersen **derlenir mi**? Çalışınca ne olur? Önce tahmin, sonra dene, stack trace'in **ilk iki satırını** kağıda yaz.

### E5 — Mini tasarım: `instanceof` zinciri ⭐ (kağıt, 10 dk)
Bir arkadaşın şunu yazmış:
```java
static double fee(BankAccount acc) {
    if (acc instanceof CheckingAccount) return 1.0;
    else if (acc instanceof SavingsAccount) return 0.0;
    else return 0.5;
}
```
Yarın `StudentAccount` eklenince ne olur? Bunu polymorphism ile nasıl düzeltirdin — **hangi class'a hangi metot** eklenir, `fee` metodu neye dönüşür? Kod yazma, 3-4 cümle + metot imzaları.

---

## 🧩 ALGORİTMA SAATİ (1 sa)

**Yöntem (değişmedi):** imza kağıda → 3-5 Türkçe cümle → bir örnekte elle durum çiz → `w2d4/Algo1.java`, `Algo2.java` içinde kendi testlerinle yaz → LeetCode'a gönder. 20 dk ilerleme yoksa dur, cümlelerini bana göster.

**A1 — [LeetCode 283: Move Zeroes](https://leetcode.com/problems/move-zeroes/)** — yavaş/hızlı pointer (dünkü A2'nin kardeşi)
`void moveZeroes(int[] nums)`: bütün `0`'ları sona taşı, sıfır olmayanların **sırası bozulmasın**, **yerinde** (yeni dizi yok). `[0,1,0,3,12]` → `[1,3,12,0,0]`. `[0]` → `[0]`.
- Dünkü kalıp: hızlı okur, yavaş "sıfır olmayan bir sonraki eleman nereye yazılacak"ı gösterir. Yeni soru: döngü bitince yavaş pointer'dan sonrası ne olmalı?
- **Kağıtta** `[0,1,0,3,12]` için her adımda iki pointer'ı ve dizinin durumunu tablo olarak çiz.
- Metot `void` → test ederken **dizinin tamamını** yazdır (`Arrays.toString(nums)`). En az 3 test: örnek, `[0]`, hiç sıfır olmayan bir dizi (dünkü bug'ı hatırla).

**A2 — [LeetCode 242: Valid Anagram](https://leetcode.com/problems/valid-anagram/)** — sayma dizisi (Gün 6 A2'nin kardeşi)
`boolean isAnagram(String s, String t)`: `t`, `s`'nin harflerinin yer değiştirilmiş hâli mi? `"anagram"`, `"nagaram"` → true. `"rat"`, `"car"` → false. Sadece küçük harf.
- Önce: uzunluklar farklıysa? (Bir satır.)
- Bir tane `int[26]` ile çözebilir misin (iki tane değil)? İpucu: biri artırır, öbürü azaltır — sonunda dizide ne görmeyi beklersin?
- En az 3 test, biri farklı uzunluklu.

---

## 🔧 GIT (10 dk)

```bash
git add hafta-02/gun-04
git status            # sadece .java ve DERS.md (.class yok)
git commit -m "feat: week 2 day 4 - polymorphism"
git push
```
Sonra `ILERLEME.md` Gün 9 logu → commit + push (`docs: day 9 progress log`).
⚠️ "Ne öğrendim (3 madde)" → **3 madde**, tek kelime değil. Ör: "referans tipi neyi çağırabileceğimi, nesne tipi hangisinin çalışacağını belirler".

---

## ✅ Gün Sonu

- [ ] Isınma soruları cevaplandı (6 ve 7 dahil)
- [ ] E0 package `w2d4`
- [ ] E1–E5 tamam (E1 ve E4 tahmin **çalıştırmadan önce** kağıtta)
- [ ] A1–A2 (önce kağıtta 3-5 cümle, A1 tablo)
- [ ] "Kağıda Yaz" kutusu kağıtta
- [ ] Commit + push, `ILERLEME.md` Gün 9 logu (3 madde!) ve commit'i

**Yarın oturum başında soracaklarım:**
1. `BankAccount acc = new SavingsAccount(...)` — `acc.addInterest()` neden derlenmez, nesne vadeli hesap olduğu hâlde?
2. Dynamic dispatch nedir? Dünkü `transfer` örneğiyle anlat.
3. Upcast ve downcast farkı; hangisi `ClassCastException` atabilir?
4. Overload seçimi ne zaman yapılır, override seçimi ne zaman?
5. Kodda uzun bir `instanceof` zinciri neyin işaretidir?

**Yarın (Hafta 2, Gün 5):** Abstract class ve interface — `new Shape()` yasaklamak, alt class'ı `area()`'yı yazmaya **zorlamak**, bir class'ın birden fazla "tür" olabilmesi. Bugünkü E3 tahmin sorusu oradan geliyor.
