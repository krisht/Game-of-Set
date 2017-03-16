package frontend;

import backend.*;
import comms.ClientConn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import org.json.JSONObject;


/*
 * Landing page with "Welcome User" title
 */

public class LandingPageV2 extends JFrame implements ActionListener {
	
	private JPanel header, serverbrowser, chatbox;
	private GridBagConstraints c_header, c_serverbrowser, c_chatbox;
	private JButton LOGOUT, JOINGAME, CREATEGAME;
	private JLabel userMessage;
	private JList serverlist;
	private JScrollPane serverlistpane, chatlogpane;
	private JTextArea chatlogarea;
	private JTextField chatinputfield;
	private DefaultListModel model;

	public LandingPageV2(String user) {

		// blah
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1280, 960);
		String username = user;
		Container cp = this.getContentPane();
		cp.setLayout(new GridBagLayout());
		blackline = BorderFactory.createLineBorder(Color.black);
		makeHeaderPanel(cp, username);
		
		
	}
	
	
	public void makeHeaderPanel(Container cp, String user) {
	
		c_header = new GridBagConstraints();
	    header = new JPanel();
	    c_header.weightx = 1;
	    c_header.weighty = 0.1;
	    c_header.fill = GridBagConstraints.BOTH;
	    c_header.gridx = 0;
	    c_header.gridy = 0;
	    c_header.gridwidth = 2;
	    c_header.gridheight = 1;
        c_header.gridwidth = 1;
        c_header.gridheight = 1;
	    header.setBorder(blackline);
	    cp.add(header, c_header);
		
		userMessage = new JLabel("Logged in as " + user + ".");
		header.add(userMessage);
	    LOGOUT = new JButton("Logout");
		LOGOUT.addActionListener(this);
	    header.add(LOGOUT);
		
	}
	
	public void makeServerBrowser(Container cp) {
		
		c_serverbrowser = new GridBagConstraints();
		serverbrowser = new JPanel();
		c_serverbrowser.fill =  GridBagConstraints.BOTH;
		c_serverbrowser.weightx = 0.7;
		c_serverbrowser.weighty = 0.9;
		c_serverbrowser.gridx = 0;
		c_serverbrowser.gridy = 1;
        c_serverbrowser.gridwidth = 1;
        c_serverbrowser.gridheight = 1;
		serverbrowser.setBorder(blackline);
		cp.add(serverbrowser, c_serverbrowser);
		
		serverbrowser.setLayout(new GridBagLayout());

		// Temporarily using defaultlistmodel. Will switch to a more suitable format once it is figured out
		model = new DefaultListModel();
		serverlist = new JList(model);
		serverlistpane = new JScrollPane(serverlist);
		/*
		 * CODE TO GET ARRAY OF GameListing objects
		 */
		for (int i = 0; i < inputlist.size(); i++) {
			model.addElement (i + ". " + inputlist[i].getGname + " - " + inputlist[i].getNumplayers + "/4");
		}
		c_serverlistpane = new GridBagConstraints();
		c_serverlistpane.fill = GridBagConstraints.BOTH;
		c_serverlistpane.weightx = 1.0;
		c_serverlistpane.weighty = 0.95;
		c_serverlistpane.gridx = 0;
		c_serverlistpane.gridy = 0;
        c_serverlistpane.gridwidth = 2;
        c_serverlistpane.gridheight =  1;
		
		JOINGAME = new JButton("Join game");
		JOINGAME.addActionListener(this);
		c_joingamebutton = new GridBagConstraints();
		c_joingamebutton.fill = GridBagConstraints.BOTH;
		c_joingamebutton.weightx = 0.5;
		c_joingamebutton.weighty = 0.05;
		c_joingamebutton.gridx = 0;
		c_joingamebutton.gridy = 1;
        c_joingamebutton.gridwidth = 1;
        c_joingamebutton.gridheight = 1;
		
		CREATEGAME = new JButton("Create new game");
		CREATEGAME.addActionListener(this);
		c_creategamebutton = new GridBagConstraints();
		c_creategamebutton.fill = GridBagConstraints.BOTH;
		c_creategamebutton.weightx = 0.5;
		c_creategamebutton.weighty = 0.05;
		c_creategamebutton.gridx = 1;
		c_creategamebutton.gridy = 1;
        c_creategamebutton.gridwidth = 1;
        c_creategamebutton.gridheight = 1;
				
		serverbrowser.add(serverlistpane, c_serverlistpane);
		serverbrowser.add(JOINGAME, c_joingamebutton);
		serverbrowser.add(CREATEGAME, c_creategamebutton);
	}
	
	public void makeChatBox(Container cp) {
        c_chatbox = new GridBagConstraints();
        chatbox = new JPanel();
        c_chatbox.fill = GridBagConstraints.BOTH;
        c_chatbox.weightx = 0.3;
        c_chatbox.weighty = 0.9;
        c_chatbox.gridx = 1;
        c_chatbox.gridy = 1;
        c_chatbox.gridwidth = 1;
        c_chatbox.gridheight = 1;
        chatbox.setBorder(blackline);
        cp.add(chatbox, c_chatbox);
		
		chatbox.setLayout(new GridBagLayout());
		
		chatlogarea = new JTextArea();
		chatlogarea.setEditable(False);
		chatlogpane = new JScrollPane(chatlogarea);
		c_chatlogpane = new GridBagConstraints();
		c_chatlogpane.fill = GridBagConstraints.BOTH;
		c_chatlogpane.weightx = 1.0;
		c_chatlogpane.weighty = 0.95;
		c_chatlogpane.gridx = 0;
		c_chatlogpane.gridy = 0;
        c_chatlogpane.gridwidth = 1;
        c_chatlogpane.gridheight = 1;
		
		chatinputfield = new JTextField();
		chatinputfield.addActionListener(this);		
		c_chatinputfield = new GridBagConstraints();
		c_chatinputfield.fill = GridBagConstraints.BOTH;
		c_chatinputfield.weightx = 1.0;
		c_chatinputfield.weighty = 0.05;
		c_chatinputfield.gridx = 0;
		c_chatinputfield.gridy = 1;
        c_chatinputfield.gridwidth = 1;
        c_chatinputfield.gridheight = 1;
		
		chatbox.add(chatlogpane, c_chatlogpane);
		chatbox.add(chatinputfield, c_chatinputfield);
		
		
	}

	public void actionPerformed(ActionEvent ae) {

		JButton b = (JButton)ae.getSource();
		// log out, this time killing the whole thing
		if (b.equals(LOGOUT)){
			
			//SEND LOGOUT REQUEST TO SERVER
			System.err.println("logging out...");
			try {
				log_out(uid);				
				this.setVisible(false);
				this.dispose();
				//WE SHOULD JUST EXIT THE CLIENT HERE RATHER THAN RETURNING TO THE LOGIN PAGE SINCE WE ARE
				//RUNNING A LOT OF NESTED OBJECTS. GARBAGE COLLECTION CAN BE AN ISSUE IN THIS CASE AND CAN
				//LEAD TO MEMORY LEAKS
				
				//LoginPage loginpage = new LoginPage();
				//loginpage.setVisible(true);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		} else if (b.equals(JOINGAME)) {
			try {
				//GET GID FROM SELECTED GAME IN SERVER BROWSER
				//TO BE IMPLEMENTED
				join_game(gid);
			} catch (Exception e) {
				//IMPLEMENT GAME FULL ERROR MESSAGE
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		} else if (b.equals(CREATEGAME)) {
			try {
				create_game(gameName);
			} catch (Exception e) {
				//IMPLEMENT ERROR CODES FOR NAME ALREADY EXISTS
				//NOT ENOUGH SEVER SPACE (MAYBE)
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
		//PERFORM ACTION ON TEXT FIELD FOR CHAT BOX
	}
	
	public void log_out() {
		JSONObject loggingoutobj = new JSONObject();
		loggingoutobj.put("fCall", "loggingOut");
		loggingoutobj.put("UID", uid);
		runningConn.messageServer(loggingoutobj);
	}
	
	public void join_game (int gid){
		JSONObject joingameobj = new JSONObject();
		joingameobj.put("fCall", "joinGame");
		joingameobj.put("UID", uid);
		joingameobj.put("GID", gid);
		runningConn.messageServer(joingameobj);
	}
	
	public void create_game (String gameName) {
		JSONObject creategameobj = new JSONObject();
		creategameobj.put("fCall", "createGame");
		creategameobj.put("UID", uid);
		creategameobj.put("gameName", gameName);
		runningConn.messageServer(creategameobj);
	}

}
