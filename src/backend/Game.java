
package backend;

import java.util.ArrayList;

public class Game {

    private ArrayList<User> users;
    private int[] currScores;

    public Game(ArrayList<User> users) {
        this.users = users;
        currScores = new int[this.users.size()];
        // insert time somehow
    }

    public Game(int numPlayers) {
        this.users = new ArrayList<>();
        currScores = new int[numPlayers];
    }

    public ArrayList<User> getUsers() {
        return this.users;
    }

    public int[] getCurrScores() {
        return this.currScores;
    }

    public void setCurrScores(int[] scores) {
        this.currScores = scores;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

}
