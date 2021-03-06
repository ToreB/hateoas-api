package no.toreb.hateoasapi.configuration;

import no.toreb.hateoasapi.api.CustomMediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.MediaTypes.HAL_JSON;

@Configuration
public class CustomMediaTypeConfiguration {

    private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @Autowired
    public CustomMediaTypeConfiguration(final RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        this.requestMappingHandlerAdapter = requestMappingHandlerAdapter;
    }

    @PostConstruct
    public void enableCustomMediaTypes() {
        for (final HttpMessageConverter<?> converter : requestMappingHandlerAdapter.getMessageConverters()) {
            final List<MediaType> supportedMediaTypes = converter.getSupportedMediaTypes();
            if (converter instanceof MappingJackson2HttpMessageConverter && supportedMediaTypes.contains(HAL_JSON)) {
                final List<MediaType> newSupportedMediaTypes = new ArrayList<>(supportedMediaTypes);
                newSupportedMediaTypes.add(new CustomMediaType("*"));
                ((MappingJackson2HttpMessageConverter) converter).setSupportedMediaTypes(newSupportedMediaTypes);
            }
        }
    }
}
