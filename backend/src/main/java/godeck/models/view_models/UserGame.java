package godeck.models.view_models;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import godeck.models.InGameCard;

/**
 * Class that represents the information a player can receive about it's own
 * game.
 * 
 * Is a component. Can be accessed from anywhere in the application.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class UserGame {
    // Properties

    private ArrayList<ArrayList<InGameCard>> board;
    private ArrayList<InGameCard> deck;
    private int number;
    private boolean turn;
    private Opponent opponent;

    // Constructors

    /**
     * Default constructor.
     */
    public UserGame() {
    }

    /**
     * Main constructor.
     * 
     * @param deck     The deck of the player.
     * @param number   The number of the player.
     * @param turn     If it is the player's turn.
     * @param opponent The opponent of the player.
     */
    public UserGame(ArrayList<ArrayList<InGameCard>> board, ArrayList<InGameCard> deck, int number, boolean turn,
            Opponent opponent) {
        this.board = board;
        this.number = number;
        this.deck = deck;
        this.turn = turn;
        this.opponent = opponent;
    }

    // Getters and Setters

    public ArrayList<ArrayList<InGameCard>> getBoard() {
        return this.board;
    }

    public ArrayList<InGameCard> getDeck() {
        return this.deck;
    }

    public int getNumber() {
        return this.number;
    }

    public boolean getTurn() {
        return this.turn;
    }

    public Opponent getOpponent() {
        return this.opponent;
    }

    public void setBoard(ArrayList<ArrayList<InGameCard>> board) {
        this.board = board;
    }

    public void setDeck(ArrayList<InGameCard> deck) {
        this.deck = deck;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public void setOpponent(Opponent opponent) {
        this.opponent = opponent;
    }

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
