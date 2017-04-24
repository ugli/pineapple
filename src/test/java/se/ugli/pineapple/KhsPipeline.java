package se.ugli.pineapple;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import se.ugli.pineapple.api.Envelope;
import se.ugli.pineapple.api.Filter;
import se.ugli.pineapple.api.Pipe;
import se.ugli.pineapple.api.Pump;
import se.ugli.pineapple.api.Sink;

@Configuration
public class KhsPipeline {

    @Bean
    public Pump indata() {
        return Pump.builder("rabbitmq:/indata").build();
    }

    @Bean
    public Sink utdata() {
        return Sink.builder("rabbitmq:/utdata").build();
    }

    @Bean
    public Filter distribute() {
        return m -> Envelope.apply((new String(m.body()) + " D").getBytes());
    }

    @Bean
    public Pipe generate_distribute() {
        return new Pipe("rabbitmq:/gen_dist");
    }

}