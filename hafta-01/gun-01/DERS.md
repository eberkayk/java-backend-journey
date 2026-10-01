# Hafta 1 · Gün 1 — Terminal, Java Nasıl Çalışır, Değişkenler ve Tipler, Git

**Hedef:** Günün sonunda IDE kullanmadan bir Java programını derleyip çalıştırabilmek, 8 primitive tipi bilmek ve kodunu GitHub'a push'lamış olmak.

**Süre:** ~6 saat · Ders 2 sa · Egzersiz 3 sa · Git 45 dk · Kapanış 15 dk

---

## 1. Terminal (20 dk)

Backend developer'ın gününün yarısı terminalde geçer: sunucular, Docker, Git, loglar. Windows'ta **Git Bash** kullan (Linux komutlarıyla aynı).

| Komut | Ne yapar |
|---|---|
| `pwd` | Şu an hangi klasördeyim? |
| `ls` / `ls -la` | Klasörde ne var? (`-la`: gizli dosyalar ve detaylar dahil) |
| `cd klasor` / `cd ..` / `cd ~` | Klasöre gir / bir üste çık / ana dizine git |
| `mkdir isim` | Klasör oluştur |
| `touch dosya.txt` | Boş dosya oluştur |
| `cat dosya.txt` | Dosya içeriğini göster |
| `rm dosya` / `rm -r klasor` | Sil (geri dönüşüm kutusu yok, dikkat!) |
| `clear` | Ekranı temizle |

**Tab** tuşu otomatik tamamlar, **↑** tuşu önceki komutu getirir. Bu ikisini refleks hâline getir.

---

## 2. Java Nasıl Çalışır? (25 dk) — ⭐ Mülakat sorusu

```
Merhaba.java  --(javac)-->  Merhaba.class  --(java / JVM)-->  Çıktı
 (kaynak kod)    derleyici    (bytecode)        yorumlar + JIT
```

- **JVM (Java Virtual Machine):** Bytecode'u çalıştıran sanal makine. Her işletim sisteminin kendi JVM'i vardır.
  → Bu yüzden Java **"Write once, run anywhere"**: aynı `.class` dosyası Windows'ta da Linux'ta da çalışır.
- **JRE (Java Runtime Environment):** JVM + standart kütüphaneler. Programı *çalıştırmak* için yeterlidir.
- **JDK (Java Development Kit):** JRE + geliştirme araçları (`javac`, `jar`, debugger…). Program *yazmak* için gerekir.

> JDK ⊃ JRE ⊃ JVM

- **JIT (Just-In-Time) compiler:** JVM, sık çalışan kodu çalışma anında makine koduna çevirir. Java'nın hızlı olmasının bir sebebi budur.

### Bir Java programının anatomisi

```java
public class Merhaba {                          // dosya adı = public class adı (Merhaba.java)
    public static void main(String[] args) {    // programın giriş noktası
        System.out.println("Merhaba dünya");    // ekrana yaz + alt satıra geç
    }
}
```

- `public`: her yerden erişilebilir
- `static`: nesne oluşturmadan çağrılabilir (JVM `main`'i nesnesiz çağırır)
- `void`: bir şey döndürmez
- `String[] args`: komut satırından gelen argümanlar

---

## 3. Değişkenler ve Tipler (40 dk) — ⭐ Mülakat sorusu

Java **statically typed** bir dildir. Her değişkenin tipi derleme zamanında bellidir ve sonradan değişmez.

### 8 Primitive Tip

| Tip | Boyut | Aralık / Örnek | Ne zaman kullanılır |
|---|---|---|---|
| `byte` | 8 bit | -128 … 127 | Ham veri, dosya |
| `short` | 16 bit | -32.768 … 32.767 | Nadiren |
| `int` | 32 bit | ~ ±2.1 milyar | **Varsayılan tam sayı** |
| `long` | 64 bit | çok büyük, `123L` | ID'ler, zaman damgası |
| `float` | 32 bit | `3.14f` | Nadiren |
| `double` | 64 bit | `3.14` | **Varsayılan ondalık** |
| `char` | 16 bit | `'A'` (tek tırnak!) | Tek karakter (aslında bir sayı: Unicode) |
| `boolean` | — | `true` / `false` | Koşullar |

> ⚠️ Para hesabında `double` kullanılmaz (`0.1 + 0.2 != 0.3`). Bankalar `BigDecimal` kullanır. Mülakatta sorulur.

### Primitive ve Reference farkı

- **Primitive** değişken değerin kendisini tutar (stack'te).
- **Reference** değişken (`String`, diziler, tüm nesneler) nesnenin bellekteki **adresini** tutar. Nesne heap'tedir.
- `String` primitive değildir, bir **class**'tır. Çift tırnakla yazılır: `"Merhaba"`.

```java
int yas = 32;
double boy = 1.78;
char harf = 'E';
boolean mezunMu = true;
String isim = "Enes";
var sehir = "Manisa";   // var: tip derleyici tarafından çıkarılır (yine de String'tir, sonradan değişmez)
final int MAX = 100;    // final: sabit, bir daha atanamaz
```

### Type Casting (tip dönüşümü)

```java
int a = 10;
double b = a;          // widening (genişletme): otomatik, veri kaybı yok
double c = 9.99;
int d = (int) c;       // narrowing (daraltma): elle yapılır, ondalık KESİLİR (yuvarlanmaz!)
```

---

## 4. Operatörler (25 dk)

| Tür | Operatörler |
|---|---|
| Aritmetik | `+ - * / %` (`%` = mod, kalan) |
| Atama | `= += -= *= /= %=` |
| Artırma | `i++` (önce kullan, sonra artır) · `++i` (önce artır, sonra kullan) |
| Karşılaştırma | `== != > < >= <=` |
| Mantıksal | `&&` (ve) · `\|\|` (veya) · `!` (değil) |

**Tuzaklar:**
- `int / int` → sonuç **int** olur, ondalık kısım atılır.
- `String + herhangi bir şey` → String birleştirme yapar. İşlemler soldan sağa değerlendirilir.
- `int` sınırını aşarsan hata vermez, **taşar (overflow)** ve eksi değere döner.

---

## 5. Kullanıcıdan Girdi Almak: Scanner (10 dk)

```java
import java.util.Scanner;

public class Girdi {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Adın: ");
        String ad = scanner.nextLine();
        System.out.print("Yaşın: ");
        int yas = scanner.nextInt();
        System.out.println("Merhaba " + ad + ", 10 yıl sonra " + (yas + 10) + " yaşında olacaksın.");
    }
}
```

---

## 📝 KAĞIDA YAZ (sadece bunlar)

1. `.java → javac → .class (bytecode) → JVM`. JDK ⊃ JRE ⊃ JVM. "Write once run anywhere" JVM sayesinde.
2. 8 primitive: `byte short int long float double char boolean`. Varsayılanlar: `int`, `double`.
3. Primitive = değerin kendisi (stack) · Reference = adres, nesne heap'te. `String` bir class'tır.
4. `int/int` = int. `(int) 3.99` → 3 (keser). Para için `BigDecimal`.
5. `i++` önce kullanır sonra artırır, `++i` önce artırır.
6. Git akışı: `git add` → `git commit` → `git push`.

---

## 💻 EGZERSİZLER (AI yok, önce kendin dene)

Her egzersiz ayrı bir `.java` dosyası, hepsi bu klasörde: `hafta-01/gun-01/`

### E1 — IDE'siz ilk program (zorunlu)
Git Bash'te `hafta-01/gun-01` klasörüne git. `Merhaba.java` dosyasını bir metin editörüyle yaz (IntelliJ **değil**, Notepad olabilir). Terminalden şunları çalıştır:
```bash
javac Merhaba.java
ls              # .class dosyasını gör
java Merhaba
```
Sonra bilerek hata yap: noktalı virgülü sil ve derle. Hata mesajını oku, hangi satırı gösterdiğine bak.

### E2 — Tahmin et, sonra doğrula ⭐
**Önce kağıda** çıktıları tahmin et. Sonra `Tahmin.java` içinde hepsini `System.out.println` ile yazdırıp kontrol et. Yanlış tahminlerini işaretle, nedenini bana anlat.
```
7 / 2
7 / 2.0
7 % 3
-7 % 3
(int) 3.99
'a' + 1
(char) ('a' + 1)
"1" + 2 + 3
1 + 2 + "3"
Integer.MAX_VALUE + 1
0.1 + 0.2
10 == 10.0
```

### E3 — Sıcaklık çevirici
Kullanıcıdan Celsius al, Fahrenheit'a çevir: `F = C × 9/5 + 32`
**Dikkat:** `C * 9 / 5` ile `C * (9 / 5)` farklı sonuç verir. Neden? Dene ve açıkla.

### E4 — Saniye çevirici
Kullanıcıdan saniye al (ör. `3725`) → `1 saat 2 dakika 5 saniye` yazdır. Sadece `/` ve `%` kullan.

### E5 — Değişken takası
`int a = 5, b = 8;` değerlerini yer değiştir. Önce üçüncü bir değişkenle, sonra **üçüncü değişken kullanmadan** (ipucu: toplama/çıkarma).

### E6 — Rakamlar toplamı (döngüsüz)
Kullanıcıdan 3 basamaklı bir sayı al (ör. `472`), rakamlarının toplamını yazdır (`13`). Döngü kullanma, sadece `/` ve `%`.

### E7 — Maaş hesaplayıcı
Kullanıcıdan brüt maaş (double) ve vergi oranını (%) al. Vergi tutarını ve net maaşı yazdır. Sonuçları 2 ondalık basamakla göster.
(İpucu: `System.out.printf("%.2f%n", sayi);` — bunu dokümandan araştır.)

---

## 🔧 GIT (45 dk)

`C:\dev\roadmap backend` klasöründe, Git Bash'te:

```bash
git config --global user.name "Enes Berkay Kumtepe"
git config --global user.email "enesberkaykumtepe@gmail.com"

git init                 # bu klasörü git reposu yap
git status               # ne değişti?
```

`.gitignore` adında bir dosya oluştur ve içine şunları yaz (derlenmiş dosyaları ve IDE ayarlarını repoya koymayız):
```
*.class
.idea/
out/
target/
```

```bash
git add .
git status               # hangi dosyalar eklendi?
git commit -m "feat: gün 1 - java temelleri ve plan"
git log --oneline
```

GitHub'da **public** repo oluştur (ör. `java-backend-journey`) ve push'la:
```bash
gh repo create java-backend-journey --public --source=. --push
```

**Araştır ve kağıda 1 satır yaz:** `git add` ile `git commit` arasındaki "staging area" nedir?

---

## ✅ Gün Sonu

- [ ] E1–E7 tamam
- [ ] "Kağıda Yaz" kutusu kâğıtta
- [ ] GitHub'a push edildi
- [ ] `ILERLEME.md`'ye Gün 1 logu yazıldı

**Yarın oturum başında sana soracaklarım (cevapları notsuz vereceksin):**
1. JDK, JRE ve JVM farkı nedir?
2. `7 / 2` neden 3?
3. Primitive ve reference tip farkı nedir?
4. E2'de hangi tahminlerin yanlış çıktı ve neden?

**Yarın (Gün 2):** if/else, switch, döngüler (for, while, do-while), break/continue.
