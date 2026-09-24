#  CS-2114-Project-1-Group94-AWESOME-Mine-Sweeper

How to download and run the game
1. Go to the GitHub page for the project.
2. Click Code, then click Download ZIP.
3. Extract the ZIP file.
4. Open the extracted project folder.
5. Open Command Prompt, PowerShell, or Terminal in that folder.
6. Make sure Java is installed by typing:
java -version
javac -version
7. Create a folder for compiled files:
mkdir bin
8. Compile the game.
Windows:
javac -d bin src\minesweeper\Tile.java src\minesweeper\Board.java src\minesweeper\Main.java
Mac or Linux:
javac -d bin src/minesweeper/Tile.java src/minesweeper/Board.java src/minesweeper/Main.java
9. Start a normal game:
java -cp bin minesweeper.Main
10. Choose easy, medium, or hard when asked.
To start a custom game, use:
java -cp bin minesweeper.Main custom
In custom mode, enter the number of rows and columns, then enter mine locations as row column. Type done when you are finished placing mines.
While playing, use:
r row column
to reveal a tile.
Use:
f row column
to place or remove a flag.