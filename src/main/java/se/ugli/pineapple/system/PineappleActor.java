package se.ugli.pineapple.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.AbstractActor;
import akka.actor.Props;
import se.ugli.pineapple.discovery.Discovery;
import se.ugli.pineapple.model.Model;

public class PineappleActor extends AbstractActor {

    private static final Logger LOG = LoggerFactory.getLogger(PineappleActor.class);

    public PineappleActor(final Model model, final Discovery discovery) {
        model.pumps.forEach(c -> context().actorOf(PumpActor.props(c, discovery)));
        model.filters.forEach(c -> context().actorOf(FilterActor.props(c, discovery)));
        model.sinks.forEach(c -> context().actorOf(SinkActor.props(c, discovery)));
    }

    public static Props props(final Model model, final Discovery discovery) {
        return Props.create(PineappleActor.class, () -> new PineappleActor(model, discovery));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().matchAny(message -> LOG.warn("Received unknown message: {}", message)).build();
    }

}
