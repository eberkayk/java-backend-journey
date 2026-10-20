package w3d1;

public class StudentMember extends Member {
    protected StudentMember(String id, String name) {
        super(id, name);
    }

    @Override
    public int maxBooks() {
        return 2;
    }
}
