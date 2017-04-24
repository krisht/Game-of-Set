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

    private int getTrueSize(ArrayList<Integer> list) {
        int realSize = list.size();
        int rec = Collections.frequency(list, -1);
        return realSize - rec;
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

        if (getTrueSize(board) >= 21)
            return sendToFE().put("numAdded", added);

        if (getTrueSize(board) < 21 && getTrueSize(board) > 18) {
            total = addCards(21 - getTrueSize(board));
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
/*
    private JSONObject updateBoard(int c1, int c2, int c3) {

        int tmp1 = board.indexOf(c1);
        int tmp2 = board.indexOf(c2);
        int tmp3 = board.indexOf(c3);
        ArrayList<Integer> mytemps = new ArrayList<>();
        mytemps.add(tmp1);
        mytemps.add(tmp2);
        mytemps.add(tmp3);
        Collections.sort(mytemps);
        tmp1 = mytemps.get(0);
        tmp2 = mytemps.get(1);
        tmp3 = mytemps.get(2);
        int[] replaced = {tmp1, tmp2, tmp3};

        JSONObject obj = new JSONObject();

        if (deck.size() >= 3) {
            board.set(tmp1, deck.remove(0));
            board.set(tmp2, deck.remove(0));
            board.set(tmp3, deck.remove(0));
        }

        if (deck.size() == 2) {
            board.set(tmp1, deck.remove(0));
            board.set(tmp2, deck.remove(0));
            board.set(tmp3, -1);
        }

        if (deck.size() == 1) {
            board.set(tmp1, deck.remove(0));
            board.set(tmp2, -1);
            board.set(tmp3, -1);
        }

        if (deck.size() == 0) {
            board.set(tmp1, -1);
            board.set(tmp2, -1);
            board.set(tmp3, -1);
        }


        obj.put("posReplaced", replaced);
        return obj;

    }
    */


    private JSONObject updateBoard(int c1, int c2, int c3) {
        try {
            int temp;
            int tmp1 = board.indexOf(c1);
            int tmp2 = board.indexOf(c2);
            int tmp3 = board.indexOf(c3);
            ArrayList<Integer> mytemps = new ArrayList<>();
            mytemps.add(tmp1);
            mytemps.add(tmp2);
            mytemps.add(tmp3);
            Collections.sort(mytemps);
            tmp1 = mytemps.get(0);
            tmp2 = mytemps.get(1);
            tmp3 = mytemps.get(2);
            int[] replaced = {tmp1, tmp2, tmp3};
            JSONObject tmpObj = new JSONObject();
            if (board.size() <= 21) {

                if ((deck.size() >= 3) && (board.size() <= 12)) {

                    board.set(tmp1, deck.remove(0));
                    board.set(tmp2, deck.remove(0));
                    board.set(tmp3, deck.remove(0));
                } else if ((deck.size() >= 3) && (board.size() >= 13)) {

                    if (tmp1 > board.size() - 3) {

                        board.remove(tmp1);
                        board.remove(tmp2);
                        board.remove(tmp3);
                    } else if (tmp2 > board.size() - 3) {

                        board.remove(tmp3);
                        board.remove(tmp2);
                        temp = board.get(board.size() - 1);
                        board.add(tmp1, temp);
                        board.remove(tmp1+1);
                        board.remove(board.size() - 1);
                    } else if (tmp3 > board.size() - 3 ){

                        board.remove(tmp3);
                        temp = board.get(board.size() - 1);
                        board.add(tmp1, temp);
                        board.remove(tmp1+1);
                        board.remove(board.size() -1 );
                        temp = board.get(board.size() - 1);
                        board.add(tmp2, temp);
                        board.remove(tmp2+1);
                        board.remove(board.size() - 1);
                    } else {

                        board.set(tmp1, board.remove(board.size() - 1));
                        board.set(tmp2, board.remove(board.size() - 1));
                        board.set(tmp3, board.remove(board.size() - 1));
                    }
                }
    
                if (board.size() == 0) {
                    board.set(tmp1, -1);
                    board.set(tmp2, -1);
                    board.set(tmp3, -1);
                }
            } else {

            }

            tmpObj.put("posReplaced", replaced);
            return tmpObj;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
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
