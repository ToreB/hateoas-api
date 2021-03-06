package no.toreb.hateoasapi.domain;

import no.toreb.hateoasapi.db.model.ItemRecord;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.UUID;

public class Item {

    private final UUID id;
    private final String name;
    private final String description;

    private final User user;

    public Item(final UUID id, final String name, final String description, final User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public User getUser() {
        return user;
    }

    public static Item of(final ItemRecord itemRecord, final User user) {
        return new Item(itemRecord.getId(), itemRecord.getName(), itemRecord.getDescription(), user);
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
                .append(name, item.name)
                .append(description, item.description)
                .append(user, item.user)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(description)
                .append(user)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .append("description", description)
                .append("user", user)
                .toString();
    }
}
