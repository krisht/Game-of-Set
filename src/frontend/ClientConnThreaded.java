package frontend;

import org.json.JSONArray;
import org.json.JSONObject;


import javax.swing.*;
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
import static frontend.LandingPage.lifetime_score;
import static frontend.LoginPage.landingPage;
import static frontend.LoginPage.username;

public class ClientConnThreaded extends JFrame implements Runnable {



    final int GAME_DOES_NOT_EXIST = 1;
    final int GAME_FULL = 2;
    final int GENERAL_ERROR = -1;
    final int SUCCESS = 3;

    private Thread t;
    private String threadName;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private GameListing[] listofGames = new GameListing[10];

    public ClientConnThreaded() {
        try {
            socket = new Socket("199.98.20.115", 5000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            System.err.println("Connected to host successfully!");

        } catch (IOException exc) {
            System.err.println("ERROR COMMUNICATING WITH SERVER");
            JOptionPane.showMessageDialog(null, "Error connecting to server.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        threadName = "Main Conn";

    }

    //public static void main(String[] args) throws Exception {
    //    ClientConn clientConn = new ClientConn(123, 456);
    // }


    public void run() {
        JSONObject msg;
        String inString;
        try {

            while (true) {
                if ((inString = in.readLine()) != null) {
                    System.out.println(inString);
                    try {
                        JSONObject data = new JSONObject(inString);
                        String fCall = data.getString("fCall");
                        switch (fCall) {
                            case "joinGameResponse":
                                switch (data.getInt("returnValue")) {
                                    case GENERAL_ERROR:
                                        break;
                                    case GAME_DOES_NOT_EXIST:
                                        break;
                                    case GAME_FULL:
                                        break;
                                    case SUCCESS:
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case "createGameResponse":
                                switch (data.getInt("returnValue")) {
                                    case GENERAL_ERROR:
                                        break;
                                    case SUCCESS:
                                        break;
                                    default:
                                        break;
                                }
                                gid = data.getInt("gid");
                                System.out.println("thread is working.");
                                break;
                            case "userSubmitsResponse":
                                if (data.getBoolean("setCorrect")){
                                    //DO STUFF
                                } else {
                                    //DO OTHER STUFF
                                    break;
                                }
                            case "loggingOutResponse":
                                //LOGOUT
                                break;
                            case "updatePublicChat":
                                updateChat(data.getString("username"), data.getString("msg"));
                                break;
                            case "updateLocalChat":
                                updateChat(data.getString("username"), data.getString("msg"));
                                break;
                            case "playerScoreResponse":
                            	lifetime_score = data.getInt("score");
                            	landingPage.reset_user_score();
                            	break;
                            case "getGameListingResponse":
                                System.out.println("test");
                                JSONArray gameList = data.getJSONArray("gamesList");
                                for (int i = 0; i < gameList.length(); i++) {
                                    System.out.println("tester" + i);
                                    JSONObject gameitem = gameList.getJSONObject(i);
                                    System.out.print(gameitem.getInt("gid") + gameitem.getString("gameName") + gameitem.getString("username1"));

                                    listofGames[i] = new GameListing(   gameitem.getInt("gid"),
                                                                        gameitem.getString("gameName"),
                                                                        gameitem.getString("username1"),
                                                                        gameitem.getString("username2"),
                                                                        gameitem.getString("username3"),
                                                                        gameitem.getString("username4"));
                                    System.out.println("test1");
                                    //ADD ITEM TO GAME BROWSER
                                }
                                updateServerList();
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e) {

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
            System.out.print("Sending: ");
            System.out.println(request);
        } catch (Exception ex) {
            System.err.println("Cannot be made into JSON Object");
        }
    }

    public void updateChat(String chatUserName, String chatMessage) {
        StringBuilder chatitem = new StringBuilder();
        chatitem.append(chatUserName);
        chatitem.append(": ");
        chatitem.append(chatMessage);
        chatitem.append("\n");
        chatlogarea.append(chatitem.toString());
    }

    public void updateServerList() {
        model.clear();
        for (int i = 0; i < listofGames.length; i++) {
            model.addElement(i + ". " + listofGames[i].getGname() + " - " + listofGames[i].getPlayer1() + ", " + listofGames[i].getPlayer2() + ", " + listofGames[i].getPlayer3() + ", " + listofGames[i].getPlayer4());
        }
    }

    public void requestupdateServerList() {
        JSONObject servupobj = new JSONObject();
        servupobj.put("fCall", "getGameListing");
        servupobj.put("uid", uid);
        try {
            messageServer(servupobj);
        } catch (Exception e){

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

    public void getUserScore(){
    	JSONObject userscoreobj = new JSONObject();
    	userscoreobj.put("fCall", "playerScore");
    	userscoreobj.put("uid", uid);
    	try{
    		messageServer(userscoreobj);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
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
