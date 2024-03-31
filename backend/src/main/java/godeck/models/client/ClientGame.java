package godeck.models.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import godeck.models.ingame.InGameCard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class that represents the information a player can receive about it's own
 * game.
 * 
 * @see godeck.models.ingame.Game
 * @author Bruno Pena Baeta
 */
@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientGame {
    // Properties

    private List<List<ClientInGameCard>> board;
    private List<ClientInGameCard> deck;
    private boolean turn;
    private int number;
    private Opponent opponent;
    private int timeLimit;

    // Constructors

    /**
     * Creates a new game client from a game.
     * 
     * @param board       The board of the game.
     * @param deck        The deck of the player.
     * @param turn        If it is the player's turn.
     * @param number      The number of the player.
     * @param opponent    The opponent of the player.
     * @param turnTimeout The time limit for the player's turn.
     */
    public ClientGame(ArrayList<ArrayList<InGameCard>> board, ArrayList<InGameCard> deck, boolean turn, int number,
            Opponent opponent, int turnTimeout) {
        this.board = new ArrayList<List<ClientInGameCard>>();
        for (ArrayList<InGameCard> column : board) {
            ArrayList<ClientInGameCard> newColumn = new ArrayList<ClientInGameCard>();
            for (InGameCard card : column) {
                if (card == null) {
                    newColumn.add(null);
                } else {
                    newColumn.add(new ClientInGameCard(card));
                }
            }
            this.board.add(newColumn);
        }

        this.deck = new ArrayList<ClientInGameCard>();
        for (InGameCard card : deck) {
            this.deck.add(new ClientInGameCard(card));
        }

        this.turn = turn;
        this.number = number;
        this.opponent = opponent;
        this.timeLimit = turnTimeout;
    }

    // Public Methods

    /**
     * Updates player's game state.
     * 
     * @param deck  The deck of the player.
     * @param board The board of the game.
     * @param turn  If it is the player's turn.
     */
    public void updateGameState(ArrayList<ArrayList<InGameCard>> board, ArrayList<InGameCard> deck, boolean turn) {
        this.board = new ArrayList<List<ClientInGameCard>>();
        for (ArrayList<InGameCard> column : board) {
            ArrayList<ClientInGameCard> newColumn = new ArrayList<ClientInGameCard>();
            for (InGameCard card : column) {
                if (card == null) {
                    newColumn.add(null);
                } else {
                    newColumn.add(new ClientInGameCard(card));
                }
            }
            this.board.add(newColumn);
        }

        this.deck = new ArrayList<ClientInGameCard>();
        for (InGameCard card : deck) {
            this.deck.add(new ClientInGameCard(card));
        }

        this.turn = turn;
    }
}
