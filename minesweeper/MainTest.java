package minesweeper;

import static org.junit.Assert.*;
import java.util.Scanner;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the Main class. Covers game creation (newGame), custom game
 * setup (customGame), player commands (getUserInput), the win and lose
 * prompts (scannerWin and scannerLose), and the board getter and setter.
 * Input is simulated by feeding a Scanner a fixed string.
 *
 * @author Group 94
 * @version 2026.09.24
 */
public class MainTest {

    private Main game;

    /**
     * Creates a fresh Main object before each test.
     */
    @Before
    public void setUp() {
        game = new Main();
    }

    /**
     * Helper that builds a 3x3 board with a single mine at (0,0), ready to
     * play.
     *
     * @return the prepared custom board
     */
    private Board customBoard() {
        Board b = new Board(3, 3, 0);
        b.getTile(0, 0).setMine(true);
        b.finalizeCustomGame();
        return b;
    }

    /**
     * Helper that creates a Scanner reading from the given text.
     *
     * @param text
     *            the simulated user input
     * @return a Scanner over the text
     */
    private Scanner in(String text) {
        return new Scanner(text);
    }

    // ---- newGame ----

    /**
     * Tests that "easy" creates a 9x9 board.
     */
    @Test
    public void testNewGameEasy() {
        Board b = game.newGame(in("easy"));
        assertNotNull(b);
        assertNotNull(b.getTile(8, 8));
        assertFalse(b.inBounds(9, 9));
    }

    /**
     * Tests that "medium" creates a 16x16 board.
     */
    @Test
    public void testNewGameMedium() {
        Board b = game.newGame(in("medium"));
        assertTrue(b.inBounds(15, 15));
        assertFalse(b.inBounds(16, 16));
    }

    /**
     * Tests that "hard" creates a board with 30 rows and 16 columns.
     */
    @Test
    public void testNewGameHard() {
        Board b = game.newGame(in("hard"));
        assertTrue(b.inBounds(29, 15));
        assertFalse(b.inBounds(30, 16));
    }

    /**
     * Tests that an unrecognized difficulty throws an
     * IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNewGameInvalid() {
        game.newGame(in("impossible"));
    }

    // ---- customGame ----

    /**
     * Tests that customGame places mines at the given coordinates and
     * calculates the surrounding counters.
     */
    @Test
    public void testCustomGamePlacesMines() {
        Board b = new Board(3, 3, 0);
        game.customGame(in("0 0\n2 2\ndone\n"), b);
        assertTrue(b.getTile(0, 0).getMine());
        assertTrue(b.getTile(2, 2).getMine());
        assertFalse(b.getTile(1, 1).getMine());
        assertEquals(2, b.getTile(1, 1).getCounter());
    }

    /**
     * Tests that customGame skips out-of-bounds coordinates and
     * non-numeric input while still placing valid mines.
     */
    @Test
    public void testCustomGameIgnoresOutOfBoundsAndBadInput() {
        Board b = new Board(3, 3, 0);
        game.customGame(in("9 9\nabc\n1 1\ndone\n"), b);
        assertTrue(b.getTile(1, 1).getMine());
        assertEquals(1, b.getTile(0, 0).getCounter());
    }

    /**
     * Tests that a custom game with no mines is won by revealing a tile.
     */
    @Test
    public void testCustomGameNoMines() {
        Board b = new Board(2, 2, 0);
        game.customGame(in("done"), b);
        b.revealTile(0, 0);
        assertTrue(b.checkWin());
    }

    // ---- getUserInput ----

    /**
     * Tests that a reveal command reveals the tile, and that revealing an
     * empty area can win the game.
     */
    @Test
    public void testGetUserInputReveal() {
        game.setBoard(customBoard());
        assertEquals("Tile revealed.", game.getUserInput(in("r 2 2")));
        assertTrue(game.getBoard().checkWin());
    }

    /**
     * Tests that revealing a mine ends the game.
     */
    @Test
    public void testGetUserInputRevealMine() {
        game.setBoard(customBoard());
        game.getUserInput(in("r 0 0"));
        assertTrue(game.getBoard().getGameOver());
    }

    /**
     * Tests that the flag command toggles a flag on and off.
     */
    @Test
    public void testGetUserInputFlagAndUnflag() {
        game.setBoard(customBoard());
        assertEquals("Tile flagged.", game.getUserInput(in("f 1 1")));
        assertTrue(game.getBoard().getTile(1, 1).getFlag());
        assertEquals("Flag removed.", game.getUserInput(in("f 1 1")));
        assertFalse(game.getBoard().getTile(1, 1).getFlag());
    }

    /**
     * Tests that a flagged tile cannot be revealed.
     */
    @Test
    public void testGetUserInputRevealFlaggedTile() {
        game.setBoard(customBoard());
        game.getUserInput(in("f 1 1"));
        assertEquals("Cannot reveal that tile.",
            game.getUserInput(in("r 1 1")));
    }

    /**
     * Tests that an already revealed tile cannot be flagged.
     */
    @Test
    public void testGetUserInputFlagRevealedTile() {
        game.setBoard(customBoard());
        game.getUserInput(in("r 1 1"));
        assertEquals("Cannot flag a revealed tile.",
            game.getUserInput(in("f 1 1")));
    }

    /**
     * Tests that an unknown command letter is rejected.
     */
    @Test
    public void testGetUserInputInvalidCommand() {
        game.setBoard(customBoard());
        assertEquals("Invalid command.", game.getUserInput(in("x 1 1")));
    }

    /**
     * Tests that non-numeric or missing coordinates are rejected.
     */
    @Test
    public void testGetUserInputInvalidCoordinates() {
        game.setBoard(customBoard());
        assertEquals("Invalid coordinates.", game.getUserInput(in("r a b")));
        assertEquals("Invalid coordinates.", game.getUserInput(in("r 1")));
    }

    /**
     * Tests that coordinates outside the board are rejected.
     */
    @Test
    public void testGetUserInputOutOfBounds() {
        game.setBoard(customBoard());
        assertEquals("Coordinates out of bounds.",
            game.getUserInput(in("r 5 5")));
    }

    // ---- scannerWin / scannerLose ----

    /**
     * Tests that answering "yes" after a win returns true.
     */
    @Test
    public void testScannerWinYes() {
        assertTrue(game.scannerWin(in("yes")));
    }

    /**
     * Tests that answering "no" after a win returns false.
     */
    @Test
    public void testScannerWinNo() {
        assertFalse(game.scannerWin(in("no")));
    }

    /**
     * Tests that an invalid answer after a win throws an
     * IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testScannerWinInvalid() {
        game.scannerWin(in("maybe"));
    }

    /**
     * Tests that answering "yes" after a loss returns true.
     */
    @Test
    public void testScannerLoseYes() {
        assertTrue(game.scannerLose(in("yes")));
    }

    /**
     * Tests that answering "no" after a loss returns false.
     */
    @Test
    public void testScannerLoseNo() {
        assertFalse(game.scannerLose(in("no")));
    }

    /**
     * Tests that an invalid answer after a loss throws an
     * IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testScannerLoseInvalid() {
        game.scannerLose(in("maybe"));
    }

    // ---- readPositiveInt / isError ----

    /**
     * Tests that readPositiveInt skips invalid entries (text, zero,
     * negative) and returns the first positive whole number.
     */
    @Test
    public void testReadPositiveIntRetries() {
        assertEquals(4, game.readPositiveInt(in("abc\n0\n-3\n4\n"), "n: "));
    }

    /**
     * Tests that isError flags failed commands and not successful ones.
     */
    @Test
    public void testIsError() {
        assertTrue(Main.isError("Invalid command."));
        assertTrue(Main.isError("Invalid coordinates."));
        assertTrue(Main.isError("Coordinates out of bounds."));
        assertTrue(Main.isError("Cannot reveal that tile."));
        assertTrue(Main.isError("Cannot flag a revealed tile."));
        assertFalse(Main.isError("Tile revealed."));
        assertFalse(Main.isError("Tile flagged."));
        assertFalse(Main.isError("Flag removed."));
    }

    // ---- getBoard / setBoard ----

    /**
     * Tests that setBoard stores the board and getBoard returns the same
     * object.
     */
    @Test
    public void testGetSetBoard() {
        Board b = customBoard();
        game.setBoard(b);
        assertSame(b, game.getBoard());
    }
}