package se.ugli.pineapple.discovery;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import se.ugli.pineapple.api.Filter;
import se.ugli.pineapple.api.Pipe;
import se.ugli.pineapple.api.Pump;
import se.ugli.pineapple.api.Sink;

public interface Discovery {

    Filter filter(String name);

    Pump pump(String name);

    Sink sink(String name);

    default Pipe pipe(final String name) {
        return new Pipe("ram:/" + name);
    }

    public static Discovery create() {
        // TODO
        final ApplicationContext ctx = new AnnotationConfigApplicationContext("se.ugli");
        return new SpringDiscovery(ctx);
    }

}