package se.ugli.pineapple.api;

import se.ugli.jocote.Message;

@FunctionalInterface
public interface Filter {

    Envelope filter(Message message);

    static FilterBuilder builder(final Filter filter) {
        return new FilterBuilder(filter);
    }

}
