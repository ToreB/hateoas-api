package no.toreb.hateoasapi.db.repository;

import no.toreb.hateoasapi.db.dao.UserDao;
import no.toreb.hateoasapi.db.model.UserRecord;
import no.toreb.hateoasapi.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class UserRepository {

    private final UserDao userDao;

    public UserRepository(final UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userDao.findAll().stream().map(User::of).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(final UUID id) {
        return userDao.findById(id).map(User::of);
    }

    @Transactional
    public void insert(final User user) {
        userDao.insert(UserRecord.of(user));
    }
}
