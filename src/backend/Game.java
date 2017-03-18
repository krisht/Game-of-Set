
package backend;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.HashMap;

public class Game {

    private DBComm gameDB = new DBComm();
    private int gid;
    private String gameName;
    private GameBoard board = new GameBoard();
    private HashMap<Integer, User> listOfPlayers = new HashMap<>();

    public Game() throws Exception {

        try {
            gameDB.DBInsert("INSERT INTO TABLE Game() VALUES ()");
            ResultSet set = gameDB.DBQuery("SELECT * FROM GAME");
            if (set.next())
                gid = set.getInt(1);
            else throw new Exception("Game not inserted into database!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        gameName = "game" + gid;

        board.initialize();
    }

    public Game(String gameName) throws Exception {
        this();
        this.gameName = gameName;
    }

    public JSONObject addToGame(int uid) {

    }

    public JSONObject removeFromGame(int uid) {

    }

    public JSONObject getScoreOfPlayer(int uid) {

    }

    public JSONObject getScoreboard() {

    }

}
