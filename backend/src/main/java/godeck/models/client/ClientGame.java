package godeck.models.client;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
 * @see godeck.models.in_game.Game
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
        this.board = board.stream().map(row -> row.stream()
                .map(card -> new ClientInGameCard(card)).toList())
                .collect(Collectors.toCollection(ArrayList::new));
        this.deck = deck.stream().map(card -> new ClientInGameCard(card)).toList();
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
        this.deck = deck.stream().map(card -> new ClientInGameCard(card)).toList();
        this.board = board.stream().map(row -> row.stream()
                .map(card -> new ClientInGameCard(card)).toList())
                .collect(Collectors.toCollection(ArrayList::new));
        this.turn = turn;
    }
}
