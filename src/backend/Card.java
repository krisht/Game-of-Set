package backend;

class Card {
    private int color;
    private int shape;
    private int fill;
    private int num;

    Card(int id) {
        this(id / 27, (id % 27) / 9, (id % 9) / 3, id % 3);
    }

    private Card(int color, int shape, int fill, int num) {
        this.color = color;
        this.shape = shape;
        this.fill = fill;
        this.num = num;
    }

    /**
     * Gets this card's color
     * @return Integer representing the color of the card
     */
    int getColor() {
        return this.color;
    }

    /**
     * Gets this card's shape
     * @return Integer representing the shape of the card
     */
    int getShape() {
        return this.shape;
    }

    /**
     * Gets this card's texture
     * @return Integer representing the texture of the card
     */
    int getFill() {
        return this.fill;
    }

    /**
     * Gets the number of items on the card
     * @return Integer representing the number of items on the card
     */
    int getNum() {
        return this.num;
    }

}
