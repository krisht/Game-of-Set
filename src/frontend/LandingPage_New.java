package frontend;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static frontend.LoginPage.newConnectionThread;
import static frontend.LoginPage.uid;
import static frontend.LoginPage.username;


/*
 * Landing page with "Welcome User" title
 */

public class LandingPage_New extends JFrame implements ActionListener {

    private JPanel header, serverbrowser, chatbox, userbox;
    private GridBagConstraints c_userbox;
    private GridBagConstraints c_header, c_serverbrowser, c_chatbox, c_serverlistpane, c_joingamebutton, c_creategamebutton, c_chatlogpane, c_chatinputfield;
    private JButton LOGOUT, JOINGAME, CREATEGAME;
    private JLabel userMessage, titleLabel, creatorLabel;
    private JList serverlist;
    private JScrollPane chatlogpane;
    //private Border blackline;

    private String gameName;

    static DefaultListModel model;
    static JScrollPane serverlistpane;
    static JTextArea chatlogarea;
    static JTextField chatinputfield;
    static int gid;

    public LandingPage_New() {

        // blah
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 960);
        gid = 0;

        Container cp = this.getContentPane();
        cp.setLayout(new GridBagLayout());

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
        //c_header.weightx = 1;
        //c_header.weighty = 0.1;
        c_header.gridx = 0;
        c_header.gridy = 0;
        c_header.ipadx = 0;
        c_header.ipady = 0;
        c_header.gridwidth = 2;
        c_header.gridheight = 1;
        header.setBackground(Color.decode("#009688"));
        cp.add(header, c_header);

        titleLabel = new JLabel("SET");
        Font f = new Font("Arial",Font.BOLD, 60);
        titleLabel.setFont(f);
        titleLabel.setForeground(Color.WHITE);
        GridBagConstraints c_title = new GridBagConstraints();
        c_title.anchor = GridBagConstraints.LINE_START;
        c_title.fill = GridBagConstraints.NONE;
        c_title.gridwidth = 1;
        c_title.gridheight = 1;
        c_title.weightx = 1;
        c_title.weighty = 1;
        c_title.gridx = 0;
        c_title.gridy = 0;
        c_title.ipadx = 0;
        c_title.ipady = 0;
        c_title.insets = new Insets(0,24,0,0);
        header.add(titleLabel, c_title);


        creatorLabel = new JLabel("by rosskaplan, krisht, abhinavj30, brendabrandy");
        Font f2 = new Font("Arial",Font.PLAIN, 13);
        creatorLabel.setFont(f2);
        creatorLabel.setForeground(Color.WHITE);
        GridBagConstraints c_creator = new GridBagConstraints();
        c_creator.anchor = GridBagConstraints.LAST_LINE_END;
        c_creator.fill = GridBagConstraints.NONE;
        c_creator.gridwidth = 1;
        c_creator.gridheight = 1;
        c_creator.weightx = 1;
        c_creator.weighty = 1;
        c_creator.gridx = 1;
        c_creator.gridy = 0;
        c_creator.ipadx = 0;
        c_creator.ipady = 0;
        c_creator.insets = new Insets(0,0,10,24);
        header.add(creatorLabel, c_creator);

	    /*Font font = new Font ("Arial", Font.PLAIN, 18);

		userMessage = new JLabel("Logged in as " + username + ".");
		header.add(userMessage);
	    LOGOUT = new JButton("Logout");
		LOGOUT.addActionListener(this);
	    header.add(LOGOUT);*/

    }

    public void makeServerBrowser(Container cp) {

        c_serverbrowser = new GridBagConstraints();
        serverbrowser = new JPanel(new GridBagLayout());
        c_serverbrowser.fill = GridBagConstraints.BOTH;
        c_serverbrowser.weightx = 0.6;
        c_serverbrowser.weighty = 0.9;
        c_serverbrowser.gridx = 0;
        c_serverbrowser.gridy = 1;
        c_serverbrowser.gridwidth = 1;
        c_serverbrowser.gridheight = 2;
        c_serverbrowser.insets = new Insets(24,24,24,12);
        serverbrowser.setBackground(Color.decode("#FFFFFF"));
        cp.add(serverbrowser, c_serverbrowser);

		/*serverbrowser.setLayout(new GridBagLayout());

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
		serverbrowser.add(CREATEGAME, c_creategamebutton);*/
    }

    // make the userbox with user name, total score, logout, and help button
    public void makeUserBox(Container cp){
        c_userbox = new GridBagConstraints();
        userbox = new JPanel();
        c_userbox.fill = GridBagConstraints.BOTH;
        c_userbox.weightx = 0.4;
        c_userbox.weighty = 0.4;
        c_userbox.gridx = 1;
        c_userbox.gridy = 1;
        c_userbox.gridwidth = 1;
        c_userbox.gridheight = 1;
        c_userbox.insets = new Insets(24,12,12,24);
        userbox.setBackground(Color.decode("#FFFFFF"));
        cp.add(userbox, c_userbox);

    }

    public void makeChatBox(Container cp) {
        c_chatbox = new GridBagConstraints();
        chatbox = new JPanel(new GridBagLayout());
        c_chatbox.fill = GridBagConstraints.BOTH;
        c_chatbox.weightx = 0.4;
        c_chatbox.weighty = 0.45;
        c_chatbox.gridx = 1;
        c_chatbox.gridy = 2;
        c_chatbox.gridwidth = 1;
        c_chatbox.gridheight = 1;
        c_chatbox.insets = new Insets(12,12,24,24);
        chatbox.setBackground(Color.decode("#FFFFFF"));
        cp.add(chatbox, c_chatbox);

		/*chatbox.setLayout(new GridBagLayout());

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
		chatbox.add(chatinputfield, c_chatinputfield);*/


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
