package godeck.components.game;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import godeck.models.User;
import godeck.models.UserNumberAndPort;

@Component
public class GameServerSingleton {
    private static GameServerSingleton instance = null;
    private Set<GameInstance> threads = new HashSet<GameInstance>();

    private GameServerSingleton() {
    }

    public static GameServerSingleton getInstance() {
        if (instance == null) {
            instance = new GameServerSingleton();
        }
        return instance;
    }

    private int findAvailablePort() {
        return 5555; // TODO: Implement this
    }

    public void startNewGame(User user0, User user1) {
        if (user0 == null || user1 == null) {
            throw new IllegalArgumentException("User can not be null.");
        }

        int port = findAvailablePort();

        GameInstance gameInstance = new GameInstance();
        threads.add(gameInstance);
        gameInstance.setupGame(user0, user1, port);
        gameInstance.start();
    }

    public UserNumberAndPort getUserNumberAndPort(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        for (GameInstance t : threads) {
            // TEST TODO: How to clean threads?
            // if (!t.isAlive()) {
            // threads.remove(t);
            // continue;
            // }
            UserNumberAndPort userNumberAndPort = t.getUserNumberAndPort(user);
            if (userNumberAndPort != null) {
                return userNumberAndPort;
            }
        }
        return null;
    }
}
