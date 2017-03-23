package backend;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class GameBoard {

    private ArrayList<Integer> board = new ArrayList<>();
    private ArrayList<Integer> deck = new ArrayList<>();
    private boolean initialized = false;

    public JSONObject initialize() {
        if (!initialized) {
            int added = 0;
            while (added != 12) {
                board.clear();
                deck.clear();
                generateCards();
                added = addCards(12);
            }
            initialized = true;
        }
        return sendToFE();
    }

    public JSONObject requestCards() {
        int added = addCards(3);
        return sendToFE().put("numAdded", added);
    }

    public JSONObject processSubmission(int c1, int c2, int c3) { //maybe add json compatibility here instead of using ints
        if ((board.contains(c1) && board.contains(c2) && board.contains(c3)) && checkSet(c1, c2, c3)) {
            JSONObject obj1 = updateBoard(c1, c2, c3);
            JSONObject obj2 = sendToFE();

            for (String key : obj1.keySet())
                obj2.put(key, obj1.get(key));

            return obj2.put("setCorrect", true);
        }
        return sendToFE().put("setCorrect", false);
    }


    private JSONObject updateBoard(int c1, int c2, int c3) {
        int tmp1 = board.indexOf(c1);
        int tmp2 = board.indexOf(c2);
        int tmp3 = board.indexOf(c2);
        JSONObject tmpObj = new JSONObject();

        if (board.size() < 12 && deck.size() > 0) {
            if (deck.size() >= 3) {
                board.set(tmp1, deck.get(0));
                board.set(tmp2, deck.get(0));
                board.set(tmp3, deck.get(0));
                int[] replaced = {tmp1, tmp2, tmp3};
                tmpObj.put("replaced", replaced);
                return tmpObj;
            }

            if (deck.size() == 2) {
                board.set(tmp1, deck.get(0));
                board.set(tmp2, deck.get(0));
                board.set(tmp3, -1);
                int[] replaced = {tmp1, tmp2};
                tmpObj.put("replaced", replaced);
                return tmpObj;
            }

            if (deck.size() == 1) {
                board.set(tmp1, deck.get(0));
                board.set(tmp2, -1);
                board.set(tmp3, -1);
                int[] replaced = {tmp1};
                tmpObj.put("replaced", replaced);
                return tmpObj;
            }
        }

        int[] replaced = {};
        board.set(tmp1, -1);
        board.set(tmp2, -1);
        board.set(tmp3, -1);
        tmpObj.put("posReplaced", replaced);
        return tmpObj;
    }


    private int addCards(int numDeal) {
        int numAdded = 0;
        for (int ii = 0; ii < numDeal; ii++)
            if (deck.size() >= 1) {
                board.add(deck.remove(0));
                numAdded++;
            }
        return numAdded;
    }

    private void generateCards() {
        for (int ii = 0; ii < 81; ii++)
            deck.add(ii);
        Collections.shuffle(deck);
    }

    private JSONObject sendToFE() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("board", this.board);
        } catch (JSONException exc) {
            System.err.println("Error on trying to make JSON Object!");
            exc.printStackTrace();
        }
        return obj;
    }

    private boolean checkSet(int card1, int card2, int card3) {
        Card c1 = new Card(card1);
        Card c2 = new Card(card2);
        Card c3 = new Card(card3);

        boolean colorTest = (c1.getColor() == c2.getColor() && c2.getColor() == c3.getColor()) || (c1.getColor() != c2.getColor() && c2.getColor() != c3.getColor() && c1.getColor() != c3.getColor());
        boolean fillTest = (c1.getFill() == c2.getFill() && c2.getFill() == c3.getFill()) || (c1.getFill() != c2.getFill() && c2.getFill() != c3.getFill() && c1.getFill() != c3.getFill());
        boolean numTest = (c1.getNum() == c2.getNum() && c2.getNum() == c3.getNum()) || (c1.getNum() != c2.getNum() && c2.getNum() != c3.getNum() && c1.getNum() != c3.getNum());
        boolean shapeTest = (c1.getShape() == c2.getShape() && c2.getShape() == c3.getShape()) || (c1.getShape() != c2.getShape() && c2.getShape() != c3.getShape() && c1.getShape() != c3.getShape());

        return (colorTest && fillTest && numTest && shapeTest);
    }

    private void removeSet(int c1, int c2, int c3) {
        board.remove(new Integer(c1));
        board.remove(new Integer(c2));
        board.remove(new Integer(c3));
    }


}
