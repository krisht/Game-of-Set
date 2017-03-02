package comms;

import org.json.JSONObject;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConn {

    private static Socket socket;
    private static int uid;
    private BufferedReader in;
    private PrintWriter out;
    private int gid;

    public ClientConn(int uid) {
        try {
            socket = new Socket("199.98.20.115", 5000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            System.err.println("Connected to host successfully!");

        } catch (IOException exc) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Error connecting to server!",
                    "Server Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            System.exit(-1);
        }

        JSONObject firstMessage = new JSONObject();
        firstMessage.append("uid", uid);


        try {
            messageServer(firstMessage);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Unable to communicate with connected server!",
                    "Communication Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            System.exit(-1);
        }

    }

    public static void main(String[] args) throws Exception {
        ClientConn clientConn = new ClientConn(1);
    }

    public JSONObject messageServer(JSONObject obj) throws Exception {
        String request = obj.toString();
        this.out.println(request);
        return new JSONObject(in.readLine());
    }

}
