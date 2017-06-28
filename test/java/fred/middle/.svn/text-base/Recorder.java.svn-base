package fred.middle;
import fred.model.Game;
import fred.model.GameDoer;

public class Recorder extends Wrapper {

	Action lastAction ;	

	protected Recorder(GameDoer doer, Game refGame) {
		super(doer, refGame);
		lastAction = null;
	}

	@Override
	protected void handleAction(Action action) {
		super.handleAction(action);
		lastAction = action;
	}

	protected Action getLastAction() {
		return lastAction;
	}
	
}
