package w3d5;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CountdownDemo {
    public static void main(String[] args) {
        Countdown c = new Countdown(3);
        for (Integer x : c) {
            System.out.print(x + " ");
        }

        for (Integer x : c) {
            System.out.print(x + " ");
        }

        Countdown c1 = new Countdown(0);
        for (Integer x : c1) {
            System.out.print(x + " ");
        }


        try {
            Countdown c2 = new Countdown(-1);
            for (Integer x : c2) {
                System.out.print(x + " ");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }


        List<String> names = new ArrayList<>();
        names.add("Ali");
        names.add("Bora");
        names.add("Can");
        names.add("Deniz");
        names.add("Ece");
        Iterator<String> it3 = names.iterator();
        while (it3.hasNext()) {
            String s = it3.next();
            if (s.length() > 3) {
                System.out.println(s);
            }
        }
    }
}
