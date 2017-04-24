package se.ugli.pineapple.system;

import akka.actor.Props;
import akka.routing.RoundRobinPool;
import se.ugli.jocote.Message;
import se.ugli.pineapple.api.Discovery;
import se.ugli.pineapple.api.Sink;
import se.ugli.pineapple.model.Component;

public class SinkActor extends ComponentActor {

    public SinkActor(final Sink sink, final Component component, final Discovery discovery) {
        super(sink, component, discovery);
        connectOut(component.name, sink.url());
    }

    public static Props props(final Component component, final Discovery discovery) {
        final Sink sink = discovery.sink(component.name);
        final Props result = Props.create(SinkActor.class, () -> new SinkActor(sink, component, discovery));
        final int numberOfInstances = sink.numberOfInstances();
        if (numberOfInstances > 1)
            return result.withRouter(new RoundRobinPool(numberOfInstances));
        return result;
    }

    @Override
    protected void consume(final Message message) {
        connectionByDestination.values().forEach(c -> c.put(message));
    }

}
