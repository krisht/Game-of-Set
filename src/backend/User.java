package backend;

public class User {

    private int uid;
    private String name;
    private String userName;
    private int currScore;
    private int gid;

    public User(int uid, String name, String userName, int gid) {
        this.uid = uid;
        this.name = name;
        this.userName = userName;
        this.currScore = 0;
        this.gid = gid;
    }

    public int getUid() {
        return uid;
    }

    private void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return userName;
    }

    private void setUsername(String userName) {
        this.userName = userName;
    }

    public void resetScore() {
        this.currScore = 0;
    }

    public int addScore(int diff) {
        currScore += diff;
        return this.currScore;
    }

    public int getScore() {
        return currScore;
    }

}
