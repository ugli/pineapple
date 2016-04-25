package se.ugli.pineapple;

import java.util.function.Consumer;

public class Pipeline<T> {

    private final Pump<T> pump;
    private final Consumer<T> pumpConsumer;

    public Pipeline(final Pump<T> pump, Consumer<T> pumpConsumer) {
        this.pump = pump;
        this.pumpConsumer = pumpConsumer;
    }

    public void start() {
        pump.start(pumpConsumer);
    }


}
