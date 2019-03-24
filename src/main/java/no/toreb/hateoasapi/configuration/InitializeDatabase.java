package no.toreb.hateoasapi.configuration;

import no.toreb.hateoasapi.db.dao.ItemDao;
import no.toreb.hateoasapi.db.dao.UserDao;
import no.toreb.hateoasapi.db.model.ItemRecord;
import no.toreb.hateoasapi.db.model.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.sql.ResultSet;
import java.util.UUID;

@Configuration
@ConditionalOnProperty(name = "app.populate-db", havingValue = "true")
public class InitializeDatabase {

    private final JdbcTemplate jdbcTemplate;
    private final String user;
    private final String password;

    @Autowired
    public InitializeDatabase(final JdbcTemplate jdbcTemplate,
                              @Value("${spring.security.user.name}") final String user,
                              @Value("${spring.security.user.password}")final String password) {
        this.jdbcTemplate = jdbcTemplate;
        this.user = user;
        this.password = password;
    }

    @Bean
    public CommandLineRunner initDatabase(final UserDao userDao, final ItemDao itemDao) {
        final UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(user, password);
        SecurityContextHolder.setContext(new SecurityContextImpl(authReq));
        return args -> {
            @SuppressWarnings("ConstantConditions")
            final boolean exists = jdbcTemplate.query("select 1 from USERS", ResultSet::next);

            if (exists) {
                return;
            }

            final UserRecord user1 = new UserRecord(UUID.randomUUID(), "User 1");
            final UserRecord user2 = new UserRecord(UUID.randomUUID(), "User 2");
            final UserRecord user3 = new UserRecord(UUID.randomUUID(), "User 3");

            final ItemRecord item1 = new ItemRecord(UUID.randomUUID(), user1.getId(), "User 1 Task 1", "Do task!");
            final ItemRecord item2 = new ItemRecord(UUID.randomUUID(), user1.getId(), "User 1 Task 2", null);
            final ItemRecord item3 = new ItemRecord(UUID.randomUUID(), user1.getId(), "User 1 Task 3", null);
            final ItemRecord item4 = new ItemRecord(UUID.randomUUID(),
                                                    user2.getId(),
                                                    "User 2 Task 1",
                                                    "My first task.");

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
