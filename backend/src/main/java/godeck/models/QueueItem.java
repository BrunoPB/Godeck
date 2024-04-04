package godeck.models;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import godeck.models.entities.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Represents an item in the queue. It holds a user and a completable future
 * object that will hold the port number. It is used to manage the queue of
 * users waiting for a game.
 * 
 * @author Bruno Pena Baeta
 */
@Component
@NoArgsConstructor
@AllArgsConstructor
public class QueueItem {
    // Properties

    public User user;
    public CompletableFuture<Void> finished;
    public int port;
    public int number;
    public String key;
    public String iv;

    // Public Methods

    /**
     * Sets port, key and iv for the queue item.
     * 
     * @param port The port number.
     * @param key  The key.
     * @param iv   The iv.
     */
    public void setQueueItem(int port, int number, String key, String iv) {
        this.port = port;
        this.number = number;
        this.key = key;
        this.iv = iv;
    }
}
