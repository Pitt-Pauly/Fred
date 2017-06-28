package fred.model;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public abstract class Territory implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
	private Player owner;
	private int unitCount;
	private final String name;
	private final List<Territory> neighbours = new ArrayList<Territory>();
	
	public Territory(String name) {
        if (name == null) {
            throw new NullPointerException();
        }
        if (name.length() == 0) {
            throw new IllegalArgumentException();
        }
        this.name = name;
	}
	
	public static Territory factory(String name, Game.Type gameType) {
		switch(gameType) {
		case Basic:
			return new BasicTerritory(name);
		case Advanced:
			return new AdvancedTerritory(name);
		default:
			throw new IllegalArgumentException();	
		}
	}
	
	abstract public Game.Type getGameType();
	
	public Player getOwner() {
		return owner;
	}
	
	public void setOwner(Player owner) {
		if (owner == null) {
			throw new NullPointerException();
		}
		this.owner = owner;
	}
	
	public int getUnitCount() {
		return unitCount;
	}
                	
	public void setUnitCount(int unitCount) {
		if (unitCount <= 0) {
			throw new IllegalArgumentException("" + unitCount);
		}
		this.unitCount = unitCount;
	}
      
	public String getName() {
		return this.name;
	}
	
	public List<Territory> getNeighbours() {
		return neighbours;
	}
	
	public void addNeighbour(Territory neighbour) {
		if (neighbour == null) {
			throw new NullPointerException();
		}
		if (neighbour == this) {
			throw new IllegalArgumentException("territory cannot have itself as neighbour");
		}
        if (!neighbours.contains(neighbour)) {
            neighbours.add(neighbour);
        }
        if (!neighbour.getNeighbours().contains(this)) {
            neighbour.getNeighbours().add(this);
        }
	}
	
	public boolean isConnected(Territory target) {
		if (target == null) {
			throw new NullPointerException();
		}
		return neighbours.contains(target);
	}

	// NOTE - hashcode and equals do not compare neighbours or owners to avoid stack overflow
	
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + unitCount;
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
        Territory other = (Territory) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        }
        else if (!name.equals(other.name))
            return false;
        if (unitCount != other.unitCount)
            return false;
        return true;
    }
	

}
