package godeck.models.view_models;

import org.springframework.stereotype.Component;

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

    private String name;

    // Public Methods

    /**
     * Returns the JSON representation of the opponent.
     * 
     * @return The JSON representation of the opponent.
     */
    public String toJSONString() {
        return "{\"name\":\"" + this.name + "\"}";
    }
}
