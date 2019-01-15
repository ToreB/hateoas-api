package no.toreb.hateoasapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.toreb.hateoasapi.api.common.response.CollectionResource;
import no.toreb.hateoasapi.api.common.response.HALResource;
import no.toreb.hateoasapi.api.v1.response.ItemResponse;
import no.toreb.hateoasapi.api.v1.response.UserResponse;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

class ResponseDeserializer {

    private final ObjectMapper objectMapper;

    ResponseDeserializer(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SuppressWarnings("unchecked")
    private <T> HALResource<T> convert(final Map<String, Object> map, final Class<T> clazz) {
        final Map<String, Object> mapCopy = new HashMap<>(map);
        final List<Link> links = new ArrayList<>();
        ((Map<String, Map<String, String>>) mapCopy.remove("_links")).forEach(
                (rel, link) -> links.add(new Link(link.get("href"), rel)));

        final Map<String, Object> embedded = new HashMap<>();
        final Map<String, Map<String, Object>> embeddedMap =
                (Map<String, Map<String, Object>>) mapCopy.remove("_embedded");
        if (embeddedMap != null && clazz == ItemResponse.class) {
            embeddedMap.forEach((relation, objectMap) -> embedded.put("user",
                                                                      convert(objectMap, UserResponse.class)));
        }

        final T converted = mapCopy.isEmpty() ? null : objectMapper.convertValue(map, clazz);

        return new HALResource<>(converted, links, embedded);
    }

    <T> ResponseEntity<HALResource<T>> deserialize(final Class<T> clazz,
                                                   final Supplier<ResponseEntity<String>> supplier) {
        final ResponseEntity<String> responseEntity = supplier.get();
        final Map<String, Object> jsonData = new JacksonJsonParser(objectMapper).parseMap(responseEntity.getBody());

        return new ResponseEntity<>(convert(jsonData, clazz),
                                    responseEntity.getHeaders(),
                                    responseEntity.getStatusCode());
    }

    @SuppressWarnings("unchecked")
    <T> ResponseEntity<CollectionResource<T>> deserializeCollection(final Class<T> clazz,
                                                                    final Supplier<ResponseEntity<String>> supplier) {
        final ResponseEntity<String> responseEntity = supplier.get();
        final String json = responseEntity.getBody();
        final Map<String, Object> jsonData = new JacksonJsonParser(objectMapper).parseMap(json);

        final Map<String, Map<String, String>> linksMap = (Map<String, Map<String, String>>) jsonData.get("_links");
        final List<Link> links = new ArrayList<>();
        linksMap.forEach((rel, link) -> links.add(new Link(link.get("href"), rel)));

        final String collectionName = clazz == UserResponse.class ? "users" : "items";
        final List<Map<String, Object>> elementsMap =
                ((Map<String, List<Map<String, Object>>>) jsonData.get("_embedded")).get(collectionName);

        final List<HALResource<T>> elements = new ArrayList<>();
        elementsMap.forEach(element -> elements.add(convert(element, clazz)));

        return new ResponseEntity<>(new CollectionResource<>(collectionName, elements, links),
                                    responseEntity.getHeaders(),
                                    responseEntity.getStatusCode());
    }
}
