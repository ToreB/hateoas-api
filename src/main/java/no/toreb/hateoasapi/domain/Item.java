package no.toreb.hateoasapi.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.hateoas.core.Relation;

import java.util.UUID;

@Relation(value = "item", collectionRelation = "items")
public class Item {

    private final UUID id;
    private final UUID userId;
    private final String name;
    private final String description;

    public Item(final UUID id, final UUID userId, final String name, final String description) {
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Item item = (Item) o;

        return new EqualsBuilder()
                .append(id, item.id)
                .append(userId, item.userId)
                .append(name, item.name)
                .append(description, item.description)
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
