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

@Component
public class GameInstance extends Thread {
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

    public GameInstance() {
    }

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
        in0 = new DataInputStream(new BufferedInputStream(socket0.getInputStream()));
        in1 = new DataInputStream(new BufferedInputStream(socket1.getInputStream()));
        in0.readByte();
        in1.readByte();
        out0 = new DataOutputStream(socket0.getOutputStream());
        out1 = new DataOutputStream(socket1.getOutputStream());
    }

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

    private void gameLoop() throws Exception {
        while (!game.isGameOver()) {
            Thread.sleep(10);
        }
    }

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

    private void sendClientNumber() throws Exception {
        out0.writeBytes("UserNumber:0\n");
        out1.writeBytes("UserNumber:1\n");
    }

    private void synchronizeClients(int player, String test) {
        try {
            if (player == 0) {
                out0.flush();
                out0.writeBytes("DebugTest:" + test + "\n");
            } else if (player == 1) {
                out1.flush();
                out1.writeBytes("DebugTest:" + test + "\n");
            } else {
                throw new IllegalArgumentException("Invalid player " + player + ".");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

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

    private void stopClients() {
        user0Client.kill();
        user1Client.kill();
        while (user0Client.isAlive() || user1Client.isAlive()) {
        }
    }

    public void setupGame(User user0, User user1, int port) {
        this.user0 = user0;
        this.user1 = user1;
        this.port = port;
        client1Ready = false;
        client2Ready = false;
        game = new Game(user0.getDeck(), user1.getDeck());
    }

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

    public void prepareClient(int number) {
        if (number == 0) {
            client1Ready = true;
        } else if (number == 1) {
            client2Ready = true;
        } else {
            throw new IllegalArgumentException("Invalid number " + number + ".");
        }
    }

    public void tryMove(int player, String test) { // TODO: Implement tryMove
        // if (verifyMove(player, move)) {
        // game.executeMove(player, move);
        synchronizeClients(player, test);
        // }
    }

    public void declareSurrender(int player) {
        game.test_gameover = true;
    }

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
