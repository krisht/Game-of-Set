package backend;


import java.sql.*;

class DBComm {

    private static Connection conn;

    /**
     * Constructor for making Database Communication object
     */
    DBComm() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Error: can't find drivers!");
        }
        try {
            conn = DriverManager.getConnection("jdbc:mysql://199.98.20.122:3306/ReadySetGo?user=ross&password=rossk&verifyServerCertificate=false&useSSL=true&autoReconnect=true");
            System.err.println("Database connection established!");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Error: unable to connect to database!");
        }
    }

    /**
     * Closes the connection to the database
     * @return Boolean indicating whether database closed properly or not
     */
//    boolean DBClose() {
//        try {
//            conn.close();
//            return true;
//        } catch (Exception ex) {
//            System.err.println("Error: Unable to close connection");
//            return false;
//        }
//    }

    /**
     * Queries the database for User information
     * @param input String representing the query to the DBMS
     * @return ResultSet containing the output of the query
     * @throws Exception Thrown when there is an SQLException
     */
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

    /**
     * Inserts items into database
     * @param input String representing the insertion
     * @throws Exception Thrown when there is an SQLException
     */
    void DBInsert(String input) throws Exception {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(input);
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.err.println("VendorError: " + ex.getErrorCode());
        }
    }
}
