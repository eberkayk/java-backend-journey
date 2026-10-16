package w2d3;

public class EmployeeDemo {
    public static void main(String[] args) {
        Employee employee = new Employee("Ahmet", 12000);
        Manager manager = new Manager("Mehmet", 23000, 4000);
        System.out.println(employee);
        System.out.println(manager);
        Employee e = new Manager("Ayşe", 5000, 1000);
        System.out.println(e.getSalary());
    }
}
