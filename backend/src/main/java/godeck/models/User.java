package godeck.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    // Properties

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "name", nullable = false, length = 15, unique = true)
    private String name;
    @Column(name = "email", nullable = false, length = 30, unique = true)
    private String email;
    @Column(name = "gold", nullable = false)
    private Integer gold;
    @Column(name = "crystals", nullable = false)
    private Integer crystals;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_deck", joinColumns = @JoinColumn(name = "game_character_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<GameCharacter> deck = new ArrayList<GameCharacter>();
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_collection", joinColumns = @JoinColumn(name = "game_character_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<GameCharacter> collection = new HashSet<>();

    // Constructors

    /**
     * Constructor without id.
     * 
     * @param name       User's name
     * @param email      User's email
     * @param gold       User's gold
     * @param crystals   User's crystals
     * @param deck       User's deck
     * @param collection User's collection
     */
    public User(String name, String email, Integer gold, Integer crystals, List<GameCharacter> deck,
            Set<GameCharacter> collection) {
        this.name = name;
        this.email = email;
        this.gold = gold;
        this.crystals = crystals;
        this.deck = deck;
        this.collection = collection;
    }

    // Methods

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User u = (User) obj;
            return this.id.equals(u.id);
        }
        return false;
    }
}
