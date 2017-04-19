package se.ugli.pineapple;

import java.util.stream.IntStream;

import se.ugli.java.io.Resource;

public class TestApp {

    public static void main(final String[] args) {
        Pineapple.start(Resource.apply("/khs.dot"));
        IntStream.of(10000).forEach(i -> {
            try {
                Thread.sleep(1000);
            } catch (final InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

}
