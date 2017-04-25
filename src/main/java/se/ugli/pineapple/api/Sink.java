package se.ugli.pineapple.api;

import scala.concurrent.duration.FiniteDuration;

@FunctionalInterface
public interface Sink extends Configuration {

    String url();

    static SinkBuilder builder(final String url) {
        return new SinkBuilder(url);
    }

    static class SinkBuilder extends ConfigurationBuilder<SinkBuilder, Sink> {

        final String url;

        SinkBuilder(final String url) {
            this.url = url;
        }

        @Override
        public Sink build() {
            return new SinkImpl(url, numberOfInstances, consumeType, idleDuration(), streamLimit);
        }

        class SinkImpl extends ConfigurationBase implements Sink {
            final String url;

            SinkImpl(final String url, final int numberOfInstances, final ConsumeType consumeType,
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
