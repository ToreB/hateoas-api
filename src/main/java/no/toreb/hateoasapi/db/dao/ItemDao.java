package no.toreb.hateoasapi.db.dao;

import no.toreb.hateoasapi.db.model.ItemRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ItemDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ItemDao(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    public List<ItemRecord> findAll() {
        return jdbcTemplate.query("select * from items", new ItemRowMapper());
    }

    @Transactional(readOnly = true)
    public Optional<ItemRecord> findById(final UUID id) {
        final ItemRecord itemRecord = DataAccessUtils.singleResult(jdbcTemplate.query("select * from items where id = :id",
                                                                                      new MapSqlParameterSource("id", id),
                                                                                      new ItemRowMapper()));
        return Optional.ofNullable(itemRecord);
    }

    @Transactional
    public void insert(final ItemRecord itemRecord) {
        final MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", itemRecord.getId())
                                                                        .addValue("user_id", itemRecord.getUserId())
                                                                        .addValue("name", itemRecord.getName())
                                                                        .addValue("description", itemRecord.getDescription());

        jdbcTemplate.update("insert into items (" +
                                     "  ID, USER_ID, NAME, DESCRIPTION) " +
                                     "values (" +
                                     "  :id, :user_id, :name, :description)", params);
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
