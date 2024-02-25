package godeck.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import godeck.components.game.GameServerSingleton;
import godeck.components.queue.QueueSingleton;
import godeck.models.QueueResponse;
import godeck.models.User;
import godeck.models.UserNumberAndPort;
import godeck.repositories.UserRepository;

@Service
public class QueueService {
    private UserRepository userRepository;

    @Autowired
    public QueueService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public QueueResponse queue(String stringUserId) {
        UUID userId = UUID.fromString(stringUserId);
        if (userId == null) {
            throw new IllegalArgumentException("User id cannot be null.");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found!"));

        QueueSingleton.getInstance().queue(user);

        while (QueueSingleton.getInstance().isInQueue(user)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        UserNumberAndPort userNumberAndPort = GameServerSingleton.getInstance().getUserNumberAndPort(user);
        return new QueueResponse(true, userNumberAndPort.port, "Game found on port " + userNumberAndPort.port + "!");
    }

    public QueueResponse dequeue(String stringUserId) {
        UUID userId = UUID.fromString(stringUserId);
        if (userId == null) {
            throw new IllegalArgumentException("User id cannot be null.");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found!"));
        QueueSingleton.getInstance().dequeue(user);
        return new QueueResponse(false, 0, "User " + user.getId() + "removed from queue!");
    }
}
