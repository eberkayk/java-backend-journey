package w2d7;

public class BadBook {
    private final String isbn;
    private final String title;
    private final String author;

    public BadBook(String isbn, String title, String author) {
        this.isbn = isbn;
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

    public boolean equals(BadBook other) {     // @Override YOK, parametre BadBook
        return isbn.equals(other.isbn);
    }
}

//`w2d7/Book.java`: `private final String isbn, title, author;` + constructor + getter'lar.
// **Henüz `equals`, `hashCode`, `toString` yazma.**