package se.ugli.pineapple.system;

import static se.ugli.pineapple.api.ConsumeType.PULL;
import static se.ugli.pineapple.api.ConsumeType.SUBSCRIBE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.AbstractActor;
import akka.actor.ActorSystem;
import scala.concurrent.duration.FiniteDuration;
import se.ugli.jocote.Connection;
import se.ugli.jocote.Jocote;
import se.ugli.jocote.Message;
import se.ugli.jocote.Subscription;
import se.ugli.pineapple.api.Configuration;
import se.ugli.pineapple.api.ConsumeType;
import se.ugli.pineapple.api.Discovery;
import se.ugli.pineapple.model.Component;

abstract class ComponentActor extends AbstractActor {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final Map<String, Connection> connectionByDestination = new HashMap<>();

    private final List<Connection> inConnections = new ArrayList<>();
    private final List<Subscription> subscriptions = new ArrayList<>();
    private final String componentName;
    private final ConsumeType consumeType;
    private final Supplier<FiniteDuration> idleDurationSupplier;

    protected ComponentActor(final Configuration configuration, final Component component, final Discovery discovery) {
        consumeType = configuration.consumeType();
        idleDurationSupplier = configuration::idleDuration;
        componentName = component.name;
        log.info("[{}] creating {}", componentName, component.type());
        component.getIn().forEach(p -> connectIn(discovery.pipe(p.name).url()));
        component.getOut().forEach(p -> connectOut(p.to.name, discovery.pipe(p.name).url()));
        if (consumeType == PULL)
            inConnections.forEach(c -> self().tell(c, self()));
    }

    protected void connectIn(final String url) {
        if (consumeType == SUBSCRIBE) {
            log.info("[{}] added subscription {}", componentName, url);
            subscriptions.add(Jocote.subscribe(url, m -> self().tell(m, self())));
        }
        else if (consumeType == PULL) {
            log.info("[{}] added in connection {}", componentName, url);
            inConnections.add(Jocote.connect(url));
            subscriptions.add(Jocote.subscribe(url, m -> self().tell(m, self())));
        }
        else
            throw new IllegalStateException(Objects.toString(consumeType));

    }

    protected void connectOut(final String destination, final String url) {
        log.info("[{}] added out connection {}", componentName, url);
        connectionByDestination.put(destination, Jocote.connect(url));
    }

    @Override
    public void postStop() {
        subscriptions.forEach(Subscription::close);
        connectionByDestination.values().forEach(Connection::close);
        inConnections.forEach(Connection::close);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(Message.class, this::consume).match(Connection.class, this::pull)
                .matchAny(this::unknown).build();
    }

    private void pull(final Connection connection) {
        final Optional<Message> optMsg = connection.get();
        if (optMsg.isPresent()) {
            consume(optMsg.get());
            self().tell(connection, self());
        }
        else {
            final ActorSystem system = context().system();
            system.scheduler().scheduleOnce(idleDurationSupplier.get(), self(), connection, system.dispatcher(),
                    self());
        }
    }

    protected abstract void consume(Message message);

    private void unknown(final Object message) {
        throw new IllegalStateException("Unknown message type: " + message.getClass().getName());
    }

}
