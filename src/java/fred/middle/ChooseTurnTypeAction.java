package fred.middle;

import fred.model.GameDoer;
import fred.model.TurnPhase;
import fred.model.GameInfo;

public class ChooseTurnTypeAction extends Action {
    
    private static final long serialVersionUID = 1L;
	
	TurnPhase type;
	
	protected ChooseTurnTypeAction(TurnPhase type) {
		this.type = type;
	}
	
	@Override
	public void perform(GameDoer toGame, GameInfo refGame) {
		toGame.chooseTurnType(type);
	}

	@Override
    public boolean canDo(GameInfo refGame) {
		return refGame.canChooseTurnType();
	}
}
