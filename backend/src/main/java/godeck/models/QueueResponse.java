package godeck.models;

import org.springframework.stereotype.Component;

@Component
public class QueueResponse extends Object {
    private boolean status;
    private int port;
    private String message;

    public QueueResponse() {
    }

    public QueueResponse(boolean status, int port, String message) {
        this.status = status;
        this.port = port;
        this.message = message;
    }

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
