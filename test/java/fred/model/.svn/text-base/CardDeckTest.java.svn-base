package fred.model;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CardDeckTest {

    private static final int TOTAL_CARDS = 22;
    private List<Territory> territories = new ArrayList<Territory>();
    
    @Before
    public void setUp() {
        for (int index = 0; index < TOTAL_CARDS - 2; index++) {
            territories.add(new BasicTerritory("" + index));
        }
    }
    
    @Test
    public void testConstructor() {
        try {
            new CardDeck(null);
            fail("null should have failed");
        }
        catch (NullPointerException ex) {
            // Expected exception
        }

        try {
            new CardDeck(new ArrayList<Territory>());
            fail("empty list should have failed");
        }
        catch (IllegalArgumentException ex) {
            // Expected exception
        }
        
        CardDeck cardDeck = new CardDeck(territories);
        assertEquals(TOTAL_CARDS, cardDeck.getRemainingCardCount());
        assertEquals(0, cardDeck.getDiscardCardCount());
        
        int cannons = 0;
        int cavalry = 0;
        int infantry = 0;
        int joker = 0;
        
        for (Card card : cardDeck.cards) {
            if (card.getType() == CardType.Cannon) {
                cannons++;
            }
            else if (card.getType() == CardType.Cavalry) {
                cavalry++;
            }
            else if (card.getType() == CardType.Infantry) {
                infantry++;
            }
            else if (card.getType() == CardType.Joker) {
                joker++;
            }
        }
        assertEquals(7, cavalry);
        assertEquals(7, infantry);
        assertEquals(6, cannons);
        assertEquals(2, joker);

        CardDeck otherDeck = new CardDeck(territories);
        boolean sameSequence = true;
        for (int index = 0; index < cardDeck.cards.size(); index++) {
            if (!cardDeck.cards.get(index).getTerritory().getName().equals(
                    otherDeck.cards.get(index).getTerritory().getName())) {
                sameSequence = false;
            }
        }
        assertFalse(sameSequence);
    }

    @Test
    public void testGetDealtCard() {
        CardDeck cardDeck = new CardDeck(territories);
        List<Card> dealtCards = new ArrayList<Card>();
        
        for (int index = 0; index < territories.size() + 2; index++) {
            Card dealtCard = cardDeck.getDealtCard();
            assertEquals(TOTAL_CARDS - index - 1, cardDeck.getRemainingCardCount());
            assertFalse(dealtCards.contains(dealtCard));
            dealtCards.add(dealtCard);
        }
    }

    @Test
    public void testAddDiscardCard() {
        CardDeck cardDeck = new CardDeck(territories);
        try {
            cardDeck.addDiscardCard(null);
            fail("null should have failed");
        }
        catch (NullPointerException ex) {
            // Expected exception
        }
        
        Card card1 = cardDeck.getDealtCard();
        Card card2 = cardDeck.getDealtCard();
        assertEquals(TOTAL_CARDS - 2, cardDeck.getRemainingCardCount());
        assertEquals(0, cardDeck.getDiscardCardCount());
        
        cardDeck.addDiscardCard(card1);
        cardDeck.addDiscardCard(card2);
        assertEquals(TOTAL_CARDS - 2, cardDeck.getRemainingCardCount());
        assertEquals(2, cardDeck.getDiscardCardCount());

        // Can only return card once
        try {
            cardDeck.addDiscardCard(card1);
            fail("invalid card should have failed");
        }
        catch (IllegalStateException ex) {
            // Expected exception
        }
    }

    @Test
    public void testGetDealtCard_SwitchingDecks() {
        CardDeck cardDeck = new CardDeck(territories);
        int dealtCardCount = 0;
        for (dealtCardCount = 0; dealtCardCount < TOTAL_CARDS - 10; dealtCardCount++){
            cardDeck.getDealtCard();
        }
        Card[] cards = new Card[10];
        for (int index = 0; index < 10; index++) {    
            cards[index] = cardDeck.getDealtCard();
            cardDeck.addDiscardCard(cards[index]);
        }
        assertEquals(10, cardDeck.getDiscardCardCount());
        assertEquals(0, cardDeck.getRemainingCardCount());
        boolean sameSequence = true;
        for (int index = 0; index < 10; index++) {
            Card card = cardDeck.getDealtCard();
            assertEquals(9 - index, cardDeck.getRemainingCardCount());
            assertEquals(0, cardDeck.getDiscardCardCount());
            if (card != cards[index]) {
                sameSequence = false;
            }
        }
        assertFalse("cards not shuffled", sameSequence);
    }
    
}
