package godeck.models;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

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

    public GameMove(String stringMove) {
        JSONObject move = new JSONObject(stringMove);
        this.player = move.getInt("player");
        this.deckIndex = move.getInt("deckIndex");
        this.coords = new Coordinates(move.getJSONObject("coords").getInt("x"),
                move.getJSONObject("coords").getInt("y"));
        this.inGameCard = new InGameCard(move.getJSONObject("inGameCard").toString());
    }

    // Public Methods

    public String toJSONString() {
        return "{\"player\":" + player + ",\"deckIndex\":" + deckIndex + ",\"coords\":" + coords.toJSONString()
                + ",\"inGameCard\":" + inGameCard.toJSONString() + "}";
    }
}
