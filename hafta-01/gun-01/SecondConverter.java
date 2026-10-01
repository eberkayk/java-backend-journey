import java.util.Scanner;
public class SecondConverter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Saat, dakika, saniye formatına çevirmek için bir saniye değeri giriniz: ");
        int second = scanner.nextInt();
        int hour = second / 3600;
        int minute = (second % 3600) / 60;
        int remainingSecond = second % 60;
        System.out.println(hour + " saat " + minute + " dakika " + remainingSecond + " saniye");
    }
}