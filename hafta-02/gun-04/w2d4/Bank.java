package w2d4;

public class Bank {
    public static void main(String[] args) {
        BankAccount[] accounts = new BankAccount[6];
        accounts[0] = new BankAccount("Ahmet");
        accounts[1] = new BankAccount("Mehmet");
        accounts[2] = new SavingsAccount("Veli", 0.1); //121
        accounts[3] = new SavingsAccount("Ayşe", 0.2); //144
        accounts[4] = new CheckingAccount("Fatma");
        accounts[5] = new CheckingAccount("Ali");

        accounts[0].deposit(100);
        accounts[1].deposit(150);
        accounts[2].deposit(160);
        accounts[3].deposit(170);
        accounts[4].deposit(180);
        accounts[5].deposit(190);


        for (int i = 0; i < accounts.length; i++) {
            accounts[i].withdraw(50);
            System.out.println("Owner: " + accounts[i].getOwner() + " balance: " + accounts[i].getBalance());
        }

        System.out.println(totalBalance(accounts)); //648
        monthEnd(accounts);
        System.out.println(totalBalance(accounts)); //683

        System.out.println(richest(accounts));

    }

    public static double totalBalance(BankAccount[] accounts) {
        double sum = 0;
        for (BankAccount acc: accounts) {
            sum += acc.getBalance();
        }
        return sum;
    }

    public static BankAccount richest(BankAccount[] accounts) {
        BankAccount richest = accounts[0];
        for (int i = 1; i < accounts.length; i++) {
            if (richest.getBalance() < accounts[i].getBalance()) {
                richest = accounts[i];
            }
        }
        return richest;
    }

    public static  void monthEnd(BankAccount[] accounts) {
        for (BankAccount account : accounts) {
            if (account instanceof SavingsAccount s) {
                s.addInterest();
            }
        }
    }
}

//### E2 — Polimorfik dizi: ay sonu ⭐⭐⭐
//        `w2d4/Bank.java` — `main` dışında **static** metotlar:
//
//        - `static double totalBalance(BankAccount[] accounts)` → toplam bakiye.
//        - `static BankAccount richest(BankAccount[] accounts)` → en yüksek bakiyeli hesap. (Başlangıç değeri: Gün 4 dersini hatırla — `-1` değil.)
//        - `static void monthEnd(BankAccount[] accounts)` → **sadece** `SavingsAccount` olanlara `addInterest()` uygular (pattern matching `instanceof` ile).
//
//        `main`'de: 2 normal, 2 vadeli (farklı faiz), 2 vadesiz hesaplık **tek** bir `BankAccount[]`. Her birine farklı miktar yatır. Sonra **aynı döngüde** hepsinden `withdraw(100)`.
//        - Her hesabın sahibini ve bakiyesini yazdır → vadesizlerde 101 düştüğünü **çıktıdan** göster.
//- `totalBalance` → `monthEnd` → tekrar `totalBalance`. Farkı **elle hesapla**, çıktıyla karşılaştır.
//        - `richest` sonucunun sahibini yazdır.
//- Soru (kağıda, 1-2 cümle): `withdraw(100)` döngüsünü yazarken `instanceof` kullandın mı? `monthEnd`'de neden kullanmak zorunda kaldın? Fark ne?
