package se.ugli.pineapple.pumps;

import se.ugli.pineapple.Pump;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SupplierPump<T> implements Pump<T> {

    private final Supplier<T> supplier;
    private Consumer<T> consumer;

    public SupplierPump(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public void setup(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void start() {
        consumer.accept(supplier.get());
    }
}
