package frontend;

import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static frontend.LandingPage.chatlogarea;
import static frontend.LandingPage.serverlistpane;
import static frontend.LandingPage.model;

import static frontend.LoginPage.uid;
import static frontend.LandingPage.gid;

public class ClientConnThreaded implements Runnable {

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

            while (true) {
                if ((inString = in.readLine()) != null) {
                    JSONObject data = new JSONObject(inString);
                    String fCall = data.getString("fCall");
                    switch (fCall) {
                        case "GameBoard.initialize":
                            System.out.println(data.toString());
                            break;
                        case "createGameResponse":
                            System.out.println("thread is working.");
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

    public int loginUser(String username, String password) {
        JSONObject obj = new JSONObject();

        obj.put("fCall", "loginUser");
        obj.put("login", username);
        obj.put("pass", password);
        try {
            messageServer(obj);
        } catch (Exception e) {
            return -1; //some error
        }
        try {
            String fCall = "";
            String inobjString;
            while ((inobjString = in.readLine()) != null) {
                System.out.println(inobjString);
                JSONObject inobj = new JSONObject(inobjString);
                fCall = inobj.getString("fCall");
                if (fCall.equals("loginResponse")) {
                    uid = inobj.getInt("uid");
                    return inobj.getInt("returnValue");
                }
            }
        } catch (Exception e) {
            System.out.println("Error");
            return -1;
        }
        return 0;
    }

    public int registerUser(String username, String password) {
        JSONObject obj = new JSONObject();

        obj.put("fCall", "registerUser");
        obj.put("login", username);
        obj.put("pass", password);
        try {
            messageServer(obj);
        } catch (Exception e) {
            return -1; //some error
        }
        try {
            String fCall = "";
            String inobjString;
            while ((inobjString = in.readLine()) != null) {
                JSONObject inobj = new JSONObject(inobjString);
                fCall = inobj.getString("fCall");
                if (fCall.equals("registerResponse")) {
                    uid = inobj.getInt("uid");
                    return inobj.getInt("returnValue");
                }
            }
        } catch (Exception e) {
            System.out.println("Error");
            return -1;
        }
        return 0;
    }
    
    // This function handles user submission of sets to the server
    public int userSubmission(int c1, int c2, int c3){
    	JSONObject submitJson = new JSONObject();
		submitJson.put("fCall", "userSubmits");
		submitJson.put("uid", uid);
		submitJson.put("gid", gid);
		submitJson.put("c1", c1);
		submitJson.put("c2", c2);
		submitJson.put("c3", c3);
		return 0;
    }
}
