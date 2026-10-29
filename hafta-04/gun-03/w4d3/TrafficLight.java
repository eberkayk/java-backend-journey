package w4d3;

public enum TrafficLight {
    RED(30),
    YELLOW(5),
    GREEN(25);

    private final int seconds;

    TrafficLight(int seconds) {
        this.seconds = seconds;
    }

    public int getSeconds() {
        return seconds;
    }

    public TrafficLight next() {
        return switch (this) {
            case RED -> GREEN;
            case GREEN -> YELLOW;
            case YELLOW -> RED;
        };
    }
}

//**`TrafficLight`** (enum, kendi dosyasında):
//        - Sabitler, bu sırayla: `RED` (30 saniye), `YELLOW` (5 saniye), `GREEN` (25 saniye)
//        - Field: `private final int seconds`
//        - Constructor: `TrafficLight(int seconds)`
//        - `public int getSeconds()`
//        - `public TrafficLight next()`: sıradaki ışık. Kural: `RED → GREEN`, `GREEN → YELLOW`, `YELLOW → RED`. Nasıl yazacağın (switch ya da başka yol) **senin tasarım kararın**.
