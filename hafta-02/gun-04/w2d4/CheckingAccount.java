package w2d4;

public class CheckingAccount extends BankAccount {
    public static final double FEE = 1.0;

    public CheckingAccount(String owner) {
        super(owner);
    }

    @Override
    public boolean withdraw(double amount) {
        return super.withdraw(amount + FEE);
    }

    @Override
    public String toString() {
        return "CheckingAccount{Owner=" + getOwner() +
                  " balance=" + getBalance() +
                '}';
    }
}
