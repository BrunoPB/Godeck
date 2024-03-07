package godeck.services;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import godeck.models.QueueItem;
import godeck.models.QueueResponse;
import godeck.models.User;
import godeck.queue.QueueSingleton;
import godeck.repositories.UserRepository;
import godeck.utils.ErrorHandler;

/**
 * Service for handling queue manipulations from the controller http requests.
 * 
 * @author Bruno Pena Baeta
 */
@Service
public class QueueService {
    // Properties

    @Value("${queue_timeout_s}")
    private int QUEUE_TIMEOUT_S;
    private UserRepository userRepository;

    // Constructors

    /**
     * Main constructor. Uses Autowire to inject the UserRepository.
     * 
     * @param userRepository
     */
    @Autowired
    public QueueService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Private Methods

    /**
     * Returns the user from the database by its id. If the user does not exist, it
     * throws an exception.
     * 
     * @param stringUserId The id of the user.
     * @return The user from the database.
     */
    private User getUserById(String stringUserId) {
        UUID userId = UUID.fromString(stringUserId);
        if (userId == null) {
            throw new IllegalArgumentException("User id cannot be null.");
        }
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found!"));
    }

    // Public Methods

    /**
     * Queues the user for a game. It adds the user to the queue and waits until the
     * user is dequeued or the game is found. It returns the queue response for the
     * user. It is used by the controller to handle the queue http request.
     * 
     * @param stringUserId The id of the user.
     * @return The queue response for the user.
     */
    public QueueResponse queue(String stringUserId) {
        User user = getUserById(stringUserId);

        CompletableFuture<Integer> futurePort = new CompletableFuture<Integer>();

        QueueSingleton.getInstance().queue(new QueueItem(user, futurePort));

        int port = 0;

        try {
            port = futurePort.get(QUEUE_TIMEOUT_S, TimeUnit.SECONDS);
        } catch (Exception e) {
            ErrorHandler.message(new Exception("Queue timeout!"));
            return new QueueResponse(false, 0, "Queue timeout!");
        }

        return new QueueResponse(true, port, "Game found!");
    }

    /**
     * Removes the user from the queue. It is used by the controller to handle the
     * dequeue http request. It returns the queue response for the user. The queue
     * response status will always be false.
     * 
     * @param stringUserId The id of the user.
     * @return The queue response for the user.
     */
    public QueueResponse dequeue(String stringUserId) {
        User user = getUserById(stringUserId);
        QueueSingleton.getInstance().dequeue(user);
        return new QueueResponse(false, 0, "User removed from queue!");
    }
}
