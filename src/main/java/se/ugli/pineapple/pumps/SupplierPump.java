package se.ugli.pineapple.pumps;

import se.ugli.pineapple.Pump;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SupplierPump<T> implements Pump<T> {

    private final Supplier<T> supplier;

    public SupplierPump(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public void start(Consumer<T> consumer) {
        consumer.accept(supplier.get());
    }
}
