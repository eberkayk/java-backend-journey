package w2d2;

public class ShopDemo {
    public static void main(String[] args) {
        Product[] products = new Product[3];
        products[0] = new Product("x", 15.22);
        products[1] = new Product("y", -12.33);
        products[2] = new Product("z", 66.67);

        System.out.println("Expected: y 0.0 Output: " + PriceUtils.cheapest(products).getName() + " " + PriceUtils.cheapest(products).getPrice());
        System.out.println(PriceUtils.totalWithTax(products));

        Product[] products1 = new Product[0];
        System.out.println(PriceUtils.cheapest(products1));
        System.out.println(PriceUtils.totalWithTax(products1));

        //PriceUtils priceUtils = new PriceUtils();

        Product p = new Product("test", 100);
        System.out.println(p.priceWithTax());
        System.out.println(p.priceWithTax());
        System.out.println(p.getPrice());
    }
}

// `w2d2/ShopDemo.java`: 3 ürünlük dizi, normal + boş dizi testleri. Son olarak `new PriceUtils()` yaz → hata ne? Yoruma al.