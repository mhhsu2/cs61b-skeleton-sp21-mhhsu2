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

        buildWall(tiles);
    }

    // Get methods for instance variables

    private int getMaxY() {
        return pos.getY() + height - 1;
    }
    private int getMinY() {
        return pos.getX();
    }

    private int getMaxX() {
        return pos.getX() + width - 1;
    }
    private int getMinX() {
        return pos.getX();
    }

    // Helper methods

    /**
     * Builds the top/bottom/left/right walls of a room.
     */
    private void buildWall(TETile[][] tiles) {
        /* Top wall */
        for (int x = getMinX(); x <= getMaxX(); x += 1) {
            tiles[x][getMaxY()] = Tileset.WALL;
        }

        /* Bottom wall */
        for (int x = getMinX(); x <= getMaxX(); x += 1) {
            tiles[x][getMinY()] = Tileset.WALL;
        }

        /* Left wall */
        for (int y = getMinY(); y <= getMaxY(); y += 1) {
            tiles[getMinX()][y] = Tileset.WALL;
        }

        /* Right wall */
        for (int y = getMinY(); y <= getMaxY(); y += 1) {
            tiles[getMaxX()][y] = Tileset.WALL;
        }
    }


}
