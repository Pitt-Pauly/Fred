package fred.middle;

import java.util.List;
import java.util.ArrayList;

import fred.model.AttackRecord;
import fred.model.Card;
import fred.model.GameDoer;
import fred.model.GameInfo;
import fred.model.Game;
import fred.model.Player;
import fred.model.Territory;
import fred.model.TurnPhase;
import fred.model.World;
import fred.model.Battlefield;


//start here

public class Wrapper implements GameDoer, GameInfo {
	GameDoer doer;
	Game refGame;
	Replay replay;
	
	public Wrapper(GameDoer doer, Game game) {
		this.doer = doer;
		this.refGame = game;
		replay = new Replay(game);
	}
	
	public Wrapper(Replay replay) {
		this.replay = replay;
		Game game = replay.lastPosition();
		this.doer = game;
		this.refGame = game;
	}
	
	protected void handleAction(Action action) {
		action.perform(doer, refGame);
		replay.addAction(action, refGame);
	}
	
	public boolean isButterflyZhuangzi() {
		return true;
	}
	
	public Replay getReplay() {
		return replay;
	}
	
	public void distributeFreeUnits(Territory target, int numberOfUnits) {
		Action action = new DeployAction(target, numberOfUnits);
		handleAction(action);
		
	}
	
	public void moveUnits(Territory source, Territory target, int numberOfUnits) {
		Action action = new MoveAction(source, target, numberOfUnits);
		handleAction(action);
	}
	
	public AttackRecord attack(ArrayList<Territory> sources, Territory target, ArrayList<Integer> numberOfUnits, boolean attackSingle, boolean withdraw) {
		AttackAction action = new AttackAction(sources, target, numberOfUnits, attackSingle);
		handleAction(action);
		return action.getRecord();
	}
	
	public void performAttack(AttackRecord record) {
		AttackAction action = new AttackAction(record);
		handleAction(action);
		return;
	}
		
	public void chooseTurnType(TurnPhase actionPhase) {
		Action action = new ChooseTurnTypeAction(actionPhase);
		handleAction(action);		
	}
	
	public void exchangeCards(Card[] cards) {
		Action action = new ExchangeCardsAction(cards);
		handleAction(action);		
	}

	public void endCurrentTurnPhase() {
		Action action = new EndPhaseAction();
		handleAction(action);
		
	}
	
	public Game.Type getType() {
		return refGame.getType();
	}
	
	public World getWorld() {
		return refGame.getWorld();
	}
    public Battlefield getBattlefield() {
        return refGame.getBattlefield();
    }

    public List<Player> getPlayers() {
		return refGame.getPlayers();
	}
	public Player getCurrentPlayer() {
		return refGame.getCurrentPlayer();
	}
	public boolean canDistributeFreeUnits() {
		return refGame.canDistributeFreeUnits();
	}
	public boolean canDistributeFreeUnits(Territory target, int numberOfUnits) {
		return refGame.canDistributeFreeUnits(target, numberOfUnits);
	}
	public boolean canMoveUnits() {
		return refGame.canMoveUnits();
	}
	public boolean canMoveUnits(Territory source, Territory target, int numberOfUnits) {
		return refGame.canMoveUnits(source, target, numberOfUnits);
	}
	public boolean canAttack() {
		return refGame.canAttack();
	}
	public boolean canAttack(List<Territory> sources, Territory target, List<Integer> numberOfUnits) {
		return refGame.canAttack(sources, target, numberOfUnits);
    }
    public boolean canChooseTurnType() {
        return refGame.canChooseTurnType();
    }
    public boolean canConscript() {
        return refGame.canConscript();
    }
	public boolean canEndTurnPhase() {
		return refGame.canEndTurnPhase();
	}
	public TurnPhase getCurrentTurnPhase() {
		return refGame.getCurrentTurnPhase();
	}
    public String getUICurrentTurnPhase() {
        return refGame.getUICurrentTurnPhase();
    }
	public int getOwnedTerritoryCount(Player player){
		return refGame.getOwnedTerritoryCount(player);
	}
	public boolean isGameOver(){
		return refGame.isGameOver();
	}
	public boolean canExchangeCards(Card[] cards) {
		return refGame.canExchangeCards(cards);
	}
    public boolean canExchangeCards() {
        return refGame.canExchangeCards();
    }
    public int getExchangeCardsValue(Card[] cards) {
        return refGame.getExchangeCardsValue(cards);
    }
	public int calculateConscriptedUnits() {
		return refGame.calculateConscriptedUnits();
	}
	
	public int maxUnitsToMove(Territory source, Territory target) {
		return refGame.maxUnitsToMove(source, target);
	}
	
    public ArrayList<Integer> maxUnitsToAttack(List<Territory> sources, Territory target) {
        return refGame.maxUnitsToAttack(sources, target);
    }
        
    public boolean canPerformAttack(AttackRecord record) {
    	return refGame.canPerformAttack(record);
    }
}
