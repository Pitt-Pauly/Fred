package fred.model;

import java.util.List;

public class AdvancedGame extends Game {

	private static final long serialVersionUID = 1L;
	public AdvancedGame(List<Player> players, World world) {
		super(players, world);
	}

        public void slaveAttack (int initialDefenders, int finalDefenders, int initialAttackers,
                int finalAttackers, boolean Succes) {
            if (Succes == false) {
                //empty
            }
            if (Succes == true) {
                //empty
            }
        }

	@Override
    public Type getType(){
		return Type.Advanced;
	}
}
