package godeck.game;

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

/**
 * Singleton class that manages the game server. It is responsible for creating
 * new game instances and managing the ports.
 * 
 * Is a singleton. Can be accessed from anywhere in the application.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class GameServerSingleton {
    // Properties

    @Value("${min_tcp_port}")
    private static int minTCPPort;
    @Value("${max_tcp_port}")
    private static int maxTCPPort;
    private static GameServerSingleton instance = null;
    private Set<GameInstance> threads = new HashSet<GameInstance>();
    private List<Port> ports = new ArrayList<Port>();

    // Constructors

    /**
     * Main constructor. Should never be called, this is a singleton. Uses Autowire
     * to inject the min and max TCP ports.
     * 
     * @param min The minimum TCP port.
     * @param max The maximum TCP port.
     */
    @Autowired
    private GameServerSingleton(@Value("${min_tcp_port}") int min,
            @Value("${max_tcp_port}") int max) {
        minTCPPort = min;
        maxTCPPort = max;
        setupPorts();
    }

    /**
     * Returns the instance of the game server singleton. If the instance does not
     * exist, it creates a new one.
     * 
     * @return The instance of the game server singleton.
     */
    public static GameServerSingleton getInstance() {
        if (instance == null) {
            instance = new GameServerSingleton(minTCPPort, maxTCPPort);
        }
        return instance;
    }

    // Private Methods

    /**
     * Sets up the ports for the game server. It creates a list of ports from the
     * minimum to the maximum TCP port.
     */
    private void setupPorts() {
        for (int i = minTCPPort; i <= maxTCPPort; i++) {
            ports.add(new Port(i));
        }
    }

    /**
     * Finds the next available port for a new game instance. It iterates through
     * the list of ports and returns the first available one.
     * 
     * @return The available port.
     * @throws IllegalStateException If there is no available port.
     */
    private int findAvailablePort() throws IllegalStateException {
        List<Port> portsCopy = new ArrayList<Port>(this.ports);
        for (Port p : portsCopy) {
            if (p.availbility) {
                return p.port;
            }
        }
        throw new IllegalStateException("No available port.");
    }

    // Public Methods

    /**
     * Returns the user number and port for a given user. It iterates through the
     * list of game instances and returns the user number and port for the given
     * user.
     * 
     * @param user The user to get the number and port.
     * @return The user number and port for the given user. Null if the user is not
     *         in any game.
     */
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

    /**
     * Sets the availability of a port. It iterates through the list of ports and
     * sets the availability of the given port.
     * 
     * @param port        The port to set the availability.
     * @param availbility The availability of the port.
     */
    public void setPortAvailbility(int port, boolean availbility) {
        for (Port p : ports) {
            if (p.port == port) {
                p.availbility = availbility;
                return;
            }
        }
    }

    /**
     * Checks if there is an available port for a new game instance.
     * 
     * @return True if there is an available port. False otherwise.
     */
    public boolean hasAvailablePort() {
        List<Port> portsCopy = new ArrayList<Port>(this.ports);
        for (Port p : portsCopy) {
            if (p.availbility) {
                return true;
            }
        }
        return false;
    }

    /**
     * Starts a new game instance. It creates a new game instance and starts it. It
     * also adds the game instance to the list of threads.
     * 
     * @param user0 User number 0.
     * @param user1 User number 1.
     * @throws IllegalArgumentException If any user is null.
     */
    public void startNewGame(User user0, User user1) throws IllegalArgumentException {
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
