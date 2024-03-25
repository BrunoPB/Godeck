package godeck.models;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Represents a pair of coordinates.
 * 
 * @author Bruno Pena Baeta
 */
@Component
@NoArgsConstructor
@AllArgsConstructor
public class Coordinates {
    // Properties

    public int x;
    public int y;

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
