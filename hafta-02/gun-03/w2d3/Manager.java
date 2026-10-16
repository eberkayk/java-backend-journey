package w2d3;

public class Manager extends Employee {
    private double bonus;

    Manager(String name, double baseSalary, double bonus) {
        super(name, baseSalary);
        setBonus(bonus);
    }

    boolean setBonus(double bonus) {
        if (bonus < 0) {
            return false;
        }
        this.bonus = bonus;
        return true;
    }

    public double getBonus() {
        return bonus;
    }

    @Override
    public double getSalary() {
        return super.getSalary() + getBonus();
    }

    @Override
    public String toString() {
        return "Manager{name=" + getName() + ", salary=" + getSalary() + ", bonus=" + getBonus() + "}";
    }
}
