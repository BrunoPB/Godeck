package godeck.models.ingame;

import org.springframework.stereotype.Component;

import godeck.models.Coordinates;
import godeck.models.client.ClientGameMove;
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

    // Constructors

    /**
     * Constructor that receives a ClientGameMove object and creates a GameMove
     * object.
     * 
     * @param player The player that made the move.
     * @param cMove  The ClientGameMove object.
     */
    public GameMove(int player, ClientGameMove cMove) {
        this.player = player;
        this.deckIndex = cMove.getDeckIndex();
        this.coords = cMove.getCoords();
        this.inGameCard = new InGameCard(cMove.getInGameCard());
    }
}
