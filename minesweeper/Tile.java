//Group Project-1-Group94
package minesweeper;
/**
 * Represents A single minesweeper tile.
 * Should be able to return an array of all neighbors.
 * Should have a variable for isMine.
 * Should have a counter for the number of mines around it with a checker method that updates.
 * Should have isCovered variable with getter/setter.
 *
 * @author David Solyom (dsolyom)
 * @version 2026.09.08
 */
public class Tile{
   private Tile[][] tileArr;
   private int counter;
   private boolean mine;
   private boolean isCovered;
   private int tileRow;
   private int tileColumn;
   // Constructor
   public Tile(boolean isMine, Tile[][] tileArr, int tileRow, int tileColumn) {
       isCovered = false;
       this.mine = isMine;
       this.tileArr = tileArr;
       this.tileRow = tileRow;
       this.tileColumn = tileColumn;
   }
   
   public Tile[] CheckTop(){
       private Tile[] top;
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
   
}