package fred.middle;

import java.io.Serializable;

import fred.model.Card;
import fred.model.CardType;
import fred.model.GameDoer;
import fred.model.GameInfo;

public class ExchangeCardsAction extends Action {

	private static final long serialVersionUID = 1L;
	private CardStuff[] sCards;
	
	ExchangeCardsAction(Card[] cards) {
		sCards = new CardStuff[3];
		for(int i = 0; i<3; i++) {
			sCards[i] = new CardStuff(cards[i]);
		}
		
	}
	@Override
	public void perform(GameDoer toGame, GameInfo refGame) {
		Card[] cards = new Card[3];
		for(int i = 0; i<3; i++) {
			cards[i] = sCards[i].getCard(refGame);
		}
		toGame.exchangeCards(cards);
	}
	
	@Override
    public boolean canDo(GameInfo refGame) {
		Card[] cards = new Card[3];
		for(int i = 0; i<3; i++) {
			cards[i] = sCards[i].getCard(refGame);
		}
		return refGame.canExchangeCards(cards);	
	}
	
	class CardStuff implements Serializable {
		private static final long serialVersionUID = 1L;
		private final CardType type;
		private final String territory;
	    protected CardStuff(Card card) {
			this.type = card.getType();
			this.territory = card.getTerritory().getName();
		}
		
	    Card getCard(GameInfo game) {
	    	for(Card card: game.getCurrentPlayer().getCards()) {
	    		if(card.getTerritory().getName().equals(territory)) {
	    			assert(card.getType() == type);
	    			return card;
	    		}
	    	}
	    	throw new IllegalStateException(game.getCurrentPlayer().getName() + " does not have " + territory);
	    }
	}
}
