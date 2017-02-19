import java.util.Date;
import java.sql.*;
import java.math.*;

public class User {

    public static void main(String args[]) {
        int connValue = connectToDB();
        System.out.println(connValue);
    }

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

    public static int connectToDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch(Exception ex) {
            System.out.println("Error: can't find drivers!");
            System.exit(1);
        }
    
        try {
            String URL = "jdbc:mysql://199.98.20.115:5122/ReadySetGo";
            String USER = "root";
            String PASS = "brenda2";
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Database connection established!");
        } catch(Exception ex) {
           System.out.println("Error: unable to connect to database!");
           System.exit(1);
        }

        //instantiate new jdbc connection as private member up top^^
        //then if connection already exists, return working.
        //otherwise, connect. if fails, return failed!
        return 0;
    }

    public int userLogin(String username, String password) {
        //call DB
        //SELECT * from Users where Users.name = 'username';
        //If no match, return 2 (user DNE), -1 on DB failure
        //SELECT * from Users where Users.name = 'username' AND Users.password = 'password';
        //Return 0 on success, 1 on failure (invalid password)
        return 0;
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
