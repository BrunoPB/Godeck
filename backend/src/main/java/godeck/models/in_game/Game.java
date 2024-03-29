package godeck.models.in_game;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import godeck.game.GameTimer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a game. It is responsible for managing the game state,
 * executing the moves and provide all game information.
 * 
 * @author Bruno Pena Baeta
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Component
public class Game {
    // Properties

    private ArrayList<InGameCard> deck0;
    private ArrayList<InGameCard> deck1;
    private ArrayList<ArrayList<InGameCard>> board;
    private int turn;
    private int round;
    private int gameWinner;
    private String endGameReason;
    private GameTimer timer;
    public CompletableFuture<String> over;

    // Constructors

    /**
     * Main constructor.
     * 
     * @param deck0 The deck of player 0.
     * @param deck1 The deck of player 1.
     */
    public Game(ArrayList<InGameCard> deck0, ArrayList<InGameCard> deck1) {
        this.deck0 = deck0;
        this.deck1 = deck1;
        buildInitialBoard();
        this.turn = 0;
        this.round = 0;
        this.gameWinner = -1;
        this.timer = new GameTimer();
        this.over = new CompletableFuture<String>();
    }

    // Private Methods

    /**
     * Builds the initial game board.
     */
    private void buildInitialBoard() {
        this.board = new ArrayList<ArrayList<InGameCard>>();
        for (int col = 0; col < 3; col++) {
            ArrayList<InGameCard> data = new ArrayList<InGameCard>();
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
        for (ArrayList<InGameCard> col : this.board) {
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
     * Executes the domination of the cards around by the given move.
     * 
     * @param move The move that caused the domination.
     */
    private void executeDomination(GameMove move) {
        Coordinates coords = move.getCoords();
        int column = coords.x;
        int row = coords.y;
        InGameCard cardData = move.getInGameCard();
        int player = move.getPlayer();

        int value;
        InGameCard compareCard = null;

        // North
        try {
            compareCard = board.get(column).get(row - 1);
            if (compareCard.exists()) {
                value = cardData.getCard().getNorth();
                if (value > compareCard.getCard().getSouth()) {
                    compareCard.setCurrentDominator(player);
                }
            }
        } catch (IndexOutOfBoundsException e) {
        } catch (NullPointerException e) {
        }

        // North East
        try {
            if (column % 2 == 0) {
                compareCard = board.get(column + 1).get(row);
            } else {
                compareCard = board.get(column + 1).get(row - 1);
            }
            if (compareCard != null && compareCard.exists()) {
                value = cardData.getCard().getNorthEast();
                if (value > compareCard.getCard().getSouthWest()) {
                    compareCard.setCurrentDominator(player);
                }
            }
        } catch (IndexOutOfBoundsException e) {
        } catch (NullPointerException e) {
        }

        // South East
        try {
            if (column % 2 == 0) {
                compareCard = board.get(column + 1).get(row + 1);
            } else {
                compareCard = board.get(column + 1).get(row);
            }
            if (compareCard != null && compareCard.exists()) {
                value = cardData.getCard().getSouthEast();
                if (value > compareCard.getCard().getNorthWest()) {
                    compareCard.setCurrentDominator(player);
                }
            }
        } catch (IndexOutOfBoundsException e) {
        } catch (NullPointerException e) {
        }

        // South
        try {
            compareCard = board.get(column).get(row + 1);
            if (compareCard.exists()) {
                value = cardData.getCard().getSouth();
                if (value > compareCard.getCard().getNorth()) {
                    compareCard.setCurrentDominator(player);
                }
            }
        } catch (IndexOutOfBoundsException e) {
        } catch (NullPointerException e) {
        }

        // South West
        try {
            if (column % 2 == 0) {
                compareCard = board.get(column - 1).get(row + 1);
            } else {
                compareCard = board.get(column - 1).get(row);
            }
            if (compareCard != null && compareCard.exists()) {
                value = cardData.getCard().getSouthWest();
                if (value > compareCard.getCard().getNorthEast()) {
                    compareCard.setCurrentDominator(player);
                }
            }
        } catch (IndexOutOfBoundsException e) {
        } catch (NullPointerException e) {
        }

        // North West
        try {
            if (column % 2 == 0) {
                compareCard = board.get(column - 1).get(row);
            } else {
                compareCard = board.get(column - 1).get(row - 1);
            }
            if (compareCard != null && compareCard.exists()) {
                value = cardData.getCard().getNorthWest();
                if (value > compareCard.getCard().getSouthEast()) {
                    compareCard.setCurrentDominator(player);
                }
            }
        } catch (IndexOutOfBoundsException e) {
        } catch (NullPointerException e) {
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
        for (ArrayList<InGameCard> col : this.board) {
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
    public boolean verifyMove(int player, GameMove move) {
        try {
            if (this.turn != player)
                return false;
            if (player != move.getPlayer())
                return false;
            if (player == 0 && !this.deck0.get(move.getDeckIndex()).exists())
                return false;
            if (player == 1 && !this.deck1.get(move.getDeckIndex()).exists())
                return false;
            if (this.board.get(move.getCoords().x).get(move.getCoords().y) == null)
                return false;
            if (this.board.get(move.getCoords().x).get(move.getCoords().y).exists())
                return false;
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Executes the given move. Updates the game state.
     * 
     * @param move The move to execute.
     */
    public void executeMove(GameMove move) {
        InGameCard card = move.getInGameCard();
        Coordinates coords = move.getCoords();
        this.board.get(coords.x).set(coords.y, card);
        if (move.getPlayer() == 0) {
            this.deck0.get(move.getDeckIndex()).setExists(false);
            turn = 1;
        } else {
            this.deck1.get(move.getDeckIndex()).setExists(false);
            turn = 0;
        }
        executeDomination(move);
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
}
