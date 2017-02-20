<<<<<<< HEAD
import java.util.*;
import java.sql.*;
import java.math.*;
=======
package backend;

import java.sql.ResultSet;
import java.util.Date;
>>>>>>> 15bc59f5611ebfb76577cb2b54386c9587b85f33

public class User {

    private String uid;
    private String pass;
    private String name;
    //private Date joinDate;

    public static void main(String [] args){
        DBComm comm = new DBComm();
        int sign = userLogin(comm, "krisht", "test123");
        System.out.println(sign);
        comm.DBClose();
    }

    public User(String uid, String pass, String name) {
        this.uid = uid;
        this.pass = pass;
        this.name = name;
        //this.joinDate = new Date();
    }

    public String getUid() {
        return this.uid;
    }

    //public Date getJoinDate() {
    //    return joinDate;
    //}

    //public String toString() {
    //    return "UID: " + uid + ", Name: " + name + ", Join Date: " + joinDate;
    //}
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

    //public void setJoinDate(Date date) {
    //    this.joinDate = date;
    //}

    public boolean pushToDB() {
        //push this user's information to database
        return true;
    }

    public static int userLogin(DBComm comm, String username, String password) {
        try {
            String sql_command = "Select uid FROM Users WHERE username = '" + username + "';";
            ResultSet rs = comm.DBCall(sql_command);
            int id;
            if (rs.next() && rs!=null) {
            } else {
                return 2;
            }

            sql_command = "SELECT uid, username, name FROM Users WHERE username = '" + username + "' and password = '" + password + "';";

            rs = comm.DBCall(sql_command);
            if (rs.next()) {
                return 0;
            }
            return 1;
        } catch(Exception ex) {
            System.out.println("Database connection failed!");
            return -1;
        }
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
