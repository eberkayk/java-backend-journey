package w2d1;

public class StudentDemo {
    public static void main(String[] args) {
        Student student = new Student();
        System.out.println(student.name);
        System.out.println(student.age);
        System.out.println(student.gpa);
        System.out.println(student.graduated);

        Student s1 = new Student();
        s1.name = "Serenay";
        s1.age = 32;
        s1.gpa = 2.88;
        s1.graduated = true;

        Student s2 = new Student();
        s2.name = "Berkay";
        s2.age = 32;
        s2.gpa = 2.84;
        s2.graduated =  true;

        System.out.println(s1.gpa);
        System.out.println(s2.gpa);

        System.out.println(s1);

//        int x;
//        System.out.println(x);

    }
}
