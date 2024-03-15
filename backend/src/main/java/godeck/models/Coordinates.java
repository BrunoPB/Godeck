package godeck.models;

import org.springframework.stereotype.Component;

/**
 * Represents a pair of coordinates.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class Coordinates {
    // Properties

    public int x;
    public int y;

    // Constructors

    /**
     * Default constructor.
     */
    public Coordinates() {
    }

    /**
     * Main constructor.
     * 
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Public Methods

    /**
     * Returns the coordinates in a JSON format.
     * 
     * @return The coordinates in a JSON format.
     */
    public String toJSONString() {
        return "{\"x\":" + this.x + ",\"y\":" + this.y + "}";
    }
}
