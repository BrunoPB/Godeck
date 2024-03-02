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
    private boolean client1Ready;
    private boolean client2Ready;

    public GameInstance() {
    }

    private void sendClientNumber() {
        try {
            out0.writeBytes("UserNumber:0\n");
            out1.writeBytes("UserNumber:1\n");
        } catch (Exception e) {
            System.out.println("Error sending client number.");
            System.out.println(e.getMessage());
        }
    }

    private boolean verifyMove(int player, GameMove move) { // TODO: Implement validations
        return true;
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
            System.out.println("Error trying to move.");
            System.out.println(e.getMessage());
        }
    }

    private void endGame() { // TODO: Implement endGame
        try {
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
        } catch (Exception e) {
            System.out.println("Error closing sockets.");
            System.out.println(e.getMessage());
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
        game = new Game(user0.getDeck(), user1.getDeck());
        client1Ready = false;
        client2Ready = false;
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

    public void run() {
        try {
            // Setting up server
            GameServerSingleton.getInstance().setPortAvailbility(port, false);
            ServerSocket server = new ServerSocket(port);
            Socket socket0 = server.accept();
            Socket socket1 = server.accept();
            DataInputStream in0 = new DataInputStream(new BufferedInputStream(socket0.getInputStream()));
            DataInputStream in1 = new DataInputStream(new BufferedInputStream(socket1.getInputStream()));
            in0.readByte();
            in1.readByte();
            out0 = new DataOutputStream(socket0.getOutputStream());
            out1 = new DataOutputStream(socket1.getOutputStream());

            // Setting up clients
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

            // Game loop
            while (!game.isGameOver()) {
                Thread.sleep(10);
            }

            // Closing clients
            stopClients();
            endGame();

            // Closing server
            in0.close();
            in1.close();
            out0.close();
            out1.close();
            socket0.close();
            socket1.close();
            server.close();
            GameServerSingleton.getInstance().setPortAvailbility(port, true);
        } catch (Exception e) {
            System.out.println("Server is not running");
            System.out.println(e.getMessage());
        }
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
}
