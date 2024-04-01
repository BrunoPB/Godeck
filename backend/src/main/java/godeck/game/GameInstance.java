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
    private Game game;
    private ArrayList<ClientGame> clientGame = new ArrayList<ClientGame>();
    private ArrayList<GameClient> gameClient = new ArrayList<GameClient>();
    private ServerSocket server;
    private ArrayList<Socket> socket = new ArrayList<Socket>();
    private ArrayList<DataOutputStream> out = new ArrayList<DataOutputStream>();
    private ArrayList<DataInputStream> in = new ArrayList<DataInputStream>();

    // Private Methods

    /**
     * Starts the server and both game clients so the game can start.
     * 
     * @throws Exception If the server could not be created or the
     *                   clients could not connect.
     */
    private void setupGameServerAndClients() throws Exception {
        server = new ServerSocket(port);
        server.setSoTimeout(5000); // 5 seconds timeout
        try {
            initiateClientSockets();
        } catch (SocketTimeoutException | TimeoutException e) {
            if (socket.get(0) != null && socket.get(0).isBound()) {
                initiateSocketDataStreams(0);
                out.get(0).writeBytes("Error:Opponent could not connect.\n");
            }
            closeServer();
            throw new SocketTimeoutException("Player could not connect.");
        }
        sendInitialInfoToClients();
    }

    /**
     * Initiate both client sockets based on the server. Tries at a maximum of
     * SOCKET_MAX_TRIES times. If the client connects and send the wrong message,
     * tries again.
     * 
     * @throws SocketTimeoutException If no client connects in time
     * @throws TimeoutException       If the client that connect does not answer in
     *                                time
     * @throws IOException            If the message could not be received
     */
    private void initiateClientSockets()
            throws SocketTimeoutException, TimeoutException, IOException {
        for (int i = 0; i < SOCKET_MAX_TRIES; i++) {
            socket.set(0, server.accept());
            initiateSocketDataStreams(0);
            try {
                setupGameClient(0);
                i = SOCKET_MAX_TRIES;
            } catch (ExecutionException | InterruptedException e) {
                gameClient.get(0).kill();
                gameClient.get(0).killed.join();
            }
        }
        for (int i = 0; i < SOCKET_MAX_TRIES; i++) {
            socket.set(1, server.accept());
            initiateSocketDataStreams(1);
            try {
                setupGameClient(1);
                i = SOCKET_MAX_TRIES;
            } catch (ExecutionException | InterruptedException e) {
                gameClient.get(1).kill();
                gameClient.get(1).killed.join();
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
        in.set(n, new DataInputStream(new BufferedInputStream(socket.get(n).getInputStream())));
        in.get(n).readByte();
        out.set(n, new DataOutputStream(socket.get(n).getOutputStream()));
    }

    /**
     * Setup and starts a game client. Waits for it to send the ready message
     * 
     * @param n the client number
     * @throws TimeoutException     If the client does not answer in time
     * @throws ExecutionException   If the client is not ready when it should be
     * @throws InterruptedException If the game client thread is interrupted while
     *                              waiting for the answer
     */
    private void setupGameClient(int n) throws TimeoutException, ExecutionException, InterruptedException {
        gameClient.get(n).setupGameClient(n, this, in.get(n));
        gameClient.get(n).start();
        gameClient.get(n).ready.get(3, TimeUnit.SECONDS);
    }

    /**
     * Sends a message to both clients.
     * 
     * @param message The message to be sent.
     * @throws IOException If the message could not be sent.
     */
    private void sendMessageToClients(String message) throws IOException {
        out.get(0).flush();
        out.get(1).flush();
        out.get(0).writeBytes(message + "\n");
        out.get(1).writeBytes(message + "\n");
    }

    /**
     * Sends two messages to the respective clients.
     * 
     * @param message0 Message to be sent to client 0.
     * @param message1 Message to be sent to client 1.
     * @throws IOException If the messages could not be sent.
     */
    private void sendMessageToClients(String message0, String message1) throws IOException {
        out.get(0).flush();
        out.get(1).flush();
        out.get(0).writeBytes(message0 + "\n");
        out.get(1).writeBytes(message1 + "\n");
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
        String m0 = "OpponentInfo:" + JSON.stringify(clientGame.get(0).getOpponent());
        String m1 = "OpponentInfo:" + JSON.stringify(clientGame.get(1).getOpponent());
        sendMessageToClients(m0, m1);
    }

    /**
     * Sends the decks to the respective clients.
     * 
     * @throws IOException If the decks could not be sent.
     */
    private void sendClientDeck() throws IOException {
        String m0 = "Deck:" + JSON.stringify(clientGame.get(0).getDeck());
        String m1 = "Deck:" + JSON.stringify(clientGame.get(1).getDeck());
        sendMessageToClients(m0, m1);
    }

    /**
     * Sends the boards to the respective clients.
     * 
     * @throws IOException If the boards could not be sent.
     */
    private void sendClientBoard() throws IOException {
        String m0 = "Board:" + JSON.stringify(clientGame.get(0).getBoard());
        String m1 = "Board:" + JSON.stringify(clientGame.get(1).getBoard());
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
        clientGame.get(0).updateGameState(game.getBoard(), game.getDeck0(), game.getTurn() == 0);
        clientGame.get(1).updateGameState(game.getBoard(), game.getDeck1(), game.getTurn() == 1);
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
        if (in.get(0) != null)
            in.get(0).close();
        if (in.get(1) != null)
            in.get(1).close();
        if (out.get(0) != null)
            out.get(0).close();
        if (out.get(1) != null)
            out.get(1).close();
        if (socket.get(0) != null)
            socket.get(0).close();
        if (socket.get(1) != null)
            socket.get(1).close();
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
        gameClient.get(0).kill();
        gameClient.get(1).kill();
        CompletableFuture.allOf(gameClient.get(0).killed, gameClient.get(1).killed).join();
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

        ArrayList<InGameCard> deck0 = new ArrayList<InGameCard>();
        ArrayList<InGameCard> deck1 = new ArrayList<InGameCard>();
        for (int i = 0; i < user0.getDeck().size(); i++) {
            deck0.add(new InGameCard(0, user0.getDeck().get(i)));
            deck1.add(new InGameCard(1, user1.getDeck().get(i)));
        }

        game = new Game(deck0, deck1, turnTimeout);

        clientGame.add(
                new ClientGame(game.getBoard(), deck0, true, 0, new Opponent(user1.getDisplayName()), turnTimeout));
        clientGame.add(
                new ClientGame(game.getBoard(), deck1, false, 1, new Opponent(user0.getDisplayName()), turnTimeout));

        gameClient.add(new GameClient());
        gameClient.add(new GameClient());

        socket.add(null);
        socket.add(null);

        in.add(null);
        in.add(null);

        out.add(null);
        out.add(null);
    }

    public void checkClientReady(int n, String msg) {
        // TODO: Decrypt msg
        if (msg.equals("Ready:")) {
            gameClient.get(n).ready.complete(null);
        } else {
            gameClient.get(n).ready.completeExceptionally(new Exception());
        }
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
            if (game.verifyMove(player, gMove)) {
                game.executeMove(gMove);
                game.resetTimer();
                updateUsersGameState();
                synchronizeClients(gMove);
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
