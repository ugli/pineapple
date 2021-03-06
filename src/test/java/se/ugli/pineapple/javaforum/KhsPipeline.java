package se.ugli.pineapple.javaforum;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import se.ugli.pineapple.api.Envelope;
import se.ugli.pineapple.api.Pipe;
import se.ugli.pineapple.api.Pump;
import se.ugli.pineapple.api.SimpleFilter;
import se.ugli.pineapple.api.Sink;

@Configuration
public class KhsPipeline {

    @Bean
    public Pump indata() {
        return () -> "rabbitmq:/indata";
    }

    @Bean
    public Sink utdata() {
        return () -> "rabbitmq:/utdata";
    }

    @Bean
    public SimpleFilter distribute() {
        return m -> Envelope.optional((new String(m.body()) + " D").getBytes());
    }

    @Bean
    public Pipe generate_distribute() {
        return () -> "rabbitmq:/gen_dist";
    }

}
