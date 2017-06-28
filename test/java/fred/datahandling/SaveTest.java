package fred.datahandling;

import org.junit.Test;
import static org.junit.Assert.*;

import fred.middle.ReplayTest;

public class SaveTest {
	public static void main(String[] args) {
		ReplayTest test = new ReplayTest();
		SaveLoader saver = new SaveLoader();
		test.systemTest();
		saver.save("save.sav", test.wrapper.getReplay());
	}
	
	@Test
	public void testGetValidFilename() {
	    try {
	        SaveLoader.getValidFilename(null);
	        fail("null should have failed");
	    }
	    catch (NullPointerException ex) {
	        // Expected exception
	    }
	    
        assertEquals(null, SaveLoader.getValidFilename(""));
        assertEquals("abc", SaveLoader.getValidFilename("abc"));
        assertEquals("abc", SaveLoader.getValidFilename("  a  \t\n\r bc  "));
        assertEquals("abc", SaveLoader.getValidFilename("./\\~: a  \t\n\r bc  "));
	}
}
