package godeck.models.client;

import org.springframework.stereotype.Component;

import godeck.models.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class that represents the information a player can receive about it's
 * opponent during a game.
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
public class Opponent {
    // Properties

    private String displayName;

    // Constructors

    /**
     * Creates a new opponent from a user.
     * 
     * @param user The user to copy the information from.
     */
    public Opponent(User user) {
        this.displayName = user.getDisplayName();
    }
}
