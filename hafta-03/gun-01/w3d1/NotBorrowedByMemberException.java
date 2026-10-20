package w3d1;

public class NotBorrowedByMemberException extends LibraryException {
    public NotBorrowedByMemberException(String memberId, String isbn) {
        super("Book " + isbn + " is not borrowed by " + memberId);
    }
}

//**6. `NotBorrowedByMemberException`**
//        - `extends LibraryException`
//        - Constructor: `public NotBorrowedByMemberException(String memberId, String isbn)`
//        - Mesaj: `"Book " + isbn + " is not borrowed by " + memberId`