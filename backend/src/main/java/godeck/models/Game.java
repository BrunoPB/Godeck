package godeck.models;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

/**
 * Represents a game. It is responsible for managing the game state,
 * executing the moves and provide all game information.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class Game {
    // Properties

    private List<GameCharacter> deck0;
    private List<GameCharacter> deck1;
    private boolean board;
    private int turn;
    private int round;
    private int gameWinner;
    public CompletableFuture<Void> over;

    // Constructors

    public Game(List<GameCharacter> deck0, List<GameCharacter> deck1) { // TODO: Implement Game
        this.deck0 = deck0;
        this.deck1 = deck1;
        this.board = false;
        this.turn = 0;
        this.round = 0;
        this.over = new CompletableFuture<Void>();
    }

    // Public Methods

    public boolean verifyMove(int player, GameMove move) { // TODO: Implement validations
        return true;
    }

    public void executeMove(int user, boolean move) {
    }

    public void executeSurrender(int user) {
        this.gameWinner = user == 0 ? 1 : 0;
        this.over.complete(null);
    }

    public int getGameWinner() { // TODO: Implement getGameWinner
        return gameWinner;
    }
}
