package se.ugli.pineapple.model;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

public class Component {

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
        return !isPump() && !isSink();
    }

    @Override
    public String toString() {
        return name;
    }

}