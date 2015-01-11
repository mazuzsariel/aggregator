package crystalol.mining.data;

public class ChampionArc {
	private String nameLeft;
	private String nameRight;
	private int weightLeft = 0;
	private int weightRight = 0;
	
	public String getNameLeft() {
		return nameLeft;
	}
	public void setNameLeft(String nameLeft) {
		this.nameLeft = nameLeft;
	}
	public String getNameRight() {
		return nameRight;
	}
	public void setNameRight(String nameRight) {
		this.nameRight = nameRight;
	}
	public int getWeightLeft() {
		return weightLeft;
	}
	public void addWeightLeft(int weightLeft) {
		this.weightLeft += weightLeft;
	}
	public int getWeightRight() {
		return weightRight;
	}
	public void addWeightRight(int weightRight) {
		this.weightRight += weightRight;
	}
	
	public void normalize(){
		if(weightRight==weightLeft){
			weightRight = 100;
			weightLeft = 100;
			return;
		}
		
		if(weightRight<weightLeft){
			int p = 100 - (weightRight*100/weightLeft);
			weightLeft = 100;
			weightRight = p;
			return;
		}
		
		if(weightLeft<weightRight){
			int p = 100 - (weightLeft*100/weightRight);
			weightRight = 100;
			weightLeft = p;
			return;
		}

	}
}
