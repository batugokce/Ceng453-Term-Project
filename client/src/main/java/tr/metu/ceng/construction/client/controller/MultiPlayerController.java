package tr.metu.ceng.construction.client.controller;

import tr.metu.ceng.construction.client.constant.NetworkConstants;
import tr.metu.ceng.construction.common.TableStateForMultiplayer;
import tr.metu.ceng.construction.client.constant.GameConstants;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Responsible from handling communication over ServerSocket bound to the
 * specified port and IP.
 */
public class MultiPlayerController {
    private static final String IP_ADDRESS = ""; //TODO
    private static final int PORT = 8084;
    ObjectInputStream readStreamFromServer;
    ObjectOutputStream writeStreamToServer;

    /**
     * Constructor for MultiPlayerController. It initializes socket and bounds input and output streams.
     */
    public MultiPlayerController() {
        try {
            Socket socket = new Socket(IP_ADDRESS, PORT);
            System.out.println("Connecting...");
            writeStreamToServer = new ObjectOutputStream(socket.getOutputStream());
            readStreamFromServer = new ObjectInputStream(socket.getInputStream());
            NetworkConstants.WRITE_TO_SOCKET = writeStreamToServer;
            NetworkConstants.READ_FROM_SOCKET = readStreamFromServer;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * This method writes a message including player username and cumulative to the output stream
     */
    public void writeUsernameAndCumulativeScore() {
        try {
            String message = GameConstants.USERNAME +
                    GameConstants.SPLIT_TOKEN +
                    GameConstants.TABLE_STATE.getPlayer1CumulativeScore().toString();
            writeStreamToServer.writeObject(message);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Listens for new table state messages from server
     * @return an updated table state object
     */
    public TableStateForMultiplayer getTableState() {
        try {
            return (TableStateForMultiplayer) readStreamFromServer.readObject();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

}
