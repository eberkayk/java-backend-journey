import java.util.ArrayDeque;
import java.util.Objects;

public class Algo1 {
    public boolean backspaceCompare(String s, String t) {
        ArrayDeque<String> stack1 = new ArrayDeque<>();
        ArrayDeque<String> stack2 = new ArrayDeque<>();
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            stack1.push(String.valueOf(s.charAt(i)));
            if (Objects.equals(stack1.peek(), "#")) {
                stack1.pop();
                if (!stack1.isEmpty()) {
                    stack1.pop();
                }
            }
        }

        while (!stack1.isEmpty()) {
            sb1.append(stack1.pop());
        }

        for (int i = 0; i < t.length(); i++) {
            stack2.push(String.valueOf(t.charAt(i)));
            if (Objects.equals(stack2.peek(), "#")) {
                stack2.pop();
                if (!stack2.isEmpty()) {
                    stack2.pop();
                }
            }
        }

        while (!stack2.isEmpty()) {
            sb2.append(stack2.pop());
        }

        return sb1.reverse().toString().contentEquals(sb2.reverse());
    }
}
