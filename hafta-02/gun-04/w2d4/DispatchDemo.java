package w2d4;

class Animal {
    public String sound() { return "..."; }
}
class Dog extends Animal {
    @Override
    public String sound() { return "Woof"; }
}
class Cat extends Animal {
    @Override
    public String sound() { return "Meow"; }
}

public class DispatchDemo {
    static String describe(Animal a) { return "animal says " + a.sound(); }
    static String describe(Dog d)    { return "dog says " + d.sound(); }

    public static void main(String[] args) {
        Animal a1 = new Dog();
        Dog d1 = new Dog();
        Animal a2 = new Cat();

        System.out.println(a1.sound());
        System.out.println(describe(a1));
        System.out.println(describe(d1));
        System.out.println(describe(a2));
        System.out.println(describe((Dog) a1));
        System.out.println(describe(new Animal()));
    }
}
