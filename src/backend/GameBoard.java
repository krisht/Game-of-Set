package backend;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

class GameBoard {

    private ArrayList<Integer> board = new ArrayList<>();
    private ArrayList<Integer> deck = new ArrayList<>();
    private boolean initialized = false;

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

    private void generateCards() {
        for (int ii = 0; ii < 81; ii++)
            deck.add(ii);
        Collections.shuffle(deck);
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

    JSONObject requestCards() {
        int added = 0;

        if (board.size() >= 21)
            return sendToFE().put("numAdded", added);

        if (board.size() < 21 && board.size() > 18)
            added = addCards(21 - board.size());
        else added = addCards(3);

        JSONObject obj = sendToFE();
        obj.put("numAdded", added);
        return obj;
    }

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

    private JSONObject updateBoard(int c1, int c2, int c3) {
        int tmp1 = board.indexOf(c1);
        int tmp2 = board.indexOf(c2);
        int tmp3 = board.indexOf(c3);
        int[] replaced = {tmp1, tmp2, tmp3};
        JSONObject tmpObj = new JSONObject();

        if (board.size() <= 12 && deck.size() > 0) {
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
        }

        tmpObj.put("posReplaced", replaced);
        return tmpObj;
    }

    private JSONObject sendToFE() {
        JSONObject obj = new JSONObject();
        obj.put("board", this.board.toArray());
        return obj;
    }


}
