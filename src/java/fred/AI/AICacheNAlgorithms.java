package fred.AI;

import fred.model.*;

import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class AICacheNAlgorithms {
	private GameInfo game;
	private Map<Territory, Integer> borderDistance = null;
	
	private Map<Player, Integer> numUnits = null;
	private Map<Player, Integer> numTerritories = null;
	private Map<Player, Integer> biggestArmy = null;

	//private Territory origin;
	//private Map<Territory, Integer> pathDistance = null;
	
	protected AICacheNAlgorithms(GameInfo game) {
		this.game = game;
	}
	
	public void invalidate(){
		borderDistance = null;
		numUnits = null;
		numTerritories = null;
		biggestArmy = null;
		//origin = null;
		//pathDistance = null;
	}

	public int borderDistance(Territory here) {
		if(borderDistance == null){			
			borderDistance = new HashMap<Territory, Integer>();
			for(Territory territory: game.getWorld().getTerritories())
			{
				boolean friendly = (territory.getOwner() == game.getCurrentPlayer());
				for(Territory neighbour: territory.getNeighbours()) {
					if((neighbour.getOwner() == game.getCurrentPlayer()) != friendly) {
						borderDistance.put(territory, 0);
						break;
					}
				}
			}
			boolean newAdded = true;
			for(int i = 0;newAdded;i++) {
				newAdded = false;
				for(Territory territory: new HashSet<Territory>(borderDistance.keySet())) {
					boolean friendly = (territory.getOwner() == game.getCurrentPlayer());
					if (borderDistance.get(territory) == i) {
						for(Territory neighbour: territory.getNeighbours()) {
							if((neighbour.getOwner() == game.getCurrentPlayer()) == friendly) {
								if(!borderDistance.containsKey(neighbour)) {
									borderDistance.put(neighbour, i+ 1);
									newAdded =true;
								}
							}
						}
					}
				}
			}
		}
		
		if(borderDistance.containsKey(here)){
			return borderDistance.get(here);
		}
		return -1;
	}
	
	
	public int countNumTerritories(Player player) {
		if(numTerritories == null) {
			doPlayerCounts();
		}
		return numTerritories.get(player);
	}
	
	public int countNumUnits(Player player) {
		if(numUnits == null) {
			doPlayerCounts();
		}
		return numUnits.get(player);
	}

	public int biggestArmy(Player player) {
		if(biggestArmy == null) {
			doPlayerCounts();
		}
		return biggestArmy.get(player);
	}
	
	private void doPlayerCounts() {
		numUnits = new HashMap<Player, Integer>();
		numTerritories = new HashMap<Player, Integer>();
		biggestArmy = new HashMap<Player, Integer>();
		for(Player player : game.getPlayers()) {
			int unitCount = 0;
			int territoryCount = 0;
			int army = 0;
			for(Territory territory : game.getWorld().getTerritories()) {
				if(player == territory.getOwner()) {
					unitCount += territory.getUnitCount();
					territoryCount++;
					army = Math.max(army, territory.getUnitCount());
				}
					
			}
			numUnits.put(player, unitCount);
			numTerritories.put(player, territoryCount);
			biggestArmy.put(player, army);			
		}		
	}
	
	public int enemyTerritoryCount(TerritoryGroup continent) {
		int count = 0;
		for(Territory territory: continent.getTerritories()) {
			if(territory.getOwner() != game.getCurrentPlayer()) {
				count++;
			}
		}
		return count;
	}
	
	public int friendlyTerritoryCount(TerritoryGroup continent) {
		int count = 0;
		for(Territory territory: continent.getTerritories()) {
			if(territory.getOwner() == game.getCurrentPlayer()) {
				count++;
			}
		}
		return count;
	}
	
	public int neighbouringEnemyCount(Territory source) {
		int count = 0;
		for(Territory neighbour : source.getNeighbours()) {
			if(neighbour.getOwner() != game.getCurrentPlayer()) {
				count += neighbour.getUnitCount();				
			}
		}
		return count;
	}
	
	
	
	public int numLeft(List<Territory> plan) {
		int numLeft = -1;
		for(Territory territory: plan) {
			if(territory == plan.get(0)) {
				numLeft = territory.getUnitCount();
			}
			else {
				numLeft -= territory.getUnitCount();
				if(territory.getOwner() == plan.get(0).getOwner()) {
					numLeft -= -1000;
				}
			}
		}
		return numLeft;
	}
	
	public int surroundDifference(Territory surrounder) {
		if(surrounder.getOwner() != game.getCurrentPlayer()) {
			throw new IllegalArgumentException();
		}
		Set<Territory> surrounders = new HashSet<Territory>();
		Set<Territory> surrounded = new HashSet<Territory>();
		Set<Territory> pounded = new HashSet<Territory>();
		surrounders.add(surrounder);
		for(Territory neighbour :surrounder.getNeighbours()) {
			if(neighbour.getOwner() != surrounder.getOwner()) {
				surrounded.add(neighbour);
			}
		}
		while(!surrounded.isEmpty()) {
			Set<Territory> nextVictims = new HashSet<Territory>();
			for(Territory next: surrounded) {
				pounded.add(next);
				for(Territory neighbour : next.getNeighbours()) {
					if(neighbour.getOwner() == surrounder.getOwner()) {
						surrounders.add(neighbour);
					} else if (!pounded.contains(neighbour) && !surrounded.contains(neighbour)){
						nextVictims.add(neighbour);
					}
				}
			}
			surrounded = nextVictims;				
		}
		int difference = 0;
		for(Territory ours: surrounders) {
			difference += ours.getUnitCount() - 1;
		}
		for(Territory theirs: pounded) {
			difference -= theirs.getUnitCount();
		}
		return difference;
	}
	
	/*private void dijkstra() {
		//Will do path-finding when I need it
	}*/
	
	
	public List<Territory> depthFirstPathSearch(Territory source, PathHeuristic heuristic) {
		List<Territory> path = new ArrayList<Territory>();
		path.add(source);
		nextPath(path, heuristic);
		List<Territory> bestPath = new ArrayList<Territory>(path);
		int bestValue = 0;
		if(bestPath.size() > 0) {
			bestValue = heuristic.pathValue(bestPath);
		}
		while(path.size() > 0) {
			int value = heuristic.pathValue(path);
			if(value > bestValue)
			{
				bestPath = new ArrayList<Territory>(path);
				bestValue = value;
			}
			nextPath(path, heuristic);
		}
		if(bestValue == 0) {
			return null;
		}
		if(bestValue < 0 || bestValue != heuristic.pathValue(bestPath))
		{
			int i = heuristic.pathValue(bestPath);
			i += 0;
			i += 0;
		}
		
		return bestPath;
	}
	// for depth-first search
	private void nextPath(List<Territory> path, PathHeuristic heuristic) {
		Territory next = null;
		int branchPoint = -1;
		//extend only positive value paths
		int start = path.size() -1;
		if(heuristic.pathValue(path) <= 0) {
			start--;
		}
		
		for(int i = start; i >= 0; i--) {
			boolean pastCurrent = (i + 1 == path.size());
			for(Territory neighbour: path.get(i).getNeighbours()) {
				if(pastCurrent) {
					if(heuristic.IsNodeValid(neighbour, path, i)) {
						branchPoint = i;
						next = neighbour;
						break;
					}
				} else {
					if(neighbour == path.get(i + 1)) {
						pastCurrent = true;
					}
				}
			}
		}
		if(branchPoint == -1) {
			path.clear();
			return;
		}
		if(branchPoint < path.size() - 1) {
			for(int index = path.size() - 1; index > branchPoint;index--) {
				path.remove(index);
			}			
		}
		path.add(next);
	}
}
