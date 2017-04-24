package se.ugli.pineapple.api;

import scala.concurrent.duration.FiniteDuration;
import se.ugli.pineapple.api.BaseConfiguration.ConfigurationBuilder;

@FunctionalInterface
public interface Sink extends Url, Configuration {

    static SinkBuilder builder(final String url) {
        return new SinkBuilder(url);
    }

    static class SinkBuilder extends ConfigurationBuilder<SinkBuilder> {

        final String url;

        SinkBuilder(final String url) {
            this.url = url;
        }

        public Sink build() {
            return new SinkImpl(url, numberOfInstances, consumeType, idleDuration());
        }

        class SinkImpl extends BaseConfiguration implements Sink {
            final String url;

            SinkImpl(final String url, final int numberOfInstances, final ConsumeType consumeType,
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
