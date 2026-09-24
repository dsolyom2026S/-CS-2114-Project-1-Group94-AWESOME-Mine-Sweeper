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
        Scanner scanner = new Scanner(System.in);
        Main game = new Main();
        boolean custom = args.length > 0 && args[0].equals("custom");
        boolean playAgain = true;
 
        while (playAgain)
        {
            if (custom)
            {
                System.out.print("Enter number of rows and columns: ");
                int rows = scanner.nextInt();
                int cols = scanner.nextInt();
                game.newBoard = new Board(rows, cols, 0);
                game.customGame(scanner, game.newBoard);
            }
            else
            {
                boolean chosen = false;
                while (!chosen)
                {
                    try
                    {
                        game.newBoard = game.newGame(scanner);
                        chosen = true;
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.out.println("Invalid difficulty, try again.");
                    }
                }
            }
 
            // Play until the game is won or lost
            while (!game.newBoard.getGameOver() && !game.newBoard.checkWin())
            {
                System.out.println(game.newBoard.boardToString());
                System.out.println(game.getUserInput(scanner));
            }
            System.out.println(game.newBoard.boardToString());
 
            boolean answered = false;
            while (!answered)
            {
                try
                {
                    if (game.newBoard.getGameOver())
                    {
                        playAgain = game.scannerLose(scanner);
                    }
                    else
                    {
                        playAgain = game.scannerWin(scanner);
                    }
                    answered = true;
                }
                catch (IllegalArgumentException e)
                {
                    System.out.println("Please answer yes or no.");
                }
            }
        }
        scanner.close();

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
        scanner.close();
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
        newBoard = board;
        System.out.println(
            "Enter mine locations as: row col. Type done when finished.");
 
        while (scanner.hasNext())
        {
            String token = scanner.next();
            if (token.equals("done"))
            {
                break;
            }
 
            if (!scanner.hasNextInt() || !isInt(token))
            {
                System.out.println("Invalid input, use: row col");
                continue;
            }
 
            int row = Integer.parseInt(token);
            int col = scanner.nextInt();
 
            if (board.inBounds(row, col))
            {
                board.getTile(row, col).setMine(true);
            }
            else
            {
                System.out.println("Coordinates out of bounds.");
            }
        }
 
        board.finalizeCustomGame();
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
        System.out.print("Enter command (r row col / f row col): ");
        String command = scanner.next();
 
        if (!command.equals("r") && !command.equals("f"))
        {
            return "Invalid command.";
        }
 
        if (!scanner.hasNextInt())
        {
            if (scanner.hasNext())
            {
                scanner.next();
            }
            return "Invalid coordinates.";
        }
        int row = scanner.nextInt();
 
        if (!scanner.hasNextInt())
        {
            if (scanner.hasNext())
            {
                scanner.next();
            }
            return "Invalid coordinates.";
        }
        int col = scanner.nextInt();
 
        if (!newBoard.inBounds(row, col))
        {
            return "Coordinates out of bounds.";
        }
 
        Tile tile = newBoard.getTile(row, col);
 
        if (command.equals("f"))
        {
            if (!tile.getCovered())
            {
                return "Cannot flag a revealed tile.";
            }
            newBoard.Flag(row, col);
            return tile.getFlag() ? "Tile flagged." : "Flag removed.";
        }
 
        if (tile.getFlag() || !tile.getCovered())
        {
            return "Cannot reveal that tile.";
        }
        newBoard.revealTile(row, col);
        return "Tile revealed.";
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
