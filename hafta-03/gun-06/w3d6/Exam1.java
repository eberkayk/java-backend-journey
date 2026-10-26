package w3d6;

public class Exam1 {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        Product pen = new Product("Pen", 2.5, 10);
        Product notebook = new Product("Notebook", 12, 3);
        Product eraser = new Product("Eraser", 1, 0);
        Product ink = new Product("Ink", 12, 5);
        inventory.add(pen);
        inventory.add(notebook);
        inventory.add(eraser);
        inventory.add(ink);

        try {
            inventory.add(pen);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        inventory.sell("Notebook", 3);

        try {
            inventory.sell("Pen", 20);
        } catch (InvalidQuantityException e) {
            System.out.println(e.getMessage());
        }

        try {
            inventory.sell("Glue", 1);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            inventory.sell("Pen", 0);
        } catch (InvalidQuantityException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(inventory.sortedByName());
        System.out.println(inventory.sortedByPriceDesc());
        System.out.println("Removed: " + inventory.removeOutOfStock());

        for (Product p : inventory) {
            System.out.println(p);
        }
    }
}
