package backend;

import java.sql.ResultSet;
import java.util.Date;

public class User {

    private String uid;
    private String pass;
    private String name;
    private Date joinDate;

    public User(){

    }

    public User(String uid, String pass, String name) {
        this.uid = uid;
        this.pass = pass;
        this.name = name;
        this.joinDate = new Date();
    }

    public static boolean userLogin(String username, String password) {
        try {
            DBComm comm = new DBComm();
            String sql_command = "SELECT * FROM Users WHERE username = '" + username + "' AND password = '" + password + "'";
            ResultSet rs = comm.DBCall(sql_command);
            return (rs.next() && rs.getString("username").equals(username));
        } catch(Exception ex) {
            System.out.println("Database connection failed!");
            return false;
        }
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPass() { //maybe protected or private?
        return this.pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date date) {
        this.joinDate = date;
    }

    public String toString() {
        return "UID: " + uid + ", Name: " + name + ", Join Date: " + joinDate;
    }

    public int createUser(String username, String name, String password) {
        //call DB
        //Select * from Users where Users.name = 'username';
        //Return 1 on successful match (User already exists), -1 on DB failure
        //INSERT INTO Users (username, name, password) VALUES (username, name, password);
        //Return 0 on successful insertion, return -1 on DB failure
        return 0;
    }
}
