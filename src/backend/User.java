package backend;

import java.util.Date;

public class User {

    private String uid;
    private String pass;
    private String name;
    private Date joinDate;

    public User(String uid, String pass, String name) {
        this.uid = uid;
        this.pass = pass;
        this.name = name;
        this.joinDate = new Date();
    }

    public String getUid() {
        return this.uid;
    }

    public String getPass() { //maybe protected or private?
        return this.pass;
    }


    public String getName() {
        return this.name;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public String toString() {
        return "UID: " + uid + ", Name: " + name + ", Join Date: " + joinDate;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJoinDate(Date date) {
        this.joinDate = date;
    }


    public boolean pushToDB() {
        //push this user's information to database
        return true;
    }

}
