package godeck.game;

import java.io.DataInputStream;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import godeck.models.GameMove;
import godeck.models.GodeckThread;
import godeck.utils.ErrorHandler;
import godeck.utils.Printer;
import godeck.utils.ThreadUtils;

/**
 * Represents a client in a game. Receives messages from the server and sends
 * moves to the server.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class GameClient extends GodeckThread {
    // Properties

    private int number;
    private DataInputStream in;
    private GameInstance gameInstance;
    public CompletableFuture<Boolean> ready = new CompletableFuture<Boolean>();

    // Constructors

    /**
     * Creates a new game client.
     */
    public GameClient() {
    }

    /**
     * Waits for messages from the client and decodes them.
     * 
     * @throws Exception If the message could not be received or decoded.
     */
    private void getMessagesFromClient() throws Exception {
        String msg = "";
        byte byteChar = 0;
        char charChar = 0;
        if (in.available() > 0) {
            while (!super.exit) {
                byteChar = in.readByte();
                charChar = (char) byteChar;
                if (charChar == '\n') {
                    break;
                }
                msg += charChar;
            }
            decodeMessage(preProcessMessage(msg));
        }
        ThreadUtils.sleep(10);
    }

    // Private Methods

    /**
     * Pre-processes the message from the client. Uses a regex to extract the
     * command and parameter from the message.
     * 
     * @param msg The message from the client.
     * @return The message after pre-processing.
     * @throws IllegalArgumentException If the message is unknown.
     */
    private String preProcessMessage(String msg) throws IllegalArgumentException {
        Pattern regex = Pattern.compile("[a-zA-Z0-9]+[:].*$");
        Matcher matcher = regex.matcher(msg);
        if (matcher.find()) {
            String result = matcher.group();
            return result;
        }
        throw new IllegalArgumentException("Unknown message from Client.");
    }

    /**
     * Decodes the message from the client and executes the corresponding command
     * with the parameter.
     * 
     * @param msg The pre-processed message from the client.
     * @throws IllegalArgumentException If the command is unknown.
     */
    private void decodeMessage(String msg) throws IllegalArgumentException {
        int index = msg.indexOf(":");
        String command = msg.substring(0, index);
        String parameter = msg.substring(index + 1);
        if (command.equals("Ready")) {
            ready.complete(Boolean.parseBoolean(parameter));
        } else if (command.equals("GameMove")) {
            sendMove(parameter);
        } else if (command.equals("Lose")) {
            gameInstance.declareSurrender(number);
        } else if (command.equals("DebugTest")) {
            Printer.printDebug("Client Message: \"" + parameter + "\"");
        } else {
            throw new IllegalArgumentException("Unknown command from Client.");
        }
    }

    /**
     * Processes the move from a string and sends it to the game instance.
     * 
     * @param msg The move string to be sent.
     */
    private void sendMove(String msg) {
        GameMove move = new GameMove(msg);
        gameInstance.tryMove(number, move);
    }

    // Public Methods

    /**
     * Sets up the game client with the number, game instance and input stream.
     * 
     * @param number       The player number.
     * @param gameInstance The game instance.
     * @param in           The data input stream from the game instance.
     */
    public void setupGameClient(int number, GameInstance gameInstance, DataInputStream in) {
        this.number = number;
        this.gameInstance = gameInstance;
        this.in = in;
    }

    /**
     * Runs the game client. Waits for messages from the client, decodes them and
     * executes the corresponding command.
     */
    public void run() {
        while (!super.exit) {
            try {
                getMessagesFromClient();
            } catch (Exception e) {
                ErrorHandler.message(e);
            }
        }
    }
}
