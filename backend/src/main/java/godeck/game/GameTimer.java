package godeck.game;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import godeck.models.GodeckThread;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class that represents a game timer. It is responsible for counting the time
 * limit per turn of a game and notifying when the time is over.
 * 
 * Is a component. Can be accessed from anywhere in the application.
 * 
 * @author Bruno Pena Baeta
 */
@Component
@NoArgsConstructor
@Getter
@Setter
public class GameTimer extends GodeckThread {
    // Properties

    private int time = 0;
    private int timeLimit;
    public CompletableFuture<String> timeOver = new CompletableFuture<String>();

    // Constructors

    /**
     * Constructor that initializes the time limit to the given value.
     * 
     * @param timeLimit The time limit.
     */
    public GameTimer(int timeLimit) {
        this.time = 0;
        this.timeLimit = timeLimit;
    }

    // Public Methods

    /**
     * Method that runs the game timer. It counts the time and notifies when the
     * time is over.
     */
    @Override
    public void run() {
        while (!super.exit) {
            try {
                Thread.sleep(1000);
                this.time++;
                if (this.time >= this.timeLimit) {
                    this.timeOver.complete("Timeout");
                    super.kill();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method that resets the game timer.
     */
    public void reset() {
        this.time = 0;
    }
}
