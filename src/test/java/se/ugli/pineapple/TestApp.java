package se.ugli.pineapple;

import se.ugli.java.io.Resource;

public class TestApp {

    public static void main(final String[] args) {
        Pineapple.start(Resource.apply("/khs.dot"));
    }

}
