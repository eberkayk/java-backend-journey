# Hafta 2 · Gün 3 (Gün 8) — Inheritance (`extends`, `super`, overriding, `protected`)

**Hedef:** Var olan bir class'ı **kopyalamadan** genişletmek; alt class'ın üst class'tan neyi aldığını, neyi alamadığını ve neyi **değiştirebildiğini** (override) açıklayabilmek; constructor zincirini kağıtta izleyebilmek. Algoritma saatinde iki pointer'ın iki farklı şekli: **iki uçtan** (palindrome) ve **yavaş/hızlı** (yerinde silme).

**Süre:** ~6 saat · Isınma 20 dk · Ders 1.5 sa · Egzersiz 2.5 sa · Algoritma 1 sa · Kapanış 20 dk

**Kurallar (değişmedi):**
- Sınıf, metot, değişken ve ekran mesajları **İngilizce**
- **Değişiklik → derle → çalıştır → çıktının her satırını oku.**
- **Metot adı/imzası görevde nasılsa öyle.**
- Hesaplayan metot yazdırmaz, **return** eder.
- ⚠️ Dünün dersi: `if (account.withdraw(x))` **sadece kontrol değil, işlemi de yapar.** Koşulun içindeki metot çağrısı **çalışır** ve nesneyi değiştirir.

---

## 0. Isınma (20 dk) — notlara bakmadan (Gün 7 soruları)

1. Encapsulation nedir, ne işe yarar? Dünkü `-100000` örneğiyle anlat.
2. Her field'a getter ve setter yazmak neden otomatik olarak encapsulation sayılmaz?
3. `private` field'a aynı class'ın **başka bir nesnesinden** erişilebilir mi?
4. Static metot içinden instance field'a neden erişilemez?
5. `final` field ne zaman atanabilir?
6. (Dünden kalan) `int x;` **field** ise ve yazdırırsan ne olur? **Metot içinde local** ise ve yazdırırsan ne olur?

---

## 1. Sorun: kopyala-yapıştır ⭐ (10 dk)

Bankaya "vadeli hesap" (`SavingsAccount`) eklenecek: normal hesabın yaptığı her şeyi yapıyor (`deposit`, `withdraw`, `getBalance`, `transfer`) **artı** faiz oranı var ve `addInterest()` metodu var.

Kötü çözüm: `BankAccount.java`'yı kopyala, adını değiştir, faizi ekle. Yarın `withdraw`'da bir bug bulursan **iki yerde** düzeltmen gerekir. Üç hesap türü olursa üç yerde.

> **Inheritance (kalıtım):** Bir class'ın (alt class / subclass / child) başka bir class'ın (üst class / superclass / parent) field ve metotlarını **devralıp** üzerine kendi şeylerini eklemesi.

```java
public class SavingsAccount extends BankAccount {
    private final double interestRate;
    ...
    public void addInterest() { ... }
}
```
`SavingsAccount` nesnesinde `deposit`, `withdraw`, `getBalance` **yazmadan** vardır.

**IS-A testi (mülakat):** Inheritance sadece "**X bir Y'dir**" cümlesi doğruysa kullanılır.
- "Vadeli hesap **bir** banka hesabıdır" ✅ → `extends` mantıklı
- "Araba **bir** motordur" ❌ → araba bir motora **sahiptir** (HAS-A) → field olur: `private Engine engine;`

---

## 2. Neler devralınır, neler devralınmaz? ⭐⭐ (15 dk)

|                         | Alt class'ta var mı?              | Alt class'ın kodu doğrudan kullanabilir mi?  |
| ----------------------- | --------------------------------- | -------------------------------------------- |
| `public` metot/field    | Var                               | Evet                                         |
| `protected` metot/field | Var                               | Evet                                         |
| package-private         | Var                               | Sadece aynı package'taysa                    |
| `private` field         | **Var** (nesnenin içinde duruyor) | **Hayır** — sadece üst class'ın metotlarıyla |
| Constructor             | **Devralınmaz**                   | `super(...)` ile çağrılır                    |

En önemli satır `private`: `SavingsAccount` nesnesinin içinde bir `balance` **var**, ama `SavingsAccount`'ın kodu `balance += ...` **yazamaz**:
```java
public void addInterest() {
    balance += balance * interestRate;           // ❌ balance has private access in BankAccount
    deposit(getBalance() * interestRate);        // ✅ üst class'ın kapısından geç
}
```
Bu bir kusur değil, **encapsulation'ın devam etmesi**: `BankAccount`'ın kuralları (negatif para yatırılamaz vs.) alt class için de geçerli kalır.

**Java'da tek kalıtım:** Bir class sadece **bir** class'ı `extends` edebilir. (`class A extends B, C` ❌ — birden fazla "tür" için Gün 5'te `interface` gelecek.)

**Her şeyin atası `Object`:** `extends` yazmazsan Java gizlice `extends Object` yazar. Dün `Product@2a84aee7` yazdıran `toString()` metodu `Object`'ten geliyordu.

---

## 3. Constructor zinciri ve `super(...)` ⭐⭐⭐ (20 dk)

Alt class nesnesi oluşturulurken **önce üst class'ın parçası** kurulur. Bu yüzden alt class constructor'ının **ilk satırı** üst class constructor'ını çağırır:

```java
public SavingsAccount(String owner, double interestRate) {
    super(owner);                     // BankAccount(String owner) çalışır → owner atanır
    this.interestRate = interestRate; // sonra kendi alanı
}
```

**Kurallar:**
1. Java 24'e kadar `super(...)` **ilk satır** olmak zorundaydı. **Java 25'ten itibaren** (JEP 513, "flexible constructor bodies") `super(...)`'dan önce kod yazılabilir: parametre kontrolü yapabilir ve **kendi** field'larına atama yapabilirsin. Ama orada nesnenin metotlarını çağıramaz, üst class'tan gelen bir şeye dokunamazsın (`cannot reference m() before supertype constructor has been called`). Mülakatta ve çoğu şirket kodunda (Java 17/21) eski kural geçerli. Alışkanlık olarak `super(...)`'ı ilk satıra yaz. (Ev: önce temel, sonra kat.)
2. Hiç `super(...)` yazmazsan Java gizlice **`super();`** (parametresiz) ekler.
3. Üst class'ta parametresiz constructor **yoksa** (dün `BankAccount(String owner)` yazdın → parametresiz olan artık yok, Gün 6 §constructor) → gizli `super();` derlenmez → hata. Bunu E1'de bilerek göreceksin.

**Zincir:** `C extends B extends A` → `new C()` → C'nin constructor'ı önce B'yi, B önce A'yı çağırır → gövdeler **A, B, C** sırasıyla biter. (E3'te kağıtta izleyeceksin.)

> `this` = bu nesne. `super` = bu nesnenin **üst class parçası**. `this(...)` aynı class'ın başka constructor'ını, `super(...)` üst class'ın constructor'ını çağırır.

---

## 4. Method overriding (ezme) ⭐⭐⭐ (25 dk)

Alt class, devraldığı bir metodun **davranışını değiştirmek** isteyebilir. Örnek: `CheckingAccount` (vadesiz hesap) her para çekişte 1.0 TL işlem ücreti kesiyor.

```java
public class CheckingAccount extends BankAccount {
    public static final double FEE = 1.0;

    public CheckingAccount(String owner) {
        super(owner);
    }

    @Override
    public boolean withdraw(double amount) {
        return super.withdraw(amount + FEE);   // üst class'ın withdraw'ını çağır, ücretle birlikte
    }
}
```

- **Override** = alt class'ta, üst class'taki metotla **aynı isim + aynı parametre listesi** (ve uyumlu dönüş tipi) olan metot yazmak. Artık `checking.withdraw(100)` → alt class'ınki çalışır.
- **`super.withdraw(...)`** = "benim ezdiğim, üst class'taki orijinal metodu çağır". Bunu yazmadan `withdraw(amount + FEE)` dersen **kendini** çağırırsın → sonsuz özyineleme → `StackOverflowError`.
- Dikkat: `amount <= 0` kontrolünü tekrar yazmadık — `super.withdraw` zaten yapıyor. (Dünkü `transfer`'da `withdraw`/`deposit` kullanmanla aynı fikir.)
- Override edilen metodun erişimi **daraltılamaz**: üstte `public` ise altta `private` yapamazsın.

### `@Override` neden yazılır?
Zorunlu değil, ama yazmamak tehlikeli:
```java
public boolean withdrow(double amount) { ... }      // yazım hatası → override DEĞİL, yeni metot!
public boolean withdraw(int amount) { ... }         // parametre tipi farklı → overload, override DEĞİL!
```
İkisi de **sessizce derlenir** ve senin metodun hiç çağrılmaz. `@Override` yazarsan derleyici "üst class'ta böyle bir metot yok" diye **hata verir**. → Her override'a `@Override` yaz.

### Overloading vs Overriding (klasik mülakat sorusu)
|                   | Overloading (Gün 3)         | Overriding (bugün)                   |
| ----------------- | --------------------------- | ------------------------------------ |
| Nerede?           | Aynı class (veya alt class) | Alt class                            |
| İsim              | Aynı                        | Aynı                                 |
| Parametreler      | **Farklı**                  | **Aynı**                             |
| Ne zaman seçilir? | Derleme anında              | Çalışma anında (yarın: polymorphism) |

### `toString()` override
```java
@Override
public String toString() {
    return "Product{name=" + name + ", price=" + price + "}";
}
```
Artık `System.out.println(product)` → `Product@2a84aee7` yerine okunabilir çıktı. (Dünkü `cheapest` hatanı bu gizlemişti.)

---

## 5. `protected` ve `final` (10 dk)

- **`protected`**: aynı package + alt class'lar erişebilir. Alt class'a `balance`'ı açmak için `protected double balance;` yapılabilir — **ama** o zaman her alt class `BankAccount`'ın kurallarını atlayabilir. Varsayılan tercih: field `private` kalır, alt class üst class'ın metotlarını kullanır. `protected` genelde **metotlar** için kullanılır.
- **`final` metot**: alt class override **edemez**. (`final` field = bir kez atanır; `final` metot = ezilemez; `final` class = extend edilemez — ör. `String` final'dır.)

---

## 📝 KAĞIDA YAZ (sadece bunlar)

1. `extends` = IS-A. "X bir Y'dir" doğru değilse inheritance değil, field (HAS-A).
2. `private` field alt class'ta **vardır** ama alt class kodu **erişemez** → üst class'ın metotlarını kullan.
3. Constructor devralınmaz. Alt constructor'ın **ilk satırı** `super(...)`; yazmazsan gizli `super()`.
4. Zincir: üstten alta kurulur (A → B → C).
5. Override = aynı imza, alt class'ta. Her zaman `@Override`. Orijinali çağırmak: `super.metot(...)`.
6. Overload = farklı parametre, derleme anında. Override = aynı parametre, çalışma anında.

---

## 💻 EGZERSİZLER (AI yok, IntelliJ'de AI kapalı)

### E0 — Kurulum (5 dk)
`hafta-02/gun-03/` → **Sources Root** → package **`w2d3`**.
Dünkü `w2d2/BankAccount.java`'yı bu package'a **kopyala** (bunu 2 kez elle yazdın, yeter). Değiştir: `package w2d3;` ve constructor + bütün metotlar **`public`**.

### E1 — `SavingsAccount` ⭐⭐
`w2d3/SavingsAccount.java` → `extends BankAccount`:
- `private final double interestRate` (ör. 0.05 = %5).
- Constructor `SavingsAccount(String owner, double interestRate)`.
- `getInterestRate()`.
- `void addInterest()` → bakiyeye faiz ekler. **`balance`'a doğrudan dokunma.**

**Deneyler (önce tahmin, sonra dene, sonra geri al):**
- (a) Constructor'dan `super(owner);` satırını sil. Hata **hangi satırda** ve mesaj ne diyor? Bir cümleyle neden. (§3 kural 2-3.)
- (b) `super(owner);`'ı `this.interestRate = ...` satırının **altına** taşı. Java 25'te derlenir (§3 kural 1). Sonra `super(owner);`'dan önce `getBalance();` yaz → hata ne?
- (c) `addInterest` içinde `balance += ...` yaz. Hata mesajını kağıda yaz.

`w2d3/AccountDemo.java`: 1000 TL yatır, `addInterest()`, bakiye yazdır (beklenen değeri yanına yaz). Ayrıca **hiç yazmadığın** `withdraw` ve `transfer`'ı `SavingsAccount` üzerinde çağırıp çalıştığını göster.

### E2 — `CheckingAccount`: override ⭐⭐⭐
`w2d3/CheckingAccount.java` → `extends BankAccount`:
- `public static final double FEE = 1.0;`
- `withdraw`'ı override et: çekilen tutar + `FEE` düşülür. Bakiye `amount + FEE`'ye yetmiyorsa `false`, **hiçbir şey değişmez**.
- `@Override` kullan.

`AccountDemo`'ya ekle (her satırda beklenen bakiye yanında yazsın):
1. 100 yatır → 50 çek → bakiye? 
2. Bakiye 49 iken 49 çek → sonuç ve bakiye?
3. `transfer` ile `CheckingAccount`'tan başka hesaba 10 aktar. **Önce tahmin:** ücret kesilir mi? Neden? (İpucu: `transfer` hangi `withdraw`'ı çağırıyor? Kendi `transfer` koduna bak.)

**Deneyler:**
- (a) `super.withdraw(amount + FEE)` yerine `withdraw(amount + FEE)` yaz, çalıştır. Hata ne? Bir cümle neden. Geri al.
- (b) `@Override`'ı bırak, metot adını `withdrow` yap. Hata ne? Sonra `@Override`'ı da sil → derleniyor mu? Test 1'in sonucu ne oldu? Neden tehlikeli? Geri al.

### E3 — Constructor zinciri tahmini ⭐⭐ (kağıt zorunlu, **önce çalıştırma**)
Kağıtta çıktıyı yaz. Sonra `w2d3/ChainDemo.java` içine (3 class aynı dosyada olabilir, sadece `ChainDemo` public) yaz, çalıştır, yanlışların yanına neden yaz.

```java
class Vehicle {
    Vehicle() {
        System.out.println("Vehicle()");
    }
    Vehicle(String name) {
        System.out.println("Vehicle(" + name + ")");
    }
}

class Car extends Vehicle {
    Car() {
        System.out.println("Car()");
    }
    Car(String name) {
        super(name);
        System.out.println("Car(" + name + ")");
    }
}

class SportsCar extends Car {
    SportsCar() {
        this("Ferrari");
        System.out.println("SportsCar()");
    }
    SportsCar(String name) {
        System.out.println("SportsCar(" + name + ")");
    }
}

public class ChainDemo {
    public static void main(String[] args) {
        new Car();
        System.out.println("---");
        new Car("Golf");
        System.out.println("---");
        new SportsCar();
    }
}
```
Soru: `Vehicle()` constructor'ını silersen hangi satır(lar) derlenmez? Neden?

### E4 — `Employee` / `Manager` ⭐⭐
`w2d3/Employee.java`:
- `private final String name`, `private final double baseSalary`.
- Constructor, getter'lar.
- `public double getSalary()` → `baseSalary` döner.
- `toString()` override → `Employee{name=..., salary=...}` (maaş için `getSalary()` kullan).

`w2d3/Manager.java` → `extends Employee`:
- `private double bonus` + kontrollü setter `boolean setBonus(double bonus)` (negatif → false).
- Constructor `Manager(String name, double baseSalary, double bonus)` (geçersiz bonus → 0.0; dünkü setter-in-constructor kalıbı).
- `getSalary()` override → taban maaş + bonus. **`baseSalary`'ye doğrudan erişme.**
- `toString()` override → `Manager{name=..., salary=..., bonus=...}`.

`w2d3/EmployeeDemo.java`: Bir `Employee`, bir `Manager` oluştur, ikisini **direkt** `println` ile yazdır. Sonra:
- **Tahmin sorusu — bilmemen normal (yarının konusu):** `Employee e = new Manager("Ayşe", 5000, 1000);` derlenir mi? Derlenirse `e.getSalary()` kaç döner — 5000 mü 6000 mi? Önce kağıda tahmin + bir cümle sebep, sonra çalıştır. (İpucu: IS-A testi + "override çalışma anında seçilir".)

### E5 — Mini tasarım: IS-A mı HAS-A mı? ⭐ (kağıt, kod yok — 10 dk)
Her biri için `extends` mi, field mı? Bir cümle sebep.
1. `Square` / `Rectangle`
2. `Library` / `Book`
3. `Student` / `Person`
4. `Car` / `Wheel`
5. `Stack` / `ArrayList` (tuzaklı — düşün: stack'e ortadan eleman eklenebilir mi?)

---

## 🧩 ALGORİTMA SAATİ (1 sa)

**Yöntem (değişmedi):** imza kağıda → 3-5 Türkçe cümle → bir örnekte elle durum çiz → `w2d3/Algo1.java`, `Algo2.java` içinde kendi testlerinle yaz → LeetCode'a gönder. 20 dk ilerleme yoksa dur, cümlelerini bana göster.

**A1 — [LeetCode 125: Valid Palindrome](https://leetcode.com/problems/valid-palindrome/)** — iki uçtan iki pointer
`isPalindrome(String s)` → boolean. Harf ve rakam dışındakiler yok sayılır, büyük/küçük harf farkı yok. `"A man, a plan, a canal: Panama"` → true, `"race a car"` → false, `" "` → true.
- Yeni araçlar: `Character.isLetterOrDigit(c)`, `Character.toLowerCase(c)`.
- Dünkü A2'nin (reverse string) iskeleti + yeni durum: pointer **geçersiz karaktere** denk gelirse ne yapar? Bu "atlama" sırasında pointer'lar birbirini geçebilir mi?
- ⚠️ Yeni String/dizi oluşturmadan çöz (önce temizleyip sonra kontrol etmek de doğru ama burada iki pointer pratiği istiyoruz).

**A2 — [LeetCode 26: Remove Duplicates from Sorted Array](https://leetcode.com/problems/remove-duplicates-from-sorted-array/)** — yavaş/hızlı iki pointer
`removeDuplicates(int[] nums)` → int: **sıralı** dizideki tekrarları **yerinde** sil; ilk `k` eleman benzersiz elemanlar olsun, `k`'yı döndür. `[1,1,2]` → 2, dizi `[1,2,_]`. `[0,0,1,1,1,2,2,3,3,4]` → 5, `[0,1,2,3,4,_,...]`.
- Yeni kalıp: iki pointer **aynı yönde** gider. Biri (hızlı) diziyi okur, diğeri (yavaş) "bir sonraki benzersiz eleman nereye yazılacak"ı gösterir.
- **Kağıtta** `[1,1,2]` ve `[0,0,1,1,1,2,2,3,3,4]` için her adımda iki pointer'ın değerini ve dizinin durumunu tablo olarak çiz — sonra kod.
- Not: "sıralı" bilgisi neden önemli? Bir cümle.

---

## 🔧 GIT (10 dk)

```bash
git add hafta-02/gun-03
git status            # sadece .java ve DERS.md (.class yok)
git commit -m "feat: week 2 day 3 - inheritance"
git push
```
Sonra `ILERLEME.md` Gün 8 logu → commit + push (`docs: day 8 progress log`).

---

## ✅ Gün Sonu

- [ ] Isınma soruları cevaplandı
- [ ] E0 package `w2d3`
- [ ] E1–E5 tamam (E3 tahmin **çalıştırmadan önce** kağıtta)
- [ ] A1–A2 (önce kağıtta 3-5 cümle, A2 tablo)
- [ ] "Kağıda Yaz" kutusu kağıtta
- [ ] Commit + push, `ILERLEME.md` Gün 8 logu (ve commit'i!)

**Yarın oturum başında soracaklarım:**
1. Inheritance ne zaman kullanılır? IS-A / HAS-A farkını bir örnekle anlat.
2. Alt class, üst class'ın `private` field'ına erişebilir mi? O field alt class nesnesinde var mı?
3. `super(...)` ne zaman ve nerede yazılır? Yazmazsan ne olur?
4. Overloading ile overriding arasındaki fark?
5. `@Override` yazmasan da kod derlenir; neden yine de yazarsın?

**Yarın (Hafta 2, Gün 4):** Polymorphism — `BankAccount acc = new CheckingAccount(...)`, `BankAccount[]` içinde farklı hesap türleri, dinamik metot seçimi. Bugünkü E4 tahmin sorusu oradan geliyor.
