package se.ugli.pineapple.api;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import se.ugli.pineapple.discovery.SpringDiscovery;

public interface Discovery {

    Filter filter(String name);

    Pump pump(String name);

    Sink sink(String name);

    default Pipe pipe(final String name) {
        return Pipe.apply("ram:/" + name);
    }

    // TODO
    static Discovery discovery = new SpringDiscovery(new AnnotationConfigApplicationContext("se.ugli"));

    public static Discovery create() {
        return discovery;
    }

}