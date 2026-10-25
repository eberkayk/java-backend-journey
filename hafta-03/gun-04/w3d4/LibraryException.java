package w3d4;

public class LibraryException extends  RuntimeException {
    public LibraryException(String message) {
        super(message);
    }
}

//**1. `LibraryException`**
//        - `public class LibraryException extends RuntimeException`
//        - Constructor: `public LibraryException(String message)` → `super(message)`
//        - Başka field/metot yok. Bu, ailenin üst class'ı.