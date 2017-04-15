package backend;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


class GameListing {
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

    static JSONObject login(String uname, String pass) {
        String query = String.format(uname, pass);

        try {
            ResultSet rs = comm.DBQuery(query);

            int uid;

            if (rs.next()) {
                uid = Integer.parseInt(rs.getString("uid"));
                JSONObject obj = new JSONObject();
                obj.put("uid", uid);
                return obj;
            }
        } catch (SQLException ex) {
            System.err.println("SQLException detected!");
        } catch (Exception ex) {
            System.err.println("Exception detected!");
        }

        JSONObject obj = new JSONObject();
        obj.put("uid", -1);
        return obj;

    }

    static JSONObject register(String uname, String pass, String name) {
        int uid = -1;
        try {

            String query = "INSERT INTO Users (username, name, password) VALUES ('" + uname + "', '" + name + "', '" + pass + "');";
            ResultSet rs = comm.DBInsert(query);

            if (rs.next())
                uid = rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject obj = new JSONObject();
        obj.put("uid", uid);
        return obj;
    }

    @Override
    protected void finalize() throws Throwable {
        comm.DBClose();
        super.finalize();
    }
}
