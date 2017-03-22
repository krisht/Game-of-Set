package backend;

public class User {

    private int uid;
    private String name;
    private String uname;
    private int currScore;

    public User(int uid, String name) {
        this.uid = uid;
        this.name = name;
        this.uname = uname;
    }

    public int getUID() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return uname;
    }

    public void addScore(int pts) {
        currScore += pts;
    }

    public void resetScore(int pts) {
        currScore = 0;
    }

    public int getScore() {
        return currScore;
    }

}
