package fred.model;

import fred.model.Game.Type;

public class AdvancedTerritory extends Territory {
	private static final long serialVersionUID = 1L;
	public AdvancedTerritory(String name) {
		super(name);
	}

        private int slaveCount;

        public int getSlaveCount() {
            return slaveCount;
        }

        public void setSlaveCount(int slaveCount) {
		if (slaveCount < 0) {
			throw new IllegalArgumentException("" + slaveCount);
		}
		this.slaveCount = slaveCount;
	}

	@Override
    public Game.Type getGameType(){
		return Type.Advanced;
	}
}
