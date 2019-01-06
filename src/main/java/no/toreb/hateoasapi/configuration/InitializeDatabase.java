package no.toreb.hateoasapi.configuration;

import no.toreb.hateoasapi.dao.ItemDao;
import no.toreb.hateoasapi.dao.UserDao;
import no.toreb.hateoasapi.domain.Item;
import no.toreb.hateoasapi.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.util.UUID;

@Configuration
public class InitializeDatabase {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public InitializeDatabase(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Bean
    public CommandLineRunner initDatabase(final UserDao userDao, final ItemDao itemDao) {
        return args -> {
            @SuppressWarnings("ConstantConditions")
            final boolean exists = jdbcTemplate.query("select 1 from USERS", ResultSet::next);

            if (exists) {
                return;
            }

            final User user1 = new User(UUID.randomUUID(), "User 1");
            final User user2 = new User(UUID.randomUUID(), "User 2");
            final User user3 = new User(UUID.randomUUID(), "User 3");

            final Item item1 = new Item(UUID.randomUUID(), user1.getId(), "User 1 Task 1", "Do task!");
            final Item item2 = new Item(UUID.randomUUID(), user1.getId(), "User 1 Task 2", null);
            final Item item3 = new Item(UUID.randomUUID(), user1.getId(), "User 1 Task 3", null);
            final Item item4 = new Item(UUID.randomUUID(), user2.getId(), "User 2 Task 1", "My first task.");

            userDao.insert(user1);
            userDao.insert(user2);
            userDao.insert(user3);
            itemDao.insert(item1);
            itemDao.insert(item2);
            itemDao.insert(item3);
            itemDao.insert(item4);
        };
    }
}
