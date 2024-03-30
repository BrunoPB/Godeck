package godeck.models.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import godeck.utils.Randomizer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a user. A user is a player of the game.
 * 
 * @author Bruno Pena Baeta
 */
@Entity(name = "user")
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    // Properties

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "username", nullable = false, length = 15, unique = true)
    private String username;
    @Column(name = "displayName", nullable = false, length = 15)
    private String displayName;
    @Column(name = "email", nullable = true, length = 30, unique = true)
    private String email;
    @Column(name = "gold", nullable = false)
    private Integer gold;
    @Column(name = "crystals", nullable = false)
    private Integer crystals;
    @ManyToMany
    @JoinTable(name = "user_deck", joinColumns = @JoinColumn(name = "game_character_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<Card> deck = new ArrayList<Card>();
    @ManyToMany
    @JoinTable(name = "user_collection", joinColumns = @JoinColumn(name = "game_character_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<Card> collection = new HashSet<Card>();
    @Column(name = "ghost", nullable = false)
    private boolean ghost;

    // Constructors

    /**
     * Main constructor.
     * 
     * @param username    User's username
     * @param displayName User's display name
     * @param email       User's email
     * @param gold        User's gold
     * @param crystals    User's crystals
     * @param deck        User's deck
     * @param collection  User's collection
     * @param ghost       User's ghost status
     */
    public User(String username, String displayName, String email, Integer gold, Integer crystals,
            List<Card> deck, Set<Card> collection, boolean ghost) {
        this.username = username;
        this.displayName = displayName;
        this.email = email;
        this.gold = gold;
        this.crystals = crystals;
        this.deck = deck;
        this.collection = collection;
        this.ghost = ghost;
    }

    // Public Methods

    /**
     * Makes this user a ghost user. A ghost user is a temporary user. It has no
     * email, no friends and a random display name.
     * 
     * @param username The ghost user's username.
     */
    public void makeGhostUser(String username) {
        this.username = username;
        this.displayName = Randomizer.generateUsername();
        this.email = null;
        this.gold = 0;
        this.crystals = 0;
        this.ghost = true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User u = (User) obj;
            return this.id.equals(u.id);
        }
        return false;
    }
}
