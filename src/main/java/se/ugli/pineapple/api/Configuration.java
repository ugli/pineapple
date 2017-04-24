package se.ugli.pineapple.api;

import static se.ugli.pineapple.api.Defaults.CONSUME_TYPE;
import static se.ugli.pineapple.api.Defaults.IDLE_DURATION;
import static se.ugli.pineapple.api.Defaults.NUMBER_OF_INSTANCES;

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

}
