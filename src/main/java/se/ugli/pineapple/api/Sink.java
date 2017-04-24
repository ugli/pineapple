package se.ugli.pineapple.api;

public class Sink {

    public final String url;
    public final int numberOfInstances;

    private Sink(final String url, final int numberOfInstances) {
        this.url = url;
        this.numberOfInstances = numberOfInstances;
    }

    public static Builder builder(final String url) {
        return new Builder(url);
    }

    public static class Builder {

        private final String url;
        private int numberOfInstances = 1;

        Builder(final String url) {
            this.url = url;
        }

        public Builder numberOfInstances(final int numberOfInstances) {
            this.numberOfInstances = numberOfInstances;
            return this;
        }

        public Sink build() {
            return new Sink(url, numberOfInstances);
        }

    }

}
