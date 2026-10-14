package w2d2;

public class StudentDemo {
    public static void main(String[] args) {
        Student student = new Student("Ahmet", 24, 2.0);
        System.out.println(student.setGpa(3.5));
        System.out.println(student.setGpa(4.5));
        System.out.println(student.setGpa(-1));
        System.out.println(student.getGpa());

        Student student1 = new Student("Ali", -5, 9.7);
        System.out.println(student1.getAge());
        System.out.println(student1.getGpa());
    }
}
