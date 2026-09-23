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
     * 
     * @param args
     *            this lets us know what kind of game are we doing whether its
     *            going to be a custom game or regular game
     */
    public void main(String[] args)
    {   
        
    }


    /**
     * creates a new game
     * 
     * @param scanner
     *            this scans the user input to see what the coordinates are
     * @return returns a board in which the new game will be played on
     */
    public Board newGame(Scanner scanner)
    {

        System.out
            .print("Enter your what difficulty you want easy medium or hard: ");
        String difficulty = scanner.next();
        if (difficulty.equals("easy"))
        {
            boardNew = new Board(9, 9, 10);
        }
        else if (difficulty.equals("medium"))
        {
            boardNew = new Board(16, 16, 40);
        }
        else if (difficulty.equals("hard"))
        {
            boardNew = new Board(30, 16, 99);
        }
        else
        {
            throw new IllegalArgumentException();
        }
        return boardNew;

        // create if statements going through if easy, medium, or hard

    }


    /**
     * creates a new custom game
     * 
     * @param scanner
     *            this scans the user input to see what the coordinates are of
     *            the mines
     * @param board
     *            this gives the board on which to place the mines on
     * @return returns a custom board in which the new game will be played on
     */
    public void customGame(Scanner scanner, Board board)
    {
        System.out
        .print("Enter your what difficulty you want easy medium or hard: ");
    String difficulty = scanner.next();
    }


    /**
     * This gets the user input on where they want to place a flag, or click
     * 
     * @param scanner
     *            this scans the user inputs, such as coordinates and the flag
     *            button
     * @return this returns the string, which indicates if their action was
     *             successful or not
     */
    public String getUserInput(Scanner scanner)
    {
           String string = "";
           return string;
    }


    /**
     * this tells the user whether they win or not
     * 
     * @return returns true if they win false if lose
     * @param scanner
     *            which scans the user input
     */
    public boolean scannerWin(Scanner scanner)
    {
        System.out.print("You win! Would you like to play again(yes/no)? ");
        String response = scanner.next();
        if (response.equals("yes"))
        {
            return true;
        }
        else if (response.equals("no"))
        {
            return false;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }


    /**
     * this tells the user whether they win or not
     * 
     * @return returns true if they win false if lose
     * @param scanner
     *            which scans the user input
     */
    public boolean scannerLose(Scanner scanner)
    {
        System.out.print(
            "KABOOM. You hit a bomb :(. Would you like to play again(yes/no)? ");
        String response = scanner.next();
        if (response.equals("yes"))
        {
            return true;
        }
        else if (response.equals("no"))
        {
            return false;
        }
        else
        {
            // might change to have a message play saying invalid input
            throw new IllegalArgumentException();
        }
    }
    // ~ Fields ................................................................

    // ~ Constructors ..........................................................

    // ~Public Methods ........................................................

}
