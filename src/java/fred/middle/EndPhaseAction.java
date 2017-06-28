package fred.middle;

import fred.model.GameDoer;
import fred.model.TurnPhase;
import fred.model.Player;
import fred.model.GameInfo;

public class EndPhaseAction extends Action {
    
    private static final long serialVersionUID = 1L;

    private TurnPhase startPhase;
	private TurnPhase endPhase;
	private boolean samePlayer;

	protected EndPhaseAction() {
		startPhase = null;
		endPhase = null;
	}
	
	@Override
	public void perform(GameDoer toGame, GameInfo refGame) {
		Player player = null;
		if(startPhase == null) {
			startPhase = refGame.getCurrentTurnPhase();
			player = refGame.getCurrentPlayer();
		}
		toGame.endCurrentTurnPhase();
		if(player != null) {
			endPhase = refGame.getCurrentTurnPhase();
			samePlayer = (player == refGame.getCurrentPlayer());
		}
	}
	
	@Override
    public boolean canDo(GameInfo refGame) {
		if(startPhase != null) {
			if(startPhase != refGame.getCurrentTurnPhase()) {
				return false;
			}
		}
		return refGame.canEndTurnPhase();
	}

	public TurnPhase getStartPhase() {
		return startPhase;
	}

	public TurnPhase getEndPhase() {
		return endPhase;
	}

	public boolean isSamePlayer() {
		return samePlayer;
	}
}
