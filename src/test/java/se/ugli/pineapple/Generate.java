package se.ugli.pineapple;

import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import se.ugli.jocote.Message;
import se.ugli.pineapple.api.Envelope;
import se.ugli.pineapple.api.StreamFilter;

@Component
public class Generate implements StreamFilter {

    @Override
    public int numberOfInstances() {
        return 2;
    }

    @Override
    public Stream<Envelope> filter(final Stream<Message> messages) {
        return messages.map(m -> Envelope.apply((new String(m.body()) + " G").getBytes()));
    }

}
