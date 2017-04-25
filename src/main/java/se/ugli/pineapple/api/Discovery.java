package se.ugli.pineapple.api;

import static se.ugli.pineapple.api.Defaults.DISCOVERY;

public interface Discovery {

    Filter filter(String name);

    Pump pump(String name);

    Sink sink(String name);

    default Pipe pipe(final String name) {
        return Pipe.apply(Defaults.URL_PREFIX + ":/" + name);
    }

    public static Discovery create() {
        return DISCOVERY;
    }

}