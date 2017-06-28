package fred.middle;

import fred.model.GameDoer;
import fred.model.Territory;
import fred.model.GameInfo;

public class MoveAction extends Action {
    
    private static final long serialVersionUID = 1L;

    private String hereName;
	private String thereName;
	private int numUnits;
	
	protected MoveAction(Territory here, Territory there, int numUnits) {
		this.hereName = here.getName();
		this.thereName = there.getName();
		this.numUnits = numUnits;
	}
		
	@Override
	public void perform(GameDoer game, GameInfo refGame) {
		game.moveUnits(refGame.getWorld().getTerritoryByName(hereName), refGame.getWorld().getTerritoryByName(thereName),  numUnits);
	}
	
	@Override
    public boolean canDo(GameInfo refGame) {
		return refGame.canMoveUnits(refGame.getWorld().getTerritoryByName(hereName), refGame.getWorld().getTerritoryByName(thereName),  numUnits);
	}
	
	protected String getHereName() {
		return hereName;
	}
	
	protected String getThereName() {
		return thereName;
	}

	protected int getNumUnits() {
		return numUnits;
	}

	/*@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MoveAction))  {
			return false;
		}
		MoveAction other = (MoveAction) obj;
		return (hereName == other.hereName) && (thereName == other.thereName) && (numUnits == other.numUnits);
	}*/
}
