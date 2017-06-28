package fred.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class DieTest {

	@Test
	public void testRoll() {
		Die die = new Die();
		int[] results = new int[6];
		
		for (int i=0; i<100; i++) {
			int roll = die.roll();
			assertTrue("Invalid roll", roll >= 1 && roll <= 6);
			results[roll-1] = results[roll-1] + 1;
		}
		for (int result : results) {
			assertTrue("missed a value", result > 0);
		}
	}

}
