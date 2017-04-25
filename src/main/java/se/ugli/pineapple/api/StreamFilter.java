package se.ugli.pineapple.api;

import static se.ugli.pineapple.api.ConsumeType.PULL;

import java.util.stream.Stream;

import scala.concurrent.duration.FiniteDuration;
import se.ugli.jocote.Message;

@FunctionalInterface
public interface StreamFilter extends Filter {

    Stream<Envelope> filter(Stream<Message> messages);

    static StreamFilterBuilder builder(final StreamFilter filter) {
        return new StreamFilterBuilder(filter);
    }

    class StreamFilterBuilder extends ConfigurationBuilder<StreamFilterBuilder, StreamFilter> {

        final StreamFilter filter;

        StreamFilterBuilder(final StreamFilter filter) {
            this.filter = filter;
        }

        @Override
        public StreamFilter build() {
            return new StreamFilterImpl(filter, numberOfInstances, PULL, idleDuration(), streamLimit);
        }

        static class StreamFilterImpl extends ConfigurationBase implements StreamFilter {

            final StreamFilter filter;

            StreamFilterImpl(final StreamFilter filter, final int numberOfInstances, final ConsumeType consumeType,
                    final FiniteDuration idleDuration, final long streamLimit) {
                super(numberOfInstances, consumeType, idleDuration, streamLimit);
                this.filter = filter;
            }

            @Override
            public Stream<Envelope> filter(final Stream<Message> messages) {
                return filter.filter(messages);
            }

        }

    }

}
