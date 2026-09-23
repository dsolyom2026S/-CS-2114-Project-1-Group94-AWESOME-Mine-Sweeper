
package minesweeper;

/*
Owns the Tile[][] and handles the main Minesweeper game 
logic, including mine generation, counters, 
revealing Tiles, flagging, BFS, win/loss conditions, 
custom boards, and creating the printable board.
*/

public class Board{
    private Tile [][] board;
    private int rows;
    private int columns;
    private int numberOfMines;
    private boolean gameOver;
    private boolean firstMove;
    private int safeTilesRemaining;
    //constructor
    public Board(int rows, int columns, int numberOfMines){
            board = new Tile[rows][columns];
    }
    public String BoardToString(){
        String board = "";
        for(int i =0; i<rows; i++) {
            board += " " + this.board[i].toString() + "\n";
            
        }
        return board;
    }
    
}