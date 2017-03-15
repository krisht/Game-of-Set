package comms;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ServerThread extends Thread {

    private ConcurrentHashMap<Integer, Socket> uidToSocket = new ConcurrentHashMap<>();

    private Socket sock;

    private BufferedReader in;
    private PrintWriter out;
    private String return_data;

    public ServerThread(Socket sock) {
        Socket tempsock = sock;
        JSONObject obj = new JSONObject();

        try {
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true);
            obj = new JSONObject(in.readLine());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this.sock = sock;
        this.processData(obj); //Uses received_data, figures out which BE function to call
        //And fills in return_data

        this.out.println(return_data);

    }

    public void processData(JSONObject obj) {

        if (obj.length() == 0)
            return_data = obj.toString();

        try {
            String temp = obj.getJSONArray("fCall").toString().substring(2);
            temp = temp.substring(0, temp.length()-2);
            switch(temp) {
                case "checkSet":
                    //System.err.println("In Checkset");
                    //CALL CHECKSET, RETURN IT INTO private STRING "return_data" HERE 
                    break;
                case "Other":
                    //System.err.println("In Other");
                    break;
                default:
                    //error
                    break;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
