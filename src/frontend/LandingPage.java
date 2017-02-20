package frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/*
 * Landing page with "Welcome User" title
 */

public class LandingPage extends JFrame implements ActionListener {

    // Create label for user logged in message, and button for logout
    JButton LOGOUT;
    JLabel userMessage;

    public LandingPage() {

        // blah
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(640, 480);

        // initialise the instance variables, leaving message blank (to be set by login page)
        userMessage = new JLabel();
        LOGOUT = new JButton("Logout");

        // blah
        Container cp = this.getContentPane();
        cp.setLayout(new FlowLayout());

        cp.add(userMessage);
        cp.add(LOGOUT);

        LOGOUT.addActionListener(this);
    }

    public void actionPerformed(ActionEvent ae) {
        // log out, this time killing the whole thing
        System.out.println("logging out...");
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}