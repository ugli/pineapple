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
import se.ugli.pineapple.discovery.Discovery;
import se.ugli.pineapple.model.Component;

abstract class ComponentActor extends AbstractActor {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final List<Subscription> subscriptions = new ArrayList<>();
    protected final Map<String, Connection> connectionByDestination = new HashMap<>();

    protected ComponentActor(final Component component, final Discovery discovery) {
        component.getIn().forEach(p -> addSubscription(discovery.pipe(p.name).url));
        component.getOut().forEach(p -> addConnection(p.to.name, discovery.pipe(p.name).url));
        log.info("{} {} created", component.type(), component.name);
    }

    protected void addSubscription(final String url) {
        subscriptions.add(Jocote.subscribe(url, m -> self().tell(m, self())));
    }

    protected void addConnection(final String destination, final String url) {
        connectionByDestination.put(destination, Jocote.connect(url));
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

    protected abstract void consume(Message message);

    private void unknown(final Object message) {
        throw new IllegalStateException("Unknown message type: " + message.getClass().getName());
    }

}
