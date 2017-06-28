package fred.graphics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.Before;
import org.newdawn.slick.Color;

import fred.model.Player;
import fred.model.BasicPlayer;
import fred.model.Territory;
import fred.model.BasicTerritory;

public class TerritoryUITest {

    private static final float FLOAT_DELTA = 0.01f;
    
    private Territory home;
    private FakeGameInfo fakeGame;
    
    @Before
    public void setup() {
    	home = new BasicTerritory("home");
    	List<Territory> territories = new ArrayList<Territory>();
    	territories.add(home);
    	fakeGame = new FakeGameInfo(territories);
    }
    

    // TODO - disabled until we get graphics unit tests working
//    @Test
    public void initTest() {
        try {
            new TerritoryUI(null, 0, 0, Color.pink);
            fail("should have failed");
        }
        catch (NullPointerException ex) {
            // expected exception
        }
    }

    // TODO - disabled until we get graphics unit tests working
//    @Test
    public void testGetSetProperties() {
        Player owner = new BasicPlayer("me");

        home.setUnitCount(5);
        home.setOwner(owner);

        TerritoryUI node = new TerritoryUI("home", 5, 15, Color.pink);
        node.setGame(fakeGame);
        
        assertEquals("home", node.getTerritoryName());
        assertEquals(5, node.getUnitCount());
        assertEquals(5, node.getX(), FLOAT_DELTA);
        assertEquals(15, node.getY(), FLOAT_DELTA);

        node.setX(-10);
        node.setY(-50);
        assertEquals(-10, node.getX(), FLOAT_DELTA);
        assertEquals(-50, node.getY(), FLOAT_DELTA);
        
        node.setSelectedAsSource();
        assertTrue(node.isSelectedAsSource());
        
        node.setSelectedAsTarget();
        assertTrue(node.isSelectedAsTarget());
        assertFalse(node.isSelectedAsSource());
        
        node.setSelectedAsSource();
        assertTrue(node.isSelectedAsSource());
        assertFalse(node.isSelectedAsTarget());
        
        node.setUnselected();
        assertFalse(node.isSelectedAsSource());
        assertFalse(node.isSelectedAsTarget());
        
        assertFalse(node.isHoveredOver());
        node.setHoveredOver(true);
        assertTrue(node.isHoveredOver());
        
    }

    // TODO - disabled until we get graphics unit tests working
//    @Test
    public void inBoundariesTest(){
    	Player owner = new BasicPlayer("me");

        home.setUnitCount(5);
        home.setOwner(owner);

        TerritoryUI node = new TerritoryUI("home", 5, 15, Color.pink);
        
        int radius = TerritoryUI.CIRCLE_RADIUS;
        assertTrue(node.inBoundaries(5+radius, 15+radius));
        assertFalse(node.inBoundaries(100, 100));
    }
    
    public static void checkTerritoryUIsEqual(List<TerritoryUI> expectedTerritoryUIs, List<TerritoryUI> actualTerritoryUIs) {
        assertNotNull(actualTerritoryUIs);

        assertEquals(expectedTerritoryUIs.size(), actualTerritoryUIs.size());

        for (TerritoryUI expectedTerritoryUI : expectedTerritoryUIs) {
            boolean found = false;
            for (TerritoryUI actualTerritoryUI : actualTerritoryUIs) {
                if (expectedTerritoryUI.getTerritoryName().equals(actualTerritoryUI.getTerritoryName())) {
                    checkTerritoryUIsEqual(expectedTerritoryUI, actualTerritoryUI);
                    found = true;
                    break;
                }
            }
            assertTrue("territoryUI not found", found);
        }
    }

    public static void checkTerritoryUIsEqual(TerritoryUI expectedTerritoryUI, TerritoryUI actualTerritoryUI) {
        assertEquals(expectedTerritoryUI.getTerritoryName(), actualTerritoryUI.getTerritoryName());
        assertEquals(expectedTerritoryUI.getX(), actualTerritoryUI.getX(), FLOAT_DELTA);
        assertEquals(expectedTerritoryUI.getY(), actualTerritoryUI.getY(), FLOAT_DELTA);
    }

}