package se.ugli.pineapple.system;

import akka.actor.Props;
import akka.routing.RoundRobinPool;
import se.ugli.jocote.Message;
import se.ugli.pineapple.api.Sink;
import se.ugli.pineapple.discovery.Discovery;
import se.ugli.pineapple.model.Component;

public class SinkActor extends ComponentActor {

    public SinkActor(final Component component, final Discovery discovery) {
        super(component, discovery);
        addConnection(component.name, discovery.sink(component.name).url);
    }

    public static Props props(final Component component, final Discovery discovery) {
        final Sink sink = discovery.sink(component.name);
        final Props result = Props.create(SinkActor.class, () -> new SinkActor(component, discovery));
        if (sink.numberOfInstances > 1)
            return result.withRouter(new RoundRobinPool(sink.numberOfInstances));
        return result;
    }

    @Override
    protected void consume(final Message message) {
        connectionByDestination.values().forEach(c -> c.put(message));
    }

}
