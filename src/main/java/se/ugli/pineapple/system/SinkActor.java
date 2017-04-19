package se.ugli.pineapple.system;

import se.ugli.jocote.Jocote;
import se.ugli.jocote.Message;
import se.ugli.pineapple.api.Sink;
import se.ugli.pineapple.discovery.Discovery;
import se.ugli.pineapple.model.Component;

public class SinkActor extends ComponentActor {

    public SinkActor(Component component, Discovery discovery) {
        super(component, discovery);
        final Sink sink = discovery.sink(component.name);
        connectionByDestination.put(component.name, Jocote.connect(sink.url));
    }

    @Override
    protected void consume(Message message) {
        connectionByDestination.values().forEach(c -> c.put(message));
    }

}
