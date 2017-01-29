
package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class Info{

    private Connection conn = null;
    private Statement stat = null;
    private PreparedStatement pStat = null;
    private ResultSet rSet = null;

    public int insertUser(String uid, String name, String pass) throws Exception{


        //do stuff
        return 0;
    }

    public int getUser(String uid, String pass) throws Exception{
        // do stuff
        return 0;
    }

    public int getGame(int gid){
        //do stuff
        return 0;
    }

    public int insertGame(){
        //do stuff
        return 0;
    }
}