package fred.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class CardTest {

    @Test
    public void testConstructor() {
        Territory territory = new BasicTerritory("name");
        try {
            new Card(territory, null);
            fail("null should have failed");
        }
        catch (NullPointerException ex) {
            // Expected exception
        }
        try {
            new Card(null, CardType.Cannon);
            fail("null should have failed");
        }
        catch (NullPointerException ex) {
            // Expected exception
        }

        Card card = new Card(territory, CardType.Cannon);
        assertSame(territory, card.getTerritory());
        assertSame(CardType.Cannon, card.getType());
    }
}
