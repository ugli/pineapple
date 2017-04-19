package se.ugli.pineapple.model;

import static java.util.Collections.unmodifiableList;
import static se.ugli.pineapple.model.Component.Type.Filter;
import static se.ugli.pineapple.model.Component.Type.Pump;
import static se.ugli.pineapple.model.Component.Type.Sink;

import java.util.ArrayList;
import java.util.List;

import se.ugli.pineapple.PineappleException;

public class Component {

    public enum Type {
        Pump, Filter, Sink
    }

    public final String name;
    private final List<Pipe> in = new ArrayList<>();
    private final List<Pipe> out = new ArrayList<>();

    Component(final String name) {
        this.name = name;
    }

    void addIn(final Pipe pipe) {
        in.add(pipe);
    }

    void addOut(final Pipe pipe) {
        out.add(pipe);
    }

    public List<Pipe> getIn() {
        return unmodifiableList(in);
    }

    public List<Pipe> getOut() {
        return unmodifiableList(out);
    }

    boolean isPump() {
        return in.isEmpty();
    }

    boolean isSink() {
        return out.isEmpty();
    }

    boolean isFilter() {
        return !in.isEmpty() && !out.isEmpty();
    }

    public Type type() {
        if (isPump()) {
            return Pump;
        }
        if (isFilter()) {
            return Filter;
        }
        if (isSink()) {
            return Sink;
        }
        throw new PineappleException("No defined type: " + name);
    }

    @Override
    public String toString() {
        return name;
    }

}