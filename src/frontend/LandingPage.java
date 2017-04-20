package frontend;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static frontend.LoginPage.newConnectionThread;
import static frontend.LoginPage.uid;


/*
 * Landing page with "Welcome User" title
 */

public class LandingPage extends JFrame implements ActionListener {
	
	private JPanel header, serverbrowser, chatbox;
	private GridBagConstraints c_header, c_serverbrowser, c_chatbox, c_serverlistpane, c_joingamebutton, c_creategamebutton, c_chatlogpane, c_chatinputfield;
	private JButton LOGOUT, JOINGAME, CREATEGAME;
	private JLabel userMessage;
	private JList serverlist;
	private JScrollPane chatlogpane;
	//private Border blackline;

	private String gameName;

	static DefaultListModel model;
	static JScrollPane serverlistpane;
	static JTextArea chatlogarea;
	static JTextField chatinputfield;
	static int gid;

	public LandingPage(String user) {

		// blah
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1280, 960);
		String username = user;

		gid = 0;

		Container cp = this.getContentPane();
		cp.setLayout(new GridBagLayout());
		//blackline = BorderFactory.createLineBorder(Color.black);

		makeHeaderPanel(cp, username);

		makeServerBrowser(cp);

		makeChatBox(cp);
		newConnectionThread.start();
		
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
	    header.setBorder(BorderFactory.createLineBorder(Color.black));
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
		serverbrowser.setBorder(BorderFactory.createLineBorder(Color.black));
		cp.add(serverbrowser, c_serverbrowser);
		
		serverbrowser.setLayout(new GridBagLayout());

		// Temporarily using defaultlistmodel. Will switch to a more suitable format once it is figured out
		model = new DefaultListModel();
		serverlist = new JList(model);
		serverlistpane = new JScrollPane(serverlist);
		update_server_list();

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
        chatbox.setBorder(BorderFactory.createLineBorder(Color.black));
        cp.add(chatbox, c_chatbox);
		
		chatbox.setLayout(new GridBagLayout());
		
		chatlogarea = new JTextArea();
		chatlogarea.setEditable(false);
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
				log_out();
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
				gameName = JOptionPane.showInputDialog(this, "Enter name of game");
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
		try {
			newConnectionThread.messageServer(loggingoutobj);
		} catch(Exception e){

		}
	}
	
	public void join_game (int newgid){
		JSONObject joingameobj = new JSONObject();
		joingameobj.put("fCall", "joinGame");
		joingameobj.put("UID", uid);
		joingameobj.put("GID", newgid);
		try {
			newConnectionThread.messageServer(joingameobj);
		} catch(Exception e){

		}
	}
	
	public void create_game (String gameName) {
		JSONObject creategameobj = new JSONObject();
		creategameobj.put("fCall", "createGame");
		creategameobj.put("uid", uid);
		creategameobj.put("gameName", gameName);
		try{
			newConnectionThread.messageServer(creategameobj);
		} catch(Exception e){
	
		}
	}

	public void update_server_list () {
		JSONObject updateserverlistobj = new JSONObject();
		updateserverlistobj.put("fCall", "getGameListing");
		updateserverlistobj.put("uid", uid);
		try {
			newConnectionThread.messageServer(updateserverlistobj);
		} catch (Exception e) {

		}
	}
}
