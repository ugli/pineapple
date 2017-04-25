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

    public static Optional<Envelope> apply(final Message message) {
        return Optional.of(new Envelope(message, null));
    }

    public static Optional<Envelope> apply(final byte[] message) {
        return Optional.of(new Envelope(new MessageBuilder().body(message).build(), null));
    }

    public static Optional<Envelope> apply(final Message message, final String destination) {
        return Optional.of(new Envelope(message, destination));
    }

    public static Optional<Envelope> apply(final byte[] message, final String destination) {
        return Optional.of(new Envelope(new MessageBuilder().body(message).build(), destination));
    }

}
