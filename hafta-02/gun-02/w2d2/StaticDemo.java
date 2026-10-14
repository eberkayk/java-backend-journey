package w2d2;

public class StaticDemo {
    public static void main(String[] args) {
        System.out.println(Student.getCount());
        Student student = new Student("Ahmet", 24, 2.0);
        System.out.println(student.getId());
        System.out.println(Student.getCount());
        student.getCount();

        Student student1 = new Student("Ali", -5, 9.7);
        System.out.println(student1.getId());
        System.out.println(Student.getCount());

        Student student2 = new Student("Mehmet", 22, 3.33);
        System.out.println(student2.getId());
        System.out.println(Student.getCount());
    }
}
