package crystalol.mining.data;

public class CounterScales {
	private int up = 0;
	private int down = 0;
	
	public int getUp() {
		return up;
	}
	public void addUp(String up) {
		this.up += Integer.parseInt(up);
	}
	public int getDown() {
		return down;
	}
	public void addDown(String down) {
		this.down += Integer.parseInt(down);
	}
}
