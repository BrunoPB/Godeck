package godeck.models.client;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import godeck.models.in_game.InGameCard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class that represents the information a player can receive about it's own
 * game.
 * 
 * Is a component. Can be accessed from anywhere in the application.
 * 
 * @author Bruno Pena Baeta
 */
@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserGame {
    // Properties

    private ArrayList<ArrayList<InGameCard>> board;
    private ArrayList<InGameCard> deck;
    private int number;
    private boolean turn;
    private Opponent opponent;

    // Public Methods

    /**
     * Updates player's game state.
     * 
     * @param deck  The deck of the player.
     * @param board The board of the game.
     * @param turn  If it is the player's turn.
     */
    public void updateGameState(ArrayList<InGameCard> deck, ArrayList<ArrayList<InGameCard>> board, boolean turn) {
        this.deck = deck;
        this.board = board;
        this.turn = turn;
    }
}
