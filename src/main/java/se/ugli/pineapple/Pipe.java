package se.ugli.pineapple;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class Pipe<T> implements Consumer<T> {

    private Optional<UnaryOperator<T>> filter;
    private Consumer<T> consumer;

    void setup(final Consumer<T> consumer, final Optional<UnaryOperator<T>> filter) {
        this.consumer = consumer;
        this.filter = filter;
    }

    @Override
    public void accept(final T msg) {
        if (filter.isPresent())
            consumer.accept(filter.get().apply(msg));
        else
            consumer.accept(msg);
    }

}
