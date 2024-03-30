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
 * Class that represents a friend to a user. It is used to provide information
 * about a friend to the user.
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
public class Friend {
    // Properties

    private UUID id;
    private String username;
    private String displayName;
    private List<Card> deck = new ArrayList<Card>();
    private Set<Card> collection = new HashSet<Card>();

    // Constructors

    /**
     * Creates a new friend from a user. It copies the necessary user's information.
     * 
     * @param user The user to copy the information from.
     */
    public Friend(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.displayName = user.getDisplayName();
        this.deck = user.getDeck();
        this.collection = user.getCollection();
    }
}