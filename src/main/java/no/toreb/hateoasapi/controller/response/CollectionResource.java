package no.toreb.hateoasapi.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class CollectionResource<T> extends HALResource<CollectionMetadata> {

    private final String collectionName;

    public CollectionResource(final String collectionName,
                              final Collection<HALResource<T>> collection,
                              final Link... links) {
        super(new CollectionMetadata(collection.size()),
              Arrays.asList(links),
              Collections.singletonMap(collectionName, new ArrayList<>(collection)));
        this.collectionName = collectionName;
    }

    @SuppressWarnings("unchecked")
    @JsonIgnore
    public Collection<HALResource<T>> getCollection() {
        return new ArrayList<>((Collection<HALResource<T>>) getEmbedded().get(collectionName));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final CollectionResource<?> that = (CollectionResource<?>) o;

        return new EqualsBuilder()
                .append(collectionName, that.collectionName)
                .append(getContent(), that.getContent())
                .isEquals()
                && CollectionUtils.isEqualCollection(getCollection(), that.getCollection());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(collectionName)
                .append(getCollection())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("collectionName", collectionName)
                .append("collection", getCollection())
                .toString();
    }
}
