package godeck.models.client;

import org.springframework.stereotype.Component;

import godeck.models.Coordinates;
import godeck.models.in_game.GameMove;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents what the client sees as a game move.
 * 
 * @see godeck.models.in_game.GameMove
 * @author Bruno Pena Baeta
 */
@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientGameMove {
    // Properties

    private int player;
    private int deckIndex;
    private Coordinates coords;
    private ClientInGameCard inGameCard;

    // Constructors

    /**
     * Constructor that receives a GameMove object and creates a ClientGameMove
     * object.
     * 
     * @param gameMove The GameMove object.
     */
    public ClientGameMove(GameMove gameMove) {
        this.player = gameMove.getPlayer();
        this.deckIndex = gameMove.getDeckIndex();
        this.coords = gameMove.getCoords();
        this.inGameCard = new ClientInGameCard(gameMove.getInGameCard());
    }
}
