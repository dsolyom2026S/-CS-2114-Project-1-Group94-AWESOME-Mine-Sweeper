package minesweeper;
import java.util.Scanner;

/**
 * 
 */
public class Main
{
    private Board newBoard;
    
    
    /**
     * Runs the main game loop
     * @param args this lets us know what kind of game are we doing
     * whether its going to be a custom game or regular game
     * 
     */
    public void main(String[] args)
    {
        
    }
    /**
     * creates a new game
     * @param scanner this scans the user input to see what the coordinates are
     * @return returns a board in which the new game will be played on
     */
    public Board newGame(Scanner scanner)
    {
        
        System.out.print("Enter your what difficulty you want easy medium or hard: ");
        String difficulty = scanner.next();
        if(difficulty.equals("easy")) {
            boardNew = new Board(9,9,10);
        }
        else if(difficulty.equals("medium")
            {
            boardNew = new Board(16,16,40);
            }
        else if(difficulty.equals("hard")
            {
            boardNew = new Board(30,16,99);
            }
        else
        {
            throw new IllegalArgumentException();
        }
        return boardNew;
        
       //create if statemtents going through if easy, medium, or hard 
       
        
    }
    /**
     * creates a new custom game
     * @param scanner this scans the user input to see what the coordinates are
     * @return returns a custom board in which the new game will be played on
     */
    public void customGame(Scanner scanner, Board board)
    {
        
    }
    public String getUserInput(Scanner scanner)
    {
        
    }
    public boolean scannerWin(Scanner scanner)
    {
        
    }
    public boolean scannerLose(Scanner scanner)
    {
        
    }
    //~ Fields ................................................................

    //~ Constructors ..........................................................

    //~Public  Methods ........................................................

}
