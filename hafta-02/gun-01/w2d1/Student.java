package w2d1;

public class Student {
    String name;
    int age;
    double gpa;
    boolean graduated;

    Student() {
    }

    Student(String name, int age, double gpa) {
        this.name = name;
        this.age = age;
        this.gpa = gpa;
    }
}


//### E1 — İlk class ve varsayılan değerler ⭐
//        `Student.java`: field'lar `name` (String), `age` (int), `gpa` (double), `graduated` (boolean). **Constructor yazma.**
//        `StudentDemo.java` (main burada):
//        1. **Önce kağıda tahmin:** `new Student()` sonrası dört field'ı yazdırınca ne çıkar? Sonra çalıştır.
//        2. İki öğrenci oluştur, field'larını ata, ikisini de yazdır — birini değiştirmek diğerini etkiliyor mu?
//        3. `System.out.println(s1);` → ne çıktı? Bir cümle: neden böyle?
//        4. main içinde `int x; System.out.println(x);` yaz → hata mesajını oku. Field'da hata yokken burada neden var? Bir cümle. Sonra o satırları sil.
