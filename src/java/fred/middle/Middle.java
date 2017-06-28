package fred.middle;


import fred.model.Game;
import fred.model.GameDoer;

import fred.AI.AI;

//Will have cool replay stuff someday.
public class Middle extends Wrapper {
	private GameDoer forReal;
	private Game reality;
	private AI AI; //must confuse things.

	public Middle(GameDoer doer, Game game) {
		super(doer, game);
		forReal = doer;
		reality = game;
		AI = new AI(this, this);
	}
	
	public Middle(Replay replay) {
		super(replay);
		forReal = doer;
		reality = refGame;
		AI = new AI(this, this);
	}
	
	public Middle(Middle replaying) {
		super(replaying.getReplay());
		forReal = null;
		reality = null;
		AI = null;
		startReplayAtStart();
	}
		
	@Override
	public boolean isButterflyZhuangzi() {
		return(refGame == reality && doer == forReal);
	}
	
	public void AIdoTurn() {
		if(!isButterflyZhuangzi()) {
			throw new IllegalStateException("AI dreaming");
		}
		AI.doTurn();
	}
	
	public void startReplayAtEnd() {
		Game game = replay.lastPosition();
		refGame = game;
		doer = null;
	}
	
	public void startReplayAtStart() {
		Game game = replay.initialPosition();
		refGame = game;
		doer = null;
	}
	
	public void replayBackTurn() {
		Game game = replay.backTurn();
		refGame = game;
		doer = null;
	}
	
	public void replayForwardTurn() {
		Game game = replay.forwardTurn();
		refGame = game;
		doer = null;
	}
	
	public void replayBackAction() {
		Game game = replay.backAction();
		refGame = game;
		doer = null;
	}
	
	public void replayForwardAction() {
		Game game = replay.forwardAction();
		refGame = game;
		doer = null;
	}
	
	public void wakeUp() {
		refGame = reality;
		doer = forReal;
	}
}
