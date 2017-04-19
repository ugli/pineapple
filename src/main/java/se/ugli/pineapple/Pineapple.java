package se.ugli.pineapple;

import akka.actor.ActorSystem;
import akka.actor.Props;
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

    private static final ActorSystem actorSystem = ActorSystem.create("pineapple");

    private Pineapple() {
    }

    public static void start(Model model) {
        final Discovery discovery = Discovery.create();
        model.pumps.forEach(c -> actorSystem.actorOf(Props.create(PumpActor.class, c, discovery)));
        model.filters.forEach(c -> actorSystem.actorOf(Props.create(FilterActor.class, c, discovery)));
        model.sinks.forEach(c -> actorSystem.actorOf(Props.create(SinkActor.class, c, discovery)));
    }

    public static Future<Terminated> stop() {
        return actorSystem.terminate();
    }

    public static void start(byte[] dotData) {
        start(ModelBuilder.apply().dotData(dotData).build());
    }

    public static void start(Resource resource) {
        start(resource.asBytes());
    }

    public static void start() {
        start(Resource.apply("/pineapple.dot"));
    }

}
