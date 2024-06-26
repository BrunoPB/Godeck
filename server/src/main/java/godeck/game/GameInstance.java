package godeck.game;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.stereotype.Component;

import godeck.models.GodeckThread;
import godeck.models.client.ClientGame;
import godeck.models.client.ClientGameMove;
import godeck.models.client.EndGameInfo;
import godeck.models.client.Opponent;
import godeck.models.entities.User;
import godeck.models.ingame.Game;
import godeck.models.ingame.GameMove;
import godeck.models.ingame.InGameCard;
import godeck.security.AESCryptography;
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

    private final int SOCKET_MAX_TRIES = 3;

    private int port;
    private AESCryptography[] crypt = { null, null };
    private Game game;
    private ServerSocket server;
    private ClientGame[] clientGame = { null, null };
    private GameClient[] gameClient = { null, null };
    private Socket[] socket = { null, null };
    private DataOutputStream[] out = { null, null };
    private DataInputStream[] in = { null, null };

    // Private Methods

    /**
     * Starts the server and both game clients so the game can start.
     * 
     * @throws Exception If the server could not be created or the
     *                   clients could not connect.
     */
    private void setupGameServerAndClients() throws Exception {
        server = new ServerSocket(port);
        try {
            initiateClientSocket(0, true);
            initiateClientSocket(1, false);
        } catch (SocketTimeoutException e) {
            if (socket[0] != null && socket[0].isBound()) {
                initiateSocketDataStreams(0);
                sendTCP(0, "Error:Opponent could not connect.\n");
            }
            closeServer();
            throw new SocketTimeoutException("Player could not connect.");
        }
        sendInitialInfoToClients();
    }

    /**
     * Initiate a single client socket based on the server. Tries at a maximum of
     * SOCKET_MAX_TRIES times. If the client connects and send the wrong message,
     * tries again.
     * 
     * @param n       The client number
     * @param isFirst If this is the first client to try to connect. This is useful
     *                for detecting the encryption key
     * @throws SocketTimeoutException If the clien does not connect in time
     * @throws IOException            If the message could not be received
     */
    private void initiateClientSocket(int n, boolean isFirst)
            throws SocketTimeoutException, IOException {
        for (int i = 0; i < SOCKET_MAX_TRIES; i++) {
            server.setSoTimeout(3000); // 3 seconds timeout
            socket[n] = server.accept();
            initiateSocketDataStreams(n);
            try {
                setupGameClient(n, isFirst);
                i = SOCKET_MAX_TRIES;
            } catch (ExecutionException | TimeoutException | InterruptedException e) {
                gameClient[n].kill();
                gameClient[n].killed.join();
            }
        }
    }

    /**
     * Initiate both input and out data streams of a single client
     * 
     * @param n The client number
     * @throws IOException If a message could not be received
     */
    private void initiateSocketDataStreams(int n) throws IOException {
        in[n] = new DataInputStream(new BufferedInputStream(socket[n].getInputStream()));
        in[n].readByte();
        out[n] = new DataOutputStream(socket[n].getOutputStream());
    }

    /**
     * Setup and starts a game client. Waits for it to send the ready message.
     * 
     * @param n       the client number
     * @param isFirst If this is the first client to try to connect. This is useful
     *                for detecting the encryption key
     * @throws TimeoutException     If the client does not answer in time
     * @throws ExecutionException   If the client is not ready when it should be
     * @throws InterruptedException If the game client thread is interrupted while
     *                              waiting for the answer
     */
    private void setupGameClient(int n, boolean isFirst)
            throws TimeoutException, ExecutionException, InterruptedException {
        gameClient[n] = new GameClient();
        gameClient[n].setupGameClient(n, this, in[n]);
        gameClient[n].start();
        int receivedNumber = gameClient[n].receivedNumber.get(2, TimeUnit.SECONDS);
        if (isFirst && n != receivedNumber) {
            AESCryptography aux = crypt[0].clone();
            crypt[0] = crypt[1].clone();
            crypt[1] = aux;
        }
        gameClient[n].setCryptography(crypt[n]);
        gameClient[n].numberProcessed.complete(null);
        gameClient[n].ready.get(2, TimeUnit.SECONDS);
    }

    /**
     * Sends a message to a client via TCP. Encrypts the message before sending.
     * 
     * @param number  The number of the client.
     * @param message The message to be sent.
     */
    private void sendTCP(int number, String message) {
        try {
            out[number].flush();
            String encoded = Base64.getEncoder().encodeToString(message.getBytes());
            out[number].writeBytes(encoded + "\n");
        } catch (Exception e) {
            ErrorHandler.message(e);
        }
    }

    /**
     * Sends a message to both clients.
     * 
     * @param message The message to be sent.
     * @throws IOException If the message could not be sent.
     */
    private void sendMessageToClients(String message) throws IOException {
        sendTCP(0, message);
        sendTCP(1, message);
    }

    /**
     * Sends two messages to the respective clients.
     * 
     * @param message0 Message to be sent to client 0.
     * @param message1 Message to be sent to client 1.
     * @throws IOException If the messages could not be sent.
     */
    private void sendMessageToClients(String message0, String message1) throws IOException {
        sendTCP(0, message0);
        sendTCP(1, message1);
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
        String m0 = "OpponentInfo:" + JSON.stringify(clientGame[0].getOpponent());
        String m1 = "OpponentInfo:" + JSON.stringify(clientGame[1].getOpponent());
        sendMessageToClients(m0, m1);
    }

    /**
     * Sends the decks to the respective clients.
     * 
     * @throws IOException If the decks could not be sent.
     */
    private void sendClientDeck() throws IOException {
        String m0 = "Deck:" + JSON.stringify(clientGame[0].getDeck());
        String m1 = "Deck:" + JSON.stringify(clientGame[1].getDeck());
        sendMessageToClients(m0, m1);
    }

    /**
     * Sends the boards to the respective clients.
     * 
     * @throws IOException If the boards could not be sent.
     */
    private void sendClientBoard() throws IOException {
        String m0 = "Board:" + JSON.stringify(clientGame[0].getBoard());
        String m1 = "Board:" + JSON.stringify(clientGame[1].getBoard());
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
     * @throws IOException If the clients could not be synchronized.
     */
    private void synchronizeClients() throws IOException {
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
        clientGame[0].updateGameState(game.getBoard(), game.getDeck0(), game.getTurn() == 0);
        clientGame[1].updateGameState(game.getBoard(), game.getDeck1(), game.getTurn() == 1);
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
        if (in[0] != null)
            in[0].close();
        if (in[1] != null)
            in[1].close();
        if (out[0] != null)
            out[0].close();
        if (out[1] != null)
            out[1].close();
        if (socket[0] != null)
            socket[0].close();
        if (socket[1] != null)
            socket[1].close();
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
        gameClient[0].kill();
        gameClient[1].kill();
        CompletableFuture.allOf(gameClient[0].killed, gameClient[1].killed).join();
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
    public void setupGame(User user0, User user1, int port, AESCryptography crypt0, AESCryptography crypt1,
            int turnTimeout) {
        this.port = port;
        this.crypt[0] = crypt0;
        this.crypt[1] = crypt1;

        ArrayList<InGameCard> deck0 = new ArrayList<InGameCard>();
        ArrayList<InGameCard> deck1 = new ArrayList<InGameCard>();
        for (int i = 0; i < user0.getDeck().size(); i++) {
            deck0.add(new InGameCard(0, user0.getDeck().get(i)));
            deck1.add(new InGameCard(1, user1.getDeck().get(i)));
        }

        game = new Game(deck0, deck1, turnTimeout);

        clientGame[0] = new ClientGame(game.getBoard(), deck0, true, 0, new Opponent(user1.getDisplayName()),
                turnTimeout);
        clientGame[1] = new ClientGame(game.getBoard(), deck1, false, 1, new Opponent(user0.getDisplayName()),
                turnTimeout);
    }

    /**
     * Tries to execute a move in the game. If the move is valid, synchronizes the
     * clients with the new game state.
     * 
     * @param player The number of the player executing the move.
     * @param move   The move to be executed.
     */
    public void tryMove(int player, ClientGameMove move) {
        try {
            GameMove gMove = new GameMove(player, move);
            if (game.validateMove(player, gMove)) {
                game.executeMove(gMove);
                game.resetTimer();
                updateUsersGameState();
                synchronizeClients();
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
            setupGameServerAndClients();
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
