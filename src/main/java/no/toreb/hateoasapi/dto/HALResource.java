package no.toreb.hateoasapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class HALResource<T> extends ResourceSupport {

    @JsonUnwrapped
    private final T content;

    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("_embedded")
    private final Map<String, ?> embedded;

    public HALResource(final T content, final Iterable<Link> links, final Map<String, ?> embedded) {
        this.content = content;
        this.embedded = new HashMap<>(embedded);
        add(links);
    }

    public HALResource(final Resource<T> resource, final Map<String, ?> embedded) {
        this(resource.getContent(), resource.getLinks(), embedded);
    }

    public HALResource(final Resource<T> resource) {
        this(resource.getContent(), resource.getLinks(), Collections.emptyMap());
    }

    public T getContent() {
        return content;
    }

    public Map<String, ?> getEmbedded() {
        return Collections.unmodifiableMap(embedded);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final HALResource<?> that = (HALResource<?>) o;

        return new EqualsBuilder()
                .append(content, that.content)
                .append(embedded, that.embedded)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(content)
                .append(embedded)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("content", content)
                .append("embedded", embedded)
                .toString();
    }
}
