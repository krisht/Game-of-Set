package backend;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArraySet;

import static backend.ServerConn.gidToUid;
import static backend.ServerConn.uidToSocket;

class ServerThread extends Thread {

    private Socket sock;

    private BufferedReader in;
    private PrintWriter out;

    ServerThread(Socket sock) {
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
                case "loginUser": //Tested as of 4/15
                    String uname = obj.getString("login");
                    String pass = obj.getString("pass");
                    JSONObject tempobj = new JSONObject();
                    tempobj = GameListing.login(uname, pass);
                    tempobj.put("fCall", "loginResponse");
                    return tempobj;

                case "registerUser": //Tested as of 4/15
                    uname = obj.getString("login");
                    pass = obj.getString("pass");
                    tempobj = new JSONObject();
                    tempobj = GameListing.register(uname, pass, "");
                    tempobj.put("fCall", "registerResponse");
                    return tempobj;

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
                case "userSubmits":
                    int uid = obj.getInt("uid");
                    int gid = obj.getInt("gid");
                    int c1 = obj.getInt("c1");
                    int c2 = obj.getInt("c2");
                    int c3 = obj.getInt("c3");
                    return GameListing.getGame(gid).userSubmits(uid, c1, c2, c3).put("fCall", "userSubmits");
                case "createGame":
                    uid = obj.getInt("uid");
                    if (obj.has("gamename")) {
                        String gamename = obj.getString("gamename");
                        return GameListing.createGame(uid, gamename);
                    } else return GameListing.createGame(uid);
                case "joinGame":
                    uid = obj.getInt("uid");
                    gid = obj.getInt("gid");
                    return GameListing.joinGame(uid, gid).put("fCall", "joinGame");

                case "sendGameMessage":
                    gid = obj.getInt("gid");
                    uid = obj.getInt("uid");
                    String msg = obj.getString("msg");
                    //Somehow send message
                    return sendGameMessage(uid, gid, msg);

                case "sendPublicMessage":
                    uid = obj.getInt("uid");
                    msg = obj.getString("msg");
                    return sendMessage(uid, msg);

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


    private JSONObject sendMessage(int uid, String message) {
        JSONObject obj = new JSONObject();
        obj.put("sentMessage", true);
        obj.put("message", message);
        obj.put("sender", uid);
        return obj;
    }

    private JSONObject sendGameMessage(int uid, int gid, String message) {
        //Get users in game gid
        //write message to all sockets of those users
        JSONObject obj = new JSONObject();
        obj.put("sentMessage", true);
        obj.put("message", message);
        obj.put("sender", uid);
        obj.put("game", gid);
        return obj;
    }



}
