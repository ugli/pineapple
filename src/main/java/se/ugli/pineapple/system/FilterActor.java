package se.ugli.pineapple.system;

import java.util.Optional;
import java.util.Set;

import akka.actor.Props;
import se.ugli.jocote.Connection;
import se.ugli.jocote.Message;
import se.ugli.pineapple.PineappleException;
import se.ugli.pineapple.api.Envelope;
import se.ugli.pineapple.api.Filter;
import se.ugli.pineapple.discovery.Discovery;
import se.ugli.pineapple.model.Component;

public class FilterActor extends ComponentActor {

    private final Filter filter;

    public FilterActor(final Component component, final Discovery discovery) {
        super(component, discovery);
        filter = discovery.filter(component.name);
    }

    public static Props props(final Component component, final Discovery discovery) {
        return Props.create(FilterActor.class, () -> new FilterActor(component, discovery));
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
