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

public class LandingPage_New extends JFrame implements ActionListener {

    static DefaultListModel model;
    static JScrollPane serverlistpane;
    static JTextArea chatlogarea;
    static JTextField chatinputfield;
    static int gid;
    private JPanel header, serverbrowser, chatbox, userbox;
    private GridBagConstraints c_gamelistLabel, c_refreshbutton, c_helpbutton, c_joingamebutton, c_creategamebutton;
    private GridBagConstraints c_userbox, c_welcomeLabel, c_scoreLabel, c_scorecapLabel, c_logout;
    private GridBagConstraints  c_chatLabel, c_chatbox, c_chatlogpane, c_chatinputfield;
    private GridBagConstraints c_header, c_serverbrowser, c_serverlistpane;
    private JButton LOGOUT, JOINGAME, CREATEGAME, HELP, REFRESH;
    private JLabel userMessage, titleLabel, creatorLabel, chatLabel;
    //private Border blackline;
    private JLabel gamelistLabel;
    private JList serverlist;
    private JLabel welcomeLabel,scoreLabel, scorecapLabel;
    private JScrollPane chatlogpane;
    private Font f, bfont;
    private String gameName;

    public LandingPage_New() {

        // blah
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 960);
        gid = 0;

        Container cp = this.getContentPane();
        cp.setLayout(new GridBagLayout());

		f = new Font("Arial", Font.BOLD, 12);
		bfont = new Font("Arial",Font.PLAIN, 18);
        makeHeaderPanel(cp);
        makeServerBrowser(cp);
        makeUserBox(cp);
        makeChatBox(cp);

        newConnectionThread.start();

    }


    public void makeHeaderPanel(Container cp) {

        header = new JPanel(new GridBagLayout());
        c_header = new GridBagConstraints();
        c_header.fill = GridBagConstraints.HORIZONTAL;
        header.setMinimumSize(new Dimension(1280,100));
        header.setPreferredSize(new Dimension(1280,100));
        c_header.gridx = 0;
        c_header.gridy = 0;
        c_header.ipadx = 0;
        c_header.ipady = 0;
        c_header.gridwidth = 2;
        c_header.gridheight = 1;
        header.setBackground(Color.decode("#009688"));
        cp.add(header, c_header);

    }

    public void makeServerBrowser(Container cp) {

        c_serverbrowser = new GridBagConstraints();
        serverbrowser = new JPanel(new GridBagLayout());
        c_serverbrowser.fill = GridBagConstraints.BOTH;
        c_serverbrowser.weightx = 0.7;
        c_serverbrowser.weighty = 0.9;
        c_serverbrowser.gridx = 0;
        c_serverbrowser.gridy = 1;
        c_serverbrowser.gridwidth = 1;
        c_serverbrowser.gridheight = 2;
        c_serverbrowser.insets = new Insets(24,24,24,12);
        serverbrowser.setBackground(Color.decode("#FFFFFF"));
        serverbrowser.setMinimumSize(new Dimension(650,800));
        serverbrowser.setPreferredSize(new Dimension(650,800));

        cp.add(serverbrowser, c_serverbrowser);

        gamelistLabel = new JLabel("Active Games");
        c_gamelistLabel = new GridBagConstraints();
        gamelistLabel.setFont(f);
        gamelistLabel.setForeground(Color.decode("#616161"));
        c_gamelistLabel.fill = GridBagConstraints.BOTH;
        c_gamelistLabel.weightx = 1.0;
        c_gamelistLabel.weighty = 0.05;
		c_gamelistLabel.gridx = 0;
		c_gamelistLabel.gridy = 0;
        c_gamelistLabel.gridwidth = 4;
        c_gamelistLabel.gridheight = 1;
        c_gamelistLabel.insets = new Insets(8,16,0,0);
        serverbrowser.add(gamelistLabel, c_gamelistLabel);
        
        model = new DefaultListModel();
		serverlist = new JList(model);
		serverlist.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		serverlistpane = new JScrollPane(serverlist);
		update_server_list();

		c_serverlistpane = new GridBagConstraints();
		c_serverlistpane.fill = GridBagConstraints.BOTH;
		c_serverlistpane.weightx = 1.0;
		c_serverlistpane.weighty = 0.9;
		c_serverlistpane.gridx = 0;
		c_serverlistpane.gridy = 1;
        c_serverlistpane.gridwidth = 4;
        c_serverlistpane.gridheight =  1;
        serverbrowser.add(serverlistpane, c_serverlistpane);
        
        JOINGAME = new JButton("JOIN GAME");
		JOINGAME.addActionListener(this);
        JOINGAME.setForeground(Color.white);
		JOINGAME.setFocusPainted(false);
		JOINGAME.setBackground(Color.decode("#FF4081"));
        JOINGAME.setMinimumSize(new Dimension(175,40));
        JOINGAME.setPreferredSize(new Dimension(175,40));
		JOINGAME.setFont(bfont);
		c_joingamebutton = new GridBagConstraints();
		c_joingamebutton.fill = GridBagConstraints.NONE;
		c_joingamebutton.weightx = 0.25;
		c_joingamebutton.weighty = 0.05;
		c_joingamebutton.gridx = 0;
		c_joingamebutton.gridy = 2;
        c_joingamebutton.gridwidth = 1;
        c_joingamebutton.gridheight = 1;
        serverbrowser.add(JOINGAME, c_joingamebutton);
        
        CREATEGAME = new JButton("CREATE GAME");
		CREATEGAME.addActionListener(this);
        CREATEGAME.setForeground(Color.white);
		CREATEGAME.setFocusPainted(false);
		CREATEGAME.setBackground(Color.decode("#FF4081"));
        CREATEGAME.setMinimumSize(new Dimension(175,40));
        CREATEGAME.setPreferredSize(new Dimension(175,40));
		CREATEGAME.setFont(bfont);
		c_creategamebutton = new GridBagConstraints();
		c_creategamebutton.fill = GridBagConstraints.NONE;
		c_creategamebutton.weightx = 0.25;
		c_creategamebutton.weighty = 0.05;
		c_creategamebutton.gridx = 1;
		c_creategamebutton.gridy = 2;
        c_creategamebutton.gridwidth = 1;
        c_creategamebutton.gridheight = 1;
        serverbrowser.add(CREATEGAME, c_creategamebutton);
        
        REFRESH = new JButton("REFRESH");
		REFRESH.addActionListener(this);
        REFRESH.setForeground(Color.white);
		REFRESH.setFocusPainted(false);
		REFRESH.setBackground(Color.decode("#2F5398"));
        REFRESH.setMinimumSize(new Dimension(175,40));
        REFRESH.setPreferredSize(new Dimension(175,40));
		REFRESH.setFont(bfont);
		c_refreshbutton = new GridBagConstraints();
		c_refreshbutton.fill = GridBagConstraints.NONE;
		c_refreshbutton.weightx = 0.25;
		c_refreshbutton.weighty = 0.05;
		c_refreshbutton.gridx = 2;
		c_refreshbutton.gridy = 2;
        c_refreshbutton.gridwidth = 1;
        c_refreshbutton.gridheight = 1;
        serverbrowser.add(REFRESH, c_refreshbutton);

        HELP = new JButton("HELP");
		HELP.addActionListener(this);
        HELP.setForeground(Color.white);
		HELP.setFocusPainted(false);
		HELP.setBackground(Color.decode("#f34711"));
        HELP.setMinimumSize(new Dimension(175,40));
        HELP.setPreferredSize(new Dimension(175,40));
		HELP.setFont(bfont);
		c_helpbutton = new GridBagConstraints();
		c_helpbutton.fill = GridBagConstraints.NONE;
		c_helpbutton.weightx = 0.25;
		c_helpbutton.weighty = 0.05;
		c_helpbutton.gridx = 3;
		c_helpbutton.gridy = 2;
        c_helpbutton.gridwidth = 1;
        c_helpbutton.gridheight = 1;
        serverbrowser.add(HELP, c_helpbutton);
        
        
		/*serverbrowser.setLayout(new GridBagLayout());

		// Temporarily using defaultlistmodel. Will switch to a more suitable format once it is figured out
		
		

		serverbrowser.add(serverlistpane, c_serverlistpane);
		serverbrowser.add(JOINGAME, c_joingamebutton);
		serverbrowser.add(CREATEGAME, c_creategamebutton);*/
    }

    // make the userbox with user name, total score, logout, and help button
    public void makeUserBox(Container cp){
        c_userbox = new GridBagConstraints();
        userbox = new JPanel(new GridBagLayout());
        c_userbox.fill = GridBagConstraints.BOTH;
        c_userbox.weightx = 0.3;
        c_userbox.weighty = 0.3;
        c_userbox.gridx = 1;
        c_userbox.gridy = 1;
        c_userbox.gridwidth = 1;
        c_userbox.gridheight = 1;
        c_userbox.insets = new Insets(24,12,12,24);
        userbox.setBackground(Color.decode("#FFFFFF"));
        userbox.setMinimumSize(new Dimension(350,300));
        userbox.setPreferredSize(new Dimension(350,300));
        cp.add(userbox, c_userbox);
        
        welcomeLabel = new JLabel("Welcome <Username>!");
        c_welcomeLabel = new GridBagConstraints();
        welcomeLabel.setFont(f);
        welcomeLabel.setForeground(Color.decode("#616161"));
        c_welcomeLabel.fill = GridBagConstraints.BOTH;
        c_welcomeLabel.weightx = 1.0;
        c_welcomeLabel.weighty = 0.05;
		c_welcomeLabel.gridx = 0;
		c_welcomeLabel.gridy = 0;
        c_welcomeLabel.gridwidth = 1;
        c_welcomeLabel.gridheight = 1;
        c_welcomeLabel.insets = new Insets(8,16,0,0);
        
        scoreLabel = new JLabel("<Score>");
        c_scoreLabel = new GridBagConstraints();
        Font scoreFont = new Font("Arial",Font.BOLD, 60);
        scoreLabel.setFont(scoreFont);
        scoreLabel.setForeground(Color.decode("#009688"));
        c_scoreLabel.fill = GridBagConstraints.NONE;
        c_scoreLabel.anchor = GridBagConstraints.PAGE_END;
        c_scoreLabel.weightx = 1.0;
        c_scoreLabel.weighty = 0.5;
		c_scoreLabel.gridx = 0;
		c_scoreLabel.gridy = 1;
        c_scoreLabel.gridwidth = 1;
        c_scoreLabel.gridheight = 1;
        
        scorecapLabel = new JLabel("points");
        c_scorecapLabel = new GridBagConstraints();
        scorecapLabel.setFont(f);
        scorecapLabel.setForeground(Color.decode("#616161")); 
        c_scorecapLabel.fill = GridBagConstraints.NONE;
        c_scorecapLabel.anchor = GridBagConstraints.CENTER;
        c_scorecapLabel.weightx = 1.0;
        c_scorecapLabel.weighty = 0.2;
		c_scorecapLabel.gridx = 0;
		c_scorecapLabel.gridy = 2;
        c_scorecapLabel.gridwidth = 1;
        c_scorecapLabel.gridheight = 1;
        
        LOGOUT = new JButton("LOGOUT");
        c_logout = new GridBagConstraints();
        LOGOUT.setForeground(Color.white);
		LOGOUT.setFocusPainted(false);
		LOGOUT.setBackground(Color.decode("#FF4081"));
        LOGOUT.setMinimumSize(new Dimension(150,40));
        LOGOUT.setPreferredSize(new Dimension(150,40));
		LOGOUT.setFont(bfont);
		LOGOUT.addActionListener(this);
        c_logout.fill = GridBagConstraints.NONE;
        c_logout.anchor = GridBagConstraints.CENTER;
        c_logout.weightx = 1.0;
        c_logout.weighty = 0.25;
		c_logout.gridx = 0;
		c_logout.gridy = 3;
        c_logout.gridwidth = 1;
        c_logout.gridheight = 1;
        
		userbox.add(welcomeLabel,c_welcomeLabel);
		userbox.add(scoreLabel,c_scoreLabel);
		userbox.add(scorecapLabel, c_scorecapLabel);
		userbox.add(LOGOUT,c_logout);
        // need to obtain username somehow
        // need to obtain total score somehow
        // logout button

    }

    public void makeChatBox(Container cp) {
    	
        c_chatbox = new GridBagConstraints();
        chatbox = new JPanel(new GridBagLayout());
        c_chatbox.fill = GridBagConstraints.BOTH;
        c_chatbox.weightx = 0.3;
        c_chatbox.weighty = 0.65;
        c_chatbox.gridx = 1;
        c_chatbox.gridy = 2;
        c_chatbox.gridwidth = 1;
        c_chatbox.gridheight = 1;
        c_chatbox.insets = new Insets(12,12,24,24);
        chatbox.setBackground(Color.decode("#FFFFFF"));
        chatbox.setMinimumSize(new Dimension(350,600));
        chatbox.setPreferredSize(new Dimension(350,600));
        cp.add(chatbox, c_chatbox);
        

        chatLabel = new JLabel("Messenger");
        c_chatLabel = new GridBagConstraints();
        chatLabel.setFont(f);
        chatLabel.setForeground(Color.decode("#616161"));
        c_chatLabel.fill = GridBagConstraints.BOTH;
        c_chatLabel.weightx = 1.0;
        c_chatLabel.weighty = 0.05;
		c_chatLabel.gridx = 0;
		c_chatLabel.gridy = 0;
        c_chatLabel.gridwidth = 1;
        c_chatLabel.gridheight = 1;
        c_chatLabel.insets = new Insets(4,16,0,0);
        
        
		chatlogarea = new JTextArea();
		chatlogarea.setEditable(false);
		chatlogpane = new JScrollPane(chatlogarea);
		chatlogarea.setBorder(null);
		chatlogpane.setViewportBorder(null);
		c_chatlogpane = new GridBagConstraints();
		c_chatlogpane.fill = GridBagConstraints.BOTH;
		c_chatlogpane.weightx = 1.0;
		c_chatlogpane.weighty = 0.9;
		c_chatlogpane.gridx = 0;
		c_chatlogpane.gridy = 1;
        c_chatlogpane.gridwidth = 1;
        c_chatlogpane.gridheight = 1;
        /* Need to figure out how to display all the messages!*/

		chatinputfield = new JTextField();
		chatinputfield.addActionListener(this);
		chatinputfield.setBorder(null);
		c_chatinputfield = new GridBagConstraints();
		c_chatinputfield.fill = GridBagConstraints.BOTH;
		c_chatinputfield.weightx = 1.0;
		c_chatinputfield.weighty = 0.05;
		c_chatinputfield.gridx = 0;
		c_chatinputfield.gridy = 2;
        c_chatinputfield.gridwidth = 1;
        c_chatinputfield.gridheight = 1;

        chatbox.add(chatLabel, c_chatLabel);
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
            	if (gid == -1){
            		JOptionPane.showMessageDialog(this, "You have not selected a game yet!");
            	}else{
            		join_game(gid);
            	}
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
        } else if (b.equals(REFRESH)){
        	// sends a message to the server
        	// obtain the json
        	// use the json to fill a list of JPanels
        	// update the JList
        	//populate_game_listings();
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
