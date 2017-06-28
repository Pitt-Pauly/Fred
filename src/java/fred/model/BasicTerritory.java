package fred.model;

import fred.model.Game.Type;

public class BasicTerritory extends Territory {
	private static final long serialVersionUID = 1L;
	public BasicTerritory(String name) {
		super(name);
	}
	
	@Override
    public Game.Type getGameType(){
		return Type.Basic;
	}
}
