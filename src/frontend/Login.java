package frontend;

import javax.swing.*;

/**
 * Created by abhinav on 1/28/2017.
 */

// Main class that creates the window of size and catches exceptions
public class Login {

    static ClientConnThreaded newConnectionThread;

    public static void main (String arg[]){

        newConnectionThread = new ClientConnThreaded();

        try {
            LoginPage loginpage = new LoginPage();
            loginpage.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}