package godeck.game;

import java.io.DataInputStream;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import godeck.models.GodeckThread;
import godeck.models.client.ClientGameMove;
import godeck.security.AESCryptography;
import godeck.utils.ErrorHandler;
import godeck.utils.JSON;
import godeck.utils.Printer;
import godeck.utils.ThreadUtils;
import lombok.NoArgsConstructor;

/**
 * Represents a client in a game. Receives messages from the server and sends
 * moves to the server.
 * 
 * @author Bruno Pena Baeta
 */
@Component
@NoArgsConstructor
public class GameClient extends GodeckThread {
    // Properties

    private int number;
    private DataInputStream in;
    private GameInstance gameInstance;
    private AESCryptography crypt;
    private boolean setted;
    public CompletableFuture<Void> ready;

    // Constructors

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
            Printer.printDebug("Client Message: \"" + msg + "\"");
            if (setted) {
                decodeMessage(preProcessMessage(msg));
            } else {
                gameInstance.checkClientReady(number, preProcessMessage(msg));
                ready.join();
            }
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
     * @throws Exception If the message is unknown.
     */
    private String preProcessMessage(String msg) throws Exception {
        // String fixedMessage = fixSpecialCharactersBugFromGodot(msg);
        String decryptedMessage = crypt.decrypt(msg);
        Pattern regex = Pattern.compile("[a-zA-Z0-9]+[:].*$");
        Matcher matcher = regex.matcher(decryptedMessage);
        if (matcher.find()) {
            String result = matcher.group();
            return result;
        }
        throw new IllegalArgumentException("Unknown message from Client. Message: " + msg);
    }

    /**
     * This method is used to decode a message from Godot that has a bug with
     * special characters. This method can also be found in Godot client. Once
     * Godot fixes this bug, this method can be removed.
     * Issue #61756
     * 
     * @param m The message to be fixed.
     * @return The fixed message.
     */
    // TODO: Remove this when encryption is implemented
    // private String fixSpecialCharactersBugFromGodot(String m) {
    // return java.net.URLDecoder.decode(m,
    // java.nio.charset.StandardCharsets.UTF_8);
    // }

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
        if (command.equals("GameMove")) {
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
        ClientGameMove move = (ClientGameMove) JSON.construct(msg, ClientGameMove.class);
        gameInstance.tryMove(number, move);
    }

    // Public Methods

    /**
     * Sets up the game client with the number, game instance and input stream.
     * 
     * @param number       The player number.
     * @param gameInstance The game instance.
     * @param in           The data input stream from the game instance.
     * @param crypt        The cryptography object.
     */
    public void setupGameClient(int number, GameInstance gameInstance, DataInputStream in, AESCryptography crypt) {
        this.number = number;
        this.gameInstance = gameInstance;
        this.in = in;
        this.crypt = crypt;
        this.setted = false;
        ready = new CompletableFuture<Void>();
        ready.thenAccept((p) -> {
            set();
        });
    }

    public void set() {
        setted = true;
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
