package no.toreb.hateoasapi.dto;

import org.springframework.hateoas.Link;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CollectionResource<T> extends HALResource<Map<String, ?>> {

    public CollectionResource(final String collectionName,
                              final Collection<HALResource<T>> collection,
                              final Link... links) {
        super(Collections.singletonMap("count", collection.size()),
              Arrays.asList(links),
              Collections.singletonMap(collectionName, collection));
    }
}
