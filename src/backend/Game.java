
package backend;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        board.initialize();
    }

    int getId() {
        return gid;
    }


    public Map<String, Integer> getScores() {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("1", 1);
        //Implement later
        return scores;
    }

    public int submit(int uid, int card1, int card2, int card3) {

        return 0;
    }

    public void removeFromGame(int uid) {
        if (listOfPlayers.containsKey(uid))
            listOfPlayers.remove(uid);
    }

    void addToGame(int uid, User user) {
        listOfPlayers.put(uid, user);
    }


}
