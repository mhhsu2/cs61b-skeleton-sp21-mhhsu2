package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

/**
 * This class represents a world in BYOW.
 * This class manges objects such as rooms and hallways in a world.
 */
public class World {

    private int width;
    private int height;
    private TETile[][] tiles;
    private List<Room> rooms;
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
        this.rooms = new ArrayList<>();
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
     * Generates a random world.
     */
    public void generateWorld() {
        int numRooms = random.nextInt(15) + 10;
        this.addRandomRooms(numRooms);
        this.connectRooms();
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
    public Hallway addHallway(int length, Position pos, String orientation) {
        Hallway hall = new Hallway(length, pos, orientation);
        hall.draw(tiles);
        return hall;
    }

    /**
     * Connects two rooms by building a hallway
     * between the rooms.
     */
    public void connectTwoRoom(Room roomA, Room roomB) {
        Position connPosA = roomA.randConnPos(random);
        Position connPosB = roomB.randConnPos(random);

        int displacementX = connPosB.getX() - connPosA.getX();
        int displacementY = connPosB.getY() - connPosA.getY();

        // Builds horizontal hallway starting from A.
        Hallway honHall;
        if (displacementX >= 0) {
            honHall = addHallway(displacementX, connPosA, "East");
        } else {
            honHall = addHallway(-displacementX, connPosA, "West");
        }

        // Builds vertical hallway starting from the end of honHall.
        Hallway verHall;
        if (displacementY >= 0) {
            verHall = addHallway(displacementY, honHall.getEndPos(), "North");
        } else {
            verHall = addHallway(-displacementY, honHall.getEndPos(), "South");
        }

        // Pads the corner at the intersection between honHall and verHall
        padCorner(honHall, verHall);
    }

    /**
     * Connects all existing rooms in the world.
     */
    public void connectRooms() {
        for (int i = 0; i < rooms.size() - 1; i++) {
            connectTwoRoom(rooms.get(i), rooms.get(i+1));
        }
    }

    /**
     * A helper method pads the corner at the intersection between honHall and verHall.
     */
    private void padCorner(Hallway honHall, Hallway verHall) {
        String honOrient = honHall.getOrientation();
        String verOrient = verHall.getOrientation();

        Position cornerPos = honHall.getEndPos();
        int padX;
        int padY;

        if (honOrient.equals("East") && verOrient.equals("North")) {
            padX = cornerPos.getX() + 1;
            padY = cornerPos.getY() - 1;
        } else if (honOrient.equals("East") && verOrient.equals("South")) {
            padX = cornerPos.getX() + 1;
            padY = cornerPos.getY() + 1;
        } else if (honOrient.equals("West") && verOrient.equals("North")) {
            padX = cornerPos.getX() - 1;
            padY = cornerPos.getY() - 1;
        } else {
            padX = cornerPos.getX() - 1;
            padY = cornerPos.getY() + 1;
        }
        if (tiles[padX][padY] == Tileset.NOTHING) {
            tiles[padX][padY] = Tileset.WALL;
        }
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

    public List<Room> getRooms() {return this.rooms;}
}
