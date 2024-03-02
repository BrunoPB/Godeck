package godeck.components.queue;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import godeck.models.User;

@Component
public class QueueSingleton {
    private static QueueSingleton instance = null;
    private static List<User> usersQueue = new LinkedList<User>();

    private QueueSingleton() {
    }

    public static QueueSingleton getInstance() {
        if (instance == null) {
            instance = new QueueSingleton();
        }
        return instance;
    }

    public boolean queue(User user) {
        return usersQueue.add(user);
    }

    public boolean dequeue(User user) {
        return usersQueue.remove(user);
    }

    public boolean isInQueue(User user) {
        List<User> usersQueueCopy = new LinkedList<User>(usersQueue);
        return usersQueueCopy.contains(user);
    }

    public int getQueueSize() {
        List<User> usersQueueCopy = new LinkedList<User>(usersQueue);
        return usersQueueCopy.size();
    }

    public List<User> getNFirstUsers(int n) {
        List<User> usersQueueCopy = new LinkedList<User>(usersQueue);
        return usersQueueCopy.subList(0, n);
    }

    public List<User> getQueue() {
        List<User> usersQueueCopy = new LinkedList<User>(usersQueue);
        return usersQueueCopy;
    }
}
