package w4d5;

public class Algo1 {
    public static void main(String[] args) {
        System.out.println(reformatDate("6th Jul 2026"));
    }

    public static String reformatDate(String date) {
        String[] dateStringSplitted = date.split(" ");
        String day = dateStringSplitted[0].substring(0,dateStringSplitted[0].length() - 2);
        if (day.length() == 1) {
            day = "0" + day;
        }
        String month = switch (dateStringSplitted[1]) {
            case "Jan" -> "01";

            case "Feb" -> "02";

            case  "Mar" -> "03";

            case "Apr" -> "04";

            case "May" -> "05";

            case "Jun" -> "06";

            case "Jul" -> "07";

            case "Aug" -> "08";

            case "Sep" -> "09";

            case "Oct" -> "10";

            case "Nov" -> "11";

            case "Dec" -> "12";

            default -> "Invalid month";
        };
        return dateStringSplitted[2] + "-" + month + "-" + day;

    }
}
