package se.ugli.pineapple.api;

import static se.ugli.pineapple.api.Defaults.CONSUME_TYPE;
import static se.ugli.pineapple.api.Defaults.IDLE_DURATION;
import static se.ugli.pineapple.api.Defaults.IDLE_TIME;
import static se.ugli.pineapple.api.Defaults.IDLE_TIME_UNIT;
import static se.ugli.pineapple.api.Defaults.NUMBER_OF_INSTANCES;
import static se.ugli.pineapple.api.Defaults.STREAM_LIMIT;

import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

public interface Configuration {

    default int numberOfInstances() {
        return NUMBER_OF_INSTANCES;
    }

    default ConsumeType consumeType() {
        return CONSUME_TYPE;
    }

    default FiniteDuration idleDuration() {
        return IDLE_DURATION;
    }

    default long streamLimit() {
        return STREAM_LIMIT;
    }

    @SuppressWarnings("unchecked")
    abstract static class ConfigurationBuilder<B, E> {

        int numberOfInstances = NUMBER_OF_INSTANCES;
        ConsumeType consumeType = CONSUME_TYPE;
        long streamLimit = STREAM_LIMIT;
        TimeUnit idleTimeUnit = IDLE_TIME_UNIT;
        long idleTime = IDLE_TIME;

        public B numberOfInstances(final int numberOfInstances) {
            this.numberOfInstances = numberOfInstances;
            return (B) this;
        }

        public B consumeType(final ConsumeType consumeType) {
            this.consumeType = consumeType;
            return (B) this;
        }

        public B idleTimeUnit(final TimeUnit idleTimeUnit) {
            this.idleTimeUnit = idleTimeUnit;
            return (B) this;
        }

        public B idleTime(final long idleTime) {
            this.idleTime = idleTime;
            return (B) this;
        }

        public B streamLimit(final long streamLimit) {
            this.streamLimit = streamLimit;
            return (B) this;
        }

        FiniteDuration idleDuration() {
            return Duration.create(idleTime, idleTimeUnit);
        }

        abstract E build();

    }

    abstract class ConfigurationBase implements Configuration {

        final int numberOfInstances;
        final ConsumeType consumeType;
        final FiniteDuration idleDuration;
        final long streamLimit;

        ConfigurationBase(final int numberOfInstances, final ConsumeType consumeType, final FiniteDuration idleDuration,
                final long streamLimit) {
            this.numberOfInstances = numberOfInstances;
            this.consumeType = consumeType;
            this.idleDuration = idleDuration;
            this.streamLimit = streamLimit;
        }

        @Override
        public int numberOfInstances() {
            return numberOfInstances;
        }

        @Override
        public ConsumeType consumeType() {
            return consumeType;
        }

        @Override
        public FiniteDuration idleDuration() {
            return idleDuration;
        }

        @Override
        public long streamLimit() {
            return streamLimit;
        }

    }

}
