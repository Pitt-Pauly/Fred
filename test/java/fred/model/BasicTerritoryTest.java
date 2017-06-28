package fred.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class BasicTerritoryTest {

    @Test
    public void testConstructor() {
        try {
            new BasicTerritory(null);
            fail("null should have failed");
        }
        catch (NullPointerException ex) {
            // Expected exception
        }

        try {
            new BasicTerritory("");
            fail("empty name should have failed");
        }
        catch (IllegalArgumentException ex) {
            // Expected exception
        }

        Territory territory = new BasicTerritory("testname");
        assertEquals("testname", territory.getName());
        assertNull(territory.getOwner());
        assertEquals(0, territory.getUnitCount());
        assertNotNull(territory.getNeighbours());
        assertEquals(0, territory.getNeighbours().size());
    }

    @Test
    public void testGetSetProperties_invalid() {
        Territory territory = new BasicTerritory("name");
        try {
            territory.setOwner(null);
            fail("null should have failed");
        }
        catch (NullPointerException ex) {
            // Expected exception
        }
        try {
            territory.setUnitCount(0);
            fail("invalid should have failed");
        }
        catch (IllegalArgumentException ex) {
            // Expected exception
        }
        try {
            territory.setUnitCount(-1);
            fail("invalid should have failed");
        }
        catch (IllegalArgumentException ex) {
            // Expected exception
        }
    }

	@Test
	public void testGetSetProperties() {
		Territory territory = new BasicTerritory("name");
		assertEquals(null, territory.getOwner());
		assertEquals(0, territory.getUnitCount());
		
		Player player = new BasicPlayer("player1");
		territory.setOwner(player);
		territory.setUnitCount(5);
		assertEquals(player, territory.getOwner());
		assertEquals(5, territory.getUnitCount());

		territory.setUnitCount(1);
		assertEquals(1, territory.getUnitCount());
	}
	
	@Test
	public void testGetAddNeighbours_invalid() {
		Territory territory = new BasicTerritory("name");
		assertNotNull(territory.getNeighbours());
		
		try {
			territory.addNeighbour(null);
			fail("null should have failed");
		}
		catch (NullPointerException ex) {
			// Expected exception
		}
		
		try {
			territory.addNeighbour(territory);
			fail("self reference should have failed");
		}
		catch (IllegalArgumentException ex) {
			// Expected exception
		}
	}

	@Test
	public void testGetAddNeighbours() {
		Territory territory1 = new BasicTerritory("name1");
		Territory territory2 = new BasicTerritory("name2");
		Territory territory3 = new BasicTerritory("name3");
		assertNotNull(territory1.getNeighbours());
		assertEquals(0, territory1.getNeighbours().size());
		
		territory1.addNeighbour(territory2);
		territory1.addNeighbour(territory3);
		assertEquals(2, territory1.getNeighbours().size());
		assertTrue(territory1.getNeighbours().contains(territory2));
		assertTrue(territory1.getNeighbours().contains(territory3));
		
		// Implicit bidirectional mapping created
        assertEquals(1, territory2.getNeighbours().size());
        assertTrue(territory2.getNeighbours().contains(territory1));
        assertEquals(1, territory3.getNeighbours().size());
        assertTrue(territory3.getNeighbours().contains(territory1));
		
		// Repeated addition ignored
		territory1.addNeighbour(territory2);
		assertEquals(2, territory1.getNeighbours().size());
	}
	

	@Test
	public void testIsConnected_invalid() {
		Territory territory = new BasicTerritory("name");

		try {
			territory.isConnected(null);
			fail("null should have failed");
		}
		catch (NullPointerException ex) {
			// Expected exception
		}
	}

	@Test
	public void testIsConnected() {
		Territory territory1 = new BasicTerritory("name1");
		Territory territory2 = new BasicTerritory("name2");
		Territory territory3 = new BasicTerritory("name3");
		territory1.addNeighbour(territory2);
		territory1.addNeighbour(territory3);

		assertTrue(territory1.isConnected(territory2));
		assertTrue(territory3.isConnected(territory1));
		assertFalse(territory3.isConnected(territory2));
		
		// Implicit bidirectional mapping
		assertTrue(territory2.isConnected(territory1));
		
		assertFalse(territory1.isConnected(new BasicTerritory("name4")));
	}
}
