package w2d5;

public class CheckingAccount extends BankAccount {
    public static final double FEE = 1.0;

    public CheckingAccount(String owner) {
        super(owner);
    }

    @Override
    public double fee() {
        return 1.0;
    }

    @Override
    public String toString() {
        return "CheckingAccount{Owner=" + getOwner() +
                  " balance=" + getBalance() +
                '}';
    }
}
