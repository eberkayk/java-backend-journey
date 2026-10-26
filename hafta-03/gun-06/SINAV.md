# Hafta 3 Mini Sınav — Gün 18

Başlangıç saati:
Bitiş saati:

## Bölüm A — Teori (her biri 1–3 cümle)

1. Checked ve unchecked exception farkı nedir? Birer örnek ver. Aşağıdaki metot derlenir mi, derlenmezse neden?
```java
void load() {
    throw new java.io.IOException("disk error");
}
```
Cevap: unchecked exception'lar runtimeexception'lardır. chechked exceptionlar ise onun dışında kalan diğer bütün exceptionlardır. unchecked exceptionlar kodun yanlış yazılmasından kaynaklanır. checked exceptionslar kod doğru olsa bile oluşabilir dış kaynaklara bağlıdır.
derlenmez çünkü IOException checked exceptiondur sadece throw new ile fırlatılamaz

2. `throw` ile `throws` farkı nedir? Hangisi metodun içinde, hangisi imzasında yazılır?
Cevap: throws metodun imzasında olur bu metodun bir exception fırlatabileceğini gösterir. throw metodun içinde hatanın fırlatılması gereken yerde olur hatayı fırlatır.

3. Kendi exception class'ımızı neden genelde `RuntimeException`'dan türetiriz?
Cevap: çünkü kendi yazdığımız exceptionlar checked exception olur.

4. `ArrayList`'te `get(i)` neden hızlı (O(1)), ama `add(0, x)` (başa ekleme) neden yavaş (O(n))?
Cevap: hashCode ile elemanın nerede olduğu bilinir ve direkt ulaşılabilir bu yüzden get hızlıdır. fakat add yapıldığında eklenecek yere direkt ulaşılsa da ekleme yapıldıktan sonra sağında kalan bütün elemanlar tek tek sağa kaydırılması gerekir bu yüzden yavaştır.

5. Şu üç durumda `List`, `Set`, `Map`'ten hangisini seçersin? (her biri tek kelime + yarım cümle sebep)
   (a) Bir metinde her kelimenin kaç kez geçtiğini saymak
   (b) Bir e-postanın daha önce kayıt olup olmadığını hızlıca kontrol etmek
   (c) Kullanıcının girdiği komutları girdiği sırayla saklamak
Cevap: a) map, kelimeyi anahtar kaç kere geçtiğini de değer olarak saklarım. b) set, çünkü duplicate kayıtlara izin vermez c) list, ekleme her zaman sondan yapılır ve O(1)'dir.

6. `HashMap.get(key)` içeride nasıl çalışır (`hashCode` ve `equals` hangi adımda kullanılır)? Bir class'ta `equals`'ı override edip `hashCode`'u etmezsen, o class'ı `HashSet`'e koyunca ne bozulur?
Cevap: hashCode ile elemanın bulunduğu kutuya gidilir, equals ile o kutudaki elemanın aranan eleman olup olmadığına bakılır. equals'ı override edip hashCode'u etmezsen, aynı iki nesne farklı hashCode'lar üreteceği için farklı kutulara gider ve bu da HashSet'in amacına ters olur. 

7. `List<int>` neden yazılamaz? Yerine ne yazılır?
Cevap: <> generic tip ister. int yerine Integer wrapper class'ı yazılır

8. `Comparable` ile `Comparator` farkı nedir? Metot adları ve parametre sayıları ne? Hangi durumda hangisini seçersin?
Cevap: comparable sınıfa yazılır doğal sıralamayı belirtmek için. comparator ise dışarıdan verilen bir hakem gibidir istenildiği kadar, istenilen parametreyle yazılabilir. comparable'ın metodu compareTo'dur comparator'un metodu compare'dir. kitapları örneğin yazarına göre doğal şekilde sıralamak için comparable kullanırız. farklı bir sıralama istediğimizde sınıfın içine erişip yeni bir metot yazamayacağımızdan comparator ile farklı parametreler verip farklı sıralamalar elde edebiliriz.

9. Aşağıdaki `compareTo` neden tehlikeli? Yerine ne yazarsın?
```java
public int compareTo(Player other) {
    return this.score - other.score;   // score bir int
}
```
Cevap: taşma olabilir o yüzden tehlikeli Integer.compareTo(score, other.score) yazılabilir

10. Bir for-each döngüsünün içinde `list.remove(x)` yapınca neden `ConcurrentModificationException` alınır? Gezerken güvenli silmenin iki yolu ne?
Cevap: çünkü for-each döngüsü listenin değişmeyeceğine güvenerek çalışır eğer çalışırken liste değişirse CME verir. güvenli silmek için iterator'u açıkca yazıp while içinde it.remove(x) kullanmak gerekir.

## Bölüm B — Çıktı tahmini (ÇALIŞTIRMADAN)

Her soruda: ekrana ne yazar? Derleme hatası veya çalışma anı hatası varsa **hangisi** olduğunu ve **hangi satırda** olduğunu yaz (satırı kopyalaman yeter). Hatadan önce yazılanları da yaz.

### B1
```java
List<Integer> nums = new ArrayList<>(List.of(5, 10, 15, 20));
nums.remove(1);
boolean r = nums.remove(Integer.valueOf(20));
System.out.println(nums + " " + r + " " + nums.indexOf(99));
```
Cevap:5, 15 true null

### B2
```java
Map<String, Integer> m = new HashMap<>();
System.out.println(m.put("a", 1));
System.out.println(m.put("a", 5));
System.out.println(m.get("b"));
System.out.println(m.getOrDefault("b", 0) + m.get("a"));
Set<String> s = new HashSet<>();
System.out.println(s.add("x") + " " + s.add("x") + " " + s.size());
```
Cevap:true
1
null
5
true false 1

### B3
```java
static int parse(String s) {
    try {
        System.out.print("A ");
        int n = Integer.parseInt(s);
        System.out.print("B ");
        return n;
    } catch (NumberFormatException e) {
        System.out.print("C ");
        return -1;
    } finally {
        System.out.print("D ");
    }
}
// main:
System.out.println(parse("7"));
System.out.println(parse("x"));
```
Cevap:A B D
A C D

### B4
```java
List<String> names = new ArrayList<>(List.of("bora", "Ali", "ce", "Deniz"));
names.sort(null);
System.out.println(names);
names.sort(Comparator.comparing(String::length).reversed());
System.out.println(names);
List<String> fixed = List.copyOf(names);
fixed.sort(null);
System.out.println(fixed);
```
Cevap:Ali, Deniz, bora, ce
Deniz, bora, Ali, ce
UnsporttedOperationException: fixed.sort(null);

### B5
```java
List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6));
Iterator<Integer> it = list.iterator();
while (it.hasNext()) {
    if (it.next() % 2 == 0) {
        it.remove();
    }
}
System.out.println(list);
for (Integer x : list) {
    if (x == 1) {
        list.remove(x);
    }
}
System.out.println(list);
```
Cevap:1, 3, 5
ConcurrentModificationException: list.remove(x);

### B6
```java
Map<String, Integer> stock = new HashMap<>();
stock.put("pen", 3);
int a = stock.get("pen");
int b = stock.get("ink");
System.out.println(a + b);
```
Cevap: 3 + null olacak ama 3 ile null toplanamaz hata verir sanırım. tam emin değilim.
