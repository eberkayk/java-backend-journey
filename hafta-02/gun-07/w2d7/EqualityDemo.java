package w2d7;

public class EqualityDemo {
    public static void main(String[] args) {
        Book b1 = new Book("978-0441", "Dune", "Frank Herbert");
        Book b2 = new Book("978-0441", "Dune", "Frank Herbert");
        Book b3 = b1;
        Book b4 = new Book("978-0441", "Dune (2nd ed.)", "Frank Herbert");

        System.out.println(b1==b2);
        System.out.println(b1.equals(b2));
        System.out.println(b1==b3);
        System.out.println(b1.equals(b3));
        System.out.println(b1.equals(b4));
        System.out.println(b1.equals(null));
        System.out.println(b1.equals("978-0441"));


        BadBook x = new BadBook("111", "A", "B");
        BadBook y = new BadBook("111", "A", "B");
        Object  z = y;

        System.out.println(x.equals(y));
        System.out.println(x.equals(z));

        System.out.println(b1);
        System.out.println(b2);
        System.out.println(Integer.toHexString(b1.hashCode()));


        java.util.Set<Book> set = new java.util.HashSet<>();
        set.add(b1);
        set.add(b2);
        System.out.println(set.size());
    }
}


//`x.equals(y)` ve `x.equals(z)`: **önce tahmin et**, sonra çalıştır.
// `y` ve `z` aynı nesne, neden farklı cevap? (Bir cümle: "referans tipi" kelimesi geçsin.)
//Sonra `BadBook.equals`'ın üstüne `@Override` ekle → derle → hata mesajını chat'e yapıştır.


//Şunları yazdır: `b1 == b2`, `b1.equals(b2)`, `b1 == b3`, `b1.equals(b3)`, `b1.equals(b4)`.
//        1. **Çalıştırmadan önce** 5 cevabı chat'e yaz, sonra çalıştır.
//        2. `Book`'a `equals`'ı yaz (iş anahtarı: **sadece ISBN**). Aynı 5 satırı tekrar tahmin et, çalıştır. Hangileri değişti?
//        3. Şu iki satırı da ekle, önce tahmin et: `b1.equals(null)`, `b1.equals("978-0441")`. İkisi de `false` olmalı ve **exception olmamalı**. Olmuyorsa şablonun hangi satırı eksik?
