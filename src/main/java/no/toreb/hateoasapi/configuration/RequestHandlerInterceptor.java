package no.toreb.hateoasapi.configuration;

import no.toreb.hateoasapi.api.CustomMediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Map;

@Component
public class RequestHandlerInterceptor implements HandlerInterceptor {

    private static final Map<String, MediaType> SUPPORTED_MEDIA_TYPES = Map.of(CustomMediaType.V1_VALUE,
                                                                               CustomMediaType.V1);

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws Exception {
        final String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
        if (!SUPPORTED_MEDIA_TYPES.containsKey(acceptHeader)) {
            throw new HttpMediaTypeNotAcceptableException(new ArrayList<>(SUPPORTED_MEDIA_TYPES.values()));
        }

        return true;
    }
}
