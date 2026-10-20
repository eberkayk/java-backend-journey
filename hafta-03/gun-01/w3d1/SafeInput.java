package w3d1;

import java.util.Scanner;

public class SafeInput {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(readInt(scanner, "Enter a number: "));
    }

    public static int readInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                int result = Integer.parseInt(scanner.nextLine());
                return result;
            } catch (NumberFormatException e) {
                System.out.print("Please enter a integer value: ");
            }
        }
    }
}


//Dünkü menünün çökme sorununu çözen yardımcı metot. `w3d1/SafeInput.java`:
//
//        **Class `SafeInput`**
//        - Field yok.
//        - `public static int readInt(Scanner scanner, String prompt)`
//        - `prompt`'u yazdırır (`System.out.print`), `scanner.nextLine()` ile bir satır okur, `Integer.parseInt` ile çevirir.
//        - Çevrilemezse (`NumberFormatException`) `"Please enter a number"` yazdırır ve **tekrar sorar**. Geçerli sayı gelene kadar döner.
//  - Geçerli sayıyı `return` eder.
//  - Döngü tipi ve `try`'ın döngünün neresinde olacağı **senin tasarım kararın**.
//        - `static void main(String[] args)`: bir `Scanner` yarat (döngü **dışında**, dünkü hata), `readInt(scanner, "Age: ")` çağır, sonucu yazdır.
//
//        **Test (elle, konsolda):** `abc` → tekrar sorar · `` (boş, direkt Enter) → tekrar sorar · `12.5` → ? (önce tahmin) · `-3` → kabul eder · `42` → kabul eder.
