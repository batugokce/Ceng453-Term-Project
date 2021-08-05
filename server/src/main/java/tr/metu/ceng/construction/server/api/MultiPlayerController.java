package tr.metu.ceng.construction.server.api;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Responsible from setting communication over ServerSocket bound to the
 * specified port and starting multi-player game thread.
 */
public class MultiPlayerController implements Runnable {

    private ServerSocket serverSocket;
    private boolean flag = true;
    public static int PORT = 8084;

    /**
     * Overrides the run function of Java Thread class.
     * It provides a session for communication between player1 and player2
     * via socket.
     */
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
            int timeout = 60 * 60 * 1000; // milliseconds
            serverSocket.setSoTimeout(timeout);
            while (flag) {
                Socket player1Socket = serverSocket.accept();
                System.out.println("Player1 connected.");
                Socket player2Socket = serverSocket.accept();
                System.out.println("Player2 connected.");
                Thread newSession = new Thread(new MultiPlayerGameController(player1Socket, player2Socket));
                newSession.start();
            }
        } catch (Exception exception) {
            flag = false;
            exception.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (Exception exception) {
                System.out.println("There is an exception while closing connection.");
                exception.printStackTrace();
            }
        }
    }
}