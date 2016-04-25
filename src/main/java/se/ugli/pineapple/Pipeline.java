package se.ugli.pineapple;

public class Pipeline<T> {

    private final Pump<T> pump;

    public Pipeline(final Pump<T> pump) {
        this.pump = pump;
    }

    public void start() {
        pump.start();
    }


}
