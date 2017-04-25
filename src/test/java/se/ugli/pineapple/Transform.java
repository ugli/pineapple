package se.ugli.pineapple;

import java.util.Optional;

import org.springframework.stereotype.Component;

import se.ugli.jocote.Message;
import se.ugli.pineapple.api.Envelope;
import se.ugli.pineapple.api.SimpleFilter;

@Component
public class Transform implements SimpleFilter {

    @Override
    public Optional<Envelope> filter(final Message message) {
        return Envelope.apply((new String(message.body()) + " T").getBytes());
    }

}
