package godeck.game;

import java.io.IOException;
import java.net.ServerSocket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import godeck.models.QueueItem;
import godeck.security.AESCryptography;

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
    @Value("${turn_timeout_s}")
    private static int turnTimeout;
    private static GameServerSingleton instance = null;
    private Set<GameInstance> threads = new HashSet<GameInstance>();
    private ArrayList<Integer> ports = new ArrayList<Integer>();

    // Constructors

    /**
     * Main constructor. Should never be called, this is a singleton. Uses Autowire
     * to inject the min and max TCP ports.
     * 
     * @param min     The minimum TCP port.
     * @param max     The maximum TCP port.
     * @param timeout The turn timeout.
     */
    @Autowired
    private GameServerSingleton(@Value("${min_tcp_port}") int min,
            @Value("${max_tcp_port}") int max, @Value("${turn_timeout_s}") int timeout) {
        minTCPPort = min;
        maxTCPPort = max;
        turnTimeout = timeout;
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
            instance = new GameServerSingleton(minTCPPort, maxTCPPort, turnTimeout);
        }
        return instance;
    }

    // Private Methods

    /**
     * Sets up the ports for the game server. It creates a list of ports from the
     * minimum to the maximum TCP port.
     */
    private void setupPorts() {
        ports = new ArrayList<Integer>((maxTCPPort - minTCPPort) + 1);
        for (int i = minTCPPort; i <= maxTCPPort; i++) {
            ports.add(i);
        }
    }

    /**
     * Checks if a port is available. It tries to bind to the port and returns true
     * if it is available.
     * 
     * @param port The port to check.
     * @return True if the port is available. False otherwise.
     */
    private synchronized boolean isPortAvailable(int port) {
        try {
            new ServerSocket(port).close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Finds the next available port for a new game instance. It iterates through
     * the list of ports and returns the first available one.
     * 
     * @return The available port.
     * @throws IllegalStateException If there is no available port.
     */
    private synchronized int findAvailablePort() throws IllegalStateException {
        for (int port : ports) {
            if (isPortAvailable(port)) {
                return port;
            }
        }
        throw new IllegalStateException("No available port.");
    }

    // Public Methods

    /**
     * Checks if there is an available port for a new game instance.
     * 
     * @return True if there is an available port. False otherwise.
     */
    public synchronized boolean hasAvailablePort() {
        try {
            findAvailablePort();
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    /**
     * Starts a new game instance. It creates a new game instance and starts it. It
     * also adds the game instance to the list of threads.
     * 
     * @param item0 User number 0.
     * @param item0 User number 1.
     * @throws IllegalStateException    If there is no available port.
     * @throws NoSuchAlgorithmException If the AES criptography fails. This should
     *                                  never happen.
     */
    public synchronized void startNewGame(QueueItem item0, QueueItem item1)
            throws IllegalStateException, NoSuchAlgorithmException {
        AESCryptography crypt0 = new AESCryptography(16);
        AESCryptography crypt1 = new AESCryptography(16);
        String key0 = crypt0.getKeyString();
        String iv0 = crypt0.getIVString();
        String key1 = crypt1.getKeyString();
        String iv1 = crypt1.getIVString();

        System.out.println("BASE64: " + key0);
        byte[] key0Bytes = crypt0.getKey().getEncoded();
        System.out.print("[");
        for (byte b : key0Bytes) {
            System.out.print((int) b + ", ");
        }
        System.out.println("]");

        int port = findAvailablePort();

        GameInstance gameInstance = new GameInstance();
        threads.add(gameInstance);
        gameInstance.setupGame(item0.user, item1.user, port, crypt0, crypt1, turnTimeout);
        gameInstance.start();

        item0.setQueueItem(port, key0, iv0);
        item1.setQueueItem(port, key1, iv1);
        item0.finished.complete(null);
        item1.finished.complete(null);
    }
}
