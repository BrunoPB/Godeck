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
}
