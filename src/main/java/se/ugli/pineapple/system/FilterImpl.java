package se.ugli.pineapple.system;

import static java.util.Objects.isNull;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.ugli.jocote.Connection;
import se.ugli.jocote.Jocote;
import se.ugli.jocote.Message;
import se.ugli.jocote.Subscription;
import se.ugli.pineapple.PineappleException;
import se.ugli.pineapple.api.Envelope;
import se.ugli.pineapple.api.Filter;
import se.ugli.pineapple.api.Pipe;
import se.ugli.pineapple.discovery.Discovery;
import se.ugli.pineapple.model.Component;

public class FilterImpl extends Item implements Consumer<Message>, Closeable {

    private static final Logger LOG = LoggerFactory.getLogger(FilterImpl.class);
    private final Filter filter;
    private final List<Subscription> subscriptions = new ArrayList<>();
    private final Map<String, Connection> connectionByDestination = new HashMap<>();

    public FilterImpl(final Discovery discovery, final Component component) {
        super(component);
        filter = discovery.filter(name);
        component.getIn().forEach(p -> {
            final Pipe pipe = discovery.pipe(p.name);
            subscriptions.add(Jocote.subscribe(pipe.url, this));
        });
        component.getOut().forEach(p -> {
            final Pipe pipe = discovery.pipe(p.name);
            connectionByDestination.put(p.to.name, Jocote.connect(pipe.url));
        });
        LOG.info("Filter created: {}", name);
    }

    @Override
    public void close() {
        subscriptions.forEach(Subscription::close);
        connectionByDestination.values().forEach(Connection::close);
    }

    @SuppressWarnings("resource")
    @Override
    public void accept(final Message message) {
        final Envelope envelope = filter.filter(message);
        if (connectionByDestination.size() == 1)
            connectionByDestination.values().iterator().next().put(envelope.message);
        else {
            if (isNull(envelope.destination) && connectionByDestination.size() > 1)
                throw new PineappleException("You have to choose destination: " + connectionByDestination.keySet());
            final Connection connection = connectionByDestination.get(envelope.destination);
            if (isNull(connection))
                throw new PineappleException("Bad destination: " + envelope.destination + ". Valid are: "
                        + connectionByDestination.keySet());
            connection.put(envelope.message);
        }
    }

}
