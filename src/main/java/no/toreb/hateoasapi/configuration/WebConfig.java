package no.toreb.hateoasapi.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RequestHandlerInterceptor requestHandlerInterceptor;

    @Autowired
    public WebConfig(final RequestHandlerInterceptor requestHandlerInterceptor) {
        this.requestHandlerInterceptor = requestHandlerInterceptor;
    }

    @Override
    public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false)
                  .favorParameter(false)
                  .ignoreAcceptHeader(false)
                  .replaceMediaTypes(Map.of());
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(requestHandlerInterceptor);
    }
}
