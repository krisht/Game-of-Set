import javax.swing.*;

/**
 * Created by abhinav on 1/28/2017.
 */
// Main class that creates the window of size and catches exceptions
public class Login {
    public static void main (String arg[]){

        try {
            LoginPage frame = new LoginPage();
            frame.setVisible(true);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}