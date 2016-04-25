package se.ugli.pineapple;

import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class PipelineBuilder<T> {

    private Pump<T> pump;
    private Consumer<T> sink;
    private final Queue<Pipe<T>> pipes = new LinkedBlockingQueue<>();
    private final Queue<UnaryOperator<T>> filters = new LinkedBlockingQueue<>();

    public PipelineBuilder<T> pump(final Pump<T> pump) {
        this.pump = pump;
        return this;
    }

    public PipelineBuilder<T> sink(final Consumer<T> sink) {
        this.sink = sink;
        return this;
    }

    public PipelineBuilder<T> pipe(final Pipe<T> pipe) {
        pipes.offer(pipe);
        return this;
    }

    public PipelineBuilder<T> filter(final UnaryOperator<T> filter) {
        filters.offer(filter);
        return this;
    }

    public Pipeline<T> build() {
        Objects.requireNonNull(pump, "A pump is required");
        Objects.requireNonNull(sink, "A sink is required");
        if (pipes.isEmpty())
            throw new IllegalStateException("There must be at least 1 pipe");
        else if (pipes.size() - 1 != filters.size())
            throw new IllegalStateException("Number of filters has to be one less then the number of pipes");
        Pipe<T> lastPipe = pipes.poll();
        pump.setup(lastPipe);
        while (!pipes.isEmpty()) {
            final Pipe<T> pipe = pipes.poll();
            lastPipe.setup(pipe, Optional.of(filters.poll()));
            lastPipe = pipe;
        }
        lastPipe.setup(sink, Optional.empty());
        return new Pipeline<>(pump);
    }

}
