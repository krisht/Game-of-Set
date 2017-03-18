package backend;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArraySet;

import static backend.ServerConn.gidToUid;
import static backend.ServerConn.uidToSocket;

public class ServerThread extends Thread {

    private Socket sock;

    private BufferedReader in;
    private PrintWriter out;

    public ServerThread(Socket sock) {
        JSONObject obj = new JSONObject();

        try {
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true);
            obj = new JSONObject(in.readLine());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this.sock = sock;
        JSONObject return_obj = this.processData(obj); //Uses received_data, figures out which BE function to call
        //And fills in return_data

        this.out.println(return_obj.toString());

    }

    private JSONObject processData(JSONObject obj) {

        JSONObject retObj = new JSONObject();

        if (obj.length() == 0) {
            retObj.put("error", -1);
            return retObj;
        }

        try {
            String fCall = obj.getString("fCall");
            switch (fCall) {
                case "addUIDToSocket":
                    uidToSocket.put(obj.getInt("UID"), this.sock); //Add to hashmap
                    retObj.put("returnValue", 0);  //Success
                    retObj.put("error", 0);
                    break;
                case "addUIDToGID":
                    CopyOnWriteArraySet setOfUID = new CopyOnWriteArraySet();
                    setOfUID.add(obj.getInt("UID"));
                    if (gidToUid.get(obj.getInt("GID")) != null)
                        setOfUID.addAll(gidToUid.get(obj.getInt("GID")));

                    gidToUid.put(obj.getInt("GID"), setOfUID);
                    retObj.put("returnValue", 0); //Success
                    retObj.put("error", 0);
                    break;
                case "processSubmission":
                    //System.err.println("In Checkset");
                    //CALL CHECKSET, RETURN IT INTO private STRING "return_data" HERE
                    retObj.put("error", 0);
                    break;
                case "createGame":
                    //Make new backend.Game Object and add to hashmap or what not
                    retObj.put("error", 0);
                    break;
                default:
                    retObj.put("error", 1);
                    //error
                    break;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retObj;

    }

}
