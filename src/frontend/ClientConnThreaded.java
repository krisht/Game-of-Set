package frontend;

import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArraySet;

import static frontend.LandingPage.chatlogarea;
import static frontend.LandingPage.serverlistpane;
import static frontend.LandingPage.model;

import static frontend.LoginPage.uid;
import static frontend.LandingPage.gid;

public class ClientConnThreaded implements Runnable {

    final int USER_NOT_EXIST = 1;
    final int PWD_INCORRECT = 2;
    final int USER_ALREADY_EXIST = 3;
    final int LOGIN_SUCCESS = 4;
    final int REGISTER_SUCCESS = 5;

    private Thread t;
    private String threadName;

    private static Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String inString;

    public ClientConnThreaded() {
        try {
            socket = new Socket("199.98.20.115", 5000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            System.err.println("Connected to host successfully!");

        } catch (IOException exc) {
            System.err.println("ERROR COMMUNICATING WITH SERVER");
        }
        threadName = "Main Conn";

    }

    //public static void main(String[] args) throws Exception {
    //    ClientConn clientConn = new ClientConn(123, 456);
    // }


    public void run() {
        JSONObject msg;
        try {

            while ((inString = in.readLine()) != null) {
                if (inString.equals("endComms")) {
                    break;
                } else {
                    JSONObject data = new JSONObject(inString);
                    String fCall = data.getString("fCall");
                    switch (fCall) {
                        case "GameBoard.initialize":
                            System.out.println(data.toString());
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void start() {
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }

    public void messageServer(JSONObject obj) throws Exception {

        try {
            String request = obj.toString();
            this.out.println(request);
        } catch (Exception ex) {
            System.err.println("Cannot be made into JSON Object");
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

    public void updateChat(String chatUserName, String chatMessage) {
        StringBuilder chatitem = new StringBuilder();
        chatitem.append(chatUserName);
        chatitem.append(": ");
        chatitem.append(chatMessage);
        chatlogarea.append(chatitem.toString());
    }

    public void updateServerList(GameListing[] listOfGames) {
        for (int i = 0; i < listOfGames.length; i++) {
            model.clear();
            model.addElement(i + ". " + listOfGames[i].getGname() + " - " + listOfGames[i].getNumplayers() + "/4");
        }
    }

    public int loginCall(String username, String password) {
        JSONObject obj = new JSONObject();
        int returnval = 0;

        obj.put("fCall", "loginUser");
        obj.put("login", username);
        obj.put("pass", password);
        try {
            messageServer(obj);
        } catch (Exception e) {
            returnval = -1; //some error
        }
        try {
            String fCall = "";
            String inobjString;
            while ((inobjString = in.readLine()) != null) {
                System.out.println(inobjString);
                JSONObject inobj = new JSONObject(inobjString);

                fCall = inobj.getString("fCall");
                System.out.println("test2");
                if (fCall.equals("loginResponse")) {
                    System.out.println("test3");
                    uid = inobj.getInt("uid");
                    return inobj.getInt("returnValue");
                } else {
                    returnval = -1;
                }
            }
        } catch (Exception e) {
            System.out.println("Error");
            returnval = -1;
        }
        System.out.println(returnval);
        return returnval;
    }
}
