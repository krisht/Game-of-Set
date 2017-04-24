package backend;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class GameListing {

    static final int LOGIN_SUCCESS = 4;
    static final int REGISTER_SUCCESS = 5;
    private static final int DATABASE_FAILURE = -1;
    private static final int USER_NOT_EXIST = 1;
    private static final int PWD_INCORRECT = 2;
    private static final int USER_ALREADY_EXIST = 3;
    private static ConcurrentHashMap<Integer, Game> gamesList = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Integer, User> usersList = new ConcurrentHashMap<>();
    private static DBComm comm = new DBComm();

    static ConcurrentHashMap<Integer, Game> getGames() {
        for (Map.Entry<Integer, Game> entry : gamesList.entrySet()) {
            int playerCount = entry.getValue().getPlayerList().size();
            int gid = entry.getKey();
            if (playerCount <= 0)
                gamesList.remove(gid);
        }
        return gamesList;
    }

    static ArrayList<Integer> getGamesList() {
        HashSet<Integer> hs = new HashSet<>();
        ArrayList<Integer> thegames = new ArrayList<>(gamesList.keySet());
        hs.addAll(thegames);
        thegames.clear();
        thegames.addAll(hs);
        return thegames;
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

    static JSONObject leaveGame(int uid, int gid) {
        Game game = gamesList.get(gid);
        int score = game.getPlayerList().get(uid).getScore();
        game.getPlayerList().remove(uid);
        JSONObject obj = new JSONObject();
        obj.put("user_status", updateScore(uid, score));
        return obj;

    }

    private static boolean updateScore(int uid, int score) {

        String sql = "UPDATE User SET score = score + " + score + "WHERE uid=" + uid + ";";
        try {
            comm.DBInsert(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }


    static JSONObject createGame(int uid, String gameName) {

        Game game = new Game(gameName);
        User user = getUser(uid);
        user.setGid(game.getGid());
        gamesList.put(game.getGid(), game);
        game.addToGame(uid, user);

        JSONObject obj = new JSONObject();
        obj.put("gid", game.getGid());
        obj.put("fCall", "createGameResponse");
        obj.put("returnValue", 3);
        return obj;
    }

    static JSONObject joinGame(int uid, int gid) {
        Game game = gamesList.get(gid);
        User user = usersList.get(uid);
        user.resetScore();
        game.addToGame(uid, user);

        JSONObject obj = new JSONObject();
        obj.put("returnValue", 3);
        return obj;
    }

    static int noMoreSets(int uid, int gid) {
        System.out.println("In here2");
        Game game = gamesList.get(gid);
        User user = usersList.get(uid);
        System.out.println("uid: " + uid);
        System.out.println("User says: " + user.getNoMoreSets());
        System.out.println("Game says: " + game.numNoMoreSets());
        if (user.getNoMoreSets() == 0) {
            user.setNoMoreSets();
            game.incNoMoreSets();
        }
        System.out.println("User says: " + user.getNoMoreSets());
        System.out.println("Game says: " + game.numNoMoreSets());
        int size = game.getPlayerList().size();
        if (game.numNoMoreSets() == size) { //Everyone agrees no more sets
            game.requestCards();
            game.clearNoMoreSets();
            return 1;
        } else if (game.numNoMoreSets() > size) {
            System.err.println("More players said no sets than there are people");
            return -1;
        } else {
            System.out.println("You are number " + game.numNoMoreSets() + " to say no more sets");
            return 0;
        }
    }

    static JSONObject updateGame(int uid, int gid) { //THIS IS THE NEW THING
        Game newgame = getGame(gid);
        JSONObject obj = new JSONObject();
        obj.put("gid", gid);
        obj.put("fCall", "updateGameResponse");
        obj.put("gameboard", newgame.getGameBoard().sendToFE());
        obj.put("gamename", newgame.getGameName());
        ArrayList<String> usernames = new ArrayList<>();
        ArrayList<Integer> uids = new ArrayList<>();
        ArrayList<Integer> scores = new ArrayList<>();

        for (Map.Entry<Integer, User> entry : newgame.getPlayerList().entrySet()) {
            usernames.add(entry.getValue().getUsername());
            uids.add(entry.getKey());
            scores.add(entry.getValue().getScore());
        }

        obj.put("scoreboard_usernames", usernames);
        obj.put("scoreboard_uids", uids);
        obj.put("scoreboard_scores", scores);

        return obj;
    }

    static JSONObject login(String username, String password) {

        try {
            String sql_command = "Select uid FROM Users WHERE username = '" + username + "';";
            ResultSet rs = comm.DBQuery(sql_command);

            if (rs == null || !rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("uid", -1);
                obj.put("returnValue", USER_NOT_EXIST);
                return obj; //Username is invalid
            }

            sql_command = "SELECT uid, username FROM Users WHERE username = '" + username + "' and password = '" + password + "';";

            rs = comm.DBQuery(sql_command);
            if (rs.next()) {
                int uid = rs.getInt("uid");
                JSONObject obj = new JSONObject();
                obj.put("uid", uid);
                obj.put("returnValue", LOGIN_SUCCESS);
                User user = new User(uid, username);
                usersList.put(uid, user);

                return obj; //Username and password are both valid, login accepted
            } else {
                JSONObject obj = new JSONObject();
                obj.put("uid", -1);
                obj.put("returnValue", PWD_INCORRECT);
                return obj; //Password does not match the username.
            }
        } catch (Exception ex) {
            System.err.println("Database connection failed!");
            JSONObject obj = new JSONObject();
            obj.put("uid", -1);
            obj.put("returnValue", DATABASE_FAILURE);
            return obj; //Database failure
        }

    }

    static JSONObject register(String uname, String pass) {
        int uid = -1;
        try {

            String query = "select uid from Users where username='" + uname + "';";
            ResultSet rs = comm.DBQuery(query);
            if (rs != null && rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("uid", uid);
                obj.put("returnValue", USER_ALREADY_EXIST);
                return obj;
            }

            query = "INSERT INTO Users (username, password) VALUES ('" + uname + "', '" + pass + "');";

            comm.DBInsert(query);

            query = "select uid from Users where username='" + uname + "' and password='" + pass + "';";
            rs = comm.DBQuery(query);
            if (rs != null && rs.next()) {
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
        User user = new User(uid, uname);
        usersList.put(uid, user);
        return obj;
    }

    static void updateUserScore(int uid, int scoreChange) {
        try {
            String sql = "UPDATE Users SET score=score+" + scoreChange + " WHERE uid=" + uid + ";";
            System.out.println(sql);
            comm.DBInsert(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        comm.DBClose();
        super.finalize();
    }
}
