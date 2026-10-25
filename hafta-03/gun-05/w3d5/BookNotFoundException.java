package w3d5;

public class BookNotFoundException extends LibraryException {
    public BookNotFoundException(String isbn) {
        super("Book not found: " + isbn);
    }
}

//**2. `BookNotFoundException`**
//        - `extends LibraryException`
//        - Constructor: `public BookNotFoundException(String isbn)`
//        - Mesaj: `"Book not found: " + isbn`