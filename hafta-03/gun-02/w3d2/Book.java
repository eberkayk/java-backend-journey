package w3d2;

import java.util.Objects;

public class Book {
    private final String isbn;
    private final String title;
    private final String author;
    private Member borrower;

    public Book(String isbn, String title, String author) {
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("ISBN must not be blank");
        }
        this.isbn = isbn;

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be blank");
        }
        this.title = title;
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;                              // (1) aynı adres → kesin eşit
        if (o == null || getClass() != o.getClass()) return false; // (2) null veya farklı class
        Book other = (Book) o;                             // (3) artık güvenli downcast
        return isbn.equals(other.isbn);                // (4) iş anahtarını karşılaştır
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);    // equals'ta hangi alanlar varsa onlar
    }

    @Override
    public String toString() {
        if (isAvailable()) {
            return "Book[" + getIsbn() + ", " + getTitle() + " by " + getAuthor() + ", available]";
        } else {
            return "Book[" + getIsbn() + ", " + getTitle() + " by " + getAuthor() + ", borrowed by " + getBorrower().getId() + "]";
        }
    }

    public boolean isAvailable() {
        return borrower == null;
    }

    public Member getBorrower() {
        return borrower;
    }

    void setBorrower(Member member) {
        borrower = member;
    }
}

//**1. `Book`** (E1–E3'ten devam)
//        - Yeni alan: `private Member borrower;`. `null` ise kitap rafta.
//- `public boolean isAvailable()`
//        - `public Member getBorrower()`
//        - `public void setBorrower(Member member)`. Sadece `Library` çağıracak; `null` vermek "iade edildi" demek.
//- `equals` / `hashCode` **sadece ISBN** (değiştirme).
//        - `toString` → rafta: `Book[978-0441, Dune by Frank Herbert, available]`, ödünçte: `Book[978-0441, Dune by Frank Herbert, borrowed by M1]`
