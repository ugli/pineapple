package se.ugli.pineapple.system;

import se.ugli.jocote.Message;
import se.ugli.pineapple.discovery.Discovery;
import se.ugli.pineapple.model.Component;

public class PumpActor extends ComponentActor {

    public PumpActor(Component component, Discovery discovery) {
        super(component, discovery);
        addSubscription(discovery.pump(component.name).url);
    }

    @Override
    protected void consume(Message message) {
        connectionByDestination.values().forEach(c -> c.put(message));
    }

}
