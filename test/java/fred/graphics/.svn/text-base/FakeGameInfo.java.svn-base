package fred.graphics;

import java.util.ArrayList;
import java.util.List;

import fred.model.AttackRecord;
import fred.model.Card;
import fred.model.Game;
import fred.model.GameInfo;
import fred.model.Player;
import fred.model.Territory;
import fred.model.TerritoryGroup;
import fred.model.TurnPhase;
import fred.model.World;

//For testing TerritoryUis
public class FakeGameInfo implements GameInfo {
	private World world;
	
	FakeGameInfo(List<Territory> territories) {
		List<TerritoryGroup> groups = new ArrayList<TerritoryGroup>();
    	groups.add(new TerritoryGroup("here", territories, 1));
    	World smallWorld = new World(territories, groups);
    	world = smallWorld; 
	}
	
	public Game.Type getType() {
		return Game.Type.Basic;
	}
	public World getWorld() {
		return world;
	}
	public List<Player> getPlayers() {
		return null;
	}
	public Player getCurrentPlayer() {
		return null;
	}
	public boolean canDistributeFreeUnits() {
		return false;
	}
	public boolean canDistributeFreeUnits(Territory target, int numberOfUnits) {
		return false;
	}
	public boolean canMoveUnits() {
		return false;
	}
	public boolean canMoveUnits(Territory source, Territory target, int numberOfUnits) {
		return false;
	}
	public boolean canAttack() {
		return false;
	}
	public boolean canAttack(List<Territory> sources, Territory target, List<Integer> numberOfUnits) {
		return false;
	}
    public boolean canChooseTurnType() {
        return false;
    }
    public boolean canConscript() {
        return false;
    }
	public boolean canEndTurnPhase() {
		return false;
	}

	public TurnPhase getCurrentTurnPhase() {
		return TurnPhase.TurnAttack;
	}
	public int getOwnedTerritoryCount(Player player){
		return 0;
	}
	public boolean isGameOver(){
		return false;
	}
	public boolean canExchangeCards(Card[] cards) {
		return false;
	}
	public boolean canExchangeCards() {
		return false;
	}
	public int getExchangeCardsValue(Card[] cards) {
	    return 0;
	}
	public int calculateConscriptedUnits() {
		return 0;
	}
	public String getUICurrentTurnPhase() {
        return null;
	}
	public int maxUnitsToMove(Territory source, Territory target) {
		return 0;
	}
	public boolean canPerformAttack(AttackRecord record) {
		return false;
	}
	
	public ArrayList<Integer> maxUnitsToAttack(List<Territory> sources, Territory target) {
		ArrayList<Integer> emptyList = new ArrayList<Integer>();
		return emptyList;
    }
}