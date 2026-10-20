# Hafta 2 · Gün 5 (Gün 10) — Abstract class ve Interface

**Hedef:** `new Shape()` yazmayı **yasaklamak**, alt class'ı `area()`'yı yazmaya **derleyiciyle zorlamak**; birbiriyle akraba olmayan class'ları ortak bir "sözleşme" (interface) altında toplamak; "abstract class mı, interface mi?" sorusuna mülakatta net cevap verebilmek. Dünkü E3 tahmin sorusunun ve E5 `fee()` tasarımının cevabı bugün. Algoritma saatinde: iki uçtan pointer + sayma dizisi, bu sefer **test tasarımına** özel dikkat.

**Süre:** ~6 saat · Isınma 20 dk · Ders 1.5 sa · Egzersiz 2.5 sa · Algoritma 1 sa · Kapanış 20 dk

**Kurallar (değişmedi):**
- Sınıf, metot, değişken ve ekran mesajları **İngilizce**
- **Değişiklik → derle → çalıştır → çıktının her satırını oku.**
- **Metot adı/imzası görevde nasılsa öyle.**
- Hesaplayan metot yazdırmaz, **return** eder.
- ⚠️ Dünün dersi: **false testlerin her biri farklı bir sebepten false olsun.** Dün A2'deki bug, iki false testin ikisi de "uzunluk farklı" satırında bittiği için yakalanmadı — sayma kısmına hiç ulaşmadılar.

---

## 0. Isınma (20 dk) — notlara bakmadan (Gün 9 soruları)

1. `BankAccount acc = new SavingsAccount(...)` — `acc.addInterest()` neden derlenmez, nesne vadeli hesap olduğu hâlde?
2. Dynamic dispatch nedir? `transfer` örneğiyle anlat.
3. Upcast ve downcast farkı; hangisi `ClassCastException` atabilir?
4. Overload seçimi ne zaman yapılır, override seçimi ne zaman?
5. Kodda uzun bir `instanceof` zinciri neyin işaretidir?
6. (Dünden kalan) `SavingsAccount s = new SavingsAccount("Ayşe", 0.05);` — `s` nesnesinin içinde `BankAccount`'ın `private double balance` alanı **var mı**? `SavingsAccount`'ın kodu ona `this.balance` diye erişebilir mi? (İki ayrı soru, iki ayrı cevap.)

---

## 1. Sorun: anlamsız nesne, unutulabilen metot ⭐ (10 dk)

Dünkü `Shape`:
```java
public class Shape {
    public double area() { return 0; }   // "şekil"in alanı 0 mı? uydurma bir değer
}
```
İki problem:
1. `new Shape()` yazılabiliyor — ama "sadece şekil" diye bir şey yok; her şekil ya daire ya dikdörtgen ya üçgen.
2. Yarın `Hexagon extends Shape` yazan biri `area()`'yı override etmeyi **unutursa** derleyici sesini çıkarmaz, program her altıgenin alanını sessizce `0` hesaplar. Bug'ı ancak çıktıya bakarak bulursun (bulursan).

İstediğimiz: **(a)** `Shape` tek başına oluşturulamasın, **(b)** her alt class `area()`'yı yazmak **zorunda** olsun — unutursa **derlenmesin**.

---

## 2. `abstract` class ve `abstract` metot ⭐⭐⭐ (25 dk)

```java
public abstract class Shape {
    public abstract double area();        // gövde YOK — sadece imza + ;

    public String getName() { return "Shape"; }   // normal metot da olabilir
}
```

- **`abstract` metot:** gövdesi yok, `{ }` yerine `;`. "Bu metot var, ama nasıl çalışacağını alt class söyleyecek."
- **`abstract` class:** `new` ile oluşturulamaz. `new Shape()` → ❌ `Shape is abstract; cannot be instantiated`.
- İçinde **en az bir** abstract metot olan class **abstract olmak zorunda**. (Tersi şart değil: hiç abstract metodu olmayan abstract class da yazılabilir.)
- `extends Shape` diyen **somut** (abstract olmayan) bir class, bütün abstract metotları override etmek **zorunda**. Etmezse:
  `Hexagon is not abstract and does not override abstract method area() in Shape` → derlenmez. İşte istediğimiz (b).

**Abstract class hâlâ normal bir class'tır**, sadece yarım:
- Field'ı olabilir (`private final String name;`)
- **Constructor'ı olabilir** — `new Shape()` yazamazsın ama `Circle`'ın constructor'ı `super(...)` ile onu çağırır. (Gün 8: her nesne oluşurken üst class'ın constructor'ı çalışır.)
- Normal (gövdeli) metotları olabilir.
- Referans tipi olarak kullanılabilir: `Shape s = new Circle(2);` ✅ , `Shape[] shapes` ✅ — dünkü polymorphism aynen çalışır.

### Abstract metodu normal metottan çağırmak ⭐⭐

```java
public abstract class Shape {
    public abstract double area();
    public abstract String getName();

    @Override
    public String toString() {
        return getName() + "[area=" + String.format("%.2f", area()) + "]";
    }
}
```
`toString` `Shape` içinde yazılı, ama `area()` ve `getName()`'in **hangi versiyonu** çalışacak? → Dün öğrendin: nesnenin gerçek tipine göre (dynamic dispatch). Yani üst class **akışı** belirler, alt class'lar **parçaları** doldurur. Böylece `toString`'i dört kere kopyalamazsın.

> Mülakat cümlesi: "Abstract class, ortak durumu (field) ve ortak davranışı tek yerde tutan, ama bazı adımları alt class'lara bırakan, kendisinden nesne üretilemeyen class'tır."

---

## 3. Interface: "ne yapabilir" sözleşmesi ⭐⭐⭐ (25 dk)

Yeni sorun: muhasebe ay sonu **ödenecek her şeyi** toplamak istiyor: çalışan maaşları **ve** tedarikçi faturaları. `Employee` ile `Invoice` arasında IS-A ilişkisi yok — bir fatura çalışan değildir, çalışan fatura değildir. Ortak üst class uydurmak (`class Thing`) saçma olur. Ama ikisinin de **ortak bir yeteneği** var: "bana ne kadar ödeneceğini söyleyebilirim".

```java
public interface Payable {
    double getPaymentAmount();     // public abstract yazmasan da öyle sayılır
}
```
```java
public class Invoice implements Payable {
    ...
    @Override
    public double getPaymentAmount() { return quantity * pricePerItem; }
}
```

- `interface` = sadece **imzalar** (sözleşme). Gövde yok, field yok (constant hariç), constructor yok.
- `implements Payable` diyen somut class, bütün metotlarını yazmak **zorunda** (abstract'taki kuralın aynısı).
- Interface metotları otomatik **`public abstract`**. Uygularken `public` yazmayı unutursan: `attempting to assign weaker access privileges` → derlenmez.
- `new Payable()` ❌. Ama referans tipi olarak ✅:
  ```java
  Payable[] bills = { new Invoice(...), new Employee(...) };
  for (Payable p : bills) total += p.getPaymentAmount();   // dynamic dispatch, aynı mantık
  ```
- **Bir class birden fazla interface uygulayabilir**, ama sadece **bir** class'ı extend edebilir:
  ```java
  public class Employee extends Person implements Payable, Comparable<Employee> { ... }
  ```
- Interface'te yazdığın field otomatik `public static final` → constant'tır: `int MAX = 10;`

### Java 8+ ek: `default` metot (bilmen yeter, bugün az kullanacağız)
```java
public interface Payable {
    double getPaymentAmount();
    default boolean isLarge() { return getPaymentAmount() > 10_000; }   // gövdeli!
}
```
Interface'e **sonradan** metot eklemek, onu uygulayan bütün class'ları bozardı (hepsi yeni metodu yazmak zorunda kalırdı). `default` bunu çözer: gövdesi hazır gelir, isteyen override eder.

### Neden bu kadar önemli? (backend köprüsü)
Spring'de neredeyse her şey interface üzerinden konuşur: `UserRepository` bir interface'tir, sen sadece onu bilirsin; arkasında veritabanı mı, test için sahte (mock) bir liste mi olduğu seni ilgilendirmez. "Koda değil, sözleşmeye bağımlı ol" — bunu Hafta 5+ ta her gün göreceksin.

---

## 4. Abstract class mı, interface mi? ⭐⭐⭐ (15 dk) — mülakat sorusu

|                      | Abstract class                                   | Interface                                     |
| -------------------- | ------------------------------------------------ | --------------------------------------------- |
| İlişki               | **IS-A** (bir türdür) — `Circle` bir `Shape`'tir | **CAN-DO** (yapabilir) — `Invoice` ödenebilir |
| Kaç tane?            | `extends` → **bir** tane                         | `implements` → **birden fazla**               |
| Field (durum)        | Var (`private`, normal field)                    | Yok — sadece `public static final` constant   |
| Constructor          | Var (alt class `super(...)` ile çağırır)         | Yok                                           |
| Gövdeli metot        | Var                                              | Sadece `default` / `static` (Java 8+)         |
| Metot erişimi        | İstediğin (`protected` dahil)                    | Hepsi `public`                                |
| `new` ile            | ❌                                               | ❌                                            |
| Referans tipi olarak | ✅                                               | ✅                                            |

**Karar kuralı (kısa):**
- Alt class'lar **ortak field ve kod** paylaşıyor, aralarında gerçek IS-A var → **abstract class**.
- Akraba olmayan class'lara **ortak bir yetenek** kazandıracaksın, ya da bir class birden fazla rol üstlenecek → **interface**.
- Emin değilsen → interface ile başla (daha esnek; Java'da tek `extends` hakkın var, harcama).

---

## 📝 KAĞIDA YAZ (sadece bunlar)

1. `abstract` metot: gövde yok, `;`. Olan class `abstract` olmak zorunda.
2. `abstract` class: `new` ❌, ama constructor, field, normal metot ✅, referans tipi ✅.
3. Somut alt class bütün abstract metotları yazmazsa → **derlenmez** (unutmayı imkânsız kılar).
4. `interface` = yetenek sözleşmesi. Metotlar otomatik `public abstract`, field'lar `public static final`.
5. `extends` **bir**, `implements` **çok**.
6. IS-A + ortak kod → abstract class. CAN-DO / akraba olmayanlar → interface.

---

## 💻 EGZERSİZLER (AI yok, IntelliJ'de AI kapalı)

### E0 — Kurulum (5 dk)
`hafta-02/gun-05/` → **Sources Root** → package **`w2d5`**.
Dünkü `w2d4`'ten `Shape`, `Circle`, `Rectangle`, `Triangle`, `BankAccount`, `SavingsAccount`, `CheckingAccount`'ı **kopyala**, `package w2d5;` yap. Constructor'lar package-private kaldıysa **`public`** yap.

### E1 — Shape'i abstract yap ⭐⭐⭐
Sırayla, **her adımda derle**:

1. `Shape`'i `abstract` yap, `area()` ve `getName()`'i abstract metoda çevir.
   - Bir yerde `new Shape()` yaz → hata mesajını **kağıda** kopyala → sil.
2. `Triangle`'dan `area()` metodunu **geçici olarak** yoruma al → derle → hata mesajını kağıda kopyala. Mesajda hangi class'ın, hangi metodun adı geçiyor? Geri aç.
3. **Refactor:** Dün her alt class'ın kendi `toString`'i vardı. `toString`'i **sadece** `Shape`'te yaz: `Circle[area=12.57]` formatında (§2'deki örnek). Alt class'lardaki `toString`'leri **sil**.
   - Çalıştırmadan önce kağıda: `new Circle(2)` için `println` ne yazar? Hangi `getName()` çalışır, neden?
4. Dünkü `ShapeDemo`'yu kopyala (`largest`, `totalArea`) — **hiç değiştirmeden** çalışıyor mu? Neden değişmesi gerekmedi? (1 cümle)
5. **Kağıda:** Dünkü tahmin cevabını aç ve bugünkü bilgiyle karşılaştır. Neyi doğru tahmin ettin?

### E2 — Dünkü `fee()` tasarımını kodla ⭐⭐⭐
Dün E5'te kağıtta çözdün; bugün kod. `w2d5` içinde:

- `BankAccount`'a `public double fee()` ekle → `0.0` döndürür.
- `CheckingAccount`'ta `fee()`'yi override et → `1.0`. `CheckingAccount`'taki **`withdraw` override'ını sil** — artık ücret mantığı `BankAccount.withdraw` içinde `fee()` kullanılarak tek yerde olsun. (Hangi satır değişir, sen bul. İpucu: "çekilecek toplam" ne?)
- Yeni class: `StudentAccount extends BankAccount` → `fee()` = `0.25`. **Başka hiçbir dosyaya dokunma.**
- `w2d5/FeeDemo.java`: 4 türden (`BankAccount`, `Savings`, `Checking`, `Student`) birer hesap, tek `BankAccount[]`. Hepsine 100 yatır, **aynı döngüde** `withdraw(10)`, her birinin `toString`'ini yazdır.
  - **Önce kağıda** 4 bakiyeyi yaz, sonra çalıştır.
  - Bir `CheckingAccount`'tan `transfer` yap → ücret yine kesiliyor mu? Bakiyeyi elle hesapla, çıktıyla karşılaştır.
- **Kağıda (2-3 cümle):** `BankAccount`'u `abstract` yapmalı mıyız? Yaparsak hangi satırlar bozulur? `fee()`'yi abstract yapsaydık her alt class'tan ne istemiş olurduk — bu bankada mantıklı mı?

### E3 — Interface: `Payable` ⭐⭐⭐
`w2d5/Payable.java`, `Invoice.java`, `Employee.java`, `PayrollDemo.java`:

- `interface Payable` → `double getPaymentAmount();`
- `Invoice implements Payable`: `private final` `String partName`, `int quantity`, `double pricePerItem`. Ödeme = adet × birim fiyat. `toString` → `Invoice[Keyboard x3 = 1500.00]`.
- `Employee implements Payable`: `private final String name`, `private final double monthlySalary`. Ödeme = maaş. `toString` → `Employee[Ayse, 42000.00]`.
- `PayrollDemo`:
  - `static double totalPayment(Payable[] items)` — `Invoice` veya `Employee` kelimesi bu metodun içinde **geçmemeli**.
  - 2 fatura + 2 çalışanlık tek `Payable[]`, hepsini yazdır, toplamı yazdır. Toplamı **önce elle** hesapla.
- Deneyler (her birini dene, hata mesajını kağıda yaz, geri al):
  1. `Invoice`'taki `getPaymentAmount`'ın önündeki `public`'i sil.
  2. `Payable p = new Invoice(...); p.toString();` derlenir mi? `p.getPartName()` (bir getter yazdıysan) derlenir mi? Neden? (Dünün §2 tablosu.)
- **Bonus (zorunlu değil):** `Payable`'a `default boolean isLarge()` ekle (> 10 000). `Invoice` veya `Employee`'yi değiştirmeden çalıştığını göster.

### E4 — İki rol birden ⭐⭐
`w2d5/Describable.java` → `String describe();`
- `Employee`'yi hem `Payable` hem `Describable` yap (`implements A, B`). `describe()` → `"Employee Ayse earns 42000.00"`.
- `Circle`'ı da `Describable` yap — `Circle` zaten `Shape`'i extend ediyor (`extends ... implements ...` sırası önemli, hangisi önce?).
- `PayrollDemo` veya ayrı bir `DescribeDemo`'da tek `Describable[]` içine bir `Employee` ve bir `Circle` koy, döngüde `describe()` yazdır.
- **Kağıda:** Bu dizideki elemanlar üzerinden `getPaymentAmount()` çağırabilir misin? Neden? (Cevap tek cümle.)

### E5 — Derlenir mi? ⭐⭐ (kağıt — çalıştırmadan, 10 dk)
Her biri için: derlenir / derlenmez + bir cümle sebep. Sonra istersen 2-3 tanesini dene.

```java
// 1
abstract class A { }
A a = new A();

// 2
abstract class B { abstract void run() { } }

// 3
class C { abstract void run(); }

// 4
interface D { void go(); }
class E implements D { void go() { } }

// 5
interface F { int LIMIT = 5; }
// başka bir yerde:
F.LIMIT = 6;

// 6
abstract class G { G(int x) { } }
class H extends G { H() { super(3); } }

// 7
class I extends Shape, Payable { }

// 8
abstract class J { void hello() { System.out.println("hi"); } }   // hiç abstract metot yok
```

---

## 🧩 ALGORİTMA SAATİ (1 sa)

**Yöntem (değişmedi):** imza kağıda → 3-5 Türkçe cümle → bir örnekte elle durum çiz → `w2d5/Algo1.java`, `Algo2.java` içinde kendi testlerinle yaz → LeetCode'a gönder. 20 dk ilerleme yoksa dur, cümlelerini bana göster.

**Yeni kural — test tablosu:** Kodu yazmadan **önce** kağıda en az 4 test yaz: `girdi | beklenen | bu test neyi kontrol ediyor`. Son sütunda iki satır aynı şeyi söylüyorsa birini değiştir.

**A1 — [LeetCode 977: Squares of a Sorted Array](https://leetcode.com/problems/squares-of-a-sorted-array/)** — iki uçtan pointer (Gün 8 A1'in kardeşi)
`int[] sortedSquares(int[] nums)`: `nums` küçükten büyüğe sıralı (negatif olabilir). Her elemanın karesini al, sonucu **sıralı** döndür. `[-4,-1,0,3,10]` → `[0,1,9,16,100]`.
- Önce kolay yol: kare al + `Arrays.sort` — O(n log n). Çalıştır, LeetCode'a gönder. Sonra soru: **O(n)** yapabilir misin?
- İpucu sorusu: sıralı dizide **en büyük kare** nerede olabilir? (Sadece iki aday var.) Yeni diziyi hangi uçtan doldurmalısın?
- Kağıtta `[-4,-1,0,3,10]` için tablo: `left | right | yazılan index | yazılan değer`.
- Testler: hepsi negatif, hepsi pozitif, karışık, tek eleman.

**A2 — [LeetCode 409: Longest Palindrome](https://leetcode.com/problems/longest-palindrome/)** — sayma dizisi
`int longestPalindrome(String s)`: `s`'nin harfleriyle (yerlerini değiştirerek) kurulabilecek **en uzun palindromun uzunluğu**. Büyük/küçük harf farklı sayılır (`"Aa"` palindrom değil). `"abccccdd"` → `7` (`"dccaccd"`). `"a"` → `1`.
- Bu sefer harf hem büyük hem küçük → `int[26]` yetmez. Kaç elemanlı bir dizi, hangi index? (İpucu: `char`'ın kendisi bir sayı — Gün 5.)
- Düşün: palindromda bir harf kaç kere kullanılabilir? 5 tane `c` varsa kaçını kullanırsın? **Tek** sayıdaki harflerden kaç tanesi ortaya gidebilir?
- Testler: hepsi çift sayıda, bir tek sayılı harf var, birden fazla tek sayılı harf var, büyük/küçük karışık (`"Aa"` → 1).

---

## 🔧 GIT (10 dk)

```bash
git add hafta-02/gun-05
git status            # sadece .java ve DERS.md (.class yok)
git commit -m "feat: week 2 day 5 - abstract class and interface"
git push
```
Sonra `ILERLEME.md` Gün 10 logu → commit + push (`docs: day 10 progress log`).
⚠️ **"Ne takıldı"** satırına `-` yazma. Dün String.format ve E4 2. satırda takıldın — bunlar takılmadır. Bugün de en az bir şey olacak; onu yaz.

---

## ✅ Gün Sonu

- [ ] Isınma soruları (6 dahil)
- [ ] E0 package `w2d5`
- [ ] E1–E5 tamam (E1/E3 hata mesajları kağıtta, E5 tahmin **çalıştırmadan önce**)
- [ ] A1–A2 (önce test tablosu + 3-5 cümle, A1 tablo)
- [ ] "Kağıda Yaz" kutusu kağıtta
- [ ] Commit + push, `ILERLEME.md` Gün 10 logu ve commit'i

**Yarın oturum başında soracaklarım:**
1. Abstract class'tan neden nesne üretilemez, ama constructor'ı neden var?
2. Bir alt class abstract metodu override etmezse ne olur? İki seçeneği var — ikisi ne?
3. Interface ile abstract class arasında 3 fark.
4. `Employee` hem `Payable` hem `Describable` — `Payable p = emp;` ile `p.describe()` çağırabilir misin?
5. Ne zaman abstract class, ne zaman interface seçersin? Bir örnekle.

**Yarın (Pazar):** Hafta 2 tekrarı + **Hafta 2 mini sınavı** (Gün 6–10: class, encapsulation, static, inheritance, polymorphism, abstract, interface). Sonra `equals/hashCode` ve haftanın projesi "Kütüphane Sistemi".
