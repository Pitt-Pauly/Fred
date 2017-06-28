package fred.model;

import org.newdawn.slick.Color;

import fred.model.Game.Type;

public class AdvancedPlayer extends Player {

	private static final long serialVersionUID = 1L;
	
	public AdvancedPlayer(String name, Color color, PlayerType playerType) {
		super(name, color, playerType);
	}

	public AdvancedPlayer(String name) {
		super(name);
	}

	@Override
    public Game.Type getGameType(){
		return Type.Advanced;
	}

}
