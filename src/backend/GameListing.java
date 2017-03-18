package backend;

import org.json.JSONObject;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by krishna on 3/18/17.
 */
public class GameListing {

    public static ConcurrentHashMap<Integer, Game> gamesList = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Integer, User> usersList = new ConcurrentHashMap<>();


    /**
     * Signs user into game lobby
     *
     * @param uname
     * @param pass
     * @return
     */
    public static JSONObject login(String uname, String pass) {

    }

    /**
     * Registers user into game lobby if they don't have a username and pass
     *
     * @param uname
     * @param pass
     * @return
     */
    public static JSONObject register(String uname, String pass) {

    }

    /**
     * Returns list of players for FE to display if necessary
     *
     * @return
     */
    public static JSONObject getPlayers() {

    }

    /**
     * Returns list of games for FE to display if necessary
     *
     * @return
     */
    public static JSONObject getGames() {

    }

    /**
     * Joins game with given gid once user selects game to join
     *
     * @param gid
     * @return
     */
    public static JSONObject joinGame(int gid) {

    }


}
