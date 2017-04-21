package backend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

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
            System.err.println("Connected to client successfully");
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
        Socket sock = uidToSocket.get(uid);
        try {
            this.out.println(obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sendToPeople(JSONObject obj, ArrayList<Integer> uids) {
        for (int uid : uids) {
            Socket sock = uidToSocket.get(uid);
            try {
                PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
                out.println(obj.toString());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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

        System.err.println(obj);
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
                    break;

                case "registerUser": //Tested as of 4/15
                    uname = obj.getString("login");
                    pass = obj.getString("pass");
                    tempobj = GameListing.register(uname, pass);
                    tempobj.put("fCall", "registerResponse");
                    if (tempobj.getInt("returnValue") == GameListing.REGISTER_SUCCESS)
                        uidToSocket.put(tempobj.getInt("uid"), this.socket);
                    sendToUser(tempobj, tempobj.getInt("uid"));
                    break;

                case "userSubmits":
                    int uid = obj.getInt("uid");
                    int gid = obj.getInt("gid");
                    int c1 = obj.getInt("c1");
                    int c2 = obj.getInt("c2");
                    int c3 = obj.getInt("c3");
                    String username = Game.playerList.get(uid).getUsername();
                    tempobj = GameListing.getGame(gid).userSubmits(uid, c1, c2, c3).put("fCall", "userSubmitsResponse").put("username", username);
                    Map<Integer, Game> games = GameListing.getGames();
                    ArrayList<User> users = new ArrayList<>(games.get(gid).getPlayerList().values());
                    ArrayList<Integer> uids = new ArrayList<>();
                    int uid_temp;
                    for (User user : users) {
                        uid_temp = user.getUid();
                        uids.add(uid_temp);
                    }
                    sendToPeople(tempobj, uids);
                    break;

                case "createGame": //Tested as of 4/20
                    uid = obj.getInt("uid");
                    String gamename = obj.getString("gameName");
                    tempobj = GameListing.createGame(uid, gamename);
                    sendToUser(tempobj, uid);
                    break;

                case "joinGame":
                    uid = obj.getInt("uid");
                    gid = obj.getInt("gid");
                    tempobj = GameListing.joinGame(uid, gid).put("fCall", "joinGameResponse");
                    ArrayList<Socket> list = new ArrayList<>(uidToSocket.values());
                    sendToSockets(tempobj, list);
                    break;

                case "sendGameMessage":
                    gid = obj.getInt("gid");
                    uid = obj.getInt("uid");
                    String msg = obj.getString("msg");
                    tempobj = sendGameMessage(uid, gid, msg);
                    tempobj.put("fCall", "sendGameMessageResponse");
                    Map<Integer, Game> games = GameListing.getGames();
                    Game game = games.get(gid);
                    ArrayList<User> users2 = new ArrayList<>(game.get(gid).getPlayerList().values());
                    ArrayList<Integer> uids2 = new Arraylist<>();
                    for (int user : users2) {
                        uid_temp = user.getUid();
                        uids2.add(uid_temp);
                    }
                    sendToPeople(tempobj, uids2);
                    break;

                case "sendGlobalMessage":
                    uid = obj.getInt("uid");
                    msg = obj.getString("msg");
                    tempobj = sendMessage(uid, msg);
                    list = new ArrayList<>(uidToSocket.values());
                    tempobj.put("fCall", "sendGlobalMessageResponse");
                    sendToSockets(tempobj, list);
                    break;

                case "getGameListing":
                    uid = obj.getInt("uid");
                    tempobj = new JSONObject();
                    JSONArray temparr = new JSONArray();
                    ArrayList<Integer> gamesList = GameListing.getGamesList();
                    Map<Integer, Game> games = GameListing.getGames();
                    for (int gid_temp : gamesList) {
                        tempobj.put("gid", gid_temp);
                        tempobj.put("gameName", games.get(gid_temp).getGameName());

                        ArrayList<User> game_users = new ArrayList<>(games.get(gid_temp).getPlayerList().values());

                        for (int ii = 0; ii < 4; ii++) {
                            if (ii < game_users.size())
                                tempobj.put("username" + (ii + 1), game_users.get(ii));
                            else tempobj.put("username" + (ii + 1), "");
                        }
                        
                        temparr.put(tempobj);
                    }
                    JSONObject newobj = new JSONObject();
                    newobj.put("fCall", "getGameListingResponse");
                    newobj.put("gamesList", temparr);
                    sendToUser(newobj, uid);
                    break;

                case "leaveGame":
                    uid = obj.getInt("uid");
                    gid = obj.getInt("gid");
                    tempobj = GameListing.leaveGame(uid, gid);
                    tempobj.put("fCall", "leaveGameResponse");
                    sendToUser(tempobj, uid);
                    break;


                default:
                    retObj.put("error", 1);
                    //error
                    break;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
