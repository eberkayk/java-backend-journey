package w5d5;

import java.util.ArrayList;
import java.util.List;

public class Algo1 {
    public static void main(String[] args) {

    }

    public static String destCity(List<List<String>> paths) {
        List<String> from = new ArrayList<>();
        for (int i = 0; i < paths.size(); i++) {
            List<String> temp = paths.get(i);
            from.add(temp.get(0));
        }
        for (int i = 0; i < paths.size(); i++) {
            List<String> temp = paths.get(i);
            if (!from.contains(temp.getLast())) {
                return temp.getLast();
            }
        }
        return "";
    }
}
