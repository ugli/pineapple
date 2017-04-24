package se.ugli.pineapple.api;

import static se.ugli.pineapple.api.Defaults.CONSUME_TYPE;
import static se.ugli.pineapple.api.Defaults.IDLE_TIME;
import static se.ugli.pineapple.api.Defaults.IDLE_TIME_UNIT;
import static se.ugli.pineapple.api.Defaults.NUMBER_OF_INSTANCES;

import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

abstract class BaseConfiguration implements Configuration {

    private final int numberOfInstances;
    private final ConsumeType consumeType;
    private final FiniteDuration idleDuration;

    protected BaseConfiguration(final int numberOfInstances, final ConsumeType consumeType,
            final FiniteDuration idleDuration) {
        this.numberOfInstances = numberOfInstances;
        this.consumeType = consumeType;
        this.idleDuration = idleDuration;
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

    @SuppressWarnings("unchecked")
    abstract static class ConfigurationBuilder<T> {

        protected int numberOfInstances = NUMBER_OF_INSTANCES;
        protected ConsumeType consumeType = CONSUME_TYPE;
        private TimeUnit idleTimeUnit = IDLE_TIME_UNIT;
        private long idleTime = IDLE_TIME;

        public T numberOfInstances(final int numberOfInstances) {
            this.numberOfInstances = numberOfInstances;
            return (T) this;
        }

        public T consumeType(final ConsumeType consumeType) {
            this.consumeType = consumeType;
            return (T) this;
        }

        public T idleTimeUnit(final TimeUnit idleTimeUnit) {
            this.idleTimeUnit = idleTimeUnit;
            return (T) this;
        }

        public T idleTime(final long idleTime) {
            this.idleTime = idleTime;
            return (T) this;
        }

        protected FiniteDuration idleDuration() {
            return Duration.create(idleTime, idleTimeUnit);
        }

    }

}
