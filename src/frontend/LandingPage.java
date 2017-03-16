package frontend;

import backend.*;
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
	JButton LOGOUT, STARTGAME;
	JLabel userMessage;

	public LandingPage() {

		// blah
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(1280, 960);

		// initialise the instance variables, leaving message blank (to be set by login page)
		userMessage = new JLabel();
		LOGOUT = new JButton("Logout");
		STARTGAME = new JButton("Start Game");
		// blah
		Container cp = this.getContentPane();
		cp.setLayout(new FlowLayout());

		cp.add(userMessage);
		cp.add(LOGOUT);
		cp.add(STARTGAME);

		LOGOUT.addActionListener(this);
		STARTGAME.addActionListener(this);
	}

	public void actionPerformed(ActionEvent ae) {

		JButton b = (JButton)ae.getSource();
		// log out, this time killing the whole thing
		if (b.equals(LOGOUT)){
			System.out.println("logging out...");
			try {
				this.setVisible(false);
				this.dispose();
				
				LoginPage loginpage = new LoginPage();
				loginpage.setVisible(true);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
    			// this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}else if (b.equals(STARTGAME)) {

			// Create a game board
			try {
				GameBoard_Front gameboard = new GameBoard_Front();
				this.setVisible(false);
				this.dispose();
				// Make page visible
				gameboard.setVisible(true);
				// Set title
				gameboard.setTitle("SET GAME");
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}
}
