package w2d1;

public class Classroom {
    public static void main(String[] args) {
        Student[] students = new Student[3];
        System.out.println(students[0]);
        students[0] = new Student("Ahmet", 22, 4.78);
        students[1] = new Student("Mehmet", 23, 3.46);
        students[2] = new Student("Veli", 20, 2.11);
        System.out.println(topStudent(students).name);
        System.out.println(averageGpa(students));

        Student[] students1 = new Student[0];
        Student student = topStudent(students1);
        if (student == null) {
            System.out.println("This object is null, therefor it can not have a name");
        } else {
            System.out.println(student.name);
        }

        Student[] students2 = new Student[1];
        students2[0] =  new Student("Ayşe", 21, 3.98);
        System.out.println(topStudent(students2).name);

    }

    public static Student topStudent(Student[] arr) {
        if (arr.length == 0) {
            return null;
        }
        double max = arr[0].gpa;
        int topStudentIndex = 0;
        for (int i = 1; i < arr.length; i++) {
            if (max < arr[i].gpa) {
                max = arr[i].gpa;
                topStudentIndex = i;
            }

        }
        return arr[topStudentIndex];
    }

    public static double averageGpa(Student[] arr) {
        double sumGpa = 0;
        for (int i = 0; i < arr.length; i++) {
            sumGpa += arr[i].gpa;
        }

        return sumGpa / arr.length;
    }
}

//### E5 — Nesne dizisi ⭐⭐
//        `Classroom.java` (main burada):
//        1. `Student[] students = new Student[3];` → hemen ardından `students[0]` yazdır. **Önce tahmin.** Neden? (Tablo 2'ye bak.)
//        2. Diziyi doldur — `Student`'a artık bir constructor ekle: `Student(String name, int age, double gpa)`. **Dikkat:** constructor ekledin → E1/E3'teki `new Student()` satırları ne oldu? Derle, gör, düzelt (nasıl düzelteceğine sen karar ver — iki yol var, birini seç ve yorumla yaz).
//        3. `static Student topStudent(Student[] arr)` → **en yüksek gpa'lı öğrenciyi** döndürsün (adını değil, **nesnenin kendisini**). Gün 4'teki max kalıbı. Boş dizi gelirse `null` dönsün.
//4. `static double averageGpa(Student[] arr)` → double.
//        5. Test: normal dizi, tek elemanlı dizi, boş dizi (`new Student[0]`). Boş dizide `topStudent(...)`'ın sonucunun `.name`'ine erişirsen ne olur? Bunu main'de nasıl önlersin?

