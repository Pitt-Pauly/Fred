package fred.datahandling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Color;

import fred.graphics.TerritoryUI;
import fred.graphics.TerritoryUITest;
import fred.model.Game;
import fred.model.Territory;
import fred.model.BasicTerritory;
import fred.model.TerritoryGroup;
import fred.model.World;

public class MapLoaderTest {

    private MapLoader mapLoader = new MapLoader(){
        @Override
        protected void addTerritoryUI(String name, int posX, int posY, Color RGB) {
            // Override to avoid unit test pain with graphics libraries
        }
    };
	
    private List<Territory> expectedTerritories;
    private List<TerritoryGroup> expectedTerritoryGroups;
    World expectedWorld;

    private List<TerritoryUI> expectedTerritoryUIs;
	
	@Before
	public void setUp() {
        expectedTerritories = new ArrayList<Territory>();
        expectedTerritoryGroups = new ArrayList<TerritoryGroup>();
        expectedWorld = new World(expectedTerritories, expectedTerritoryGroups);
        expectedTerritoryUIs = new ArrayList<TerritoryUI>();
	}

    @Test
    public void testLoadReader_invalid() {
        try {
            mapLoader.load((Reader) null, Game.Type.Basic);
            fail("null pointer exception");
        }
        catch (NullPointerException ex) {
            // Expected exception
        }
    }

    @Test
    public void testLoadReader_initialState() {
        try {
            mapLoader.getTerritoryUIs();
            fail("retriving territoryUIs before loading should have failed");
        }
        catch (IllegalStateException ex) {
            // Expected exception
        }
    }

	@Test
	public void testLoadReader_emptyData() {
		String inputData = "";
		checkLoadReader_emptyData(inputData);

		inputData = "\n\n\n\n#with stuff\n\n\n";
		checkLoadReader_emptyData(inputData);
	}

    private void checkLoadReader_emptyData(String inputData) {
        try {
            mapLoader.load(new StringReader(inputData), Game.Type.Basic);
            fail("expected failure on empty input data");
        }
        catch (IllegalArgumentException ex) {
            assertEquals("Input data contains no territories.", ex.getMessage());
        }
    }

	@Test
	public void testLoadReader_simpleCaseWithComments() {
		String inputData = "#Credits: http://en.wikipedia.org/wiki/Risk_(game)\n"
				+ "\n"
				+ "#[IDEA] get an extension for the map type. (.md ?)\n"
				+ "#Initial Data: Mapname\n"
				+ "Original Risk\n"
				+ "\n"
				+ "#Starting map - territory/node data\n"
				+ "#----------------------------------\n"
				+ "\n"
				+ "G|North America|2\n"
				+ "C|0|Alaska|66|60|DEAD01\n";

		Territory territory = new BasicTerritory("Alaska");
		expectedTerritories.add(territory);
		expectedTerritoryGroups.add(new TerritoryGroup("North America", new Territory[] {territory}, 2));

		World actualWorld = mapLoader.load(new StringReader(inputData), Game.Type.Basic);
		assertEquals(expectedWorld, actualWorld);
        
		// TODO - disabled until we get graphics unit tests working
//		expectedTerritoryUIs.add(new TerritoryUI("Alaska", 66, 60, Color.pink));

//		TerritoryUITest.checkTerritoryUIsEqual(expectedTerritoryUIs, mapLoader.getTerritoryUIs());
	}
	
	@Test
	public void testLoadReader_territoryOnly() {
		String inputData = "Original Risk\n"
				+ "C|0|Alaska|66|60|DEAD01\n";

		Territory territory = new BasicTerritory("Alaska");
        expectedTerritories.add(territory);

        World actualWorld = mapLoader.load(new StringReader(inputData), Game.Type.Basic);
        assertEquals(expectedWorld, actualWorld);
        
        // TODO - disabled until we get graphics unit tests working
//       expectedTerritoryUIs.add(new TerritoryUI("Alaska", 66, 60, Color.pink));

//        TerritoryUITest.checkTerritoryUIsEqual(expectedTerritoryUIs, mapLoader.getTerritoryUIs());
	}

	@Test
	public void testLoadReader_3Territories() {
		String inputData = "Original Risk\n"
				+ "G|North America|2\n"
				+ "C|0|Alaska|66|60|DEAD01|1\n"
				+ "C|1|Alberta|117|95|DEAD02|2\n"
				+ "C|2|Central America|113|207|DEAD03\n";

        Territory alaska = new BasicTerritory("Alaska");
        Territory alberta = new BasicTerritory("Alberta");
        Territory centralAmerica = new BasicTerritory("Central America");
        alaska.addNeighbour(alberta);
        alberta.addNeighbour(centralAmerica);
        
        expectedTerritories.add(alaska);
        expectedTerritories.add(alberta);
        expectedTerritories.add(centralAmerica);
        expectedTerritoryGroups.add(new TerritoryGroup("North America", new Territory[] {alaska, alberta, centralAmerica}, 2));

        World actualWorld = mapLoader.load(new StringReader(inputData), Game.Type.Basic);
        assertEquals(expectedWorld, actualWorld);
        
        // TODO - disabled until we get graphics unit tests working
//        expectedTerritoryUIs.add(new TerritoryUI("Alaska", 66, 60, Color.pink));
//        expectedTerritoryUIs.add(new TerritoryUI("Alberta", 117, 95, Color.pink));
//        expectedTerritoryUIs.add(new TerritoryUI("Central America", 113, 207, Color.pink));
//
//        TerritoryUITest.checkTerritoryUIsEqual(expectedTerritoryUIs, mapLoader.getTerritoryUIs());
	}

	@Test
	public void testLoadReader_outOfOrderTerritories() {
		String inputData = "Original Risk\n"
				+ "\n"
				+ "C|12|Alaska|66|60|DEAD01|10\n"
				+ "C|10|Alberta|117|95|DEAD02\n";

        Territory alaska = new BasicTerritory("Alaska");
        Territory alberta = new BasicTerritory("Alberta");
        alaska.addNeighbour(alberta);
        
        expectedTerritories.add(alberta);
        expectedTerritories.add(alaska);

        World actualWorld = mapLoader.load(new StringReader(inputData), Game.Type.Basic);
        assertEquals(expectedWorld, actualWorld);
        
        // TODO - disabled until we get graphics unit tests working
//        expectedTerritoryUIs.add(new TerritoryUI("Alberta", 117, 95, Color.pink));
//        expectedTerritoryUIs.add(new TerritoryUI("Alaska", 66, 60,Color.pink));
//
//        TerritoryUITest.checkTerritoryUIsEqual(expectedTerritoryUIs, mapLoader.getTerritoryUIs());
	}

	@Test
	public void testLoadReader_InvalidTerritories() {
		String inputData = "C|0|Alaska|66|60|DEAD01|1|2\n"
		    + "C|1|Alberta|117|95||DEAD02|2garbage"
			+ "C|2|Central America|113|207|DEAD03\n";
		checkInvalidTerritories(inputData);

		inputData = "C|0|Alaska|66|60|123456|1\n";
		checkInvalidTerritories(inputData);

        inputData = "C|asd|asd|ase";
        checkInvalidTerritories(inputData);

        inputData = "D|0|Alaska|66|60|123456";
        checkInvalidTerritories(inputData);

        inputData = "G|America";
        checkInvalidTerritories(inputData);

		inputData = "C|1|asd|asd|asd|asd|æsd";
		checkInvalidTerritories(inputData);

	}

	private void checkInvalidTerritories(String badData) {
		String inputData = "Original Risk\n"
				+ badData;

		try {
			mapLoader.load(new StringReader(inputData), Game.Type.Basic);
			fail("invalid data should have failed");
		} catch (IllegalArgumentException ex) {
			assertEquals("Input data contains illegal data.", ex.getMessage());
		}

	}
    
    @Test
    public void testLoad_filePath_invalid() {
        try {
            mapLoader.load((String) null, Game.Type.Basic);
            fail("null pointer exception");
        } catch (NullPointerException ex) {
            // Expected exception
        }
        
        try {
            mapLoader.load("unknown", Game.Type.Basic);
            fail("file not found should have failed");
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }
    }
    
    @Test
    public void testLoad_filePath_valid() throws Exception {
        File tempFile = File.createTempFile("data", ".tmp");
        try {
            Writer writer = new FileWriter(tempFile);
            writer.write("some data");
            writer.close();
            
            MapLoader testLoader = new MapLoader() {
                @Override
                public World load(Reader inputdata, Game.Type gameType) {
                    assertNotNull(inputdata);
                    String data;
                    try {
                        data = new BufferedReader(inputdata).readLine();
                    }
                    catch (IOException e) {
                        throw new RuntimeException("exception", e);
                    }
                    assertEquals("some data", data);
                    return expectedWorld;
                }
            };
            
            assertSame(expectedWorld, testLoader.load(tempFile.getAbsolutePath(), Game.Type.Basic));
        }
        finally {
            tempFile.delete();
        }
        
    }
    
    @Test
    public void testLoad_world() throws Exception {
        World world = mapLoader.load("World.txt", Game.Type.Basic);
        assertNotNull(world);
        assertEquals(42, world.getTerritories().size());
        assertEquals(6, world.getTerritoryGroups().size());

        // TODO - disabled until we get graphics unit tests working
//        assertEquals(42, mapLoader.getTerritoryUIs().size());
    }
}
