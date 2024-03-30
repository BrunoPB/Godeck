package godeck.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import godeck.models.entities.Friendship;
import godeck.models.entities.User;
import godeck.repositories.FriendshipRepository;

/**
 * Service for handling friendship manipulations.
 * 
 * @author Bruno Pena Baeta
 */
@Service
public class FriendshipService {
    // Properties

    private FriendshipRepository friendshipRepository;

    // Constructors

    @Autowired
    public FriendshipService(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    // CRUD Methods

    /**
     * Returns the friendship from the database by its id. If the friendship does
     * not exist, it throws an exception.
     * 
     * @param id The id of the friendship.
     * @return The friendship from the database.
     */
    public Friendship getById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return friendshipRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Friendship not found!"));
    }

    /**
     * Returns the friendship from the database by the ids of the users. If the
     * friendship does not exist, it throws an exception.
     * 
     * @param user0Id The id of the first user.
     * @param user1Id The id of the second user.
     * @return The friendship from the database.
     */
    public Friendship getFriendship(UUID user0Id, UUID user1Id) {
        if (user0Id == null || user1Id == null) {
            throw new IllegalArgumentException("User ids cannot be null");
        }
        return friendshipRepository.getFriendship(user0Id, user1Id)
                .orElseThrow(() -> new IllegalArgumentException("Friendship not found!"));
    }

    /**
     * Saves the friendship in the database.
     * 
     * @param friendship The friendship to save.
     */
    public void save(Friendship friendship) {
        if (friendship == null) {
            throw new IllegalArgumentException("Friendship cannot be null");
        }
        if (friendship.getUser0() == null || friendship.getUser1() == null) {
            throw new IllegalArgumentException("Friendship must have two users");
        }
        if (friendship.getUser0().getId().equals(friendship.getUser1().getId())) {
            throw new IllegalArgumentException("Friendship cannot be between the same user");
        }
        if (existsFriendship(friendship.getUser0().getId(), friendship.getUser1().getId())) {
            throw new IllegalArgumentException("Friendship already exists");
        }
        friendshipRepository.save(friendship);
    }

    /**
     * Deletes the friendship from the database by its id.
     * 
     * @param id The id of the friendship to be deleted.
     */
    public void deleteById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        friendshipRepository.deleteById(id);
    }

    /**
     * Deletes the friendship from the database.
     * 
     * @param friendship The friendship to be deleted.
     */
    public void delete(Friendship friendship) {
        if (friendship == null) {
            throw new IllegalArgumentException("Friendship cannot be null");
        }
        friendshipRepository.delete(friendship);
    }

    // Public Methods

    /**
     * Checks if a friendship exists between two users. The order of the users does
     * not matter.
     * 
     * @param user0Id The id of the first user.
     * @param user1Id The id of the second user.
     * @return If the friendship exists.
     */
    public boolean existsFriendship(UUID user0Id, UUID user1Id) {
        if (user0Id == null || user1Id == null) {
            throw new IllegalArgumentException("User ids cannot be null");
        }
        try {
            getFriendship(user0Id, user1Id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the list of friends of a user.
     * 
     * @param userId The id of the user.
     * @return The list of friends of the user.
     */
    public List<User> getUserFriendList(UUID userId) {
        return friendshipRepository.getUserFriendList(userId);
    }

    /**
     * Returns the list of friendships of a user.
     * 
     * @param userId The id of the user.
     * @return The list of friendships of the user.
     */
    public List<Friendship> getUserFriendshipList(UUID userId) {
        return friendshipRepository.getUserFriendshipList(userId);
    }
}
