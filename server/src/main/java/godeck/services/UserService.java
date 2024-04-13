package godeck.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import godeck.models.client.Friend;
import godeck.models.entities.Friendship;
import godeck.models.entities.Card;
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
    private FriendshipService friendshipService;
    private CardService cardService;

    // Constructors

    /**
     * Main constructor. Uses Autowire to inject the UserRepository.
     * 
     * @param userRepository The repository for the user model.
     */
    @Autowired
    public UserService(UserRepository userRepository, FriendshipService friendshipService,
            CardService cardService) {
        this.userRepository = userRepository;
        this.friendshipService = friendshipService;
        this.cardService = cardService;
    }

    // CRUD Methods

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
     * Returns the user from the database by its email. If the user does not exist,
     * it returns an empty list.
     * 
     * @param email The email of the user.
     * @return The user from the database.
     */
    public User getByEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found!"));
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
     * Deletes the user from the database.
     * 
     * @param user The user to be deleted.
     */
    public void delete(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        userRepository.delete(user);
    }

    // Private Methods

    /**
     * Generates a list of cards for ghost users.
     * 
     * @return The list of cards for ghost users.
     */
    private List<Card> generateGhostUserCards() { // TODO: Establish the initial cards for ghost users
        List<Card> fullChars = (List<Card>) cardService.findAll();
        List<Card> randomList = new ArrayList<Card>();
        List<Card> characters = new ArrayList<Card>(fullChars);
        Random r = new Random();
        for (int i = 0; i < 7; i++) {
            Card randomElement = characters.get(r.nextInt(characters.size()));
            randomList.add(randomElement);
            characters.remove(randomElement);
        }
        return randomList;
    }

    // Public Methods

    /**
     * Generates a brand new ghost user. A Ghost User starts with a basic set of
     * cards and a random display name.
     * 
     * @return The ghost user.
     */
    public synchronized User generateGhostUser() {
        int numberOfUsers = ((List<User>) userRepository.findAll()).size();
        User user = new User();
        user.makeGhostUser("user0" + numberOfUsers);
        List<Card> cards = generateGhostUserCards();
        user.setCollection(new HashSet<Card>(cards));
        user.setDeck(cards);
        return user;
    }

    /**
     * Adds a friend to the user. It creates a friendship between the user and the
     * friend.
     * 
     * @param user           The user to add the friend.
     * @param friendUsername The username of the friend to be added.
     * @return The friend added.
     */
    public Friend addFriend(User user, String friendUsername) {
        if (user.getUsername().equals(friendUsername)) {
            throw new IllegalArgumentException("Cannot add yourself as a friend");
        }
        User friend = userRepository.findByUsername(friendUsername)
                .orElseThrow(() -> new IllegalArgumentException("Friend not found!"));
        Friendship friendship = new Friendship(user, friend);
        friendshipService.save(friendship);
        return new Friend(friend);
    }
}
