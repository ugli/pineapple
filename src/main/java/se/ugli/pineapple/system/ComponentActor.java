package se.ugli.pineapple.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.AbstractActor;
import se.ugli.jocote.Connection;
import se.ugli.jocote.Jocote;
import se.ugli.jocote.Message;
import se.ugli.jocote.Subscription;
import se.ugli.pineapple.api.Pipe;
import se.ugli.pineapple.discovery.Discovery;
import se.ugli.pineapple.model.Component;

abstract class ComponentActor extends AbstractActor {

    protected final Logger LOG = LoggerFactory.getLogger(getClass());
    protected final List<Subscription> subscriptions = new ArrayList<>();
    protected final Map<String, Connection> connectionByDestination = new HashMap<>();

    protected ComponentActor(Component component, Discovery discovery) {
        component.getIn().forEach(p -> addSubscription(discovery.pipe(p.name).url));
        component.getOut().forEach(p -> {
            final Pipe pipe = discovery.pipe(p.name);
            connectionByDestination.put(p.to.name, Jocote.connect(pipe.url));
        });
        LOG.info("{} {} created.", component.type(), component.name);
    }

    @Override
    public void postStop() {
        subscriptions.forEach(Subscription::close);
        connectionByDestination.values().forEach(Connection::close);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(Message.class, this::consume).matchAny(this::unknown).build();
    }

    protected void addSubscription(String url) {
        subscriptions.add(Jocote.subscribe(url, m -> self().tell(m, self())));
    }

    protected abstract void consume(Message message);

    private void unknown(Object message) {
        LOG.warn("Received unknown message: {}", message);
    }

}
