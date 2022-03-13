package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import jh61b.junit.In;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static Long SEED;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // Runs the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //

        InputSource is = new StringInputDevice(input);
        return playGame(is);
    }

    private TETile[][] playGame(InputSource is) {
        World world = null;
        char mode = Character.toUpperCase(is.getNextKey());
//        TERenderer ter = new TERenderer();
//        ter.initialize(WIDTH, HEIGHT);

        switch (mode) {
            case 'N' -> {
                SEED = getSeedFromInputString(is);
                world = new World(WIDTH, HEIGHT, SEED);
                world.generateWorld();
            }
//                ter.renderFrame(world.getTiles());
        }

        if (world != null) {
            return world.getTiles();
        }
        return null;
    }

    // Helper Methods
    private Long getSeedFromInputString(InputSource is) {
        StringBuilder sb = new StringBuilder();
        while(is.possibleNextInput()) {
            char c = is.getNextKey();

            if (Character.toUpperCase(c) == 'S') {
                break;
            } else {
                sb.append(c);
            }
        }

        return Long.parseLong(sb.toString());
    }
}
