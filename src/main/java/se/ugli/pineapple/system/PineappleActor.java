package se.ugli.pineapple.system;

import akka.actor.AbstractActor;
import akka.actor.Props;
import se.ugli.pineapple.discovery.Discovery;
import se.ugli.pineapple.model.Model;

public class PineappleActor extends AbstractActor {

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
        return receiveBuilder().matchAny(this::unknown).build();
    }

    private void unknown(final Object message) {
        throw new IllegalStateException("Unknown message type: " + message.getClass().getName());
    }

}
