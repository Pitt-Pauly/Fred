package fred.middle;

import java.util.ArrayList;
import java.util.List;

import fred.model.GameDoer;
import fred.model.Territory;
import fred.model.AttackRecord;
import fred.model.GameInfo;

public class AttackAction extends Action {
    
    private static final long serialVersionUID = 1L;

	private List<String> hereName;
	private String thereName;
	private ArrayList<Integer> numUnits;
	private AttackRecord record;
	private boolean single;
	
	protected AttackAction(List<Territory> here, Territory there, ArrayList<Integer> numUnits, boolean single) {
		// Converting from List<Territory> to List<String>
		List<String> hereList = new ArrayList<String>();
		for (Territory currentHere: here) {
			hereList.add(currentHere.getName());
		}
		
		this.hereName = hereList;
		this.thereName = there.getName();
		this.numUnits = numUnits;
		this.single = single;
		record = null;
	}
	
	protected AttackAction(AttackRecord record) {
		this.hereName = record.fromName;
		this.thereName = record.toName;
		this.numUnits = record.initialAttackers;
		this.record = record;
	}
		
	@Override
	public void perform(GameDoer game, GameInfo refGame) {
		if(record != null) {
			if(record.initialAttackers.isEmpty()) {
				throw new IllegalStateException();
			}
			game.performAttack(record);
			return;
		}
		
		// Converting from List<String> to List<Territory>
		ArrayList<Territory> hereTerritory = new ArrayList<Territory>();
		for (String currentHere: hereName) {
			hereTerritory.add(refGame.getWorld().getTerritoryByName(currentHere));
		}
		record = game.attack(hereTerritory, refGame.getWorld().getTerritoryByName(thereName), numUnits, single, false);
		
		if(record != null && record.initialAttackers.isEmpty()) {
			throw new IllegalStateException();
		}
	}
	
	@Override
    public boolean canDo(GameInfo refGame) {
		if(record != null) {
			refGame.canPerformAttack(record);
		}
		// Converting from List<String> to List<Territory>
		List<Territory> hereTerritory = new ArrayList<Territory>();
		for (String currentHere: hereName) {
			hereTerritory.add(refGame.getWorld().getTerritoryByName(currentHere));
		}
		return refGame.canAttack(hereTerritory, refGame.getWorld().getTerritoryByName(thereName),  numUnits);
	}
	
	protected List<String> getHereName() {
		return hereName;
	}
	
	protected String getThereName() {
		return thereName;
	}

	protected ArrayList<Integer> getNumUnits() {
		return numUnits;
	}

	protected AttackRecord getRecord() {
		return record;
	}
}
