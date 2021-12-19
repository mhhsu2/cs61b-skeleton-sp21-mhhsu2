package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.Tileset;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestWorld {
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    @Test
    public void testAddRoom() {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        World w = new World(WIDTH, HEIGHT);

        Position pos = new Position(10, 10);
        w.addRoom(pos, 10, 15);

//        assertEquals(tiles[55][55], Tileset.FLOOR);

        ter.renderFrame(w.getTiles());
    }

    public static void main(String[] args) {
        TestWorld tester = new TestWorld();
        tester.testAddRoom();
    }

}
