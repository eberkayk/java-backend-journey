package w2d1;

public class ReferenceDemo {
    public static void main(String[] args) {
        Student a = new Student();
        a.name = "Ali";

        Student b = a;
        b.name = "Veli";

        System.out.println(a.name);

        Student c = new Student();
        c.name = "Veli";
        System.out.println(b==c);
        System.out.println(b.name.equals(c.name));

        System.out.println(a.name);
        rename(a);
        System.out.println(a.name);
        replace(a);
        System.out.println(a.name);

    }

    public static void rename(Student s) {
        s.name = "Renamed";
    }

    public static void replace(Student s) {
        s = new Student();
        s.name = "New";
    }
}
