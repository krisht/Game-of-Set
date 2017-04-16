
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


    /**
     * Constructor for Game class given a user defined gameName
     * @param gameName Game name for particular game instance
     */
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

    /**
     * Constructor for Game class to retrieve from Game database
     * in case of the need to recover stuff
     * @param gid Game ID in the database
     * @param gameName Game Name in the database
     */
    Game(int gid, String gameName) {
        this.gid = gid;
        this.gameName = gameName;

        /*
         Add try catch here to select and add games with users already there lol from DB
         Something like
         SELECT *
         FROM Users U, playsin P
         WHERE U.uid = P.uid AND P.gid = gid;
          */

    }

    /**
     * Test Bench Game Constructor
     */
    public Game(int gid) {
        this.gid = gid;
        this.gameName = "game" + gid;
        for (int ii = 1; ii <= 4; ii++)
            this.addToGame(ii, new User(ii, "user" + ii, ii + "user", this.gid));
        gameBoard.initialize();
    }

    /**
     * Game constructor for a game without no given name
     */
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

    /**
     * Gets gid of this Game
     * @return Integer representing game's id
     */
    int getGid() {
        return this.gid;
    }

    /**
     * Gets game name of this Game
     * @return String representing game ID
     */
    String getGameName() {
        return this.gameName;
    }

    /**
     * Gets list of players as a HashMap
     * @return HashMap of Integer to user objects
     */
    HashMap<Integer, User> getPlayerList() {
        return playerList;
    }

    /**
     * Allows a user with uid to submit their selected set
     *
     * @param uid Integer representing uid
     * @param c1  Integer representing id of card 1
     * @param c2  Integer representing id of card 2
     * @param c3  Integer representing id of card 3
     * @return JSONObject containing a data regarding board, submission etc.
     */
    JSONObject userSubmits(int uid, int c1, int c2, int c3) {
        JSONObject obj = gameBoard.processSubmission(c1, c2, c3);
        if (obj.getBoolean("setCorrect")) {
            User user = findPlayer(uid);
            if (user != null)
                user.incScore();
            obj.put("uid", uid);
            obj.put("scorechange", 1);
            /*
            Apply score change in database. or shall we do this when closing the game some how?
             */
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

    /**
     * Allows user to requestCards. Doesn't allow user to
     * request more than 3 cards at a time. Doesn't allow user
     * to request cards if more than 21 cards.
     * @return JSONObject with new board, replaced positions and other items
     */
    JSONObject requestCards() {
        return gameBoard.requestCards();
    }

    /**
     * Adds user with uid to game to playerList HashMap
     * @param uid Integer representing User
     * @param user User object representing User
     * @return JSONObject verifying that user was added
     */
    JSONObject addToGame(int uid, User user) {
        playerList.put(uid, user);
        user.resetScore();
        JSONObject obj = new JSONObject();
        obj.put("addUser", true);
        return obj;
    }

    /**
     * Kicks user out of game given a uid representing User
     *
     * @param uid Integer representing User with uid
     * @return JSONObject verifying that user was kicked
     */
    JSONObject kickUser(int uid) {
        playerList.remove(uid);
        JSONObject obj = new JSONObject();
        obj.put("kickUser", true);
        return obj;
    }

    /**
     * Method that finds player by uid in the HashMap
     *
     * @param uid Integer reresenting User with uid
     * @return User object representing uid
     */
    private User findPlayer(int uid) {
        if (playerList.containsKey(uid))
            return playerList.get(uid);
        return null;
    }

    /**
     * Overrides finalize to incorporate and make
     * sure that links to database are closed
     * @throws Throwable Throws in case of error on closing
     */
    @Override
    protected void finalize() throws Throwable {
        gameDB.DBClose();
        super.finalize();
    }
}
