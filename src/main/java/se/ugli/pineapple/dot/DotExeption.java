package se.ugli.pineapple.dot;

import java.io.IOException;

public class DotExeption extends RuntimeException {

    private static final long serialVersionUID = 2329384105673166294L;

    public DotExeption(final IOException e) {
        super(e);
    }

    public DotExeption(final String msg) {
        super(msg);
    }

}
