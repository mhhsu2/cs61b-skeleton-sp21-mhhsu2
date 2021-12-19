package byow.Core;

public class Position {
    private int x;
    private int y;

    /**
     * Constructs a position object with the given x and y.
     */
    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get methods for instance variables.
     */
    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
