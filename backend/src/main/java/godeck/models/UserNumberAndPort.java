package godeck.models;

import org.springframework.stereotype.Component;

@Component
public class UserNumberAndPort {
    public int port;
    public int number;

    public UserNumberAndPort() {
    }

    public UserNumberAndPort(int port, int number) {
        this.port = port;
        this.number = number;
    }
}
