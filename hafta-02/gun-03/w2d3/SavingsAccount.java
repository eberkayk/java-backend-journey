package w2d3;

public class SavingsAccount extends BankAccount {
    private final double interestRate;

    SavingsAccount(String owner, double interestRate) {
        super(owner);
        this.interestRate = interestRate;
    }

    double getInterestRate() {
        return interestRate;
    }

    void addInterest() {
        deposit(getBalance() * interestRate);
    }
}


//### E1 — `SavingsAccount` ⭐⭐
//        `w2d3/SavingsAccount.java` → `extends BankAccount`:
//        - `private final double interestRate` (ör. 0.05 = %5).
//        - Constructor `SavingsAccount(String owner, double interestRate)`.
//        - `getInterestRate()`.
//        - `void addInterest()` → bakiyeye faiz ekler. **`balance`'a doğrudan dokunma.**
//
//        **Deneyler (önce tahmin, sonra dene, sonra geri al):**
//        - (a) Constructor'dan `super(owner);` satırını sil. Hata **hangi satırda** ve mesaj ne diyor? Bir cümleyle neden. (§3 kural 2-3.)
//        - (b) `super(owner);`'ı `this.interestRate = ...` satırının **altına** taşı. Hata ne?
//        - (c) `addInterest` içinde `balance += ...` yaz. Hata mesajını kağıda yaz.
//
//        `w2d3/AccountDemo.java`: 1000 TL yatır, `addInterest()`, bakiye yazdır (beklenen değeri yanına yaz). Ayrıca **hiç yazmadığın** `withdraw` ve `transfer`'ı `SavingsAccount` üzerinde çağırıp çalıştığını göster.
