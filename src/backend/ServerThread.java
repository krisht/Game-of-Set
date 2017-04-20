package backend;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import static backend.ServerConn.gidToUid;
import static backend.ServerConn.uidToSocket;

public class ServerThread implements Runnable {

    private Thread t;
    private String threadName;

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
        threadName = "thread";
    }

    public void run() {
        String inString;
        try {
            while (true) {
                if ((inString = in.readLine()) != null) {
                    try {
                        JSONObject obj = new JSONObject(inString);
                        processData(obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void start() {
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }

    private void sendToUser(JSONObject obj, int uid) {
        Socket sock = new Socket(); //get socket from the ServerConn
        try {
            PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void sendToSockets(JSONObject obj, ArrayList<Socket> socks) {

        for (Socket sock : socks) {
            try {
                PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
                out.println(obj.toString());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    //change this to void and make sure it writes to the right sockets
    private void processData(JSONObject obj) {

        System.out.println(obj);
        JSONObject retObj = new JSONObject();

        if (obj.length() == 0)
            return;

        try {
            String fCall = obj.getString("fCall");
            switch (fCall) {
                case "loginUser": //Tested as of 4/15
                    String uname = obj.getString("login");
                    String pass = obj.getString("pass");
                    JSONObject tempobj = GameListing.login(uname, pass);
                    if (tempobj.getInt("returnValue") == GameListing.LOGIN_SUCCESS)
                        uidToSocket.put(tempobj.getInt("uid"), this.socket);

                    tempobj.put("fCall", "loginResponse");
                    sendToUser(tempobj, tempobj.getInt("uid")); //update uid

                case "registerUser": //Tested as of 4/15
                    uname = obj.getString("login");
                    pass = obj.getString("pass");
                    tempobj = GameListing.register(uname, pass, "");
                    tempobj.put("fCall", "registerResponse");
                    if (tempobj.getInt("returnValue") == GameListing.REGISTER_SUCCESS)
                        uidToSocket.put(tempobj.getInt("uid"), this.socket);
                    sendToUser(tempobj, tempobj.getInt("uid"));

                case "userSubmits":
                    int uid = obj.getInt("uid");
                    int gid = obj.getInt("gid");
                    int c1 = obj.getInt("c1");
                    int c2 = obj.getInt("c2");
                    int c3 = obj.getInt("c3");
                    tempobj = GameListing.getGame(gid).userSubmits(uid, c1, c2, c3).put("fCall", "userSubmits");
                    sendToSockets(tempobj, gidToUid.get(gid));

                case "createGame":
                    uid = obj.getInt("uid");
                    String gamename = obj.getString("gamename");
                    tempobj = GameListing.createGame(uid, gamename);
                    if (tempobj.get)
                        sendToSockets(tempobj, uidToSocket.keySet());
                case "joinGame":
                    uid = obj.getInt("uid");
                    gid = obj.getInt("gid");
                    tempobj = GameListing.joinGame(uid, gid).put("fCall", "joinGame");
                    sendToSockets(tempobj, uidToSocket.keySet())

                case "sendGameMessage":
                    gid = obj.getInt("gid");
                    uid = obj.getInt("uid");
                    String msg = obj.getString("msg");
                    tempobj = sendGameMessage(uid, gid, msg)
                    //Somehow send message
                    sendToSockets(tempobj, gidToUid.get(gid));

                case "sendPublicMessage":
                    uid = obj.getInt("uid");
                    msg = obj.getString("msg");
                    tempobj = sendMessage(uid, msg);
                    sendToSockets(tempobj, uidToSocket.keySet())

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
