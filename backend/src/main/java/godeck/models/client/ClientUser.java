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
    private List<ClientCard> deck;
    private Set<ClientCard> collection;
    private boolean ghost;
    private Set<Friend> friends;

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
        for (Card card : user.getDeck()) {
            this.deck.add(new ClientCard(card));
        }
        this.collection = new HashSet<ClientCard>();
        for (Card card : user.getCollection()) {
            this.collection.add(new ClientCard(card));
        }
        this.ghost = user.isGhost();
        this.friends = new HashSet<Friend>();
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
        this.deck = new ArrayList<ClientCard>();
        for (Card card : user.getDeck()) {
            this.deck.add(new ClientCard(card));
        }
        this.collection = new HashSet<ClientCard>();
        for (Card card : user.getCollection()) {
            this.collection.add(new ClientCard(card));
        }
        this.ghost = user.isGhost();
        this.friends = new HashSet<Friend>();
        for (User uf : friends) {
            this.friends.add(new Friend(uf));
        }
    }
}
