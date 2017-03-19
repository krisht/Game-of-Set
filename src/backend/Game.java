
package backend;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Game {

    private DBComm gameDB = new DBComm();
    private int gid;
    private String gameName;
    private GameBoard board = new GameBoard();
    private HashMap<Integer, User> listOfPlayers = new HashMap<>();

    public Game(String gameName) {
        try {
            gameDB.DBInsert("INSERT INTO Game(gname) VALUES(" + gameName + ")");
            ResultSet set = gameDB.DBQuery("SELECT * FROM GAME");
            if (set.next()) {
                this.gid = set.getInt(1);
                this.gameName = gameName;
            } else throw new Exception("Game not inserted into database!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        board.initialize();
    }

    public Game() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        String gName = "game" + dateFormat.format(date);
        try {
            gameDB.DBInsert("INSERT INTO Game(gname) VALUES(" + gName + ")");
            ResultSet set = gameDB.DBQuery("SELECT * FROM GAME");
            if (set.next()) {
                this.gid = set.getInt(1);
                this.gameName = gName;
            }
            else throw new Exception("Game not inserted into database!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        board.initialize();
    }

    public JSONObject addToGame(int uid) {
        User

    }

    public JSONObject removeFromGame(int uid) {
        listOfPlayers.remove(uid);
        //Return JSONObject indicating player remove properly
    }

    public JSONObject getScoreOfPlayer(int uid) {

    }

    public JSONObject getScoreboard() {

    }

}
