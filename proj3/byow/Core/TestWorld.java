package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.Tileset;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestWorld {
    private TERenderer ter;
    private World w;

    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final long SEED = 1256;

    TestWorld() {
        ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        w = new World(WIDTH, HEIGHT, SEED);
    }

    @Test
    public void testAddRoom() {
        Position p1 = new Position(10, 10);
        w.addRoom(p1, 10, 15);

        Position p2 = new Position(25, 25);
        w.addRoom(p2, 5, 5);

//        assertEquals(tiles[55][55], Tileset.FLOOR);
        ter.renderFrame(w.getTiles());
    }

    @Test
    public void testAddRoomOverlapped() {
        Position p3 = new Position(12, 8);
        w.addRoom(p3, 5, 5);

        assertEquals(2, w.getNumRooms());

        ter.renderFrame(w.getTiles());
    }

    @Test
    public void testAddRoomOutOfBoundary() {
        Position p4 = new Position(75, 8);
        w.addRoom(p4, 10, 10);
        assertEquals(2, w.getNumRooms());

        Position p5 = new Position(-2, 10);
        w.addRoom(p5, 10, 10);
        assertEquals(2, w.getNumRooms());

        Position p6 = new Position(10, 26);
        w.addRoom(p6, 10, 5);
        assertEquals(2, w.getNumRooms());

        Position p7 = new Position(10, -2);
        w.addRoom(p7, 10, 10);
        assertEquals(2, w.getNumRooms());

        ter.renderFrame(w.getTiles());
    }

    @Test
    public void testAddRandomRooms() {
        w.addRandomRooms(12);

        ter.renderFrame(w.getTiles());
    }

    @Test
    public void testAddHallway() {
        Position p1 = new Position(25, 8);
        w.addHallway(15, p1, "North");

        Position p2 = new Position(20, 15);
        w.addHallway(15, p2, "South");

        Position p3 = new Position(25, 8);
        w.addHallway(15, p3, "East");

        Position p4 = new Position(60, 12);
        w.addHallway(15, p4, "West");

        ter.renderFrame(w.getTiles());
    }

    public static void main(String[] args) {
        TestWorld tester = new TestWorld();
//        tester.testAddRoom();
//        tester.testAddRoomOverlapped();
//        tester.testAddRoomOutOfBoundary();
//        tester.testAddRandomRooms();
        tester.testAddHallway();
    }

}
