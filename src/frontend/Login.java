package frontend;

import javax.swing.*;

/**
 * Created by abhinav on 1/28/2017.
 */

// Main class that creates the window of size and catches exceptions
public class Login {

    static ClientConnThreaded newConnectionThread;
    static int uid;
    static int gid;

    public static void main (String arg[]){

        uid = 0;
        gid = 0;

        newConnectionThread = new ClientConnThreaded(uid, gid);

        try {
            LoginPage loginpage = new LoginPage();
            loginpage.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}