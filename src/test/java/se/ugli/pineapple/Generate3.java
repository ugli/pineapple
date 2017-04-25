package se.ugli.pineapple;

import java.util.Optional;

import se.ugli.jocote.Message;
import se.ugli.pineapple.api.Envelope;
import se.ugli.pineapple.api.SimpleFilter;

//@Component
public class Generate3 implements SimpleFilter {

    @Override
    public Optional<Envelope> filter(final Message message) {
        return Envelope.optional((new String(message.body()) + " G").getBytes());
    }

    @Override
    public int numberOfInstances() {
        return 2;
    }

}
