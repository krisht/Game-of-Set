package frontend;

import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static frontend.LandingPage.gid;
import static frontend.LoginPage.newConnectionThread;
import static frontend.LoginPage.uid;

public class GameBoard_Front extends JFrame implements ActionListener{

    // this is the variable to be changed for list of card IDs
    static ArrayList<Integer> list_of_cardids = new ArrayList<Integer>();
    static ArrayList<Friends> list_of_users = new ArrayList<Friends>();
    static int posinlist = -1;
    private final int SQUIGGLE = 0;
    private final int OVAL = 1;
    private final int DIAMOND = 2;
    private final int SOLID = 0;
    private final int STRIPE = 1;
    private final int OUTLINE = 2;
    private final int RED = 0;
    private final int GREEN = 1;
    private final int PURPLE = 2;
    // header stuff
	private JPanel header;
	private JLabel titleLabel, creatorLabel;
	private GridBagConstraints c_header, c_titleLabel, c_creatorLabel;
	// gameboard stuff
	private JPanel game, gameboard;
	private JLabel gameLabel;
	private JButton NO_MORE_SETS, EXIT, SUBMIT, HELP;
	private GridBagConstraints c_game, c_gameboard, c_gameLabel, c_gameboard_pane;
	private GridBagConstraints c_submitbutton, c_exitbutton, c_helpbutton, c_nomoresetsbutton;
	private HashMap location_to_card = new HashMap<Integer, Integer>();
	private ArrayList<JButton> list_of_card_buttons = new ArrayList<JButton>();
	private ArrayList<JPanel> list_of_user_panels = new ArrayList<JPanel>();
	private ArrayList<Integer> selectedLocations = new ArrayList<Integer>();
	// chatbox stuff
	private JPanel chatbox;
	private JLabel chatLabel;
	private JTextField chatinputfield;
	private JTextArea chatlogarea;
	private JScrollPane chatlogpane;
	private GridBagConstraints c_chatbox, c_chatLabel, c_chatlogpane, c_chatinputfield;
	// leaderboard stuff
	private JPanel leaderboard, u1, u2, u3, u4;
	private JLabel leaderboardLabel, name1, name2, name3, name4, score1, score2, score3, score4;
	private GridBagConstraints c_leaderboard, c_leaderboardLabel;
	private ActionListener listener;
	// others
    private Font f, bfont;
    private int velX = 2;
    private int velY = 2;
    private Timer tm;
	private int[] cardIds = new int[21];
    private HashMap card_to_filename = new HashMap<Integer, Integer>();
    private int game_uid, game_gid;
    private GraphicsEnvironment ge;
	// make a map int : Card
    
	public GameBoard_Front(){
		 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     setSize(1280, 960);
	     tm = new Timer(5, this);
	     game_uid = uid;
	     game_gid = gid;
	     ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	     ge.getAllFonts();
	     f = new Font("Arial", Font.BOLD, 12);
	     bfont = new Font("Arial",Font.PLAIN, 18);
	     
	     listener = new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                for (int i = 0 ; i < list_of_card_buttons.size(); i++){
	                	if (e.getSource() == list_of_card_buttons.get(i)){
	                		// see whether it was selected, if it was, deselect it
	                		int selectedId = (int) location_to_card.get(i);
	                		int alreadySelected = 0;
	                		for (int j = 0 ; j < selectedLocations.size(); j++){
	                			if (selectedId == selectedLocations.get(j)){
	                				alreadySelected = 1;
	                				selectedLocations.remove(j);
	                				break;
	                			}
	                		}
	                		// set the appropriate colors base on whether it was already selected or not
	                		if (alreadySelected == 1){
	                			list_of_card_buttons.get(i).setBorder(null);
	                		}else{
	                			list_of_card_buttons.get(i).setBorder(BorderFactory.createLineBorder(Color.decode("#009688"),5));
                				selectedLocations.add(selectedId);
                            }
	                	break;	
	                	}
	                }
	            }
	        };
	     Container cp = this.getContentPane();
	     cp.setLayout(new GridBagLayout());
	     fillhashMap();
	     makeHeader(cp); 
	     makeGameboard(cp);
		 makeLeaderboard(cp);
		 makeChatBox(cp);
		 // for (int i = 0 ; i < 6; i++){
		 //	list_of_cardids.add(i);
		 // }
		 updateGameBoard();
		 updateLeaderboard();
	}

// SOUND CAN BE ADDED HERE!
//	private static synchronized void playSound() {
//
//		try {
//			InputStream in = new FileInputStream("./src/sounds/correct.wav");
//			AudioStream as = new AudioStream(in);
//			AudioPlayer.player.start(as);
//			// Probably need to wait here before shutting off sound
//		} catch(IOException ex){
//			ex.printStackTrace();
//
//		}
//	}

	// updates the leaderboard base on list_of_users;
	public void updateLeaderboard(){
		
    	for (int i = 0 ; i < list_of_user_panels.size(); i++){
    		leaderboard.remove(list_of_user_panels.get(i));
    	}
		list_of_user_panels.clear();
    	this.repaint();
    	this.revalidate();
		
		int numUsers = list_of_users.size();
		for (int i = 0 ; i < numUsers; i++){
			GridBagConstraints c_panel = new GridBagConstraints();
    		c_panel.fill = GridBagConstraints.NONE;
    		c_panel.weightx = 1.0;
    		c_panel.weighty = 1.0;
    		c_panel.gridx = 0;
    		c_panel.gridy = i+1;
    		c_panel.gridwidth = 1;
    		c_panel.gridheight = 1;
			leaderboard.add(make_score_board(list_of_users.get(i).getName(), 
					list_of_users.get(i).getScore(),
					list_of_users.get(i).getNo_more_sets()), c_panel);
		}
		while (numUsers < 4){
			GridBagConstraints c_panel = new GridBagConstraints();
    		c_panel.fill = GridBagConstraints.NONE;
    		c_panel.weightx = 1.0;
    		c_panel.weighty = 1.0;
    		c_panel.gridx = 0;
    		c_panel.gridy = numUsers+1;
    		c_panel.gridwidth = 1;
    		c_panel.gridheight = 1;
    		numUsers += 1;
    		leaderboard.add(make_score_board(null, -1, -1), c_panel);
		}
    	this.repaint();
    	this.revalidate();
	}

	// move a card by animation from original location to new location
	/*public void moveCards(int original_x, int original_y, int new_x, int new_y, JButton card){
		// if new_x is less than original_x, move left
		// if new_y is less than original_y, move up
		tm.start();
		int newVelX = velX;
		int newVelY = velY;
		int incX = original_x;
		int incY = original_y;
		if (new_x < original_x){
			newVelX = -velX;
		}
		if (new_y < original_y){
			newVelY = -velY;
		}

		while (incX != new_x || incY != new_y){
			System.out.println("In while loop moving the card");
			if (incX != new_x){
				incX = incX + newVelX;
			}
			if (incY != new_y){
				incY = incY + newVelY;
			}
			System.out.println("originalX: " + original_x + " originalY: " + original_y +" incX:  " + incX + " incY : " + incY + " newX : " + new_x + " newY: " + new_y + " newVelX: " + newVelX + " newVelY: " + newVelY);

			card.setLocation(incX, incY);
			this.repaint();
		}
		tm.stop();
	}

	// 3 new cards are added to the set
	public void add3Cards(ArrayList<Integer> newCards){
		// move the previous n cards upwards
	}

	// 3 cards are removed and no new cards come in
	public void remove3Cards(ArrayList<Integer> oldCards){
		int counter = 0;
		ArrayList<JButton> old_list_of_card_buttons = new ArrayList<JButton>(list_of_card_buttons);
		ArrayList<JButton> new_list_of_card_buttons = new ArrayList<JButton>();
		ArrayList<Pair<Integer, Integer>> old_card_locations = new ArrayList<>();
		ArrayList<Pair<Integer, Integer>> new_card_locations = new ArrayList<>();
		list_of_card_buttons.clear();
		location_to_card.clear();
		int column_counter = 0;
		int row_counter = 0;
		// update gameboard but make new stuff invisible
		for (int i = 0 ; i < list_of_card_buttons.size(); i++){
    		gameboard.remove(list_of_card_buttons.get(i));
    	}
    	list_of_card_buttons.clear();
    	this.repaint();
    	this.revalidate();
		// populate the gameboard with new cards
    	while (counter < list_of_cardids.size()){
        	GridBagConstraints c_panel = new GridBagConstraints();
    		c_panel.fill = GridBagConstraints.NONE;
    		c_panel.weightx = 1.0;
    		c_panel.weighty = 1.0;
    		c_panel.gridx = column_counter;
    		c_panel.gridy = row_counter;
    		c_panel.gridwidth = 1;
    		c_panel.gridheight = 1;
    		list_of_card_buttons.add(make_card_panel(list_of_cardids.get(counter)));
            location_to_card.put(counter, list_of_cardids.get(counter));
            gameboard.add(list_of_card_buttons.get(counter), c_panel);
            list_of_card_buttons.get(counter).setVisible(false);
    		column_counter += 1;
    		if (column_counter == 3){
    			column_counter = 0;
    			row_counter += 1;
    		}
    		counter += 1;
    	}
		// get old location and new location, but make the new location cards invisible first
		for (int i = 0 ; i < old_list_of_card_buttons.size(); i++){
			// if the card still exist, then I need its old location and new location
			if (list_of_cardids.contains(oldCards.get(i))){
				old_card_locations.add(new Pair(old_list_of_card_buttons.get(i).getX(), old_list_of_card_buttons.get(i).getY()));
				new_card_locations.add(new Pair(gameboard.getComponent(i).getX(), gameboard.getComponent(i).getY()));
			}
		}
		// make cards that is gone disappear
		// make old card disappear
		// make animation of card moving from old location to new location
		// make new card appear

		list_of_card_buttons.clear();
		location_to_card.clear();
		// remove the cards
		// move the remaining cards around


		for (int i = 0 ; i < old_list_of_card_buttons.size(); i++){
			// check whether oldCards[i] still exist in list_of_cardsids
				// if it still exist, move it around
				GridBagConstraints c_panel = new GridBagConstraints();
				c_panel.fill = GridBagConstraints.NONE;
				c_panel.weightx = 1.0;
				c_panel.weighty = 1.0;
				c_panel.gridx = column_counter;
				c_panel.gridy = row_counter;
				c_panel.gridwidth = 1;
				c_panel.gridheight = 1;
				gameboard.add(old_list_of_card_buttons.get(i), c_panel);
				new_list_of_card_buttons.add((JButton)gameboard.getComponent(counter));
				System.out.println("Old Button: " + old_list_of_card_buttons.get(i));
				System.out.println("New Button: " + new_list_of_card_buttons.get(counter));
				moveCards(old_list_of_card_buttons.get(i).getX(),
						  old_list_of_card_buttons.get(i).getY(),
						  new_list_of_card_buttons.get(counter).getX(),
						  new_list_of_card_buttons.get(counter).getY(),
						  old_list_of_card_buttons.get(i));
				// update location_to_card
				gameboard.add(old_list_of_card_buttons.get(i), c_panel);
				list_of_card_buttons.add(new_list_of_card_buttons.get(counter));
				location_to_card.put(counter, list_of_cardids);
				this.repaint();
				this.revalidate();
				column_counter += 1;
				if (column_counter == 3){
					column_counter = 0;
					row_counter += 1;
				}
				counter ++;
			}
		// remove all the list_of_card_buttons that shouldn't be there anymore
	}*/

	// updates the gameboard (and later the leaderboard)
	public void updateGameBoard(){
		// if there are old cards, get rid of all of them
		int counter = 0;
    	int column_counter = 0;
    	int row_counter = 0;
    	location_to_card.clear();
    	selectedLocations.clear();
    	for (int i = 0 ; i < list_of_card_buttons.size(); i++){
    		gameboard.remove(list_of_card_buttons.get(i));
    	}
    	list_of_card_buttons.clear();
    	this.repaint();
    	this.revalidate();
		// populate the gameboard with new cards
    	while (counter < list_of_cardids.size()){
        	GridBagConstraints c_panel = new GridBagConstraints();
    		c_panel.fill = GridBagConstraints.NONE;
    		c_panel.weightx = 1.0;
    		c_panel.weighty = 1.0;
    		c_panel.gridx = column_counter;
    		c_panel.gridy = row_counter;
    		c_panel.gridwidth = 1;
    		c_panel.gridheight = 1;
    		list_of_card_buttons.add(make_card_panel(list_of_cardids.get(counter)));
            location_to_card.put(counter, list_of_cardids.get(counter));
            gameboard.add(list_of_card_buttons.get(counter), c_panel);
    		column_counter += 1;
    		if (column_counter == 3){
    			column_counter = 0;
    			row_counter += 1;
    		}
    		counter += 1;
    	}
    	/*for (int i = counter; i < 21; i++){
    		GridBagConstraints c_panel = new GridBagConstraints();
    		c_panel.fill = GridBagConstraints.NONE;
    		c_panel.weightx = 1.0;
    		c_panel.weighty = 1.0;
    		c_panel.gridx = column_counter;
    		c_panel.gridy = row_counter;
    		c_panel.gridwidth = 1;
    		c_panel.gridheight = 1;
    		list_of_card_buttons.add(make_card_panel(-1));
    		gameboard.add(list_of_card_buttons.get(i), c_panel);
    		column_counter += 1;
    		if (column_counter == 3){
    			column_counter = 0;
    			row_counter += 1;
    		}
    	}*/
    	// serverlistpane.add(list_of_games_panel);
    	this.repaint();
        this.revalidate();
	}
	
	public JButton make_card_panel(int cid){
		JButton new_button = new JButton();
		if (cid != -1){
		try {
			BufferedImage img = null;
			img = ImageIO.read(new File("./src/images/"+card_to_filename.get(cid)));
			ImageIcon imageIcon = new ImageIcon(img);
			new_button.setIcon(imageIcon);
			new_button.addActionListener(listener);
		    // create card class here
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		new_button.setOpaque(false);
		new_button.setBorder(null);
		new_button.setContentAreaFilled(false);
		//buttonGrid[location].setBorderPainted(false);
        new_button.setMinimumSize(new Dimension(135,90));
        new_button.setPreferredSize(new Dimension(135,90));
        new_button.setMaximumSize(new Dimension(135,90));
        return new_button;
	}
	
	public JPanel make_score_board(String uname, int score, int no_more_set){
		JPanel new_panel = new JPanel(new GridBagLayout());
		GridBagConstraints c_name = new GridBagConstraints();
		GridBagConstraints c_score = new GridBagConstraints();
		JLabel new_name, new_score;
		new_panel.setMinimumSize(new Dimension(350, 60));
		new_panel.setMaximumSize(new Dimension(350, 60));
		new_panel.setPreferredSize(new Dimension(350, 60));
		new_panel.setBackground(Color.WHITE);
		if (score == -1){
			// make an empty board
			new_name = new JLabel(" ");
			new_score = new JLabel(" ");
		}else{
			// make a normal board
			new_name = new JLabel(uname);
			new_score = new JLabel(String.valueOf(score));
		}
		new_name.setFont(bfont);
		new_score.setFont(bfont);
		if (no_more_set == 1){
			new_name.setForeground(Color.decode("#F44336"));
			new_score.setForeground(Color.decode("#F44336"));
		}
		c_name.fill = GridBagConstraints.NONE;
		c_name.anchor = GridBagConstraints.LINE_START;
		c_name.weightx = 0.75;
		c_name.weighty = 1.0;
		c_name.gridx = 0;
		c_name.gridy = 0;
		c_name.gridwidth = 1;
		c_name.gridheight = 1;
		new_panel.add(new_name, c_name);

		c_score.fill = GridBagConstraints.NONE;
		c_score.anchor = GridBagConstraints.LINE_END;
		c_score.weightx = 0.25;
		c_score.weighty = 1.0;
		c_score.gridx = 1;
		c_score.gridy = 0;
		c_score.gridwidth = 1;
		c_score.gridheight = 1;
		new_panel.add(new_score, c_score);
		list_of_user_panels.add(new_panel);
		return new_panel;
		
	}
	
	// makes the gameboard that conatins the 21 cards, and 2 buttons 
	public void makeGameboard(Container cp){
		
		c_game = new GridBagConstraints();
        game = new JPanel(new GridBagLayout());
        c_game.fill = GridBagConstraints.BOTH;
        c_game.weightx = 0.7;
        c_game.weighty = 0.9;
        c_game.gridx = 0;
        c_game.gridy = 1;
        c_game.gridwidth = 1;
        c_game.gridheight = 2;
        c_game.insets = new Insets(24,24,24,12);
        game.setBackground(Color.decode("#FFFFFF"));
        game.setMinimumSize(new Dimension(650,800));
        game.setMaximumSize(new Dimension(650,800));
        game.setPreferredSize(new Dimension(650,800));

        cp.add(game, c_game);

        gameLabel = new JLabel("Gameboard");
        c_gameLabel = new GridBagConstraints();
        gameLabel.setFont(f);
        gameLabel.setForeground(Color.decode("#616161"));
        c_gameLabel.fill = GridBagConstraints.BOTH;
        c_gameLabel.weightx = 1.0;
        c_gameLabel.weighty = 0.05;
		c_gameLabel.gridx = 0;
		c_gameLabel.gridy = 0;
        c_gameLabel.gridwidth = 4;
        c_gameLabel.gridheight = 1;
        c_gameLabel.insets = new Insets(8,16,0,0);
        game.add(gameLabel, c_gameLabel);
        
        gameboard = new JPanel(new GridBagLayout());
        gameboard.setBackground(Color.decode("#CFD8DC"));
		gameboard.setBorder(null);
        gameboard.setMinimumSize(new Dimension(500,700));
        gameboard.setMaximumSize(new Dimension(500,700));
        gameboard.setPreferredSize(new Dimension(500,700));
		
		c_gameboard = new GridBagConstraints();
		c_gameboard.fill = GridBagConstraints.BOTH;
		c_gameboard.weightx = 1.0;
		c_gameboard.weighty = 0.9;
		c_gameboard.gridx = 0;
		c_gameboard.gridy = 1;
        c_gameboard.gridwidth = 4;
        c_gameboard.gridheight =  1;
        c_gameboard.insets = new Insets(16,16,16,16);
        game.add(gameboard, c_gameboard);
        
        SUBMIT = new JButton("SUBMIT");
		SUBMIT.addActionListener(this);
        SUBMIT.setForeground(Color.white);
		SUBMIT.setFocusPainted(false);
		SUBMIT.setBackground(Color.decode("#4CAF50"));
        SUBMIT.setMinimumSize(new Dimension(175,40));
        SUBMIT.setPreferredSize(new Dimension(175,40));
        SUBMIT.setMaximumSize(new Dimension(175,40));
		SUBMIT.setFont(bfont);
		c_submitbutton = new GridBagConstraints();
		c_submitbutton.fill = GridBagConstraints.NONE;
		c_submitbutton.weightx = 0.25;
		c_submitbutton.weighty = 0.05;
		c_submitbutton.gridx = 0;
		c_submitbutton.gridy = 2;
        c_submitbutton.gridwidth = 1;
        c_submitbutton.gridheight = 1;
        game.add(SUBMIT, c_submitbutton);
        
        NO_MORE_SETS = new JButton("NO MORE SETS");
        NO_MORE_SETS.addActionListener(this);
        NO_MORE_SETS.setForeground(Color.white);
        NO_MORE_SETS.setFocusPainted(false);
        NO_MORE_SETS.setBackground(Color.decode("#F44336"));
        NO_MORE_SETS.setMinimumSize(new Dimension(175,40));
        NO_MORE_SETS.setPreferredSize(new Dimension(175,40));
        NO_MORE_SETS.setMaximumSize(new Dimension(175, 40));
        NO_MORE_SETS.setFont(bfont);
		c_nomoresetsbutton = new GridBagConstraints();
		c_nomoresetsbutton.fill = GridBagConstraints.NONE;
		c_nomoresetsbutton.weightx = 0.25;
		c_nomoresetsbutton.weighty = 0.05;
		c_nomoresetsbutton.gridx = 1;
		c_nomoresetsbutton.gridy = 2;
		c_nomoresetsbutton.gridwidth = 1;
		c_nomoresetsbutton.gridheight = 1;
        game.add(NO_MORE_SETS, c_nomoresetsbutton);
        
        EXIT = new JButton("EXIT");
		EXIT.addActionListener(this);
        EXIT.setForeground(Color.white);
		EXIT.setFocusPainted(false);
		EXIT.setBackground(Color.decode("#2f5398"));
        EXIT.setMinimumSize(new Dimension(175,40));
        EXIT.setMaximumSize(new Dimension(175, 40));
        EXIT.setPreferredSize(new Dimension(175,40));
		EXIT.setFont(bfont);
		c_exitbutton = new GridBagConstraints();
		c_exitbutton.fill = GridBagConstraints.NONE;
		c_exitbutton.weightx = 0.25;
		c_exitbutton.weighty = 0.05;
		c_exitbutton.gridx = 2;
		c_exitbutton.gridy = 2;
        c_exitbutton.gridwidth = 1;
        c_exitbutton.gridheight = 1;
        game.add(EXIT, c_exitbutton);

        HELP = new JButton("HELP");
		HELP.addActionListener(this);
        HELP.setForeground(Color.white);
		HELP.setFocusPainted(false);
		HELP.setBackground(Color.decode("#f34711"));
        HELP.setMinimumSize(new Dimension(175,40));
        HELP.setMaximumSize(new Dimension(175, 40));
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
        game.add(HELP, c_helpbutton);
	    
	    // make 21 slots
	}
	
	
	// make the chatbox
	public void makeChatBox(Container cp){
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
	
	// make the leaderboard
	public void makeLeaderboard(Container cp){
		c_leaderboard = new GridBagConstraints();
        leaderboard = new JPanel(new GridBagLayout());
        c_leaderboard.fill = GridBagConstraints.BOTH;
        c_leaderboard.weightx = 0.3;
        c_leaderboard.weighty = 0.3;
        c_leaderboard.gridx = 1;
        c_leaderboard.gridy = 1;
        c_leaderboard.gridwidth = 1;
        c_leaderboard.gridheight = 1;
        c_leaderboard.insets = new Insets(24,12,12,24);
        leaderboard.setBackground(Color.decode("#FFFFFF"));
        leaderboard.setMinimumSize(new Dimension(350,300));
        leaderboard.setPreferredSize(new Dimension(350,300));
        leaderboard.setMaximumSize(new Dimension(350,300));
        cp.add(leaderboard, c_leaderboard);
		
        leaderboardLabel = new JLabel("Leaderboard");
        c_leaderboardLabel = new GridBagConstraints();
        leaderboardLabel.setFont(f);
        leaderboardLabel.setForeground(Color.decode("#616161"));
        c_leaderboardLabel.fill = GridBagConstraints.BOTH;
        c_leaderboardLabel.weightx = 1.0;
        c_leaderboardLabel.weighty = 0.05;
		c_leaderboardLabel.gridx = 0;
		c_leaderboardLabel.gridy = 0;
        c_leaderboardLabel.gridwidth = 1;
        c_leaderboardLabel.gridheight = 1;
        c_leaderboardLabel.insets = new Insets(8,16,0,0);
        leaderboard.add(leaderboardLabel, c_leaderboardLabel);
        
	}
	

	
	// make the first header row that contains the name and such 
	public void makeHeader(Container cp){
		
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
	
	
	// This function handles button presses in GameBoard_Front
	// When submit is selected 
	
	@Override 
	public void actionPerformed(ActionEvent ae) {
		
		
		JButton b = (JButton)ae.getSource();
		if (b.equals(NO_MORE_SETS)){
            nomoresetsRequest();
		}else if (b.equals(SUBMIT)){
			// if the submit button is clicked, we need to check multiple things
			int total_cards_selected = selectedLocations.size();
			if (total_cards_selected < 3){
				// Show error if not enough cards are selected
				JOptionPane.showMessageDialog(this, "Not enough cards to form a set", "Error", JOptionPane.ERROR_MESSAGE);
			}else if (total_cards_selected > 3){
				// Show error if too many cards are selected
				JOptionPane.showMessageDialog(this, "Please only select 3 cards!", "Error", JOptionPane.ERROR_MESSAGE);
			}else{
                usersubmitsRequest();
			}
			
		}else if (b.equals(EXIT)){
            leavegameRequest();
		}
	}

	public void nomoresetsRequest() {
        JSONObject nomoresetsobj = new JSONObject();
        nomoresetsobj.put("fCall", "noMoreSets");
        nomoresetsobj.put("uid", uid);
        nomoresetsobj.put("gid", gid);
        try {
            newConnectionThread.messageServer(nomoresetsobj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void usersubmitsRequest() {
	    JSONObject usersubmitsobj = new JSONObject();
	    usersubmitsobj.put("fCall", "userSubmits");
	    usersubmitsobj.put("uid", uid);
	    usersubmitsobj.put("gid", gid);
	    usersubmitsobj.put("c1", selectedLocations.get(0));
	    usersubmitsobj.put("c2", selectedLocations.get(1));
	    usersubmitsobj.put("c3", selectedLocations.get(2));
        try {
            newConnectionThread.messageServer(usersubmitsobj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void leavegameRequest() {
	    JSONObject leavegameobj = new JSONObject();
	    leavegameobj.put("fCall", "leaveGame");
	    leavegameobj.put("uid", uid);
	    leavegameobj.put("gid", gid);
    }


		
	

}
