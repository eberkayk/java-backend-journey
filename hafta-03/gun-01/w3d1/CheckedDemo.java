package w3d1;

import java.io.IOException;

public class CheckedDemo {
    public static void main(String[] args) {
        try {
            readConfig("");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    static String readConfig(String path) throws java.io.IOException {
        if (path.isBlank()) {
            throw new java.io.IOException("No config file");
        }
        return "config: " + path;
    }
}
