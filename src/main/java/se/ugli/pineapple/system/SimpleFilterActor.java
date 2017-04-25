package se.ugli.pineapple.system;

import java.util.Optional;
import java.util.Set;

import akka.actor.Props;
import akka.routing.RoundRobinPool;
import se.ugli.jocote.Connection;
import se.ugli.jocote.Message;
import se.ugli.pineapple.PineappleException;
import se.ugli.pineapple.api.Discovery;
import se.ugli.pineapple.api.SimpleFilter;
import se.ugli.pineapple.model.Component;

public class SimpleFilterActor extends ComponentActor {

    private final SimpleFilter filter;

    public SimpleFilterActor(final SimpleFilter filter, final Component component, final Discovery discovery) {
        super(filter, component, discovery);
        this.filter = filter;
    }

    public static Props props(final Component component, final Discovery discovery) {
        final SimpleFilter filter = (SimpleFilter) discovery.filter(component.name);
        final Props result = Props.create(SimpleFilterActor.class,
                () -> new SimpleFilterActor(filter, component, discovery));
        final int numberOfInstances = filter.numberOfInstances();
        if (numberOfInstances > 1)
            return result.withRouter(new RoundRobinPool(numberOfInstances));
        return result;
    }

    @Override
    protected void consume(final Message inMsg) {
        filter.filter(inMsg).ifPresent(e -> connection(e.destination).put(e.message));
    }

    private Connection connection(final Optional<String> optionalDestination) {
        if (connectionByDestination.size() == 1)
            return connectionByDestination.values().iterator().next();
        final Set<String> validDestinations = connectionByDestination.keySet();
        final String destination = optionalDestination
                .orElseThrow(() -> new PineappleException("You have to choose destination: " + validDestinations));
        return Optional.ofNullable(connectionByDestination.get(destination)).orElseThrow(
                () -> new PineappleException("Bad destination: " + destination + ". Valid are: " + validDestinations));
    }

}
