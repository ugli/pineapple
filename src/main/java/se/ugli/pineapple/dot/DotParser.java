package se.ugli.pineapple.dot;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import guru.nidi.graphviz.parse.Parser;

public class DotParser {

    private DotParser() {
    }

    public static Graph parser(final byte[] dotData) {
        try {
            return new Graph(Parser.read(new ByteArrayInputStream(dotData)));
        }
        catch (final IOException e) {
            throw new DotExeption(e);
        }
    }

}
