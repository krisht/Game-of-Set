package backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class GameListing {

    private static ConcurrentHashMap<Integer, Game> gamesList = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Integer, User> usersList = new ConcurrentHashMap<>();
    private static DBComm comm = new DBComm();

    public int login(String uname, String pass) {
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

    public int register(String uname, String pass, String name) {
        int uid = -1;
        try {

            String query = String.format(uname, pass, name);
            ResultSet rs = comm.DBInsert(query);

            if (rs.next())
                uid = rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return uid;
    }

    public void removeUser(int uid) {
        if (usersList.containsKey(uid))
            usersList.remove(uid);
    }

    public ArrayList<User> getUsers() {
        return new ArrayList<>(usersList.values());
    }

    public ArrayList<Game> getGames() {
        return new ArrayList<>(gamesList.values());
    }

    public Game getGame(int gid) {
        return gamesList.get(gid);
    }

    private User getUser(int uid) {
        return usersList.get(uid);
    }

    public Game createGame(int uid) {
        Game game = new Game();
        User user = getUser(uid);
        game.addToGame(uid, user);
        gamesList.put(game.getId(), game);
        return game;
    }

    public void joinGame(int uid, int gid) {
        Game game = gamesList.get(gid);
        User user = usersList.get(uid);
        game.addToGame(uid, user);
    }

}
