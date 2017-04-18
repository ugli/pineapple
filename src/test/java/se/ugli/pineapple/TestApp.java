package se.ugli.pineapple;

import java.util.stream.IntStream;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import se.ugli.pineapple.discovery.Discovery;
import se.ugli.pineapple.discovery.SpringDiscovery;
import se.ugli.pineapple.impl.FilterImpl;
import se.ugli.pineapple.impl.PumpImpl;
import se.ugli.pineapple.impl.SinkImpl;
import se.ugli.pineapple.model.Model;
import se.ugli.pineapple.model.ModelBuilder;

public class TestApp {

    public static void main(final String[] args) {
        final Model model = ModelBuilder.apply()
                .dotData("digraph {Indata->Transform->Generate->Distribute->Utdata}".getBytes()).build();
        final ApplicationContext ctx = new AnnotationConfigApplicationContext("se.ugli");
        final Discovery discovery = new SpringDiscovery(ctx);
        model.pumps.forEach(c -> {
            new PumpImpl(discovery, c);
        });
        model.filters.forEach(c -> {
            new FilterImpl(discovery, c);
        });
        model.sinks.forEach(c -> {
            new SinkImpl(discovery, c);
        });
        IntStream.of(10000).forEach(i -> {
            try {
                Thread.sleep(1000);
            }
            catch (final InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

}
