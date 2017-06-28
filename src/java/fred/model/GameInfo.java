package fred.model;

import java.util.List;
import java.util.ArrayList;

public interface GameInfo {
	public Game.Type getType();
	public World getWorld();
	public List<Player> getPlayers();
	public Player getCurrentPlayer();
	public boolean canDistributeFreeUnits();
	public boolean canDistributeFreeUnits(Territory target, int numberOfUnits);
	public boolean canMoveUnits();
	public boolean canMoveUnits(Territory source, Territory target, int numberOfUnits);
	public boolean canAttack();
	public boolean canAttack(List<Territory> sources, Territory target, List<Integer> numberOfUnits);
    public boolean canChooseTurnType();
    public boolean canConscript();
	public boolean canEndTurnPhase();
	public TurnPhase getCurrentTurnPhase();
	public String getUICurrentTurnPhase();
	public int getOwnedTerritoryCount(Player player);
	public boolean isGameOver();
	public boolean canExchangeCards(Card[] cards);
	public boolean canExchangeCards();
	public int getExchangeCardsValue(Card[] cards);
	public int calculateConscriptedUnits();
	public int maxUnitsToMove(Territory source, Territory target);
    public ArrayList<Integer> maxUnitsToAttack(List<Territory> sources, Territory target);
    public boolean canPerformAttack(AttackRecord record);
}
