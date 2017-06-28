package fred.model;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

public class AttackRecord implements Serializable {
    
    private static final long serialVersionUID = 1L;

    public final String attackerName;
	public final String defenderName;
	public final ArrayList<String> fromName;
	public final String toName;
	public String CardTerritoryName;
	
	public final ArrayList<Integer> initialAttackers;
	public final int initialDefenders;
	private boolean success;
	private boolean finished;
	private int survivors;
	private List<AttackRound> rounds;
	public Boolean withdraw = false;
	public List<Integer> returnedUnits;
	
	
	public boolean isFinished() {
		return finished;
	}

	public boolean isSuccess() {
		return success;
	}

	
	public int getSurvivors() {
		return survivors;
	}
	
	public int getRounds() {
		return rounds.size();
	}
	
	//Needless to say, round goes from 0 to getRounds() - 1.
	public int getAttackersAfterRound(int round) {
		return getRound(round).attackUnits;
	}
	
	public int getDefendersAfterRound(int round) {
		return getRound(round).defenderUnits;
	}
	
	public List<Integer> getAttackRolls(int round) {
		return getRound(round).attackerRolls;
	}
	
	public List<Integer> getDefenceRolls(int round) {
		return getRound(round).defenderRolls;
	}
	
	public String getCardTerritoryName() {
		return CardTerritoryName;
	}

	public void setCardTerritoryName(String cardTerritoryName) {
		CardTerritoryName = cardTerritoryName;
	}
	
	public void withdraw(List<Integer> returned) {
		this.returnedUnits = returned;
		withdraw = true;
	}
	
	public Boolean isWithdrawn() {
		return withdraw;
	}
	
	public List<Integer> returnedUnits() {
		return returnedUnits;
	}
	
	//Will have methods here to convert to or construct from raw data for replay/communications
	
	protected void setFinished(boolean finished) {
		this.finished = finished;
	}

	protected void setSuccess(boolean success) {
		this.success = success;
	}

	protected void setSurvivors(int survivors) {
		this.survivors = survivors;
	}

	protected AttackRecord( Player attacker, Player defender, List<Territory> fromSource, 
			Territory to, ArrayList<Integer> attackers, int defenders) {
		this.attackerName = attacker.getName();
		this.defenderName = defender.getName();
		
		
		//Creating List<String> from List<Territory>
		ArrayList<String> fromd = new ArrayList<String>();
		for (Territory source: fromSource) {
			fromd.add(source.getName());
		}

		this.fromName = fromd;
		this.toName = to.getName();
		
		this.initialAttackers = attackers;
		this.initialDefenders = defenders;
		this.success = false;
		this.finished = false;
		this.survivors = -1;
		this.rounds = new ArrayList<AttackRound>();
		if(initialAttackers.size() == 0) {
			throw new IllegalArgumentException();
		}
		
	}
	
	protected void addRound(int attackers, int defenders, List<Integer> attackRolls, List<Integer> defenceRolls) {
		if(attackRolls.size() > 3 || defenceRolls.size() > 2) {
			throw new IllegalArgumentException("too many dice");
		}
		
		rounds.add(new AttackRound(attackers, defenders, attackRolls, defenceRolls));
	}
	
	private AttackRound getRound(int round) {
		if(round >= rounds.size() || round < 0) {
			throw new IllegalArgumentException();
		}
		return rounds.get(round);
	}
	
	class AttackRound implements Serializable {
		private static final long serialVersionUID = 1L;
		public final int attackUnits; //these are the numbers after the round is resolved
		public final int defenderUnits;
		public final List<Integer> attackerRolls;
		public final List<Integer> defenderRolls;
		
		public AttackRound(int attackUnits, int defenderUnits,
				List<Integer> attackerRolls, List<Integer> defenderRolls) {
			this.attackUnits = attackUnits;
			this.defenderUnits = defenderUnits;
			this.attackerRolls = attackerRolls;
			this.defenderRolls = defenderRolls;
		}
		
	}
}
