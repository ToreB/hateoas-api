package no.toreb.hateoasapi.service;

import no.toreb.hateoasapi.db.repository.UserRepository;
import no.toreb.hateoasapi.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

   private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(final UUID id) {
        return userRepository.findById(id);
    }

    public void insert(final User user) {
        userRepository.insert(user);
    }
}
