package se.ugli.pineapple;

import se.ugli.jocote.Message;
import se.ugli.pineapple.api.Envelope;
import se.ugli.pineapple.api.Filter;

//@Component
public class Distribute implements Filter {

    @Override
    public Envelope filter(final Message message) {
        System.out.println(message);
        System.out.println(getClass());
        return new Envelope(message);
    }

}
