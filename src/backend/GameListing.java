package backend;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class GameListing {

    static final int DATABASE_FAILURE = -1;
    static final int USER_NOT_EXIST = 1;
    static final int PWD_INCORRECT = 2;
    static final int USER_ALREADY_EXIST = 3;
    static final int LOGIN_SUCCESS = 4;
    static final int REGISTER_SUCCESS = 5;

    private static ConcurrentHashMap<Integer, Game> gamesList = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Integer, User> usersList = new ConcurrentHashMap<>();
    private static DBComm comm = new DBComm();

    GameListing() {
        try {
            ResultSet set = comm.DBQuery("SELECT U.uid, U.username, U.name FROM Users U");
            while (set.next()) {
                int uid = set.getInt("uid");
                String username = set.getString("username");
                String name = set.getString("name");
                User tempUser = new User(uid, name, username, -1);
                usersList.put(uid, tempUser);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        try {
            ResultSet set = comm.DBQuery("SELECT G.gid, G.gamename FROM Game G");
            while (set.next()) {
                int gid = set.getInt("gid");
                String gameName = set.getString("gamename");
                Game game = new Game(gid, gameName);
                gamesList.put(gid, game);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static ConcurrentHashMap<Integer, Game> getGames() {
        for (Map.Entry<Integer, Game> entry : gamesList.entrySet()) {
            int playerCount = entry.getValue().getPlayerList().size();
            int gid = entry.getKey();
            if (playerCount <= 0)
                gamesList.remove(gid);
        }
        return gamesList;
    }

    static ConcurrentHashMap<Integer, User> getUsers() {
        return usersList;
    }

    static Game getGame(int gid) {
        return gamesList.get(gid);
    }

    private static User getUser(int uid) {
        return usersList.get(uid);
    }

    static void removeUser(int uid) {
        if (usersList.containsKey(uid))
            usersList.remove(uid);
    }

    static JSONObject createGame(int uid) {
        Game game = new Game();
        User user = getUser(uid);
        gamesList.put(game.getGid(), game);
        game.addToGame(uid, user);
        JSONObject obj = new JSONObject();
        obj.put("gid", game.getGid());
        obj.put("gamename", game.getGameName());
        obj.put("function", "createGame");
        return obj;
    }

    static JSONObject createGame(int uid, String gameName) {
        Game game = new Game(gameName);
        User user = getUser(uid);
        gamesList.put(game.getGid(), game);
        game.addToGame(uid, user);
        JSONObject obj = new JSONObject();
        obj.put("gid", game.getGid());
        obj.put("gamename", game.getGameName());
        obj.put("function", "createGame");
        return obj;

    }

    static JSONObject joinGame(int uid, int gid) {
        Game game = gamesList.get(gid);
        User user = usersList.get(uid);
        user.resetScore();
        game.addToGame(uid, user);

        JSONObject obj = new JSONObject();
        obj.put("added", true);
        return obj;
    }

    static JSONObject login(String username, String password) {

        try {
            String sql_command = "Select uid FROM Users WHERE username = '" + username + "';";
            ResultSet rs = comm.DBQuery(sql_command);
            if (rs.next() && rs != null) {
            } else {
                JSONObject obj = new JSONObject();
                obj.put("uid", -1);
                obj.put("returnValue", USER_NOT_EXIST);
                return obj; //Username is invalid
            }

            sql_command = "SELECT uid, username, name FROM Users WHERE username = '" + username + "' and password = '" + password + "';";

            rs = comm.DBQuery(sql_command);
            if (rs.next()) {
                int uid = rs.getInt("uid");
                JSONObject obj = new JSONObject();
                obj.put("uid", uid);
                obj.put("returnValue", LOGIN_SUCCESS);
                return obj; //Username and password are both valid, login accepted
            } else {
                JSONObject obj = new JSONObject();
                obj.put("uid", -1);
                obj.put("returnValue", PWD_INCORRECT);
                return obj; //Password does not match the username.
            }
        } catch(Exception ex) {
            System.out.println("Database connection failed!");
            JSONObject obj = new JSONObject();
            obj.put("uid", -1);
            obj.put("returnValue", DATABASE_FAILURE);
            return obj; //Database failure
        }

    }

    static JSONObject register(String uname, String pass, String name) {
        int uid = -1;
        try {

            String query = "select uid from Users where username='" + uname + "';";
            ResultSet rs = comm.DBQuery(query);
            if (rs.next() && rs != null) {
                JSONObject obj = new JSONObject();
                obj.put("uid", uid);
                obj.put("returnValue", USER_ALREADY_EXIST);
                return obj;
            }

            query = "INSERT INTO Users (username, name, password) VALUES ('" + uname + "', '" + name + "', '" + pass + "');";

            comm.DBInsert(query);

            query = "select uid from Users where username='" + uname + "' and password='" + pass + "' and name='" + name + "';";
            rs = comm.DBQuery(query);
            if (rs.next() && rs != null) {
                uid = rs.getInt("uid");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JSONObject obj = new JSONObject();
            obj.put("uid", uid);
            obj.put("returnValue", DATABASE_FAILURE);
            return obj;
        }

        JSONObject obj = new JSONObject();
        obj.put("returnValue", REGISTER_SUCCESS);
        obj.put("uid", uid);
        return obj;
    }

    @Override
    protected void finalize() throws Throwable {
        comm.DBClose();
        super.finalize();
    }
}
