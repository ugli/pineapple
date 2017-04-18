package se.ugli.pineapple.impl;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.ugli.jocote.Connection;
import se.ugli.jocote.Jocote;
import se.ugli.jocote.Message;
import se.ugli.jocote.Subscription;
import se.ugli.pineapple.api.Pipe;
import se.ugli.pineapple.api.Sink;
import se.ugli.pineapple.discovery.Discovery;
import se.ugli.pineapple.model.Component;

public class SinkImpl extends Item implements Consumer<Message>, Closeable {

    private static final Logger LOG = LoggerFactory.getLogger(SinkImpl.class);
    private final List<Subscription> subscriptions = new ArrayList<>();
    private final Connection connection;

    public SinkImpl(final Discovery discovery, final Component component) {
        super(component);
        final Sink sink = discovery.sink(name);
        component.getIn().forEach(p -> {
            final Pipe pipe = discovery.pipe(p.name);
            subscriptions.add(Jocote.subscribe(pipe.url, this));
        });
        connection = Jocote.connect(sink.url);
        LOG.info("Sink created: {}", name);
    }

    @Override
    public void close() throws IOException {
        subscriptions.forEach(Subscription::close);
        connection.close();
    }

    @Override
    public void accept(final Message message) {
        connection.put(message);
    }

}
