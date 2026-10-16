package w2d3;

public class Employee {
    private final String name;
    private final double baseSalary;

    Employee(String name, double baseSalary) {
        this.name = name;
        this.baseSalary = baseSalary;
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return getBaseSalary();
    }

    @Override
    public String toString() {
        return "Employee{name=" + name + ", salary=" + getSalary() + "}";
    }
}
