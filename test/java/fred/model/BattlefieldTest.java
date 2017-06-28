package fred.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class BattlefieldTest {
    
	public List<Player> players = new ArrayList<Player>();
	public World world;
	public Territory territory1 = new BasicTerritory("1");
	public Territory territory2 = new BasicTerritory("2");
	public Territory territory3 = new BasicTerritory("3");
	private Territory territory4 = new BasicTerritory("4");
	private Territory territory5 = new BasicTerritory("5");
	public Player player1 = new BasicPlayer("player1");
	private Player player2 = new BasicPlayer("player2");
	private List<TerritoryGroup> territoryGroups = new ArrayList<TerritoryGroup>();
	private TerritoryGroup territoryGroup1;
	private TerritoryGroup territoryGroup2;

	private World conscriptionWorld;
	private Game basicTestGame;
	
    @Test
    public void testConstructor() {
        Battlefield battlefield = new Battlefield();
      
        assertEquals(0, battlefield.getAttackers());
        assertEquals(0, battlefield.getDefenders());
        assertEquals(true, battlefield.isCleared());
        assertEquals(false, battlefield.isFinished());
        assertEquals(false, battlefield.isSuccess());
        assertTrue(battlefield.getInitialAttackers().isEmpty());
        assertFalse(battlefield.getSources().isEmpty());
        
    }

    @Test
	public void testDieOverride() {
		Game game = new BasicGame(players, world);
		TestDie die = new TestDie();
		game.setDie(die);
		assertSame(die, game.getDie());

		try {
			game.getDie().roll();
			fail("empty test die values should have failed");
		} catch (RuntimeException ex) {
			// Expected exception
		}

		die.setValues(new int[] { 2, 4, 6 });
		assertEquals(2, game.getDie().roll());
		assertEquals(4, game.getDie().roll());
		assertEquals(6, game.getDie().roll());

		try {
			game.getDie().roll();
			fail("no more test die values should have failed");
		} catch (RuntimeException ex) {
			// Expected exception
		}
	}

	assertNotNull(getDie());
	assertEquals(Die.class, getDie().getClass());
    
    
}
