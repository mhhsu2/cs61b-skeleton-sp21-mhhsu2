package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

/**
 * This class is a basic unit, hallway, that comprises a world in BYOW.
 */
public class Hallway {
    private int length;
    private Position pos;
    private String orientation;

    /**
     * Constructs a hallway with a given position and dimensions.
     * The position address is the starting point of the hallway.
     */
    Hallway(int length, Position pos, String orientation) {
        this.length = length;
        this.pos = pos;
        this.orientation = orientation;
    }

    /**
     * Draws this hallway to the given world (tiles).
     */
    public void draw(TETile[][] tiles) {
        switch (orientation) {
            case "North":
                for (int dy = 0; dy < length; dy++) {
                    if (isOutOfBorder(tiles, pos.getX(), pos.getY() + dy)) {
                        drawWall(tiles, pos.getX()-1, pos.getY() + dy);
                        tiles[pos.getX()][pos.getY() + dy] = Tileset.FLOOR;
                        drawWall(tiles, pos.getX()+1, pos.getY() + dy);
                    }
                }
                break;
            case "South":
                for (int dy = 0; dy < length; dy++) {
                    if (isOutOfBorder(tiles, pos.getX(), pos.getY() - dy)) {
                        drawWall(tiles, pos.getX() - 1, pos.getY() - dy);
                        tiles[pos.getX()][pos.getY() - dy] = Tileset.FLOOR;
                        drawWall(tiles, pos.getX() + 1, pos.getY() - dy);
                    }
                }
                break;
            case "West":
                for (int dx = 0; dx < length; dx++) {
                    if (isOutOfBorder(tiles, pos.getX() - dx, pos.getY())) {
                        drawWall(tiles, pos.getX() - dx, pos.getY() - 1);
                        tiles[pos.getX() - dx][pos.getY()] = Tileset.FLOOR;
                        drawWall(tiles, pos.getX() - dx, pos.getY() + 1);
                    }
                }
                break;
            case "East":
                for (int dx = 0; dx < length; dx++) {
                    if (isOutOfBorder(tiles, pos.getX() + dx, pos.getY())) {
                        drawWall(tiles, pos.getX() + dx, pos.getY() - 1);
                        tiles[pos.getX() + dx][pos.getY()] = Tileset.FLOOR;
                        drawWall(tiles, pos.getX() + dx, pos.getY() + 1);
                    }
                }
                break;

        }
    }

    // Get Methods
    public String getOrientation() {
        return this.orientation;
    }

    /**
     * Returns the end position of this hallway.
     */
    public Position getEndPos() {
        int endX;
        int endY;
        switch (orientation) {
            case "North" -> {
                endX = pos.getX();
                endY = pos.getY() + length - 1;
                return new Position(endX, endY);
            }
            case "South" -> {
                endX = pos.getX();
                endY = pos.getY() - length + 1;
                return new Position(endX, endY);
            }
            case "West" -> {
                endX = pos.getX() - length + 1;
                endY = pos.getY();
                return new Position(endX, endY);
            }
            case "East" -> {
                endX = pos.getX() + length - 1;
                endY = pos.getY();
                return new Position(endX, endY);
            }
        }
        return null;
    }

    // Helper Methods

    /**
     * Sets a tile to Tileset.WALL if the tile is not FLOOR.
     */
    private void drawWall(TETile[][] tiles, int x, int y) {
        if (tiles[x][y] == Tileset.FLOOR) {
            return;
        }
        tiles[x][y] = Tileset.WALL;
    }

    /**
     * Validates if a given position is out of border.
     */
    private boolean isOutOfBorder(TETile[][] tiles, int x, int y) {
        int maxX = tiles.length;
        int maxY = tiles[0].length;

        if (x < 0 || x > maxX) {
            return false;
        }

        if (y < 0 || y > maxY) {
            return false;
        }
        return true;
    }


}