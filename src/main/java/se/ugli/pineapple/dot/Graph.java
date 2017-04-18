package se.ugli.pineapple.dot;

import static java.util.Collections.unmodifiableCollection;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.Collection;

import guru.nidi.graphviz.model.MutableGraph;

public class Graph {

    public final Collection<Link> links;

    public Graph(final MutableGraph graph) {
        if (!graph.isDirected())
            throw new DotExeption("Graph has to be directed");
        links = unmodifiableCollection(
                graph.nodes().stream().flatMap(n -> n.links().stream()).map(Link::new).collect(toList()));
    }

    @Override
    public String toString() {
        return "digraph{" + links.stream().map(Object::toString).collect(joining(";")) + "}";
    }

}
