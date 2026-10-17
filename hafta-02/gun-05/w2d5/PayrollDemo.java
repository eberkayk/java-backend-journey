package w2d5;

public class PayrollDemo {
    public static void main(String[] args) {
        Payable[] items =  new Payable[4];
        items[0] = new Employee("Ahmet", 15000);
        items[1] = new Employee("Mehmet", 22000);
        items[2] = new Invoice("x", 5, 15);
        items[3] = new Invoice("y", 7, 12.50);
        System.out.println(items[0]);
        System.out.println(items[1]);
        System.out.println(items[2]);
        System.out.println(items[3]);

        System.out.println(items[0].isLarge());
        System.out.println(items[1].isLarge());
        System.out.println(items[2].isLarge());
        System.out.println(items[3].isLarge());

        System.out.println(totalPayment(items)); //37162.5

        Describable[] describables = new Describable[2];
        describables[0] = new Employee("Ali", 42000);
        describables[1] = new Circle(15.21);

        for (int i = 0; i < describables.length; i++) {
            System.out.println(describables[i].describe());
        }

    }

    public static double totalPayment(Payable[] items) {
        double totalPayment = 0.0;
        for (int i = 0; i < items.length; i++) {
            totalPayment += items[i].getPaymentAmount();
        }
        return totalPayment;
    }
}

//        - `PayrollDemo`:
//        - `static double totalPayment(Payable[] items)` — `Invoice` veya `Employee` kelimesi bu metodun içinde **geçmemeli**.
//        - 2 fatura + 2 çalışanlık tek `Payable[]`, hepsini yazdır, toplamı yazdır. Toplamı **önce elle** hesapla.
//- Deneyler (her birini dene, hata mesajını kağıda yaz, geri al):
//        1. `Invoice`'taki `getPaymentAmount`'ın önündeki `public`'i sil.
//        2. `Payable p = new Invoice(...); p.toString();` derlenir mi? `p.getPartName()` (bir getter yazdıysan) derlenir mi? Neden? (Dünün §2 tablosu.)
//        - **Bonus (zorunlu değil):** `Payable`'a `default boolean isLarge()` ekle (> 10 000). `Invoice` veya `Employee`'yi değiştirmeden çalıştığını göster.
