package se.ugli.pineapple.impl;

import java.util.LinkedHashSet;
import java.util.Set;

import se.ugli.pineapple.model.Component;

class Item {

    static final Set<Object> registry = new LinkedHashSet<>();

    final String name;

    Item(final Component component) {
        registry.add(this);
        name = component.name;
    }

}
