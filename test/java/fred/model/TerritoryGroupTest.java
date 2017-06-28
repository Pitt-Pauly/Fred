package fred.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TerritoryGroupTest {

	private List<Territory> territories;
	private Territory territory1 = new BasicTerritory("1");
	private Territory territory2 = new BasicTerritory("2");
	private Territory territory3 = new BasicTerritory("3");
	private Player player1 = new BasicPlayer("player1");
	private Player player2 = new BasicPlayer("player2");

	@Before
	public void setUp() {
		territories = new ArrayList<Territory>();
		territories.add(territory1);
		territories.add(territory2);
		territories.add(territory3);
	}
	
    @Test
    public void testConstructor_invalid3Parameters() {
        try {
            new TerritoryGroup(null, territories, 2);
            fail("null should have failed");
        }
        catch (NullPointerException ex) {
            // Expected exception
        }
        
        try {
            new TerritoryGroup("name", (List<Territory>) null, 2);
            fail("null should have failed");
        }
        catch (NullPointerException ex) {
            // Expected exception
        }
        
        try {
            new TerritoryGroup("name", (Territory[]) null, 2);
            fail("null should have failed");
        }
        catch (NullPointerException ex) {
            // Expected exception
        }
        
        try {
            new TerritoryGroup("name", new ArrayList<Territory>(), 2);
            fail("empty should have failed");
        }
        catch (IllegalArgumentException ex) {
            // Expected exception
        }
        
        try {
            new TerritoryGroup("name", new Territory[0], 2);
            fail("empty should have failed");
        }
        catch (IllegalArgumentException ex) {
            // Expected exception
        }
    }

    @Test
    public void testConstructor_invalid2Parameters() {
        try {
            new TerritoryGroup(null, 2);
            fail("null should have failed");
        }
        catch (NullPointerException ex) {
            // Expected exception
        }
    }

    @Test
    public void testConstructor_3Parameters() {
        TerritoryGroup territoryGroup = new TerritoryGroup("Amerika", territories, 2);
        assertSame(territories, territoryGroup.getTerritories());
        assertEquals(2, territoryGroup.getBonusValue());
        assertEquals("Amerika", territoryGroup.getName());
        
        territoryGroup = new TerritoryGroup("Amerika", new Territory[] {territory1}, 2);
        assertEquals(1, territoryGroup.getTerritories().size());
        assertSame(territory1, territoryGroup.getTerritories().get(0));
        assertEquals(2, territoryGroup.getBonusValue());
        assertEquals("Amerika", territoryGroup.getName());
        
        // No real reason why we can't have 0 and negative bonuses for territory groups
        territoryGroup = new TerritoryGroup("Amerika", territories, 0);
        assertEquals(0, territoryGroup.getBonusValue());
        
        territoryGroup = new TerritoryGroup("Amerika", territories, -1);
        assertEquals(-1, territoryGroup.getBonusValue());
    }


    @Test
    public void testConstructor_2Parameters() {
        TerritoryGroup territoryGroup = new TerritoryGroup("Amerika", 2);
        assertEquals(0, territoryGroup.getTerritories().size());
        assertEquals(2, territoryGroup.getBonusValue());
        assertEquals("Amerika", territoryGroup.getName());
    }

	@Test
	public void testOwnership() {
		TerritoryGroup territoryGroup = new TerritoryGroup("Amerika", territories, 2);
		assertFalse(territoryGroup.hasOwner());
		assertNull(territoryGroup.getOwner());
		
		territory1.setOwner(player1);
		territory2.setOwner(player1);
		assertFalse(territoryGroup.hasOwner());
		assertNull(territoryGroup.getOwner());
		
		territory3.setOwner(player1);
		assertTrue(territoryGroup.hasOwner());
		assertSame(player1, territoryGroup.getOwner());
		
		territory2.setOwner(player2);
		assertFalse(territoryGroup.hasOwner());
		assertNull(territoryGroup.getOwner());
		
		territory1.setOwner(player2);
		territory3.setOwner(player2);
		assertTrue(territoryGroup.hasOwner());
		assertSame(player2, territoryGroup.getOwner());
	}

}
