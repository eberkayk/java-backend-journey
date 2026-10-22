package w3d2;

import java.util.Objects;

public abstract class Member {
    private final String id;
    private final String name;
    private int borrowedCount;

    protected Member(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public abstract int maxBooks();

    public boolean canBorrow() {
        return borrowedCount < maxBooks();
    }

    public void onBorrow() {
        borrowedCount++;
    }

    public void onReturn() {
        borrowedCount--;
    }

    public int getBorrowedCount() {
        return borrowedCount;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this==obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Member)) {
            return false;
        }
        Member other = (Member) obj;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + id + ", " + name + ", " + borrowedCount + "/" + maxBooks() + " books]";
    }
}

//**2. `abstract class Member`**
//        - `private final String id; private final String name; private int borrowedCount;`
//        - `public abstract int maxBooks();`
//        - `public boolean canBorrow()` → limit dolmadıysa true
//        - `public void onBorrow()` / `public void onReturn()` → sayacı artır / azalt
//- `equals` / `hashCode` → E4-5'te verdiğin karara göre
//        - `toString` → `StudentMember[M1, Ayse, 1/2 books]` (class adı alt class'tan gelsin: `getClass().getSimpleName()` kullanabilirsin)
