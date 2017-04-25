package se.ugli.pineapple.system;

import static java.util.stream.Collectors.groupingBy;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import akka.actor.Props;
import akka.routing.RoundRobinPool;
import se.ugli.jocote.Connection;
import se.ugli.jocote.Message;
import se.ugli.pineapple.PineappleException;
import se.ugli.pineapple.api.Discovery;
import se.ugli.pineapple.api.Envelope;
import se.ugli.pineapple.api.Filter;
import se.ugli.pineapple.api.SimpleFilter;
import se.ugli.pineapple.api.StreamFilter;
import se.ugli.pineapple.model.Component;

public class FilterActor extends ComponentActor {

    private final Filter filter;

    public FilterActor(final Filter filter, final Component component, final Discovery discovery) {
        super(filter, component, discovery);
        this.filter = filter;
    }

    public static Props props(final Component component, final Discovery discovery) {
        final Filter filter = discovery.filter(component.name);
        final Props result = Props.create(FilterActor.class, () -> new FilterActor(filter, component, discovery));
        final int numberOfInstances = filter.numberOfInstances();
        if (numberOfInstances > 1)
            return result.withRouter(new RoundRobinPool(numberOfInstances));
        return result;
    }

    @Override
    protected void consume(final Message inMsg) {
        ((SimpleFilter) filter).filter(inMsg).ifPresent(e -> connection(e.destination).put(e.message));
    }

    @Override
    protected boolean consume(final Stream<Message> messages) {
        final Map<Optional<String>, List<Envelope>> map = ((StreamFilter) filter)
                .filter(messages.limit(filter.streamLimit())).collect(groupingBy(e -> e.destination));
        map.entrySet().forEach(e -> {
            final Stream<Message> newMessages = e.getValue().stream().map(s -> s.message);
            connection(e.getKey()).put(newMessages);
        });
        return !map.isEmpty();
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
