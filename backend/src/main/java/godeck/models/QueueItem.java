package godeck.models;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

/**
 * Represents an item in the queue. It holds a user and a completable future
 * object that will hold the port number. It is used to manage the queue of
 * users waiting for a game.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class QueueItem {
    // Properties

    public User user;
    public CompletableFuture<Integer> futurePort;

    // Constructors

    /**
     * Default constructor.
     */
    public QueueItem() {
    }

    /**
     * Main constructor.
     * 
     * @param user       The user.
     * @param futurePort The completable future object that will hold the port
     *                   number.
     */
    public QueueItem(User user, CompletableFuture<Integer> futurePort) {
        this.user = user;
        this.futurePort = futurePort;
    }
}
