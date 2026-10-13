package w2d1;

public class BankAccount {
    String owner;
    double balance;

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
        if (balance <= 0 || balance < amount || amount <= 0) {
            return false;
        }
        balance -= amount;
        return true;
    }

    public double getBalance() {
        return balance;
    }
}
