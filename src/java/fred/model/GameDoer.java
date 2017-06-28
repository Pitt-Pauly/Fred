package fred.model;

import java.util.List;
import java.util.ArrayList;

public interface GameDoer {
	public void distributeFreeUnits(Territory target, int numberOfUnits);
	public void moveUnits(Territory source, Territory target, int numberOfUnits);
	public AttackRecord attack(ArrayList<Territory> sources, Territory target, ArrayList<Integer> numberOfUnits, boolean attackSingle, boolean withdraw);
	public void performAttack(AttackRecord record);
	public void endCurrentTurnPhase();
	public void chooseTurnType(TurnPhase actionPhase);
	public void exchangeCards(Card[] cards);
}
