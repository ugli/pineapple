package se.ugli.pineapple.api;

public class Pump {

    public final String url;
    public final int numberOfInstances;

    private Pump(final String url, final int numberOfInstances) {
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

        public Pump build() {
            return new Pump(url, numberOfInstances);
        }

    }

}
