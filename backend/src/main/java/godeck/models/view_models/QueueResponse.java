package godeck.models.view_models;

import org.springframework.stereotype.Component;

/**
 * Represents a response from the queue service. It is used to store the status
 * of the request, the port number and a message.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class QueueResponse extends Object {
    // Properties

    private boolean status;
    private int port;
    private String message;

    // Constructors

    /**
     * Default constructor.
     */
    public QueueResponse() {
    }

    /**
     * Main constructor.
     * 
     * @param status  The status of the request. True if it was successful, false
     *                otherwise.
     * @param port    The port number. 0 if the request was not successful.
     * @param message The message of the request. It can be an error message or a
     *                success message.
     */
    public QueueResponse(boolean status, int port, String message) {
        this.status = status;
        this.port = port;
        this.message = message;
    }

    // Getters and Setters

    public boolean getStatus() {
        return this.status;
    }

    public int getPort() {
        return this.port;
    }

    public String getMessage() {
        return this.message;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
