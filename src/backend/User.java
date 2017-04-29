package backend;

class User {

    private int uid;
    private String userName;
    private int currScore;
    private boolean noMoreSets;

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

    void setNoMoreSets() {
        this.noMoreSets = true;
    }

    void setNoMoreSetsOff() {
        this.noMoreSets = false;
    }

    boolean getNoMoreSets() {
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
