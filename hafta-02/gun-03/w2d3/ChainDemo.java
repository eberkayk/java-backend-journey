package w2d3;

class Vehicle {
    Vehicle() {
        System.out.println("Vehicle()");
    }
    Vehicle(String name) {
        System.out.println("Vehicle(" + name + ")");
    }
}

class Car extends Vehicle {
    Car() {
        System.out.println("Car()");
    }
    Car(String name) {
        super(name);
        System.out.println("Car(" + name + ")");
    }
}

class SportsCar extends Car {
    SportsCar() {
        this("Ferrari");
        System.out.println("SportsCar()");
    }
    SportsCar(String name) {
        System.out.println("SportsCar(" + name + ")");
    }
}

public class ChainDemo {
    public static void main(String[] args) {
        new Car();
        System.out.println("---");
        new Car("Golf");
        System.out.println("---");
        new SportsCar();
    }
}
