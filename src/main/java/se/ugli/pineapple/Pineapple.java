package se.ugli.pineapple;

import se.ugli.java.io.Resource;
import se.ugli.pineapple.discovery.Discovery;
import se.ugli.pineapple.model.Model;
import se.ugli.pineapple.model.ModelBuilder;
import se.ugli.pineapple.system.FilterImpl;
import se.ugli.pineapple.system.PumpImpl;
import se.ugli.pineapple.system.SinkImpl;

public class Pineapple {

    private Pineapple() {
    }

    public static void start(Model model) {
        final Discovery discovery = Discovery.create();
        model.pumps.forEach(c -> new PumpImpl(discovery, c));
        model.filters.forEach(c -> new FilterImpl(discovery, c));
        model.sinks.forEach(c -> new SinkImpl(discovery, c));
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
