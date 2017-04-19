package se.ugli.pineapple.system;

import static java.util.Objects.isNull;

import se.ugli.jocote.Connection;
import se.ugli.jocote.Message;
import se.ugli.pineapple.PineappleException;
import se.ugli.pineapple.api.Envelope;
import se.ugli.pineapple.api.Filter;
import se.ugli.pineapple.discovery.Discovery;
import se.ugli.pineapple.model.Component;

public class FilterActor extends ComponentActor {

    private final Filter filter;

    public FilterActor(Component component, Discovery discovery) {
        super(component, discovery);
        filter = discovery.filter(component.name);
    }

    @Override
    protected void consume(Message message) {
        final Envelope envelope = filter.filter(message);
        if (connectionByDestination.size() == 1) {
            connectionByDestination.values().iterator().next().put(envelope.message);
        } else {
            if (isNull(envelope.destination) && connectionByDestination.size() > 1) {
                throw new PineappleException("You have to choose destination: " + connectionByDestination.keySet());
            }
            final Connection connection = connectionByDestination.get(envelope.destination);
            if (isNull(connection)) {
                throw new PineappleException("Bad destination: " + envelope.destination + ". Valid are: "
                        + connectionByDestination.keySet());
            }
            connection.put(envelope.message);
        }
    }

}
