package se.ugli.pineapple;

import se.ugli.pineapple.pumps.SupplierPump;

public class Test {

    public static void main(final String[] args) {
        final Pipeline<String> pipeline = new PipelineBuilder<String>()
                .pump(new SupplierPump<>(() -> "hej"))
                .pipe(new Pipe<>())
                .filter(t -> t + "1")
                .pipe(new Pipe<>())
                .filter(t -> t + "2")
                .pipe(new Pipe<>())
                .sink(System.out::println)
                .build();
        pipeline.start();
    }

}
