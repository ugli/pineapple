package se.ugli.pineapple.system;

import java.util.Optional;
import java.util.Set;

import akka.actor.Props;
import akka.routing.RoundRobinPool;
import se.ugli.jocote.Connection;
import se.ugli.jocote.Message;
import se.ugli.pineapple.PineappleException;
import se.ugli.pineapple.api.Envelope;
import se.ugli.pineapple.api.Filter;
import se.ugli.pineapple.api.FilterBuilder.FilterWithConfiguration;
import se.ugli.pineapple.discovery.Discovery;
import se.ugli.pineapple.model.Component;

public class FilterActor extends ComponentActor {

    private final Filter filter;

    public FilterActor(final Filter filter, final Component component, final Discovery discovery) {
        super(component, discovery);
        this.filter = filter;
    }

    public static Props props(final Component component, final Discovery discovery) {
        final Filter filter = discovery.filter(component.name);
        final Props result = Props.create(FilterActor.class, () -> new FilterActor(filter, component, discovery));
        if (filter instanceof FilterWithConfiguration) {
            final FilterWithConfiguration filterWithConf = (FilterWithConfiguration) filter;
            if (filterWithConf.numberOfInstances > 1)
                return result.withRouter(new RoundRobinPool(filterWithConf.numberOfInstances));
        }
        return result;
    }

    @Override
    protected void consume(final Message inMsg) {
        final Envelope envelope = filter.filter(inMsg);
        connection(envelope.destination).put(envelope.message);
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
