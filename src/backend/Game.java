
package backend;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class Game {

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

        /**
         * Add try catch here to select and add games with users already there from DB
         */

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

    HashMap<Integer, User> getPlayerList() {
        return playerList;
    }

    JSONObject userSubmits(int uid, int c1, int c2, int c3) {
        JSONObject obj = gameBoard.processSubmission(c1, c2, c3);
        if (obj.getBoolean("setCorrect")) {
            User user = findPlayer(uid);
            if (user != null)
                user.incScore();
            obj.put("uid", uid);
            obj.put("scorechange", 1);
        }

        ArrayList<Integer> uids = new ArrayList<>();
        ArrayList<Integer> scores = new ArrayList<>();

        for (Map.Entry<Integer, User> entry : playerList.entrySet()) {
            uids.add(entry.getKey());
            scores.add(entry.getValue().getScore());
        }

        obj.put("scoreboard_uids", uids);
        obj.put("scoreboard_scores", scores);

        return obj;
    }

    JSONObject addToGame(int uid, User user) {
        playerList.put(uid, user);
        user.resetScore();
        JSONObject obj = new JSONObject();
        obj.put("addUser", true);
        return obj;
    }

    JSONObject kickUser(int uid) {
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

    @Override
    protected void finalize() throws Throwable {
        gameDB.DBClose();
        super.finalize();
    }
}
