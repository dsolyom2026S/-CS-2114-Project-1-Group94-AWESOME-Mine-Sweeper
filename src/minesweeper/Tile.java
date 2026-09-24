// Group Project-1-Group94
package minesweeper;

/**
 * An object of the Tile class represents one individual square on a
 * Minesweeper board. It stores whether it is covered, flagged, or a mine,
 * along with its surrounding-mine count, coordinates, BFS search level, and
 * a reference to its Board.
 *
 * @author David Solyom (dsolyom)
 * @version 2026.09.24
 * @author Maple Stechl (stechlmh)
 * @version 2026.09.24
 */
public class Tile {

    // Board containing this tile
    private Board board;

    // Number of surrounding mines
    private int counter;

    // State of this tile
    private boolean mine;
    private boolean flagged;
    private boolean covered;

    // Coordinates of this tile
    private int tileRow;
    private int tileCol;

    // BFS level (-1 means not currently part of a search)
    private int searchLevel;

    // ----------------------------------------------------------

    /**
     * Creates a covered, unflagged, non-mine tile at a location on a board.
     *
     * @param board
     *            board containing this tile
     * @param tileRow
     *            row coordinate
     * @param tileCol
     *            column coordinate
     */
    public Tile(Board board, int tileRow, int tileCol) {
        this.board = board;
        this.tileRow = tileRow;
        this.tileCol = tileCol;
        counter = 0;
        mine = false;
        flagged = false;
        covered = true;
        searchLevel = -1;
    }

    // ----------------------------------------------------------

    /**
     * Returns all valid tiles touching this tile horizontally, vertically,
     * or diagonally.
     *
     * @return array containing every valid neighboring tile
     */
    public Tile[] CheckAround() {
        Tile[] possibleNeighbors = new Tile[8];
        int neighborCount = 0;

        for (int rowChange = -1; rowChange <= 1; rowChange++) {
            for (int colChange = -1; colChange <= 1; colChange++) {
                if (rowChange == 0 && colChange == 0) {
                    continue;
                }

                int checkRow = tileRow + rowChange;
                int checkCol = tileCol + colChange;

                if (board.inBounds(checkRow, checkCol)) {
                    possibleNeighbors[neighborCount] =
                        board.getTile(checkRow, checkCol);
                    neighborCount++;
                }
            }
        }

        Tile[] neighbors = new Tile[neighborCount];
        for (int i = 0; i < neighborCount; i++) {
            neighbors[i] = possibleNeighbors[i];
        }

        return neighbors;
    }

    // ----------------------------------------------------------

    /**
     * Finds and stores how many surrounding tiles contain mines.
     *
     * @postcondition counter is equal to the number of surrounding mines
     */
    public void findCounter() {
        int count = 0;
        Tile[] neighbors = CheckAround();

        for (int i = 0; i < neighbors.length; i++) {
            if (neighbors[i].getMine()) {
                count++;
            }
        }

        setCounter(count);
    }

    // ----------------------------------------------------------

    /**
     * Returns the number of surrounding mines.
     *
     * @return surrounding mine count
     */
    public int getCounter() {
        return counter;
    }

    /**
     * Sets the surrounding mine count.
     *
     * @param count
     *            surrounding mine count
     */
    public void setCounter(int count) {
        counter = count;
    }

    /**
     * Returns whether this tile contains a mine.
     *
     * @return true when this tile contains a mine
     */
    public boolean getMine() {
        return mine;
    }

    /**
     * Sets whether this tile contains a mine.
     *
     * @param isMine
     *            new mine state
     */
    public void setMine(boolean isMine) {
        mine = isMine;
    }

    /**
     * Returns this tile's row coordinate.
     *
     * @return row coordinate
     */
    public int getTileRow() {
        return tileRow;
    }

    /**
     * Sets this tile's row coordinate.
     *
     * @param row
     *            new row coordinate
     */
    public void setTileRow(int row) {
        tileRow = row;
    }

    /**
     * Returns this tile's column coordinate.
     *
     * @return column coordinate
     */
    public int getTileCol() {
        return tileCol;
    }

    /**
     * Sets this tile's column coordinate.
     *
     * @param col
     *            new column coordinate
     */
    public void setTileCol(int col) {
        tileCol = col;
    }

    /**
     * Returns the Board containing this tile.
     *
     * @return containing Board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns whether this tile is covered.
     *
     * @return true when this tile is covered
     */
    public boolean getCovered() {
        return covered;
    }

    /**
     * Sets whether this tile is covered.
     *
     * @param isCovered
     *            new covered state
     */
    public void setCovered(boolean isCovered) {
        covered = isCovered;
    }

    /**
     * Returns whether this tile is flagged.
     *
     * @return true when this tile is flagged
     */
    public boolean getFlagged() {
        return flagged;
    }

    /**
     * Sets whether this tile is flagged.
     *
     * @param isFlagged
     *            new flag state
     */
    public void setFlagged(boolean isFlagged) {
        flagged = isFlagged;
    }

    /**
     * Returns current BFS search level.
     *
     * @return search level, or -1 when not part of a search
     */
    public int getSearchLevel() {
        return searchLevel;
    }

    /**
     * Sets current BFS search level.
     *
     * @param level
     *            BFS level
     */
    public void setSearchLevel(int level) {
        searchLevel = level;
    }

    // ----------------------------------------------------------
    // Compatibility methods used by existing Board/Main code and tests.

    /**
     * Returns this tile's column coordinate.
     *
     * @return column coordinate
     */
    public int getTileColumn() {
        return getTileCol();
    }

    /**
     * Sets this tile's column coordinate.
     *
     * @param column
     *            new column coordinate
     */
    public void setTileColumn(int column) {
        setTileCol(column);
    }

    /**
     * Returns whether this tile is flagged.
     *
     * @return true when this tile is flagged
     */
    public boolean getFlag() {
        return getFlagged();
    }

    /**
     * Sets whether this tile is flagged.
     *
     * @param isFlagged
     *            new flag state
     */
    public void setFlag(boolean isFlagged) {
        setFlagged(isFlagged);
    }
}
