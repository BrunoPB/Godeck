package godeck.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import godeck.models.User;
import godeck.repositories.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public User getById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        UUID uuid = UUID.fromString(id);
        if (uuid == null) {
            throw new IllegalArgumentException("Invalid UUID");
        }
        return userRepository.findById(uuid).orElse(null);
    }

    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return userRepository.save(user);
    }

    public User update(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return userRepository.save(user);
    }

    public void delete(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        UUID uuid = UUID.fromString(id);
        if (uuid == null) {
            throw new IllegalArgumentException("Invalid UUID");
        }
        userRepository.deleteById(uuid);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    public List<User> getByEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        return userRepository.findByEmail(email);
    }
}
