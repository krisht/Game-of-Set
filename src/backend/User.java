package backend;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class User {

    static final int DATABASE_FAILURE = -1;
    static final int USER_NOT_EXIST = 1;
    static final int PWD_INCORRECT = 2;
    static final int USER_ALREADY_EXIST = 3;
    static final int LOGIN_SUCCESS = 4;
    static final int REGISTER_SUCCESS = 5;

    private String uid;
    private String pass;
    private String name;
    //private Date joinDate;

    //public static void main(String [] args){
    //DBComm comm = new DBComm();
    //int sign = userLogin(comm, "krisht", "test123");
    //System.out.println(sign);
    //sign = createUser(comm, "newusrname", "new name", "new pass");
    //System.out.println(sign);
    //comm.DBClose();
    //}

    public User(String uid, String pass, String name) {
        this.uid = uid;
        this.pass = pass;
        this.name = name;
        //this.joinDate = new Date();
    }

    public static int userLogin(DBComm comm, String username, String password) {
        try {
            String sql_command = "Select uid FROM Users WHERE username = '" + username + "';";
            ResultSet rs = comm.DBQuery(sql_command);
            int id;
            if (rs != null && rs.next()) {
            } else {
                return USER_NOT_EXIST; //Username is invalid
            }

            sql_command = "SELECT uid, username, name FROM Users WHERE username = '" + username + "' and password = '" + password + "';";

            rs = comm.DBQuery(sql_command);
            if (rs.next()) {
                return LOGIN_SUCCESS; //Username and password are both valid, login accepted.
            }
            return PWD_INCORRECT; //Password does not match the username.
        } catch(Exception ex) {
            System.err.println("Database connection failed!");
            return DATABASE_FAILURE; //Database failure
        }
    }

    //public Date getJoinDate() {
    //    return joinDate;
    //}

    public static int createUser(DBComm comm, String username, String name, String password) {

        try {
            String sql_command = "SELECT * FROM Users WHERE username='" + username + "';";
            ResultSet rs = comm.DBQuery(sql_command);
            if (rs != null && rs.next()) {
                return USER_ALREADY_EXIST; //User already exists, cannot create a new user.
            }

            String temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
            sql_command = "INSERT INTO Users (username, name, password, joindate) VALUES ('" + username + "', '" + name + "', '" + password + "', '" + temp + "');";
            comm.DBInsert(sql_command);
            return REGISTER_SUCCESS; //Successful user creation.
        } catch(Exception ex) {
            System.err.println("Database connection failed!");
            return DATABASE_FAILURE; //DB failure
        }
    }

    public String getUid() {
        return this.uid;
    }

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

    //public void setJoinDate(Date date) {
    //    this.joinDate = date;
    //}

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean pushToDB() {
        //push this user's information to database
        return true;
    }
}
