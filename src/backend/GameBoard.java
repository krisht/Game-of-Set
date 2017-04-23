package backend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

class GameBoard {

    private ArrayList<Integer> board = new ArrayList<>();
    private ArrayList<Integer> deck = new ArrayList<>();
    private boolean initialized = false;

    /**
     * Initializes the gameboard with 12 cards
     *
     * @return JSONObject indicating that gameboard has been initialized
     */

    ArrayList<Integer> getBoard() {
        return this.board;
    }

    ArrayList<Integer> getDeck() {
        return this.deck;
    } 

    JSONObject initialize() {
        if (!initialized) {
            board.clear();
            deck.clear();
            generateCards();
            addCards(12);
            initialized = true;
        }

        JSONObject obj = sendToFE();
        obj.put("function", "GameBoard.initialize");
        obj.put("return", true);
        return obj;
    }

    /**
     * Generates and shuffles card in the deck
     */
    private void generateCards() {
        for (int ii = 0; ii < 81; ii++)
            deck.add(ii);
        Collections.shuffle(deck);
    }

    /**
     * Adds cards to the board on request
     *
     * @param numDeal Number of cards to deal out
     * @return Integer indicating the number of cards actually dealt out
     */
    JSONObject addCards(int numDeal) {
        //Return as JSON and pos replaced
        JSONObject retObj = new JSONObject();
        ArrayList<Integer> posReplaced = new ArrayList<>();

        int numAdded = 0;
        for (int ii = 0; ii < numDeal; ii++)
            if (deck.size() >= 1) {
                if (board.contains(-1)) {
                    posReplaced.add(board.indexOf(-1));
                    board.set(board.indexOf(-1), deck.remove(0));
                } else {
                    board.add(deck.remove(0));
                    posReplaced.add(deck.size() - 1);
                }
                numAdded++;
            }

        retObj.put("numAdded", numAdded);
        retObj.put("posReplaced", posReplaced.toArray()); //Ross fix this
        return retObj;
    }

    /**
     * Checks whether set is correct given each card id
     *
     * @param card1 Integer ID representing card 1
     * @param card2 Integer ID representing card 2
     * @param card3 Integer ID representing card 3
     * @return Boolean indicating whether set is correct or not
     */
    private boolean checkSet(int card1, int card2, int card3) {
        Card c1 = new Card(card1);
        Card c2 = new Card(card2);
        Card c3 = new Card(card3);

        boolean colorTest = ((((c1.getColor() == c2.getColor()) && (c1.getColor() == c3.getColor())) || ((c1.getColor() != c2.getColor())) && (c1.getColor() != c3.getColor()) && (c2.getColor() != c3.getColor())));
        boolean shapeTest = (((c1.getShape() == c2.getShape()) && (c1.getShape() == c3.getShape())) || ((c1.getShape() != c2.getShape()) && (c1.getShape() != c3.getShape()) && (c2.getShape() != c3.getShape())));
        boolean fillTest = (((c1.getFill() == c2.getFill()) && (c1.getFill() == c3.getFill())) || ((c1.getFill() != c2.getFill()) && (c1.getFill() != c3.getFill()) && (c2.getFill() != c3.getFill())));
        boolean numTest = (((c1.getNum() == c2.getNum()) && (c1.getNum() == c3.getNum())) || ((c1.getNum() != c2.getNum()) && (c1.getNum() != c3.getNum()) && (c2.getNum() != c3.getNum())));

        return (colorTest && fillTest && numTest && shapeTest);
    }

    /**
     * Requests card in the GameBoard
     *
     * @return JSONObject indicating relevant information
     * regarding requested cards
     */
    JSONObject requestCards() {
        JSONObject total;
        int added = 0;


        if (board.size() >= 21) //Fix card count to include negative ones
            return sendToFE().put("numAdded", added);

        if (board.size() < 21 && board.size() > 18) {
            total = addCards(21 - board.size());
            added = total.getInt("numAdded");
        } else {
            total = addCards(3);
            added = total.getInt("numAdded");
        }

        JSONObject obj = sendToFE();

        for (String key : total.keySet())
            obj.put(key, total.get(key));

        obj.put("numAdded", added);
        return obj;
    }

    /**
     * Processes submission given c1, c2, c3 Card ID integers
     *
     * @param c1 Integer ID representing card 1
     * @param c2 Integer ID representing card 2
     * @param c3 Integer ID representing card 3
     * @return JSONObject with relevant information
     */
    JSONObject processSubmission(int c1, int c2, int c3) { //maybe add json compatibility here instead of using ints
        if ((board.contains(c1) && board.contains(c2) && board.contains(c3)) && checkSet(c1, c2, c3)) {
            JSONObject obj1 = updateBoard(c1, c2, c3);
            JSONObject obj2 = sendToFE();

            for (String key : obj1.keySet())
                obj2.put(key, obj1.get(key));

            return obj2.put("setCorrect", true);
        }
        return sendToFE().put("setCorrect", false);
    }

    /**
     * Updates boards given that those cards are all correct
     *
     * @param c1 Integer ID representing card 1
     * @param c2 Integer ID representing card 2
     * @param c3 Integer ID representing card 3
     * @return JSONObject with relevant information
     */
    private JSONObject updateBoard(int c1, int c2, int c3) {
        int tmp1 = board.indexOf(c1);
        int tmp2 = board.indexOf(c2);
        int tmp3 = board.indexOf(c3);
        System.out.println("tmp1: " + tmp1);
        System.out.println("tmp2: " + tmp2);
        System.out.println("tmp3: " + tmp3);
        int[] replaced = {tmp1, tmp2, tmp3};
        JSONObject tmpObj = new JSONObject();

        if (board.size() <= 21) {
            if (deck.size() >= 3) {
                System.out.println("Board before removing: " + board);
                board.set(tmp1, deck.remove(0));
                board.set(tmp2, deck.remove(0));
                board.set(tmp3, deck.remove(0));
                System.out.println("Board after removing: " + board);
            }

            if (deck.size() == 0) {
                board.set(tmp1, -1);
                board.set(tmp2, -1);
                board.set(tmp3, -1);
            }
        }

        tmpObj.put("posReplaced", replaced);
        return tmpObj;
    }

    /**
     * Packs relevant information into a JSONObject
     *
     * @return JSONObject containing relevant information
     */
    JSONObject sendToFE() {
        JSONObject obj = new JSONObject();
        //obj.put("board", this.board.toArray());
        JSONArray jsonArray = new JSONArray(this.board);
        obj.put("board", jsonArray);
        return obj;
    }


}
