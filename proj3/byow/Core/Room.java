package byow.Core;


import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

/**
 * This class is a basic unit that comprises a world in BYOW.
 */
public class Room {
    private int width;
    private int height;
    private Position pos;

    /**
     * Constructs a room with given position and dimensions.
     */
    Room(Position pos, int width, int height) {
        this.width = width;
        this.height = height;
        this.pos = pos;
    }

    /**
     * Draw the room to the given world (tiles).
     */
    public void draw(TETile[][] tiles) {
        for (int dx = 0; dx < width; dx += 1) {
            for (int dy = 0; dy < height; dy += 1) {
                tiles[pos.getX() + dx][pos.getY() + dy] = Tileset.FLOOR;
            }
        }
    }
}
