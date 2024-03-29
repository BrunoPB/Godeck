package godeck.models.in_game;

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
}
