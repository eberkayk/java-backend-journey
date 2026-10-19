package w2d7;

import java.util.Arrays;

public class LibraryDemo {
    public static void main(String[] args) {
        Library library = new Library(10, 5);
        System.out.println(library.addBook(new Book("123", "title1", "author1")));
        System.out.println(library.addBook(new Book("123", "title2", "author3"))); //aynı isbn ekleme
        System.out.println(library.addBook(new Book("124", "title1", "author1")));
        System.out.println(library.addBook(new Book("125", "title1", "author1")));
        System.out.println(library.addBook(new Book("126", "title1", "author1")));
        System.out.println(library.addBook(new Book("127", "title1", "author2")));
        System.out.println(library.addBook(new Book("128", "title1", "author3")));
        System.out.println(library.addBook(new Book("129", "title1", "author4")));
        System.out.println(library.addBook(new Book("130", "title1", "author1")));
        System.out.println(library.addBook(new Book("131", "title1", "author1")));
        System.out.println(library.addBook(new Book("132", "title1", "author1")));
        System.out.println(library.addBook(new Book("133", "title1", "author1"))); // fazla üye
        System.out.println(library.addMember(new StudentMember("123","x")));
        System.out.println(library.addMember(new StudentMember("124","y")));
        System.out.println(library.borrowBook("156", "123")); //olmayan üye
        System.out.println(library.borrowBook("123", "156")); //olmayan kitap
        System.out.println(library.borrowBook("123","123"));
        System.out.println(library.returnBook("124", "123")); //yanlış üye kitap verir
        System.out.println(library.borrowBook("124", "123")); // akınmış kitabı alma
        System.out.println(library.borrowBook("123","124"));
        System.out.println(library.borrowBook("123","125")); // öğrenci 3. kitap
        System.out.println(library.returnBook("123", "123"));
        System.out.println(library.borrowBook("124", "123"));  //returned kitabın tekrar borrow edilmesi
        System.out.println(Arrays.toString(library.booksByAuthor("author5"))); // 0 kitaplı author
        System.out.println(Arrays.toString(library.booksByAuthor("author1"))); // 2+ kitaplı author

    }
}

//
//**5. `LibraryDemo` (test senaryosu)**
//        - **Kodlamadan önce chat'e en az 10 satırlık test tablosu:**
//        `işlem | beklenen | neyi kontrol ediyor`. En az şunları kapsasın:
//        aynı ISBN'i tekrar ekleme, kapasite dolu, var olmayan üye, var olmayan kitap,
//        ödünçteki kitabı tekrar ödünç alma,
//        öğrenci 3. kitap, yanlış üyenin iade etmesi,
//        iade sonrası başka üyenin ödünç alması,
//        `booksByAuthor` sonuç yok (uzunluk 0), `booksByAuthor` 2+ sonuç.
//- `main` tabloyu sırayla çalıştırsın, her satır için `beklenen` ve `gerçek` yazdırsın.