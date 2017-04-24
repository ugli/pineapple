package se.ugli.pineapple.api;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static se.ugli.pineapple.api.ConsumeType.PULL;

import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

class Defaults {

    static final TimeUnit IDLE_TIME_UNIT = MILLISECONDS;
    static final long IDLE_TIME = 500l;
    static final FiniteDuration IDLE_DURATION = Duration.create(IDLE_TIME, IDLE_TIME_UNIT);
    static final int NUMBER_OF_INSTANCES = 1;
    static final ConsumeType CONSUME_TYPE = PULL;

    private Defaults() {
    }

}
