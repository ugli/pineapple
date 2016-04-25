package se.ugli.pineapple;

import java.util.function.Consumer;

public interface Pump<T> {

    void setup(final Consumer<T> consumer);

    void start();

}
