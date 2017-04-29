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
                        System.out.println("Server received"  +  inString);
                        JSONObject obj = new JSONObject(inString);
                        processData(obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception ex) {
            Thread.currentThread().interrupt();
        }
    }

    void start() {
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }

    private void sendToUser(JSONObject obj) {
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
                    String uName = obj.getString("login");
                    String pass = obj.getString("pass");
                    JSONObject tempObj = GameListing.login(uName, pass);
                    if (tempObj.getInt("returnValue") == GameListing.LOGIN_SUCCESS)
                        uidToSocket.put(tempObj.getInt("uid"), this.socket);

                    tempObj.put("fCall", "loginResponse");
                    sendToUser(tempObj); //update uid
                    break;

                case "registerUser": //Tested as of 4/15
                    uName = obj.getString("login");
                    pass = obj.getString("pass");
                    tempObj = GameListing.register(uName, pass);
                    tempObj.put("fCall", "registerResponse");
                    if (tempObj.getInt("returnValue") == GameListing.REGISTER_SUCCESS)
                        uidToSocket.put(tempObj.getInt("uid"), this.socket);
                    sendToUser(tempObj);
                    break;

                case "userSubmits":
                    int uid = obj.getInt("uid");
                    int gid = obj.getInt("gid");
                    int c1 = obj.getInt("c1");
                    int c2 = obj.getInt("c2");
                    int c3 = obj.getInt("c3");
                    GameListing.resetNoMoreSets(gid);
                    Game game = GameListing.getGame(gid);
                    String username = game.getPlayerList().get(uid).getUsername();
                    tempObj = GameListing.getGame(gid).userSubmits(uid, c1, c2, c3).put("fCall", "userSubmitsResponse").put("username", username);
                    Map<Integer, Game> games = GameListing.getGames();
                    ArrayList<User> users = new ArrayList<>(games.get(gid).getPlayerList().values());
                    ArrayList<Integer> uids = new ArrayList<>();
                    int uid_temp;
                    for (User user : users) {
                        uid_temp = user.getUid();
                        uids.add(uid_temp);
                    }
                    sendToPeople(tempObj, uids);
                    JSONObject tempObj10 = GameListing.updateGame(uid, gid);
                    sendToPeople(tempObj10, uids);

                    if(GameListing.checkGameOver(gid)){
                        //Return shit
                    }
                    break;

                case "createGame": //Tested as of 4/20
                    uid = obj.getInt("uid");
                    String gameName = obj.getString("gameName");
                    tempObj = GameListing.createGame(uid, gameName);
                    sendToUser(tempObj);

                    if(tempObj != null) {
                        JSONObject tempObj5 = GameListing.updateGame(uid, tempObj.getInt("gid"));
                        ArrayList<Integer> gameuids = new ArrayList<>();

                        JSONArray uidlist = tempObj5.getJSONArray("scoreboard_uids");
                        for (int i = 0; i < uidlist.length(); i++) {
                            gameuids.add(uidlist.getInt(i));
                        }

                        sendToPeople(tempObj5, gameuids);
                    }
                    break;

                case "joinGame":
                    uid = obj.getInt("uid");
                    gid = obj.getInt("gid");
                    if(GameListing.getGame(gid).getPlayerList().containsKey(uid)) //User is already in the game
                        break;
                    tempObj = GameListing.joinGame(uid, gid).put("fCall", "joinGameResponse").put("uid", uid).put("gid", gid);
                    ArrayList<Socket> list = new ArrayList<>(uidToSocket.values());
                    sendToSockets(tempObj, list);
                    JSONObject tempObj6 = GameListing.updateGame(uid, gid);

                    ArrayList<Integer> gameuids2 = new ArrayList<>();

                    JSONArray uidlist2 = tempObj6.getJSONArray("scoreboard_uids");
                    for (int i = 0; i < uidlist2.length(); i++) {
                        gameuids2.add(uidlist2.getInt(i));
                    }
                    sendToPeople(tempObj6, gameuids2);

                    if(GameListing.checkGameOver(gid)){
                        //Return shit
                    }
                    break;

                case "sendGameMessage":
                    gid = obj.getInt("gid");
                    uid = obj.getInt("uid");
                    String msg = obj.getString("msg");
                    tempObj = sendGameMessage(uid, gid, msg);
                    tempObj.put("fCall", "updateGameChat");
                    Map<Integer, Game> games2 = GameListing.getGames();
                    ArrayList<User> users2 = new ArrayList<>(games2.get(gid).getPlayerList().values());
                    ArrayList<Integer> uids2 = new ArrayList<>();
                    for (User user2 : users2) {
                        uid_temp = user2.getUid();
                        uids2.add(uid_temp);
                    }
                    sendToPeople(tempObj, uids2);
                    break;

                case "sendPublicMessage":
                    uid = obj.getInt("uid");
                    msg = obj.getString("msg");
                    tempObj = sendMessage(uid, msg);
                    list = new ArrayList<>(uidToSocket.values());
                    tempObj.put("fCall", "updatePublicChat");
                    sendToSockets(tempObj, list);
                    break;

                case "getGameListing":
                    JSONArray temparr = new JSONArray();
                    ArrayList<Integer> gamesList = GameListing.getGamesList();
                    Map<Integer, Game> games3 = GameListing.getGames();


                    for (int gid_temp : gamesList) {
                        tempObj = new JSONObject();
                        tempObj.put("gid", gid_temp);
                        tempObj.put("gameName", games3.get(gid_temp).getGameName()); //Null pointer when trying to get games
                        /*
                         *
                         * Connected to client successfully
                         * PlayerScore
                         * java.lang.NullPointerException
                         *  at backend.ServerThread.processData(ServerThread.java:225)
                         *  at backend.ServerThread.run(ServerThread.java:47)
                         *  at java.lang.Thread.run(Thread.java:745)
                         *
                         *
                         */

                        ArrayList<User> game_users = new ArrayList<>(games3.get(gid_temp).getPlayerList().values());

                        for (int ii = 0; ii < 4; ii++) {
                            if (ii < game_users.size())
                                tempObj.put("username" + (ii + 1), game_users.get(ii).getUsername());
                            else tempObj.put("username" + (ii + 1), "");
                        }
                        temparr.put(tempObj);
                    }

                    JSONObject newobj = new JSONObject();
                    newobj.put("fCall", "getGameListingResponse");
                    newobj.put("gamesList", temparr);
                    sendToUser(newobj);
                    break;

                case "noMoreSets":
                    uid = obj.getInt("uid");
                    gid = obj.getInt("gid");
                    int retVal = GameListing.noMoreSets(uid, gid);
                    System.out.println("Retval is: " + retVal);
                    games = GameListing.getGames();
                    users = new ArrayList<>(games.get(gid).getPlayerList().values());
                    if (retVal == 1)
                        for (User user : users)
                            user.setNoMoreSetsOff();

                    tempObj = GameListing.updateGame(uid, gid);
                    ArrayList<Integer> guids = new ArrayList<>();


                    JSONArray uidList = tempObj.getJSONArray("scoreboard_uids");
                    for (int i = 0; i < uidList.length(); i++)
                        guids.add(uidList.getInt(i));
                    sendToPeople(tempObj, guids);

                    if(GameListing.checkGameOver(gid)){
                        //Return shit
                    }
                    break;

                case "leaveGame":
                    uid = obj.getInt("uid");
                    gid = obj.getInt("gid");
                    tempObj = GameListing.leaveGame(uid, gid);
                    tempObj.put("fCall", "leaveGameResponse");
                    tempObj.put("uid", uid);
                    sendToUser(tempObj);

                    JSONObject tempObj5 = GameListing.updateGame(uid, gid);
                    ArrayList<Integer> gameuids = new ArrayList<>();

                    JSONArray uidlist = tempObj5.getJSONArray("scoreboard_uids");
                    for (int i = 0; i < uidlist.length(); i++)
                        gameuids.add(uidlist.getInt(i));

                    sendToPeople(tempObj5, gameuids);

                    break;

                case "playerScore":
                    uid = obj.getInt("uid");
                    System.out.println("PlayerScore");
                    sendToUser(GameListing.getPlayerScore(uid));
                    break;

                case "loggingOut":
                    uid = obj.getInt("uid");
                    Socket tempSock = ServerConn.uidToSocket.get(uid);
                    ServerConn.uidToSocket.remove(uid);
                    ArrayList<Socket> sock = new ArrayList<>();
                    sock.add(tempSock);
                    tempObj = new JSONObject();
                    tempObj.put("fCall", "loggingOutResponse");
                    tempObj.put("loggedout", true);

                    sendToSockets(tempObj, sock);
                    //close input and output stream
                    in.close();
                    out.close();
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
        User user = GameListing.getUsers().get(uid);
        obj.put("username", user.getUsername());
        obj.put("msg", message);
        return obj;
    }

    private JSONObject sendGameMessage(int uid, int gid, String message) {
        JSONObject obj = new JSONObject();
        User user = GameListing.getUsers().get(uid);
        obj.put("username", user.getUsername());
        obj.put("msg", message);
        obj.put("gid", gid);
        return obj;
    }


}
