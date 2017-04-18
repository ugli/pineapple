package se.ugli.pineapple.model;

public class Pipe {

    public final Component from;
    public final Component to;
    public final String name;

    Pipe(final Component from, final Component to) {
        this.from = from;
        this.to = to;
        name = from.name + "_" + to.name;
    }

    @Override
    public String toString() {
        return from.name + "->" + to.name;
    }

}