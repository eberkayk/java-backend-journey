package w3d3;

public class BorrowLimitExceededException extends LibraryException {
    public BorrowLimitExceededException(Member member) {
        super("Borrow limit reached for " + member.getId() + " (max " + member.maxBooks() + ")");
    }
}

//**5. `BorrowLimitExceededException`**
//        - `extends LibraryException`
//        - Constructor: `public BorrowLimitExceededException(Member member)`
//        - Mesaj: `"Borrow limit reached for " + <üyenin id'si> + " (max " + <üyenin maxBooks()> + ")"`
//        - Örnek: `Borrow limit reached for M1 (max 2)`