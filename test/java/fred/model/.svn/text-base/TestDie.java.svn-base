package fred.model;

public class TestDie extends Die {
    
    private static final long serialVersionUID = 1L;

	private int index;
	private int[] values = new int[0];
	
	public void setValues(int[] values) {
		this.values = values;
		index = 0;
	}
	
	@Override
	public int roll() {
		if (index >= values.length) {
			throw new RuntimeException("No more die rolls");
		}
		return values[index++];
	}
}
