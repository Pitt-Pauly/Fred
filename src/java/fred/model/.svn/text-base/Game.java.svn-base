package fred.model;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.ListIterator;


public abstract class Game implements Serializable, GameDoer, GameInfo {

    private static final long serialVersionUID = 1L;
    
    public static final int POST_ACTION_MOVE_COUNT = 7;
    private static final int SETUP_UNIT_TURN_DEPLOYMENT_COUNT = 2;
    
    CardDeck cardDeck;
    private final List<Player> players;
    private final World world;
    int currentPlayerIndex;
    int currentPlayerVictories;
    private TurnPhase turnPhase = TurnPhase.SetupUnitDeployment;
    private Battlefield battlefield = new Battlefield();
    
    public enum Type {Basic, Advanced}

    public Game(List<Player> players, World world) {
        if (players == null || world == null) {
            throw new NullPointerException();
        }
        if (players.size() == 0) {
            throw new IllegalArgumentException("Player list empty");
        }
        if (players.size() > 6) {
        	throw new IllegalArgumentException("Too Many Players");
        }
        this.players = players;
        this.world = world;
        cardDeck = new CardDeck(world.getTerritories());
        
        for(Player player: players) {
        	if(player.getGameType() != getType()) {
        		throw new IllegalArgumentException("Player for wrong Game type");
        	}
        }
        
        //Needs testing.
        for(Territory territory: world.getTerritories()) {
        	if(territory.getGameType() != getType()) {
        		throw new IllegalArgumentException("Territory for wrong Game type");
        	}
        }
    }
    
    public abstract Type getType();

    public World getWorld() {
        return world;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void setup() {
    	handOutTerritories();
    	for (Player player : getPlayers()) {
    		int total = (50 - (5 * players.size()));
    		for(Territory territory: world.getTerritories()) {
    			if(territory.getOwner() == player) {
    				total -= territory.getUnitCount();
    			}
    		}
            player.setFreeUnitCount(total);
        }
    	getCurrentPlayer().setRemainingDeployCount(2);
    }
    
    protected void handOutTerritories() {
        List<Territory> unownedTerritories = new ArrayList<Territory>(world.getTerritories());
        ListIterator<Territory> iter = unownedTerritories.listIterator();
        while (iter.hasNext()) {
            if (iter.next().getOwner() != null) {
                iter.remove();
            }
        }

        while (unownedTerritories.size() > 0) {
            for (int j = players.size() - 1; j >= 0; j--) {
                if (unownedTerritories.size() == 0) {
                    break;
                }
                int index = (int) (Math.random() * unownedTerritories.size());
                Territory unowned = unownedTerritories.get(index);
                unownedTerritories.remove(index);
                unowned.setOwner(players.get(j));
                unowned.setUnitCount(1);
            }
        }
    }

    public void addFreeUnits(int numberOfUnits) {
        if (numberOfUnits <= 0) {
            throw new IllegalArgumentException("" + numberOfUnits);
        }
        getCurrentPlayer().setFreeUnitCount(getCurrentPlayer().getFreeUnitCount() + numberOfUnits);
    }

    public void distributeFreeUnits(Territory target, int numberOfUnits) {
        if (turnPhase != TurnPhase.TurnUnitDeployment && turnPhase != TurnPhase.TurnConscriptUnitDeployment && turnPhase != TurnPhase.SetupUnitDeployment) {
            throw new IllegalStateException("Cannot deploy in TurnPhase " + turnPhase.name());
        }
        if (numberOfUnits <= 0 || numberOfUnits > getCurrentPlayer().getFreeUnitCount()) {
            throw new IllegalArgumentException("" + numberOfUnits);
        }
        if (turnPhase == TurnPhase.SetupUnitDeployment && numberOfUnits > getCurrentPlayer().getRemainingDeployCount()) {
            throw new IllegalArgumentException("units " + numberOfUnits + " remaining " + getCurrentPlayer().getRemainingDeployCount());
        }
        if (target == null) {
            throw new NullPointerException();
        }
        if (target.getOwner() != getCurrentPlayer()) {
        	if(target.getOwner() == null) {
        		throw new IllegalArgumentException("territory not owned");
        	}
        	throw new IllegalArgumentException(target.getName() + " owned by " + target.getOwner().getName() + " not " + getCurrentPlayer().getName());
        }
        target.setUnitCount(target.getUnitCount() + numberOfUnits);
        getCurrentPlayer().setFreeUnitCount(getCurrentPlayer().getFreeUnitCount() - numberOfUnits);
        if (turnPhase == TurnPhase.SetupUnitDeployment) {
            getCurrentPlayer().setRemainingDeployCount(getCurrentPlayer().getRemainingDeployCount() - numberOfUnits);
        }
        if (getCurrentPlayer().getFreeUnitCount() == 0) {
            endCurrentTurnPhase();
        }
    }

    public boolean canDistributeFreeUnits() {
        if (turnPhase != TurnPhase.TurnUnitDeployment && turnPhase != TurnPhase.TurnConscriptUnitDeployment && turnPhase != TurnPhase.SetupUnitDeployment) {
            return false;
        }
        return true;
    }

    public boolean canDistributeFreeUnits(Territory target, int numberOfUnits) {
        if (!canDistributeFreeUnits()) {
            return false;
        }
        if (numberOfUnits <= 0 || numberOfUnits > getCurrentPlayer().getFreeUnitCount()) {
            return false;
        }
        if (turnPhase == TurnPhase.SetupUnitDeployment && numberOfUnits > getCurrentPlayer().getRemainingDeployCount()) {
            return false;
        }
        if (target == null) {
            throw new NullPointerException();
        }
        if (target.getOwner() != getCurrentPlayer()) {
            return false;
        }
        return true;
    }

    public void moveUnits(Territory source, Territory target, int numberOfUnits) {
        if (!canMoveUnits(source, target, numberOfUnits)) {
            throw new IllegalArgumentException();
        }
        
        if (turnPhase == TurnPhase.TurnAction) {
        	turnPhase = TurnPhase.TurnUnlimitedMovement;
        }
        target.setUnitCount(target.getUnitCount() + numberOfUnits);
        source.setUnitCount(source.getUnitCount() - numberOfUnits);
        
        //sometimes tests are great
        if (turnPhase == TurnPhase.TurnPostActionMovement) {
        	getCurrentPlayer().setRemainingMoveCount(getCurrentPlayer().getRemainingMoveCount() - numberOfUnits * world.getPathwayCost(source, target));
        }
    }

    public boolean canMoveUnits() {
        if (turnPhase != TurnPhase.TurnPostActionMovement && turnPhase != TurnPhase.TurnUnlimitedMovement && turnPhase != TurnPhase.TurnAction) {
            return false;
        }
        return true;
    }

    public boolean canMoveUnits(Territory source, Territory target, int numberOfUnits) {
        if (source == null || target == null) {
            throw new NullPointerException();
        }
        if (!canMoveUnits()) {
            return false;
        }
        if (source.getOwner() != getCurrentPlayer() || target.getOwner() != getCurrentPlayer()) {
            return false;
        }
        if (source == target) {
            return false;
        }
        if (numberOfUnits <= 0 || numberOfUnits > maxUnitsToMove(source, target)) {
            return false;
        }
       
        return true;
    }
    
    public int maxUnitsToMove(Territory source, Territory target) {
		if (source == null || target == null) {
		    throw new NullPointerException();
		}
		
		if (source == target) {
			return 0;
		}
		if (source.getOwner() != target.getOwner()) {
			return 0;
		}
		
		int pathCost = world.getPathwayCost(source, target);
		
		if(pathCost < 1) {
			return 0;
		}
		
		if(turnPhase == TurnPhase.TurnUnlimitedMovement || turnPhase == TurnPhase.TurnAction) {
			return source.getUnitCount() - 1;
		}
 	
		return Math.min(source.getUnitCount() - 1, getCurrentPlayer().getRemainingMoveCount() / pathCost);
    }
   
    public AttackRecord attack(ArrayList<Territory> sources, Territory target, ArrayList<Integer> numberOfUnits, boolean attackSingle, boolean withdraw) {
        if (battlefield == null) {
        	battlefield = new Battlefield();
        }
    	if (!battlefield.isCleared() && !attackSingle) {
            throw new IllegalStateException("Battlefield must be clear to start new attack");
        }
        if (battlefield.isCleared()) {
            if (!canAttack(sources, target, numberOfUnits)) {
                throw new IllegalArgumentException();
            }

            if (turnPhase == TurnPhase.TurnAction) {
                setCurrentTurnPhase(TurnPhase.TurnAttack);
            }

            battlefield.setup(sources.get(0).getOwner(), target.getOwner(), numberOfUnits, target.getUnitCount(), sources, target);
        }
        
        if (withdraw) {
        	battlefield.withdraw();
        } else
        	if (attackSingle) {
        		battlefield.performSingleAttack();
        	} else {
        		battlefield.performFullAttack();
        	}


        if (battlefield.isFinished()) {
            if (battlefield.isSuccess()) {
                currentPlayerVictories++;
                target.setUnitCount(battlefield.getAttackers());
                if (currentPlayerVictories==1 && getCurrentPlayer().getCardHandCount() < Player.MAX_CARDS_IN_HAND){
                    Card newCard = cardDeck.getDealtCard();
                    if(newCard != null) {
                        getCurrentPlayer().addCardHandCard(newCard);
                        battlefield.getRecord().setCardTerritoryName(newCard.getTerritory().getName());
                    }
                }
            }
            if (!battlefield.isSuccess()) {
                if (battlefield.getAttackers() > 0) {
                    if (!battlefield.isWithdrawn()) {
                    	throw new RuntimeException("Something went wrong here");
                    }
                }
                else if (target.getUnitCount() <= 0) {
                    throw new RuntimeException("Something went wrong here");
                    }
             }

            battlefield.clearBattlefield();
        }
        else if (!battlefield.isFinished() && !attackSingle) {
                throw new RuntimeException("Something went wrong here");
            }
        
        return battlefield.getRecord();
    }
    
    public void performAttack(AttackRecord record) {
    	if(!getCurrentPlayer().getName().equals(record.attackerName)) {
    		throw new IllegalArgumentException("player " + getCurrentPlayer().getName() + " attacker " + record.attackerName);
    	}
    	
    	if (record.initialAttackers.size() == 0) {
            throw new IllegalArgumentException();
        }

    	
    	// Extracting source Territories from attackRecord
    	List<Territory> sources = new ArrayList<Territory>();
    	for (String territoryName: record.fromName) {
    		sources.add(world.getTerritoryByName(territoryName));
    	}
    	// Extracting target territory from attackRecord
    	Territory target = world.getTerritoryByName(record.toName);
    	
        if (!canAttack(sources, target, record.initialAttackers)) {
            throw new IllegalArgumentException();
        }

        if (turnPhase == TurnPhase.TurnAction) {
            setCurrentTurnPhase(TurnPhase.TurnAttack);
        }
        

    	int sourceTerritoryPositionInArray = 0;
        for (Territory source: sources) {
        	int unitsLost = record.initialAttackers.get(sourceTerritoryPositionInArray);
        	if(record.isWithdrawn()) {
        		unitsLost -= record.returnedUnits().get(sourceTerritoryPositionInArray);
        	}
        	source.setUnitCount(source.getUnitCount() - unitsLost);
        	sourceTerritoryPositionInArray++;
        }
        
        if(!record.isSuccess()) {
        	target.setUnitCount(record.getSurvivors());
        	return;
        }
        
        target.setOwner(getCurrentPlayer());
        target.setUnitCount(record.getSurvivors());
        currentPlayerVictories++;
        
        if(record.getCardTerritoryName() != null) {
        	getCurrentPlayer().addCardHandCard(cardDeck.fixedDeal(record.getCardTerritoryName()));
        }        
    }
    
    public boolean canPerformAttack(AttackRecord record) {
    	if(!getCurrentPlayer().getName().equals(record.attackerName)) {
    		return false;
    	}
    	
    	// Extracting source Territories from attackRecord
    	List<Territory> sources = new ArrayList<Territory>();
    	for (String territoryName: record.fromName) {
    		sources.add(world.getTerritoryByName(territoryName));
    	}
    	// Extracting target territory from attackRecord
    	Territory target = world.getTerritoryByName(record.toName);
    	
    	// Extracting initialAttackers from attackRecord
    	List<Integer> numberOfUnits = new ArrayList<Integer>();
    	for (int attackers: record.initialAttackers) {
    		numberOfUnits.add(attackers);
    	}
    	
        if (!canAttack(sources, target, numberOfUnits)) {
            return false;
        }
        
        
        
        for (int attackerUnits: numberOfUnits) {
        	int positionInArray = 0;
        	if(attackerUnits >= sources.get(positionInArray).getUnitCount()) {
        	return false;
        	}
        	positionInArray++;
        }
        return true;
    }


    public boolean canAttack() {
        if (turnPhase != TurnPhase.TurnAttack && turnPhase != TurnPhase.TurnAction) {
            return false;
        }
        return true;
    }

    public boolean canAttack(List<Territory> sources, Territory target, List<Integer> numberOfUnits) {
        if (sources == null || target == null || numberOfUnits == null) {
            throw new NullPointerException();
        }
        if (sources.size() != numberOfUnits.size()) {
        	return false;
        }
    	if (!canAttack()) {
            return false;
        }
        for (Territory source: sources) {
        	if (source.getOwner() != getCurrentPlayer()) {
        		return false;
        	}
        }
        if (target.getOwner() == getCurrentPlayer()) {
            return false;
        }
        //Checking if any territory attacks with too many or too few Units
        for (int index = 0; index < numberOfUnits.size(); index++) {
        	if (numberOfUnits.get(index) <= 0) {
        		return false;
        	}
        	if (numberOfUnits.get(index) > maxUnitsToAttack(sources, target).get(index)) {
        		return false;
        	}
        }
        
        for (Territory source: sources){
        	if (!source.isConnected(target)) {
            	return false;
        	}
        }
        return true;
    }
    
    public ArrayList<Integer> maxUnitsToAttack(List<Territory> sources, Territory target) {
    	if (sources == null || target == null) {
		    throw new NullPointerException();
		}
    	
    	ArrayList<Integer> maxAttackUnits = new ArrayList<Integer>();
        for (Territory source: sources) {
        	maxAttackUnits.add(source.getUnitCount() - 1);
        }
		
        return maxAttackUnits;
    }

    public void chooseTurnType(TurnPhase actionPhase) {
        if (!canChooseTurnType()) {
            throw new IllegalStateException();
        }
        if (actionPhase != TurnPhase.TurnAttack && actionPhase != TurnPhase.TurnConscriptUnitDeployment && actionPhase != TurnPhase.TurnUnlimitedMovement) {
            throw new IllegalArgumentException();
        }
        if (actionPhase == TurnPhase.TurnConscriptUnitDeployment) {
            getCurrentPlayer().setFreeUnitCount(calculateConscriptedUnits());
        }
        setCurrentTurnPhase(actionPhase);
    }

    public boolean canChooseTurnType() {
        return (turnPhase == TurnPhase.TurnAction);
    }
    

    public boolean canConscript() {
        return (turnPhase == TurnPhase.TurnAction);
    }


    void nextPlayer() {
        if (turnPhase == TurnPhase.SetupUnitDeployment) {
            int nextIndex = getNextPlayerWithFreeUnits();
            if (nextIndex > -1) {
                currentPlayerIndex = nextIndex;
                getCurrentPlayer().setRemainingDeployCount(
                        Math.min(getCurrentPlayer().getFreeUnitCount(), SETUP_UNIT_TURN_DEPLOYMENT_COUNT));
            }
            else {
                currentPlayerIndex = 0;
                setCurrentTurnPhase(TurnPhase.TurnReceiveTurnUnits);
                receiveTurnUnits();
            }
        }
        else {
            int nextIndex = getNextPlayerIndexWithTerritories(currentPlayerIndex);
            int nextAgainIndex = getNextPlayerIndexWithTerritories(nextIndex);
            if (nextIndex != nextAgainIndex) {
                currentPlayerIndex = nextIndex;
                setCurrentTurnPhase(TurnPhase.TurnReceiveTurnUnits);
                receiveTurnUnits();
            }
            else {
                currentPlayerIndex = nextIndex;
                setCurrentTurnPhase(TurnPhase.GameOver);
                if (getOwnedTerritoryCount(getCurrentPlayer()) <= 0) {
                	throw new IllegalStateException("Wrong Player");
                }
            }
            
            //TODO - temporary
            // temporarily removed: can run out of cards
            //getCurrentPlayer().addCardHandCard(cardDeck.getDealtCard());
        }
    }

    private int getNextPlayerIndexWithTerritories(int currentIndex) {
        int count = 0;
        for (int index = (currentIndex + 1) % players.size(); count < players.size(); index = (index + 1) % players.size()) {
            if (getOwnedTerritoryCount(players.get(index)) > 0) {
                return index;
            }
            count++;
        }
        throw new IllegalStateException("No one has any territories");
    }

    private int getNextPlayerWithFreeUnits() {
        int count = 0;
        for (int index = (currentPlayerIndex + 1) % players.size(); count < players.size(); index = (index + 1) % players.size()) {
            if (players.get(index).getFreeUnitCount() > 0) {
                return index;
            }
            count++;
        }
        return -1;
    }
    
    public TurnPhase getCurrentTurnPhase() {
        return turnPhase;
    }

    public String getUICurrentTurnPhase() {
            if (turnPhase == null) {
                   throw new IllegalStateException("Should not have null turnPhase");
            }
            // TODO - with current font size, max length should be 15 chars
            switch(turnPhase) {
                    case SetupUnitDeployment:
                            return "Deploy Initial Units";
                    case TurnReceiveTurnUnits:
                            return "Receive Units";
                    case TurnExchangeCards:
                            return "Card Exchange";
                    case TurnUnitDeployment:
                            return "Deploy Units";
                    case TurnAction:
                            return "Action";
                    case TurnAttack:
                            return "Attack";
                    case TurnConscriptUnitDeployment:
                            return "Deploy Conscript Units";
                    case TurnPostActionMovement:
                            return "Post-Action Movement";
                    case TurnUnlimitedMovement:
                            return "Movement";
                    case GameOver:
                            return "Game Over";
                    default:
                            return "Default Turn";
            }

    }

    // Package private for unit test access
    void setCurrentTurnPhase(TurnPhase turnPhase) {
        this.turnPhase = turnPhase;
    }
   
//TODO: hacked. pleas test.
    public boolean canEndTurnPhase() {
    	if (turnPhase == null) {
            throw new IllegalStateException("Should not have null turnPhase");
        }
        switch (turnPhase) {
        case TurnAction:
        	return false;
        case SetupUnitDeployment:
        	return (getCurrentPlayer().getRemainingDeployCount() == 0) || (getCurrentPlayer().getFreeUnitCount() == 0);
        case TurnUnitDeployment:
        	return (getCurrentPlayer().getFreeUnitCount() == 0);
        default:
        	return true;	
        }
        
    }

    public void endCurrentTurnPhase() {
        if (turnPhase == null) {
            throw new IllegalStateException("Should not have null turnPhase");
        }
        switch (turnPhase) {

        case TurnExchangeCards:
            turnPhase = TurnPhase.TurnUnitDeployment;
            break;
        case TurnAction:
            throw new IllegalStateException("Ambiguous next phase for" + turnPhase.name());

        case TurnConscriptUnitDeployment:
            turnPhase = TurnPhase.TurnPostActionMovement;
            getCurrentPlayer().setRemainingMoveCount(POST_ACTION_MOVE_COUNT);
            break;

        case TurnAttack:
            turnPhase = TurnPhase.TurnPostActionMovement;
            getCurrentPlayer().setRemainingMoveCount(POST_ACTION_MOVE_COUNT);
            currentPlayerVictories = 0;
            break;

        case SetupUnitDeployment:
        	if((getCurrentPlayer().getRemainingDeployCount() > 0) && (getCurrentPlayer().getFreeUnitCount() > 0)) {
        		throw new IllegalStateException("Still have units to deploy");
        	}
        	nextPlayer();
            break;
        case TurnPostActionMovement:
        case TurnUnlimitedMovement: 
            nextPlayer();
            break;

        case TurnUnitDeployment:
        	if(getCurrentPlayer().getFreeUnitCount() > 0) {
        		throw new IllegalStateException("Still have units to deploy");
        	}
            turnPhase = TurnPhase.TurnAction;
            break;

        default:
            break;
        }
    }

    public int calculateConscriptedUnits() {
        int result = 3;
        int ownedTerritoryCount = getOwnedTerritoryCount(getCurrentPlayer());
        if (ownedTerritoryCount > 9) {
            result = ownedTerritoryCount / 3;
            if (ownedTerritoryCount % 3 > 0) {
                result++;
            }
        }
        return result;
    }

    public boolean canExchangeCards(Card[] cards) {
        if (cards == null) {
            throw new NullPointerException();
        }
        if (cards.length !=3) {
            return false;
        }
        CardType cardType1 = cards[0].getType();
        CardType cardType2 = cards[1].getType();
        CardType cardType3 = cards[2].getType();

        if (!getCurrentPlayer().getCards().contains(cards[0])) {
        	return false;
        }
        if (!getCurrentPlayer().getCards().contains(cards[1])) {
        	return false;
        }
        if (!getCurrentPlayer().getCards().contains(cards[2])) {
        	return false;
        }
        
        if(cards[0].equals(cards[1]) || cards[0].equals(cards[2]) || cards[1].equals(cards[2])) {
        	return false;
        }
        
        if (cardType1 == cardType2) {
            if (cardType3 == CardType.Joker || cardType1 == CardType.Joker) {
                return true;
            } else if (cardType3 != cardType1) {
                return false;
            } 
        }
        
        if (cardType1 == cardType3) {
        	if (cardType2 == CardType.Joker || cardType1 == CardType.Joker) {
                return true;
            } else if (cardType2 != cardType1) {
                return false;
            } 
        }
        
        if (cardType2 == cardType3) {
        	if (cardType1 == CardType.Joker || cardType2 == CardType.Joker) {
                return true;
            } else if (cardType1 != cardType2) {
                return false;
            } 
        }
        
        return true;
    }

    public boolean canExchangeCards() {
        if (turnPhase != TurnPhase.TurnExchangeCards) {
            return false;
        }
        return true;
    }

    public void exchangeCards(Card[] cards) {
        if (!canExchangeCards()) {
            throw new IllegalStateException();
        }
        if (!canExchangeCards(cards)) {
            throw new IllegalArgumentException();
        }
        if (cards == null) {
            throw new NullPointerException();
        }

        for (Card card : cards) {
            if (card.getTerritory().getOwner() == getCurrentPlayer()) {
                card.getTerritory().setUnitCount(card.getTerritory().getUnitCount() + 2);
            }
        }

        getCurrentPlayer().setFreeUnitCount(getCurrentPlayer().getFreeUnitCount() + getExchangeCardsBasicValue(cards));

        for (Card card : cards) {
            cardDeck.addDiscardCard(getCurrentPlayer().getCardHandCard(card));
        }
        endCurrentTurnPhase();
    }

    public int getExchangeCardsValue(Card[] cards) {
        if (!canExchangeCards()) {
            throw new IllegalStateException();
        }
        if (!canExchangeCards(cards)) {
            throw new IllegalArgumentException();
        }
        if (cards == null) {
            throw new NullPointerException();
        }

        int result = 0;
        
        for (Card card : cards) {
            if (card.getTerritory().getOwner() == getCurrentPlayer()) {
                result += 2;
            }
        }
       
        result += getExchangeCardsBasicValue(cards);
        
        return result;
    }

    private int getExchangeCardsBasicValue(Card[] cards) {
        CardType cardType1 = cards[0].getType();
        CardType cardType2 = cards[1].getType();
        CardType cardType3 = cards[2].getType();
        // Exchanging 3 of the same kind
        if (cardType1 == cardType2 && cardType1 == cardType3) {
            if (cardType1 == CardType.Infantry) {
                return 4;
            }
            if (cardType1 == CardType.Cavalry) {
                return 6;
            }
            if (cardType1 == CardType.Cannon) {
                return 8;
            }
        }
        // Exchanging 2 of the same plus 1 Joker
        if (cardType1 == cardType2 && cardType3 == CardType.Joker || cardType1 == cardType3 && cardType2 == CardType.Joker || cardType3 == cardType2
                && cardType1 == CardType.Joker) {
            if (cardType1 == CardType.Infantry || cardType2 == CardType.Infantry) {
                return 4;
            }
            if (cardType1 == CardType.Cavalry || cardType2 == CardType.Cavalry) {
                return 6;
            }

            if (cardType1 == CardType.Cannon || cardType2 == CardType.Cannon) {
                return 8;
            }
        }
        // Exchanging three different cards or 2 Jokers and 1 other
        if (cardType1 != cardType2 && cardType2 != cardType3 && cardType1 != cardType3 || cardType1 == CardType.Joker && cardType2 == CardType.Joker
                || cardType1 == CardType.Joker && cardType3 == CardType.Joker || cardType2 == CardType.Joker && cardType3 == CardType.Joker) {
            return 10;
        }
        throw new IllegalArgumentException("Unexpected card combination");
    }

    public int getOwnedTerritoryCount(Player player) {
        if (player == null) {
            throw new NullPointerException();
        }
        int counter = 0;
        for (Territory territory : world.getTerritories()) {
            if (player == territory.getOwner()) {
                counter++;
            }
        }
        return counter;
    }

    public Battlefield getBattlefield(){
        return battlefield;
    }

    void receiveTurnUnits() {
        if (turnPhase != TurnPhase.TurnReceiveTurnUnits) {
            throw new IllegalStateException("Cannot receiveTurnUnits in TurnPhase " + turnPhase.name());
        }
        getCurrentPlayer().setFreeUnitCount(calculateTurnUnits());
        turnPhase = TurnPhase.TurnExchangeCards;
    }

    private int calculateTurnUnits() {
        int result = 1;
        int ownedTerritoryCount = getOwnedTerritoryCount(getCurrentPlayer());
        if (ownedTerritoryCount > 5) {
            result = ownedTerritoryCount / 3;
        }
        List<TerritoryGroup> fullyOwnedGroups = world.getFullyOwnedTerritoryGroups(getCurrentPlayer());
        for (TerritoryGroup territoryGroup : fullyOwnedGroups) {
            result += territoryGroup.getBonusValue();
        }
        return result;
    }

    public boolean isGameOver() {
        return (turnPhase == TurnPhase.GameOver);
    }
}
