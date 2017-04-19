package se.ugli.pineapple;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import se.ugli.pineapple.api.Envelope;
import se.ugli.pineapple.api.Filter;
import se.ugli.pineapple.api.Pump;
import se.ugli.pineapple.api.Sink;

@Configuration
public class Pipp {

    @Bean
    public Pump indata() {
        return new Pump("activemq:/indata");
    }

    @Bean
    public Sink utdata() {
        return new Sink("log:/INFO");
    }

    @Bean
    public Filter distribute() {
        return m -> new Envelope(m);
    }

    // @Bean
    // public Pipe generate_distribute() {
    // return new Pipe("activemq:/gen_dist");
    // }

}
