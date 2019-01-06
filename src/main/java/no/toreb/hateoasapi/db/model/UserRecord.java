package no.toreb.hateoasapi.db.model;

import no.toreb.hateoasapi.domain.User;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.UUID;

public class UserRecord {

    private final UUID id;
    private final String name;

    public UserRecord(final UUID id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static UserRecord of(final User user) {
        return new UserRecord(user.getId(), user.getName());
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final UserRecord userRecord = (UserRecord) o;

        return new EqualsBuilder()
                .append(id, userRecord.id)
                .append(name, userRecord.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .toString();
    }
}
