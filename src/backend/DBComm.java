package backend;

import java.sql.*;

class DBComm {

    private static Connection conn;

    DBComm() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Error: can't find drivers!");
        }
        try {
            conn = DriverManager.getConnection("jdbc:mysql://199.98.20.115:3306/ReadySetGo?user=ross&password=ross2&verifyServerCertificate=false&useSSL=true&autoReconnect=true");
            System.err.println("Database connection established!");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error: unable to connect to database!");
        }
    }

    boolean DBClose() { //return 0 success, 1 failure
        try {
            conn.close();
            return true;
        } catch (Exception ex) {
            System.out.println("Error: Unable to close connection");
            return false;
        }
    }

    ResultSet DBQuery(String input) throws Exception {
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(input);
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.err.println("VendorError: " + ex.getErrorCode());
            return null;
        }
    }

    ResultSet DBInsert(String input) throws Exception {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(input);
            return stmt.getGeneratedKeys();
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.err.println("VendorError: " + ex.getErrorCode());
        }
        return null;
    }
}
