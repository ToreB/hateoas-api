package no.toreb.hateoasapi.db.model;

import no.toreb.hateoasapi.domain.Item;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.UUID;

public class ItemRecord {

    private final UUID id;
    private final UUID userId;
    private final String name;
    private final String description;

    public ItemRecord(final UUID id, final UUID userId, final String name, final String description) {
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

    public static ItemRecord of(final Item item) {
        return new ItemRecord(item.getId(), item.getUser().getId(), item.getName(), item.getDescription());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final ItemRecord itemRecord = (ItemRecord) o;

        return new EqualsBuilder()
                .append(id, itemRecord.id)
                .append(userId, itemRecord.userId)
                .append(name, itemRecord.name)
                .append(description, itemRecord.description)
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
