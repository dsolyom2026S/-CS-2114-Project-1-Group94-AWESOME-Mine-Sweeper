//Group Project-1-Group94
package minesweeper;
/**
<<<<<<< Updated upstream
 * An object of the Tile class represents one individual square on the
 * Minesweeper board. It Stores whether it is covered, flagged, or a mine,
 * along with its surrounding mine count, coordinates, BFS search level, and 
 * reference to its Board.
=======
 * Represents A single minesweeper tile.
 * Should be able to return an array of all neighbors.
 * Should have a variable for isMine.
 * Should have a counter for the number of mines around it with a checker method that updates.
 * Should have isCovered variable with getter/setter.
 * Should have a isFlaged variable with getter/setter.
>>>>>>> Stashed changes
 *
 * @author David Solyom (dsolyom)
 * @version 2026.09.08
 * @author Maple Stechl (stechlmh)
 * @version 2026.09.17
 */
public class Tile{
    
   // Reference to board tile is a part of
   private Tile[][] board;
   // Number of surrounding mines
   private int counter = 0;
   // Booleans denoting the state of tile
   private boolean mine = false;
   private boolean flag = false;
   private boolean covered = true;
   // Coordinates of tile relative to board
   private int tileRow;
   private int tileColumn;
   // BFS level (-1 means not currently part of a search)
   private int searchLevel;
   
   // Constructor
   public Tile(boolean mine, Tile[][] board, int tileRow, int tileColumn) {
       this.board = board;
       this.tileRow = tileRow;
       this.tileColumn = tileColumn;
   }
   
   public Tile[] CheckAround(){
       Arraylist <Tile>  top;
       if(tileRow != 0) {
           if(tileColumn != 0) {
               if(tileColumn != (tileArr[tileRow].length-1)){
                   for(int i = -1; i > 2; i++) {
                       top[i+1]= tileArr[tileRow-1][tileColumn+i];
                   }
               }
               else {
                   for(int i = -1; i > 1; i++) {
                       top[i+1]= tileArr[tileRow-1][tileColumn+i];
                   }
               }
           }
           else {
               for(int i = 0; i > 2; i++) {
                   top[i]= tileArr[tileRow-1][tileColumn+i];
               }
           }
       }
       else {
       return null;
       }
       for(int i = 0; i < top.length; i++) {
           if(top[i].isMine()) {
                   counter++;
           }
       }
   }
   // ----------------------------------------------------------
   /**
    * finds and sets the number of mines surrounding the tile
    *
    * @postcondition counter of tile is set to the number of mines surrounding it
    */
   public void findCounter() {
       int count;
       //code to find number of mines around tile
       //TO BE IMPLEMENTED BY DAVID
       setCounter(count);
   }
   /**
    * returns the number of mines around tile
    *
    * @return int counter
    */
   public int getCounter() {
       return counter;
   }
   /**
    * sets counter to count
    *
    * @param count
    *            number of mines around tile
    * @postcondition counter set to count
    */
   public void setCounter(int count) {
       counter = count;
   }
   /**
    * returns if the tile is a mine
    *
    * @return boolean mine
    */
   public boolean getMine() {
       return mine;
   }
   /**
    * sets the tile to a mine
    *
    * @param isMine
    *            whther or not object is a mine
    * @postcondition mine set to isMine
    */
   public void setMine(boolean isMine) {
       mine = isMine;
   }
   /**
    * returns the tile's row relative to board
    *
    * @return int tileRow
    */
   public int getTileRow() {
       return tileRow;
   }
   /**
    * returns the tile's column relative to board
    *
    * @return int tileColumn
    */
   public int getTileColumn() {
       return tileColumn;
   }
   /**
    * returns whether the board the tile is a part of
    *
    * @return Tile[][] board
    */
   public Tile[][] getBoard() {
       return board;
   }
   /**
    * returns whether the tile is covered
    *
    * @return boolean covered
    */
   public boolean getCovered() {
       return covered;
   }
   /**
    * sets covered to isCovered
    *
    * @param isCovered
    *            whether the tile is covered
    * @postcondition covered set to isCovered
    */
   public void setCovered(boolean isCovered) {
       covered = isCovered;
   }
   /**
    * returns whether the tile is flagged
    *
    * @return boolean flag
    */
   public boolean getFlag() {
       return flag;
   }
   /**
    * sets flag to isFlagged
    *
    * @param isFlagged
    *            whether the tile is flagged
    * @postcondition flag set to isFlagged
    */
   public void setCovered(boolean isFlagged) {
       flag = isFlagged;
   }
   /**
    * returns current search level
    *
    * @return int searchLevel
    */
   public int getSearchLevel() {
       return covered;
   }
   /**
    * sets searchLevel to level
    *
    * @param level
    *            BFS Depth
    * @postcondition covered set to isCovered
    */
   public void setSearchLevel(int level) {
       searchLevel = level;
   }
}