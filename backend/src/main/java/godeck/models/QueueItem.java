package godeck.models;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

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
    public CompletableFuture<Integer> futurePort;
}
