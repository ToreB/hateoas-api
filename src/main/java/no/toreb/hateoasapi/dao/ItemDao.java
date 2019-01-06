package no.toreb.hateoasapi.dao;

import no.toreb.hateoasapi.domain.Item;
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
    public List<Item> findAll() {
        return jdbcTemplate.query("select * from items", new ItemRowMapper());
    }

    @Transactional(readOnly = true)
    public Optional<Item> findById(final UUID id) {
        final Item item = DataAccessUtils.singleResult(jdbcTemplate.query("select * from items where id = :id",
                                                                          new MapSqlParameterSource("id", id),
                                                                          new ItemRowMapper()));
        return Optional.ofNullable(item);
    }

    @Transactional
    public void insert(final Item item) {
        final MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", item.getId())
                                                                        .addValue("user_id", item.getUserId())
                                                                        .addValue("name", item.getName())
                                                                        .addValue("description", item.getDescription());

        jdbcTemplate.update("insert into items (" +
                                     "  ID, USER_ID, NAME, DESCRIPTION) " +
                                     "values (" +
                                     "  :id, :user_id, :name, :description)", params);
    }

    private static class ItemRowMapper implements RowMapper<Item> {

        @Override
        public Item mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            return new Item(rs.getObject("id", UUID.class),
                            rs.getObject("user_id", UUID.class),
                            rs.getString("name"),
                            rs.getString("description"));
        }
    }
}
