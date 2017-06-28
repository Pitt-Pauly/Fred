package fred.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Color;

import fred.datahandling.MapLoader;

public class WorldTest {

	private Territory territory1 = new BasicTerritory("1");
	private Territory territory2 = new BasicTerritory("2");
	private Territory territory3 = new BasicTerritory("3");
	private Territory territory4 = new BasicTerritory("4");
	private Territory territory5 = new BasicTerritory("5");
	private List<Territory> territories = new ArrayList<Territory>();
	private World world;
	private List<TerritoryGroup> territoryGroups = new ArrayList<TerritoryGroup>();
	private TerritoryGroup territoryGroup1;
	private TerritoryGroup territoryGroup2;
	private Player player = new BasicPlayer("1");
	
	@Before
	public void setUp() {
		territories.add(territory1);
		territories.add(territory2);
		territories.add(territory3);
		territories.add(territory4);
		territories.add(territory5);
		
		List<Territory> territoriesInGroup = new ArrayList<Territory>();
		territoriesInGroup.add(territory1);
		territoriesInGroup.add(territory2);
		territoryGroup1 = new TerritoryGroup("tg1", territoriesInGroup, 2);
		
		territoriesInGroup = new ArrayList<Territory>();
		territoriesInGroup.add(territory3);
		territoryGroup2 = new TerritoryGroup("tg2", territoriesInGroup, 3);
		
		territoryGroups.add(territoryGroup1);
		territoryGroups.add(territoryGroup2);
	}
	
	@Test
	public void testWorld_invalid() {
		try {
			new World(null, territoryGroups);
			fail("null should have failed");
		}
		catch (NullPointerException ex) {
			// Expected exception
		}
		
		try {
			new World(new ArrayList<Territory>(), null);
			fail("null should have failed");
		}
		catch (NullPointerException ex) {
			// Expected exception
		}
	}

	@Test
	public void testGetTerritories() {
		world = new World(territories, territoryGroups);
		assertSame(territories, world.getTerritories());
	}

	@Test
	public void testGetTerritoryGroups() {
		world = new World(territories, territoryGroups);
		assertSame(territoryGroups, world.getTerritoryGroups());
	}
	
	@Test
	public void testHasPathway_invalid() {
		world = new World(territories, territoryGroups);
		territory1.setOwner(player);
		territory2.setOwner(new BasicPlayer("other player"));
		
		try {
			world.hasPathway(null, territory2);
			fail("null should have failed");
		}
		catch (NullPointerException ex) {
			// Expected exception
		}
		
		try {
			world.hasPathway(territory1, null);
			fail("null should have failed");
		}
		catch (NullPointerException ex) {
			// Expected exception
		}
		
		try {
			world.hasPathway(territory1, territory1);
			fail("same should have failed");
		}
		catch (IllegalArgumentException ex) {
			// Expected exception
		}
		
		try {
			world.hasPathway(territory1, territory2);
			fail("different owners should have failed");
		}
		catch (IllegalArgumentException ex) {
			// Expected exception
		}
	}
	
	@Test
	public void testHasPathway() {
		world = new World(territories, territoryGroups);
		territory1.setOwner(player);
		territory2.setOwner(player);
		territory3.setOwner(player);
		territory4.setOwner(player);
		territory5.setOwner(player);
		
		territory1.addNeighbour(territory2);
		territory2.addNeighbour(territory3);
		territory3.addNeighbour(territory4);
		
		assertTrue(world.hasPathway(territory1, territory4));
		assertTrue(world.hasPathway(territory4, territory1));
		assertFalse(world.hasPathway(territory1, territory5));
		
		territory4.addNeighbour(territory5);
		assertTrue(world.hasPathway(territory2, territory5));
		
		// Path blocked by unowned territory
		territory3.setOwner(new BasicPlayer("other player"));
		assertFalse(world.hasPathway(territory1, territory5));
		
		// Add path around
		territory2.addNeighbour(territory4);
		assertTrue(world.hasPathway(territory1, territory5));
	}
	
	@Test
	public void testGetPathwayCost_invalid() {
		world = new World(territories, territoryGroups);
		territory1.setOwner(player);
		territory2.setOwner(new BasicPlayer("other player"));
		
		try {
			world.getPathwayCost(null, territory2);
			fail("null should have failed");
		}
		catch (NullPointerException ex) {
			// Expected exception
		}
		
		try {
			world.getPathwayCost(territory1, null);
			fail("null should have failed");
		}
		catch (NullPointerException ex) {
			// Expected exception
		}
		
		try {
			world.getPathwayCost(territory1, territory1);
			fail("same should have failed");
		}
		catch (IllegalArgumentException ex) {
			// Expected exception
		}
		
		try {
			world.getPathwayCost(territory1, territory2);
			fail("different owners should have failed");
		}
		catch (IllegalArgumentException ex) {
			// Expected exception
		}
	}
	
    @Test
    public void testGetPathwayCost() {
        world = new World(territories, territoryGroups);
        territory1.setOwner(player);
        territory2.setOwner(player);
        territory3.setOwner(player);
        territory4.setOwner(player);
        territory5.setOwner(player);
        
        territory1.addNeighbour(territory2);
        territory2.addNeighbour(territory3);
        territory3.addNeighbour(territory4);
        
        assertEquals(3, world.getPathwayCost(territory1, territory4));
        assertEquals(3, world.getPathwayCost(territory4, territory1));
        assertEquals(0, world.getPathwayCost(territory1, territory5));
        
        territory4.addNeighbour(territory5);
        assertEquals(3, world.getPathwayCost(territory2, territory5));
        
        // Path blocked by unowned territory
        territory3.setOwner(new BasicPlayer("other player"));
        assertEquals(0, world.getPathwayCost(territory1, territory5));
        
        // Add path around
        territory2.addNeighbour(territory4);
        assertEquals(3, world.getPathwayCost(territory1, territory5));
    }
    
    @Test
    public void testGetPathwayCost_worldMap() {
        world = new TestMapLoader().load("World.txt", Game.Type.Basic);
        Territory alberta = world.getTerritoryByName("Alberta");
        Territory brazil = world.getTerritoryByName("Brazil");
        
        alberta.setOwner(player);
        brazil.setOwner(player);
        
        assertEquals(0, world.getPathwayCost(alberta, brazil));
        
        world.getTerritoryByName("Venezuela").setOwner(player);
        world.getTerritoryByName("Central America").setOwner(player);
        world.getTerritoryByName("Eastern United States").setOwner(player);
        world.getTerritoryByName("Ontario").setOwner(player);
        
        assertEquals(5, world.getPathwayCost(alberta, brazil));
        
        world.getTerritoryByName("Western United States").setOwner(player);
        
        assertEquals(4, world.getPathwayCost(alberta, brazil));
        
        for(Territory territory: world.getTerritories()) {
        	territory.setOwner(player);
        }
        
        assertEquals(4, world.getPathwayCost(alberta, brazil));
        
    }
    
	@Test
	public void testGetFullyOwnedTerritoryGroups_invalid() {
		world = new World(territories, territoryGroups);
		try {
			world.getFullyOwnedTerritoryGroups(null);
			fail("null should have failed");
		}
		catch (NullPointerException ex) {
			// Expected exception
		}
	}

	@Test
	public void testGetFullyOwnedTerritoryGroups() {
		world = new World(territories, territoryGroups);
		List<TerritoryGroup> result = world.getFullyOwnedTerritoryGroups(player);
		assertNotNull(result);
		assertTrue(result.isEmpty());
		
		territory1.setOwner(player);
		assertTrue(world.getFullyOwnedTerritoryGroups(player).isEmpty());

		territory2.setOwner(player);
		result = world.getFullyOwnedTerritoryGroups(player);
		assertEquals(1, result.size());
		assertTrue(result.contains(territoryGroup1));

		territory3.setOwner(player);
		result = world.getFullyOwnedTerritoryGroups(player);
		assertEquals(2, result.size());
		assertTrue(result.contains(territoryGroup1));
		assertTrue(result.contains(territoryGroup2));
		
		territory1.setOwner(new BasicPlayer("other"));
		result = world.getFullyOwnedTerritoryGroups(player);
		assertEquals(1, result.size());
		assertTrue(result.contains(territoryGroup2));
	}
	
	class TestMapLoader extends MapLoader {
	    @Override
	    protected void addTerritoryUI(String name, int posX, int posY, Color RGB) {
	        // TODO - disabled until we get graphics unit tests working
	    }
	}
}
