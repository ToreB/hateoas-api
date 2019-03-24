package no.toreb.hateoasapi.db.dao;

import no.toreb.hateoasapi.db.model.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Repository
public class UserDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Supplier<SecurityContext> securityContextSupplier;

    @Autowired
    public UserDao(final NamedParameterJdbcTemplate jdbcTemplate,
                   final Supplier<SecurityContext> securityContextSupplier) {
        this.jdbcTemplate = jdbcTemplate;
        this.securityContextSupplier = securityContextSupplier;
    }

    @Transactional
    public void insert(final UserRecord userRecord) {
        final String username = ((UserDetails)securityContextSupplier.get().getAuthentication().getPrincipal()).getUsername();
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", userRecord.getId())
                .addValue("name", userRecord.getName())
                .addValue("created", LocalDateTime.now())
                .addValue("created_by", username)
                .addValue("last_modified", LocalDateTime.now())
                .addValue("last_modified_by", username);

        jdbcTemplate.update("insert into USERS (" +
                                    "   ID, NAME, CREATED, CREATED_BY, LAST_MODIFIED, LAST_MODIFIED_BY) " +
                                    " values (" +
                                    "   :id, :name, :created, :created_by, :last_modified, :last_modified_by)",
                            params);
    }

    @Transactional(readOnly = true)
    public Optional<UserRecord> findById(final UUID id) {
        final UserRecord userRecord = DataAccessUtils.singleResult(jdbcTemplate.query(
                "select * from USERS where id = :id",
                new MapSqlParameterSource("id", id),
                new UserRowMapper()));
        return Optional.ofNullable(userRecord);
    }

    @Transactional(readOnly = true)
    public List<UserRecord> findAll() {
        return jdbcTemplate.query("select * from USERS", new UserRowMapper());
    }

    private class UserRowMapper implements RowMapper<UserRecord> {

        @Override
        public UserRecord mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            return new UserRecord(rs.getObject("id", UUID.class), rs.getString("name"));
        }
    }
}
