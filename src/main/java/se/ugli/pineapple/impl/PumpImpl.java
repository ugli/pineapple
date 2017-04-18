package se.ugli.pineapple.impl;

import java.io.Closeable;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.ugli.jocote.Connection;
import se.ugli.jocote.Jocote;
import se.ugli.jocote.Message;
import se.ugli.jocote.Subscription;
import se.ugli.pineapple.api.Pipe;
import se.ugli.pineapple.api.Pump;
import se.ugli.pineapple.discovery.Discovery;
import se.ugli.pineapple.model.Component;

public class PumpImpl extends Item implements Consumer<Message>, Closeable {

    private static final Logger LOG = LoggerFactory.getLogger(PumpImpl.class);
    private final Subscription subscription;
    private final Connection connection;

    public PumpImpl(final Discovery discovery, final Component component) {
        super(component);
        final Pump pump = discovery.pump(name);
        final Pipe pipe = discovery.pipe(component.getOut().get(0).name);
        subscription = Jocote.subscribe(pump.url, this);
        connection = Jocote.connect(pipe.url);
        LOG.info("Pump created: {}", name);
    }

    @Override
    public void close() {
        subscription.close();
        connection.close();
    }

    @Override
    public void accept(final Message message) {
        connection.put(message);
    }

}
