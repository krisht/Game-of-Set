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
    private String received_data;
    private String return_data;

    public ServerThread(Socket sock) {
        Socket tempsock = sock;

        try {
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true);

            received_data = in.readLine();
            JSONObject obj = new JSONObject(received_data);

            //System.err.println("RECEIVED: " + received_data);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this.sock = sock;
        this.processData(); //Uses received_data, figures out which BE function to call
                            //And fills in return_data

        this.out.println(return_data);
        
    }

    public void processData() {
        try {
            //System.err.println("RECEIVED: " + received_data);
            JSONObject received_object = new JSONObject(received_data);
            String temp = received_object.getJSONArray("fCall").toString().substring(2);
            temp = temp.substring(0, temp.length()-2);
            switch(temp) {
                case "CheckSet":
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
