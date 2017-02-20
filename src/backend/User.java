package backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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

    public static int userLogin(String username, String password) {
        try {
<<<<<<< HEAD
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception ex) {
            System.out.println("Error: can't find drivers!");
            System.exit(1);
        }

        try {
            //Connection conn = DriverManager.getConnection("jdbc:mysql://199.98.20.115:5122/ReadySetGo", "root", "brenda2");
            Connection conn = DriverManager.getConnection("jdbc:mysql://199.98.20.115:3306/ReadySetGo?user=ross&password=ross2");
            System.out.println("Database connection established!");
        } catch (SQLException ex) {
            System.out.println("Error: unable to connect to database!");
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());


            System.exit(1);
=======
            DBComm comm = new DBComm();

            String sql_command = "Select uid FROM Users WHERE username = '" + username + "';";
            ResultSet rs = comm.DBCall(sql_command);
            int id;
            if (rs.next() && rs!=null) {
                id = rs.getInt("uid");
                if (id == 0) {
                    return 2;
                }
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
>>>>>>> 6b902fc2a7798bf00dc77968ca4afbd73894a196
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
