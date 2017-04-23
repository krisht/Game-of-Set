package backend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;

import static backend.ServerConn.uidToSocket;

class ServerThread implements Runnable {

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
                    Game game = GameListing.getGame(gid);
                    String username = game.getPlayerList().get(uid).getUsername();
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
                    JSONObject tempobj10 = new JSONObject();
                    tempobj10 = GameListing.updateGame(uid, gid);
                    sendToPeople(tempobj, uids);
                    break;

                case "createGame": //Tested as of 4/20
                    uid = obj.getInt("uid");
                    String gamename = obj.getString("gameName");
                    tempobj = GameListing.createGame(uid, gamename);
                    tempobj.put("fCall", "createGameResponse");
                    sendToUser(tempobj, uid);
                    JSONObject tempobj5 = new JSONObject();
                    tempobj5 = GameListing.updateGame(uid, tempobj.getInt("gid"));
                    ArrayList<Integer> gameuids = new ArrayList<>();

                    JSONArray uidlist = tempobj5.getJSONArray("scoreboard_uids");
                    for (int i = 0; i < uidlist.length(); i++) {
                        gameuids.add(uidlist.getInt(i));
                    }

                    sendToPeople(tempobj5, gameuids);
                    break;

                case "joinGame":
                    uid = obj.getInt("uid");
                    gid = obj.getInt("gid");
                    tempobj = GameListing.joinGame(uid, gid).put("fCall", "joinGameResponse").put("uid", uid).put("gid", gid);
                    ArrayList<Socket> list = new ArrayList<>(uidToSocket.values());
                    sendToSockets(tempobj, list);
                    JSONObject tempobj6 = new JSONObject();
                    tempobj6 =  GameListing.updateGame(uid, gid);

                    ArrayList<Integer> gameuids2 = new ArrayList<>();

                    JSONArray uidlist2 = tempobj6.getJSONArray("scoreboard_uids");
                    for (int i = 0; i < uidlist2.length(); i++) {
                        gameuids2.add(uidlist2.getInt(i));
                    }
                    sendToPeople(tempobj6, gameuids2);
                    break;

                case "sendGameMessage":
                    gid = obj.getInt("gid");
                    uid = obj.getInt("uid");
                    String msg = obj.getString("msg");
                    tempobj = sendGameMessage(uid, gid, msg);
                    tempobj.put("fCall", "updateGameChat");
                    Map<Integer, Game> games2 = GameListing.getGames();
                    ArrayList<User> users2 = new ArrayList<>(games2.get(gid).getPlayerList().values());
                    ArrayList<Integer> uids2 = new ArrayList<>();
                    for (User user2 : users2) {
                        uid_temp = user2.getUid();
                        uids2.add(uid_temp);
                    }
                    sendToPeople(tempobj, uids2);
                    break;

                case "sendPublicMessage":
                    uid = obj.getInt("uid");
                    msg = obj.getString("msg");
                    tempobj = sendMessage(uid, msg);
                    list = new ArrayList<>(uidToSocket.values());
                    tempobj.put("fCall", "updatePublicChat");
                    sendToSockets(tempobj, list);
                    break;

                case "getGameListing":
                    uid = obj.getInt("uid");

                    JSONArray temparr = new JSONArray();
                    ArrayList<Integer> gamesList = GameListing.getGamesList();
                    Map<Integer, Game> games3 = GameListing.getGames();


                    for (int gid_temp : gamesList) {
                        tempobj = new JSONObject();
                        tempobj.put("gid", gid_temp);
                        tempobj.put("gameName", games3.get(gid_temp).getGameName());

                        ArrayList<User> game_users = new ArrayList<>(games3.get(gid_temp).getPlayerList().values());

                        for (int ii = 0; ii < 4; ii++) {
                            if (ii < game_users.size())
                                tempobj.put("username" + (ii + 1), game_users.get(ii).getUsername());
                            else tempobj.put("username" + (ii + 1), "");
                        }
                        temparr.put(tempobj);
                    }

                    JSONObject newobj = new JSONObject();
                    newobj.put("fCall", "getGameListingResponse");
                    newobj.put("gamesList", temparr);
                    sendToUser(newobj, uid);
                    break;

                case "noMoreSets":
                    uid = obj.getInt("uid");
                    gid = obj.getInt("gid");
                    JSONObject tempobj8 = new JSONObject();
                    int retval = GameListing.noMoreSets(uid, gid);
                    System.out.println("Retval is: " + retval);
                    JSONObject tempobj9 = new JSONObject();
                    tempobj9 = GameListing.updateGame(uid, gid);
                    ArrayList<Integer> gameuids3 = new ArrayList<>();

                    JSONArray uidlist5 = tempobj9.getJSONArray("scoreboard_uids");
                    for (int i = 0; i < uidlist5.length(); i++) {
                        gameuids3.add(uidlist5.getInt(i));
                    }
                    sendToPeople(tempobj9, gameuids3);
                    break;

                case "leaveGame":
                    uid = obj.getInt("uid");
                    gid = obj.getInt("gid");
                    tempobj = GameListing.leaveGame(uid, gid);
                    tempobj.put("fCall", "leaveGameResponse");

                    Map<Integer, Game> games5 = GameListing.getGames();
                    Game mygame = games5.get(gid);
                    User user = mygame.findPlayer(uid);
                    //Insert user overall score into DB
                    int currscore = user.getScore();
                    int overallscore = 0;
                    int dbscore = 0;
                    DBComm mycomms = new DBComm();
                    ResultSet myrs = mycomms.DBQuery("Select score from User where uid='" + uid + "';");
                    if (myrs != null && myrs.next()) {
                        dbscore = myrs.getInt("score");
                    }
                    overallscore = dbscore + currscore;
                    mycomms.DBInsert("UPDATE User SET score='" + overallscore + "' where uid='" + uid + "';");
                    sendToUser(tempobj, uid);
                    break;

                case "playerScore":
                    uid = obj.getInt("uid");
                    try {
                        DBComm mycomms2 = new DBComm();
                        ResultSet scorers = mycomms2.DBQuery("Select score from Users where uid='" + uid + "';");
                        dbscore = -1;
                        if (scorers != null && scorers.next()) {
                            dbscore = scorers.getInt("score");
                        }

                        JSONObject tempobj2 = new JSONObject();
                        tempobj2.put("score", dbscore);
                        tempobj2.put("fCall", "playerScoreResponse");
                        sendToUser(tempobj2, uid);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JSONObject tempobj3 = new JSONObject();
                        tempobj3.put("Error", "Error");
                        sendToUser(tempobj3, uid);
                    }
                    break;

                case "loggingOut":
                    Thread.dumpStack();
                    uid = obj.getInt("uid");
                    Socket tempSock = ServerConn.uidToSocket.get(uid);
                    ServerConn.uidToSocket.remove(uid);
                    ArrayList<Socket> sock = new ArrayList<>();
                    sock.add(tempSock);
                    tempobj = new JSONObject();
                    tempobj.put("fCall", "loggingOutResponse");
                    tempobj.put("loggedout", true);

                    sendToSockets(tempobj, sock);

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
