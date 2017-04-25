package se.ugli.pineapple.system;

import static se.ugli.pineapple.api.ConsumeType.PULL;
import static se.ugli.pineapple.api.ConsumeType.STREAM;
import static se.ugli.pineapple.api.ConsumeType.SUBSCRIBE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

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
        log.info("[{}] creating {} with {}", componentName, component.type(), consumeType);
        component.getIn().forEach(p -> connectIn(discovery.pipe(p.name).url()));
        component.getOut().forEach(p -> connectOut(p.to.name, discovery.pipe(p.name).url()));
    }

    @SuppressWarnings("resource")
    protected void connectIn(final String url) {
        if (consumeType == SUBSCRIBE) {
            log.info("[{}] added subscription {}", componentName, url);
            subscriptions.add(Jocote.subscribe(url, m -> self().tell(m, self())));
        }
        else if (consumeType == PULL || consumeType == STREAM) {
            log.info("[{}] added in connection {}", componentName, url);
            final Connection connection = Jocote.connect(url);
            inConnections.add(connection);
            noIdle(connection);
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
        return receiveBuilder().match(Message.class, this::consume).match(Connection.class, this::action)
                .matchAny(this::unknown).build();
    }

    private void action(final Connection connection) {
        if (consumeType == PULL)
            pull(connection);
        else
            stream(connection);

    }

    private void stream(final Connection connection) {
        if (consume(connection.messageStream()))
            noIdle(connection);
        else
            idle(connection);
    }

    private void pull(final Connection connection) {
        final Optional<Message> optMsg = connection.get();
        if (optMsg.isPresent()) {
            consume(optMsg.get());
            noIdle(connection);
        }
        else
            idle(connection);
    }

    private void idle(final Connection connection) {
        final ActorSystem system = context().system();
        system.scheduler().scheduleOnce(idleDurationSupplier.get(), self(), connection, system.dispatcher(), self());
    }

    private void noIdle(final Connection connection) {
        self().tell(connection, self());
    }

    protected abstract void consume(Message message);

    protected abstract boolean consume(Stream<Message> messages);

    private void unknown(final Object message) {
        throw new IllegalStateException("Unknown message type: " + message.getClass().getName());
    }

}
