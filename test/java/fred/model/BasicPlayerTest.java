package fred.model;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Color;

import fred.model.Player.PlayerType;

public class BasicPlayerTest {

    private static final int TOTAL_CARDS = 20;
    private List<Territory> territories = new ArrayList<Territory>();

    @Before
    public void setUp() {
        for (int index = 0; index < TOTAL_CARDS; index++) {
            territories.add(new BasicTerritory("" + index));
        }
    }

    @Test
    public void testConstructor() {
        try {
            new BasicPlayer(null, Color.orange, PlayerType.Human);
            fail("null should have failed");
        }
        catch (NullPointerException ex) {
            // Expected exception
        }

        try {
            new BasicPlayer("name", null, PlayerType.Human);
            fail("null should have failed");
        }
        catch (NullPointerException ex) {
            // Expected exception
        }
        
        try {
            new BasicPlayer("name", Color.orange, null);
            fail("null should have failed");
        }
        catch (NullPointerException ex) {
            // Expected exception
        }
        
        Player player = new BasicPlayer("name", Color.orange, PlayerType.Human);
        assertEquals("name", player.getName());
        assertEquals(Color.orange, player.getColor());
        assertEquals(PlayerType.Human, player.getType());
        assertEquals(0, player.getFreeUnitCount());
        
        try {
            new BasicPlayer(null);
            fail("null should have failed");
        }
        catch (NullPointerException ex) {
            // Expected exception
        }

        player = new BasicPlayer("name");
        assertEquals("name", player.getName());
        assertNotNull(player.getColor());
        assertNotNull(player.getType());
        assertEquals(0, player.getFreeUnitCount());
    }

    @Test
    public void testGetSetProperties() {
        Player player = new BasicPlayer("name");

        assertEquals(0, player.getFreeUnitCount());

        try {
            player.setFreeUnitCount(-1);
            fail("invalid should have failed");
        }
        catch (IllegalArgumentException ex) {
            // Expected exception
        }

        try {
            player.setRemainingMoveCount(-1);
            fail("invalid should have failed");
        }
        catch (IllegalArgumentException ex) {
            // Expected exception
        }

        player.setFreeUnitCount(5);
        assertEquals(5, player.getFreeUnitCount());

        player.setFreeUnitCount(0);
        assertEquals(0, player.getFreeUnitCount());

        player.setRemainingMoveCount(5);
        assertEquals(5, player.getRemainingMoveCount());

        player.setRemainingMoveCount(0);
        assertEquals(0, player.getRemainingMoveCount());
    }

    @Test
    public void testAddCardHandCard() {

        Player player = new BasicPlayer("name");

        CardDeck cardDeck = new CardDeck(territories);

        try {
            player.addCardHandCard(null);
            fail("null should have failed");
        }
        catch (NullPointerException ex) {
            // Expected exception
        }

        Card card1 = cardDeck.getDealtCard();
        Card card2 = cardDeck.getDealtCard();
        Card card3 = cardDeck.getDealtCard();
        Card card4 = cardDeck.getDealtCard();
        Card card5 = cardDeck.getDealtCard();
        Card card6 = cardDeck.getDealtCard();
        
        List<Card> expected = new ArrayList<Card>();

        assertEquals(0, player.getCardHandCount());
        assertEquals(expected, player.getCards());

        player.addCardHandCard(card1);
        
        expected.add(card1);
        assertEquals(1, player.getCardHandCount());
        assertEquals(expected, player.getCards());
        
        // Can only return card once
        try {
            player.addCardHandCard(card1);
            fail("invalid card should have failed");
        }
        catch (IllegalStateException ex) {
            // Expected exception
        }

        player.addCardHandCard(card2);
        player.addCardHandCard(card3);
        player.addCardHandCard(card4);
        player.addCardHandCard(card5);

        expected.add(card2);
        expected.add(card3);
        expected.add(card4);
        expected.add(card5);
        assertEquals(5, player.getCardHandCount());
        assertEquals(expected, player.getCards());

        // Can only have 5 cards
        try {
            player.addCardHandCard(card6);
            fail("sixth card should have failed");
        }
        catch (IllegalStateException ex) {
            // Expected exception
        }
    }
    
    @Test
    public void testGetCardHandCard () {
    	
    	Player player = new BasicPlayer("name");
    	
    	Territory territory1 = new BasicTerritory("1");
        Territory territory2 = new BasicTerritory("2");
        Territory territory3 = new BasicTerritory("3");
        Territory territory4 = new BasicTerritory("4");
        
        Card card1 = new Card(territory1, CardType.Cannon);
        Card card2 = new Card(territory2, CardType.Cavalry);
        Card card3 = new Card(territory3, CardType.Infantry);
        Card card4 = new Card(territory4, CardType.Infantry);
        
        try {
            player.getCardHandCard(null);
            fail("null should have failed");
        }
        catch (NullPointerException ex) {
            // Expected exception
        }
        
        player.addCardHandCard(card1);
        player.addCardHandCard(card2);
        player.addCardHandCard(card3);
        
        try {
            player.getCardHandCard(card4);
            fail("cannot deal card player does not have");
        }
        catch (IllegalArgumentException ex) {
            // Expected exception
        }
              
        assertEquals(3, player.getCardHandCount());
        
        player.getCardHandCard(card1);
        
        assertEquals(2, player.getCardHandCount());
        assertTrue(player.getCards().contains(card2));
        assertTrue(player.getCards().contains(card3));
        
    }
}