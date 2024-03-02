package godeck.components.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import godeck.models.Port;
import godeck.models.User;
import godeck.models.UserNumberAndPort;

@Component
public class GameServerSingleton {
    @Value("${min_tcp_port}")
    private static int minTCPPort;
    @Value("${max_tcp_port}")
    private static int maxTCPPort;
    private static GameServerSingleton instance = null;
    private Set<GameInstance> threads = new HashSet<GameInstance>();
    private List<Port> ports = new ArrayList<Port>();

    @Autowired
    private GameServerSingleton(@Value("${min_tcp_port}") int min,
            @Value("${max_tcp_port}") int max) {
        minTCPPort = min;
        maxTCPPort = max;
        setupPorts();
    }

    private void setupPorts() {
        for (int i = minTCPPort; i <= maxTCPPort; i++) {
            ports.add(new Port(i));
        }
    }

    public static GameServerSingleton getInstance() {
        if (instance == null) {
            instance = new GameServerSingleton(minTCPPort, maxTCPPort);
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

    public UserNumberAndPort getUserNumberAndPort(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        for (GameInstance t : threads) {
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
}
