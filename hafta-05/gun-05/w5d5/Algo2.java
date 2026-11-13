package w5d5;

public class Algo2 {
    static void main(String[] args) {
        System.out.println(largestOddNumber("4206"));
    }

    public static String largestOddNumber(String num) {
        StringBuilder sb = new StringBuilder(num);
        for (int i = num.length() - 1; i >= 0; i--) {
            if (Integer.parseInt(String.valueOf(num.charAt(i))) %2 == 0) {
                sb.deleteCharAt(i);
            } else {
                break;
            }
        }
        return sb.toString();
    }
}
