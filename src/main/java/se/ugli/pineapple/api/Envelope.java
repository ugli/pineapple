package se.ugli.pineapple.api;

import se.ugli.jocote.Message;

public class Envelope {

    public final String destination;
    public final Message message;

    public Envelope(final Message message, final String destination) {
        this.destination = destination;
        this.message = message;
    }

    public Envelope(final Message message) {
        this(message, null);
    }

}
