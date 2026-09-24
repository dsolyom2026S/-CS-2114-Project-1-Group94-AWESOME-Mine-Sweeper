package minesweeper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import student.TestCase;

/**
 * Tests for Main.
 *
 * @author Group 94
 * @version 2026.09.24
 */
public class MainTest extends TestCase {

    private Main game;

    /**
     * Creates a fresh Main object before each test.
     */
    public void setUp() {
        game = new Main();
    }

    /**
     * Tests each valid difficulty.
     */
    public void testNewGameDifficulties() {
        Board easy = game.newGame(in("easy"));
        assertTrue(easy.inBounds(8, 8));
        assertFalse(easy.inBounds(9, 9));

        Board medium = game.newGame(in("medium"));
        assertTrue(medium.inBounds(15, 15));
        assertFalse(medium.inBounds(16, 16));

        Board hard = game.newGame(in("hard"));
        assertTrue(hard.inBounds(29, 15));
        assertFalse(hard.inBounds(30, 16));
    }

    /**
     * Tests invalid difficulty handling.
     */
    public void testNewGameInvalid() {
        try {
            game.newGame(in("impossible"));
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests valid custom mine placement and counter calculation.
     */
    public void testCustomGamePlacesMines() {
        Board board = new Board(3, 3, 0);
        game.customGame(in("0 0\n2 2\ndone\n"), board);

        assertTrue(board.getTile(0, 0).getMine());
        assertTrue(board.getTile(2, 2).getMine());
        assertFalse(board.getTile(1, 1).getMine());
        assertEquals(2, board.getTile(1, 1).getCounter());
        assertSame(board, game.getBoard());
    }

    /**
     * Tests both invalid-input paths and out-of-bounds custom coordinates.
     */
    public void testCustomGameBadInputAndOutOfBounds() {
        Board board = new Board(3, 3, 0);
        game.customGame(in("abc def\n9 9\n1 1\ndone\n"), board);

        assertTrue(board.getTile(1, 1).getMine());
        assertEquals(1, board.getTile(0, 0).getCounter());
    }


    /**
     * Tests custom-game finalization when input is already exhausted.
     */
    public void testCustomGameEmptyInput() {
        Board board = new Board(1, 1, 0);
        game.customGame(in(""), board);
        board.revealTile(0, 0);
        assertTrue(board.checkWin());
    }

    /**
     * Tests a custom game with no mines.
     */
    public void testCustomGameNoMines() {
        Board board = new Board(2, 2, 0);
        game.customGame(in("done"), board);
        board.revealTile(0, 0);
        assertTrue(board.checkWin());
    }

    /**
     * Tests a normal reveal command.
     */
    public void testGetUserInputReveal() {
        game.setBoard(customBoard());
        assertEquals("Tile revealed.", game.getUserInput(in("r 2 2")));
    }

    /**
     * Tests revealing a mine.
     */
    public void testGetUserInputRevealMine() {
        game.setBoard(customBoard());
        assertEquals("Tile revealed.", game.getUserInput(in("r 0 0")));
        assertTrue(game.getBoard().getGameOver());
    }

    /**
     * Tests flag and unflag commands.
     */
    public void testGetUserInputFlagAndUnflag() {
        game.setBoard(customBoard());
        assertEquals("Tile flagged.", game.getUserInput(in("f 1 1")));
        assertTrue(game.getBoard().getTile(1, 1).getFlag());
        assertEquals("Flag removed.", game.getUserInput(in("f 1 1")));
        assertFalse(game.getBoard().getTile(1, 1).getFlag());
    }

    /**
     * Tests rejecting reveal of a flagged tile and an already revealed tile.
     */
    public void testGetUserInputCannotReveal() {
        game.setBoard(customBoard());
        game.getUserInput(in("f 1 1"));
        assertEquals("Cannot reveal that tile.",
            game.getUserInput(in("r 1 1")));

        game.getUserInput(in("f 1 1"));
        game.getUserInput(in("r 1 1"));
        assertEquals("Cannot reveal that tile.",
            game.getUserInput(in("r 1 1")));
    }

    /**
     * Tests rejecting a flag on a revealed tile.
     */
    public void testGetUserInputCannotFlagRevealed() {
        game.setBoard(customBoard());
        game.getUserInput(in("r 1 1"));
        assertEquals("Cannot flag a revealed tile.",
            game.getUserInput(in("f 1 1")));
    }

    /**
     * Tests invalid commands with and without a remaining line.
     */
    public void testGetUserInputInvalidCommand() {
        game.setBoard(customBoard());
        assertEquals("Invalid command.", game.getUserInput(in("x 1 1")));
        assertEquals("Invalid command.", game.getUserInput(in("x")));
    }

    /**
     * Tests bad and missing coordinate input.
     */
    public void testGetUserInputInvalidCoordinates() {
        game.setBoard(customBoard());
        assertEquals("Invalid coordinates.", game.getUserInput(in("r a b")));
        assertEquals("Invalid coordinates.", game.getUserInput(in("r 1")));
    }

    /**
     * Tests coordinates outside the current board.
     */
    public void testGetUserInputOutOfBounds() {
        game.setBoard(customBoard());
        assertEquals("Coordinates out of bounds.",
            game.getUserInput(in("r 5 5 trailing")));
    }

    /**
     * Tests win responses and compatibility wrapper.
     */
    public void testUserWin() {
        assertTrue(game.userWin(in("yes")));
        assertFalse(game.userWin(in("no")));
        assertTrue(game.scannerWin(in("yes")));

        try {
            game.scannerWin(in("maybe"));
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests loss responses and compatibility wrapper.
     */
    public void testUserLose() {
        assertTrue(game.userLose(in("yes")));
        assertFalse(game.userLose(in("no")));
        assertFalse(game.scannerLose(in("no")));

        try {
            game.scannerLose(in("maybe"));
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests readPositiveInt retry behavior through text, zero, and negatives.
     */
    public void testReadPositiveInt() {
        assertEquals(4, game.readPositiveInt(in("abc\n0\n-3\n4\n"), "n: "));
    }

    /**
     * Tests each short-circuit path in isError.
     */
    public void testIsError() {
        assertTrue(Main.isError("Invalid command."));
        assertTrue(Main.isError("Coordinates out of bounds."));
        assertTrue(Main.isError("Cannot reveal that tile."));
        assertFalse(Main.isError("Tile revealed."));
        assertFalse(Main.isError("Tile flagged."));
    }

    /**
     * Tests board getter and setter.
     */
    public void testGetSetBoard() {
        Board board = customBoard();
        game.setBoard(board);
        assertSame(board, game.getBoard());
    }

    /**
     * Runs main in custom mode, covers an invalid command, a win, an invalid
     * replay answer, and a second game through the play-again path.
     */
    public void testMainCustomWinAndPlayAgain() {
        String input =
            "1\n1\ndone\n"
                + "x 0 0\n"
                + "r 0 0\n"
                + "maybe\n"
                + "yes\n"
                + "1\n1\ndone\n"
                + "r 0 0\n"
                + "no\n";

        runMain(new String[] { "custom" }, input);
    }

    /**
     * Runs main in custom mode through the loss path.
     */
    public void testMainCustomLoss() {
        String input =
            "1\n1\n"
                + "0 0\n"
                + "done\n"
                + "r 0 0\n"
                + "no\n";

        runMain(new String[] { "custom" }, input);
    }


    /**
     * Runs main with a non-custom argument so both sides of the argument
     * check are exercised while still entering regular-game mode.
     */
    public void testMainNonCustomArgument() {
        StringBuilder input = new StringBuilder();
        input.append("easy\n");

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                input.append("r ").append(row).append(" ").append(col)
                    .append("\n");
            }
        }
        input.append("no\n");

        runMain(new String[] { "normal" }, input.toString());
    }

    /**
     * Runs main in regular mode. An invalid difficulty is retried, then every
     * coordinate is offered until the randomly generated easy game must end
     * in either a win or a loss.
     */
    public void testMainRegularInvalidDifficulty() {
        StringBuilder input = new StringBuilder();
        input.append("wrong\n");
        input.append("easy\n");

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                input.append("r ").append(row).append(" ").append(col)
                    .append("\n");
            }
        }
        input.append("no\n");

        runMain(new String[0], input.toString());
    }

    /**
     * Creates a Scanner reading from fixed text.
     *
     * @param text
     *            simulated input
     * @return scanner over that input
     */
    private Scanner in(String text) {
        return new Scanner(text);
    }

    /**
     * Creates a 3x3 custom board with one mine at (0,0).
     *
     * @return prepared board
     */
    private Board customBoard() {
        Board board = new Board(3, 3, 0);
        board.getTile(0, 0).setMine(true);
        board.finalizeCustomGame();
        return board;
    }

    /**
     * Runs Main.main with simulated standard input and hidden console output.
     *
     * @param args
     *            command-line arguments
     * @param input
     *            simulated keyboard input
     */
    private void runMain(String[] args, String input) {
        InputStream oldIn = System.in;
        PrintStream oldOut = System.out;

        ByteArrayInputStream fakeIn = new ByteArrayInputStream(
            input.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setIn(fakeIn);
            System.setOut(new PrintStream(output));
            Main.main(args);
        }
        finally {
            System.setIn(oldIn);
            System.setOut(oldOut);
        }

        assertTrue(output.size() > 0);
    }
}
