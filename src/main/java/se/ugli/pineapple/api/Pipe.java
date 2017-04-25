package se.ugli.pineapple.api;

@FunctionalInterface
public interface Pipe {

    String url();

    public static Pipe apply(final String url) {
        return new PipeImpl(url);
    }

    class PipeImpl implements Pipe {

        final String url;

        PipeImpl(final String url) {
            this.url = url;
        }

        @Override
        public String url() {
            return url;
        }

    }

}
