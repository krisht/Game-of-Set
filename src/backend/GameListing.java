package backend;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameListing {

    public static ConcurrentHashMap<Integer, Game> gamesList = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Integer, User> usersList = new ConcurrentHashMap<>();
    private static DBComm comm = new DBComm();

    /**
     * Signs user into game lobby
     *
     * @param uname Username of person trying to log in
     * @param pass Password of person trying to log in
     * @return uid
     */
    public static int login(String uname, String pass) {
        String query = String.format(uname, pass);

        try {
            ResultSet rs = comm.DBQuery(query);

            int uid;

            if (rs.next()) {
                uid = Integer.parseInt(rs.getString("uid"));
                return uid;
            }
        } catch (SQLException ex) {
            System.err.println("SQLException detected!");
        } catch (Exception ex) {
            System.err.println("Exception detected!");
        }

        return -1;

    }

    /**
     * Registers user into game lobby if they don't have a username and pass
     *
     * @param uname Username of the person trying to register
     * @param pass Password of the person trying to register
     * @param name Name of the person trying to register
     * @return uid
     */
    public static int register(String uname, String pass, String name) {
        int uid = 0;
        try {

            String query = String.format(uname, pass, name);
            ResultSet rs = comm.DBInsert(query);

            if (rs.next())
                uid = rs.getInt(1);

            return uid;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns list of players for FE to display if necessary
     *
     * @return
     */
    public static JSONObject getPlayers() {
        JSONObject obj = new JSONObject();
        for (Map.Entry<Integer, User> entry : usersList.entrySet()) {
            String uname = entry.getValue().getUsername();
            int uid = entry.getKey();
            //Insert stuff into json somehow
            //Insert other shit here
        }
        return obj;
    }

    /**
     * Returns list of games for FE to display if necessary
     *
     * @return
     */
    public static JSONObject getGames() {
        JSONObject obj = new JSONObject():

        for (Map.Entry<Integer, Game> games : gamesList.entrySet()) {
            int gid = games.getKey();
            String lol = games.getValue().
        }

        return obj;

    }

    /**
     * Joins game with given gid once user selects game to join
     *
     * @param gid
     * @return
     */
    public static JSONObject joinGame(int gid) {

    }

    public static int createGame(int uid) {

    }

}
