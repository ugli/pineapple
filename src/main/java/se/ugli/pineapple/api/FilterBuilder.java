package se.ugli.pineapple.api;

import se.ugli.jocote.Message;

public class FilterBuilder {

    private final Filter filter;
    private int numberOfInstances = 1;

    FilterBuilder(final Filter filter) {
        this.filter = filter;
    }

    public FilterBuilder numberOfInstances(final int numberOfInstances) {
        this.numberOfInstances = numberOfInstances;
        return this;
    }

    public Filter build() {
        return new FilterWithConfiguration(filter, numberOfInstances);
    }

    public static class FilterWithConfiguration implements Filter {

        private final Filter filter;
        public final int numberOfInstances;

        FilterWithConfiguration(final Filter filter, final int numberOfInstances) {
            this.filter = filter;
            this.numberOfInstances = numberOfInstances;
        }

        @Override
        public Envelope filter(final Message message) {
            return filter.filter(message);
        }

    }

}
