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

    }

    //public static void main(String[] args) throws Exception {
    //    ClientConn clientConn = new ClientConn(123, 456);
    // }
	
		
	public void run() {
		JSONObject msg;
		try {
            inString = in.readLine();
			while (inString != null) {
				if (inString.equals("endComms")) {
					break;
				} else {
                    JSONObject data = new JSONObject(inString);
                    //Process data here
                }
				//PARSE INPUT JSON AND DO WHAT NEEDS TO BE DONE
			}
		}
		catch (Exception e) {
		}
	}
	
	public void start() {
		if (t == null) {
			t = new Thread (this, threadName);
			t.start();
		}
	}

    public JSONObject messageServer(JSONObject obj) throws Exception {
        String request = obj.toString();
        this.out.println(request);
        try {
            String check = in.readLine();
            //System.err.println("RETURNED DATA: " + check);
            JSONObject retObj = new JSONObject(check);
            String fCall = retObj.getString("function");
            switch (fCall) {
            	case "GameBoard.initialize":
            		System.out.println(retObj.toString());
            		break;
                default:
                    retObj.put("error", 1);
                    //error
                    break;
            }
            return retObj;
        } catch (Exception ex) {
            System.err.println("Cannot be made into JSON Object");
            return null;
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
	
	public void updateChat (String chatUserName, String chatMessage) {
		StringBuilder chatitem = new StringBuilder();
		chatitem.append(chatUserName);
		chatitem.append(": ");
		chatitem.append(chatMessage);
		chatlogarea.append(chatitem.toString());
	}

	public void updateServerList (GameListing[] listOfGames) {
        for (int i = 0; i < listOfGames.length; i++) {
            model.clear();
            model.addElement (i + ". " + listOfGames[i].getGname() + " - " + listOfGames[i].getNumplayers() + "/4");
        }
    }
}
