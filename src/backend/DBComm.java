package backend;

import java.sql.*;

public class DBComm {

    private static Connection conn;

    public static void main(String[] args) throws Exception {
        DBComm comms = new DBComm();
        boolean sign = comms.findUser("krisht", "test123");
        System.out.println(sign);
        conn.close();

    }

    public DBComm() { //1 on failure, 0 on success
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error: can't find drivers!");
        }
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://199.98.20.115:3306/ReadySetGo?user=ross&password=ross2&verifyServerCertificate=false&useSSL=true&autoReconnect=true");
            System.out.println("Database connection established!");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error: unable to connect to database!");
        }
    }

    public boolean DBClose() { //return 0 success, 1 failure
        try {
            conn.close();
            return true;
        } catch (Exception ex) {
            System.out.println("Error: Unable to close connection");
            return false;
        }
    }


    public boolean insertUser(String username, String name, String password) throws Exception {
        String sql = "INSERT INTO Users(username, name, password) values (?, ?, ?)";
        PreparedStatement prepStat = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        prepStat.setString(1, username);
        prepStat.setString(2, name);
        prepStat.setString(3, password);
        prepStat.executeUpdate();
        ResultSet rs = prepStat.getGeneratedKeys();

        return rs.next();
    }

    public boolean findUser(String username, String pass) throws Exception {
        try {
            String sql_command = "SELECT uid, username, name FROM Users WHERE username = '" + username + "' and password = '" + pass + "';";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql_command);

            if (rs.next()) {
                int id = rs.getInt("uid");
                if (id != 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return false;
    }

    public ResultSet query(String sql) throws Exception {
        PreparedStatement prepStat = conn.prepareStatement(sql);
        return prepStat.executeQuery();
    }

}
