package no.toreb.hateoasapi.api.v1.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.UUID;

public class ItemRequest {

    private final UUID user;
    private final String name;
    private final String description;

    @JsonCreator
    public ItemRequest(final UUID user, final String name, final String description) {
        this.user = user;
        this.name = name;
        this.description = description;
    }

    public UUID getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final ItemRequest that = (ItemRequest) o;

        return new EqualsBuilder()
                .append(user, that.user)
                .append(name, that.name)
                .append(description, that.description)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(user)
                .append(name)
                .append(description)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("user", user)
                .append("name", name)
                .append("description", description)
                .toString();
    }
}
