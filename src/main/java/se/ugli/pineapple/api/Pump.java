package se.ugli.pineapple.api;

import scala.concurrent.duration.FiniteDuration;
import se.ugli.pineapple.api.BaseConfiguration.ConfigurationBuilder;

@FunctionalInterface
public interface Pump extends Url, Configuration {

    static PumpBuilder builder(final String url) {
        return new PumpBuilder(url);
    }

    static class PumpBuilder extends ConfigurationBuilder<PumpBuilder> {

        private final String url;

        PumpBuilder(final String url) {
            this.url = url;
        }

        public Pump build() {
            return new PumpImpl(url, numberOfInstances, consumeType, idleDuration());
        }

        class PumpImpl extends BaseConfiguration implements Pump {

            final String url;

            PumpImpl(final String url, final int numberOfInstances, final ConsumeType consumeType,
                    final FiniteDuration idleDuration) {
                super(numberOfInstances, consumeType, idleDuration);
                this.url = url;
            }

            @Override
            public String url() {
                return url;
            }

        }

    }

}
