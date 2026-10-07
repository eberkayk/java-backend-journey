### Bölüm A — Teori (20 dk, her biri 1–3 cümle)
1. JDK, JRE, JVM farkı nedir? `javac` ve `java` komutları ne yapar?
jdk: kod yazmak için gerekli her şey. jre: kodu sadece çalıştırmak için gerekli şeyler. jvm: class dosyasını bytecode'a çeviren sanal makine. jre, jmv içerir. jdk, jre içerir.

2. `int x = 7 / 2;` ve `double y = 7 / 2;` → değerleri ne? `y`'nin 3.5 olması için ne yazmalısın?
x = 3 y = 3 y'nin değerinin 3.5 olması için double y = (double) 7 / 2; yazarız.

3. `&&` kısa devre nedir? `if (arr != null && arr.length > 0)` ifadesinde sıra neden önemli?
&& kısa devre ilk değer yanlışsa ikinci değere bakılmaz çünkü ikinci değer ne olursa olsun sonuç her zaman yanlış (false) çıkar. sıra önemli çünkü arr != null eğer yanlışsa yani arr null ise sonuç her türlü yanlış olur ve ikiinci koşula bakılmaz. yani null dizinin uzunluğuna bakılmasına gerek olmadığı bilinir.

4. `while` ile `do-while` farkı nedir? Hangisi en az bir kez çalışır?
do-while en az bir kere çalışır. döngünün ilk çalışmadan sonra devam edip etmeyeceği while koşuluna bağlıdır. while'ın direkt olarak çalışıp çalışmaması ve döngünün devam etip etmemesi koşula bağlıdır. do-while'da koşul yanlış olsa bile en az bir kere çalışır.

5. Java pass-by-value mu? `void change(int[] a) { a[0] = 9; }` ile `void change(int[] a) { a = new int[]{9}; }` farkını açıkla.
evet java pass-by-value'dır. ilk metot orijinal diziyi değiştirir. ikinci metot yeni bir dizi yaratır ve onun üzerinde işlem yapar.

6. Overloading nedir? Sadece dönüş tipi farklı iki metot olur mu?
overloading iki metodun aynı isme sahip olup farklı parametre listesine sahip olmasıdır. sadece dönüş tipi farklı olursa hata verir çünkü aynı isim ve aynı parametre tipleri ile hangi metotun çağırılacağı bilinemez.

7. `String s1 = "hi"; String s2 = "hi"; String s3 = new String("hi");` → `s1 == s2`, `s1 == s3`, `s1.equals(s3)` ne verir, neden?
s1==s2 true s1==s3 false s1.equals(s3) true verir. çünkü == adres karşılaştırır, equals() içerik karşılaştırır. s1 ve s2'nin ikisinin de içeriği aynı olmasından dolayı String pool'dan aynı nesneyi paylaşır. bu yüzden adresleri aynıdır.

8. String neden immutable? En az iki sebep.
thread safe olması için immutable'dır. yani örneğin bir şifre değişkeni oluşturulduktan sonra başka metotlarla yanlışlıkla değiştirilirse sorun yaşanır bunun önüne geçilmesi için.
ikinci bir sebep şu an aklıma gelmedi.

9. Ne zaman `StringBuilder` kullanırsın?
bir string'i yeni bir string yaratmadan değiştirmek istediğimizde kullanırız.

10. `git add`, `git commit`, `git push` sırayla ne yapar?
git add, gönderilecek dosyaların listesini hazırlar. git commit, add'de eklenmiş dosyaları stage area'ya alır, gönderime hazır hale getirir. git push ise commit edilmiş dosyaları uzak ortama gönderir.


### Bölüm B — Çıktı tahmini (10 dk, çalıştırmadan)
```java
// B1
int i = 5;
int j = i++ + ++i;
System.out.println(i + " " + j);

5 11

// B2
System.out.println(1 + 2 + "3" + 4 + 5);

3345

// B3
int[] a = {1, 2, 3};
int[] b = a;
b = new int[]{7, 8, 9};
b[0] = 100;
System.out.println(a[0] + " " + b[0]);

1 100

// B4
String s = "abc";
s.concat("def");
System.out.println(s + " " + s.length());

abc 3

// B5
for (int k = 0; k < 10; k += 3) {
    if (k == 6) continue;
    System.out.print(k + " ");
}

0 3 9 