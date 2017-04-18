package se.ugli.pineapple.dot;

import se.ugli.pineapple.model.Model;
import se.ugli.pineapple.model.ModelBuilder;

public final class DotImportExample {

    public static void main(final String[] args) {
        final Model model = ModelBuilder.apply().dotData("digraph G {a->b;b->c;b->d;d->a;x->a}".getBytes()).build();

        System.out.println(model);
    }

}