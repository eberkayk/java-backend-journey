package w2d5;

public class Invoice implements Payable {
    private final String partName;
    private final int quantity;
    private final double pricePerItem;

    public Invoice(String partName, int quantity, double pricePerItem) {
        super();
        this.partName = partName;
        this.quantity = quantity;
        this.pricePerItem = pricePerItem;
    }

    @Override
    public double getPaymentAmount() {
        return quantity * pricePerItem;
    }

    @Override
    public String toString() {
        return "Invoice[" + partName + " x" + quantity + " = " + String.format("%.2f", getPaymentAmount()) + "]";
    }
}

//        - `Invoice implements Payable`: `private final` `String partName`, `int quantity`, `double pricePerItem`.
//        Ödeme = adet × birim fiyat. `toString` → `Invoice[Keyboard x3 = 1500.00]`.
