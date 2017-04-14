package backend;

class User {

    private int uid;
    private String name;
    private String userName;
    private int currScore;
    private int gid;

    User(int uid, String name, String userName, int gid) {
        this.uid = uid;
        this.name = name;
        this.userName = userName;
        this.currScore = 0;
        this.gid = gid;
    }

    int getUid() {
        return uid;
    }

    String getName() {
        return name;
    }

    String getUsername() {
        return userName;
    }

    int getGid() {
        return gid;
    }

    void resetScore() {
        this.currScore = 0;
    }

    int incScore() {
        currScore += 1;
        return this.currScore;
    }

    int getScore() {
        return currScore;
    }

}
