package godeck.components.game;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
    private DataOutputStream out0;
    private DataOutputStream out1;

    public GameInstance() {
    }

    private boolean verifyMove(int player, GameMove move) { // TODO: Implement validations
        return true;
    }

    private void synchronizeClients(int player, String test) {
        try {
            if (player == 0) {
                out0.flush();
                out0.writeBytes("Parabens, seu animal, você é o jogador 0!\n");
                // out0.writeBytes(test + "\n");
            } else if (player == 1) {
                out1.flush();
                out0.writeBytes("Parabens, seu animal, você é o jogador 1!\n");
                // out1.writeBytes(test + "\n");
            } else {
                throw new IllegalArgumentException("Invalid player " + player + ".");
            }
        } catch (Exception e) {
            System.out.println("Error trying to move.");
            System.out.println(e.getMessage());
        }
    }

    private void endGame() { // TODO: Implement endGame
        try {
            out0.writeByte(0);
            out1.writeByte(0);
            out0.close();
            out1.close();
        } catch (Exception e) {
            System.out.println("Error closing sockets.");
            System.out.println(e.getMessage());
        }
    }

    public void setupGame(User user0, User user1, int port) {
        this.user0 = user0;
        this.user1 = user1;
        this.port = port;
        game = new Game(user0.getDeck(), user1.getDeck());
    }

    public UserNumberAndPort getUserNumberAndPort(User user) {
        UserNumberAndPort userNumberAndPort = new UserNumberAndPort();
        if (user.getId().equals(user0.getId())) {
            userNumberAndPort.number = 0;
        } else if (user.getId().equals(user1.getId())) {
            userNumberAndPort.number = 1;
        } else {
            throw new IllegalArgumentException("User not found in this game.");
        }
        userNumberAndPort.port = port;
        return userNumberAndPort;
    }

    public void run() {
        try {
            ServerSocket server = new ServerSocket(port);
            Socket socket0 = server.accept();
            Socket socket1 = server.accept();

            DataInputStream in0 = new DataInputStream(new BufferedInputStream(socket0.getInputStream()));
            DataInputStream in1 = new DataInputStream(new BufferedInputStream(socket1.getInputStream()));

            out0 = new DataOutputStream(socket0.getOutputStream());
            out1 = new DataOutputStream(socket1.getOutputStream());

            user0Client = new GameClient();
            user0Client.setupGameClient(0, this, in0);
            user1Client = new GameClient();
            user1Client.setupGameClient(1, this, in1);

            user0Client.start();
            user1Client.start();

            in0.readByte();
            in1.readByte();

            while (!game.isGameOver()) {
            }

            endGame();

            server.close();
        } catch (Exception e) {
            System.out.println("Server is not running");
            System.out.println(e.getMessage());
        }
    }

    public void tryMove(int player, String test) { // TODO: Implement tryMove

        // if (verifyMove(player, move)) {
        // game.executeMove(player, move);
        synchronizeClients(player, test);
        // }

    }
}
