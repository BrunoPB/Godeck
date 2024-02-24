package godeck.models;

import org.springframework.stereotype.Component;

@Component
public class Coordinates {
    public int x;
    public int y;

    public Coordinates() {
    }

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
