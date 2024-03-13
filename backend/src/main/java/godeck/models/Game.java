package godeck.models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import godeck.game.GameTimer;

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
    private List<List<InGameCard>> board;
    private int turn;
    private int round;
    private int gameWinner;
    private String endGameReason;
    private GameTimer timer;
    public CompletableFuture<String> over;

    // Constructors

    /**
     * Default constructor.
     */
    public Game() {
    }

    /**
     * Main constructor.
     * 
     * @param deck0 The deck of player 0.
     * @param deck1 The deck of player 1.
     */
    public Game(List<GameCharacter> deck0, List<GameCharacter> deck1) { // TODO: Implement Game
        this.deck0 = deck0;
        this.deck1 = deck1;
        this.board = new ArrayList<List<InGameCard>>();
        this.turn = 0;
        this.round = 0;
        this.gameWinner = -1;
        this.timer = new GameTimer();
        this.over = new CompletableFuture<String>();
    }

    // Private Methods

    /**
     * Ends the game with the given reason.
     * 
     * @param reason The reason the game ended.
     */
    private void endGame(String reason) {
        if (this.timer.isAlive()) {
            this.timer.kill();
        }
        this.endGameReason = reason;
        this.over.complete(reason);
    }

    // Public Methods

    /**
     * Starts the game timer.
     */
    public GameTimer startTimer(int timeLimit) {
        this.timer.setTimeLimit(timeLimit);
        this.timer.start();
        return this.timer;
    }

    /**
     * Stops the game timer.
     */
    public void stopTimer() {
        if (this.timer.isAlive()) {
            this.timer.kill();
        }
    }

    /**
     * Notifies the game that the time is over. Decides the winner and ends the
     * game.
     */
    public void notifyTimeout() {
        this.gameWinner = this.turn == 0 ? 1 : 0;
        endGame("Timeout");
    }

    /**
     * Verifies if the given move is valid to be executed at given moment.
     * 
     * @param player The player that made the move.
     * @param move   The move to verify.
     * @return True if the move is valid. False otherwise.
     */
    public boolean verifyMove(int player, GameMove move) { // TODO: Implement validations
        return true;
    }

    public void executeMove(int user, GameMove move) {
    }

    /**
     * Executes the surrender of the given player.
     * 
     * @param user The player that surrendered.
     */
    public void executeSurrender(int user) {
        this.gameWinner = user == 0 ? 1 : 0;
        endGame("Surrender");
    }

    public int getGameWinner() { // TODO: Implement getGameWinner
        return gameWinner;
    }

    public String getEndGameReason() { // TODO: Implement getEndGameReason
        return endGameReason;
    }
}
