
package backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBComm {

    private Connection conn;

    public DBComm(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://199.98.20.115:3306/ReadySetGo?user=ross&password=ross2");
        } catch(Exception exp){
            exp.printStackTrace();
        }finally{
            try {
                if (conn != null)
                    conn.close();
            }catch(Exception exp2){
                exp2.printStackTrace();
            }
        }
    }

    public boolean insertUser(String username, String name, String password) throws Exception{
        Statement stat = conn.createStatement();
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
        String sql = "SELECT uid, username, name FROM Users WHERE username = ? and password = ? ";
        PreparedStatement prepStat = conn.prepareStatement(sql);
        prepStat.setString(1, username);
        prepStat.setString(2, pass);
        ResultSet rs = prepStat.executeQuery();

        return rs.next(); // Modify as needed
    }

    public ResultSet query(String sql) throws Exception {
        PreparedStatement prepStat = conn.prepareStatement(sql);
        ResultSet rs = prepStat.executeQuery();
        return rs;
    }

    public static void main(String [] args){
        DBComm comms = new DBComm();
        try {
            ResultSet rs = comms.query("SELECT * FROM Users");
            System.out.println(rs.next());
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

}
