package minesweeper;

import java.util.Scanner;

/**
 * Runs the console version of Minesweeper. Handles menus, reading player
 * commands, and the play-again loop. Game logic lives in Board and Tile.
 *
 * Run with no arguments for a normal game, or with "custom" for a game where
 * you place the mines yourself.
 */
public class Main
{
    private Board newBoard;

    /**
     * Runs the main game loop
     *
     * @param args
     *            "custom" starts a custom game; anything else (or nothing)
     *            starts a regular game
     */
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        Main game = new Main();
        boolean custom = args.length > 0 && args[0].equals("custom");
        boolean playAgain = true;

        while (playAgain)
        {
            if (custom)
            {
                int rows = game.readPositiveInt(scanner, "Enter number of rows: ");
                int cols =
                    game.readPositiveInt(scanner, "Enter number of columns: ");
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

                // Keep asking until the player gives a usable command
                String result = game.getUserInput(scanner);
                System.out.println(result);
                while (isError(result))
                {
                    System.out.println(
                        "Please try again: r row col (reveal) or f row col (flag).");
                    result = game.getUserInput(scanner);
                    System.out.println(result);
                }
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
     * returns the board currently being played
     *
     * @return current board
     */
    public Board getBoard()
    {
        return newBoard;
    }


    /**
     * sets the board currently being played
     *
     * @param board
     *            board to play on
     */
    public void setBoard(Board board)
    {
        newBoard = board;
    }


    /**
     * creates a new game
     *
     * @param scanner
     *            this scans the user input for the difficulty
     * @return returns a board in which the new game will be played on
     * @throws IllegalArgumentException
     *             if difficulty is not easy, medium, or hard
     */
    public Board newGame(Scanner scanner)
    {
        System.out
            .print("Enter what difficulty you want (easy, medium, or hard): ");
        String difficulty = scanner.next();
        if (difficulty.equals("easy"))
        {
            newBoard = new Board(9, 9, 10);
        }
        else if (difficulty.equals("medium"))
        {
            newBoard = new Board(16, 16, 40);
        }
        else if (difficulty.equals("hard"))
        {
            newBoard = new Board(30, 16, 99);
        }
        else
        {
            throw new IllegalArgumentException();
        }
        return newBoard;
    }


    /**
     * sets up a custom game by placing mines where the user says.
     * The user enters "row col" pairs, then "done" to finish.
     *
     * @param scanner
     *            this scans the user input for the mine coordinates
     * @param board
     *            this gives the board on which to place the mines on
     * @postcondition board is ready to play (counters calculated)
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
     * This gets the user input on where they want to place a flag, or click,
     * and applies it to the board. Commands: "r row col" (reveal) or
     * "f row col" (flag/unflag).
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
            skipRestOfLine(scanner);
            return "Invalid command.";
        }

        if (!scanner.hasNextInt())
        {
            skipRestOfLine(scanner);
            return "Invalid coordinates.";
        }
        int row = scanner.nextInt();

        if (!scanner.hasNextInt())
        {
            skipRestOfLine(scanner);
            return "Invalid coordinates.";
        }
        int col = scanner.nextInt();

        if (!newBoard.inBounds(row, col))
        {
            skipRestOfLine(scanner);
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
     * tells the user they won and asks whether to play again
     *
     * @return returns true if they want to play again, false if not
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
     * tells the user they lost and asks whether to play again
     *
     * @return returns true if they want to play again, false if not
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
            throw new IllegalArgumentException();
        }
    }


    /**
     * asks for a positive whole number, re-asking until the user gives one
     *
     * @param scanner
     *            which scans the user input
     * @param prompt
     *            the message shown before each attempt
     * @return the positive number the user entered
     */
    public int readPositiveInt(Scanner scanner, String prompt)
    {
        while (true)
        {
            System.out.print(prompt);
            String token = scanner.next();
            if (isInt(token) && Integer.parseInt(token) > 0)
            {
                return Integer.parseInt(token);
            }
            System.out.println("Invalid input, enter a positive whole number.");
        }
    }


    /**
     * checks whether a message from getUserInput means the command failed
     *
     * @param message
     *            message returned by getUserInput
     * @return true if the command was not carried out
     */
    public static boolean isError(String message)
    {
        return message.startsWith("Invalid")
            || message.startsWith("Coordinates")
            || message.startsWith("Cannot");
    }


    private static void skipRestOfLine(Scanner scanner)
    {
        if (scanner.hasNextLine())
        {
            scanner.nextLine();
        }
    }


    private static boolean isInt(String s)
    {
        try
        {
            Integer.parseInt(s);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }
}