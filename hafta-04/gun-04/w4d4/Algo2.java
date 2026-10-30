package w4d4;

public class Algo2 {
    public static void main(String[] args) {
        System.out.println(dayOfYear("2019-02-10"));
    }

    public static int dayOfYear(String date) {
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(5,7));
        int day = Integer.parseInt(date.substring(8));
        int[] months = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int result = 0;
        result += day;
        if (month > 1) {
            for (int i = 0; i < month - 1; i++) {
                result += months[i];
            }
        }
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            if(month > 2) {
                result++;
            }
        }
        return result;
    }
}
