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
            System.err.println("ERROR COMMUNICATING WITH SERVER");
        }

    }

    public static void main(String[] args) throws Exception {
        ClientConn clientConn = new ClientConn(1);
        JSONObject obj = new JSONObject();
        obj.append("fCall", "CheckSet");
        int a[]={33,3,4,5};
        obj.append("uid", 1234);
        obj.append("gid", 23);
        obj.append("params", a);
        clientConn.messageServer(obj);
    }

    public JSONObject messageServer(JSONObject obj) throws Exception {
        String request = obj.toString();
        this.out.println(request);
        try {
            String check = in.readLine();
            //System.err.println("RETURNED DATA: " + check);
            JSONObject temp = new JSONObject(check);
            return temp;
        } catch (Exception ex) {
            System.err.println("Cannot be made into JSON Object");
            return null;
        }
    }

}
