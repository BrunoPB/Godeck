package godeck.queue;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.stereotype.Component;

import godeck.game.GameServerSingleton;
import godeck.models.GodeckThread;
import godeck.models.QueueItem;
import godeck.utils.ErrorHandler;
import godeck.utils.ThreadUtils;
import lombok.NoArgsConstructor;

/**
 * Thread that manages the queue of users waiting for a game. It is responsible
 * for checking if a game can be started and starting it.
 * 
 * Is a thread. Can be started and stopped.
 * 
 * @author Bruno Pena Baeta
 */
@Component
@NoArgsConstructor
public class QueueSystem extends GodeckThread {

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
     * @throws IllegalArgumentException If there are not enough users in the queue
     *                                  to start a game.
     */
    private void startGame() throws IllegalArgumentException {
        List<QueueItem> items = QueueSingleton.getInstance().getNFirstItems(2);
        try {
            GameServerSingleton.getInstance().startNewGame(items.get(0), items.get(1));
        } catch (IllegalStateException | NoSuchAlgorithmException e) {
            ErrorHandler.message(e);
            items.get(0).finished.completeExceptionally(e);
            items.get(1).finished.completeExceptionally(e);
        }
        QueueSingleton.getInstance().dequeue(items.get(1).user);
        QueueSingleton.getInstance().dequeue(items.get(0).user);
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
        while (!super.exit) {
            if (hasGameToStart()) {
                try {
                    startGame();
                } catch (IllegalArgumentException e) {
                    ErrorHandler.message(e);
                }
            }
            ThreadUtils.sleep(10);
        }
    }
}
