package godeck.queue;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import godeck.models.User;

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
public class QueueSingleton {
    // Properties

    private static QueueSingleton instance = null;
    private static List<User> usersQueue = new LinkedList<User>();

    // Constructors

    /**
     * Main constructor. Should never be called, this is a singleton.
     */
    private QueueSingleton() {
    }

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
     * @param user The user to be added to the queue.
     * @return True if the user was added to the queue, false otherwise.
     */
    public boolean queue(User user) {
        return usersQueue.add(user);
    }

    /**
     * Removes a user from the queue.
     * 
     * @param user The user to be removed from the queue.
     * @return True if the user was removed from the queue, false otherwise.
     */
    public boolean dequeue(User user) {
        return usersQueue.remove(user);
    }

    /**
     * Checks if a user is in the queue.
     * 
     * @param user The user to be checked.
     * @return True if the user is in the queue, false otherwise.
     */
    public boolean isInQueue(User user) {
        List<User> usersQueueCopy = new LinkedList<User>(usersQueue);
        return usersQueueCopy.contains(user);
    }

    /**
     * Gets the size of the queue.
     * 
     * @return The size of the queue.
     */
    public int getQueueSize() {
        List<User> usersQueueCopy = new LinkedList<User>(usersQueue);
        return usersQueueCopy.size();
    }

    /**
     * Gets the first n users in the queue.
     * 
     * @param n The number of users to be returned.
     * @return The first n users in the queue.
     * @throws IllegalArgumentException If there are not enough users in the queue.
     */
    public List<User> getNFirstUsers(int n) throws IllegalArgumentException {
        List<User> usersQueueCopy = new LinkedList<User>(usersQueue);
        if (n > usersQueueCopy.size()) {
            throw new IllegalArgumentException("Not enough users in the queue.");
        }
        return usersQueueCopy.subList(0, n);
    }

    /**
     * Gets a copy of the users queue. This method is used to avoid the queue being
     * modified from outside the class.
     * 
     * @return A copy of the users queue.
     */
    public List<User> getQueue() {
        List<User> usersQueueCopy = new LinkedList<User>(usersQueue);
        return usersQueueCopy;
    }
}
