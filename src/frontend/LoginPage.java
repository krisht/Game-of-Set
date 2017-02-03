import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by abhinav on 1/28/2017.
 */
public class LoginPage extends JFrame implements ActionListener {

    // Create the basic instance variables for Submit button, username and password labels,
    // and username and password text fields
    JButton SUBMIT,REGISTER;
    JLabel usernameLabel, passwordLabel;
    final JTextField usernameField, passwordField;

    // Constructor that initialises and sets the labels to "username" and "password", initialises the fields, and
    // prepares the submit button. Then, creates the layout and sets the label.
    public LoginPage() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640, 480);

        // make the title 
        
        // Initialise and set instance variables
        usernameLabel = new JLabel();
        usernameLabel.setText("Username: ");
        passwordLabel = new JLabel();
        passwordLabel.setText("Password: ");

        usernameField = new JTextField(15);
        passwordField = new JTextField(15);

        SUBMIT = new JButton("SUBMIT");
        REGISTER = new JButton("REGISTER");
        // create two buttons, login and register
        // Retrieve the top-level content-plane
        Container cp = this.getContentPane();
        // Set layout
        cp.setLayout(new FlowLayout());
        // Add the components
        cp.add(usernameLabel);
        cp.add(usernameField);
        cp.add(passwordLabel);
        cp.add(passwordField);
        cp.add(SUBMIT);
        cp.add(REGISTER);
        // Prepare the action for the submit button
        SUBMIT.addActionListener(this);
        REGISTER.addActionListener(this);
        // Set the title of the login window
        setTitle("Login for Game");
    }

    // Checks username and password, and logs in if both present, throws error else.
    public void actionPerformed(ActionEvent ae) {

    	// Fetch the username and password from the textboxes
        String value1 = usernameField.getText();
        String value2 = passwordField.getText();
        
        // Check which button is pressed
    	JButton b = (JButton)ae.getSource();
        if (b.equals(REGISTER)){
        	RegisterPage rPage = new RegisterPage();
            // Close the login page while we're at it
        	setVisible(false);
        	dispose();
        	// Make page visible
            rPage.setVisible(true);
        }else if (b.equals(SUBMIT)){
        	// Check that both fields are present
            
            if ((value1 != null && !value1.isEmpty()) && (value2 != null && !value2.isEmpty())) {

                System.out.println("logged in");

                // Create a landing page
                LandingPage page = new LandingPage();

                // NOTE: The proper way as implemented in the landing page closes the landing page too, so use this way
                setVisible(false);
                dispose();

                // Make page visible
                page.setVisible(true);
                // Set title
                page.setTitle("Welcome " + value1);

                // Set uername in login window
                page.userMessage.setText("User " + value1 + " logged in.");

            } else {    // Else, throw an error in an error box
                System.out.println("username or password not present");
                JOptionPane.showMessageDialog(this, "Username or password not present", "Error", JOptionPane.ERROR_MESSAGE);
            }	
        }
    }
}