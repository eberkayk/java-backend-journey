package w2d5;

public interface Payable {
    public double getPaymentAmount();
    default boolean isLarge() {
        return getPaymentAmount() > 10000;
    }
}


//### E3 — Interface: `Payable` ⭐⭐⭐
//        `w2d5/Payable.java`, `Invoice.java`, `Employee.java`, `PayrollDemo.java`:
//
//        - `interface Payable` → `double getPaymentAmount();`
//        - `Invoice implements Payable`: `private final` `String partName`, `int quantity`, `double pricePerItem`. Ödeme = adet × birim fiyat. `toString` → `Invoice[Keyboard x3 = 1500.00]`.
//        - `Employee implements Payable`: `private final String name`, `private final double monthlySalary`. Ödeme = maaş. `toString` → `Employee[Ayse, 42000.00]`.
//        - `PayrollDemo`:
//        - `static double totalPayment(Payable[] items)` — `Invoice` veya `Employee` kelimesi bu metodun içinde **geçmemeli**.
//        - 2 fatura + 2 çalışanlık tek `Payable[]`, hepsini yazdır, toplamı yazdır. Toplamı **önce elle** hesapla.
//- Deneyler (her birini dene, hata mesajını kağıda yaz, geri al):
//        1. `Invoice`'taki `getPaymentAmount`'ın önündeki `public`'i sil.
//        2. `Payable p = new Invoice(...); p.toString();` derlenir mi? `p.getPartName()` (bir getter yazdıysan) derlenir mi? Neden? (Dünün §2 tablosu.)
//        - **Bonus (zorunlu değil):** `Payable`'a `default boolean isLarge()` ekle (> 10 000). `Invoice` veya `Employee`'yi değiştirmeden çalıştığını göster.
