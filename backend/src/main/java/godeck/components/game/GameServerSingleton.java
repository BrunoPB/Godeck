package godeck.components.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import godeck.models.Port;
import godeck.models.User;
import godeck.models.UserNumberAndPort;

@Component
public class GameServerSingleton {
    private static GameServerSingleton instance = null;
    private Set<GameInstance> threads = new HashSet<GameInstance>();
    private List<Port> ports = new ArrayList<Port>();

    private GameServerSingleton() {
        for (int i = 5555; i < 5565; i++) {
            ports.add(new Port(i));
        }
    }

    public static GameServerSingleton getInstance() {
        if (instance == null) {
            instance = new GameServerSingleton();
        }
        return instance;
    }

    private int findAvailablePort() {
        List<Port> portsCopy = new ArrayList<Port>(this.ports);
        for (Port p : portsCopy) {
            if (p.availbility) {
                return p.port;
            }
        }
        throw new IllegalStateException("No available port.");
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

    public void setPortAvailbility(int port, boolean availbility) {
        for (Port p : ports) {
            if (p.port == port) {
                p.availbility = availbility;
                return;
            }
        }
    }

    public boolean hasAvailablePort() {
        List<Port> portsCopy = new ArrayList<Port>(this.ports);
        for (Port p : portsCopy) {
            if (p.availbility) {
                return true;
            }
        }
        return false;
    }
}
