package w3d4;

import java.util.ArrayList;
import java.util.List;

public class RawListDemo {
    public static void main(String[] args) {
        List<String> raw = new ArrayList<>();      // bilerek tipsiz
        raw.add("Dune");
        for (Object o : raw) {
            String s = (String) o;
            System.out.println(s.toUpperCase());
        }
    }
}
