package se.ugli.pineapple;

import akka.actor.ActorSystem;
import akka.actor.Terminated;
import scala.concurrent.Future;
import se.ugli.java.io.Resource;
import se.ugli.pineapple.discovery.Discovery;
import se.ugli.pineapple.model.Model;
import se.ugli.pineapple.model.ModelBuilder;
import se.ugli.pineapple.system.FilterActor;
import se.ugli.pineapple.system.PumpActor;
import se.ugli.pineapple.system.SinkActor;

public class Pineapple {

    private static ActorSystem actorSystem;

    private Pineapple() {
    }

    public static void start(final Model model, final Discovery discovery) {
        actorSystem = ActorSystem.create("pineapple");
        model.pumps.forEach(c -> actorSystem.actorOf(PumpActor.props(c, discovery)));
        model.filters.forEach(c -> actorSystem.actorOf(FilterActor.props(c, discovery)));
        model.sinks.forEach(c -> actorSystem.actorOf(SinkActor.props(c, discovery)));
    }

    public static Future<Terminated> stop() {
        return actorSystem.terminate();
    }

    public static void start(final byte[] dotData, final Discovery discovery) {
        start(ModelBuilder.apply().dotData(dotData).build(), discovery);
    }

    public static void start(final Resource resource, final Discovery discovery) {
        start(resource.asBytes(), discovery);
    }

    public static void start(final Resource resource) {
        start(resource, Discovery.create());
    }

    public static void start() {
        start(Resource.apply("/pineapple.dot"), Discovery.create());
    }

}
