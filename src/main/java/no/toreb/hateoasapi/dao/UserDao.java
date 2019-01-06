package no.toreb.hateoasapi.dao;

import no.toreb.hateoasapi.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public UserDao(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void insert(final User user) {
        final MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", user.getId())
                                                                        .addValue("name", user.getName());

        jdbcTemplate.update("insert into USERS (ID, NAME) values (:id, :name)", params);
    }
}
