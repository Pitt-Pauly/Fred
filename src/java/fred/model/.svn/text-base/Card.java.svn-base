package fred.model;

import java.io.Serializable;

public class Card implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private final CardType type;
    private final Territory territory;

    public Card(Territory territory, CardType type) {
        if (territory == null || type == null) {
            throw new NullPointerException();
        }
        this.territory = territory;
        this.type = type;
    }

    public Territory getTerritory() {
        return territory;
    }

    public CardType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((territory == null) ? 0 : territory.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        Card other = (Card) obj;
        if (territory == null) {
            if (other.territory != null)
                return false;
        }
        else if (!territory.equals(other.territory))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        }
        else if (!type.equals(other.type))
            return false;
        return true;
    }

}
