package no.toreb.hateoasapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.IdGenerator;

import java.util.UUID;

@Configuration
public class AppConfig {

    @Bean
    public IdGenerator idGenerator() {
        return UUID::randomUUID;
    }
}
