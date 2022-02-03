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
     * Constructs a room with a given position and dimensions.
     * The position address is the bottom left corner of the room.
     */
    Room(Position pos, int width, int height) {
        this.width = width;
        this.height = height;
        this.pos = pos;
    }

    /**
     * Draws the room to the given world (tiles).
     */
    public void draw(TETile[][] tiles) {
        for (int dx = 0; dx < width; dx += 1) {
            for (int dy = 0; dy < height; dy += 1) {
                tiles[getMinX() + dx][getMinY() + dy] = Tileset.FLOOR;
            }
        }

        buildWall(tiles);
    }

    /**
     * Returns true if the room is out of the border of the world.
     */
    public boolean isOutOfBoundary(World world) {
        if (getMaxX() > world.getWidth()) {
            return true;
        }
        if (getMaxY() > world.getHeight()) {
            return true;
        }
        if (getMinX() < 0) {
            return true;
        }
        if (getMinY() < 0) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if this room is overlapped with other objects on a world.
     */
    public boolean isOverlapped(TETile[][] tiles) {
        boolean rv = false;
        for (int dx = 0; dx < width; dx += 1) {
            for (int dy = 0; dy < height; dy += 1) {
                rv = isOccupied(tiles, getMinX() + dx, getMinY() + dy);
            }
        }
        return rv;
    }

    // Get methods for instance variables

    private int getMaxY() {
        return pos.getY() + height - 1;
    }
    private int getMinY() {
        return pos.getY();
    }
    private int getMaxX() {
        return pos.getX() + width - 1;
    }
    private int getMinX() {
        return pos.getX();
    }

    // Helper methods

    /**
     * Builds the top/bottom/left/right walls of a room
     * by substituting a one-tile-thick layer
     * of the room with Tileset.WALL.
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

    /**
     * Returns true if the tile at x and y has something there.
     * Else returns false.
     */
    private boolean isOccupied(TETile[][] tiles, int x, int y) {
        // Checks if the tile is occupied by or not.
        return !tiles[x][y].equals(Tileset.NOTHING);
    }

}
