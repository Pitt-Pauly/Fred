package fred.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TerritoryGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Territory> territories;
    private final int bonusValue;
    private final String name;

    public TerritoryGroup(String name, List<Territory> territories, int bonusValue) {
        if (territories == null || name == null) {
            throw new NullPointerException();
        }
        if (territories.size() == 0) {
            throw new IllegalArgumentException("no territories");
        }
        this.territories = territories;
        this.bonusValue = bonusValue;
        this.name = name;
    }

    public TerritoryGroup(String name, int bonusValue) {
        if (name == null) {
            throw new NullPointerException();
        }
        this.territories = new ArrayList<Territory>();
        this.bonusValue = bonusValue;
        this.name = name;
    }

    public TerritoryGroup(String name, Territory[] territories, int bonusValue) {
        this(name, Arrays.asList(territories), bonusValue);
    }

    public boolean hasOwner() {
        return getOwner() != null;
    }

    public Player getOwner() {
        Player owner = null;
        for (Territory territory : territories) {
            if (territory.getOwner() == null) {
                return null;
            }
            if (owner != null && owner != territory.getOwner()) {
                return null;
            }
            owner = territory.getOwner();
        }

        return owner;
    }

    public int getBonusValue() {
        return bonusValue;
    }

    public List<Territory> getTerritories() {
        return territories;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + bonusValue;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((territories == null) ? 0 : territories.hashCode());
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
        TerritoryGroup other = (TerritoryGroup) obj;
        if (bonusValue != other.bonusValue)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        }
        else if (!name.equals(other.name))
            return false;
        if (territories == null) {
            if (other.territories != null)
                return false;
        }
        else if (!territories.equals(other.territories))
            return false;
        return true;
    }

}
