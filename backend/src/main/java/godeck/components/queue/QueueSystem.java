package godeck.components.queue;

import java.util.List;

import org.springframework.stereotype.Component;

import godeck.components.game.GameServerSingleton;
import godeck.models.User;

@Component
public class QueueSystem extends Thread {
    private boolean exit = false;

    public QueueSystem() {
    }

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

    public void stopQueueSystem() {
        exit = true;
    }

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
}
