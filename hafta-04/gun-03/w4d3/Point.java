package w4d3;

public record Point(int x, int y) {
    public Point {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Coordinates must not be negative");
        }
    }

    public int manhattan(Point other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }
}

//**`Point`** (record, kendi dosyasında):
//        - Alanlar: `int x`, `int y`
//        - Compact constructor: `x` ya da `y` negatifse → `IllegalArgumentException("Coordinates must not be negative")`
//        - `public int manhattan(Point other)`: `|x1 - x2| + |y1 - y2|` (`Math.abs`)
