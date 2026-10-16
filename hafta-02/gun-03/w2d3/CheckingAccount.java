package w2d3;

public class CheckingAccount extends BankAccount {
    public static final double FEE = 1.0;

    CheckingAccount(String owner) {
        super(owner);
    }

    @Override
    public boolean withdraw(double amount) {
        return super.withdraw(amount + FEE);
    }
}
