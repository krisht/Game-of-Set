package frontend;

import javax.swing.*;

// Main class that creates the window of size and catches exceptions
public class Login {
    public static void main(String arg[]) {

        try {
            LoginPage loginpage = new LoginPage();
            loginpage.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}