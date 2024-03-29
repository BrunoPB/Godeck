package godeck.queue;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import godeck.models.QueueItem;
import godeck.models.entities.User;
import lombok.NoArgsConstructor;

/**
 * Singleton class that manages the queue of users waiting for a game. It is
 * responsible for adding and removing users from the queue and checking if a
 * game can be started.
 * 
 * Is a singleton. Can be accessed from anywhere in the application.
 * 
 * @author Bruno Pena Baeta
 */
@Component
@NoArgsConstructor
public class QueueSingleton {
    // Properties

    private static QueueSingleton instance = null;
    private static List<QueueItem> queue = new LinkedList<QueueItem>();

    // Constructors

    /**
     * Gets the instance of the queue singleton. If the instance does not exist, it
     * creates a new one.
     * 
     * @return The instance of the queue singleton.
     */
    public static QueueSingleton getInstance() {
        if (instance == null) {
            instance = new QueueSingleton();
        }
        return instance;
    }

    // Methods

    /**
     * Adds a user to the queue.
     * 
     * @param qi The queue item that represents the user to be added to the queue.
     * @return True if the user was added to the queue, false otherwise.
     */
    public synchronized boolean queue(QueueItem qi) {
        return queue.add(qi);
    }

    /**
     * Removes a user from the queue.
     * 
     * @param user The user to be removed from the queue.
     * @return True if the user was removed from the queue, false otherwise.
     */
    public synchronized boolean dequeue(User user) {
        List<QueueItem> queueCopy = new LinkedList<QueueItem>(queue);
        for (QueueItem qi : queueCopy) {
            if (qi.user.equals(user)) {
                return queue.remove(qi);
            }
        }
        return false;
    }

    /**
     * Checks if a user is in the queue.
     * 
     * @param user The user to be checked.
     * @return True if the user is in the queue, false otherwise.
     */
    public boolean isInQueue(User user) {
        List<QueueItem> queueCopy = new LinkedList<QueueItem>(queue);
        for (QueueItem qi : queueCopy) {
            if (qi.user.equals(user)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the size of the queue.
     * 
     * @return The size of the queue.
     */
    public int getQueueSize() {
        List<QueueItem> queueCopy = new LinkedList<QueueItem>(queue);
        return queueCopy.size();
    }

    /**
     * Gets the first n items in the queue.
     * 
     * @param n The number of items to be returned.
     * @return The first n items in the queue.
     * @throws IllegalArgumentException If there are not enough items in the queue.
     */
    public List<QueueItem> getNFirstItems(int n) throws IllegalArgumentException {
        List<QueueItem> queueCopy = new LinkedList<QueueItem>(queue);
        if (n > queueCopy.size()) {
            throw new IllegalArgumentException("Not enough items in the queue.");
        }
        return queueCopy.subList(0, n);
    }

    /**
     * Gets a copy of the queue. This method is used to avoid the queue being
     * modified from outside the class.
     * 
     * @return A copy of the queue.
     */
    public List<QueueItem> getQueue() {
        List<QueueItem> queueCopy = new LinkedList<QueueItem>(queue);
        return queueCopy;
    }
}
