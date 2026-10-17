package w2d5;

public class BankAccount {
    private final String owner;
    private double balance;

    public BankAccount(String owner) {
        this.owner = owner;
    }

    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        balance += amount;
        return true;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0 || amount + fee() > balance) {
            return false;
        }
        balance -= amount + fee();
        return true;
    }

    public double getBalance() {
        return balance;
    }

    public String getOwner() {
        return owner;
    }

    public boolean transfer(BankAccount to, double amount) {
        if (this.equals(to) || to == null) {
            return false;
        }
        if (this.withdraw(amount)) {
            return to.deposit(amount);
        }
        return false;
    }

    public double fee() {
        return 0.0;
    }

    @Override
    public String toString() {
        return "BankAccount{Owner=" + getOwner() + " balance=" + getBalance() + "}";
    }
}

//### E2 — Dünkü `fee()` tasarımını kodla ⭐⭐⭐
//Dün E5'te kağıtta çözdün; bugün kod. `w2d5` içinde:
//
//        - `BankAccount`'a `public double fee()` ekle → `0.0` döndürür.
//        - `CheckingAccount`'ta `fee()`'yi override et → `1.0`. `CheckingAccount`'taki **`withdraw` override'ını sil** — artık ücret mantığı `BankAccount.withdraw` içinde `fee()` kullanılarak tek yerde olsun. (Hangi satır değişir, sen bul. İpucu: "çekilecek toplam" ne?)
//        - Yeni class: `StudentAccount extends BankAccount` → `fee()` = `0.25`. **Başka hiçbir dosyaya dokunma.**
//        - `w2d5/FeeDemo.java`: 4 türden (`BankAccount`, `Savings`, `Checking`, `Student`) birer hesap, tek `BankAccount[]`. Hepsine 100 yatır, **aynı döngüde** `withdraw(10)`, her birinin `toString`'ini yazdır.
//        - **Önce kağıda** 4 bakiyeyi yaz, sonra çalıştır.
//  - Bir `CheckingAccount`'tan `transfer` yap → ücret yine kesiliyor mu? Bakiyeyi elle hesapla, çıktıyla karşılaştır.
//        - **Kağıda (2-3 cümle):** `BankAccount`'u `abstract` yapmalı mıyız? Yaparsak hangi satırlar bozulur? `fee()`'yi abstract yapsaydık her alt class'tan ne istemiş olurduk — bu bankada mantıklı mı?
