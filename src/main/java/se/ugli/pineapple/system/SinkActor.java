package se.ugli.pineapple.system;

import akka.actor.Props;
import se.ugli.jocote.Message;
import se.ugli.pineapple.discovery.Discovery;
import se.ugli.pineapple.model.Component;

public class SinkActor extends ComponentActor {

    public SinkActor(final Component component, final Discovery discovery) {
        super(component, discovery);
        addConnection(component.name, discovery.sink(component.name).url);
    }

    public static Props props(final Component component, final Discovery discovery) {
        return Props.create(SinkActor.class, () -> new SinkActor(component, discovery));
    }

    @Override
    protected void consume(final Message message) {
        connectionByDestination.values().forEach(c -> c.put(message));
    }

}
