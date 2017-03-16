package comms;

import org.json.JSONObject;

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

    public ClientConn(int uid, int gid) {
        this.uid = uid;
        this.gid = gid;
        try {
            socket = new Socket("199.98.20.115", 5000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            System.err.println("Connected to host successfully!");

        } catch (IOException exc) {
            System.err.println("ERROR COMMUNICATING WITH SERVER");
        }

    }

    //public static void main(String[] args) throws Exception {
    //    ClientConn clientConn = new ClientConn(123, 456);
    // }

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

    public int addUIDToSocket(int uid) {

        JSONObject obj = new JSONObject();

        obj.put("fCall", "addUIDToSocket");
        obj.put("UID", uid);
        try {
            this.messageServer(obj);
            return 0; //Success
        } catch (Exception ex) {
            return 1; //Error
        }
    }

    public int addUIDToGID(int uid, int gid) {
        
        JSONObject obj = new JSONObject();

        obj.put("fCall", "addUIDToGID");
        obj.put("UID", uid);
        obj.put("GID", gid);
        try {
            this.messageServer(obj);
            return 0; //Success
        } catch (Exception ex) {
            return 1; //Error
        }
    }

}
