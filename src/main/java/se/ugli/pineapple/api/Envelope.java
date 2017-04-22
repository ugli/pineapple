package se.ugli.pineapple.api;

import java.util.Optional;

import se.ugli.jocote.Message;
import se.ugli.jocote.Message.MessageBuilder;

public class Envelope {

    public final Optional<String> destination;
    public final Message message;

    private Envelope(final Message message, final String destination) {
        this.destination = Optional.ofNullable(destination);
        this.message = message;
    }

    public static Envelope apply(final Message message) {
        return new Envelope(message, null);
    }

    public static Envelope apply(final byte[] message) {
        return new Envelope(new MessageBuilder().body(message).build(), null);
    }

    public static Envelope apply(final Message message, final String destination) {
        return new Envelope(message, destination);
    }

    public static Envelope apply(final byte[] message, final String destination) {
        return new Envelope(new MessageBuilder().body(message).build(), destination);
    }

}
