package frontend;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import static frontend.GameBoard_Front.*;
import static frontend.LandingPage.*;
import static frontend.LoginPage.*;


public class ClientConnThreaded extends JFrame implements Runnable {


    public static ArrayList<GameListing> listofGames = new ArrayList<GameListing>();
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

    public ClientConnThreaded() {
        try {
            socket = new Socket("199.98.20.122", 5000);
            //socket = new Socket("127.0.0.1", 5000);
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
                                if (data.getInt("gid") == gid) {
                                    JSONArray gameboard = data.getJSONObject("gameboard").getJSONArray("board");
                                    JSONArray scoreboard_usernames = data.getJSONArray("scoreboard_usernames");
                                    JSONArray scoreboard_scores = data.getJSONArray("scoreboard_scores");
                                    JSONArray scoreboard_nomoresets = data.getJSONArray("nomoresets");
                                    gameName = data.getString("gamename");
                                    list_of_cardids.clear();
                                    list_of_users.clear();
                                    for (int i = 0; i < gameboard.length(); i++) {
                                        list_of_cardids.add(gameboard.getInt(i));
                                    }
                                    for (int i = 0; i < scoreboard_scores.length(); i++) {
                                        Friends tempfriend = new Friends(scoreboard_usernames.getString(i), scoreboard_scores.getInt(i), scoreboard_nomoresets.getInt(i));
                                        list_of_users.add(tempfriend);
                                    }
                                    gb.updateGameBoard();
                                    gb.updateLeaderboard();
                                }
                                break;
                            case "joinGameResponse":
                                if (data.getInt("uid") == uid) {
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
                                        case GENERAL_ERROR:
                                            JOptionPane.showMessageDialog(null, "Could not process Join Game request. Please Refresh and try again.", "Error", JOptionPane.ERROR_MESSAGE);
                                            gameName = "";
                                            gid = -1;
                                            break;
                                    }
                                } else {
                                    System.err.println("New player has joined.");
                                }
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
                                    	posinlist = 0;
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case "userSubmitsResponse":
                                switch (data.getInt("returnValue")) {
                                    case 0:
                                        if (data.getInt("uid") == uid) {
                                            JOptionPane.showMessageDialog(null, "Not a set. Sorry. Sucks to suck.", "Error", JOptionPane.ERROR_MESSAGE);
                                            gb.resetBorders();
                                        }
                                        break;
                                    case 1:
                                        if (data.getInt("uid") == uid) {
                                            JOptionPane.showMessageDialog(null, "You made a set, you bloody genius.", "YAY!!!", JOptionPane.PLAIN_MESSAGE);
                                         } else {
                                            JOptionPane.showMessageDialog(null, "Someone scored.", "Bleh!!!", JOptionPane.PLAIN_MESSAGE);
                                        }
                                        gb.resetBorders();
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case "loggingOutResponse":
                                System.exit(0);
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
                            case "leaveGameResponse":
                                if (data.getInt("uid") == uid) {
                                    if (data.getInt("returnValue") == 1) {
                                        StringBuilder leavemsg = new StringBuilder();
                                        try {
                                            for (int i = 0; i < list_of_users.size(); i++) {
                                                if (list_of_users.get(i).getName().equals(username)){
                                                    posinlist = i;
                                                    break;
                                                }
                                            }
                                            leavemsg.append("Leaving game with a final score of ");
                                            leavemsg.append(list_of_users.get(posinlist).getScore());
                                            JOptionPane.showMessageDialog(null, leavemsg, "YAY!!!", JOptionPane.PLAIN_MESSAGE);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        gb.returnToLanding();
                                        gid = -1;
                                        landingPage.getUserScore();
                                        landingPage.requestupdateServerList();
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Error leaving the game. Please try again.", "NAY!!!", JOptionPane.ERROR_MESSAGE);
                                }
                                break;
                            case "gameOverResponse":
                                if (data.getInt("gid") == gid) {
                                    StringBuilder gameovermsg = new StringBuilder();
                                    gameovermsg.append("Game is over!\n");
                                    ArrayList<Integer> winpos = new ArrayList<Integer>();
                                    int winscore = 0;
                                    int selfpos = -1;
                                    for (int i = 0; i < list_of_users.size(); i++) {
                                        //gameovermsg.append(list_of_users.get(i).getName());
                                        //gameovermsg.append(": ");
                                        //gameovermsg.append(list_of_users.get(i).getScore());
                                        //gameovermsg.append("\n");
                                        if (list_of_users.get(i).getScore() == winscore) {
                                            winpos.add(i);
                                        } else if (list_of_users.get(i).getScore() > winscore) {
                                            winpos.clear();
                                            winpos.add(i);
                                            winscore = list_of_users.get(i).getScore();
                                        }
                                        if (list_of_users.get(i).getName().equals(username)){
                                            selfpos = i;
                                        }
                                    }
                                    if (winpos.size() > 1) {
                                        gameovermsg.append("It's a draw between ");
                                        for (int i = 0; i < winpos.size() - 1; i++) {
                                            gameovermsg.append(list_of_users.get(winpos.get(i)).getName());
                                            gameovermsg.append(", ");
                                        }
                                        gameovermsg.append("and ");
                                        gameovermsg.append(list_of_users.get(winpos.get(winpos.size() - 1)).getName());
                                        gameovermsg.append(" with a final score of ");
                                        gameovermsg.append(winscore);
                                    } else {
                                        gameovermsg.append(list_of_users.get(winpos.get(0)).getName());
                                        gameovermsg.append(" won the game with a score of ");
                                        gameovermsg.append(winscore);
                                        gameovermsg.append("!");
                                    }
                                    JOptionPane.showMessageDialog(gb, gameovermsg, "YAY!!!", JOptionPane.PLAIN_MESSAGE);
                                }
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
        chatlogarea.setCaretPosition(chatlogarea.getDocument().getLength());
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
                System.err.println("Received: " + inobjString);
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

}


