package se.ugli.pineapple.system;

import akka.actor.Props;
import se.ugli.jocote.Message;
import se.ugli.pineapple.discovery.Discovery;
import se.ugli.pineapple.model.Component;

public class PumpActor extends ComponentActor {

    public PumpActor(final Component component, final Discovery discovery) {
        super(component, discovery);
        addSubscription(discovery.pump(component.name).url);
    }

    public static Props props(final Component component, final Discovery discovery) {
        return Props.create(PumpActor.class, () -> new PumpActor(component, discovery));
    }

    @Override
    protected void consume(final Message message) {
        connectionByDestination.values().forEach(c -> c.put(message));
    }

}
