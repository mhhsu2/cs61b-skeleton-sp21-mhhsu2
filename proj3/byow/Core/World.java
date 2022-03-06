package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.HashSet;
import java.util.Random;

/**
 * This class represents a world in BYOW.
 * This class manges objects such as rooms and hallways in a world.
 */
public class World {

    private int width;
    private int height;
    private TETile[][] tiles;
    private HashSet<Room> rooms;
    private long seed;
    private Random random;

    private static final int MIN_ROOM_SIZE = 5;

    /**
     * Constructs a initialized world.
     */
    World(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.tiles = new TETile[width][height];
        this.rooms = new HashSet<>();
        this.seed = seed;
        this.random = new Random(seed);
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
        if (!room.isOutOfBoundary(this) && !room.isOverlapped(tiles)) {
            room.draw(tiles);
            rooms.add(room);
        }
    }

    /**
     * Adds rooms with random positions and dimensions
     * until numRooms rooms exists.
     */
    public void addRandomRooms(int numRooms) {
        while (rooms.size() < numRooms) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Position pos = new Position(x, y);
            int w = random.nextInt(5) + MIN_ROOM_SIZE;
            int h = random.nextInt(5) + MIN_ROOM_SIZE;

            addRoom(pos, w, h);
        }
    }

    /**
     * Adds a hallway to the world.
     */
    public void addHallway(int length, Position pos, String orientation) {
        Hallway hall = new Hallway(length, pos, orientation);
        hall.draw(tiles);
    }

    /**
     * Connects two rooms by building a hallway
     * between the rooms.
     */
    private void connectRooms(Room roomA, Room roomB) {

    }


    /**
     * Get methods for instance variables.
     */
    public TETile[][] getTiles() {
        return this.tiles;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getNumRooms() {
        return this.rooms.size();
    }
}
