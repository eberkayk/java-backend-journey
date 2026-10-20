# Hafta 3 · Gün 1 (Gün 13) — Exceptions

**Hedef:** Bir metodun "olmadı" demesinin `false`/`null` dönmekten daha iyi bir yolunu öğrenmek: **exception**. Dün `borrowBook` `false` döndüğünde kullanıcıya **neden** başarısız olduğunu söyleyemedin (4 sebep, tek `boolean`). Bugün sonunda Kütüphane her hatanın sebebini söyleyecek, menüye `abc` yazınca program çökmeyecek.

**Süre:** ~6 saat · Isınma 20 dk · Ders 1.5 sa · Egzersiz 1 sa · Proje 2 sa · Algoritma 45 dk · Kapanış 20 dk

**Kurallar (değişmedi):**
- Sınıf, metot, değişken ve ekran mesajları **İngilizce**
- **Değişiklik → derle → çalıştır → çıktının her satırını oku.**
- **Metot adı/imzası görevde nasılsa öyle.**
- Hesaplayan metot yazdırmaz, **return** eder (ya da bugünden itibaren: **throw** eder).
- Tahminler **çalıştırmadan önce** chat'e.

---

## 0. Isınma (20 dk) — notlara bakmadan, cevapları chat'e

1. `Object`'in `equals`'ı neyi karşılaştırır? Neden override ederiz?
2. `equals(Book other)` yazarsan ne olur? `@Override` seni nasıl korur?
3. equals–hashCode kuralı nedir? Tersi neden şart değil?
4. `equals`'ı override edip `hashCode`'u etmezsen `HashSet`'te ne olur? Kutu modeliyle anlat.
5. `Book`'un iş anahtarı neden başlık değil ISBN?
6. (Dünkü E2'den) `x.equals(z)` çağrısında Java hangi `equals`'ı seçeceğine bakarken **`x`'in** gerçek tipine mi bakar, **`z`'nin** gerçek tipine mi? (Bir cümle.)

---

## 1. Sorun: `false` ve `null` yetmiyor ⭐⭐ (10 dk)

Dünkü kodun:

```java
public boolean borrowBook(String memberId, String isbn) {
    if (findMember(memberId) == null || findBook(isbn) == null
            || !findBook(isbn).isAvailable() || !findMember(memberId).canBorrow()) {
        return false;
    }
    ...
}
```

Menüde `Borrow failed` yazıyor. Kullanıcı şunu bilmiyor:
- Üye mi yok? Kitap mı yok? Kitap başkasında mı? Limit mi dolu?

Bir de şu var: menüde sayı yerine `abc` yazarsan program **çöküyor**:

```
Exception in thread "main" java.lang.NumberFormatException: For input string: "abc"
    at java.base/java.lang.Integer.parseInt(...)
    at w2d7.LibraryApp.main(LibraryApp.java:29)
```

Bu yazıyı Gün 4'ten beri görüyorsun (stack trace). Bugün ne olduğunu ve nasıl **yakalanacağını** öğreniyoruz.

**Üç eski yöntem, üç sorun:**

| Yöntem         | Sorun                                                                                                          |
| -------------- | -------------------------------------------------------------------------------------------------------------- |
| `return false` | Sebep kaybolur. Çağıran kontrol etmeyi unutabilir (dün `LibraryApp` ilk hâlinde dönüş değerine bakmıyordu).    |
| `return null`  | Çağıran kontrol etmeyi unutursa birkaç satır sonra `NullPointerException`, hata gerçek yerinden uzakta patlar. |
| `return -1`    | Sadece sayı dönen metotlarda var, ve `-1` bazen geçerli bir değer.                                             |

**Exception** bunların hepsini çözer: metot "olmadı" der, **sebebini bir nesnenin içine koyar** ve çağıran bunu **görmezden gelemez** (yakalamazsa program durur, sessizce yanlış devam etmez).

---

## 2. Exception nedir? ⭐⭐⭐ (15 dk)

Exception **bir nesnedir**. Sıradan bir class'tan `new` ile yaratılır. Tek farkı: `throw` ile **fırlatılabilir**.

```java
NumberFormatException e = new NumberFormatException("bad number");  // sadece bir nesne, hiçbir şey olmaz
throw e;                                                               // şimdi fırlatıldı
```

`throw` çalıştığı an:
1. Metodun **geri kalanı çalışmaz** (`return` gibi, ama değer dönmez).
2. Java, bu metodu çağıran metoda bakar: "Bunu yakalayan bir `catch` var mı?" Yoksa onu çağırana, onu çağırana… (Stack trace'te gördüğün liste tam bu zincir, Gün 4.)
3. `main`'e kadar kimse yakalamazsa program durur ve stack trace basılır.

### Hiyerarşi (Hafta 2'de öğrendiğin inheritance, burada da var)

```
                 Throwable
                /         \
           Error           Exception
   (JVM'in sorunu:          /        \
    OutOfMemoryError,   (diğerleri)   RuntimeException
    StackOverflowError)  IOException       |-- NullPointerException
   → DOKUNMA             ...               |-- ArrayIndexOutOfBoundsException
                         = CHECKED         |-- ClassCastException
                                           |-- ArithmeticException      (5 / 0)
                                           |-- IllegalArgumentException
                                           |      |-- NumberFormatException
                                           |-- IllegalStateException
                                           = UNCHECKED
```

Şimdiye kadar gördüğün hataların **hepsi** `RuntimeException`'ın altında. `NumberFormatException` bir `IllegalArgumentException`'dır (IS-A, Gün 8).

---

## 3. Yakalamak: `try` / `catch` / `finally` ⭐⭐⭐ (20 dk)

```java
try {
    // hata çıkabilecek kod
} catch (NumberFormatException e) {
    // sadece NumberFormatException (veya alt class'ı) fırlatılırsa buraya gelir
} finally {
    // hata olsa da olmasa da, yakalansa da yakalanmasa da HER ZAMAN çalışır
}
```

**Akış kuralları (E1'de bunları izleyeceksin):**
1. `try` içinde bir satır exception fırlatırsa, `try`'ın **o satırdan sonraki kısmı atlanır**. Geri dönülmez.
2. Java `catch` bloklarına **yukarıdan aşağı** bakar, tipi uyan **ilk** `catch` çalışır. (Uyan = aynı class veya üst class'ı. Polymorphism, Gün 9.)
3. Hiçbir `catch` uymazsa exception yukarı (çağırana) gider, **ama önce `finally` çalışır**.
4. `catch` bittikten sonra program `try-catch-finally`'nin **altındaki** satırdan normal devam eder.

### Exception nesnesinden ne alabilirsin?

| Çağrı                          | Ne verir                                                                 |
| ------------------------------ | ------------------------------------------------------------------------ |
| `e.getMessage()`               | Fırlatırken verilen mesaj (`"For input string: \"abc\""`)                |
| `e.getClass().getSimpleName()` | Class adı (`"NumberFormatException"`), `Member.toString`'de kullanmıştın |
| `e.printStackTrace()`          | Stack trace'i basar, program devam eder                                  |

### Birden fazla `catch`

```java
try {
    ...
} catch (NumberFormatException e) {       // önce ALT class
    ...
} catch (IllegalArgumentException e) {    // sonra ÜST class
    ...
}
```

Sırayı ters yazarsan derlenmez: üst class zaten her şeyi yakalar, alttaki `catch`'e hiç sıra gelmez, Java bunu "ölü kod" sayar (E3).

İki farklı tipe **aynı** şeyi yapacaksan (multi-catch):
```java
} catch (IllegalArgumentException | IllegalStateException e) {
    System.out.println("Error: " + e.getMessage());
}
```

### ⚠️ Asla yapma: hatayı yutmak

```java
try {
    library.borrowBook(id, isbn);
} catch (Exception e) {
    // boş
}
```

Hata oldu, kimse bilmiyor. `return false`'tan bile kötü. Yakaladıysan **bir şey yap**: kullanıcıya söyle, logla ya da tekrar dene.

---

## 4. Checked vs Unchecked ⭐⭐⭐ (15 dk) — mülakatta kesin sorulur

|                      | Unchecked                                                                    | Checked                                                                                      |
| -------------------- | ---------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------- |
| Kimler               | `RuntimeException` ve alt class'ları (+ `Error`)                             | `Exception`'ın geri kalanı (`IOException`, `SQLException`, ...)                              |
| Derleyici zorlar mı? | **Hayır**                                                                    | **Evet**: ya `catch` et ya da imzaya `throws` yaz, yoksa **derlenmez**                       |
| Genelde ne anlatır   | **Programcı hatası / geçersiz girdi**: null geçtin, index taştı, yanlış sayı | **Dış dünya sorunu**: dosya yok, ağ koptu, DB cevap vermiyor. Kodun doğru olsa bile olabilir |

### `throw` ≠ `throws`

```java
//                                  throws: İMZADA. "Bu metot bunu fırlatabilir, haberin olsun."
static String readConfig(String path) throws IOException {
    if (path.isBlank()) {
        throw new IOException("No config file");   // throw: GÖVDEDE. Gerçekten fırlatır.
    }
    return "ok";
}
```

`IOException` checked olduğu için:
- `throws IOException` yazmazsan `readConfig` **derlenmez**.
- Yazarsan, bu sefer `readConfig`'i çağıran her yer ya `catch` etmek ya da kendisi `throws` yazmak zorunda.

`RuntimeException`'larda ikisini de yazmak zorunda değilsin.

> Yeni API: `String.isBlank()` → string boşsa **veya sadece boşluktan** oluşuyorsa `true`. `"".isBlank()` → true, `"   ".isBlank()` → true, `" a ".isBlank()` → false.

**Modern Java/Spring'de pratik:** Kendi exception'larımızı neredeyse hep **unchecked** (`extends RuntimeException`) yazarız. Spring de böyle yapar. Checked exception'ı genelde sadece Java'nın kendi API'leri (dosya, ağ) sana verir. Hafta 4'te dosya okurken `IOException` ile tanışacaksın.

---

## 5. Kendi exception'ını yazmak ⭐⭐⭐ (15 dk)

Exception bir class olduğuna göre `extends` ile kendi tipini yaratabilirsin:

```java
public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(double balance, double amount) {
        super("Insufficient funds: balance " + balance + ", requested " + amount);
    }
}
```

- `super(mesaj)`: mesajı `RuntimeException`'ın constructor'ına verir. `getMessage()` bunu döndürür.
- Class adının kendisi de bilgidir: `catch (InsufficientFundsException e)` dediğinde sadece bu durumu yakalarsın.

Kullanımı (Hafta 2'deki `BankAccount.withdraw` boolean dönüyordu, şimdi):

```java
public void withdraw(double amount) {
    if (amount <= 0) {
        throw new IllegalArgumentException("Amount must be positive: " + amount);   // hazır class yetiyor
    }
    if (amount > balance) {
        throw new InsufficientFundsException(balance, amount);                       // kendi class'ımız
    }
    balance -= amount;
}
```

Dikkat: artık `if`'lerin hiçbirinde `else` yok. `throw` metodu bitirdiği için gerek kalmadı. Bu yapıya **guard clause** denir: önce bütün kötü durumları ele, en altta sadece "mutlu yol" kalsın.

### Ortak bir üst class (aile)

```java
public class BankException extends RuntimeException { ... }
public class InsufficientFundsException extends BankException { ... }
public class AccountNotFoundException extends BankException { ... }
```

Böylece çağıran **seçebilir**: sadece birini yakala, ya da `catch (BankException e)` ile hepsini tek yerde yakala (polymorphism).

### Kim fırlatır, kim yakalar?

- **İş mantığı (`Library`, `BankAccount`) fırlatır.** Ne olduğunu bilir ama ekrana ne yazılacağını bilmez (konsol mu, web sayfası mı?).
- **Kullanıcıya dokunan katman (`LibraryApp`, ileride Spring controller) yakalar** ve mesajı gösterir.
- `Library` kendi fırlattığını kendi içinde yakalayıp `false`'a çevirmez, yoksa dünkü sorun geri gelir.

---

## 📝 KAĞIDA YAZ (sadece bunlar)

```
1. Throwable → Error (dokunma) | Exception → RuntimeException = UNCHECKED, gerisi CHECKED
2. Checked: derleyici zorlar → catch ET ya da imzaya throws YAZ
3. throw = gövdede, fırlatır (new ...)  |  throws = imzada, uyarır
4. try'da hata → o satırdan sonrası ATLANIR → uyan İLK catch → finally HER ZAMAN → alttan devam
5. catch sırası: ALT class önce, ÜST class sonra
6. Boş catch YASAK (hatayı yutma)
7. class XException extends RuntimeException { XException(String m) { super(m); } }
8. İş katmanı fırlatır, UI katmanı yakalar
```

---

## 💻 EGZERSİZLER (AI yok, IntelliJ'de AI kapalı)

### E0 — Kurulum (5 dk)
`hafta-03/gun-01/` klasörünü **Sources Root** işaretle, içinde `w3d1` paketi aç. Bugünkü bütün dosyalar `package w3d1;`.

### E1 — Akışı izle ⭐⭐⭐ (çalıştırmadan önce tahmin chat'e)

`w3d1/FlowDemo.java`:

```java
package w3d1;

public class FlowDemo {
    static int parse(String s) {
        System.out.println("parse start: " + s);
        int n = Integer.parseInt(s);
        System.out.println("parse end");
        return n;
    }

    static void main(String[] args) {
        try {
            System.out.println("A");
            int x = parse("42");
            System.out.println("B " + x);
            int y = parse("4x2");
            System.out.println("C " + y);
        } catch (NumberFormatException e) {
            System.out.println("D " + e.getMessage());
        } finally {
            System.out.println("E");
        }
        System.out.println("F");
    }
}
```

**(a)** Çıktıyı satır satır tahmin et. `getMessage()`'ın tam metnini bilmiyorsun, o satır için `D <mesaj>` yazman yeterli. Çalıştır, karşılaştır.
**(b)** Sadece `catch (NumberFormatException e)` satırını `catch (ArithmeticException e)` yap. Şimdi çıktı ne? `E` yazılır mı? `F` yazılır mı? Önce tahmin, sonra çalıştır. Stack trace'te **hangi satır numaraları** var, chat'e yaz.
**(c)** Tek cümle: (b)'de `"parse end"` neden yazılmadı?

### E2 — Derleyici seni zorluyor (checked) ⭐⭐⭐

`w3d1/CheckedDemo.java`. Her adımda **önce** "derlenir mi?" tahmini chat'e, sonra dene. Derlenmiyorsa hata mesajını **aynen** kopyala.

1. Şu metodu yaz ve `main`'den `readConfig("")` diye çağır:
   ```java
   static String readConfig(String path) {
       if (path.isBlank()) {
           throw new java.io.IOException("No config file");
       }
       return "config: " + path;
   }
   ```
2. `readConfig`'in imzasına `throws java.io.IOException` ekle. Hata kayboldu mu, yoksa **yer mi değiştirdi**? Neresi şimdi kırmızı?
3. `main`'de çağrıyı `try`/`catch (java.io.IOException e)` ile sar, catch içinde `"Error: " + e.getMessage()` yazdır. Çalıştır.
4. Şimdi `IOException` yerine `IllegalStateException` fırlat, `throws`'u **ve** `main`'deki `try/catch`'i sil. Derlenir mi? Çalışınca ne olur?
5. Chat'e iki cümle: 1. adım ile 4. adım arasındaki fark neden var?

> `java.io.IOException` yazmak yerine dosyanın başına `import java.io.IOException;` ekleyebilirsin. İkisi aynı.

### E3 — catch sırası ⭐⭐ (çalıştırmadan, tahmin chat'e)

```java
try {
    int n = Integer.parseInt("abc");
} catch (IllegalArgumentException e) {
    System.out.println("IAE");
} catch (NumberFormatException e) {
    System.out.println("NFE");
}
```

1. Derlenir mi? Derlenmezse neden? (Bölüm 2'deki hiyerarşiye bak.)
2. İki `catch`'in yerini değiştirirsen ne yazar?
3. Sadece `catch (IllegalArgumentException e)` kalsa (NFE'yi sil), `"abc"` için ne yazar? Neden?

Sonra `w3d1/CatchOrder.java`'da dene, tahminlerini kontrol et.

### E4 — Güvenli sayı okuma ⭐⭐⭐

Dünkü menünün çökme sorununu çözen yardımcı metot. `w3d1/SafeInput.java`:

**Class `SafeInput`**
- Field yok.
- `public static int readInt(Scanner scanner, String prompt)`
  - `prompt`'u yazdırır (`System.out.print`), `scanner.nextLine()` ile bir satır okur, `Integer.parseInt` ile çevirir.
  - Çevrilemezse (`NumberFormatException`) `"Please enter a number"` yazdırır ve **tekrar sorar**. Geçerli sayı gelene kadar döner.
  - Geçerli sayıyı `return` eder.
  - Döngü tipi ve `try`'ın döngünün neresinde olacağı **senin tasarım kararın**.
- `static void main(String[] args)`: bir `Scanner` yarat (döngü **dışında**, dünkü hata), `readInt(scanner, "Age: ")` çağır, sonucu yazdır.

**Test (elle, konsolda):** `abc` → tekrar sorar · `` (boş, direkt Enter) → tekrar sorar · `12.5` → ? (önce tahmin) · `-3` → kabul eder · `42` → kabul eder.

---

## 🏛️ PROJE — Kütüphane v3: exception'larla (~2 saat)

Dünkü Kütüphaneyi kopyalayıp `false`/`null` dönüşlerini exception'a çeviriyoruz. Collections'a **yarın** geçeceğiz, bugün diziler aynen kalıyor.

### Adım 0 — Kopyala
`hafta-02/gun-07/w2d7/` içinden şunları `hafta-03/gun-01/w3d1/` içine kopyala ve her dosyada `package w2d7;` → `package w3d1;` yap:
`Book`, `Member`, `StudentMember`, `StaffMember`, `Library`, `LibraryApp`.
Derle, çalıştığından emin ol.

### Adım 1 — Exception class'ları (her biri ayrı dosya, `w3d1` paketinde)

**1. `LibraryException`**
- `public class LibraryException extends RuntimeException`
- Constructor: `public LibraryException(String message)` → `super(message)`
- Başka field/metot yok. Bu, ailenin üst class'ı.

**2. `BookNotFoundException`**
- `extends LibraryException`
- Constructor: `public BookNotFoundException(String isbn)`
- Mesaj: `"Book not found: " + isbn`

**3. `MemberNotFoundException`**
- `extends LibraryException`
- Constructor: `public MemberNotFoundException(String memberId)`
- Mesaj: `"Member not found: " + memberId`

**4. `BookNotAvailableException`**
- `extends LibraryException`
- Constructor: `public BookNotAvailableException(String isbn)`
- Mesaj: `"Book is already borrowed: " + isbn`

**5. `BorrowLimitExceededException`**
- `extends LibraryException`
- Constructor: `public BorrowLimitExceededException(Member member)`
- Mesaj: `"Borrow limit reached for " + <üyenin id'si> + " (max " + <üyenin maxBooks()> + ")"`
- Örnek: `Borrow limit reached for M1 (max 2)`

**6. `NotBorrowedByMemberException`**
- `extends LibraryException`
- Constructor: `public NotBorrowedByMemberException(String memberId, String isbn)`
- Mesaj: `"Book " + isbn + " is not borrowed by " + memberId`

### Adım 2 — `Book` constructor'ında doğrulama
`Book(String isbn, String title, String author)` constructor'ının **en başına**:
- `isbn` `null` ise **veya** `isBlank()` ise → `throw new IllegalArgumentException("ISBN must not be blank")`
- `title` için aynısı → `"Title must not be blank"`
- Geri kalan her şey (alanlar, `equals`, `hashCode`, `toString`) aynı kalır.

(Neden önemli: artık geçersiz bir `Book` nesnesi **hiç yaratılamaz**. Hata, verinin sisteme girdiği kapıda yakalanır.)

### Adım 3 — `Library`'nin yeni imzaları

Field'lar ve constructor **aynı kalır**. Değişen metotlar:

| Metot        | Eski                               | Yeni                                                                       |
| ------------ | ---------------------------------- | -------------------------------------------------------------------------- |
| `addBook`    | `boolean addBook(Book book)`       | `public void addBook(Book book)`                                           |
| `addMember`  | `boolean addMember(Member member)` | `public void addMember(Member member)`                                     |
| `findBook`   | `null` dönerdi                     | `public Book findBook(String isbn)`: bulamazsa `BookNotFoundException`     |
| `findMember` | `null` dönerdi                     | `public Member findMember(String id)`: bulamazsa `MemberNotFoundException` |
| `borrowBook` | `boolean`                          | `public void borrowBook(String memberId, String isbn)`                     |
| `returnBook` | `boolean`                          | `public void returnBook(String memberId, String isbn)`                     |

`availableCount`, `booksByAuthor`, `getAllBooks` **değişmez**.

**`addBook(Book book)`**, kontroller bu sırayla:
1. `book == null` → `IllegalArgumentException("Book must not be null")`
2. Dizi dolu → `IllegalStateException("Library is full")`
3. Aynı ISBN'li kitap var → `IllegalArgumentException("Duplicate ISBN: " + isbn)`
4. Ekle.

**`addMember(Member member)`**: aynı yapı. Mesajlar: `"Member must not be null"`, `"Member list is full"`, `"Duplicate member id: " + id`.

**`borrowBook(String memberId, String isbn)`**, kontroller bu sırayla:
1. Üye yok → `MemberNotFoundException` (bunu `findMember` zaten fırlatıyor, sen ayrıca `if` yazmana gerek yok)
2. Kitap yok → `BookNotFoundException` (`findBook` fırlatıyor)
3. Kitap başkasında → `BookNotAvailableException`
4. Üyenin limiti dolu → `BorrowLimitExceededException`
5. Ödünç ver (dünkü iki satır).

**`returnBook(String memberId, String isbn)`**, kontroller bu sırayla:
1. Üye yok → `MemberNotFoundException`
2. Kitap yok → `BookNotFoundException`
3. Kitabın ödünç alanı bu üye değil (kitap rafta olsa bile) → `NotBorrowedByMemberException`
4. İade al (dünkü iki satır).

**Ek kural:** `borrowBook` ve `returnBook` içinde `findMember` ve `findBook` **birer kez** çağrılsın; sonucu bir local değişkende tut (`Member member = findMember(memberId);`). Dünkü kodda `findBook(isbn)` aynı metotta 4 kez çağrılıyordu.

**Yasak:** `Library` içinde `try`/`catch` yok. `Library` sadece fırlatır.

### Adım 4 — Testler

Testleri Claude yazıp çalıştırıyor. Bu adımda senin yapacağın bir şey yok.

### Adım 5 — `LibraryApp` güncelle

1. E4'teki `readInt` metodunu `LibraryApp`'e `private static` olarak kopyala, menü seçimini onunla oku. `abc` artık çökertmemeli.
2. Seçenek 3 (Borrow) ve 4 (Return): çağrıyı `try`/`catch (LibraryException e)` ile sar.
   - Başarılı → `"Borrowed"` / `"Returned"`
   - Hata → `"Error: " + e.getMessage()` (örn. `Error: Borrow limit reached for M1 (max 2)`)
3. Seçenek 2 (Add book): hem `Book` constructor'ı hem `addBook` hata atabilir. **Multi-catch** kullan: `catch (IllegalArgumentException | IllegalStateException e)` → `"Error: " + e.getMessage()`.
4. Açılıştaki örnek veri ekleme satırları (`library.addBook(...)`) `try` gerektirir mi? Gerektirmez ama veri yanlışsa ne olur? Chat'e bir cümle.

**Manuel test:** Menüde `abc`, olmayan üye ile ödünç, öğrenciyle 3. kitap, rafta olan kitabı iade. Her birinin ekran çıktısını chat'e yapıştır.

### Proje sonu düşünme soruları (chat'e, 2-3 cümle)
1. `LibraryException`'ı `RuntimeException` yerine `Exception`'dan türetseydin, `LibraryApp`'te ne değişirdi?
2. `LibraryApp`'te `catch (LibraryException e)` yerine `catch (Exception e)` yazsaydın ne kaybederdin? (İpucu: kodunda bir `NullPointerException` bug'ı olsaydı ne görürdün?)
3. Dün `setBorrower` public'ti. Bugünkü `Library` bunu çözdü mü? Çözmediyse neden?

---

## 🧩 ALGORİTMA SAATİ (45 dk) — yeni kalıp: prefix sum

**Yöntem (değişmedi):** imza kağıda → 3-5 Türkçe cümle → `w3d1/Algo1.java`, `Algo2.java` → LeetCode.

**A1 — [LeetCode 1480: Running Sum of 1d Array](https://leetcode.com/problems/running-sum-of-1d-array/)**
`int[] runningSum(int[] nums)`: `result[i] = nums[0] + nums[1] + ... + nums[i]`.
- Girdi dizisini **bozma**, yeni dizi döndür.
- O(n) olmalı: her `result[i]`'yi baştan toplama. `result[i]` ile `result[i-1]` arasındaki ilişkiyi bul.

**A2 — [LeetCode 724: Find Pivot Index](https://leetcode.com/problems/find-pivot-index/)**
`int pivotIndex(int[] nums)`: solundaki elemanların toplamı sağındakilerin toplamına eşit olan **en soldaki** index; yoksa `-1`. (En soldaki index'in solu boş = toplam 0.)
- Önce O(n²) yolu Türkçe cümleyle anlat (kodlama), sonra O(n) yolu kodla.
- İpucu (O(n)): Dizinin **toplamını** bir kere hesapla. `i`'deyken solun toplamını biliyorsan, sağın toplamını nasıl bulursun?

---

## 🔧 GIT (10 dk)

```bash
git add hafta-03/gun-01
git status            # sadece .java ve DERS.md (.class yok)
git commit -m "feat: week 3 day 1 - exceptions"
git push
```
Sonra `ILERLEME.md`'ye Gün 13 logu → `docs: day 13 progress log`.

---

## ✅ Gün Sonu

- [ ] Isınma (6 soru, chat'e)
- [ ] E0–E4 (E1/E2/E3 tahminler **çalıştırmadan önce** chat'e)
- [ ] Proje Adım 0–5
- [ ] Proje düşünme soruları
- [ ] A1 + A2
- [ ] "Kağıda Yaz" kutusu kağıtta
- [ ] Commit + push, `ILERLEME.md` Gün 13

**Yarın oturum başında soracaklarım:**
1. Checked ve unchecked arasındaki fark ne? Birer örnek ver.
2. `throw` ile `throws` farkı?
3. `try` içinde 2. satır exception fırlattı, `catch` uymadı. `finally` çalışır mı? `try-catch`'in altındaki satır çalışır mı?
4. Neden alt class'ın `catch`'i üst class'ınkinden önce yazılmak zorunda?
5. Kendi exception'ını neden genelde `RuntimeException`'dan türetiriz?
6. `Library` neden kendi exception'larını kendisi yakalamıyor?

**Sıradaki:** Hafta 3 Gün 2: **Collections** (`List`, `ArrayList`). Kütüphanedeki `Book[]` + sayaç → `List<Book>`. "Library is full" hatası tamamen ortadan kalkacak.
