package w2d5;

public class StudentAccount extends BankAccount {
    StudentAccount (String owner) {
        super(owner);
    }

    @Override
    public double fee() {
        return 0.25;
    }
}
