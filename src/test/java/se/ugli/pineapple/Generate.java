package se.ugli.pineapple;

import org.springframework.stereotype.Component;

import se.ugli.jocote.Message;
import se.ugli.pineapple.api.Envelope;
import se.ugli.pineapple.api.Filter;

@Component
public class Generate implements Filter {

    @Override
    public Envelope filter(final Message message) {
        return new Envelope(message);
    }

}
