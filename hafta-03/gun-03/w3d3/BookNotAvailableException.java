package w3d3;

public class BookNotAvailableException extends LibraryException {
    public BookNotAvailableException(String isbn) {
        super("Book is already borrowed: " + isbn);
    }
}

//**4. `BookNotAvailableException`**
//        - `extends LibraryException`
//        - Constructor: `public BookNotAvailableException(String isbn)`
//        - Mesaj: `"Book is already borrowed: " + isbn`