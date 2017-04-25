package se.ugli.pineapple;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Test;

import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import se.ugli.java.io.Resource;
import se.ugli.jocote.Jocote;
import se.ugli.jocote.Message;
import se.ugli.pineapple.api.Discovery;
import se.ugli.pineapple.api.Envelope;
import se.ugli.pineapple.api.SimpleFilter;
import se.ugli.pineapple.api.Pump;
import se.ugli.pineapple.api.Sink;

public class PineappleTest {

    @Test
    public void test() throws InterruptedException {
        final String pumpUrl = "ram:/pump";
        final String sinkUrl = "ram:/sink";
        Pineapple.start(Resource.apply("/khs.dot"), new Discovery() {

            @Override
            public SimpleFilter filter(final String name) {
                return m -> Envelope.apply((new String(m.body()) + " " + name).getBytes());
            }

            @Override
            public Pump pump(final String name) {
                return Pump.builder(pumpUrl).build();
            }

            @Override
            public Sink sink(final String name) {
                return Sink.builder(sinkUrl).build();
            }
        });
        Jocote.connect(pumpUrl).put("hej".getBytes());
        Thread.sleep(10000);
        final Message message = Jocote.connect(sinkUrl).get().orElseThrow(() -> new AssertionError("Bad stuff"));
        assertThat(new String(message.body()), is("hej Transform Generate Distribute"));
    }

    @After
    public void stop() throws Exception {
        Await.result(Pineapple.stop(), Duration.create(2, TimeUnit.SECONDS));
    }

}
