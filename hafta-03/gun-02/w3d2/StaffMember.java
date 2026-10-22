package w3d2;

public class StaffMember extends Member {
    protected StaffMember(String id, String name) {
        super(id, name);
    }

    @Override
    public int maxBooks() {
        return 5;
    }
}
