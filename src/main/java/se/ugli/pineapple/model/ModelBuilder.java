package se.ugli.pineapple.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import se.ugli.pineapple.dot.DotParser;

public class ModelBuilder {

    private final Map<String, Component> componentByName = new LinkedHashMap<>();
    private final List<Pipe> pipes = new ArrayList<>();

    private ModelBuilder() {
    }

    public static ModelBuilder apply() {
        return new ModelBuilder();
    }

    public ModelBuilder pipe(final String from, final String to) {
        final Component fromComp = componentByName.computeIfAbsent(from, Component::new);
        final Component toComp = componentByName.computeIfAbsent(to, Component::new);
        final Pipe pipe = new Pipe(fromComp, toComp);
        fromComp.addOut(pipe);
        toComp.addIn(pipe);
        pipes.add(pipe);
        return this;
    }

    public ModelBuilder dotData(final byte[] dotData) {
        DotParser.parser(dotData).links.forEach(link -> pipe(link.from, link.to));
        return this;
    }

    public Model build() {
        return new Model(pipes, componentByName.values());
    }

}
