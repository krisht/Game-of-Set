package frontend;

import javax.swing.JOptionPane;

import org.json.JSONObject;

public class Main {
	final int LOGIN = 1;
	final int LANDING = 2;
	final int GAMEBOARD_FRONT = 3;

    public static void main (String arg[]){
        try {
            GameBoard_Front front = new GameBoard_Front();
            front.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    // send things to backend, with the JSON object and whatever page i am on etc.
    public void sendToBE(int location, JSONObject object){
    	
    }
    
    // send things from backend to front end, with the JSON object and whatever 
    
    public void receiveFromBE(int location, JSONObject object){
    	
    }
}
