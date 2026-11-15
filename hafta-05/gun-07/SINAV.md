# Kapı Sınavı — Gün 31 (Hafta 1–4)

Başlangıç saati:
Bitiş saati:

Kapalı kitap. Not, eski kod, internet, AI yok. Sadece `Cevap:` satırlarını doldur.
Exception fırlatan satır için exception'ın **adını** yaz.

---

## Bölüm A — Teori (her biri 1–3 cümle) · 20 puan

**A1. (2p — Temeller)** `javac Main.java` ne üretir? Onu kim çalıştırır? JDK ile JVM arasındaki fark ne?
Cevap: .class dosyası üretir. onu da jvm çalıştırır. jvm bytecode'u çalıştıran makinedir, jdk ise jvm'i ve jre'i içerir, kod yazmak ve çalıştırmak için gerekli her şeyi barındırır.

**A2. (2p — Temeller)** `git add`, `git commit`, `git push` — her biri ne yapar (tek cümle)?
Cevap: git add eklenecek dosyaları commit'e gitmesi için bir alana alır commit ile dosyalar staginig area'ya alınır ve push ile uzak sunucuya gönderilir.

**A3. (3p — OOP)** `abstract class` ile `interface` arasında 2 fark yaz. Bir sınıf kaç tane `extends`, kaç tane `implements` yapabilir?
Cevap: abstract classdan yeni bir sınıf yaratılamaz, constructori field, normal metot ve abstract metot, refereans tipine sahiptir. interface ise bir yetenek sözleşmesidir. metotlar otomatik olarak public abstract fieldlar public static final. bir sınıfta en fazla bir extends olabilir implementsin sınırı yoktur.

**A4. (3p — OOP)** Aşağıdaki sınıf `equals`'ı override ediyor ama `hashCode`'u etmiyor. Son satır büyük ihtimalle ne yazdırır, neden? Nasıl düzeltilir?
```java
class Tag {
    String name;
    Tag(String name) { this.name = name; }
    @Override public boolean equals(Object o) {
        return o instanceof Tag t && t.name.equals(name);
    }
}
Set<Tag> tags = new HashSet<>();
tags.add(new Tag("java"));
tags.add(new Tag("java"));
System.out.println(tags.size());
```
Cevap: büyük ihtimalle 2 yazdırır çünkü içerikleri aynı olsa bile hashCode override edilmediği için üretilecek hashCode'ları farklıdır ve set'de farklı kutulara gider bu yüzden eşit olduğu bulunamaz ve set'e eklenir.

**A5. (3p — Exceptions)** Checked ile unchecked exception farkı ne? Her birinden 2 örnek isim yaz. `throw` ile `throws` farkı ne?
Cevap: checked exceptions derleyiciyi zorlar catch etmen veya metot imzasına throws yazman gerekir. unchecked exceptions ise muhtemelen kod yazımında yapılan hatalardan kaynaklanır. unchecked: runtimeExceptions checked: kalan bütün exceptions (IOE vb.)

**A6. (3p — Collections)** `HashMap`, `TreeMap`, `LinkedHashMap` — her birinde for-each ile dönünce key'ler hangi sırayla gelir?
Cevap: hashmap'de sırasız, treemap'de alfabetik sıralı, linkedhashmap'de eklenme sırası ile

**A7. (2p — Comparator)** `Comparable` ile `Comparator` farkı ne (hangi metot, nerede yazılır)? `compare(a, b)` negatif dönerse `sort` ne anlar?
Cevap: comparator dışarıdan hakem gibidir. comparable o sınıfın sıralamasını belirlemek içindir. compare(a,b) negatif gelirse a b'den önce demektir.

**A8. (2p — Lambda)** Bu kod neden derlenmez? Aynı toplamı veren tek satırlık stream'i yaz.
```java
List<Integer> nums = List.of(4, 5, 6);
int total = 0;
nums.forEach(n -> total = total + n);
```
Cevap: List.of() değiştirilemez bir liste üretir o yüzden derlenmez. nums.stream().mapToInt(n -> n).sum();

---

## Bölüm B — Çıktı tahmini (ÇALIŞTIRMADAN) · 30 puan (her blok 5p)

**B1. (Temeller)**
```java
static void change(int[] a, int x) {
    a[0] = 99;
    x = 99;
    a = new int[]{7};
    a[0] = 5;
}

int i = 3;
int a = i++ * 2 + i;
System.out.println(a + " " + i);
System.out.println(7 / 2 + " " + 7 % 3 + " " + 7 / 2.0);
String s = "abc";
s.toUpperCase();
System.out.println(s + s.length());
int[] arr = {1, 2};
int n = 1;
change(arr, n);
System.out.println(arr[0] + " " + n);
System.out.println("a" + 1 + 2 + " " + (1 + 2) + "a");
```
Cevap: 10
3 1 3.5
abc3
5 1
a12 3a

**B2. (OOP)**
```java
abstract class Animal {
    static int count = 0;
    protected String name;
    Animal(String name) { this.name = name; count++; }
    abstract String sound();
    public String toString() { return name + " says " + sound(); }
}
class Dog extends Animal {
    Dog(String n) { super(n); }
    String sound() { return "Woof"; }
}
class Puppy extends Dog {
    Puppy(String n) { super(n); }
    @Override String sound() { return "Yip"; }
}

Animal a = new Puppy("Rex");
Dog d = new Dog("Max");
System.out.println(a);
System.out.println(d);
System.out.println(Animal.count);
System.out.println(a instanceof Dog);
Object o = d;
Puppy p = (Puppy) o;
```
Cevap: Yip
Woof
2
true
ClassCastException

**B3. (Exceptions)**
```java
static int parse(String s) {
    try {
        System.out.println("try " + s);
        return Integer.parseInt(s);
    } catch (NumberFormatException e) {
        System.out.println("bad");
        return -1;
    } finally {
        System.out.println("finally");
    }
}

System.out.println(parse("12"));
System.out.println(parse("x"));
List<Integer> list = List.of(1, 2, 3);
list.add(4);
```
Cevap: try 12
finally
try x
bad
finally
UOE

**B4. (Collections — dönüş değerleri)**
```java
List<Integer> list = new ArrayList<>(List.of(10, 20, 30, 20));
System.out.println(list.remove(1));
System.out.println(list.remove(Integer.valueOf(20)));
System.out.println(list);
Map<String, Integer> m = new HashMap<>();
System.out.println(m.put("a", 1));
System.out.println(m.put("a", 5));
m.merge("a", 2, Integer::sum);
System.out.println(m.get("a") + " " + m.get("b") + " " + m.getOrDefault("b", 0));
Set<String> set = new TreeSet<>(List.of("pear", "Apple", "fig"));
System.out.println(set.add("fig") + " " + set);
```
Cevap: 20
true
[10, 30]
null
1
7 null 0
false (pear, Apple, fig)
not: buradaki .merge metodunu daha önce göstermemiştin bana buna internetten bakmak zorunda kaldım.

**B5. (Comparator + Iterator)**
```java
List<String> w = new ArrayList<>(List.of("bb", "a", "ccc", "dd"));
w.sort(Comparator.comparing(String::length).thenComparing(Comparator.reverseOrder()));
System.out.println(w);
System.out.println(Integer.compare(3, 7) < 0);
Iterator<String> it = w.iterator();
it.next();
it.remove();
System.out.println(w + " " + it.next());
for (String x : w) {
    if (x.length() == 2) w.remove(x);
}
```
Cevap: [ccc, dd, bb, a]
true
[dd, bb, a] dd
CME

**B6. (Stream + Optional)**
```java
List<Integer> nums = List.of(4, 1, 3, 1, 5);
Stream<Integer> st = nums.stream().peek(x -> System.out.println("see " + x)).filter(x -> x > 2);
System.out.println("start");
System.out.println(nums.stream().filter(x -> x > 2).map(x -> x * 10).toList());
System.out.println(nums.stream().distinct().sorted().toList());
Optional<Integer> first = nums.stream().filter(x -> x > 4).findFirst();
System.out.println(first + " " + first.map(x -> x + 1));
System.out.println(nums.stream().filter(x -> x > 9).findFirst().orElse(0));
Map<Integer, Long> g = nums.stream().collect(Collectors.groupingBy(x -> x % 2, Collectors.counting()));
System.out.println(new TreeMap<>(g));
```
Cevap: start
[40, 30, 50]
[1, 3, 4, 5]
Optional[5] Optional[6]
0
{0=1, 1=4}

---

Bölüm C (kod, 30p) ve Bölüm D (algoritma, 20p) → `DERS.md`'de, kodlar package `w5d7`.
