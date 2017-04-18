package se.ugli.pineapple.api;

import se.ugli.jocote.Message;

@FunctionalInterface
public interface Filter {

    Envelope filter(Message message);

}
