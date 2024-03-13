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

import godeck.models.EndGameInfo;
import godeck.models.Game;
import godeck.models.GameMove;
import godeck.models.GodeckThread;
import godeck.models.User;
import godeck.models.view_models.Opponent;
import godeck.models.view_models.UserGame;
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

    private int turnTimeout;
    private int port;
    private Game game;
    private UserGame user0game;
    private UserGame user1game;
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
        sendInitialInfoToClients();
    }

    /**
     * Sends the client numbers to the respective clients.
     * 
     * @throws IOException If the client numbers could not be sent.
     */
    private void sendClientNumber() throws IOException {
        out0.flush();
        out1.flush();
        out0.writeBytes("UserNumber:0\n");
        out1.writeBytes("UserNumber:1\n");
    }

    /**
     * Sends the opponent information to the respective clients.
     * 
     * @throws IOException If the opponent information could not be sent.
     */
    private void sendOpponentInfo() throws IOException {
        out0.flush();
        out1.flush();
        out0.writeBytes("OpponentInfo:" + user0game.getOpponent().toJSONString() + "\n");
        out1.writeBytes("OpponentInfo:" + user1game.getOpponent().toJSONString() + "\n");
    }

    /**
     * Sends the decks to the respective clients.
     * 
     * @throws IOException If the decks could not be sent.
     */
    private void sendClientDeck() throws IOException {
        out0.flush();
        out1.flush();
        out0.writeBytes("Deck:" + user0game.getDeckJSONString() + "\n");
        out1.writeBytes("Deck:" + user1game.getDeckJSONString() + "\n");
    }

    /**
     * Sends the boards to the respective clients.
     * 
     * @throws IOException If the boards could not be sent.
     */
    private void sendClientBoard() throws IOException {
        out0.flush();
        out1.flush();
        out0.writeBytes("Board:" + user0game.getBoardJSONString() + "\n");
        out1.writeBytes("Board:" + user1game.getBoardJSONString() + "\n");
    }

    /**
     * Sends the turn timeout to the respective clients.
     * 
     * @throws IOException If the turn timeout could not be sent.
     */
    private void sendClientTimer() throws IOException {
        out0.flush();
        out1.flush();
        out0.writeBytes("Timer:" + turnTimeout + "\n");
        out1.writeBytes("Timer:" + turnTimeout + "\n");
    }

    /**
     * Sends the game confirmation to the respective clients. The game confirmation
     * is a message that tells the clients that the game is ready to start.
     * 
     * @throws IOException If the game confirmation could not be sent.
     */
    private void sendGameConfirmation() throws IOException {
        out0.flush();
        out1.flush();
        out0.writeBytes("GameStart:true\n");
        out1.writeBytes("GameStart:true\n");
    }

    /**
     * Sends all the initial information the clients need to start the game.
     * 
     * @throws IOException If the information could not be sent.
     */
    private void sendInitialInfoToClients() throws IOException {
        try {
            sendClientNumber();
            Thread.sleep(100);
            sendOpponentInfo();
            Thread.sleep(100);
            sendClientDeck();
            Thread.sleep(100);
            sendClientBoard();
            Thread.sleep(100);
            sendClientTimer();
            Thread.sleep(100);
            sendGameConfirmation();
            Thread.sleep(100);
        } catch (InterruptedException e) {
            ErrorHandler.message(e);
        }
    }

    /**
     * Synchronizes both clients with the current game state.
     * 
     * @param move The move that was last executed.
     * 
     * @throws IOException If the clients could not be synchronized.
     */
    private void synchronizeClients(GameMove move) throws IOException {
        try {
            sendClientBoard();
            Thread.sleep(100);
        } catch (InterruptedException e) {
            ErrorHandler.message(e);
        }
    }

    /**
     * Main game loop. Waits for the game to be over. Then takes the necessary
     * actions. Also starts and verify the turn timer.
     * 
     * @throws Exception If some error occurs during the game loop.
     */
    private void gameLoop() throws Exception {
        GameTimer timer = game.startTimer(turnTimeout + 2);
        CompletableFuture.anyOf(timer.timeOver, game.over).thenAccept((result) -> {
            if (result.equals("Timeout")) {
                game.notifyTimeout();
            }
        }).join();
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
     * Ends the game and sends the result to both clients.
     * 
     * @throws IOException If the result could not be sent.
     */
    private void endGame() throws IOException { // TODO: Implement endGame
        int winner = game.getGameWinner();
        String reason = game.getEndGameReason();
        EndGameInfo endGameInfo = new EndGameInfo(winner, reason, 0, 0);
        out0.flush();
        out1.flush();
        out0.writeBytes("GameEnd:" + endGameInfo.toJSONString() + "\n");
        out1.writeBytes("GameEnd:" + endGameInfo.toJSONString() + "\n");
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
     * @param user0       User number 0.
     * @param user1       User number 1.
     * @param port        The port to be used.
     * @param turnTimeout The turn timeout.
     */
    public void setupGame(User user0, User user1, int port, int turnTimeout) {
        this.port = port;
        this.turnTimeout = turnTimeout;
        game = new Game(user0.getDeck(), user1.getDeck());
        user0game = new UserGame(user0.getDeck(), 0, true, new Opponent(user1.getName()));
        user1game = new UserGame(user1.getDeck(), 1, false, new Opponent(user0.getName()));
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
            // synchronizeClients(move);
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
