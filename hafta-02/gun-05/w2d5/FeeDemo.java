package w2d5;

public class FeeDemo {
    public static void main(String[] args) {
        BankAccount[] accounts = new BankAccount[4];
        accounts[0] = new BankAccount("Ahmet"); //90
        accounts[1] = new CheckingAccount("Mehmet"); //89
        accounts[2] = new SavingsAccount("Ali", 0.1); //90
        accounts[3] = new StudentAccount("Veli"); //89.75

        for (int i = 0; i < accounts.length; i++) {
            accounts[i].deposit(100);
            accounts[i].withdraw(10);
            System.out.println(accounts[i]);
        }

        accounts[1].transfer(accounts[3], 20);
        System.out.println(accounts[1]); //68
    }
}
//
//- `w2d5/FeeDemo.java`: 4 türden (`BankAccount`, `Savings`, `Checking`, `Student`) birer hesap,
// tek `BankAccount[]`. Hepsine 100 yatır, **aynı döngüde** `withdraw(10)`, her birinin `toString`'ini yazdır.
//        - **Önce kağıda** 4 bakiyeyi yaz, sonra çalıştır.
//  - Bir `CheckingAccount`'tan `transfer` yap → ücret yine kesiliyor mu? Bakiyeyi elle hesapla, çıktıyla karşılaştır.