package w4d3;

public class LightDemo {
    public static void main(String[] args) {
        for (TrafficLight trafficLight : TrafficLight.values()) {
            System.out.println(trafficLight + " " + trafficLight.getSeconds() + "s -> " + trafficLight.next());
        }
        int sum = 0;
        for (TrafficLight trafficLight : TrafficLight.values()) {
            sum += trafficLight.getSeconds();
        }
        System.out.println("Cycle: " + sum + "s");
        System.out.println(TrafficLight.valueOf("GREEN").next());
    }
}

//**`LightDemo`** (`main`):
//        1. `values()` üzerinde dön, her ışık için: `<ad> <saniye>s -> <sonraki>`
//        2. `Cycle: ` + bütün ışıkların saniye toplamı + `s` (döngü ya da stream, serbest)
//        3. `valueOf("GREEN")`'in `next()`'ini yazdır