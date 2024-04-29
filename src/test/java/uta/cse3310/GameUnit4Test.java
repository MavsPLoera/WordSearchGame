package uta.cse3310;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.ArrayList;

public class GameUnit4Test extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public GameUnit4Test( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( GameUnit4Test.class );
    }

    //Testing adding to a players score on gameover
    public void testGame4()
    {
        ArrayList<User> testPlayers = new ArrayList<User>(); 
        User p1 = new User();
        p1.name = "Test Player 1";
        p1.socket = new TestConnection();
        User p2 = new User();
        p2.name = "Test Player 2";
        p2.socket = new TestConnection();

        testPlayers.add(p1);
        testPlayers.add(p2);

        
        Game testGame = new Game(testPlayers);
        int wordListSize = testGame.grid.wordIndices.size();
        testGame.grid.useSeed = true;
        
        Point point = new Point();
        Point point2 = new Point();
        point.x = 16;
        point.y = 11;
        point2.x = 11;
        point2.y = 16;

        //Should not work
        testGame.input( testPlayers.get(0), point2);
        testGame.input( testPlayers.get(0), point);
        assertTrue(testGame.grid.grid[16][11].foundBy.size() == 0);
        assertTrue(testGame.grid.grid[13][14].foundBy.size() == 0);

        //User selecting start and end of the word
        testGame.input( testPlayers.get(0), point);
        testGame.input( testPlayers.get(1), point);
        testGame.input( testPlayers.get(0), point2);

        //Checks if other players selectedPoint was removed when word was found. 
        assertTrue(testPlayers.get(1).selectedPoint.equals(point));
        assertTrue(testGame.grid.grid[16][11].foundBy.contains(0));
        assertTrue(testGame.grid.grid[13][14].foundBy.contains(0));
        assertTrue(testGame.grid.grid[11][16].foundBy.contains(0));
        assertTrue(testGame.grid.wordIndices.size() == (wordListSize-1));
        
    }
}
