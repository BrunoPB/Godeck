package godeck.components.game;

import java.io.DataInputStream;

import org.springframework.stereotype.Component;

import godeck.models.Coordinates;
import godeck.models.GameMove;

@Component
public class GameClient extends Thread {
    private int number;
    private DataInputStream in;
    private GameInstance gameInstance;

    public GameClient() {

    }

    private void sendMove(String msg) { // TODO: Implement this
        // GameMove move = new GameMove(msg);
        // gameInstance.tryMove(number, move);
        gameInstance.tryMove(number, msg);
    }

    public void setupGameClient(int number, GameInstance gameInstance, DataInputStream in) {
        this.number = number;
        this.gameInstance = gameInstance;
        this.in = in;
    }

    public void run() {
        while (true) {
            String msg = "";
            byte byteChar = 0;
            char charChar = 0;
            try {
                while (true) {
                    byteChar = in.readByte();
                    charChar = (char) byteChar;
                    if (charChar == '\n') {
                        break;
                    }
                    msg += charChar;
                }
                sendMove(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
