package w2d2;

public class BankAccount {
    private final String owner;
    private double balance;

    BankAccount(String owner) {
        this.owner = owner;
    }

    boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        balance += amount;
        return true;
    }

    boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    double getBalance() {
        return balance;
    }

    String getOwner() {
        return owner;
    }

    boolean transfer(BankAccount to, double amount) {
        if (this.equals(to) || to == null) {
            return false;
        }
        if (this.withdraw(amount)) {
            return to.deposit(amount);
        }
        return false;
    }
}
