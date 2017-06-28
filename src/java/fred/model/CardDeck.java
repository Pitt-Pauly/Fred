package fred.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardDeck implements Serializable {

    private static final long serialVersionUID = 1L;
    
    final List<Card> cards = new ArrayList<Card>();
    final List<Card> discards = new ArrayList<Card>();
    
    public CardDeck(List<Territory> territories) {
        if (territories == null) {
            throw new NullPointerException();
        }
        if (territories.isEmpty()) {
            throw new IllegalArgumentException();
        }
        for (int index = 0; index < territories.size(); index++) {
            switch(index%3) {
                case(0):
                    cards.add(new Card(territories.get(index), CardType.Infantry));
                break; //end Case(1)
                case(1):
                    cards.add(new Card(territories.get(index), CardType.Cavalry));
                break; //end Case(2)
                case(2):
                    cards.add(new Card(territories.get(index), CardType.Cannon));
                break; //end Case(3)
            } // end switch(index%3)
        }
        Territory territoryJoker1 = new BasicTerritory("Joker1"); // This should work even for advanced games.
        Territory territoryJoker2 = new BasicTerritory("Joker2");
        cards.add(new Card(territoryJoker1, CardType.Joker));
        cards.add(new Card(territoryJoker2, CardType.Joker));

        Collections.shuffle(cards);
        
    }
    
    public Card getDealtCard() {
        if (cards.isEmpty()) {
        	if(discards.isEmpty()) {
        		return null;
        	}
            cards.addAll(discards);
            Collections.shuffle(cards);
            discards.clear();
        }
        return cards.remove(0);
    }
    
    //TODO:test
    protected Card fixedDeal(String territoryName) {
        if (cards.isEmpty()) {
        	if(discards.isEmpty()) {
        		return null;
        	}
            cards.addAll(discards);
            Collections.shuffle(cards);
            discards.clear();
        }
        for(Card card: cards) {
        	if(card.getTerritory().getName().equals(territoryName)) {
        		cards.remove(card);
        		return card;
        	}
        }
       throw new IllegalArgumentException(territoryName + " not in deck");
    }
    
    public void addDiscardCard(Card discard) {
        if (discard == null) {
            throw new NullPointerException();
        }
        if (discards.contains(discard)) {
            throw new IllegalStateException();
        }
        discards.add(discard);
    }
    
    public int getRemainingCardCount() {
        return cards.size();
    }
    
    public int getDiscardCardCount() {
        return discards.size();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cards == null) ? 0 : cards.hashCode());
        result = prime * result + ((discards == null) ? 0 : discards.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CardDeck other = (CardDeck) obj;
        if (cards == null) {
            if (other.cards != null)
                return false;
        }
        else if (!cards.equals(other.cards))
            return false;
        if (discards == null) {
            if (other.discards != null)
                return false;
        }
        else if (!discards.equals(other.discards))
            return false;
        return true;
    }
}
