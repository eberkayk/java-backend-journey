import java.util.ArrayDeque;
import java.util.Deque;

public class Algo1 {

    public static boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(' || s.charAt(i) == '[' || s.charAt(i) == '{') {
                stack.push(s.charAt(i));
            }

            if (s.charAt(i) == ')' || s.charAt(i) == ']' || s.charAt(i) == '}') {
                if (stack.isEmpty()) {
                    return false;
                } else {
                    if ((s.charAt(i) == ')' && stack.peek() == '(') || (s.charAt(i) == ']' && stack.peek() == '[') || (s.charAt(i) == '}' && stack.peek() == '{')) {
                        stack.pop();
                    } else {
                        return false;
                    }
                }
            }
        }

        if (stack.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
