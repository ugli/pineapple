package se.ugli.pineapple.api;

import scala.concurrent.duration.FiniteDuration;

@FunctionalInterface
public interface Pump extends Url, Configuration {

    static PumpBuilder builder(final String url) {
        return new PumpBuilder(url);
    }

    static class PumpBuilder extends ConfigurationBuilder<PumpBuilder, Pump> {

        private final String url;

        PumpBuilder(final String url) {
            this.url = url;
        }

        @Override
        public Pump build() {
            return new PumpImpl(url, numberOfInstances, consumeType, idleDuration(), streamLimit);
        }

        class PumpImpl extends ConfigurationBase implements Pump {

            final String url;

            PumpImpl(final String url, final int numberOfInstances, final ConsumeType consumeType,
                    final FiniteDuration idleDuration, final long streamLimit) {
                super(numberOfInstances, consumeType, idleDuration, streamLimit);
                this.url = url;
            }

            @Override
            public String url() {
                return url;
            }

        }

    }

}
