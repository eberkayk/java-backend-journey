package w2d3;

public class BankAccount {
    private final String owner;
    private double balance;

    public BankAccount(String owner) {
        this.owner = owner;
    }

    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        balance += amount;
        return true;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    public double getBalance() {
        return balance;
    }

    public String getOwner() {
        return owner;
    }

    public boolean transfer(BankAccount to, double amount) {
        if (this.equals(to) || to == null) {
            return false;
        }
        if (this.withdraw(amount)) {
            return to.deposit(amount);
        }
        return false;
    }
}
