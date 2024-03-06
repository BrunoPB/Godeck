package godeck.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import godeck.game.GameServerSingleton;
import godeck.models.QueueResponse;
import godeck.models.User;
import godeck.models.UserNumberAndPort;
import godeck.queue.QueueSingleton;
import godeck.repositories.UserRepository;
import godeck.utils.ErrorHandler;
import godeck.utils.ThreadUtils;

/**
 * Service for handling queue manipulations from the controller http requests.
 * 
 * @author Bruno Pena Baeta
 */
@Service
public class QueueService {
    private UserRepository userRepository;

    /**
     * Main constructor. Uses Autowire to inject the UserRepository.
     * 
     * @param userRepository
     */
    @Autowired
    public QueueService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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

    /**
     * Waits while the user is in the queue. It is used by the queue method to wait
     * until the user is dequeued or the game is found.
     * 
     * @param user The user that is in the queue.
     */
    private void waitWhileUserIsInQueue(User user) {
        while (QueueSingleton.getInstance().isInQueue(user)) {
            ThreadUtils.sleep(10);
        }
    }

    /**
     * Returns the queue response for the user. It is used by the queue method to
     * return the response to the http request.
     * 
     * @param user The user that is in the queue.
     * @return The queue response for the user.
     */
    private QueueResponse getQueueResponseForUser(User user) {
        UserNumberAndPort userNumberAndPort;
        try {
            userNumberAndPort = GameServerSingleton.getInstance().getUserNumberAndPort(user);
        } catch (IllegalArgumentException e) {
            ErrorHandler.message(e);
            return new QueueResponse(false, 0, "No game found!");
        }
        return new QueueResponse(true, userNumberAndPort.port, "Game found on port " + userNumberAndPort.port + "!");
    }

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
        QueueSingleton.getInstance().queue(user);
        waitWhileUserIsInQueue(user);
        return getQueueResponseForUser(user);
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
        return new QueueResponse(false, 0, "User " + user.getId() + "removed from queue!");
    }
}
