package godeck.models;

import org.springframework.stereotype.Component;

@Component
public class QueueResponse extends Object {
    private boolean status;
    private int port;
    private int userNumber;
    private String message;

    public QueueResponse() {
    }

    public QueueResponse(boolean status, int port, int userNumber, String message) {
        this.status = status;
        this.port = port;
        this.userNumber = userNumber;
        this.message = message;
    }

    public boolean getStatus() {
        return this.status;
    }

    public int getPort() {
        return this.port;
    }

    public int getUserNumber() {
        return this.userNumber;
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

    public void setUserNumber(int userNumber) {
        this.userNumber = userNumber;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
