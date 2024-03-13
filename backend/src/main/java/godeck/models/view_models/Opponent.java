package godeck.models.view_models;

import org.springframework.stereotype.Component;

/**
 * Class that represents the information a player can receive about it's
 * opponent during a game.
 * 
 * Is a component. Can be accessed from anywhere in the application.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class Opponent {
    // Properties

    private String name;

    // Constructors

    /*
     * Default constructor.
     */
    public Opponent() {
    }

    /*
     * Main constructor.
     * 
     * @param name The name of the opponent.
     */
    public Opponent(String name) {
        this.name = name;
    }

    // Getters and Setters

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
