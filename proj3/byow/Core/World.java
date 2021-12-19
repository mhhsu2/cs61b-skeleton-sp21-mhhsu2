package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

/**
 * This class represents a world in BYOW.
 * This class manges objects such as rooms and hallways in a world.
 */
public class World {

    private int width;
    private int height;
    private TETile[][] tiles;
//    private SomeDataStructures rooms:

    /**
     * Constructs a initialized world.
     */
    World(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new TETile[width][height];
        initWorld();
    }

    /**
     * Initializes tiles.
     */
    private void initWorld() {
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    /**
     * Adds a room that is not overlapped with
     * the existing rooms to the world.
     */
    public void addRoom(Position pos, int width, int height) {
        Room room = new Room(pos, width, height);
        room.draw(tiles);

    }

    /**
     * Get methods for instance variables.
     */
    public TETile[][] getTiles() {
        return this.tiles;
    }
}
