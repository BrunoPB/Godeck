package godeck.queue;

import java.util.List;

import org.springframework.stereotype.Component;

import godeck.game.GameServerSingleton;
import godeck.models.User;
import godeck.utils.ErrorHandler;
import godeck.utils.ThreadUtils;

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

    /**
     * Main constructor. Should never be called, this is a thread.
     */
    public QueueSystem() {
    }

    // Private Methods

    /**
     * Checks if there are enough users in the queue to start a game and if there
     * are available ports to start the game. If both conditions are met, returns
     * true, otherwise returns false.
     * 
     * @return True if there are enough users in the queue to start a game and there
     *         are available ports to start the game, false otherwise.
     */
    private boolean hasGameToStart() {
        return QueueSingleton.getInstance().getQueueSize() >= 2
                && GameServerSingleton.getInstance().hasAvailablePort();
    }

    /**
     * Starts a new game with the first two users in the queue. If the game is
     * started, removes the users from the queue.
     * 
     * @throws IllegalStateException If there are not enough users in the queue to
     *                               start a game or there are no available ports to
     *                               start the game.
     */
    private void startGame() throws IllegalStateException {
        List<User> users = QueueSingleton.getInstance().getNFirstUsers(2);
        GameServerSingleton.getInstance().startNewGame(users.get(0), users.get(1));
        QueueSingleton.getInstance().dequeue(users.get(1));
        QueueSingleton.getInstance().dequeue(users.get(0));
    }

    // Public Methods

    /**
     * Starts the thread. It will keep checking if there are enough users in the
     * queue to start a game and if there are available ports to start the game.
     * When both conditions are met, it will start a new game. It will keep running
     * until the kill method is called.
     */
    @Override
    public void run() {
        while (!exit) {
            if (hasGameToStart()) {
                try {
                    startGame();
                } catch (IllegalStateException e) {
                    ErrorHandler.message(e);
                }
            }
            ThreadUtils.sleep(10);
        }
    }

    /**
     * Kills the thread. It will stop running the run method.
     */
    public void kill() {
        exit = true;
    }
}
