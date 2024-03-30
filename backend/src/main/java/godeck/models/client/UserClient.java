package godeck.models.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;

import godeck.models.entities.Card;
import godeck.models.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class that represents the information a player can receive about it's own
 * user.
 * 
 * Is a component. Can be accessed from anywhere in the application.
 * 
 * @author Bruno Pena Baeta
 */
@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserClient {
    // Properties

    private UUID id;
    private String username;
    private String displayName;
    private String email;
    private Integer gold;
    private Integer crystals;
    private List<Card> deck = new ArrayList<Card>();
    private Set<Card> collection = new HashSet<Card>();
    private boolean ghost;
    private List<Friend> friends = new ArrayList<Friend>();

    // Constructors

    /**
     * Creates a new user client from a user. Creates an empty list of friends.
     * 
     * @param user The user to copy the information from.
     */
    public UserClient(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.displayName = user.getDisplayName();
        this.email = user.getEmail();
        this.gold = user.getGold();
        this.crystals = user.getCrystals();
        this.deck = user.getDeck();
        this.collection = user.getCollection();
        this.ghost = user.isGhost();
        this.friends = new ArrayList<Friend>();
    }

    /**
     * Creates a new user client from a user and a list of friends.
     * 
     * @param user    The user to copy the information from.
     * @param friends The list of friends to copy the information from.
     */
    public UserClient(User user, List<User> friends) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.displayName = user.getDisplayName();
        this.email = user.getEmail();
        this.gold = user.getGold();
        this.crystals = user.getCrystals();
        this.deck = user.getDeck();
        this.collection = user.getCollection();
        this.ghost = user.isGhost();
        this.friends = friends.stream().map(friend -> new Friend(friend)).toList();
    }
}
