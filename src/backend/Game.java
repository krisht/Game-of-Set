
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

    Game(String gameName) {
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

    Game(int gid, String gameName) {
        this.gid = gid;
        this.gameName = gameName;

        try {
            //gameDB.DBQuery("SELECT P.uid FROM ");
            //What the carp should go in here if anything?
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    Game() {
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

    int getGid() {
        return this.gid;
    }

    String getGameName() {
        return this.gameName;
    }

    GameBoard getGameBoard() {
        return this.gameBoard;
    }

    HashMap<Integer, User> getPlayerList() {
        return playerList;
    }

    JSONObject userSubmits(int uid, int c1, int c2, int c3) {
        JSONObject obj = gameBoard.processSubmission(c1, c2, c3);
        if (obj.getBoolean("setCorrect"))
            findPlayer(uid).addScore(1);
        return obj;
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
