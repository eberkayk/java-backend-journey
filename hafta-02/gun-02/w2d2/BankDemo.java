package w2d2;

public class BankDemo {
    public static void main(String[] args) {
        BankAccount account = new BankAccount("Ahmet");
//        account.balance = -100000;
//        account.owner =  "Hacker";
        account.deposit(10000);
        BankAccount account1 = new BankAccount("Mehmet");
        System.out.println(account.transfer(account1, 3000));
        System.out.println("Expected true 7000 3000 " + account.getBalance() + " " + account1.getBalance());
        System.out.println(account.transfer(account1, -1500));
        System.out.println("Expected false 7000 3000 " + account.getBalance() + " " + account1.getBalance());
        System.out.println(account.transfer(account1, 9000));
        System.out.println("Expected false 7000 3000 " + account.getBalance() + " " + account1.getBalance());
        System.out.println(account.transfer(null, 2500));
        System.out.println("Expected false 7000 3000 " + account.getBalance() + " " + account1.getBalance());
        System.out.println(account.transfer(account, 5000));
        System.out.println("Expected false 7000 3000 " + account.getBalance() + " " + account1.getBalance());

    }


}
