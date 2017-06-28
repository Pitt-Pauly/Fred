package fred.middle;

import java.util.ArrayList;
import java.util.List;

import fred.datahandling.PackedGame;
import fred.model.Game;
import fred.model.TurnPhase;
import java.io.Serializable;

public class Replay implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<ReplayTurn> turns;
	int replayTurn;
	int replayAction;
	PackedGame start;
	
	class ReplayTurn implements Serializable {
		private static final long serialVersionUID = 1L;
		int turnNo;
		List<Action> actions;		
	}
	
	public Replay(Game game) {
		if(game == null) {
			throw new NullPointerException();
		}
		turns = new ArrayList<ReplayTurn>();
		startTurn(game);	
	}
	
	public void addAction(Action action, Game gameAfter) {
		if(gameAfter == null) {
			throw new NullPointerException();
		}
		if(action == null) {
			throw new NullPointerException();
		}
		turns.get(turns.size() - 1).actions.add(action);
		if(action instanceof EndPhaseAction) {
			if(((EndPhaseAction) action).getEndPhase() != TurnPhase.SetupUnitDeployment) {
				if (!((EndPhaseAction) action).isSamePlayer() || turns.size() == 1) {
					startTurn(gameAfter);					
				}
			}
		}
	}
	
	private void startTurn(Game game) {
		ReplayTurn newTurn = new ReplayTurn();
		if(turns.size() == 0) {
			newTurn.turnNo = -1;
			start = new PackedGame(game);
		}
		else {
			newTurn.turnNo = turns.get(turns.size() - 1).turnNo + 1;
		}
		newTurn.actions = new ArrayList<Action>();
		turns.add(newTurn);
	}
	
	public Action getLastAction() {
		List<Action> actions =  turns.get(turns.size() - 1).actions;
		if (actions.size() > 0) {
			return actions.get(actions.size() - 1);
		}
		if(turns.size() == 1) {
			return null;
		}
		
		actions =  turns.get(turns.size() - 2).actions;
		if (actions.size() > 0) {
			return actions.get(actions.size() - 1);
		}
		return null;
	}
	
	//Replace this with something more useful once replay mechanism in place
	protected Game initialPosition (){
		replayTurn = 0;
		replayAction = 0;
		return start.unPack();
	}
	
	private int convertTurnNoToIndex(int turnNo) {
		for(int i=0; i < turns.size(); i++) {
			if(turns.get(i).turnNo == turnNo) {
				return i;
			}
		}
		throw new IllegalStateException("No turn number" + turnNo);
	}
	
	protected Action nextAction() {
		if(turns.size() == replayTurn || turns.get(replayTurn).actions.size() == 0) {
			return null;
		} else if(turns.size() < replayTurn) {
			throw new IllegalStateException("what?turn" + replayTurn + "action" + replayAction);
		} else if(turns.size() == 1 + replayTurn && replayAction == turns.get(replayTurn).actions.size()) {
			return null;
		}
		
		Action action = turns.get(replayTurn).actions.get(replayAction);
		replayAction++;
		if(turns.get(replayTurn).actions.size() <= replayAction) {
			if(replayTurn < turns.size() - 1) {
				replayTurn++;			
				replayAction = 0;
			}
		} 
		return action;
	}
	
	public int turnNo() {
		return turns.get(turns.size() - 1).turnNo;
	}
	
	public int replayTurnNo() {
		return turns.get(Math.min(turns.size() - 1, replayTurn)).turnNo;
	}
	
	public Game lastPosition() {
		Game game = initialPosition();
		Action next = nextAction();
		 while(next != null) {
			next.perform(game, game);
			next = nextAction();
		}
		replayTurn = turns.size() - 1;
		replayAction = turns.get(replayTurn).actions.size();
		if(replayTurn >= turns.size() || replayAction > turns.get(replayTurn).actions.size()) {
			 throw new IllegalStateException("Missed AT t:" + replayTurn + "A: " + replayAction + "turns size " + turns.size());
		 }
		return game;
	}
	
	private Game reposition(int newTurn, int actionNo) {
		Game game = initialPosition();
		 while(actionNo > replayAction || replayTurn < newTurn) {
			Action next = nextAction();
			if(next == null) {
				String exString = "Aiming for turn: " + newTurn + "A: " + actionNo +"but failed AT t:" + replayTurn + "A: " + replayAction + "turns size " + turns.size() + "last turn size:" + turns.get(turns.size() - 1).actions.size();
				throw new IllegalStateException(exString);
			}
			next.perform(game, game);
		}
		 if(game == null) {
			 throw new NullPointerException("I don't understand");
		 }
		 if(actionNo != replayAction || replayTurn != newTurn || replayTurn >= turns.size()) {
			 throw new IllegalStateException("Missed! Aiming for turn: " + newTurn + "A: " + actionNo +"but failed AT t:" + replayTurn + "A: " + replayAction + "turns size " + turns.size());
		 }
		return game;
	}
	
	public Game backAction() {
		if(turns.size() <= replayTurn) {
		 if(turns.size() < replayTurn || replayAction > 0) {
			throw new IllegalStateException("what?turn" + replayTurn + "action" + replayAction);
		 }
		}
		replayAction--;
		if(replayAction < 0) {
			replayTurn--;
			if(replayTurn < 0) {
				replayTurn = 0;
				replayAction = 0;
			} else {
				replayAction = turns.get(replayTurn).actions.size() - 1;
			}
		}
		
		return reposition(replayTurn, replayAction);
	}
	
	public Game forwardAction() {
		if(replayTurn >= turns.size() || replayAction > turns.get(replayTurn).actions.size()) {
			 throw new IllegalStateException("Missed AT t:" + replayTurn + "A: " + replayAction + "turns size " + turns.size());
		 }
		replayAction++;
		if(turns.get(replayTurn).actions.size() <= replayAction) {
			replayAction = 0;
			replayTurn++;
			if(turns.size() == replayTurn) {
				replayTurn--;
				replayAction = turns.get(replayTurn).actions.size();
			}
		}
		
		return reposition(replayTurn, replayAction);
	}
	
	public Game backTurn() {
		if(turns.size() <= replayTurn) {
			replayTurn = turns.size() -1;
		} else if(replayAction == 0){			
			int ReplayTurnNo = turns.get(replayTurn).turnNo;
			if(ReplayTurnNo < 0) {
				replayTurn = 0;
			} else {
				replayTurn = convertTurnNoToIndex(ReplayTurnNo - 1);
			}			
		}		
		replayAction = 0;
				
		return reposition(replayTurn, replayAction);
	}
	
	public Game forwardTurn() {
		replayAction = 0;
		if(turns.size() <= replayTurn + 1) {
			replayTurn = turns.size() -1;
			replayAction = turns.get(replayTurn).actions.size();
		} else {			
			int ReplayTurnNo = turns.get(replayTurn).turnNo;
			replayTurn = convertTurnNoToIndex(ReplayTurnNo + 1);
		}		
				
		return reposition(replayTurn, replayAction);
	}
	
}
