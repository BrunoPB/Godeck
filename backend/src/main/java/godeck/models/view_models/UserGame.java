package godeck.models.view_models;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import godeck.models.InGameCard;
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

    /**
     * Returns the JSON representation of the user's deck in the game.
     * 
     * @return The JSON representation of the user's deck in the game.
     */
    public String getDeckJSONString() {
        String deckString = "[";
        for (InGameCard card : this.deck) {
            deckString += card.toJSONString() + ",";
        }
        if (deckString.length() > 1) {
            deckString = deckString.substring(0, deckString.length() - 1);
        }
        deckString += "]";
        return deckString;
    }

    /**
     * Returns the JSON representation of the board in the game.
     * 
     * @return The JSON representation of the board in the game.
     */
    public String getBoardJSONString() {
        String boardString = "[";
        for (ArrayList<InGameCard> row : this.board) {
            boardString += "[";
            for (InGameCard card : row) {
                if (card != null) {
                    boardString += card.toJSONString() + ",";
                } else {
                    boardString += "null,";
                }
            }
            if (boardString.length() > 1) {
                boardString = boardString.substring(0, boardString.length() - 1);
            }
            boardString += "],";
        }
        if (boardString.length() > 1) {
            boardString = boardString.substring(0, boardString.length() - 1);
        }
        boardString += "]";
        return boardString;
    }
}
