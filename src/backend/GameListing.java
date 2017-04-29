package backend;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
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
    private static final int USER_SIGNED_IN_ELSEWHERE = 6;
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
        //Refresh games
        for (Map.Entry<Integer, Game> entry : gamesList.entrySet()) {
            int playerCount = entry.getValue().getPlayerList().size();
            int gid = entry.getKey();
            if (playerCount <= 0)
                gamesList.remove(gid);
        }

        HashSet<Integer> hs = new HashSet<>();
        ArrayList<Integer> games = new ArrayList<>(gamesList.keySet());
        hs.addAll(games);
        games.clear();
        games.addAll(hs);
        return games;
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

    static boolean checkGameOver(int gid){
        Game game = GameListing.getGame(gid);

        boolean playersSayNo = (game.numNoMoreSets() == game.getPlayerList().size() && game.getGameBoard().getDeck().size() == 0);
        boolean boardIsEmpty = game.getGameBoard().getBoard().size() == 0;
        boolean boardHasAllNegOnes = Collections.frequency(game.getGameBoard().getBoard(), -1) == game.getGameBoard().getBoard().size();
        boolean zeroPlayers = game.getPlayerList().size() == 0;

        return playersSayNo || boardIsEmpty || boardHasAllNegOnes || zeroPlayers;
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
        //Done
        if(updateScore(uid, score))
            obj.put("returnValue", 1);
        else obj.put("returnValue", 0);
        return obj;

    }

    static JSONObject getPlayerScore(int uid){
        JSONObject tempObj = new JSONObject();

        int dbscore= -1;
        try {
            ResultSet scores = comm.DBQuery("SELECT score FROM Users WHERE uid='" + uid + "';");
            if (scores != null && scores.next())
                dbscore = scores.getInt("score");
            tempObj.put("score", dbscore);
            tempObj.put("fCall", "playerScoreResponse");
            return tempObj;
        } catch (Exception ex) {
            ex.printStackTrace();
            tempObj.put("Error", "Error");
            return tempObj;
        }

    }

    static void resetNoMoreSets(int gid){
        Game game = GameListing.getGame(gid);
        for(User user : game.getPlayerList().values())
            user.setNoMoreSetsOff();
    }

    private static boolean updateScore(int uid, int score) {
        try {
            String sql_command = "SELECT score FROM Users WHERE uid = " + uid + ";";
            ResultSet rs = comm.DBQuery(sql_command);
            int dbscore;

            if (rs.next())
                dbscore = rs.getInt("score");
            else return false;

            score += dbscore;

            sql_command = "UPDATE Users SET score=" + score + " WHERE uid=" + uid + ";";

            comm.DBInsert(sql_command);

        } catch (Exception ex) {
            System.err.println("Database connection failed!");
            JSONObject obj = new JSONObject();
            obj.put("uid", -1);
            obj.put("returnValue", DATABASE_FAILURE);
            return false;
        }
        return true;
    }


    static JSONObject createGame(int uid, String gameName) {

        JSONObject obj = new JSONObject();
        //Loop over all games to get game names
        for (Game temp_game : gamesList.values()) {
            if (gameName.equals(temp_game.getGameName())) {
                obj.put("returnValue", 4);
                obj.put("fCall", "createGameResponse");
                return obj;
            }
        }

        Game game = new Game(gameName);
        User user = getUser(uid);
        user.setGid(game.getGid());
        gamesList.put(game.getGid(), game);
        game.addToGame(uid, user);

        obj.put("gid", game.getGid());
        obj.put("fCall", "createGameResponse");
        obj.put("returnValue", 3);
        return obj;
    }

    static JSONObject joinGame(int uid, int gid) {
        JSONObject obj = new JSONObject();
        Game game = gamesList.get(gid);
        if (game == null) {
            obj.put("returnValue", 1); //Game does not exist
            return obj;
        } else if (game.getPlayerList().size() >= 4) {
            obj.put("returnValue", 2);
            return obj;
        } else {
            obj.put("returnValue", 3);
            return obj;
        }
        User user = usersList.get(uid);
        user.resetScore();
        if(game != null){
            game.addToGame(uid, user);
        }

        return obj;
    }

    static int noMoreSets(int uid, int gid) {
        Game game = gamesList.get(gid);
        User user = usersList.get(uid);
        if (user.getNoMoreSets() == 0) {
            user.setNoMoreSets();
            game.incNoMoreSets();
        }
        int size = game.getPlayerList().size();
        if (game.numNoMoreSets() == size) { //Everyone agrees no more sets
            game.requestCards();
            game.clearNoMoreSets();
            return 1;
        } else if (game.numNoMoreSets() > size)
            return -1;
        else return 0;
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
        ArrayList<Integer> noMoreSets = new ArrayList<>();

        for (Map.Entry<Integer, User> entry : newgame.getPlayerList().entrySet()) {
            usernames.add(entry.getValue().getUsername());
            uids.add(entry.getKey());
            scores.add(entry.getValue().getScore());
            noMoreSets.add(entry.getValue().getNoMoreSets()); //No more sets
        }

        obj.put("scoreboard_usernames", usernames);
        obj.put("scoreboard_uids", uids);
        obj.put("scoreboard_scores", scores);
        obj.put("nomoresets", noMoreSets);

        return obj;
    }

    static JSONObject login(String username, String password) {

        try {
            String sql_command = "SELECT uid FROM Users WHERE username = '" + username + "';";
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
                //Indicates that user is already signed in
                if(ServerConn.uidToSocket.containsKey(uid)){
                    obj.put("uid", -1);
                    obj.put("returnValue", USER_SIGNED_IN_ELSEWHERE);
                    return obj;
                }


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

            String query = "SELECT uid FROM Users WHERE username='" + uname + "';";
            ResultSet rs = comm.DBQuery(query);
            if (rs != null && rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("uid", uid);
                obj.put("returnValue", USER_ALREADY_EXIST);
                return obj;
            }

            query = "INSERT INTO Users (username, password) VALUES ('" + uname + "', '" + pass + "');";

            comm.DBInsert(query);

            query = "SELECT uid FROM Users WHERE username='" + uname + "' and password='" + pass + "';";
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

    @Override
    protected void finalize() throws Throwable {
        comm.DBClose();
        super.finalize();
    }
}
