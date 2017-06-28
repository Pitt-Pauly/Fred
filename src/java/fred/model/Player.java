package fred.model;

import org.newdawn.slick.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

abstract public class Player implements Serializable {

    public static final int MAX_CARDS_IN_HAND = 5;
    private static final long serialVersionUID = 1L;

    public enum PlayerType {Human, AI}
    
    private final String name;
    private final Color color;
    private final PlayerType type;
    private final List<Card> cardHand = new ArrayList<Card>();
    private int freeUnitCount;
    private int remainingMoveCount;
    private int remainingDeployCount;

    public Player(String name, Color color, PlayerType playerType) {
        if (name == null || color == null || playerType == null) {
            throw new NullPointerException();
        }
        this.name = name;
        this.color = color;
        this.type = playerType;
    }

    public Player(String name) {
        this(name, Color.black, PlayerType.Human);
    }
    
    abstract public Game.Type getGameType();

    public String getName() {
        return name;
    }

    public int getFreeUnitCount() {
        return freeUnitCount;
    }

    public void setFreeUnitCount(int freeUnitCount) {
        if (freeUnitCount < 0) {
            throw new IllegalArgumentException("" + freeUnitCount);
        }

        this.freeUnitCount = freeUnitCount;
    }

    public int getRemainingMoveCount() {
        return remainingMoveCount;
    }

    public void setRemainingMoveCount(int remainingMoveCount) {
        if (remainingMoveCount < 0) {
            throw new IllegalArgumentException("" + remainingMoveCount);
        }

        this.remainingMoveCount = remainingMoveCount;
    }

    public int getRemainingDeployCount() {
        return remainingDeployCount;
    }

    public void setRemainingDeployCount(int remainingDeployCount) {
        if (remainingDeployCount < 0) {
            throw new IllegalArgumentException("" + remainingDeployCount);
        }
        this.remainingDeployCount = remainingDeployCount;
    }

    public Color getColor() {
        return color;
    }

    public int getCardHandCount() {
        return cardHand.size();
    }
    
    public Card getCardHandCard(Card card) {
    	if (card == null) {
    		throw new NullPointerException();
    	}
    	if (!cardHand.contains(card)) {
    		throw new IllegalArgumentException();
    	}
    	
    	int cardPosition = 10;
    	
    	for (int index = 0; index < cardHand.size();) {
    		if (cardHand.get(index) == card) {
    			cardPosition = index;
    			index = cardHand.size();
    		}
    		else {
    			index++;
    		}
    		
    	}
    	
    	if (cardPosition == 10) {
    		throw new IllegalStateException("player cannot have this many cards");
    	}
    	
    	return cardHand.remove(cardPosition);
    }
    
    public void addCardHandCard(Card cardHandCard) {
        if (cardHandCard == null) {
            throw new NullPointerException();
        }
        if (cardHand.contains(cardHandCard)) {
            throw new IllegalStateException("Cannot add card twice");
        }
        if (getCardHandCount() >= MAX_CARDS_IN_HAND) {
            throw new IllegalStateException("Too many cards added");
        }
        cardHand.add(cardHandCard);
    }
    
    public PlayerType getType() {
        return type;
    }
    
    public boolean isAI() {
        return type == PlayerType.AI;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cardHand == null) ? 0 : cardHand.hashCode());
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        result = prime * result + freeUnitCount;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + remainingDeployCount;
        result = prime * result + remainingMoveCount;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        Player other = (Player) obj;
        if (cardHand == null) {
            if (other.cardHand != null)
                return false;
        }
        else if (!cardHand.equals(other.cardHand))
            return false;
        if (color == null) {
            if (other.color != null)
                return false;
        }
        else if (!color.equals(other.color))
            return false;
        if (freeUnitCount != other.freeUnitCount)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        }
        else if (!name.equals(other.name))
            return false;
        if (remainingDeployCount != other.remainingDeployCount)
            return false;
        if (remainingMoveCount != other.remainingMoveCount)
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        }
        else if (!type.equals(other.type))
            return false;
        return true;
    }

    public List<Card> getCards() {
        return cardHand;
    }
}
