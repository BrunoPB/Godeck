package godeck.models.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import godeck.models.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class that represents the information a player can receive about it's own
 * user.
 * 
 * @see godeck.models.entities.User
 * @author Bruno Pena Baeta
 */
@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientUser {
    // Properties

    private UUID id;
    private String username;
    private String displayName;
    private String email;
    private Integer gold;
    private Integer crystals;
    private Integer platinum;
    private List<ClientCard> deck = new ArrayList<ClientCard>();
    private Set<ClientCard> collection = new HashSet<ClientCard>();
    private boolean ghost;
    private List<Friend> friends = new ArrayList<Friend>();

    // Constructors

    /**
     * Creates a new user client from a user. Creates an empty list of friends.
     * 
     * @param user The user to copy the information from.
     */
    public ClientUser(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.displayName = user.getDisplayName();
        this.email = user.getEmail();
        this.gold = user.getGold();
        this.crystals = user.getCrystals();
        this.platinum = user.getPlatinum();
        this.deck = user.getDeck().stream().map(card -> new ClientCard(card)).toList();
        this.collection = user.getCollection().stream().map(card -> new ClientCard(card)).collect(Collectors.toSet());
        this.ghost = user.isGhost();
        this.friends = new ArrayList<Friend>();
    }

    /**
     * Creates a new user client from a user and a list of friends.
     * 
     * @param user    The user to copy the information from.
     * @param friends The list of friends to copy the information from.
     */
    public ClientUser(User user, List<User> friends) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.displayName = user.getDisplayName();
        this.email = user.getEmail();
        this.gold = user.getGold();
        this.crystals = user.getCrystals();
        this.platinum = user.getPlatinum();
        this.deck = user.getDeck().stream().map(card -> new ClientCard(card)).toList();
        this.collection = user.getCollection().stream().map(card -> new ClientCard(card)).collect(Collectors.toSet());
        this.ghost = user.isGhost();
        this.friends = friends.stream().map(friend -> new Friend(friend)).toList();
    }
}
