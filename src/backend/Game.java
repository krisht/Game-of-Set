
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
    private GameBoard gameBoard = new GameBoard();
    private HashMap<Integer, User> playerList = new HashMap<>();

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
        gameBoard.initialize();
    }

    public Game() {
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
            } else throw new Exception("Game not inserted into database!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        gameBoard.initialize();
    }

    private DBComm getGameDB() {
        return this.gameDB;
    }

    public int getGid() {
        return this.gid;
    }

    public String getGameName() {
        return this.gameName;
    }

    public GameBoard getGameBoard() {
        return this.gameBoard;
    }

    public HashMap<Integer, User> getPlayerList() {
        return playerList;
    }

    public JSONObject userSubmits(int uid, int c1, int c2, int c3) {

        return null;

    }

    public HashMap<String, Integer> getScoreBoard() {

        return null;

    }

    public JSONObject getScore() {

        return null;

    }

    public JSONObject removeFromScoreBoard(int uid) {
        JSONObject obj = new JSONObject();
        obj.put("removeFromScoreBoard", uid);
        return obj;
    }

    public JSONObject addToScoreBoard(int uid) {
        JSONObject obj = new JSONObject();
        obj.put("addToScoreBoard", true);
        return obj;
    }

    public JSONObject addToGame(int uid, User user) {
        playerList.put(uid, user);
        user.resetScore();
        JSONObject obj = new JSONObject();
        obj.put("addUser", true);
        return obj;
    }

    public JSONObject kickUser(int uid) {
        playerList.remove(uid);
        JSONObject obj = new JSONObject();
        obj.put("kickUser", true);
        return obj;
    }

    private User findPlayer(int uid) {
        if (playerList.containsKey(uid))
            return playerList.get(uid);
        return null;
    }

}
