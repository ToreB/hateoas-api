package no.toreb.hateoasapi.api.v1.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import no.toreb.hateoasapi.domain.Item;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.UUID;

@JsonInclude(Include.NON_NULL)
public class ItemResponse {

    private final UUID id;
    private final UUID userId;
    private final String name;
    private final String description;

    @JsonCreator
    public ItemResponse(final UUID id, final UUID userId, final String name, final String description) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static ItemResponse of(final Item item) {
        return new ItemResponse(item.getId(),
                                item.getUser().getId(),
                                item.getName(),
                                item.getDescription());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final ItemResponse response = (ItemResponse) o;

        return new EqualsBuilder()
                .append(id, response.id)
                .append(userId, response.userId)
                .append(name, response.name)
                .append(description, response.description)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(userId)
                .append(name)
                .append(description)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("userId", userId)
                .append("name", name)
                .append("description", description)
                .toString();
    }
}
