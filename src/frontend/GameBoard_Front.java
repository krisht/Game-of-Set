package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import static frontend.LoginPage.newConnectionThread;
import static frontend.LoginPage.uid;
import static frontend.LandingPage.gid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GameBoard_Front extends JFrame implements ActionListener, ItemListener{
	
	private JPanel header,gameboard, chatbox, leaderboard, board;
	private JButton NO_MORE_SETS, EXIT, SUBMIT;
	private Border blackline;
	private int total_cards_selected;
	private File folder = new File("./images");
	private File[] listOfFiles = folder.listFiles();
	private JPanel[][] panelHolder = new JPanel[7][3];
	private JToggleButton[] buttonGrid = new JToggleButton[21];
	private int[] cardIds = new int[21];
	private ArrayList<Integer> selectedLocations = new ArrayList<Integer>();
    private HashMap card_to_filename = new HashMap();
    private int game_uid, game_gid;
    
    private GraphicsEnvironment ge;
    
    private static int SQUIGGLE = 0;
    private static int OVAL = 1;
    private static int DIAMOND = 2;

    private static int SOLID = 0;
    private static int STRIPE = 1;
    private static int OUTLINE = 2;

    private static int RED = 0;
    private static int GREEN = 1;
    private static int PURPLE = 2;
	// make a map int : Card
    
	public GameBoard_Front(){
		 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     setSize(1280, 960);
	     game_uid = uid;
	     game_gid = gid;
	     ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	     ge.getAllFonts();

	     
	     total_cards_selected = 0;
	     Container cp = this.getContentPane();
	     cp.setLayout(new GridBagLayout());
	     fillhashMap();
	     makeHeader(cp); 
	     makeGameboard(cp);
		 makeLeaderboard(cp);
		 makeChatBox(cp);
		 makeGameBox(cp);
	}
	
	// make the first header row that contains the name and such 
	public void makeHeader(Container cp){
		header = new JPanel(new GridBagLayout());
	    EXIT = new JButton("Exit");
	    
	    JLabel LABEL_SETGAME = new JLabel("Set Game");
	    Font font = new Font("Arial", Font.PLAIN, 20);
		GridBagConstraints c = new GridBagConstraints();
		GridBagConstraints c_exit = new GridBagConstraints();
		GridBagConstraints c_label = new GridBagConstraints();
	    
		header.setBackground(Color.decode("#009688"));
	    LABEL_SETGAME.setForeground(Color.white);
	    LABEL_SETGAME.setFont(font);
		
		c.fill = GridBagConstraints.BOTH;
	    c.weightx = 1;
	    c.weighty = 0.05;
	    c.gridx = 0;
	    c.gridy = 0;
	    c.gridwidth = 2;
	    c.gridheight = 1;
	    
	    c_exit.fill = GridBagConstraints.BOTH;
	    c_exit.weightx = 0.05;
	    c_exit.weighty = 1;
	    c_exit.gridx = 0;
	    c_exit.gridy = 0;
	    c_exit.gridwidth = 1;
	    c_exit.gridheight = 1;
	    
	    c_label.fill = GridBagConstraints.BOTH;
	    c_label.weightx = 0.95;
	    c_label.weighty = 1;
	    c_label.gridx = 1;
	    c_label.gridy = 0;
	    c_label.gridwidth = 1;
	    c_label.gridheight = 1;
	    c_label.insets = new Insets(0,12,0,0);
	    
	    cp.add(header, c); 
	    header.add(EXIT,c_exit);
	    header.add(LABEL_SETGAME, c_label);
	}
	
	
	// makes the gameboard that conatins the 21 cards, and 2 buttons 
	public void makeGameboard(Container cp){

	    gameboard = new JPanel(new GridBagLayout());
	    board = new JPanel(new GridLayout(7,3));
	    NO_MORE_SETS = new JButton("No More Sets");
	    SUBMIT = new JButton("Submit");
	    

		GridBagConstraints c = new GridBagConstraints();
		GridBagConstraints c_no_sets = new GridBagConstraints();
		GridBagConstraints c_submit = new GridBagConstraints();
		GridBagConstraints c_title = new GridBagConstraints();
		GridBagConstraints c_board = new GridBagConstraints();
		Font font = new Font("Arial", Font.BOLD, 12);
		Font font2 = new Font("Arial",Font.PLAIN, 25);
		JLabel title = new JLabel("Gameboard");
		
		
		gameboard.setBackground(Color.decode("#FFFFFF"));
		title.setFont(font);
		title.setForeground(Color.decode("#616161"));
		SUBMIT.setForeground(Color.white);
		SUBMIT.setFocusPainted(false);
		SUBMIT.setBackground(Color.decode("#8BC34A"));
		SUBMIT.setBorderPainted(false);
		SUBMIT.setFont(font2);
		NO_MORE_SETS.setForeground(Color.white);
		NO_MORE_SETS.setFocusPainted(false);
		NO_MORE_SETS.setBackground(Color.decode("#F44336"));
		NO_MORE_SETS.setBorderPainted(false);
		NO_MORE_SETS.setFont(font2);
		
		
	    c.fill = GridBagConstraints.BOTH;
	    c.weightx = 0.7;
	    c.weighty = 0.6;
	    c.gridx = 0;
	    c.gridy = 1;
	    c.gridwidth = 1;
	    c.gridheight = 2;
	    c.insets = new Insets(24,24,12,24);
	    

	    c_title.fill = GridBagConstraints.BOTH;
	    c_title.weightx = 1;
	    c_title.weighty = 0.05;
	    c_title.gridx = 0;
	    c_title.gridy = 0;
	    c_title.gridwidth = 2;
	    c_title.gridheight = 1;
	    c_title.insets = new Insets(4,16,0,0);
	    
	    c_board.fill = GridBagConstraints.BOTH;
	    c_board.weightx = 1;
	    c_board.weighty = 0.95;
	    c_board.gridx = 0;
	    c_board.gridy = 1;
	    c_board.gridwidth = 2;
	    c_board.gridheight = 1;
	    c_board.insets = new Insets(12,16,0,16);
	    
	    c_no_sets.fill = GridBagConstraints.BOTH;
	    c_no_sets.weightx = 0.5;
	    c_no_sets.weighty = 0.05;
	    c_no_sets.gridx = 0;
	    c_no_sets.gridy = 2;
	    c_no_sets.gridwidth = 1;
	    c_no_sets.gridheight = 1;
	    c_no_sets.insets = new Insets(24,16,12,16);
	    
	    c_submit.fill = GridBagConstraints.BOTH;
	    c_submit.weightx = 0.5;
	    c_submit.weighty = 0.05;
	    c_submit.gridx = 1;
	    c_submit.gridy = 2;
	    c_submit.gridwidth = 1;
	    c_submit.gridheight = 1;
	    c_submit.insets = new Insets(24,16,12,16);
	    
	    cp.add(gameboard, c);
	    gameboard.add(title, c_title);
	    gameboard.add(NO_MORE_SETS, c_no_sets);
	    gameboard.add(SUBMIT, c_submit);
	    gameboard.add(board, c_board);
	    
	    // make 21 slots
	    
	    // THIS IS WHERE WE CALL BACKEND FUNCTION TO POPULATE THE BOARD
	    /*for (int i = 0 ; i < 7; i++){
	    	for (int j = 0 ;j < 3; j++){
	    		panelHolder[i][j] = new JPanel();
	    		board.add(panelHolder[i][j]);
	    	}
	    
	    }
	    //JSONObject initial_board = myconn.messageServer(obj);
	    JSONArray card_list;
	    System.out.println(initial_board.toString());
	    try {
	    	card_list = new JSONArray(initial_board.get("board"));
			for (int i = 0; i < card_list.length(); i++){
		    	addCard((Integer)card_list.get(i), gameboard, i);
		    }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	
	// make the chatbox
	public void makeChatBox(Container cp){
		GridBagConstraints c = new GridBagConstraints();
	    chatbox = new JPanel();
	    c.fill = GridBagConstraints.BOTH;
	    c.weightx = 1;
	    c.weighty = 0.3;
	    c.gridx = 0;
	    c.gridy = 3;
	    c.gridwidth = 2;
	    c.gridheight = 1;
	    c.insets = new Insets(12,24,24,24);
	    chatbox.setBackground(Color.decode("#FFFFFF"));
	    cp.add(chatbox, c);
	   
	}
	
	// make the leaderboard
	public void makeLeaderboard(Container cp){
		GridBagConstraints c = new GridBagConstraints();
	    leaderboard = new JPanel();
	    c.fill = GridBagConstraints.BOTH;
	    c.weightx = 0.3;
	    c.weighty = 0.5;
	    c.gridx = 1;
	    c.gridy = 2;
	    c.gridwidth = 1;
	    c.gridheight = 1;
	    c.insets = new Insets(24,12,12,24);
	    leaderboard.setBackground(Color.decode("#FFFFFF"));
	    cp.add(leaderboard, c);
		
	}
	
	// makes the extra box that contains the name of the game and 
	// other stuff you wanna put in there
	public void makeGameBox(Container cp){
		GridBagConstraints c = new GridBagConstraints();
	    leaderboard = new JPanel();
	    c.fill = GridBagConstraints.BOTH;
	    c.weightx = 0.3;
	    c.weighty = 0.15;
	    c.gridx = 1;
	    c.gridy = 1;
	    c.gridwidth = 1;
	    c.gridheight = 1;
	    c.insets = new Insets(24,12,12,24);
	    leaderboard.setBackground(Color.decode("#FFFFFF"));
	    cp.add(leaderboard, c);
	}
	
	// This function fills in a hash map of numbers of cards against its filenames
	public void fillhashMap(){
		String filename = "";
		for (int i = 0 ; i < 81; i++){
			if (i /27 == RED){
				filename = "red_";
			}else if (i / 27 == GREEN){
				filename = "green_";
			}else{
				filename = "purple_";
			}
			if ((i % 9) / 3 == SOLID){
				filename += "solid_";
			}else if ((i % 9) / 3 == STRIPE){
				filename += "striped_";
			}else{
				filename += "hollow_";
			}
			
			if ((i % 3) == 0){
				filename += "one_";
			}else if ((i % 3) == 1){
				filename += "two_";
			}else{
				filename += "three_";
			}
			
			if ((i % 27) / 9 == SQUIGGLE){
				filename += "curvy.bmp";
			}else if ((i%27)/9 == OVAL){
				filename += "rect.bmp";
			}else{
				filename += "diam.bmp";
			}
			card_to_filename.put(i, filename);
		}
	}
	
	
	
	public void addCard(int cardId, JPanel board, int location){
		int rNum = Math.round(location/3);
		int cNum = location %3;
		buttonGrid[location] = new JToggleButton();
		cardIds[location] = cardId;
        buttonGrid[location].addItemListener(this);
		BufferedImage img = null;
		try {
			System.out.println(card_to_filename.get(cardId));
		    img = ImageIO.read(new File("./src/images/"+card_to_filename.get(cardId)));
		    // create card class here
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon imageIcon = new ImageIcon(img);
		buttonGrid[location].setIcon(imageIcon);
		buttonGrid[location].setOpaque(false);
		buttonGrid[location].setContentAreaFilled(false);
		//buttonGrid[location].setBorderPainted(false);
		buttonGrid[location].setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
		panelHolder[rNum][cNum].setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
		panelHolder[rNum][cNum].add(buttonGrid[location]);
	}
	
	
	public BufferedImage createImage(JPanel panel) {

	    int w = panel.getWidth();
	    int h = panel.getHeight();
	    BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = bi.createGraphics();
	    panel.paint(g);
	    return bi;
	}
	public void itemStateChanged(ItemEvent e){

        JToggleButton JTB = (JToggleButton)e.getSource();
        if (e.getStateChange() == ItemEvent.SELECTED){
        	JTB.setSelected(true);
        	total_cards_selected += 1;
        	for (int i = 0 ; i < 21; i++){
        		if (buttonGrid[i] == JTB){
        			selectedLocations.add(i);
        			break;
        		}
        	}
            JTB.setBorder(blackline);
        	if (total_cards_selected == 3){
        		
        		//cc.messageServer(caller);
        		// NOTE: Then what?
        		try {
        			for (int locations : selectedLocations){
						buttonGrid[locations].setSelected(false);
						total_cards_selected -= 3;
					}
					//if (response.getBoolean("setCorrect")){
					//	System.out.print("You are correct!");
					//}else{
						
					//	System.out.print("You are wrong!");
					//}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        }else{
            JTB.setSelected(false);
	       	total_cards_selected -= 1;
	       	JTB.setBorder(null);
        }
    }
	
	// This function handles button presses in GameBoard_Front
	// When submit is selected 
	
	@Override 
	public void actionPerformed(ActionEvent ae) {
		
		
		JButton b = (JButton)ae.getSource();
		if (b.equals(NO_MORE_SETS)){
			
		}else if (b.equals(SUBMIT)){
			// if the submit button is clicked, we need to check multiple things
			
			if (total_cards_selected < 3){
				// Show error if not enough cards are selected
				JOptionPane.showMessageDialog(this, "Not enough cards to form a set", "Error", JOptionPane.ERROR_MESSAGE);
			}else if (total_cards_selected > 3){
				// Show error if too many cards are selected
				JOptionPane.showMessageDialog(this, "Please only select 3 cards!", "Error", JOptionPane.ERROR_MESSAGE);
			}else{
				// enough cards! Put everything in a JSON
				// include uid, gid, c1, c2 and c3
				newConnectionThread.userSubmission(cardIds[selectedLocations.get(1)], cardIds[selectedLocations.get(2)], 
						cardIds[selectedLocations.get(3)]);
        		// should i freeze and wait?
			}
			
		}else if (b.equals(EXIT)){
			// Check that both fields are present

			
		}
	}
	
		
	

}
