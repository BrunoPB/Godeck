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
    public boolean test_gameover; // TODO: Delete this later

    public Game(List<GameCharacter> deck0, List<GameCharacter> deck1) { // TODO: Implement Game
        this.deck0 = deck0;
        this.deck1 = deck1;
        this.board = false;
        this.turn = 0;
        this.round = 0;
        this.test_gameover = false; // TODO: Delete this later
    }

    public boolean verifyMove(int player, GameMove move) { // TODO: Implement validations
        return true;
    }

    public void executeMove(int user, boolean move) {
    }

    public boolean isGameOver() { // TODO: This is just a test
        return test_gameover;
    }

    public int getGameWinner() { // TODO: Implement getGameWinner
        return 0;
    }
}
