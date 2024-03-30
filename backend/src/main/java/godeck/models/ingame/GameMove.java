package godeck.models.ingame;

import org.springframework.stereotype.Component;

import godeck.models.Coordinates;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a move in the game.
 * 
 * @author Bruno Pena Baeta
 */
@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameMove {
    // Properties

    private int player;
    private int deckIndex;
    private Coordinates coords;
    private InGameCard inGameCard;
}
