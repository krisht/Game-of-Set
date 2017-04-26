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
            //bs
            conn = DriverManager.getConnection("jdbc:mysql://199.98.20.122:3306/ReadySetGo?user=ross&password=rossk&verifyServerCertificate=false&useSSL=true&autoReconnect=true");
            System.err.println("Database connection established!");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Error: unable to connect to database!");
        }
    }
//
//    public static void main(String [] args){
//        DBComm comm = new DBComm();
//        int currscore = 1;
//        int uid = 0;
//        try {
//            comm.DBInsert("UPDATE Users SET score=score+" + currscore + " WHERE uid=" + uid + ";");
//
//        } catch(Exception ex){
//            ex.printStackTrace();
//        }
//        comm.DBClose();
//    }

    boolean DBClose() { //return 0 success, 1 failure
        try {
            conn.close();
            return true;
        } catch (Exception ex) {
            System.err.println("Error: Unable to close connection");
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
