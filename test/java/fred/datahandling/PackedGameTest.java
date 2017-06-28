package fred.datahandling;

import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.fail;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.newdawn.slick.Color;

import fred.model.Game;
import fred.model.BasicGame;
import fred.model.BasicGameTest;
import fred.model.Player;
import fred.model.BasicPlayer;
import fred.model.World;

public class PackedGameTest {
	
	@Test
	public void testPackedGame() {
	    try {
	        new PackedGame(null);
	        fail("Null should have failed");
	    }
	    catch (NullPointerException ex) {
	        // Expected exception
	    }
	    
		MapLoader loader = new TestMapLoader();
		FileReader map = null;
		try {
			map = new FileReader("World.txt"); // Did I not say hard-coded paths rule?
		} catch( FileNotFoundException ex) {
			fail("no file");
			return;
		}
        World world = loader.load(map, Game.Type.Basic);
        
		List<Player> players = new ArrayList<Player>();
        players.add(new BasicPlayer("Tom"));
        players.add(new BasicPlayer("Dick"));
        players.add(new BasicPlayer("Harry"));
        
        Game game = new BasicGame(players, world);
        PackedGame pgame = new PackedGame(game);
        Game unpackedGame = pgame.unPack();
        assertNotSame(game, unpackedGame);
        BasicGameTest.assertGameEquals(game, unpackedGame);
	}
	
	   
    class TestMapLoader extends MapLoader {
        @Override
        protected void addTerritoryUI(String name, int posX, int posY, Color RGB) {
            // TODO - disabled until we get graphics unit tests working
        }
    }

	
}
