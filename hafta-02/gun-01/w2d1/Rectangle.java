package w2d1;

public class Rectangle {
    double width;
    double height;

    Rectangle(double width,double height) {
        this.height = height;
        this.width = width;
    }

    Rectangle(double side) {
        this(side, side);
    }

    double area() {
        return height * width;
    }

    double perimeter() {
        return 2 * (height + width);
    }

    boolean isSquare() {
        return height == width;
    }

    void scale(double factor) {
        this.height = height * factor;
        this.width = width * factor;
    }
}
