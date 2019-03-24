package no.toreb.hateoasapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.IdGenerator;

import java.util.UUID;
import java.util.function.Supplier;

@Configuration
public class AppConfig {

    @Bean
    public IdGenerator idGenerator() {
        return UUID::randomUUID;
    }

    @Bean
    public Supplier<SecurityContext> securityContextSupplier() {
        return SecurityContextHolder::getContext;
    }
}
