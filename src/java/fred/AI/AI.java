// I'm new to java so expect a few ignorance related bugs.
package fred.AI;

import fred.model.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class AI {
	private GameInfo game;
	public GameDoer doer;
	AICacheNAlgorithms CnA;
	private List<Territory> plan;
	private PrintWriter log = null;
	
	public AI(GameDoer doer, GameInfo game) {
		this.doer = doer;
		this.game = game;
		CnA = new AICacheNAlgorithms(game);
		plan = new ArrayList<Territory>();
		//setupLog();
	}
	
	public void doTurn() {
		if(log != null) {
			log.println(game.getCurrentPlayer().getName() + " will crush all humans.");
			log.flush();
		}
		plan.clear();
		
		Player us = game.getCurrentPlayer();
		
		do {
			if(!us.isAI()) {
				throw new IllegalStateException("AI should not be playing human's turn");
			}
			while(us == game.getCurrentPlayer() && TurnPhase.GameOver != game.getCurrentTurnPhase()) {
				doSomething();
			}
			us = game.getCurrentPlayer();
		} while (game.getCurrentTurnPhase() == TurnPhase.SetupUnitDeployment && game.getCurrentPlayer().isAI());
	}
	
	private void doSomething() {
		
		CnA.invalidate();
		
		if(exchangeCards()) {
			return;
		}
		
		if(distributeFreeUnits(-1)) {
			return;
		}
		
		if(game.getCurrentTurnPhase() != TurnPhase.SetupUnitDeployment && game.getCurrentTurnPhase() != TurnPhase.TurnExchangeCards && game.getCurrentPlayer().getFreeUnitCount() > 0) {
			throw new IllegalStateException();
		}
		
		if(chooseTurnType())
		{
			return;
		}
		
		if(attack())
		{
			return;
		}
		
		if(game.canChooseTurnType())
		{
			doer.chooseTurnType(TurnPhase.TurnConscriptUnitDeployment);
			return;
		}
		
		if(moveUnits())
		{
			return;
		}
		
		if(!game.canEndTurnPhase()) {
			throw new IllegalStateException("AI can't do anything");
		}
		doer.endCurrentTurnPhase();		
		return;
	}
	
	public boolean exchangeCards() {
		if(!game.canExchangeCards()) {
			return false;
		}
		List<Card> hand = game.getCurrentPlayer().getCards();
		if(hand.size() < 3) {
			return false;
		}
		for(Card first: hand) {
			for(Card second: hand) {
				for(Card third: hand) {
					Card[] exchange = {first, second, third};
					if(game.canExchangeCards(exchange)) {
						doer.exchangeCards(exchange);
						return true;
					}
				}
			}
		}
		return false;		
	}
	
	public boolean distributeFreeUnits(int numberOfUnits) {
		if(numberOfUnits == -1)
		{
			numberOfUnits = game.getCurrentPlayer().getFreeUnitCount();
			if(game.getCurrentTurnPhase() == TurnPhase.SetupUnitDeployment)
			{
				numberOfUnits = Math.min(numberOfUnits, game.getCurrentPlayer().getRemainingDeployCount());
			}
		}
		if(numberOfUnits <= 0)
		{
			return false;
		}
		if(!game.canDistributeFreeUnits())
		{
			return false;
		}
		Territory bestTerritory = null;
		int bestValue = -1;
		int bestDifference = 56;
		for(Territory loopTerritory : game.getWorld().getTerritories()) {
			if(game.canDistributeFreeUnits(loopTerritory, numberOfUnits))
			{
				int difference = CnA.surroundDifference(loopTerritory);
				int value = reinforceValue(loopTerritory, difference);
				if ((value > bestValue) || (bestValue == -1) || (bestDifference >= 2 && difference < bestDifference && value > 0)) {
					if(difference < 2 || difference <= bestDifference) {
						bestValue = value;
						bestTerritory = loopTerritory;
						bestDifference = difference;
					}
				}
			}
		}
		
		if(bestTerritory == null) {
			return false;
		}
		if(bestDifference < 2 && numberOfUnits > 2 - bestDifference) {
			numberOfUnits = 2 - bestDifference;
		}
		doer.distributeFreeUnits(bestTerritory, numberOfUnits);
		if(log != null) {
			log.println("Reinforcing " + bestTerritory.getName() + " with value:" + bestValue + " and surround difference " + bestDifference);
		}
		return true;
	}
	
	private int reinforceValue(Territory territory, int surroundDifference) {
		if(CnA.borderDistance(territory) > 0) {
			return 0;
		}
		int value = 100 * (Math.min(10,territory.getUnitCount()) + 1);
		if(game.getCurrentTurnPhase() != TurnPhase.SetupUnitDeployment) {
			value = Math.max(value, 500);
		}
		TerritoryGroup continent = game.getWorld().findGroup(territory);
		value += Math.max(0, 300 * CnA.friendlyTerritoryCount(continent) - 200 * CnA.enemyTerritoryCount(continent));
		if(-3 < surroundDifference && surroundDifference < 2) {
			value += 5000;
		}
		// continent or strategic region stuff to go here
		return (int) (Math.random() * value);		
	}
	

	public boolean attack() {
		if(!game.canAttack())
		{
			return false;
		}
		
		if(plan.size() > 0) {
			if(executeAttackPlan(false)) {
				return true;
			}
		}
		
		if(conquerContinents()) {
			return true;
		}
		
		int bestValue = 0;
		Territory bestFromTerritory = null;
		Territory bestToTerritory = null;
		for(Territory loopFromTerritory : game.getWorld().getTerritories()) {
			
			if((loopFromTerritory.getOwner() == game.getCurrentPlayer())) {
				for(Territory loopToTerritory : loopFromTerritory.getNeighbours()) {
					//Creating lists for use in canAttack)
					List<Territory> loopFromTerritoryList = new ArrayList<Territory>();
					loopFromTerritoryList.add(loopFromTerritory);
					List<Integer> numberOfUnits = new ArrayList<Integer>();
					numberOfUnits.add(1); //1 was used as value
					if(game.canAttack(loopFromTerritoryList, loopToTerritory, numberOfUnits)) {
						int loopValue = attackValue(loopFromTerritory, loopToTerritory);
						if (loopValue > bestValue) {
							bestValue = loopValue;
							bestFromTerritory = loopFromTerritory;
							bestToTerritory = loopToTerritory;
						}
					}
					loopFromTerritoryList.clear();
					numberOfUnits.clear();
				}
			}
		}
		if((bestFromTerritory == null) || (bestToTerritory == null)) {
			return false;
		}
		//Making Lists for use in maxUnitsToAttack and attackFull
		ArrayList<Territory> bestFromTerritoryList = new ArrayList<Territory>();
		bestFromTerritoryList.add(bestFromTerritory);
		ArrayList<Integer> attackUnits = game.maxUnitsToAttack(bestFromTerritoryList, bestToTerritory);
		doer.attack(bestFromTerritoryList, bestToTerritory, attackUnits, false, false);
		if(log != null) {
			log.println("Attacked " + bestToTerritory.getName() + " with value " + bestValue);
		}
		//Clearing Lists after use just in case
		bestFromTerritoryList.clear();
		attackUnits.clear();
		
		return true;
	}
	private int attackValue(Territory fromTerritory, Territory toTerritory) {
		if( fromTerritory.getOwner() == toTerritory.getOwner()) {
			throw new IllegalArgumentException();	
		}
		
		for(Territory neighbour : toTerritory.getNeighbours()) {
			if(neighbour.getOwner() != fromTerritory.getOwner()) {
				if(neighbour.getUnitCount() >= fromTerritory.getUnitCount()) {
					if(!isOurThreat(neighbour))
					{
						return 0;
					}
				}
			}
		}
		if(fromTerritory.getUnitCount() > 7 && isOurThreat(toTerritory)) {
			return fromTerritory.getUnitCount() + toTerritory.getUnitCount();
		}
		
		return fromTerritory.getUnitCount() - toTerritory.getUnitCount(); 
	}
	
	
	public boolean moveUnits() {
		if(!game.canMoveUnits())
		{
			return false;
		}
		
		int bestValue = 0;
		Territory bestFromTerritory = null;
		Territory bestToTerritory = null;
		for(Territory loopFromTerritory : game.getWorld().getTerritories()) {
			if((loopFromTerritory.getOwner() == game.getCurrentPlayer())) {
				if(CnA.borderDistance(loopFromTerritory) > 0) {
					for(Territory loopToTerritory : loopFromTerritory.getNeighbours()) {
						if(game.canMoveUnits(loopFromTerritory, loopToTerritory, 1)) {							
							int loopValue = moveUnitValue(loopFromTerritory, loopToTerritory);
							if (loopValue > bestValue) {
								bestValue = loopValue;
								bestFromTerritory = loopFromTerritory;
								bestToTerritory = loopToTerritory;
							}
						}					
					}
				}
			}
		}
		if((bestFromTerritory == null) || (bestToTerritory == null)) {
			return false;
		}
		int numUnits = game.maxUnitsToMove(bestFromTerritory, bestToTerritory);
		if(!game.canMoveUnits(bestFromTerritory, bestToTerritory, numUnits))
		{
			return false;
		}
		doer.moveUnits(bestFromTerritory, bestToTerritory, numUnits);
		return true;
	}
	
	private int moveUnitValue(Territory source, Territory target) {
		int sourceInterior = CnA.borderDistance(source);
		int targetInterior = CnA.borderDistance(target);
		if(targetInterior >= sourceInterior) {
		return 0;
		}
		int numUnits = game.maxUnitsToMove(source, target);
		if(targetInterior == 0) {
			return numUnits * CnA.neighbouringEnemyCount(target);
		}
		return numUnits;
	}
	
	
	private boolean chooseTurnType() {
		if(!game.canChooseTurnType()) {
			return false;
		}
		//how many units are stuck inside
		int insideArmies = 0;
		for(Territory territory: game.getWorld().getTerritories()) {
			if(territory.getOwner() == game.getCurrentPlayer()) {
				if(CnA.borderDistance(territory) > 0) {
					insideArmies += territory.getUnitCount() - 1;
				} else if (CnA.surroundDifference(territory) > 0) {
					//We should attack if we could take offside territories.
					return false;
				}
			}
		}
		
		//The units we could attack with this turn.
		// We try and double this number by moving or drafting.
		int attackArmies = CnA.countNumUnits(game.getCurrentPlayer()) - CnA.countNumTerritories(game.getCurrentPlayer()) - insideArmies;

		if(insideArmies > Math.max(attackArmies, Game.POST_ACTION_MOVE_COUNT + game.calculateConscriptedUnits())) {
			doer.chooseTurnType(TurnPhase.TurnUnlimitedMovement);
			return true;
		}
		if(attackArmies < game.calculateConscriptedUnits()) {
			doer.chooseTurnType(TurnPhase.TurnConscriptUnitDeployment);
			return true;
		}
		if(insideArmies > Math.max(attackArmies, Game.POST_ACTION_MOVE_COUNT)) {
			doer.chooseTurnType(TurnPhase.TurnUnlimitedMovement);
			return true;
		}
		return false;
	}
	
	private boolean executeAttackPlan(boolean raiseExceptions) {
		if(plan.size() < 2) {
			throw new IllegalArgumentException("No plan!");
		}
		
		//Creating List for use in planned attack
		ArrayList<Territory> planTerritoryAttack = new ArrayList<Territory>();
		planTerritoryAttack.add(plan.get(0)); //It wants to attack from 0 all the time
		
		if(!game.canAttack(planTerritoryAttack, plan.get(1),  game.maxUnitsToAttack(planTerritoryAttack, plan.get(1)))) {
			if(raiseExceptions) {
				throw new IllegalArgumentException("Plan failed! Can't attack.");
			}
			if(log != null) {
				log.println("Plan failed: can't attack");
			}
			return false;
		}

		if(!isSensiblePlan(plan)) {
			if(raiseExceptions) {
				throw new IllegalArgumentException("Plan failed! not sensible. Left" + CnA.numLeft(plan));
			}
			if(log != null) {
				log.println("Plan failed: not sensible");
			}
			return false;
		}
		if(log != null) {
			log.println("Attacking " + plan.get(1).getName() + " just as planned.");
		}
		doer.attack(planTerritoryAttack, plan.get(1), game.maxUnitsToAttack(planTerritoryAttack, plan.get(1)), false, false);
		if(plan.get(1).getOwner() != game.getCurrentPlayer()) {
			plan.clear();
		} else if(plan.size() == 2) {
				plan.clear();
		} else {
			plan.remove(0);
		}
		//Clearing List after use just in case
		planTerritoryAttack.clear();
		
		return true;
	}
	
	boolean isSensiblePlan(List<Territory> attackPlan) {
		if(attackPlan.size() < 2) {
			throw new IllegalArgumentException();
		}
		int numLeft = CnA.numLeft(attackPlan);
		if(numLeft <= 0) {
			// is this a good suicide mission?
			if(!isOurThreat(attackPlan.get(attackPlan.size() - 1))){
				return false;
			}
			numLeft += attackPlan.get(attackPlan.size() - 1).getUnitCount() - 7;
			if(numLeft <= 0) {
				return false;
			}
			
		}
		for(Territory territory: attackPlan) {
			for(Territory neighbour: territory.getNeighbours()) {
				if(neighbour.getOwner() != game.getCurrentPlayer()) {
					if(neighbour.getUnitCount() > numLeft) {
						if(!attackPlan.contains(neighbour)) {
							return false;
						}
					}
				}
			}
		}
		return numLeft > 0;
	}
	
	boolean isOurThreat(Territory territory) {
		boolean us = false;
		for(Territory neighbour: territory.getNeighbours()) {
			Player owner = neighbour.getOwner();
			if(owner == game.getCurrentPlayer()) {
				us = true;
			} else if(owner != territory.getOwner()) {
				return false;
			}
		}
		return us;
	}
	
	
	private boolean conquerContinents() {
		plan.clear();
		int planValue = -57;
		List<TerritoryGroup> continents = new ArrayList<TerritoryGroup>(game.getWorld().getTerritoryGroups());
		Collections.sort(continents, new Comparator<TerritoryGroup>() {
			public int compare(TerritoryGroup c1, TerritoryGroup c2) {
				return CnA.enemyTerritoryCount(c1) - CnA.enemyTerritoryCount(c2);
			}
		} );
		for(TerritoryGroup continent: continents) {
			if(CnA.enemyTerritoryCount(continent) > 0) {
				List<Territory> newPlan = new ArrayList<Territory>();
				int mostUnits = 1;
				for(Territory start: game.getWorld().getTerritories()) {
					if(start.getOwner() == game.getCurrentPlayer() && start.getUnitCount() > mostUnits) {
						List<Territory> invPlan = invasionPlan(start, continent);
						if(invPlan != null) {
							newPlan = invPlan;
						}
					}
				}
				if(newPlan.size() > 0)
				{
					int value = newPlan.size();
					value += CnA.neighbouringEnemyCount(newPlan.get(newPlan.size() - 1));
					if(newPlan.size() == CnA.enemyTerritoryCount(continent)) {
						value += 100 * continent.getBonusValue();
					}
					if(value > planValue) {
						planValue = value;
						plan = newPlan;
					}
				}
			}
		}
		if(plan.size() == 0)
		{
			return false;
		}
		if(log != null) {
			log.println("Invasion plan! size:" + plan.size() + " value: " + planValue);
			for(Territory terr: plan) {
				log.print(terr.getName() + " ");
			}
			log.println();
		}
		return executeAttackPlan(true);
	}
	
	private List<Territory> invasionPlan(Territory source, final TerritoryGroup continent) {
		boolean wayIn = false;
		for(Territory neighbour: source.getNeighbours()) {
			if(continent.getTerritories().contains(neighbour) && neighbour.getOwner() != game.getCurrentPlayer()) {
				wayIn = true;
			}
		}
		if(!wayIn) {
			return null;
		}
		class ContinentHeuristic implements PathHeuristic {
			public int pathValue(List<Territory> path) {
				if(CnA.numLeft(path) <= 0) {
					return -1;
				}
				int size = path.size();
				if(size < CnA.enemyTerritoryCount(continent))
				{
					return size;
				}
				return size + CnA.neighbouringEnemyCount(path.get(path.size() - 1));
			}
			public int pathLastValue(List<Territory> path, int penultimateValue) {
				throw new IllegalStateException();
			}
			public boolean IsNodeValid(Territory node, List<Territory> path,int index) {
				if(index < 0 || index >= path.size()) {
					throw new IllegalArgumentException();
				}
				if(node.getOwner() == path.get(0).getOwner()) {
					return false;
				}
				if(!continent.getTerritories().contains(node)) {
					return false;
				}
				if(path.contains(node) && path.indexOf(node) <= index) {
					return false;
				}
				
				return true;
			}
		}
		ContinentHeuristic heuristic = new ContinentHeuristic();
		List<Territory> invPlan =  CnA.depthFirstPathSearch(source, heuristic);
		
		if(!isSensiblePlan(invPlan))
		{
			return null;
		}
		
		return invPlan;
	}
	
	public void setupLog() {
		try {
			log = new PrintWriter(new FileWriter("AIlog.txt")); // Hard-coded paths rule.
		} catch (IOException e) {
			//
		}
	}
}