package se.ugli.pineapple;

import java.util.function.Consumer;

@FunctionalInterface
public interface Pump<T> {

    void start(Consumer<T> consumer);

}
