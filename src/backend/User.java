package backend;

class User {

    private int uid;
    private String userName;
    private int currScore;
    private int gid;

    User(int uid, String userName) {
        this.uid = uid;
        this.userName = userName;
        this.currScore = 0;
    }

    int getUid() {
        return uid;
    }

    String getUsername() {
        return userName;
    }

    int getGid() {
        return gid;
    }

    void setGid(gid) {
        this.gid = gid;
    }

    void resetScore() {
        this.currScore = 0;
    }

    int incScore() {
        return ++this.currScore;
    }

    int getScore() {
        return currScore;
    }

}
