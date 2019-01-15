package no.toreb.hateoasapi.service;

import no.toreb.hateoasapi.db.repository.UserRepository;
import no.toreb.hateoasapi.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.util.IdGenerator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final IdGenerator idGenerator;

    public UserService(final UserRepository userRepository, final IdGenerator idGenerator) {
        this.userRepository = userRepository;
        this.idGenerator = idGenerator;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(final UUID id) {
        return userRepository.findById(id);
    }

    public User insert(final User newUser) {
        final User user = new User(idGenerator.generateId(), newUser.getName());

        userRepository.insert(user);

        return user;
    }
}
