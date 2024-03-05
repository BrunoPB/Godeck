package godeck.components.queue;

import java.util.List;

import org.springframework.stereotype.Component;

import godeck.components.game.GameServerSingleton;
import godeck.models.User;

/**
 * Thread that manages the queue of users waiting for a game. It is responsible
 * for checking if a game can be started and starting it.
 * 
 * Is a thread. Can be started and stopped.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class QueueSystem extends Thread {
    // Properties
    private boolean exit = false;

    // Constructors

    public QueueSystem() {
    }

    // Private Methods

    private boolean hasGameToStart() {
        return QueueSingleton.getInstance().getQueueSize() >= 2
                && GameServerSingleton.getInstance().hasAvailablePort();
    }

    private void startGame() {
        List<User> users = QueueSingleton.getInstance().getNFirstUsers(2);
        GameServerSingleton.getInstance().startNewGame(users.get(0), users.get(1));
        QueueSingleton.getInstance().dequeue(users.get(1));
        QueueSingleton.getInstance().dequeue(users.get(0));
    }

    // Public Methods

    public void run() {
        while (!exit) {
            if (hasGameToStart()) {
                startGame();
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void kill() {
        exit = true;
    }
}
