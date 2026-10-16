package w2d4;

public class TypeDemo {
    public static void main(String[] args) {
        BankAccount a = new CheckingAccount("Can");
        a.deposit(100);
        a.withdraw(20);
        System.out.println(a.getBalance());              // 1

        BankAccount b = new SavingsAccount("Ayşe", 0.10);
        b.deposit(1000);
        ((SavingsAccount)b).addInterest();                                 // 2
        System.out.println(((SavingsAccount)b).getInterestRate());         // 3
        System.out.println(b.getBalance());

        //SavingsAccount c = new BankAccount("Ali");       // 4
        CheckingAccount d = new CheckingAccount("Deniz");
        BankAccount e = d;                               // 5
        System.out.println(e == d);                      // 6
    }
}
