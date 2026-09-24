package minesweeper;

import student.TestCase;

/**
 * Tests for Tile.
 *
 * @author Group 94
 * @version 2026.09.24
 */
public class TileTest extends TestCase {

    private Board board;
    private Tile center;

    /**
     * Creates a 3x3 board before each test.
     */
    public void setUp() {
        board = new Board(3, 3, 0);
        center = board.getTile(1, 1);
    }

    /**
     * Tests constructor defaults and stored Board/coordinates.
     */
    public void testConstructor() {
        assertSame(board, center.getBoard());
        assertEquals(1, center.getTileRow());
        assertEquals(1, center.getTileCol());
        assertEquals(0, center.getCounter());
        assertFalse(center.getMine());
        assertFalse(center.getFlagged());
        assertTrue(center.getCovered());
        assertEquals(-1, center.getSearchLevel());
    }

    /**
     * Tests neighbor counts for center, edge, corner, and one-tile boards.
     */
    public void testCheckAround() {
        assertEquals(8, center.CheckAround().length);
        assertEquals(5, board.getTile(0, 1).CheckAround().length);
        assertEquals(3, board.getTile(0, 0).CheckAround().length);

        Board oneTileBoard = new Board(1, 1, 0);
        assertEquals(0, oneTileBoard.getTile(0, 0).CheckAround().length);
    }

    /**
     * Tests finding a surrounding mine count with both mined and safe
     * neighbors.
     */
    public void testFindCounter() {
        board.getTile(0, 0).setMine(true);
        board.getTile(0, 1).setMine(true);
        center.findCounter();
        assertEquals(2, center.getCounter());

        board.getTile(0, 0).setMine(false);
        board.getTile(0, 1).setMine(false);
        center.findCounter();
        assertEquals(0, center.getCounter());
    }

    /**
     * Tests counter getter and setter, including boundary values.
     */
    public void testCounterGetterSetter() {
        center.setCounter(0);
        assertEquals(0, center.getCounter());
        center.setCounter(8);
        assertEquals(8, center.getCounter());
    }

    /**
     * Tests mine getter and setter.
     */
    public void testMineGetterSetter() {
        center.setMine(true);
        assertTrue(center.getMine());
        center.setMine(false);
        assertFalse(center.getMine());
    }

    /**
     * Tests covered getter and setter.
     */
    public void testCoveredGetterSetter() {
        center.setCovered(false);
        assertFalse(center.getCovered());
        center.setCovered(true);
        assertTrue(center.getCovered());
    }

    /**
     * Tests both flag getter/setter pairs.
     */
    public void testFlagGetterSetter() {
        center.setFlagged(true);
        assertTrue(center.getFlagged());
        assertTrue(center.getFlag());

        center.setFlag(false);
        assertFalse(center.getFlagged());
        assertFalse(center.getFlag());

        center.setFlag(true);
        assertTrue(center.getFlag());
        center.setFlagged(false);
        assertFalse(center.getFlag());
    }

    /**
     * Tests both column naming styles plus row getter/setter.
     */
    public void testCoordinateGetterSetter() {
        center.setTileRow(2);
        center.setTileCol(0);
        assertEquals(2, center.getTileRow());
        assertEquals(0, center.getTileCol());
        assertEquals(0, center.getTileColumn());

        center.setTileColumn(2);
        assertEquals(2, center.getTileCol());
        assertEquals(2, center.getTileColumn());
    }

    /**
     * Tests BFS search-level getter and setter.
     */
    public void testSearchLevelGetterSetter() {
        center.setSearchLevel(4);
        assertEquals(4, center.getSearchLevel());
        center.setSearchLevel(-1);
        assertEquals(-1, center.getSearchLevel());
    }
}
