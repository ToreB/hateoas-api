package no.toreb.hateoasapi.db.dao;

import no.toreb.hateoasapi.db.model.ItemRecord;
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
public class ItemDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Supplier<SecurityContext> securityContextSupplier;

    @Autowired
    public ItemDao(final NamedParameterJdbcTemplate jdbcTemplate,
                   final Supplier<SecurityContext> securityContextSupplier) {
        this.jdbcTemplate = jdbcTemplate;
        this.securityContextSupplier = securityContextSupplier;
    }

    @Transactional(readOnly = true)
    public List<ItemRecord> findAll() {
        return jdbcTemplate.query("select * from items", new ItemRowMapper());
    }

    @Transactional(readOnly = true)
    public Optional<ItemRecord> findById(final UUID id) {
        final ItemRecord itemRecord = DataAccessUtils.singleResult(jdbcTemplate.query(
                "select * from items where id = :id",
                new MapSqlParameterSource("id", id),
                new ItemRowMapper()));
        return Optional.ofNullable(itemRecord);
    }

    @Transactional
    public void insert(final ItemRecord itemRecord) {
        jdbcTemplate.update("insert into items (" +
                                    "  ID, USER_ID, NAME, DESCRIPTION, " +
                                    "  CREATED, CREATED_BY, LAST_MODIFIED, LAST_MODIFIED_BY) " +
                                    "values (" +
                                    "  :id, :user_id, :name, :description, " +
                                    "  :created, :created_by, :last_modified, :last_modified_by)",
                            toParameterSource(itemRecord));
    }

    @Transactional
    public void update(final ItemRecord itemRecord) {
        jdbcTemplate.update("update items " +
                                    " set NAME = :name, " +
                                    "    DESCRIPTION = :description, " +
                                    "    USER_ID = :user_id," +
                                    "    LAST_MODIFIED = :last_modified," +
                                    "    LAST_MODIFIED_BY = :last_modified_by " +
                                    " where ID = :id", toParameterSource(itemRecord));
    }

    private MapSqlParameterSource toParameterSource(final ItemRecord itemRecord) {
        return new MapSqlParameterSource().addValue("id", itemRecord.getId())
                                          .addValue("user_id", itemRecord.getUserId())
                                          .addValue("name", itemRecord.getName())
                                          .addValue("description", itemRecord.getDescription())
                                          .addValue("created", LocalDateTime.now())
                                          .addValue("created_by", getUsername())
                                          .addValue("last_modified", LocalDateTime.now())
                                          .addValue("last_modified_by", getUsername());
    }

    private String getUsername() {
        return ((UserDetails)securityContextSupplier.get().getAuthentication().getPrincipal()).getUsername();
    }

    @Transactional
    public void delete(final UUID id) {
        jdbcTemplate.update("delete from items where id = :id", new MapSqlParameterSource("id", id));
    }

    private static class ItemRowMapper implements RowMapper<ItemRecord> {

        @Override
        public ItemRecord mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            return new ItemRecord(rs.getObject("id", UUID.class),
                                  rs.getObject("user_id", UUID.class),
                                  rs.getString("name"),
                                  rs.getString("description"));
        }
    }
}
