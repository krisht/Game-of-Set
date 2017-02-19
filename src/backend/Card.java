package backend;

import java.awt.*;

public class Card {

    public static int SQUIGGLE = 0;
    public static int OVAL = 1;
    public static int DIAMOND = 2;

    public static int SOLID = 0;
    public static int STRIPE = 1;
    public static int OUTLINE = 2;

    private int color;
    private int shape;
    private int fill;
    private int num;

    public Card(int id){
        this(id/27, (id%27)/9, (id%9)/3, id%3);
    }

    public Card(int color, int shape, int fill, int num) {
        this.color = color;
        this.shape = shape;
        this.fill = fill;
        this.num = num;
    }

    public int getColor() {
        return this.color;
    }

    public int getShape() {
        return this.shape;
    }

    public int getFill() {
        return this.fill;
    }

    public int getNum() {
        return this.num;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setShape(int shape) {
        this.shape = shape;
    }

    public void setFill(int fill) {
        this.fill = fill;
    }

    public void setNum(int num) {
        this.num = num;
    }

}
