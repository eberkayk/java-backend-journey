# Hafta 2 · Gün 6 (Gün 11, Pazar) — Hafta 2 Tekrarı + Mini Sınav

**Hedef:** Gün 6–10'u (class/object, encapsulation, static, inheritance, polymorphism, abstract class, interface) kapalı kitap bir sınavla ölçmek. Puan değil, **nerede boşluk olduğu** önemli — Hafta 3 öncesi neyi tekrar edeceğimize bununla karar vereceğiz.

**Süre:** ~2.5 saat · Isınma 15 dk · Tekrar 30 dk · Mini sınav 75 dk · Kapanış 20 dk

**Kurallar (değişmedi):**
- Sınıf, metot, değişken ve ekran mesajları **İngilizce**
- **Değişiklik → derle → çalıştır → çıktının her satırını oku.**
- **Metot adı/imzası görevde nasılsa öyle.**
- Hesaplayan metot yazdırmaz, **return** eder.

---

## 0. Isınma (15 dk) — notlara bakmadan (Gün 10 soruları, cevapları chat'e)

1. Abstract class'tan neden nesne üretilemez, ama constructor'ı neden var?
2. Bir alt class abstract metodu override etmezse ne olur? İki seçeneği var — ikisi ne?
3. Interface ile abstract class arasında 3 fark.
4. `Employee` hem `Payable` hem `Describable` — `Payable p = emp;` ile `p.describe()` çağırabilir misin? `p.toString()` peki?
5. Ne zaman abstract class, ne zaman interface seçersin? Bir örnekle.

---

## 1. Tekrar (30 dk) — sınavdan ÖNCE, notlara bakmak serbest

Sınav kapalı kitap; tekrar açık. Sırayla:

1. Gün 6–10'un **"Kağıda Yaz" kutularını** baştan oku (5 kutu, ~10 dk).
2. Kendi takıldığın yerleri tekrar et (~15 dk). Bu hafta takıldıkların:
   - Field varsayılan değeri vs local değişken (local'in neden derlenmediği)
   - `private` class seviyesinde — aynı class'ın **başka nesnesinin** private alanına erişilir
   - Alt class nesnesinin içinde parent'ın `private` alanı **vardır**, sadece erişilemez
   - `if (withdraw(x))` çağrısı metodu **çalıştırır** — sadece kontrol etmez (Gün 7 transfer)
   - Overload **referans tipine** göre, override **nesne tipine** göre seçilir (Gün 9 E4)
   - Interface referansıyla da `Object` metotları (`toString`, `equals`) çağrılabilir
3. Sorun çıkan bir şey varsa **sınavdan önce** sor (~5 dk). Sınav başladıktan sonra soru yok.

---

## 🧪 HAFTA 2 MİNİ SINAV (75 dk) — kapalı kitap

**Kurallar:** Notlara, DERS dosyalarına, eski koduna, internete bakmak **yok**. IntelliJ açık olabilir ama **AI kapalı**. Başlangıç saatini yaz, süreyi tut. 75 dk dolduğunda nerede kaldıysan bırak — yarım kod da teslim edilir.

- Teori ve çıktı cevapları → `hafta-02/gun-06/SINAV.md` (sorular hazır, sadece `Cevap:` satırlarını doldur)
- Kodlar → package **`w2d6`** (`hafta-02/gun-06/w2d6/`, Sources Root ayarını unutma)
- **Ben kontrol edeceğim — kendin "doğru" diye işaretleme.**

**Puanlama:** A teori 30 · B çıktı 20 · C kod 50

### Bölüm A — Teori (20 dk) → `SINAV.md`
### Bölüm B — Çıktı tahmini (15 dk, ÇALIŞTIRMADAN) → `SINAV.md`
Sorular `SINAV.md` içinde.

### Bölüm C — Kod (40 dk)

**C1 (25 puan) — Araç kiralama.** Her public class/interface kendi dosyasında, package `w2d6`.

1. `abstract class Rental`
   - `private final String customer`, `private final int days`
   - `private static int count` — şimdiye kadar oluşturulan kiralama sayısı. `public static int getCount()`
   - Constructor `(String customer, int days)`: `days < 1` gelirse `1` kabul et. Her nesnede `count` artsın.
   - getter'lar
   - `public abstract double dailyRate();`
   - `public double totalPrice()` → `dailyRate() * days`
   - `toString()` → `"Enes | 3 days | 450.0"` formatında (müşteri, gün, toplam fiyat)
2. `class CarRental extends Rental` — günlük ücret `150`.
3. `class BikeRental extends Rental` — günlük ücret `40`. **7 gün ve üstünde** toplam fiyata **%20 indirim**. (Hesaplamayı baştan yazma, parent'ın hesabını kullan.)
4. `interface Insurable` — tek metot: `double insuranceFee();`
   `CarRental` bunu implement etsin: sigorta = günde `30`.
5. `Exam1.java` içinde:
   - `static double totalRevenue(Rental[] rentals)` → tüm kiralamaların `totalPrice()` toplamı
   - `static double totalInsurance(Rental[] rentals)` → sadece sigortalanabilir olanların `insuranceFee()` toplamı
   - `main`: en az 4 kiralama (araba, kısa bisiklet, 7+ günlük bisiklet, `days = 0` olan biri) → hepsini yazdır, iki toplamı yazdır, `Rental.getCount()` yazdır.

**C2 (15 puan) — `Exam2.java`:** `static int[] mergeSorted(int[] a, int[] b)` → ikisi de küçükten büyüğe sıralı; ikisinin tüm elemanlarını içeren **yeni, sıralı** dizi döndür. `Arrays.sort` **yasak**, O(n+m) olmalı.
`{1, 4, 7}` + `{2, 4, 9, 10}` → `{1, 2, 4, 4, 7, 9, 10}`
- Kodlamadan önce `SINAV.md`'ye **en az 4 satırlık test tablosu** (`girdi | beklenen | neyi kontrol ediyor`). Tablo puana dahil.

**C3 (10 puan) — `Exam3.java`:** `static char mostFrequent(String s)` → en çok tekrar eden harf. Sadece küçük harf `a–z` gelir. **Eşitlikte, String'de daha önce görünen** harf kazanır.
`"abbcc"` → `'b'` · `"ccbba"` → `'c'` · `"aab"` → `'a'`
- Boş String için ne döneceğine karar ver, yorumla yaz.
- `main`'de en az 3 test, biri eşitlik durumu.

---

## 📝 Kağıda Yaz (sınavdan sonra, 5 dk)

```
HAFTA 2 — 5 CÜMLE
1. Class kalıp, object heap'teki nesne; değişken nesnenin adresini tutar.
2. Encapsulation: alan private, değişiklik sadece doğrulayan metotla.
3. static = class'a ait, tek kopya; nesneye değil.
4. Override nesne tipine göre (çalışma anı), overload referans tipine göre (derleme anı).
5. Abstract class = ortak kod + zorunlu metot; interface = yetenek sözleşmesi, çoklu implement.
```

---

## 🔧 GIT (10 dk)

```bash
git add hafta-02/gun-06
git status            # sadece .java ve .md (.class yok)
git commit -m "feat: week 2 mini exam"
git push
```
Ben notu verdikten sonra: `ILERLEME.md` → Gün 11 logu + **Haftalık Kontrol tablosunda Hafta 2 satırı** (mini sınav puanı + bir cümle not) → `docs: day 11 progress log`.

---

## ✅ Gün Sonu

- [ ] Isınma (5 soru, chat'e)
- [ ] Tekrar 30 dk
- [ ] Sınav: SINAV.md A + B + C2 test tablosu, `Exam1`–`Exam3` + C1 class'ları
- [ ] Not verildikten sonra hataları birlikte düzelt
- [ ] "Kağıda Yaz" kutusu
- [ ] Commit + push, `ILERLEME.md` Gün 11 + Hafta 2 satırı

**Sıradaki gün (Gün 12):** `equals` / `hashCode` / `toString` (Object'in üç metodu — neden `==` nesnelerde yetmez) + Hafta 2 projesi **"Kütüphane Sistemi"**.
