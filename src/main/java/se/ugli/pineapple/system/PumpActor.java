package se.ugli.pineapple.system;

import akka.actor.Props;
import akka.routing.RoundRobinPool;
import se.ugli.jocote.Message;
import se.ugli.pineapple.api.Pump;
import se.ugli.pineapple.discovery.Discovery;
import se.ugli.pineapple.model.Component;

public class PumpActor extends ComponentActor {

    public PumpActor(final Pump pump, final Component component, final Discovery discovery) {
        super(component, discovery);
        addSubscription(pump.url);
    }

    public static Props props(final Component component, final Discovery discovery) {
        final Pump pump = discovery.pump(component.name);
        final Props result = Props.create(PumpActor.class, () -> new PumpActor(pump, component, discovery));
        if (pump.numberOfInstances > 1)
            return result.withRouter(new RoundRobinPool(pump.numberOfInstances));
        return result;
    }

    @Override
    protected void consume(final Message message) {
        connectionByDestination.values().forEach(c -> c.put(message));
    }

}
