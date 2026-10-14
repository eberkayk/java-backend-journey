package w2d2;

public class Student {
    private String name;
    private int age;
    private double gpa;
    private static int count;
    private final int id;
    private static int nextId = 1;

    Student(String name, int age, double gpa) {
        this.name = name;
        setAge(age);
        setGpa(gpa);
        this.id = nextId;
        nextId++;
        count++;
    }

    String getName() {
        return name;
    }

    int getAge() {
        return age;
    }

    double getGpa() {
        return gpa;
    }

    boolean setAge(int age) {
        if (age < 0 || age > 150) {
            return false;
        }
        this.age = age;
        return true;
    }

    boolean setGpa(double gpa) {
        if (gpa < 0.0 || gpa > 4.0) {
            return false;
        }
        this.gpa = gpa;
        return true;
    }

    int getId() {
        return id;
    }

    public static int getCount() {
        return count;
    }
}
