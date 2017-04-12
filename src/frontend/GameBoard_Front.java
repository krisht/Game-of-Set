package frontend;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.border.Border;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import backend.Card;
import backend.GameBoard;

import javax.imageio.ImageIO;
import javax.swing.*;

public class GameBoard_Front extends JFrame implements ActionListener, ItemListener{
	
	private JPanel header, gameboard, chatbox, gamestats;
	private GridLayout board;
	private GameBoard gb;
	private GridBagConstraints c_header, c_gameboard, c_chatbox, c_gamestats;
	private JButton NO_MORE_CARDS, EXIT;
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
    
    public static int SQUIGGLE = 0;
    public static int OVAL = 1;
    public static int DIAMOND = 2;

    public static int SOLID = 0;
    public static int STRIPE = 1;
    public static int OUTLINE = 2;

    public static int RED = 0;
    public static int GREEN = 1;
    public static int PURPLE = 2;
	// make a map int : Card
    
	public GameBoard_Front(int uid, int gid){
		 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     setSize(1280, 960);
	     gb = new GameBoard();
	     game_uid = uid;
	     game_gid = gid;
	     ClientConnThreaded newConnectionThread = new ClientConnThreaded(uid, gid);
	     newConnectionThread.start();
	     total_cards_selected = 0;
	     Container cp = this.getContentPane();
	     cp.setLayout(new GridBagLayout());
	     fillhashMap();
		 blackline = BorderFactory.createLineBorder(Color.black);
	     makeHeaderPanel(cp);
	     makeGameBoardPanel(cp);
	     makeChatBox(cp);
	     makeGameStats(cp);
	}
	
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
	
	// Make header here
	public void makeHeaderPanel(Container cp){
		c_header = new GridBagConstraints();
	    header = new JPanel();
	    c_header.weightx = 1;
	    c_header.weighty = 0.1;
	    c_header.fill = GridBagConstraints.BOTH;
	    c_header.gridx = 0;
	    c_header.gridy = 0;
	    c_header.gridwidth = 2;
	    c_header.gridheight = 1;
	    header.setBorder(blackline);
	    cp.add(header, c_header);
	    
	    NO_MORE_CARDS = new JButton("No More Sets");
	    NO_MORE_CARDS.addActionListener(this);
	    header.add(NO_MORE_CARDS);
	    EXIT = new JButton("Exit");
	    header.add(EXIT);
	}
	// Make Gameboard here
	public void makeGameBoardPanel(Container cp){
		c_gameboard = new GridBagConstraints();
	    gameboard = new JPanel();
	    c_gameboard.fill = GridBagConstraints.BOTH;
	    c_gameboard.weightx = 0.7;
	    c_gameboard.weighty = 0.9;
	    c_gameboard.gridx = 0;
	    c_gameboard.gridy = 1;
	    c_gameboard.gridwidth = 1;
	    c_gameboard.gridheight = 2;
	    gameboard.setBorder(blackline);
	    cp.add(gameboard, c_gameboard);
	    // make 21 slots
	    gameboard.setLayout(new GridLayout(7,3));
	    // THIS IS WHERE WE CALL BACKEND FUNCTION TO POPULATE THE BOARD
	    for (int i = 0 ; i < 7; i++){
	    	for (int j = 0 ;j < 3; j++){
	    		panelHolder[i][j] = new JPanel();
	    		gameboard.add(panelHolder[i][j]);
	    	}
	    
	    }
	    JSONObject initial_board = gb.initialize();
	    JSONArray card_list;
		
	    try {
			card_list = initial_board.getJSONArray("board");
			for (int i = 0; i < card_list.length(); i++){
		    	addCard(card_list.optInt(i), gameboard, i);
		    }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	// Make chatbox here
	public void makeChatBox(Container cp){
	     c_chatbox = new GridBagConstraints();
	     chatbox = new JPanel();
	     c_chatbox.fill = GridBagConstraints.BOTH;
	     c_chatbox.weightx = 0.3;
	     c_chatbox.weighty = 0.45;
	     c_chatbox.gridx = 1;
	     c_chatbox.gridy = 1;
	     c_chatbox.gridwidth = 1;
	     c_chatbox.gridheight = 1;
	     chatbox.setBorder(blackline);
	     cp.add(chatbox, c_chatbox);
		
	}
	
	// Make game statistics chart here
	public void makeGameStats(Container cp){

	     c_gamestats = new GridBagConstraints();
	     gamestats = new JPanel();
	     c_gamestats.fill = GridBagConstraints.BOTH;
	     c_gamestats.weightx = 0.3;
	     c_gamestats.weighty = 0.45;
	     c_gamestats.gridx = 1;
	     c_gamestats.gridy = 2;
	     c_gamestats.gridwidth = 1;
	     c_gamestats.gridheight = 1;
		 gamestats.setBorder(blackline);
	     cp.add(gamestats, c_gamestats);
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
        		int[] card_nums = new int[3];
        		for (int i = 0 ; i < 3; i++){
					card_nums[i] = cardIds[selectedLocations.get(i)];
                	// obtain information about the card
        			// call backend submissions
        		}
        		// make said JSON
        		JSONObject caller = new JSONObject();
        		JSONArray paramList = new JSONArray();
        		caller.put("fCall", "processSubmission");
        		caller.put("c1", card_nums[1]);
        		caller.put("c2", card_nums[2]);
        		caller.put("c3", card_nums[3]);
        		cc.messageServer(caller);
        		// NOTE: Then what?
        		try {
        			for (int locations : selectedLocations){
						buttonGrid[locations].setSelected(false);
						total_cards_selected -= 3;
					}
					if (response.getBoolean("setCorrect")){
						System.out.print("You are correct!");
					}else{
						
						System.out.print("You are wrong!");
					}
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
	
	public void actionPerformed(ActionEvent ae) {

		// Fetch the username and password from the textboxes
		
		JButton b = (JButton)ae.getSource();
		if (b.equals(NO_MORE_CARDS)){
			JSONObject moreCards = gb.requestCards();
			System.out.print(moreCards);
			JSONArray card_list;
			    try {
					card_list = moreCards.getJSONArray("board");
					for (int i = 0; i < card_list.length(); i++){
				    	addCard(card_list.optInt(i), gameboard, i);
				    }
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}else if (b.equals(EXIT)){
			// Check that both fields are present

			
		}
	}

}
