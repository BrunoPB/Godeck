package godeck.game;

import java.io.DataInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

/**
 * Represents a client in a game. Receives messages from the server and sends
 * moves to the server.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class GameClient extends Thread {
    // Properties

    private int number;
    private DataInputStream in;
    private GameInstance gameInstance;
    private boolean exit;

    // Constructors

    /**
     * Creates a new game client.
     */
    public GameClient() {
    }

    /**
     * Waits for messages from the client and decodes them.
     */
    private void getMessagesFromClient() {
        try {
            while (!exit) {
                String msg = "";
                byte byteChar = 0;
                char charChar = 0;
                if (in.available() > 0) {
                    while (!exit) {
                        byteChar = in.readByte();
                        charChar = (char) byteChar;
                        if (charChar == '\n') {
                            break;
                        }
                        msg += charChar;
                    }
                    decodeMessage(preProcessMessage(msg));
                }
                Thread.sleep(10);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    // Private Methods

    /**
     * Pre-processes the message from the client. Uses a regex to extract the
     * command and parameter from the message.
     * 
     * @param msg The message from the client.
     * @return The message after pre-processing.
     */
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

    /**
     * Decodes the message from the client and executes the corresponding command
     * with the parameter.
     * 
     * @param msg The pre-processed message from the client.
     * @throws Exception If the command is unknown.
     */
    private void decodeMessage(String msg) throws Exception {
        String command = msg.split(":")[0];
        String parameter = msg.split(":")[1];
        if (command.equals("Ready")) {
            gameInstance.prepareClient(number);
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

    /**
     * Processes the move from a string and sends it to the game instance.
     * 
     * @param msg The move string to be sent.
     */
    private void sendMove(String msg) { // TODO: Implement this
        // GameMove move = new GameMove(msg);
        // gameInstance.tryMove(number, move);
        gameInstance.tryMove(number, msg);
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
        this.exit = false;
    }

    /**
     * Runs the game client. Waits for messages from the client, decodes them and
     * executes the corresponding command.
     */
    public void run() {
        getMessagesFromClient();
    }

    /**
     * Kills the game client. Stops the thread.
     */
    public void kill() {
        exit = true;
    }
}
