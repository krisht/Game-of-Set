package backend;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread implements Runnable {

    private Thread t;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;


    ServerThread(Socket sock) {

        try {
            this.socket = sock;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            System.err.println("Connected to client successfully?");

        } catch (IOException exc) {
            exc.printStackTrace();
        }

    }

    public void run() {
        String inString;
        try {
            while (true) {
                if ((inString = in.readLine()) != null) {
                    JSONObject obj = new JSONObject(inString);
                    processData(obj);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }

    void start() {
        if (t == null) {
            t = new Thread(this, socket.toString());
            t.start();
        }
    }


    //change this to void and make sure it writes to the right sockets
    private JSONObject processData(JSONObject obj) {

        System.out.println(obj);
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
                    JSONObject tempobj = GameListing.login(uname, pass);
                    tempobj.put("fCall", "loginResponse");
                    return tempobj;

                case "registerUser": //Tested as of 4/15
                    uname = obj.getString("login");
                    pass = obj.getString("pass");
                    tempobj = GameListing.register(uname, pass, "");
                    tempobj.put("fCall", "registerResponse");
                    return tempobj;

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
                    } else return GameListing.createGame(uid, "");
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
