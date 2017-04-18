package se.ugli.pineapple.model;

import static java.util.Collections.unmodifiableCollection;
import static java.util.stream.Collectors.toList;

import java.util.Collection;

import se.ugli.pineapple.PineappleException;

public class Model {

    public final Collection<Pipe> pipes;
    public final Collection<Component> filters;
    public final Collection<Component> pumps;
    public final Collection<Component> sinks;

    Model(final Collection<Pipe> pipes, final Collection<Component> components) {
        this.pipes = unmodifiableCollection(pipes);
        filters = unmodifiableCollection(components.stream().filter(Component::isFilter).collect(toList()));
        pumps = unmodifiableCollection(components.stream().filter(Component::isPump).collect(toList()));
        sinks = unmodifiableCollection(components.stream().filter(Component::isSink).collect(toList()));
    }

    void validate() {
        pumps.forEach(p -> {
            if (p.getOut().size() > 1)
                throw new PineappleException("A pump can only have one out pipe.");
        });
        filters.forEach(f -> {
            if (f.getIn().isEmpty() || f.getOut().isEmpty())
                throw new PineappleException("A filter must have both in and out pipes.");
        });
    }

    @Override
    public String toString() {
        return "Model [pipes=" + pipes + ", filters=" + filters + ", pumps=" + pumps + ", sinks=" + sinks + "]";
    }

}
