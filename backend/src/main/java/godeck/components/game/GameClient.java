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

    public GameClient() {
    }

    private String preProcessMessage(String msg) {
        Pattern regex = Pattern.compile("[a-zA-Z0-9]+[:][a-zA-Z0-9 ]+"); // TODO: Update regex when GameMove is
                                                                         // implemented

        Matcher matcher = regex.matcher(msg);
        if (matcher.find()) {
            String result = matcher.group();
            return result;
        }

        throw new IllegalArgumentException("Unknown message from Client.");
    }

    private void decodeMessage(String msg) {
        String command = msg.split(":")[0];
        String parameter = msg.split(":")[1];

        if (command.equals("Ready")) {
        } else if (command.equals("GameMove")) {
            sendMove(parameter);
        } else if (command.equals("Lose")) {
            gameInstance.declareSurrender(number);
        } else if (command.equals("DebugTest")) {
            System.out.println("DebugTest: \"" + parameter + "\"");
        } else {
            throw new IllegalArgumentException("Unknown command from Client.");
        }
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
                decodeMessage(preProcessMessage(msg));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
