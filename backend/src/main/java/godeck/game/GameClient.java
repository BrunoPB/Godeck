package godeck.game;

import java.io.DataInputStream;
import java.util.Base64;
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
    private boolean readyFlag;
    public CompletableFuture<Void> ready;
    private boolean receivedNumberFlag;
    public CompletableFuture<Integer> receivedNumber;

    // Private Methods

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
            dealWithMessage(msg);
        }
        ThreadUtils.sleep(10);
    }

    /**
     * Deals with the message from the client. If it is the initial number message,
     * sets the number. If it is the ready message, sets the ready flag. If it is a
     * move message, sends the move to the game instance.
     * 
     * @param msg The message from the client.
     * @throws Exception If the message could not be processed.
     */
    private void dealWithMessage(String msg) throws Exception {
        if (!receivedNumberFlag) {
            try {
                msg = processBase64(msg);
                receivedNumber.complete(Integer.parseInt(msg));
                receivedNumberFlag = true;
            } catch (Exception e) {
                receivedNumber.completeExceptionally(e);
            }
        } else if (!readyFlag) {
            try {
                msg = crypt.decrypt(msg);
                if (msg.equals("TheClientIsReady")) {
                    ready.complete(null);
                    readyFlag = true;
                } else {
                    ready.completeExceptionally(null);
                }
            } catch (Exception e) {
                ready.completeExceptionally(e);
            }
        } else {
            decodeMessage(preProcessMessage(msg));
        }
    }

    /**
     * Processes the Base64 string from the client. Decodes the Base64 string and
     * returns the decoded string.
     * 
     * @param base64String The Base64 string to be processed.
     * @return The decoded string.
     * @throws IllegalArgumentException If the Base64 string is unknown.
     */
    private String processBase64(String base64String) throws IllegalArgumentException {
        Pattern regex = Pattern.compile("[A-Za-z0-9/+=]+");
        Matcher matcher = regex.matcher(base64String);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Unknown Base64 from Client. Message: " + base64String);
        }
        return new String(Base64.getDecoder().decode(matcher.group()));
    }

    /**
     * Pre-processes the message from the client. Decodes the message from Base64
     * and extracts the command and parameter.
     * 
     * @param msg The message from the client.
     * @return The message after pre-processing.
     * @throws Exception If the message is unknown.
     */
    private String preProcessMessage(String msg) throws Exception {
        String decodedMsg = processBase64(msg);
        Pattern regex = Pattern.compile("[a-zA-Z0-9]+[:].*$");
        Matcher matcher = regex.matcher(decodedMsg);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Unknown message from Client. Message: " + decodedMsg);
        }
        return matcher.group();
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
        this.readyFlag = false;
        this.receivedNumberFlag = false;
        ready = new CompletableFuture<Void>();
        ready.thenAccept((p) -> {
            readyFlag = true;
        });
        receivedNumber = new CompletableFuture<Integer>();
        receivedNumber.thenAccept((p) -> {
            receivedNumberFlag = true;
        });
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
