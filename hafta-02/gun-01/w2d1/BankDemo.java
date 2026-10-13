package w2d1;

public class BankDemo {
    public static void main(String[] args) {
        BankAccount bankAccount = new BankAccount("Ahmet");
        System.out.println("Deposit 100 -> " + bankAccount.deposit(100) + " balance: " + bankAccount.getBalance());
        System.out.println("Withdraw 100 -> " + bankAccount.withdraw(100) + " balance: " + bankAccount.getBalance());
        System.out.println("Withdraw 0 -> " + bankAccount.withdraw(0) + " balance: " + bankAccount.getBalance());
        System.out.println("Deposit -100 -> " + bankAccount.deposit(-100) + " balance: " + bankAccount.getBalance());
        System.out.println("Withdraw 500 -> " + bankAccount.withdraw(500) + " balance: " + bankAccount.getBalance());
        System.out.println("Deposit 399.99 -> " + bankAccount.deposit(399.99) + " balance: " + bankAccount.getBalance());
        bankAccount.balance = -100000;
        System.out.println(bankAccount.balance);
    }
}
