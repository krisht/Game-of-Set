package comms;

import org.json.JSONObject;

import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;


public class ClientSide {

    private static PrintWriter writer;
    private final int port = 8052;
    private final String host = "199.97.20.114";
    private Socket clSock;
    private Thread clThread;


    public ClientSide() {
        try {
            clSock = new Socket(host, port);
            writer = new PrintWriter(clSock.getOutputStream());
            clThread = new Thread();
            clThread.start();
        } catch (ConnectException exp) {
            // Show error message that can't connect to server
        } catch (Exception exp) {
            // Show that an unknown error occurred
        }
    }

    public static void receiveRequest() {

    }

    public static void sendRequest(JSONObject obj) {
        writer.println(obj);
        writer.flush();
    }

}
