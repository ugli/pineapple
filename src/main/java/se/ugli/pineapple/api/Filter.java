package se.ugli.pineapple.api;

import scala.concurrent.duration.FiniteDuration;
import se.ugli.jocote.Message;
import se.ugli.pineapple.api.BaseConfiguration.ConfigurationBuilder;

@FunctionalInterface
public interface Filter extends Configuration {

    Envelope filter(Message message);

    static FilterBuilder builder(final Filter filter) {
        return new FilterBuilder(filter);
    }

    class FilterBuilder extends ConfigurationBuilder<FilterBuilder> {

        private final Filter filter;

        FilterBuilder(final Filter filter) {
            this.filter = filter;
        }

        public Filter build() {
            return new FilterImpl(filter, numberOfInstances, consumeType, idleDuration());
        }

        static class FilterImpl extends BaseConfiguration implements Filter {

            final Filter filter;

            FilterImpl(final Filter filter, final int numberOfInstances, final ConsumeType consumeType,
                    final FiniteDuration idleDuration) {
                super(numberOfInstances, consumeType, idleDuration);
                this.filter = filter;
            }

            @Override
            public Envelope filter(final Message message) {
                return filter.filter(message);
            }

        }

    }

}
