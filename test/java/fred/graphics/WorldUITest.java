
package fred.graphics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import fred.model.BasicPlayer;
import fred.model.BasicTerritory;
import fred.model.Player;
import fred.model.Territory;

public class WorldUITest {

    private final GUIContext context = EasyMock.createMock(GUIContext.class);

    private WorldUI worldUI;
    
    private TerritoryUI n1;
	private TerritoryUI n2;
	private TerritoryUI n3;
	
	private Player owner;
	private Player owned;
    
    private int radius;
    
    @Before
    public void setUp() throws Exception {
        EasyMock.expect(context.getInput()).andReturn(new Input(5));
        EasyMock.replay(context);
        
        List<TerritoryUI> nList = new ArrayList<TerritoryUI>();
        
        Territory territoryTest1 = new BasicTerritory("Test1");
        Territory territoryTest2 = new BasicTerritory("Test2");
        Territory territoryTest3 = new BasicTerritory("Test3");
        List<Territory> territories = new ArrayList<Territory>();
        territories.add(territoryTest1);
        territories.add(territoryTest2);
        territories.add(territoryTest3);
        
        FakeGameInfo fakeGame = new FakeGameInfo(territories);
        
        
        n1 = new TerritoryUI("Test1",0,0,Color.pink);
        n2 = new TerritoryUI("Test2",300,300,Color.pink);
        n3 = new TerritoryUI("Test3",700,700, Color.pink);
        n1.setGame(fakeGame);
        n2.setGame(fakeGame);
        n3.setGame(fakeGame);
        
        radius = TerritoryUI.CIRCLE_RADIUS;
        
        nList.add(n1);
        nList.add(n2);
        nList.add(n3);
        
        owner = new BasicPlayer("me");
        owned = new BasicPlayer("me");
        territoryTest1.setOwner(owner);
        territoryTest2.setOwner(owner);
        territoryTest3.setOwner(owner);
        
        worldUI = new TestWorldUI(context, nList);
    }
    
    
    // TODO - disabled until we get graphics unit tests working
//    @Test
    public void testConstructor_invalid() throws Exception {
        try {
            new WorldUI(null, new ArrayList<TerritoryUI>());
            fail("should have failed");
        }
        catch (NullPointerException ex) {
            // expected
        }

        EasyMock.reset(context);
        EasyMock.expect(context.getInput()).andReturn(new Input(5));
        EasyMock.replay(context);
        try {
            new WorldUI(context, null);
            fail("should have failed");
        }
        catch (NullPointerException ex) {
            // expected
        }
    }

    // TODO - disabled until we get graphics unit tests working
//    @Test
    public void testSelectSourceOn(){
    	 //should select nothing
        assertNull(worldUI.selectSourceOn( 999,999, owner, false));
        assertEquals(0, worldUI.getSelectedSources().size());
        assertTrue(worldUI.getSelectedSources().isEmpty());


        //should select n1
        assertEquals(n1, worldUI.selectSourceOn(n1.getX()+radius, n1.getY()+radius, owner, false)); 
        assertEquals(n1,worldUI.getSelectedSources().get(0));
        assertFalse(worldUI.getSelectedSources().isEmpty());

        //should select n2 as well as n1
        assertEquals(n2, worldUI.selectSourceOn(n2.getX()+radius, n2.getY()+radius, owner, false));
        assertEquals(n1,worldUI.getSelectedSources().get(0));
        assertEquals(n2,worldUI.getSelectedSources().get(1));
        assertFalse(worldUI.getSelectedSources().isEmpty());

        // TODO - test deselection
    }
      
    // TODO - disabled until we get graphics unit tests working
//    @Test
    public void testSelectTargetOn(){
    	//should select nothing
    	assertFalse(worldUI.selectTargetOn(999,999, owned, false, false)); 
        assertEquals(null,worldUI.getSelectedTarget());
        
        //should select nothing (no source selected)
        assertFalse(worldUI.selectTargetOn(n2.getX()+radius, n2.getY()+radius, owned, false, false));
        assertEquals(null,worldUI.getSelectedTarget());
        
        //should select n2
        assertEquals(n2, worldUI.selectSourceOn(n1.getX()+radius, n1.getY()+radius, owner, false));
        assertTrue(worldUI.selectTargetOn( n2.getX()+radius, n2.getY()+radius, owned, false, false)); 
        assertEquals(n2,worldUI.getSelectedTarget());
    }
    
    // TODO - disabled until we get graphics unit tests working
//    @Test
    public void testPlayerSpecificity() {
    	assertSame(n1.getTerritory().getOwner(), owner);
    	 //should select nothing
    	assertNull(worldUI.selectSourceOn(n1.getX(),n1.getY(), owned, false));
        assertTrue(worldUI.getSelectedSources().isEmpty());

        //should select n1
        assertEquals(n1, worldUI.selectSourceOn(n1.getX(), n1.getY(), owner, false)); 
        assertEquals(n1,worldUI.getSelectedSources().get(0));
        
      //should select nothing
        assertFalse(worldUI.selectTargetOn(n1.getX(),n1.getY(), owner, false, true));
        assertEquals(null,worldUI.getSelectedTarget());

        //should select n1
        assertTrue(worldUI.selectTargetOn(n1.getX(), n1.getY(), owned, false, true)); 
        assertEquals(n1,worldUI.getSelectedTarget());
        
        worldUI.deselectAllNodes();
        worldUI.selectSourceOn(n1.getX(), n1.getY(), owner, false);
        
      //should select nothing
        assertFalse(worldUI.selectTargetOn(n1.getX(),n1.getY(), owned, true, false));
        assertEquals(null,worldUI.getSelectedTarget());

        //should select n1
        assertTrue(worldUI.selectTargetOn(n1.getX(), n1.getY(), owner, true, false)); 
        assertEquals(n1,worldUI.getSelectedTarget());

        //multiselect test
        worldUI.deselectAllNodes();
        worldUI.selectSourceOn(n1.getX(), n1.getY(), owner, true);
        worldUI.selectSourceOn(n2.getX(), n2.getY(), owner, true);

      //should select nothing
        assertFalse(worldUI.selectTargetOn(n1.getX(),n1.getY(), owned, true, false));
        assertEquals(null,worldUI.getSelectedTarget());

        //should select n1
        assertTrue(worldUI.selectTargetOn(n1.getX(), n1.getY(), owner, true, false));
        assertEquals(n1,worldUI.getSelectedTarget());

        // TODO test deselection ?
    }

    // TODO - disabled until we get graphics unit tests working
//    @Test
    public void testDeselectAll(){
        worldUI.deselectAllNodes();
        assertTrue(worldUI.getSelectedSources().isEmpty());
        assertEquals(null, worldUI.getSelectedTarget());
    }
    
    public static void checkWorldUIsEqual(WorldUI expectedWorldUI, WorldUI actualWorldUI) {
        assertNotNull(actualWorldUI);

        List<TerritoryUI> actualTerritoryUIs = actualWorldUI.getNodes();
        List<TerritoryUI> expectedTerritoryUIs = expectedWorldUI.getNodes();
        assertEquals(expectedTerritoryUIs.size(), actualTerritoryUIs.size());

        for (TerritoryUI expectedTerritoryUI : expectedTerritoryUIs) {
            boolean found = false;
            for (TerritoryUI actualTerritoryUI : actualTerritoryUIs) {
                if (expectedTerritoryUI.getTerritoryName().equals(actualTerritoryUI.getTerritoryName())) {
                    TerritoryUITest.checkTerritoryUIsEqual(expectedTerritoryUI, actualTerritoryUI);
                    found = true;
                    break;
                }
            }
            assertTrue("territoryUI not found", found);
        }
    }

    // Test implementation to avoid all the slick machinery
    private static class TestWorldUI extends WorldUI {
        
        public TestWorldUI(GUIContext container, List<TerritoryUI> nodes) throws SlickException {
            super(container, nodes);
        }
    }
    
}