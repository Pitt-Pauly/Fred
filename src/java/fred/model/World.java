package fred.model;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class World implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
	private final List<TerritoryGroup> territoryGroups;
	private final List<Territory> territories;
	
	public World(List<Territory> territories, List<TerritoryGroup> territoryGroups) {
		if (territories == null || territoryGroups == null) {
			throw new NullPointerException();
		}
		this.territories = territories;
		this.territoryGroups = territoryGroups;
	}
	
	public List<Territory> getTerritories() {
		return territories;
	}
	
	public List<TerritoryGroup> getTerritoryGroups() {
		return territoryGroups;
	}
	
	public TerritoryGroup findGroup(Territory territory) {
		for (TerritoryGroup group: territoryGroups) {
			if(group.getTerritories().contains(territory)) {
				return group;
			}
		}
		return null;
	}
	
	public Territory getTerritoryByName(String territoryName) {
	    for (Territory territory : getTerritories()) {
	        if (territoryName.equals(territory.getName())) {
	            return territory;
	        }
	    }
	    throw new IllegalArgumentException("No territory: " + territoryName);
	}
	
	//maybe delete. Maybe slow to call getPathWayCost twice so duplicating this code is good.
	protected boolean hasPathway(Territory source, Territory target) {
		return getPathwayCost(source, target) > 0;
	}
	
	
	class DijkstraVertex {
	    public static final int NO_PATH = 1000000;
	    final Territory territory;
	    int cost = NO_PATH;
	    
	    public DijkstraVertex(Territory territory) {
	        this.territory = territory;
	    }
	}
	
	public int getPathwayCost(Territory source, Territory target) {
		if (source == null || target == null || source.getOwner() == null || target.getOwner() == null) {
			throw new NullPointerException();
		}
		if (source == target) {
			throw new IllegalArgumentException("No pathway between the same territory");
		}
		if (source.getOwner() != target.getOwner()) {
			throw new IllegalArgumentException("Different owners");
		}

		List<DijkstraVertex> vertices = new ArrayList<DijkstraVertex>();
		for (Territory territory : territories) {
		    DijkstraVertex vertex = new DijkstraVertex(territory);
		    if (source == territory) {
		        vertex.cost = 0;
		    }
		    vertices.add(vertex);
		}
		
		while (!vertices.isEmpty()) {
		    DijkstraVertex currentVertex = getNextVertex(vertices);
		    if (DijkstraVertex.NO_PATH == currentVertex.cost) {
		        return 0;
		    }
		    if (currentVertex.territory == target) {
		        return currentVertex.cost;
		    }
		    
		    if (currentVertex.territory.getOwner() != source.getOwner()) {
		        // Drop the vertex and continue
		        continue;
		    }
		    
            for (Territory neighbour : currentVertex.territory.getNeighbours()) {
		        DijkstraVertex neighbourVertex = findVertexForTerritory(vertices, neighbour);
		        if (neighbourVertex != null && neighbourVertex.cost > currentVertex.cost + 1) {
		            neighbourVertex.cost = currentVertex.cost + 1;
		        }
		    }
		}
		return 0;
	}

	private DijkstraVertex getNextVertex(List<DijkstraVertex> queue) {
	    DijkstraVertex result = null;
	    for (DijkstraVertex current : queue) {
	        if (result == null || current.cost < result.cost) {
	            result = current;
	        }
	    }
	    queue.remove(result);
        return result;
    }

    private DijkstraVertex findVertexForTerritory(List<DijkstraVertex> vertices, Territory territory) {
	    for (DijkstraVertex current : vertices) {
	        if (territory == current.territory) {
	            return current;
	        }
	    }
        return null;
    }

    public List<TerritoryGroup> getFullyOwnedTerritoryGroups(Player player) {
		if (player == null) {
			throw new NullPointerException();
		}
		List<TerritoryGroup> result = new ArrayList<TerritoryGroup>();
		for (TerritoryGroup territoryGroup : territoryGroups) {
			if (territoryGroup.getOwner() == player) {
				result.add(territoryGroup);
			}
		}
		return result;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((territories == null) ? 0 : territories.hashCode());
        result = prime * result + ((territoryGroups == null) ? 0 : territoryGroups.hashCode());
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
        World other = (World) obj;
        if (territories == null) {
            if (other.territories != null)
                return false;
        }
        else if (!territories.equals(other.territories))
            return false;
        if (territoryGroups == null) {
            if (other.territoryGroups != null)
                return false;
        }
        else if (!territoryGroups.equals(other.territoryGroups))
            return false;
        return true;
    }
}
