package godeck.models;

import org.springframework.stereotype.Component;

/**
 * Represents a move in the game.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class GameMove {
    // Properties

    private int player;
    private Coordinates initialCoords;
    private Coordinates finalCoords;
    private GameCharacter character;

    // Constructors

    public GameMove() {
    }

    public GameMove(int player, Coordinates initialCoords, Coordinates finalCoords) {
        this.player = player;
        this.initialCoords = initialCoords;
        this.finalCoords = finalCoords;
    }

    public GameMove(int player, Coordinates initialCoords, Coordinates finalCoords, GameCharacter character) {
        this.player = player;
        this.initialCoords = initialCoords;
        this.finalCoords = finalCoords;
        this.character = character;
    }

    public GameMove(String stringMove) { // TODO: Improve this
        String[] move = stringMove.split(" ");
        this.player = Integer.parseInt(move[0]);
        this.initialCoords = new Coordinates(Integer.parseInt(move[1]), Integer.parseInt(move[2]));
        this.finalCoords = new Coordinates(Integer.parseInt(move[3]), Integer.parseInt(move[4]));
    }

    // Getters and Setters

    public int getPlayer() {
        return player;
    }

    public Coordinates getInitialCoords() {
        return initialCoords;
    }

    public Coordinates getFinalCoords() {
        return finalCoords;
    }

    public GameCharacter getCharacter() {
        return character;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public void setInitialCoords(Coordinates initialCoords) {
        this.initialCoords = initialCoords;
    }

    public void setFinalCoords(Coordinates finalCoords) {
        this.finalCoords = finalCoords;
    }

    public void setCharacter(GameCharacter character) {
        this.character = character;
    }

    // Public Methods

    public String toString() {
        return "GameMove [player=" + player + ", initialCoords=" + initialCoords + ", finalCoords=" + finalCoords
                + ", character=" + character + "]";
    }
}
