package backend;

import java.sql.*;

public class DBComm {

    private static Connection conn;

    public DBComm() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error: can't find drivers!");
        }
        try {
            conn = DriverManager.getConnection("jdbc:mysql://199.98.20.115:3306/ReadySetGo?user=ross&password=ross2&verifyServerCertificate=false&useSSL=true&autoReconnect=true");
            System.out.println("Database connection established!");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error: unable to connect to database!");
        }
    }
    
    public static boolean DBClose() { //return 0 success, 1 failure
        try {
            conn.close();
            return true;
        } catch (Exception ex) {
            System.out.println("Error: Unable to close connection");
            return false;
        }
    }

    public ResultSet DBCall(String input) throws Exception {
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(input);
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return null;
        }
    }

}
