
package backend;

import java.sql.ResultSet;
import java.util.ArrayList;

public class Game {

    DBComm gameDB = new DBComm();

    private ArrayList<User> users;
    private int[] currScores;
    private int gid;
    private GameBoard board = new GameBoard();

    public Game() throws Exception {

        try {
            gameDB.DBInsert("INSERT INTO TABLE Game() VALUES ()");
            ResultSet set = gameDB.DBQuery("SELECT * FROM GAME");
            if (set.next())
                gid = set.getInt(1);
            else throw new Exception("Game not inserted into database!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        board.initialize();
    }

    public Game(int numPlayers) {
        this.users = new ArrayList<>();
        currScores = new int[numPlayers];
    }

    public ArrayList<User> getUsers() {
        return this.users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public int[] getCurrScores() {
        return this.currScores;
    }

    public void setCurrScores(int[] scores) {
        this.currScores = scores;
    }

}
