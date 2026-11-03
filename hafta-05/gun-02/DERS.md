# Hafta 5 · Gün 2 (Gün 26) — TEKRAR: Map + Set

**Hedef:** Yeni konu yok. Hafta 3'teki `HashMap` / `HashSet` / `TreeMap` / `equals`-`hashCode` konularını **notsuz yazabilir** hâle getirmek.
Kapı sınavında (Gün 31) bugünkü konular ~15 puan.

**Süre:** ~5-6 saat
Isınma 25 dk · Konu özeti 30 dk · Çıktı tahmini 20 dk · Notsuz kod görevi 1.5–2 sa · Algoritma 1 sa · Kapanış 15 dk

**Kurallar (değişmedi):**
- Sınıf, metot, değişken ve mesajlar **İngilizce**
- **Değişiklik → derle → çalıştır → çıktının her satırını oku.**
- **Metot adı/imzası görevde nasılsa öyle.** Exception mesajları **birebir**.
- `main`'e test yazmak yok. Testleri ben yazıp çalıştırıyorum.

**Kurulum:** `hafta-05/gun-02/` klasöründe package **`w5d2`** (`hafta-05/gun-02/w5d2/`). `gun-02`'yi Sources Root yap.

---

## 0. Isınma (25 dk)

### 0.1 Yazdırma kuralları (dün kaçanlar, yeni değerlerle) — çıktıyı chat'e yaz

**S1.**
```java
Optional<String> a = Optional.of("Bora");
Optional<String> b = Optional.empty();
System.out.println(a);
System.out.println(b);
System.out.println(a.map(s -> s + "!"));
System.out.println(b.orElse("none"));
```

**S2.**
```java
System.out.println(LocalDate.of(2026, 11, 3).plusDays(30));
```

**S3.**
```java
record Item(String name, int qty) {}
System.out.println(new Item("pen", 4));
System.out.println(List.of(new Item("a", 1), new Item("b", 2)));
```

### 0.2 LC 88 — notsuz yeniden yaz (bilinçli tekrar, Gün 25 A2)

Dün çözdük. Bugün **dünkü koda bakmadan**, iki pointer (sondan doldurma) versiyonunu sıfırdan yaz ve LeetCode'a submit et. Accepted alınca "88 tamam" yaz, yeter.

---

## 1. Konu Özeti (30 dk) — oku, takıldığın yeri chat'e sor

### 1.1 Map — dönüş değerleri (List'teki gibi, en çok kaçan yer)

```
map.put(k, v)            → ESKİ değer (k yoksa null)        ← set() gibi
map.get(k)               → değer (k yoksa null, exception YOK)
map.getOrDefault(k, d)   → değer (k yoksa d)
map.containsKey(k)       → boolean   (hızlı: hashCode ile bakar)
map.containsValue(v)     → boolean   (yavaş: tüm değerleri tek tek gezer)
map.remove(k)            → silinen değer (k yoksa null)
map.size()               → int
map.keySet()             → Set<K>
map.values()             → Collection<V>
map.entrySet()           → Set<Map.Entry<K, V>>  → e.getKey(), e.getValue()
```

Aynı key'e ikinci kez `put` → **üstüne yazar**, size artmaz.

### 1.2 `get` → `null` tuzağı (unboxing)

```java
Map<String, Integer> m = new HashMap<>();
Integer x = m.get("a");   // null — sorun yok
int y = m.get("a");       // NullPointerException! null'ı int'e açamaz
int z = m.getOrDefault("a", 0);  // 0 — güvenli yol
```

### 1.3 Sayma kalıbı (ezber)

```java
Map<String, Integer> counts = new HashMap<>();
for (String w : words) {
    counts.put(w, counts.getOrDefault(w, 0) + 1);
}
```

### 1.4 Gruplama kalıbı (key → liste)

```java
Map<String, List<String>> byCity = new TreeMap<>();
for (Person p : people) {
    if (!byCity.containsKey(p.city())) {
        byCity.put(p.city(), new ArrayList<>());
    }
    byCity.get(p.city()).add(p.name());   // get listeyi döndürür, add o listeye ekler
}
```

### 1.5 Entry ile gezme

```java
for (Map.Entry<String, Integer> e : counts.entrySet()) {
    System.out.println(e.getKey() + ": " + e.getValue());
}
```

### 1.6 Hangi Map / Set? → sadece SIRA farkı

```
HashMap / HashSet               → sıra YOK (garanti yok, "rastgele gibi")
LinkedHashMap / LinkedHashSet   → EKLEME sırası
TreeMap / TreeSet               → SIRALI (String alfabetik, sayı küçükten büyüğe)
```

Testte sıra önemliyse `HashMap` kullanma. `new TreeMap<>(hashMap)` → aynı içerik, sıralı kopya.

### 1.7 Set

```
set.add(x)       → boolean (zaten varsa false, eklemez)
set.remove(x)    → boolean
set.contains(x)  → boolean (hızlı)
```

Set'te **index yok**: `set.get(0)` diye bir şey yok.

### 1.8 `equals` + `hashCode` — HashSet/HashMap nasıl bakar?

1. Önce `hashCode()` → hangi kutuya bakacağını bulur.
2. Sonra o kutudaki elemanlarla `equals()` → gerçekten aynı mı?

- `equals`'ı yazıp `hashCode`'u yazmazsan: iki "eşit" nesne farklı kutulara düşer → Set ikisini de tutar.
- Aynı `hashCode` = aynı nesne **demek değil** (çakışma olabilir) — o yüzden `equals` da var.
- **`record`** `equals` + `hashCode` + `toString`'i **otomatik** yazar (bütün alanlarla).

Elle yazma şablonu:
```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Seat)) return false;
    Seat other = (Seat) o;
    return row == other.row && number == other.number;
}

@Override
public int hashCode() {
    return Objects.hash(row, number);
}
```

### 1.9 Değiştirilemezler

```java
Map.of("a", 1, "b", 2)   // değiştirilemez → put/remove → UnsupportedOperationException
Set.of("x", "y")         // değiştirilemez
Set.copyOf(set) / Map.copyOf(map)  // değiştirilemez KOPYA
```

**Ek bilgi (yeni, kullanmak serbest, şart değil):** `a.addAll(b)` birleşim, `a.retainAll(b)` kesişim, `a.removeAll(b)` fark — üçü de `a`'yı **değiştirir**. Kopya üzerinde çalış: `Set<String> r = new TreeSet<>(a); r.retainAll(b);`

### ✍️ Kağıda Yaz

```
1. put → ESKİ değer (yoksa null) · get → yoksa null · remove(k) → silinen değer
2. int x = map.get(yok) → NPE · getOrDefault(k, 0) güvenli
3. Sayma: map.put(k, map.getOrDefault(k, 0) + 1)
4. Hash → sırasız · Linked → ekleme sırası · Tree → sıralı
5. set.add → boolean (varsa false) · Set'te index yok
6. HashSet: önce hashCode (kutu), sonra equals · ikisini birlikte yaz · record otomatik yazar
7. Map.of / Set.of → değiştirilemez → UnsupportedOperationException
```

---

## 2. Çıktı Tahmini (20 dk) — ÇALIŞTIRMADAN, cevaplar chat'e

Her snippet `main` içinde. Exception çıkarsa **hangi exception** olduğunu yaz, sonrasında yazdırılmayan satırları yazma.

**P1.**
```java
Map<String, Integer> m = new HashMap<>();
System.out.println(m.put("a", 1));
System.out.println(m.put("a", 5));
System.out.println(m.get("a"));
System.out.println(m.size());
```

**P2.**
```java
Map<String, Integer> m = new HashMap<>();
m.put("x", 3);
System.out.println(m.remove("x"));
System.out.println(m.remove("x"));
System.out.println(m.getOrDefault("x", -1));
int v = m.get("x");
System.out.println(v);
```

**P3.**
```java
Set<String> s = new HashSet<>();
System.out.println(s.add("java"));
System.out.println(s.add("java"));
System.out.println(s.add("Java"));
System.out.println(s.size());
```

**P4.**
```java
Map<String, Integer> t = new TreeMap<>();
t.put("pear", 2);
t.put("apple", 7);
t.put("fig", 1);
System.out.println(t);
System.out.println(t.keySet());
System.out.println(t.values());
```

**P5.**
```java
Set<Integer> a = new TreeSet<>(List.of(5, 1, 5, 3, 1));
System.out.println(a);
System.out.println(a.size());
Set<String> b = new LinkedHashSet<>(List.of("c", "a", "c", "b"));
System.out.println(b);
```

**P6.** `Box` sınıfında **sadece `equals`** yazılmış (value'ya göre), `hashCode` yok.
```java
class Box {
    int value;
    Box(int value) { this.value = value; }
    @Override
    public boolean equals(Object o) {
        return o instanceof Box && ((Box) o).value == value;
    }
}
// main:
Box x = new Box(1);
Box y = new Box(1);
System.out.println(x.equals(y));
Set<Box> set = new HashSet<>();
set.add(x);
set.add(y);
System.out.println(set.size());
```

**P7.**
```java
record Tag(String name) {}
Set<Tag> tags = new HashSet<>();
tags.add(new Tag("a"));
tags.add(new Tag("a"));
System.out.println(tags.size());
System.out.println(tags.contains(new Tag("a")));
```

**P8.**
```java
String text = "to be or not to be";
Map<String, Integer> c = new TreeMap<>();
for (String w : text.split(" ")) {
    c.put(w, c.getOrDefault(w, 0) + 1);
}
System.out.println(c);
```

**P9.**
```java
Map<String, Integer> m = Map.of("a", 1, "b", 2);
System.out.println(m.get("b"));
System.out.println(m.containsKey("c"));
m.put("c", 3);
System.out.println(m);
```

---

## 3. Notsuz Kod Görevi (1.5–2 sa) — Etkinlik Katılımı

**Kural:** DERS dosyalarına (bu dosyanın özet kısmı dahil), eski koduna, internete bakmak **yok**. AI kapalı. Bu bölümün **görev metni** açık kalabilir. Takılırsan önce 10 dk kendin dene, sonra chat'e sor.

Her public tip kendi dosyasında, package `w5d2`. Döngü serbest (stream de serbest ama şart değil).

### 3.1 `record Visit(String person, String event)` (`Visit.java`)
- **Compact constructor**, kontroller bu sırayla:
  - `person == null` ya da `person.isBlank()` → `IllegalArgumentException("Person must not be blank")`
  - `event == null` ya da `event.isBlank()` → `IllegalArgumentException("Event must not be blank")`
- Başka metot yok.

### 3.2 `class AttendanceLog` (`AttendanceLog.java`)

**Field:**
- `private final List<Visit> visits = new ArrayList<>();`

**Constructor:** yok (varsayılan yeterli).

**Metotlar:**
- `public void add(Visit visit)`
  - `visit == null` → `IllegalArgumentException("Visit must not be null")`
  - Değilse sona ekle. **Aynı ziyaret iki kez eklenebilir** (aynı kişi aynı etkinliğe iki kez gelmiş olabilir), engelleme.
- `public int size()` → kaç ziyaret kaydı var.
- `public Map<String, Integer> visitsPerPerson()` → her kişi → kaç ziyaret kaydı. **Alfabetik sıralı** Map döndür.
- `public Set<String> attendeesOf(String event)` → o etkinliğe gelen kişiler, **tekrarsız**, **listede ilk görüldükleri sırayla**. Etkinlik yoksa boş set.
- `public Map<String, Set<String>> eventsByPerson()` → her kişi → gittiği etkinlikler (tekrarsız). Hem kişiler hem her kişinin etkinlikleri **alfabetik sıralı**.
- `public Set<String> commonAttendees(String event1, String event2)` → **iki** etkinliğe de gelen kişiler, **alfabetik sıralı**.
- `public Set<String> onlyIn(String event1, String event2)` → `event1`'e gelip `event2`'ye **gelmeyen** kişiler, **alfabetik sıralı**.
- `public Optional<String> mostActive()` → en çok ziyaret kaydı olan kişi. Eşitlikte **alfabetik olarak önce gelen**. Hiç kayıt yoksa `Optional.empty()`.
- `public Map<Integer, List<String>> peopleByVisitCount()` → ziyaret sayısı → o sayıda ziyareti olan kişiler. Key'ler **küçükten büyüğe**, her listedeki isimler **alfabetik**.
  - Örnek: Ada 2, Bora 1, Can 2 → `{1=[Bora], 2=[Ada, Can]}`

### 3.3 `final class Seat` (`Seat.java`) — record DEĞİL, elle yaz

**Field'lar:**
- `private final char row;`
- `private final int number;`

**Constructor:**
- `public Seat(char row, int number)`
  - `row` `'A'..'Z'` dışındaysa → `IllegalArgumentException("Bad row: " + row)`
  - `number < 1` → `IllegalArgumentException("Bad number: " + number)`

**Metotlar:**
- `public char getRow()`, `public int getNumber()`
- `equals` → aynı `row` **ve** aynı `number` ise eşit.
- `hashCode` → `equals` ile uyumlu.
- `toString` → `row` + `number` yan yana: `new Seat('B', 12)` → `B12`

### 3.4 `class Venue` (`Venue.java`)

**Field'lar:**
- `private final Set<Seat> booked = new HashSet<>();`
- `private final Map<Seat, String> owners = new HashMap<>();` (koltuk → rezervasyon yapan kişi)

**Metotlar:**
- `public boolean book(Seat seat, String person)`
  - `seat == null` → `IllegalArgumentException("Seat must not be null")`
  - Koltuk zaten doluysa → `false` döndür, hiçbir şeyi değiştirme.
  - Değilse rezerve et, sahibini kaydet, `true` döndür.
- `public boolean cancel(Seat seat)` → doluysa boşalt (iki yapıdan da sil), `true`; boşsa `false`.
- `public boolean isBooked(Seat seat)`
- `public Optional<String> ownerOf(Seat seat)` → sahibi; boşsa `Optional.empty()`.
- `public int bookedCount()`
- `public Set<Seat> seatsOf(String person)` → o kişinin koltukları, **değiştirilemez** set (sıra önemli değil).

**Bitince:** "bitti" yaz, dosyaları okuyup testlerimi çalıştıracağım.

---

## 4. Algoritma (1 sa) — HashMap / HashSet

**Yöntem:**
1. Örnek girdiyi **elle** çöz, her adımda Map/Set'in hâlini chat'e yaz.
2. Ben "tamam" deyince kodla.
3. LeetCode'a submit et.

### A1 — LeetCode 219: Contains Duplicate II (Easy)
`nums` içinde `nums[i] == nums[j]` ve `|i - j| <= k` olan iki **farklı** index var mı?

**Elle çöz:** `nums = [1, 2, 3, 1, 2, 3]`, `k = 2` (cevap `false`).
Map: **sayı → en son görüldüğü index**. `i = 0`'dan `5`'e kadar her adımda yaz: `nums[i]`, map'te var mı, varsa `i - eskiIndex` kaç, map'in son hâli.

### A2 — LeetCode 290: Word Pattern (Easy)
`pattern = "abba"`, `s = "dog cat cat dog"` → `true`. Her harf tek bir kelimeye, her kelime tek bir harfe karşılık gelmeli.

**Elle çöz:** iki örnek:
- `pattern = "abba"`, `s = "dog cat cat fish"` → `false`
- `pattern = "abba"`, `s = "dog dog dog dog"` → `false`

İkinci örnekte sadece **harf → kelime** map'i tutarsan ne olur? `i = 0..3` her adımda map'i yaz ve nerede yakalaman gerektiğini söyle.

---

## 5. Kapanış (15 dk)

- Isınma + tahmin sonuçlarından **en çok yanlış yaptığın 1 konuyu** chat'e yaz.
- `ILERLEME.md`'ye Gün 26 satırı (satırları ben hazırlayacağım).
- Commit + push:
  ```
  git add hafta-05
  git commit -m "feat: day 26 - review map + set"
  git push
  ```
