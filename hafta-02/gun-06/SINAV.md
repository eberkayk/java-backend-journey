# Hafta 2 Mini Sınav — Gün 11

Başlangıç saati:
Bitiş saati:

## Bölüm A — Teori (her biri 1–3 cümle)

1. Class ile object farkı nedir? `Student s;` satırı çalıştığında kaç nesne oluşur?
Cevap: Class bir blueprint gibidir. oluşturulacak nesnelerin özelliklerini yeteneklerini belirtir. nesne ise o sınıftan o kurallara uyan özelliklerini taşıyan üretilel şeylerdir. Student s ile nesne oluşmaz. new kelimesi yoksa yeni bir nesne yoktur.

2. `int x; System.out.println(x);` — `x` bir metodun içindeyse ne olur? Class'ın field'ı olsaydı ne yazdırırdı? Farkın sebebi?
Cevap: bir metodun içindeyse hata verir. çünkü local variable'lar instantiate edilmelidir varsayılan değerleri yoktur. class field'ı olsaydı varsayılan değeri olacağı için kod çalışırdı ve x'i yazdırırdi.

3. `private` erişim class seviyesinde mi, nesne seviyesinde mi? `BankAccount`'ın `transfer(BankAccount other, ...)` metodu içinde `other.balance` yazılabilir mi?
Cevap: private erişim class seviyesinde. yazılabilir. aynı sınıftan private field'lara erişilebilir çünkü.

4. Encapsulation nedir? Alanı `public` yapmak yerine neden doğrulama yapan bir metot kullanırız?
Cevap: encapsulation verilerin doğru olmasını sağlamak için kullanılan bir yöntemdir. bir alan public olursa istenmeyen bir şekilde değiştirilebilir. bu değişikliği bir metot içinde kurallara tabi tutarak doğruluğunu sağlarız.

5. `static` field ile instance field farkı nedir? `static` bir metodun içinde `this` kullanılabilir mi, neden?
Cevap: static field class'a aittir. instance field nesneye aittir. kullanılamaz çünkü static metotlar static olmayan variable'lara nesnelere erişemezler.

6. Alt class'ın constructor'ında `super(...)` ne işe yarar? Parent class'ta **parametresiz** constructor yoksa ve alt class `super(...)` yazmazsa ne olur?
Cevap: üst sınıfın constructor'ını çağırır. alt class'da super(...) yoksa gizli bir super() yazar fakat üst sınıfta da parametresiz bir contructor yoksa burada derleme zamanında bir hata verir.

7. Overloading ve overriding farkı nedir? Hangisi derleme anında, hangisi çalışma anında seçilir?
Cevap: overloading derleme anında seçilir aynı metot adı farklı parametrelerle oluşur. overriding çalışma anında seçilir aynı metot imzası ile olur genelde alt sınıfların üst sınıfların metotlarını kendi işleyişlerinin kurallarına göre değiştirmesinde kullanılır.

8. `Animal a = new Dog();` — referans tipi ne, nesne tipi ne? `bark()` sadece `Dog`'da varsa `a.bark()` derlenir mi? Derlenmesi için ne yazarsın?
Cevap: refreans tipi Animal nesne tipi Dog. derlenmez çünkü önce referans tipinde o metot var mı diye bakılır eğer varsa nesne tipine göre hangisinin çağırılacağı seçilir. derlenmesi için Animal sınıfına bark() metodu yazılır.

9. Bir class kaç class'ı `extends`, kaç interface'i `implements` edebilir?
Cevap: bir class en fazla bir class'ı extends edebilir birden fazla interface'i implements edebilir.

10. `default` metot nedir? Interface'e neden eklendi?
Cevap: interface'de bulunan metotları bütün alt sınıflar implemente etmek zorundadır. fakat bütün alt sınıflar oluşturulduktan sonra yeni bir metot eklenmek istenirse hepsine, bunu interface'e default metot olarak ekleyebiliriz, bu durumda alt sınıflar implemente etmek zorunda kalmaz veya bütün alt sınıflara kod eklemesi yapılmak zorunda kalmaz.

11. `final` bir field, `final` bir metot ve `final` bir class ne anlama gelir?
Cevap: final bir field bir kere atandıktan sonra tekrar atanamayan değişkendir. final bir metot bilmiyorum. final bir class herhangi bir sınıfın üst sınıfı olamaz.

12. Kodda uzun bir `if (x instanceof A) ... else if (x instanceof B) ...` zinciri neyin işaretidir? Yerine ne yaparsın?
Cevap: override eksikliğidir. eksik olan sınıflarda metot override edilir.

## Bölüm B — Çıktı tahmini (ÇALIŞTIRMADAN)

Her soruda: ekrana ne yazar? Derleme hatası veya çalışma anı hatası varsa **hangisi** olduğunu ve **hangi satırda** olduğunu yaz.

### B1
```java
class Counter {
    static int total = 0;
    int mine = 0;
    void inc() { total++; mine++; }
}
// main:
Counter a = new Counter();
Counter b = new Counter();
a.inc(); a.inc(); b.inc();
System.out.println(a.mine + " " + b.mine + " " + Counter.total);
```
Cevap:2 1 3

### B2
```java
class A { A() { System.out.print("A "); } }
class B extends A { B() { System.out.print("B "); } }
class C extends B { C() { super(); System.out.print("C "); } }
// main:
new C();
```
Cevap:A B C 

### B3
```java
class Animal {
    String sound() { return "..."; }
    String speak() { return "I say " + sound(); }
}
class Cat extends Animal {
    @Override String sound() { return "Meow"; }
}
// main içinde, Main class'ında:
static void show(Animal a) { System.out.println("animal: " + a.speak()); }
static void show(Cat c)    { System.out.println("cat: " + c.speak()); }

Animal y = new Cat();
show(y);
show((Cat) y);
```
Cevap:animal: I say Meow
cat: I say Meow

### B4
```java
class Box {
    int v;
    Box(int v) { this.v = v; }
}
// Main class'ında:
static void change(Box b) {
    b.v = 10;
    b = new Box(20);
    b.v = 30;
}
// main:
Box p = new Box(1);
change(p);
System.out.println(p.v);
```
Cevap:10

### B5
```java
// B3'teki Animal ve Cat
Animal z = new Animal();
Cat c = (Cat) z;
System.out.println("done");
```
Cevap:downcast error verir

## Bölüm C — C2 test tablosu (kodlamadan ÖNCE)
{1, 4, 7}` + `{2, 4, 9, 10}` → `{1, 2, 4, 4, 7, 9, 10}

| girdi a | girdi b | beklenen | neyi kontrol ediyor |
|---|---|---|---|
| 1,2,3| 2,3,4| 1,2,2,3,3,4| hepsi pozitif ve tekrar edenleri|
|-6,-3,-2| -8,-7,-5| -8,-7,-6,-5,-3,-2| negatifleri |
| 2| 4| 2,4| tek elemanlıları |
| {}| 4| 4| boş diziyi|
