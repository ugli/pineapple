package se.ugli.pineapple.api;

import java.util.Optional;

import scala.concurrent.duration.FiniteDuration;
import se.ugli.jocote.Message;

@FunctionalInterface
public interface SimpleFilter extends Filter {

    Optional<Envelope> filter(Message message);

    static SimpleFilterBuilder builder(final SimpleFilter filter) {
        return new SimpleFilterBuilder(filter);
    }

    class SimpleFilterBuilder extends ConfigurationBuilder<SimpleFilterBuilder, SimpleFilter> {

        private final SimpleFilter filter;

        SimpleFilterBuilder(final SimpleFilter filter) {
            this.filter = filter;
        }

        @Override
        public SimpleFilter build() {
            return new SimpleFilterImpl(filter, numberOfInstances, consumeType, idleDuration(), streamLimit);
        }

        static class SimpleFilterImpl extends ConfigurationBase implements SimpleFilter {

            final SimpleFilter filter;

            SimpleFilterImpl(final SimpleFilter filter, final int numberOfInstances, final ConsumeType consumeType,
                    final FiniteDuration idleDuration, final long streamLimit) {
                super(numberOfInstances, consumeType, idleDuration, streamLimit);
                this.filter = filter;
            }

            @Override
            public Optional<Envelope> filter(final Message message) {
                return filter.filter(message);
            }

        }

    }

}
