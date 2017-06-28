package fred.middle;

import fred.model.*;
import fred.model.GameInfo;

public class DeployAction extends Action {
    
    private static final long serialVersionUID = 1L;

    private String hereName;
	private int numUnits;
	
	protected DeployAction(Territory here, int numUnits) {
		this.hereName = here.getName();
		this.numUnits = numUnits;
	}
		
	@Override
	public void perform(GameDoer game, GameInfo refGame) {
		game.distributeFreeUnits(refGame.getWorld().getTerritoryByName(hereName), numUnits);
	}
	
	@Override
    public boolean canDo(GameInfo refGame) {
		return refGame.canDistributeFreeUnits(refGame.getWorld().getTerritoryByName(hereName), numUnits);
	}
	
	protected String getHereName() {
		return hereName;
	}

	protected int getNumUnits() {
		return numUnits;
	}

	/*@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DeployAction))  {
			return false;
		}
		DeployAction other = (DeployAction) obj;
		return (hereName == other.hereName) && (numUnits == other.numUnits);
	}*/

}
