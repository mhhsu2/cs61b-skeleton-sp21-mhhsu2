package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private int width;
    private int height;
    private TETile[][] world;
    private TERenderer ter;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    HexWorld(int width, int height) {
        this.width = width;
        this.height = height;

        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        this.ter = new TERenderer();
        ter.initialize(width, height);

        // initialize tiles
        this.world = new TETile[width][height];
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    /** Picks a RANDOM tile with a 33% change of being
     *  a wall, 33% chance of being a flower, and 33%
     *  chance of being empty space.
     */
    private TETile randomTile() {
        int tileNum = RANDOM.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.SAND;
            case 3: return Tileset.GRASS;
            case 4: return Tileset.MOUNTAIN;

            default: return Tileset.NOTHING;
        }
    }

    public void addHexagon(int x, int y, int size) {
        TETile tile = randomTile();

        for (int l = 0; l < size; l += 1) {
            int lenLayer = size + 2 * l;
            for (int c = 0; c < lenLayer; c += 1) {
                int tx = x - lenLayer/2 + c;
                int tyu = y + size - 1 - l;
                int tyb = y - size + l;

                if (tx >= 0 && tx < width && tyu >= 0 && tyu < height) {
                    world[tx][tyu] = tile;
                }
                if (tx >= 0 && tx < width && tyb >= 0 && tyb < height) {
                    world[tx][tyb] = tile;
                }
            }
        }
    }

    public void tessWorld(int tSize, int hexSize) {
        int cx = width/2;
        int cy = height/2;

        for (int l = 0; l < tSize; l += 1) {
            int lenLayer = tSize + l;
            for (int r = 0; r < lenLayer; r += 1) {
                int hxl = cx - (2 * hexSize - 1) * (tSize - l - 1);
                int hxr = cx + (2 * hexSize - 1) * (tSize - l - 1);
                int hy = cy - 2 * hexSize * (lenLayer / 2 - r) + ((l + tSize + 1) % 2) * hexSize;
                addHexagon(hxl, hy, hexSize);
                addHexagon(hxr, hy, hexSize);
            }
        }
    }

    public void renderWorld() {
        ter.renderFrame(world);
    }

    public static void main(String[] args) {
        HexWorld w = new HexWorld(50, 50);
        w.tessWorld(3, 2);


        w.renderWorld();
    }

}
