package godeck.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import godeck.models.entities.User;
import godeck.repositories.UserRepository;

/**
 * Service for the user model. It is responsible for managing the user data in
 * the database. It uses the UserRepository to provide the basic CRUD
 * operations.
 * 
 * @author Bruno Pena Baeta
 */
@Service
public class UserService {
    // Properties

    private UserRepository userRepository;

    // Constructors

    /**
     * Main constructor. Uses Autowire to inject the UserRepository.
     * 
     * @param userRepository The repository for the user model.
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Methods

    /**
     * Returns all the users from the database.
     * 
     * @return All the users from the database.
     */
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Returns the user from the database by its id. If the user does not exist, it
     * throws an exception.
     * 
     * @param id The id of the user.
     * @return The user from the database.
     */
    public User getById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        UUID uuid = UUID.fromString(id);
        if (uuid == null) {
            throw new IllegalArgumentException("Invalid UUID");
        }
        return userRepository.findById(uuid).orElseThrow(() -> new IllegalArgumentException("User not found!"));
    }

    /**
     * Saves the user in the database.
     * 
     * @param user The user to be saved.
     * @return The user saved in the database.
     */
    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return userRepository.save(user);
    }

    /**
     * Updates the user in the database.
     * 
     * @param user The user to be updated.
     * @return The user updated in the database.
     */
    public User update(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return userRepository.save(user);
    }

    /**
     * Deletes the user from the database by its id.
     * 
     * @param id The id of the user to be deleted.
     */
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

    /**
     * Deletes all the users from the database. It is used for testing purposes.
     */
    public void deleteAll() {
        userRepository.deleteAll();
    }

    /**
     * Returns the user from the database by its email. If the user does not exist,
     * it returns an empty list.
     * 
     * @param email The email of the user.
     * @return The user from the database.
     */
    public List<User> getByEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        return userRepository.findByEmail(email);
    }
}
