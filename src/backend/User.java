package backend;

class User {

    private int uid;
    private String userName;
    private int currScore;
    private int gid;
    private int noMoreSets;

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

    void setGid(int gid) {
        this.gid = gid;
    }
    
    void setNoMoreSets() {
        this.noMoreSets = 1;
    }

    void setNoMoreSetsOff() {
        this.noMoreSets = 0;
    }

    int getNoMoreSets() {
        return this.noMoreSets;
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
