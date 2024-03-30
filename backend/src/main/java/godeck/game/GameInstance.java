package godeck.game;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import godeck.models.GodeckThread;
import godeck.models.client.EndGameInfo;
import godeck.models.client.Opponent;
import godeck.models.client.ClientGame;
import godeck.models.entities.User;
import godeck.models.in_game.Game;
import godeck.models.in_game.GameMove;
import godeck.models.in_game.InGameCard;
import godeck.utils.ErrorHandler;
import godeck.utils.JSON;
import lombok.NoArgsConstructor;

/**
 * Represents a game between 2 players. Has game and server information.
 * 
 * Is a thread. Can be started and stopped.
 * 
 * @author Bruno Pena Baeta
 */
@Component
@NoArgsConstructor
public class GameInstance extends GodeckThread {
    // Properties

    private int port;
    private Game game;
    private ClientGame user0game;
    private ClientGame user1game;
    private GameClient user0Client;
    private GameClient user1Client;
    private ServerSocket server;
    private DataOutputStream out0;
    private DataOutputStream out1;
    private Socket socket0;
    private Socket socket1;
    private DataInputStream in0;
    private DataInputStream in1;

    // Private Methods

    /**
     * Allocates a port for the game server, connects the clients and initiates data
     * streams.
     * 
     * @throws Exception If the server could not be created or the
     *                   clients could not connect.
     */
    private void setupGameServer() throws Exception {
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
     * Sends a message to both clients.
     * 
     * @param message The message to be sent.
     * @throws IOException If the message could not be sent.
     */
    private void sendMessageToClients(String message) throws IOException {
        out0.flush();
        out1.flush();
        out0.writeBytes(message + "\n");
        out1.writeBytes(message + "\n");
    }

    /**
     * Sends two messages to the respective clients.
     * 
     * @param message0 Message to be sent to client 0.
     * @param message1 Message to be sent to client 1.
     * @throws IOException If the messages could not be sent.
     */
    private void sendMessageToClients(String message0, String message1) throws IOException {
        out0.flush();
        out1.flush();
        out0.writeBytes(message0 + "\n");
        out1.writeBytes(message1 + "\n");
    }

    /**
     * Sends the client numbers to the respective clients.
     * 
     * @throws IOException If the client numbers could not be sent.
     */
    private void sendClientNumber() throws IOException {
        String m0 = "UserNumber:0";
        String m1 = "UserNumber:1";
        sendMessageToClients(m0, m1);
    }

    /**
     * Sends the game turn to the respective clients.
     * 
     * @throws IOException If the game turn could not be sent.
     */
    private void sendGameTurn() throws IOException {
        String m0 = "GameTurn:false";
        String m1 = "GameTurn:false";
        if (game.getTurn() == 0) {
            m0 = "GameTurn:true";
        } else {
            m1 = "GameTurn:true";
        }
        sendMessageToClients(m0, m1);
    }

    /**
     * Sends the opponent information to the respective clients.
     * 
     * @throws IOException If the opponent information could not be sent.
     */
    private void sendOpponentInfo() throws IOException {
        String m0 = "OpponentInfo:" + JSON.stringify(user0game.getOpponent());
        String m1 = "OpponentInfo:" + JSON.stringify(user1game.getOpponent());
        sendMessageToClients(m0, m1);
    }

    /**
     * Sends the decks to the respective clients.
     * 
     * @throws IOException If the decks could not be sent.
     */
    private void sendClientDeck() throws IOException {
        String m0 = "Deck:" + JSON.stringify(user0game.getDeck());
        String m1 = "Deck:" + JSON.stringify(user1game.getDeck());
        sendMessageToClients(m0, m1);
    }

    /**
     * Sends the boards to the respective clients.
     * 
     * @throws IOException If the boards could not be sent.
     */
    private void sendClientBoard() throws IOException {
        String m0 = "Board:" + JSON.stringify(user0game.getBoard());
        String m1 = "Board:" + JSON.stringify(user1game.getBoard());
        sendMessageToClients(m0, m1);
    }

    /**
     * Sends the turn timeout to the respective clients.
     * 
     * @throws IOException If the turn timeout could not be sent.
     */
    private void sendClientTimer() throws IOException {
        sendMessageToClients("Timer:" + game.getTimeLimit());
    }

    /**
     * Sends the game confirmation to the respective clients. The game confirmation
     * is a message that tells the clients that the game is ready to start.
     * 
     * @throws IOException If the game confirmation could not be sent.
     */
    private void sendGameConfirmation() throws IOException {
        sendMessageToClients("GameStart:true");
    }

    /**
     * Sends an update message to the clients. The update message tells the clients
     * they should update their game state.
     * 
     * @throws IOException If the update message could not be sent.
     */
    private void sendClientUpdate() throws IOException {
        sendMessageToClients("Update:");
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
            sendGameTurn();
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
            sendClientTimer();
            Thread.sleep(100);
            sendGameTurn();
            Thread.sleep(100);
            sendClientDeck();
            Thread.sleep(100);
            sendClientBoard();
            Thread.sleep(100);
            sendClientUpdate();
            Thread.sleep(100);
        } catch (InterruptedException e) {
            ErrorHandler.message(e);
        }
    }

    /**
     * Updates the game state of both users.
     */
    private void updateUsersGameState() {
        user0game.updateGameState(game.getBoard(), game.getDeck0(), game.getTurn() == 0);
        user1game.updateGameState(game.getBoard(), game.getDeck1(), game.getTurn() == 1);
    }

    /**
     * Main game loop. Waits for the game to be over. Then takes the necessary
     * actions. Also starts and verify the turn timer.
     * 
     * @throws Exception If some error occurs during the game loop.
     */
    private void gameLoop() throws Exception {
        GameTimer timer = game.startTimer(game.getTimeLimit() + 2);
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
    }

    /**
     * Ends the game and sends the result to both clients.
     * 
     * @throws IOException If the result could not be sent.
     */
    private void endGame() throws IOException {
        int winner = game.getGameWinner();
        String reason = game.getEndGameReason();
        EndGameInfo endGameInfo = new EndGameInfo(winner, reason, 0, 0);
        sendMessageToClients("GameEnd:" + JSON.stringify(endGameInfo));
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
        ArrayList<InGameCard> deck0 = user0.getDeck().stream().map((card) -> new InGameCard(0, card))
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<InGameCard> deck1 = user1.getDeck().stream().map((card) -> new InGameCard(1, card))
                .collect(Collectors.toCollection(ArrayList::new));
        game = new Game(deck0, deck1, turnTimeout);
        user0game = new ClientGame(game.getBoard(), deck0, true, 0, new Opponent(user1.getDisplayName()), turnTimeout);
        user1game = new ClientGame(game.getBoard(), deck1, false, 1, new Opponent(user0.getDisplayName()), turnTimeout);
    }

    /**
     * Tries to execute a move in the game. If the move is valid, synchronizes the
     * clients with the new game state.
     * 
     * @param player The number of the player executing the move.
     * @param move   The move to be executed.
     */
    public void tryMove(int player, GameMove move) {
        try {
            if (game.verifyMove(player, move)) {
                game.executeMove(move);
                game.resetTimer();
                updateUsersGameState();
                synchronizeClients(move);
                game.checkEndGame();
            }
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
