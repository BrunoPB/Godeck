package godeck.components.game;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.springframework.stereotype.Component;

import godeck.models.Game;
import godeck.models.GameMove;
import godeck.models.User;
import godeck.models.UserNumberAndPort;

/**
 * Represents a game between 2 players. Has game and server information.
 * 
 * Is a thread. Can be started and stopped.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class GameInstance extends Thread {
    // Properties

    private User user0;
    private User user1;
    private int port;
    private Game game;
    private GameClient user0Client;
    private GameClient user1Client;
    private ServerSocket server;
    private DataOutputStream out0;
    private DataOutputStream out1;
    private boolean client1Ready;
    private boolean client2Ready;
    private Socket socket0;
    private Socket socket1;
    private DataInputStream in0;
    private DataInputStream in1;

    // Constructors

    /**
     * Creates a new game instance.
     */
    public GameInstance() {
    }

    // Private Methods

    /**
     * Allocates a port for the game server, connects the clients and initiates data
     * streams.
     * 
     * @throws Exception If the server could not be created or the clients could
     *                   not connect.
     */
    private void setupGameServer() throws Exception {
        GameServerSingleton.getInstance().setPortAvailbility(port, false);
        server = new ServerSocket(port);
        server.setSoTimeout(5000); // 5 seconds timeout
        try {
            socket0 = server.accept();
            socket1 = server.accept();
        } catch (SocketTimeoutException e) {
            if (socket0.isBound()) {
                in0 = new DataInputStream(new BufferedInputStream(socket0.getInputStream()));
                in0.readByte();
                out0 = new DataOutputStream(socket0.getOutputStream());
                out0.writeBytes("Error:Opponent could not connect.\n");
            }
            closeServer();
            throw new Exception("Opponent could not connect.");
        }
        initiateDataStreams();
    }

    /**
     * Initiates the server data input and output streams.
     * 
     * @throws Exception If the data streams could not be initiated.
     */
    private void initiateDataStreams() throws Exception {
        in0 = new DataInputStream(new BufferedInputStream(socket0.getInputStream()));
        in1 = new DataInputStream(new BufferedInputStream(socket1.getInputStream()));
        in0.readByte();
        in1.readByte();
        out0 = new DataOutputStream(socket0.getOutputStream());
        out1 = new DataOutputStream(socket1.getOutputStream());
    }

    /**
     * Starts both game clients, waits for them to be ready and sends their
     * respective numbers.
     * 
     * @throws Exception If the clients could not be started or if they could not be
     *                   synchronized.
     */
    private void setupGameClients() throws Exception {
        user0Client = new GameClient();
        user0Client.setupGameClient(0, this, in0);
        user1Client = new GameClient();
        user1Client.setupGameClient(1, this, in1);
        user0Client.start();
        user1Client.start();
        while (!client1Ready || !client2Ready) {
            Thread.sleep(10);
        }
        sendClientNumber();
    }

    /**
     * Main game loop. Waits for the game to be over.
     * 
     * @throws Exception
     */
    private void gameLoop() throws Exception {
        while (!game.isGameOver()) {
            Thread.sleep(10);
        }
    }

    /**
     * Closes the server and all sockets and data streams.
     * 
     * @throws Exception If the server or any of the sockets or data streams could
     *                   not be closed.
     */
    private void closeServer() throws Exception {
        if (in0 != null)
            in0.close();
        if (in1 != null)
            in1.close();
        if (out0 != null)
            out0.close();
        if (out1 != null)
            out1.close();
        if (socket0 != null)
            socket0.close();
        if (socket1 != null)
            socket1.close();
        if (server != null)
            server.close();
        GameServerSingleton.getInstance().setPortAvailbility(port, true);
    }

    /**
     * Sends the client numbers to the respective clients.
     * 
     * @throws Exception If the client numbers could not be sent.
     */
    private void sendClientNumber() throws Exception {
        out0.writeBytes("UserNumber:0\n");
        out1.writeBytes("UserNumber:1\n");
    }

    /**
     * Synchronizes both clients with the current game state.
     * 
     * @param move The last move executed.
     * @throws Exception If the clients could not be synchronized.
     */
    private void synchronizeClients(String test) throws Exception {
        out0.flush();
        out1.flush();
        out0.writeBytes("DebugTest:" + test + "\n");
        out1.writeBytes("DebugTest:" + test + "\n");
    }

    /**
     * Ends the game and sends the result to both clients.
     * 
     * @throws Exception If the game could not be ended or if the result could not
     *                   be sent.
     */
    private void endGame() throws Exception { // TODO: Implement endGame
        int winner = game.getGameWinner();
        if (winner == 0) {
            out0.writeBytes("GameEnd:SurrenderOpponent\n");
            out1.writeBytes("GameEnd:SurrenderPlayer\n");
        } else if (winner == 1) {
            out0.writeBytes("GameEnd:SurrenderPlayer\n");
            out1.writeBytes("GameEnd:SurrenderOpponent\n");
        } else {
            throw new IllegalArgumentException("Invalid winner " + winner + ".");
        }
    }

    /**
     * Stops both game clients, killing their threads. Waits for them to be dead.
     */
    private void stopClients() {
        user0Client.kill();
        user1Client.kill();
        while (user0Client.isAlive() || user1Client.isAlive()) {
        }
    }

    // Public Methods

    /**
     * Sets up a new game.
     * 
     * @param user0 User number 0.
     * @param user1 User number 1.
     * @param port  The port to be used.
     */
    public void setupGame(User user0, User user1, int port) {
        this.user0 = user0;
        this.user1 = user1;
        this.port = port;
        client1Ready = false;
        client2Ready = false;
        game = new Game(user0.getDeck(), user1.getDeck());
    }

    /**
     * Gets the user number and port for a given user.
     * 
     * @param user The user to get the number and port for.
     * @return The user number and port. Null if the user is not in the game.
     */
    public UserNumberAndPort getUserNumberAndPort(User user) {
        UserNumberAndPort userNumberAndPort = new UserNumberAndPort();
        if (user.getId().equals(user0.getId())) {
            userNumberAndPort.number = 0;
        } else if (user.getId().equals(user1.getId())) {
            userNumberAndPort.number = 1;
        } else {
            return null;
        }
        userNumberAndPort.port = port;
        return userNumberAndPort;
    }

    /**
     * Prepares a client to start the game. Mark the client as ready.
     * 
     * @param number The client number.
     * @throws Exception If the client number is invalid.
     */
    public void prepareClient(int number) throws Exception {
        if (number == 0) {
            client1Ready = true;
        } else if (number == 1) {
            client2Ready = true;
        } else {
            throw new IllegalArgumentException("Invalid number " + number + ".");
        }
    }

    /**
     * Tries to execute a move in the game. If the move is valid, synchronizes the
     * clients with the new game state.
     * 
     * @param player The number of the player executing the move.
     * @param move   The move to be executed.
     */
    public void tryMove(int player, String test) { // TODO: Implement tryMove
        try {
            // if (verifyMove(player, move)) {
            // game.executeMove(player, move);
            synchronizeClients(test);
            // }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Declares a surrender in the game.
     * 
     * @param player The number of the player surrendering.
     */
    public void declareSurrender(int player) {
        game.test_gameover = true;
    }

    /**
     * Prepares, runs and closes the game instance.
     */
    public void run() {
        try {
            setupGameServer();
            setupGameClients();
            gameLoop();
            stopClients();
            endGame();
            closeServer();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
