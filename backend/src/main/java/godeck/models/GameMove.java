package godeck.models;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

/**
 * Represents a move in the game.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class GameMove {
    // Properties

    private int player;
    private int deckIndex;
    private Coordinates coords;
    private InGameCard card;

    // Constructors

    /**
     * Default constructor.
     */
    public GameMove() {
    }

    public GameMove(int player, int deckIndex, Coordinates coords, InGameCard character) {
        this.player = player;
        this.coords = coords;
        this.card = character;
    }

    public GameMove(String stringMove) {
        JSONObject move = new JSONObject(stringMove);
        this.player = move.getInt("player");
        this.deckIndex = move.getInt("deckIndex");
        this.coords = new Coordinates(move.getJSONObject("coords").getInt("x"),
                move.getJSONObject("coords").getInt("y"));
        this.card = new InGameCard(move.getJSONObject("card").toString());
    }

    // Getters and Setters

    public int getPlayer() {
        return player;
    }

    public int getDeckIndex() {
        return deckIndex;
    }

    public Coordinates getCoords() {
        return coords;
    }

    public InGameCard getInGameCard() {
        return card;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public void setDeckIndex(int deckIndex) {
        this.deckIndex = deckIndex;
    }

    public void setCoords(Coordinates coords) {
        this.coords = coords;
    }

    public void setInGameCard(InGameCard character) {
        this.card = character;
    }

    // Public Methods

    public String toJSONString() {
        return "{\"player\":" + player + ",\"deckIndex\":" + deckIndex + ",\"coords\":" + coords.toJSONString()
                + ",\"card\":" + card.toJSONString() + "}";
    }
}
