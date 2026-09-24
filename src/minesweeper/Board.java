//Group Project-1-Group94

package minesweeper;

/**
 * An object of the Board class represents a Minesweeper board.
 * It owns the Tile[][] and handles the main game logic including
 * mine generation, counters, revealing tiles, flagging, BFS,
 * win/loss conditions, custom boards, and creating the printable board.
 *
 * @author David Solyom (dsolyom)
 * @version 2026.09.24
 */
public class Board {

    // 2D array containing all tiles on board
    private Tile[][] board;

    // Number of rows and columns on board
    private int rows;
    private int columns;

    // Number of mines on board
    private int numberOfMines;

    // Whether game has ended in a loss
    private boolean gameOver = false;

    // Whether first move has been made
    private boolean firstMove = true;

    // Number of safe tiles that are still covered
    private int safeTilesRemaining;

    // ----------------------------------------------------------

    /**
     * Creates a board with given size and number of mines.
     *
     * @param rows
     *            number of rows
     * @param columns
     *            number of columns
     * @param numberOfMines
     *            number of mines
     * @postcondition board is created and every location contains a Tile
     */
    public Board(int rows, int columns, int numberOfMines) {

        this.rows = rows;
        this.columns = columns;
        this.numberOfMines = numberOfMines;

        board = new Tile[rows][columns];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                board[row][col] = new Tile(this, row, col);
            }
        }

        safeTilesRemaining = rows * columns - numberOfMines;
    }

    // ----------------------------------------------------------

    /**
     * returns a printable version of board
     *
     * @return String representation of board
     */
    public String boardToString() {

        String result = "       COLUMNS\n";

        // Print column numbers across top of board.
        result += "       ";
        for (int col = 0; col < columns; col++) {
            if (col > 0) {
                result += "  ";
            }
            result += col;
        }
        result += "\n";

        // Print each row number along left side of board.
        for (int row = 0; row < rows; row++) {
            result += "ROW " + row;
            if (row < 10) {
                result += "  ";
            }
            else {
                result += " ";
            }

            for (int col = 0; col < columns; col++) {
                if (col > 0) {
                    result += "  ";
                }

                Tile tile = board[row][col];

                if (tile.getCovered()) {
                    if (tile.getFlag()) {
                        result += "F";
                    }
                    else {
                        result += "#";
                    }
                }
                else if (tile.getMine()) {
                    result += "*";
                }
                else {
                    result += tile.getCounter();
                }
            }

            result += "\n";
        }

        return result;
    }

    // ----------------------------------------------------------

    /**
     * returns tile at given coordinate
     *
     * @param row
     *            row of tile
     * @param col
     *            column of tile
     * @return Tile at coordinate
     */
    public Tile getTile(int row, int col) {

        return board[row][col];
    }

    // ----------------------------------------------------------

    /**
     * returns whether game is over
     *
     * @return boolean gameOver
     */
    public boolean getGameOver() {

        return gameOver;
    }

    // ----------------------------------------------------------

    /**
     * reveals tile at given coordinate
     *
     * @param row
     *            row of tile
     * @param col
     *            column of tile
     * @postcondition tile is revealed if it can be revealed
     */
    public void revealTile(int row, int col) {

        if (!inBounds(row, col)) {
            return;
        }

        Tile tile = board[row][col];

        // Ignore flagged or already uncovered tiles
        if (tile.getFlag() || !tile.getCovered()) {
            return;
        }

        // Generate mines only after first click
        if (firstMove) {
            generateMines(row, col);
            calculateCounters();
            firstMove = false;
        }

        // Player selected a mine
        if (tile.getMine()) {
            gameOver();
            return;
        }

        // Reveal selected safe tile
        tile.setCovered(false);
        safeTilesRemaining--;

        // Start BFS if tile has no surrounding mines
        if (tile.getCounter() == 0) {
            revealEmptyArea(row, col);
        }
    }

    // ----------------------------------------------------------

    /**
     * checks whether all safe tiles are uncovered
     *
     * @return true if player has won
     */
    public boolean checkWin() {

        return safeTilesRemaining == 0 && !gameOver;
    }

    // ----------------------------------------------------------

    /**
     * flags or unflags a covered tile
     *
     * @param row
     *            row of tile
     * @param col
     *            column of tile
     * @postcondition covered tile has its flag toggled
     */
    public void Flag(int row, int col) {

        if (!inBounds(row, col)) {
            return;
        }

        Tile tile = board[row][col];

        // Uncovered tiles cannot be flagged
        if (!tile.getCovered()) {
            return;
        }

        tile.setFlag(!tile.getFlag());
    }

    // ----------------------------------------------------------

    /**
     * ends game and reveals all mines
     *
     * @postcondition gameOver is true and mines are uncovered
     */
    public void gameOver() {

        gameOver = true;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {

                if (board[row][col].getMine()) {
                    board[row][col].setCovered(false);
                }
            }
        }
    }

    // ----------------------------------------------------------

    /**
     * randomly places mines while protecting the first click and all 8
     * surrounding neighbors. This guarantees the first revealed tile has a
     * counter of 0, so BFS will expand from it.
     *
     * @param safeRow
     *            row of first clicked tile
     * @param safeCol
     *            column of first clicked tile
     * @postcondition requested number of mines are placed outside the 3x3 safe
     *                zone centered on the first click
     */
    public void generateMines(int safeRow, int safeCol) {

        int minesPlaced = 0;

        while (minesPlaced < numberOfMines) {

            int row = (int)(Math.random() * rows);
            int col = (int)(Math.random() * columns);

            // First clicked tile and all touching neighbors must stay safe.
            if (Math.abs(row - safeRow) <= 1
                && Math.abs(col - safeCol) <= 1) {
                continue;
            }

            // Do not place two mines in same location
            if (board[row][col].getMine()) {
                continue;
            }

            board[row][col].setMine(true);
            minesPlaced++;
        }
    }

    // ----------------------------------------------------------

    /**
     * removes all mines and resets board
     *
     * @postcondition all mines and counters are reset
     */
    public void clearMines() {

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {

                Tile tile = board[row][col];

                tile.setMine(false);
                tile.setCounter(0);
            }
        }

        gameOver = false;
        firstMove = true;
        safeTilesRemaining = rows * columns;
    }

    // ----------------------------------------------------------

    /**
     * calculates surrounding mine count for every tile
     *
     * @postcondition every tile stores correct surrounding mine count
     */
    public void calculateCounters() {

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {

                board[row][col].findCounter();
            }
        }
    }

    // ----------------------------------------------------------

    /**
     * checks whether coordinate is inside board
     *
     * @param row
     *            row being checked
     * @param col
     *            column being checked
     * @return true if coordinate is valid
     */
    public boolean inBounds(int row, int col) {

        return row >= 0
            && row < rows
            && col >= 0
            && col < columns;
    }

    // ----------------------------------------------------------

    /**
     * reveals connected empty area using BFS
     *
     * @param row
     *            starting row
     * @param col
     *            starting column
     * @postcondition connected zero tiles and bordering numbered tiles
     *                are revealed
     */
    public void revealEmptyArea(int row, int col) {

        resetSearchLevels();

        Tile start = board[row][col];
        start.setSearchLevel(0);

        int currentLevel = 0;
        boolean foundNewTile = true;

        // BFS uses search levels instead of a Queue
        while (foundNewTile) {

            foundNewTile = false;

            for (int currentRow = 0; currentRow < rows; currentRow++) {
                for (int currentCol = 0;
                    currentCol < columns;
                    currentCol++) {

                    Tile current = board[currentRow][currentCol];

                    if (current.getSearchLevel() == currentLevel
                        && current.getCounter() == 0) {

                        for (int rowChange = -1;
                            rowChange <= 1;
                            rowChange++) {

                            for (int colChange = -1;
                                colChange <= 1;
                                colChange++) {

                                if (rowChange == 0 && colChange == 0) {
                                    continue;
                                }

                                int checkRow = currentRow + rowChange;
                                int checkCol = currentCol + colChange;

                                if (inBounds(checkRow, checkCol)) {

                                    Tile next =
                                        board[checkRow][checkCol];

                                    // Flagged tiles stay covered
                                    if (next.getFlag()) {
                                        continue;
                                    }

                                    if (next.getSearchLevel() == -1) {

                                        next.setSearchLevel(
                                            currentLevel + 1);

                                        if (next.getCovered()
                                            && !next.getMine()) {

                                            next.setCovered(false);
                                            safeTilesRemaining--;
                                        }

                                        foundNewTile = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            currentLevel++;
        }

        resetSearchLevels();
    }

    // ----------------------------------------------------------

    /**
     * resets all BFS search levels
     *
     * @postcondition every tile has search level -1
     */
    public void resetSearchLevels() {

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {

                board[row][col].setSearchLevel(-1);
            }
        }
    }

    // ----------------------------------------------------------

    /**
     * prepares manually created mine layout for play
     *
     * @postcondition mine count and counters are calculated
     */
    public void finalizeCustomGame() {

        numberOfMines = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {

                if (board[row][col].getMine()) {
                    numberOfMines++;
                }
            }
        }

        calculateCounters();

        safeTilesRemaining =
            rows * columns - numberOfMines;

        firstMove = false;
        gameOver = false;

        resetSearchLevels();
    }
}
