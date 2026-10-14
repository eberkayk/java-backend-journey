package w2d2;

public class PriceUtils {
    private PriceUtils() {}

    public static double totalWithTax(Product[] products) {
        double totalWithTax = 0.0;
        if (products.length == 0) {
            return totalWithTax;
        }

        for (int i = 0; i < products.length; i++) {
            totalWithTax += products[i].priceWithTax();
        }

        return totalWithTax;
    }

    public static  Product cheapest(Product[] products) {
        if (products.length == 0) {
            return null;
        }

        Product cheapest = products[0];
        for (int i = 1; i < products.length; i++) {
            if (cheapest.getPrice() > products[i].getPrice()) {
                cheapest = products[i];
            }
        }
        return cheapest;
    }
}


//        `w2d2/PriceUtils.java` — utility class:
//        - `private` constructor.
//- `static double totalWithTax(Product[] products)` → hepsinin vergili toplamı. Boş dizi → 0.0.
//        - `static Product cheapest(Product[] products)` → en ucuz ürünün **kendisi**; boş dizi → `null`. (Dünkü E5'teki max kalıbı — başlangıç değerini neye eşitliyorsun?)
//