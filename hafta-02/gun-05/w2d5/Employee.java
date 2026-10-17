package w2d5;

public class Employee implements Payable, Describable {
    private final String name;
    private final double monthlySalary;

    public Employee(String name, double monthlySalary) {
        this.name = name;
        this.monthlySalary = monthlySalary;
    }

    @Override
    public double getPaymentAmount() {
        return monthlySalary;
    }

    @Override
    public String toString() {
        return "Employee[" + name + ", " + String.format("%.2f", getPaymentAmount()) + "]";
    }

    @Override
    public String describe() {
        return "Employee " + name + " earns " + String.format("%.2f", getPaymentAmount());
    }
}

//        - `Employee implements Payable`: `private final String name`, `private final double monthlySalary`.
//        Ödeme = maaş. `toString` → `Employee[Ayse, 42000.00]`.
