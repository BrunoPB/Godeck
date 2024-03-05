package godeck.models;

import org.springframework.stereotype.Component;

/**
 * Represents a port. It is used to store the port number and its availability.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class Port {
    // Properties

    public int port;
    public boolean availbility;

    // Constructors

    /**
     * Default constructor.
     */
    public Port() {
    }

    /**
     * Main constructor.
     * 
     * @param port The port number.
     */
    public Port(int port) {
        this.port = port;
        this.availbility = true;
    }
}
