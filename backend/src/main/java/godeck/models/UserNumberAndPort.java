package godeck.models;

import org.springframework.stereotype.Component;

/**
 * Represents a user number and a port number.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class UserNumberAndPort {
    // Properties

    public int port;
    public int number;

    // Constructors

    /**
     * Default constructor.
     */
    public UserNumberAndPort() {
    }

    /**
     * Main constructor.
     * 
     * @param port   The port number.
     * @param number The user number.
     */
    public UserNumberAndPort(int port, int number) {
        this.port = port;
        this.number = number;
    }
}
