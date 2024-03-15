package godeck.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
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

    private List<InGameCard> deck0;
    private List<InGameCard> deck1;
    private Vector<Vector<InGameCard>> board;
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
    public Game(List<InGameCard> deck0, List<InGameCard> deck1) { // TODO: Implement Game
        this.deck0 = deck0;
        this.deck1 = deck1;
        buildInitialBoard();
        this.turn = 0;
        this.round = 0;
        this.gameWinner = -1;
        this.timer = new GameTimer();
        this.over = new CompletableFuture<String>();
    }

    // Getters and Setters

    public List<InGameCard> getDeck0() {
        return deck0;
    }

    public void setDeck0(List<InGameCard> deck0) {
        this.deck0 = deck0;
    }

    public List<InGameCard> getDeck1() {
        return deck1;
    }

    public void setDeck1(List<InGameCard> deck1) {
        this.deck1 = deck1;
    }

    public Vector<Vector<InGameCard>> getBoard() {
        return board;
    }

    public void setBoard(Vector<Vector<InGameCard>> board) {
        this.board = board;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getGameWinner() {
        return gameWinner;
    }

    public String getEndGameReason() {
        return endGameReason;
    }

    // Private Methods

    /**
     * Builds the initial game board.
     */
    private void buildInitialBoard() {
        this.board = new Vector<Vector<InGameCard>>();
        for (int col = 0; col < 3; col++) {
            Vector<InGameCard> data = new Vector<InGameCard>();
            for (int row = 0; row < 5; row++) {
                if ((col == 0 && row == 4) || (col == 2 && row == 4)) {
                    data.add(null);
                } else {
                    data.add(new InGameCard());
                }
            }
            this.board.add(data);
        }
    }

    /**
     * Calculates the winner of the game. Stores the result in the gameWinner
     * variable.
     */
    private void calculateGameWinner() {
        int p0 = 0;
        int p1 = 0;
        for (Vector<InGameCard> col : this.board) {
            for (InGameCard card : col) {
                if (card != null && card.exists()) {
                    if (card.getCurrentDominator() == 0) {
                        p0++;
                    } else {
                        p1++;
                    }
                }
            }
        }
        if (p0 > p1) {
            this.gameWinner = 0;
        } else if (p1 > p0) {
            this.gameWinner = 1;
        } else {
            this.gameWinner = -1;
        }
    }

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
     * Resets the game timer.
     */
    public void resetTimer() {
        this.timer.reset();
    }

    /**
     * Checks if the game is over. The game is over when the board is full.
     * 
     * @return True if the game is over. False otherwise.
     */
    public boolean checkEndGame() {
        for (Vector<InGameCard> col : this.board) {
            for (InGameCard card : col) {
                if (card != null && !card.exists()) {
                    return false;
                }
            }
        }
        calculateGameWinner();
        endGame("End");
        return true;
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
        return player == move.getPlayer() && this.turn == player;
    }

    /**
     * Executes the given move. Updates the game state.
     * 
     * @param move The move to execute.
     */
    public void executeMove(GameMove move) {
        InGameCard card = move.getCard();
        Coordinates coords = move.getCoords();
        this.board.get(coords.x).set(coords.y, card);
        if (move.getPlayer() == 0) {
            this.deck0.get(move.getDeckIndex()).setExistance(false);
            turn = 1;
        } else {
            this.deck1.get(move.getDeckIndex()).setExistance(false);
            turn = 0;
        }
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

    /**
     * Returns the JSON representation of the game board.
     * 
     * @return The JSON representation of the game board.
     */
    public String getBoardJSONString() {
        String boardString = "[";
        for (Vector<InGameCard> row : this.board) {
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
