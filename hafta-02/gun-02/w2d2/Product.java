package w2d2;

public class Product {
    public static final double TAX_RATE = 0.20;
    private final String name;
    private double price;

    Product(String name, double price){
        this.name = name;
        setPrice(price);
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public boolean setPrice(double price) {
        if (price < 0) {
            this.price = 0.0;
            return false;
        } else {
            this.price = price;
            return true;
        }
    }

    public double priceWithTax() {
        return price * (1 + TAX_RATE);
    }
}


//### E5 — `Product` + sabit + utility ⭐⭐
//        `w2d2/Product.java`:
//        - `public static final double TAX_RATE = 0.20;`
//        - `private final String name`, `private double price`.
//        - Constructor `Product(String name, double price)` — price negatifse 0.0 olsun (E2'deki gibi setter üzerinden).
//        - `boolean setPrice(double price)` (negatif → false), `getName()`, `getPrice()`.
//        - `double priceWithTax()` → vergili fiyat. **`0.20` yazma, sabiti kullan.**
//
//        `w2d2/PriceUtils.java` — utility class:
//        - `private` constructor.
//- `static double totalWithTax(Product[] products)` → hepsinin vergili toplamı. Boş dizi → 0.0.
//        - `static Product cheapest(Product[] products)` → en ucuz ürünün **kendisi**; boş dizi → `null`. (Dünkü E5'teki max kalıbı — başlangıç değerini neye eşitliyorsun?)
//
//        `w2d2/ShopDemo.java`: 3 ürünlük dizi, normal + boş dizi testleri. Son olarak `new PriceUtils()` yaz → hata ne? Yoruma al.
