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

    private User getUserById(String stringUserId) {
        UUID userId = UUID.fromString(stringUserId);
        if (userId == null) {
            throw new IllegalArgumentException("User id cannot be null.");
        }
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found!"));
    }

    private void waitWhileUserIsInQueue(User user) {
        while (QueueSingleton.getInstance().isInQueue(user)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private QueueResponse getQueueResponseForUser(User user) {
        UserNumberAndPort userNumberAndPort = GameServerSingleton.getInstance().getUserNumberAndPort(user);
        if (userNumberAndPort == null) {
            return new QueueResponse(false, 0, "No game found!");
        }
        return new QueueResponse(true, userNumberAndPort.port, "Game found on port " + userNumberAndPort.port + "!");
    }

    public QueueResponse queue(String stringUserId) {
        User user = getUserById(stringUserId);
        QueueSingleton.getInstance().queue(user);
        waitWhileUserIsInQueue(user);
        return getQueueResponseForUser(user);
    }

    public QueueResponse dequeue(String stringUserId) {
        User user = getUserById(stringUserId);
        QueueSingleton.getInstance().dequeue(user);
        return new QueueResponse(false, 0, "User " + user.getId() + "removed from queue!");
    }
}
