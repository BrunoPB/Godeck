package godeck.game;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import godeck.models.Game;
import godeck.models.GodeckThread;
import godeck.models.User;
import godeck.utils.ErrorHandler;

/**
 * Represents a game between 2 players. Has game and server information.
 * 
 * Is a thread. Can be started and stopped.
 * 
 * @author Bruno Pena Baeta
 */
@Component
public class GameInstance extends GodeckThread {
    // Properties

    private int port;
    private Game game;
    private GameClient user0Client;
    private GameClient user1Client;
    private ServerSocket server;
    private DataOutputStream out0;
    private DataOutputStream out1;
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
     * @throws Exception If the server could not be created or the
     *                   clients could not connect.
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
            throw new SocketTimeoutException("Opponent could not connect.");
        }
        initiateDataStreams();
    }

    /**
     * Initiates the server data input and output streams.
     * 
     * @throws IOException If the data input stream could not read the first byte.
     */
    private void initiateDataStreams() throws IOException {
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
     * @throws IOException If the clients could not be started or if they could not
     *                     be synchronized.
     */
    private void setupGameClients() throws IOException {
        user0Client = new GameClient();
        user0Client.setupGameClient(0, this, in0);
        user1Client = new GameClient();
        user1Client.setupGameClient(1, this, in1);
        user0Client.start();
        user1Client.start();
        CompletableFuture.allOf(user0Client.ready, user1Client.ready).join();
        sendClientNumber();
    }

    /**
     * Main game loop. Waits for the game to be over. Then takes the necessary
     * actions.
     */
    private void gameLoop() {
        game.over.join();
    }

    /**
     * Closes the server and all sockets and data streams.
     * 
     * @throws IOException If the server could not be closed.
     */
    private void closeServer() throws IOException {
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
     * @throws IOException If the client numbers could not be sent.
     */
    private void sendClientNumber() throws IOException {
        out0.writeBytes("UserNumber:0\n");
        out1.writeBytes("UserNumber:1\n");
    }

    /**
     * Synchronizes both clients with the current game state.
     * 
     * @param move The last move executed.
     * @throws IOException If the clients could not be synchronized.
     */
    private void synchronizeClients(String test) throws IOException { // TODO: Implement synchronizeClients
        out0.flush();
        out1.flush();
    }

    /**
     * Ends the game and sends the result to both clients.
     * 
     * @throws IOException If the result could not be sent.
     */
    private void endGame() throws IOException { // TODO: Implement endGame
        int winner = game.getGameWinner();
        if (winner == 0) {
            out0.writeBytes("GameEnd:SurrenderOpponent\n");
            out1.writeBytes("GameEnd:SurrenderPlayer\n");
        } else if (winner == 1) {
            out0.writeBytes("GameEnd:SurrenderPlayer\n");
            out1.writeBytes("GameEnd:SurrenderOpponent\n");
        } else {
            out0.writeBytes("GameEnd:SurrenderOpponent\n");
            out1.writeBytes("GameEnd:SurrenderOpponent\n");
        }
    }

    /**
     * Stops both game clients, killing their threads. Waits for them to be dead.
     */
    private void stopClients() {
        user0Client.kill();
        user1Client.kill();
        CompletableFuture.allOf(user0Client.killed, user1Client.killed).join();
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
        this.port = port;
        game = new Game(user0.getDeck(), user1.getDeck());
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
            ErrorHandler.message(e);
        }
    }

    /**
     * Declares a surrender in the game.
     * 
     * @param player The number of the player surrendering.
     */
    public void declareSurrender(int player) {
        game.executeSurrender(player);
    }

    /**
     * Prepares, runs and closes the game instance.
     */
    @Override
    public void run() {
        try {
            setupGameServer();
            setupGameClients();
            gameLoop();
            stopClients();
            endGame();
            closeServer();
        } catch (Exception e) {
            ErrorHandler.message(e);
        }
    }

    /**
     * Kills the game clients, ends the game and closes the server.
     */
    @Override
    public void kill() {
        try {
            stopClients();
            endGame();
            closeServer();
        } catch (IOException e) {
            ErrorHandler.message(e);
        } finally {
            super.killed.complete(null);
        }
    }
}
