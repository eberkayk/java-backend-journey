package w2d3;

public class AccountDemo {
    public static void main(String[] args) {
        SavingsAccount savingsAccount = new SavingsAccount("Ahmet", 0.05);
        savingsAccount.deposit(1000);
        savingsAccount.addInterest();
        System.out.println("Expected 1050.0 " + savingsAccount.getBalance());
        savingsAccount.withdraw(500);
        savingsAccount.transfer(new BankAccount("Veli"),200);
        System.out.println("Expected 350.0 " + savingsAccount.getBalance());

        CheckingAccount checkingAccount = new CheckingAccount("Mehmet");
        checkingAccount.deposit(100);
        checkingAccount.withdraw(50);
        System.out.println("Expected 49 " + checkingAccount.getBalance());
        checkingAccount.withdraw(49);
        System.out.println("Expected 49 " + checkingAccount.getBalance());
        checkingAccount.transfer(new BankAccount("Veli"), 10);
        System.out.println("Expected 38 " + checkingAccount.getBalance());
    }
}
