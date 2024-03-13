package godeck.models;

import org.springframework.stereotype.Component;

/**
 * Class that represents the information a player should receive about the end
 * of a game.
 * 
 * Is a component. Can be accessed from anywhere in the application.
 * 
 * @author Bruno Pena
 */
@Component
public class EndGameInfo {
    // Properties

    private int winner;
    private String reason;
    private int gold;
    private int ranking;

    // Constructors

    /**
     * Default constructor.
     */
    public EndGameInfo() {
    }

    /**
     * Main constructor.
     * 
     * @param winner  The winner of the game.
     * @param reason  The reason the game ended.
     * @param gold    The gold the winner received.
     * @param ranking The ranking of the winner.
     */
    public EndGameInfo(int winner, String reason, int gold, int ranking) {
        this.winner = winner;
        this.reason = reason;
        this.gold = gold;
        this.ranking = ranking;
    }

    // Public Methods

    /**
     * Returns the JSON representation of the end game info.
     * 
     * @return The JSON representation of the end game info.
     */
    public String toJSONString() {
        return "{\"winner\":" + winner + ",\"reason\":\"" + reason + "\",\"gold\":" + gold + ",\"ranking\":" + ranking
                + "}";
    }
}
