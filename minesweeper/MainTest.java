import static org.junit.Assert.*;
import org.junit.Test;

public class MainTest

{

    private Main game;
    
    @Before
    public void setUp() {
        game = new Main();
    }
    @Test
    public void testMain()
    {
        fail("Not yet implemented");
    }
    //~ Fields ................................................................
    @Test
    public void testNewGameEasy() {
        Board b = game.newGame(in("easy"));
        assertNotNull(b);
        assertNotNull(b.getTile(8, 8));
        assertFalse(b.inBounds(9, 9));
    }

    @Test
    public void testNewGame()
    {
        fail("Not yet implemented");
    }


    @Test
    public void testCustomGame()
    {
        fail("Not yet implemented");
    }


    @Test
    public void testGetUserInput()
    {
        fail("Not yet implemented");
    }


    @Test
    public void testScannerWin()
    {
        fail("Not yet implemented");
    }


    @Test
    public void testScannerLose()
    {
        fail("Not yet implemented");
    }

    //~ Constructors ..........................................................

    //~Public  Methods ........................................................

}
