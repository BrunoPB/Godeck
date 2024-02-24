package godeck.models;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class Game {
    private List<GameCharacter> deck0;
    private List<GameCharacter> deck1;
    private boolean board;
    private int turn;
    private int round;

    public Game(List<GameCharacter> deck0, List<GameCharacter> deck1) { // TODO: Implement Game
        this.deck0 = deck0;
        this.deck1 = deck1;
        this.board = false;
        this.turn = 0;
        this.round = 0;
    }

    public void executeMove(int user, boolean move) {
    }

    public boolean isGameOver() {
        return false;
    }
}
