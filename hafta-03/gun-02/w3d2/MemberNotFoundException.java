package w3d2;

public class MemberNotFoundException extends LibraryException {
    public MemberNotFoundException(String memberId) {
        super("Member not found: " + memberId);
    }
}

//**3. `MemberNotFoundException`**
//        - `extends LibraryException`
//        - Constructor: `public MemberNotFoundException(String memberId)`
//        - Mesaj: `"Member not found: " + memberId`