package minesweeper;

import student.TestCase;

/**
 * Tests for Board.
 *
 * @author Group 94
 * @version 2026.09.24
 */
public class BoardTest extends TestCase {

    /**
     * Tests construction and direct tile access.
     */
    public void testConstructorAndGetTile() {
        Board board = new Board(3, 4, 2);
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 4; col++) {
                Tile tile = board.getTile(row, col);
                assertNotNull(tile);
                assertSame(board, tile.getBoard());
                assertEquals(row, tile.getTileRow());
                assertEquals(col, tile.getTileCol());
            }
        }
        assertFalse(board.getGameOver());
    }

    /**
     * Tests every way a coordinate can be inside or outside the board.
     */
    public void testInBounds() {
        Board board = new Board(3, 4, 0);
        assertTrue(board.inBounds(0, 0));
        assertTrue(board.inBounds(2, 3));
        assertFalse(board.inBounds(-1, 0));
        assertFalse(board.inBounds(3, 0));
        assertFalse(board.inBounds(0, -1));
        assertFalse(board.inBounds(0, 4));
    }

    /**
     * Tests covered, flagged, mine, and numbered output symbols.
     */
    public void testBoardToString() {
        Board board = new Board(2, 2, 0);
        board.getTile(0, 0).setMine(true);
        board.finalizeCustomGame();
        board.Flag(0, 1);
        board.getTile(1, 0).setCovered(false);
        board.getTile(0, 0).setCovered(false);

        assertEquals("* F \n1 # \n", board.boardToString());
    }

    /**
     * Tests that out-of-bounds reveal requests do nothing.
     */
    public void testRevealTileOutOfBounds() {
        Board board = new Board(2, 2, 0);
        board.revealTile(-1, 0);
        board.revealTile(2, 0);
        assertTrue(board.getTile(0, 0).getCovered());
        assertFalse(board.getGameOver());
    }

    /**
     * Tests first-move setup and BFS on a board with no mines.
     */
    public void testRevealTileFirstMoveAndWin() {
        Board board = new Board(2, 2, 0);
        board.revealTile(0, 0);

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                assertFalse(board.getTile(row, col).getCovered());
                assertEquals(0, board.getTile(row, col).getCounter());
            }
        }
        assertTrue(board.checkWin());
    }

    /**
     * Tests a normal numbered reveal that does not start BFS.
     */
    public void testRevealTileNumber() {
        Board board = customBoard();
        board.revealTile(1, 1);
        assertFalse(board.getTile(1, 1).getCovered());
        assertFalse(board.getGameOver());
        assertFalse(board.checkWin());
    }

    /**
     * Tests that flagged and already uncovered tiles are ignored.
     */
    public void testRevealTileIgnoredCases() {
        Board board = customBoard();
        board.Flag(1, 1);
        board.revealTile(1, 1);
        assertTrue(board.getTile(1, 1).getCovered());

        board.Flag(1, 1);
        board.revealTile(1, 1);
        assertFalse(board.getTile(1, 1).getCovered());
        board.revealTile(1, 1);
        assertFalse(board.getTile(1, 1).getCovered());
    }

    /**
     * Tests loss state after revealing a mine.
     */
    public void testRevealMineAndGameOver() {
        Board board = new Board(2, 2, 0);
        board.getTile(0, 0).setMine(true);
        board.getTile(1, 1).setMine(true);
        board.finalizeCustomGame();

        board.revealTile(0, 0);
        assertTrue(board.getGameOver());
        assertFalse(board.getTile(0, 0).getCovered());
        assertFalse(board.getTile(1, 1).getCovered());
        assertTrue(board.getTile(0, 1).getCovered());
        assertFalse(board.checkWin());
    }


    /**
     * Tests both parts of the checkWin condition when no safe tiles remain.
     */
    public void testCheckWinGameOverCondition() {
        Board board = new Board(1, 1, 1);
        assertTrue(board.checkWin());
        board.gameOver();
        assertFalse(board.checkWin());
    }

    /**
     * Tests flagging, unflagging, out-of-bounds input, and attempts to flag an
     * uncovered tile.
     */
    public void testFlag() {
        Board board = customBoard();

        board.Flag(-1, 0);
        board.Flag(3, 0);
        assertFalse(board.getTile(0, 0).getFlag());

        board.Flag(1, 1);
        assertTrue(board.getTile(1, 1).getFlag());
        board.Flag(1, 1);
        assertFalse(board.getTile(1, 1).getFlag());

        board.getTile(1, 1).setCovered(false);
        board.Flag(1, 1);
        assertFalse(board.getTile(1, 1).getFlag());
    }

    /**
     * Tests requested mine count and first-click protection.
     */
    public void testGenerateMines() {
        Board board = new Board(5, 5, 6);
        board.generateMines(2, 2);

        int mines = 0;
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (board.getTile(row, col).getMine()) {
                    mines++;
                }
            }
        }

        assertEquals(6, mines);
        assertFalse(board.getTile(2, 2).getMine());
    }

    /**
     * Exercises retry paths in random mine generation repeatedly so both the
     * protected-square and duplicate-mine checks are executed during coverage
     * runs.
     */
    public void testGenerateMinesRetryPaths() {
        for (int run = 0; run < 40; run++) {
            Board board = new Board(2, 2, 3);
            board.generateMines(0, 0);
            assertFalse(board.getTile(0, 0).getMine());

            int mines = 0;
            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 2; col++) {
                    if (board.getTile(row, col).getMine()) {
                        mines++;
                    }
                }
            }
            assertEquals(3, mines);
        }
    }

    /**
     * Tests clearing mines, counters, loss state, and first-move state.
     */
    public void testClearMines() {
        Board board = customBoard();
        board.revealTile(0, 0);
        assertTrue(board.getGameOver());

        board.clearMines();
        assertFalse(board.getGameOver());
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                assertFalse(board.getTile(row, col).getMine());
                assertEquals(0, board.getTile(row, col).getCounter());
            }
        }

        board.revealTile(0, 0);
        assertFalse(board.getTile(0, 0).getCovered());
        assertFalse(board.getGameOver());
    }

    /**
     * Tests counter calculations around known mines.
     */
    public void testCalculateCounters() {
        Board board = new Board(3, 3, 0);
        board.getTile(0, 0).setMine(true);
        board.getTile(2, 2).setMine(true);
        board.calculateCounters();

        assertEquals(2, board.getTile(1, 1).getCounter());
        assertEquals(1, board.getTile(0, 1).getCounter());
        assertEquals(0, board.getTile(0, 0).getCounter());
    }

    /**
     * Tests BFS expansion with a flagged tile and an adjacent mine guard.
     */
    public void testRevealEmptyAreaFlagAndMineGuard() {
        Board board = new Board(3, 3, 0);
        board.getTile(0, 0).setMine(true);
        board.Flag(2, 2);
        board.getTile(1, 2).setCovered(false);

        board.getTile(1, 1).setCovered(false);
        board.revealEmptyArea(1, 1);

        assertTrue(board.getTile(0, 0).getCovered());
        assertTrue(board.getTile(2, 2).getCovered());
        assertTrue(board.getTile(2, 2).getFlag());

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                assertEquals(-1, board.getTile(row, col).getSearchLevel());
            }
        }
    }

    /**
     * Tests BFS on a normal calculated board where numbered border tiles stop
     * expansion.
     */
    public void testRevealEmptyAreaStopsAtNumbers() {
        Board board = new Board(4, 4, 0);
        board.getTile(0, 0).setMine(true);
        board.finalizeCustomGame();
        board.revealTile(3, 3);

        assertFalse(board.getTile(1, 1).getCovered());
        assertEquals(1, board.getTile(1, 1).getCounter());
        assertTrue(board.getTile(0, 0).getCovered());
    }

    /**
     * Tests direct search-level resetting.
     */
    public void testResetSearchLevels() {
        Board board = new Board(2, 2, 0);
        board.getTile(0, 0).setSearchLevel(0);
        board.getTile(1, 1).setSearchLevel(2);
        board.resetSearchLevels();

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                assertEquals(-1, board.getTile(row, col).getSearchLevel());
            }
        }
    }

    /**
     * Tests custom-game finalization with mines.
     */
    public void testFinalizeCustomGameWithMines() {
        Board board = new Board(3, 3, 0);
        board.getTile(0, 0).setMine(true);
        board.getTile(2, 2).setMine(true);
        board.finalizeCustomGame();

        assertEquals(2, board.getTile(1, 1).getCounter());
        board.revealTile(1, 1);
        assertFalse(board.getTile(1, 1).getCovered());
    }

    /**
     * Tests custom-game finalization with no mines.
     */
    public void testFinalizeCustomGameNoMines() {
        Board board = new Board(2, 2, 0);
        board.finalizeCustomGame();
        assertFalse(board.checkWin());
        board.revealTile(0, 0);
        assertTrue(board.checkWin());
    }

    /**
     * Creates a 3x3 custom board with a mine at (0,0).
     *
     * @return prepared custom board
     */
    private Board customBoard() {
        Board board = new Board(3, 3, 0);
        board.getTile(0, 0).setMine(true);
        board.finalizeCustomGame();
        return board;
    }
}
