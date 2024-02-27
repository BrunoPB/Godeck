package godeck.components.queue;

import java.util.List;

import org.springframework.stereotype.Component;

import godeck.components.game.GameServerSingleton;
import godeck.models.User;

@Component
public class QueueSystem extends Thread {
    public QueueSystem() {

    }

    public void run() {
        while (true) {
            if (QueueSingleton.getInstance().getQueueSize() >= 2 && GameServerSingleton.getInstance().hasAvailablePort()){
                List<User> users = QueueSingleton.getInstance().getNFirstUsers(2);
                GameServerSingleton.getInstance().startNewGame(users.get(0), users.get(1));
                QueueSingleton.getInstance().dequeue(users.get(1));
                QueueSingleton.getInstance().dequeue(users.get(0));
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
