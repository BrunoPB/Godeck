package godeck.models;

import org.springframework.stereotype.Component;

@Component
public class Port {
    public int port;
    public boolean availbility;

    public Port() {
    }

    public Port(int port) {
        this.port = port;
        this.availbility = true;
    }
}
