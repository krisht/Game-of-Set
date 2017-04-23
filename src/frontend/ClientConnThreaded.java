package frontend;

import jdk.nashorn.internal.scripts.JO;
import org.json.JSONArray;
import org.json.JSONObject;


import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import static frontend.LandingPage.*;
import static frontend.LandingPage.model;
import static frontend.LoginPage.uid;
import static frontend.LoginPage.landingPage;
import static frontend.LandingPage.gameName;
import static frontend.GameBoard_Front.list_of_cardids;
import static frontend.GameBoard_Front.list_of_users;
import static frontend.LandingPage.gb;
import static frontend.LoginPage.username;


public class ClientConnThreaded extends JFrame implements Runnable {



    final int GAME_DOES_NOT_EXIST = 1;
    final int GAME_FULL = 2;
    final int GENERAL_ERROR = -1;
    final int SUCCESS = 3;
    final int GAME_NAMAE_ALREADY_EXISTS = 4;

    private Thread t;
    private String threadName;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    public static ArrayList<GameListing> listofGames = new ArrayList<GameListing>();

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


    public void run() {
        JSONObject msg;
        String inString;
        try {

            while (true) {
                if ((inString = in.readLine()) != null) {
                    System.err.println("Received: " + inString);
                    try {
                        JSONObject data = new JSONObject(inString);
                        String fCall = data.getString("fCall");
                        switch (fCall) {
                            case "updateGameResponse":
                                JSONArray gameboard = data.getJSONObject("gameboard").getJSONArray("board");
                                JSONArray scoreboard_usernames = data.getJSONArray("scoreboard_usernames");
                                JSONArray scoreboard_scores = data.getJSONArray("scoreboard_scores");
                                gameName = data.getString("gamename");
                                list_of_cardids.clear();
                                list_of_users.clear();
                                for (int i = 0; i < gameboard.length(); i++) {
                                    list_of_cardids.add(gameboard.getInt(i));
                                }
                                for (int i = 0; i < scoreboard_scores.length(); i++) {
                                    Friends tempfriend = new Friends(scoreboard_usernames.getString(i), scoreboard_scores.getInt(i), 0);
                                    list_of_users.add(tempfriend);
                                }
                                gb.updateGameBoard();
                                gb.updateLeaderboard();
                                break;
                            case "joinGameResponse":
                                switch (data.getInt("returnValue")) {
                                    case GAME_DOES_NOT_EXIST:
                                        JOptionPane.showMessageDialog(null, "Game no longer exists. Please click refresh.", "Error", JOptionPane.ERROR_MESSAGE);
                                        gameName = "";
                                        gid = -1;
                                        break;
                                    case GAME_FULL:
                                        JOptionPane.showMessageDialog(null, "Game is already full.", "Error", JOptionPane.ERROR_MESSAGE);
                                        gameName = "";
                                        gid = -1;
                                        break;
                                    case SUCCESS:
                                        gid = data.getInt("gid");
                                        landingPage.enterGame();
                                        break;
                                }
                                landingPage.enterGame();
                                break;
                            case "createGameResponse":
                                switch (data.getInt("returnValue")) {
                                    case GENERAL_ERROR:
                                        break;
                                    case GAME_NAMAE_ALREADY_EXISTS:
                                        JOptionPane.showMessageDialog(null, "Game name is already taken. Please choose another name and try again.", "Error", JOptionPane.ERROR_MESSAGE);
                                        gameName = "";
                                        gid = -1;
                                        break;
                                    case SUCCESS:
                                        gid = data.getInt("gid");
                                    	landingPage.enterGame();
                                        break;
                                    default:
                                        break;
                                }
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
                                JSONArray gameList = data.getJSONArray("gamesList");
                                listofGames.clear();
                                for (int i = 0; i < gameList.length(); i++) {
                                    JSONObject gameitem = gameList.getJSONObject(i);
                                    listofGames.add(new GameListing(   gameitem.getInt("gid"),
                                                                        gameitem.getString("gameName"),
                                                                        gameitem.getString("username1"),
                                                                        gameitem.getString("username2"),
                                                                        gameitem.getString("username3"),
                                                                        gameitem.getString("username4")));
                                }
                                landingPage.makeGameListings();
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
            System.err.println("Sending: " + request);
        } catch (Exception ex) {
            System.err.println("Error: Could not send to server.");
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

    // This function requests the server for a list of games!
    public void requestupdateServerList() {
        JSONObject servupobj = new JSONObject();
        servupobj.put("fCall", "getGameListing");
        servupobj.put("uid", uid);
        try {
            messageServer(servupobj);
        } catch (Exception e){
        	e.printStackTrace();
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
                System.err.println(inobjString);
                JSONObject inobj = new JSONObject(inobjString);
                fCall = inobj.getString("fCall");
                if (fCall.equals("loginResponse")) {
                    uid = inobj.getInt("uid");
                    return inobj.getInt("returnValue");
                }
            }
        } catch (Exception e) {
            System.err.println("Error");
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
            System.err.println("Error");
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

    public void updateServerList() {
        model.clear();
        for (int i = 0; i < listofGames.size(); i++) {
            model.addElement(i + ". " + listofGames.get(i).getGname() + " - " + listofGames.get(i).getPlayer1() + ", " + listofGames.get(i).getPlayer2() + ", " + listofGames.get(i).getPlayer3() + ", " + listofGames.get(i).getPlayer4());
        }
    }
}


