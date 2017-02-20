package backend;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class GameBoard {

    private ArrayList<Integer> board = new ArrayList<>();
    private ArrayList<Integer> deck = new ArrayList<>();

    public JSONObject initialize() {
        generateCards();
        addCards(12);
        return sendToFE();
    }

    public JSONObject processSubmission(int c1, int c2, int c3) { //maybe add json compatibility here instead of using ints
        if ((board.contains(c1) && board.contains(c2) && board.contains(c3)) && checkSet(c1, c2, c3)) {
            removeSet(c1, c2, c3);
            if (board.size() < 12 && deck.size() > 0)
                addCards(3);
            return sendToFE().append("correctSet", true); // Everything's guch
        }
        return sendToFE().append("correctSet", false);
    }

    private void addCards(int numDeal) {
        for (int ii = 0; ii < numDeal; ii++)
            board.add(deck.remove(0));
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
            System.out.println("Error on trying to make JSON Object!");
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
