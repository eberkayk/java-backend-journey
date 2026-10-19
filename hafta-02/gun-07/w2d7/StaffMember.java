package w2d7;

public class StaffMember extends Member {
    protected StaffMember(String id, String name) {
        super(id, name);
    }

    @Override
    public int maxBooks() {
        return 5;
    }
}
