package se.ugli.pineapple.discovery;

import java.util.Map.Entry;
import java.util.Optional;

import org.springframework.context.ApplicationContext;

import se.ugli.pineapple.PineappleException;
import se.ugli.pineapple.api.Filter;
import se.ugli.pineapple.api.Pipe;
import se.ugli.pineapple.api.Pump;
import se.ugli.pineapple.api.Sink;

public class SpringDiscovery implements Discovery {

    private final ApplicationContext cxt;

    public SpringDiscovery(final ApplicationContext cxt) {
        this.cxt = cxt;
    }

    @Override
    public Filter filter(final String name) {
        return findBean(name, Filter.class);
    }

    @Override
    public Pump pump(final String name) {
        return findBean(name, Pump.class);
    }

    @Override
    public Sink sink(final String name) {
        return findBean(name, Sink.class);
    }

    @Override
    public Pipe pipe(final String name) {
        return findBeanOpt(name, Pipe.class).orElse(Discovery.super.pipe(name));
    }

    private <T> T findBean(final String name, final Class<T> type) {
        return findBeanOpt(name, type)
                .orElseThrow(() -> new PineappleException("No bean with name " + name + " and type " + type.getName()));
    }

    private <T> Optional<T> findBeanOpt(final String name, final Class<T> type) {
        return cxt.getBeansOfType(type).entrySet().stream().filter(e -> e.getKey().equalsIgnoreCase(name))
                .map(Entry::getValue).findFirst();
    }

}
