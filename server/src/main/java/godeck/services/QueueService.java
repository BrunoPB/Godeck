package godeck.services;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import godeck.models.QueueItem;
import godeck.models.entities.User;
import godeck.models.responses.QueueResponse;
import godeck.queue.QueueSingleton;
import godeck.utils.ErrorHandler;
import lombok.NoArgsConstructor;

/**
 * Service for handling queue manipulations from the controller http requests.
 * 
 * @author Bruno Pena Baeta
 */
@Service
@NoArgsConstructor
public class QueueService {
    // Properties

    @Value("${queue_timeout_s}")
    private int QUEUE_TIMEOUT_S;

    // Public Methods

    /**
     * Queues the user for a game. It adds the user to the queue and waits until the
     * user is dequeued or the game is found. It returns the queue response for the
     * user. It is used by the controller to handle the queue http request.
     * 
     * @param user The user to be queued.
     * @return The queue response for the user.
     */
    public QueueResponse queue(User user) {
        CompletableFuture<Void> finished = new CompletableFuture<Void>();
        QueueItem queueItem = new QueueItem(user, finished, 0, 0, null, null);

        if (QueueSingleton.getInstance().isInQueue(user)) {
            QueueSingleton.getInstance().dequeue(user);
        }
        QueueSingleton.getInstance().queue(queueItem);

        try {
            queueItem.finished.get(QUEUE_TIMEOUT_S, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            QueueSingleton.getInstance().dequeue(user);
            return new QueueResponse(false, 0, 0, null, null, "Queue timeout!");
        } catch (Exception e) {
            ErrorHandler.message(e);
            return new QueueResponse(false, 0, 0, null, null, "Error while waiting for game!");
        }

        return new QueueResponse(true, queueItem.port, queueItem.number, queueItem.key, queueItem.iv, "Game found!");
    }

    /**
     * Removes the user from the queue. It is used by the controller to handle the
     * dequeue http request. It returns the queue response for the user. The queue
     * response status will always be false.
     * 
     * @param user The user to be dequeued.
     * @return The queue response for the user.
     */
    public QueueResponse dequeue(User user) {
        QueueSingleton.getInstance().dequeue(user);
        return new QueueResponse(false, 0, 0, null, null, "User removed from queue!");
    }
}
