package byow.lab13;

import byow.Core.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    /** The width of the window of this game. */
    private int width;
    /** The height of the window of this game. */
    private int height;
    /** The current round the user is on. */
    private int round;
    /** The Random object used to randomly generate Strings. */
    private Random rand;
    /** Whether or not the game is over. */
    private boolean gameOver;
    /** Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'. */
    private boolean playerTurn;
    /** The characters we generate random Strings from. */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** Encouraging phrases. Used in the last section of the spec, 'Helpful UI'. */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        // Initialize random number generator
        this.rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        // Generate random string of letters of length n
        StringBuilder sb = new StringBuilder();
        int count = 0;
        while (count < n) {
            char c = CHARACTERS[rand.nextInt(26)];
            sb.append(c);
            count++;
        }

        return sb.toString();
    }

    public void drawFrame(String s) {
        // Take the string and display it in the center of the screen
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text(width/2, height/2, s);
        StdDraw.setPenColor(Color.WHITE);

        // If game is not over, display relevant game information at the top of the screen
        if (!gameOver) {
            Font fontHeader = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(fontHeader);
            StdDraw.textLeft(1, height - 1, "Round: " + round);

            String state = playerTurn ? "Type!" : "Watch!";
            StdDraw.text(width/2, height - 1, state);

            if (playerTurn) {
                StdDraw.textRight(width - 1, height - 1, ENCOURAGEMENT[rand.nextInt(ENCOURAGEMENT.length)]);
            }

            StdDraw.line(0, height - 2, width, height - 2);
        }
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        // Display each character in letters, making sure to blank the screen between letters
        for (char c : letters.toCharArray()) {
            drawFrame(Character.toString(c));
            StdDraw.pause(1000);
            drawFrame("");
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
        // Read n letters of player input
        StringBuilder sb = new StringBuilder();
        int count = 0;
        while (count < n) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                sb.append(c);
                drawFrame(sb.toString());
                count++;
            }
        }
        StdDraw.pause(1000);
        return sb.toString();
    }

    public void startGame() {
        // Set any relevant variables before the game starts
        gameOver = false;
        round = 1;
        playerTurn = false;

        // Establish Engine loop
        while (!gameOver) {
            playerTurn = false;
            String randStr = generateRandomString(round);
            drawFrame("Round: " + round);
            StdDraw.pause(2000);
            flashSequence(randStr);
            playerTurn = true;
            String userStr = solicitNCharsInput(round);

            if (!userStr.equals(randStr)) {
                gameOver = true;
                drawFrame("Game Over! You made it to round: " + round);
            }
            round++;
        }

    }

}
