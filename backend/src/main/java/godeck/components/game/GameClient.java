package godeck.components.game;

import java.io.DataInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import godeck.models.Coordinates;
import godeck.models.GameMove;

@Component
public class GameClient extends Thread {
    private int number;
    private DataInputStream in;
    private GameInstance gameInstance;
    private boolean gameover;

    public GameClient() {
    }

    private String preProcessMessage(String msg) {
        Pattern regex = Pattern.compile("[a-zA-Z0-9]+"); // TODO: Update regex when GameMove is implemented

        Matcher matcher = regex.matcher(msg);
        if (matcher.find()) {
            String result = matcher.group();
            return result;
        }

        throw new IllegalArgumentException("Unknown message from Client.");
    }

    private void sendMove(String msg) { // TODO: Implement this
        // GameMove move = new GameMove(msg);
        // gameInstance.tryMove(number, move);
        System.out.println("msg.equals(\"over\") => " + msg.equals("over"));
        if (msg.equals("over")) {
            gameInstance.looser = number;
            gameInstance.test_gameover();
        } else {
            gameInstance.tryMove(number, msg);
        }
    }

    public void setupGameClient(int number, GameInstance gameInstance, DataInputStream in) {
        this.gameover = false;
        this.number = number;
        this.gameInstance = gameInstance;
        this.in = in;
    }

    public void run() {
        while (!gameover) {
            String msg = "";
            byte byteChar = 0;
            char charChar = 0;
            try {
                while (true) {
                    byteChar = in.readByte();
                    charChar = (char) byteChar;
                    if (charChar == '\n' || gameover) {
                        break;
                    }
                    msg += charChar;
                }
                sendMove(preProcessMessage(msg));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void endClient() {
        gameover = true;
        try {
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
