package backend;

import java.io.*;
import java.util.*;
import java.sql.*;
import java.math.*;
import java.text.*;

public class User {

    private String uid;
    private String pass;
    private String name;
    //private Date joinDate;

    //public static void main(String [] args){
        //DBComm comm = new DBComm();
        //int sign = userLogin(comm, "krisht", "test123");
        //System.out.println(sign);
        //int sign = createUser(comm, "newusername", "new name", "new pass");
        //System.out.println(sign);
        //comm.DBClose();
    //}

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
            ResultSet rs = comm.DBQuery(sql_command);
            int id;
            if (rs.next() && rs!=null) {
            } else {
                return 2; //Username is invalid
            }

            sql_command = "SELECT uid, username, name FROM Users WHERE username = '" + username + "' and password = '" + password + "';";

            rs = comm.DBQuery(sql_command);
            if (rs.next()) {
                return 0; //Username and password are both valid, login accepted.
            }
            return 1; //Password does not match the username.
        } catch(Exception ex) {
            System.out.println("Database connection failed!");
            return -1; //Database failure
        }
    }

    public static int createUser(DBComm comm, String username, String name, String password) {

        try {
            String sql_command = "SELECT * FROM Users WHERE username='"+username+"';";
            ResultSet rs = comm.DBQuery(sql_command);
            if (rs.next() && rs!=null) {
                return 1; //User already exists, cannot create a new user.
            }

            String temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
            sql_command = "INSERT INTO Users (username, name, password, joindate) VALUES ('"+username+"', '"+name+"', '"+password+"', '"+temp+"');";
            System.out.println(sql_command);
            comm.DBInsert(sql_command);
            return 0; //Successful user creation.
        } catch(Exception ex) {
            System.out.println("Database connection failed!");
            return -1; //DB failure
        }
    }
}
