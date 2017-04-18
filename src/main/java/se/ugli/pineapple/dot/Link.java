package se.ugli.pineapple.dot;

import static java.util.Objects.isNull;

import guru.nidi.graphviz.model.Label;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.model.MutableNodePoint;

public class Link {

    public final String from;
    public final String to;

    Link(final guru.nidi.graphviz.model.Link link) {
        from = resolve(link.from());
        to = resolve(link.to());
    }

    private static String resolve(final Object obj) {
        if (obj instanceof MutableNodePoint)
            return label(((MutableNodePoint) obj).node());
        throw new IllegalStateException(obj.getClass().getName());
    }

    private static String label(final MutableNode mutableNode) {
        final Label label = mutableNode.label();
        if (isNull(label) || label.isEmpty())
            throw new DotExeption("Node has no label");
        return label.toString();
    }

    @Override
    public String toString() {
        return from + "->" + to;
    }

}
