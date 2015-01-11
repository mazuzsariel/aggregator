package crystalol.mining.data;

public class ChampionPower {
	private enum STATUS{NEW, DELETED, DEFAULT};
	private String nameEncoded;
	private int power;
	private int progress;
	private STATUS status;
	
	public String getNameEncoded() {
		return nameEncoded;
	}
	public void setNameEncoded(String nameEncoded) {
		this.nameEncoded = nameEncoded;
	}
	public Integer getPower() {
		return power;
	}
	public void setPower(int power) {
		this.power = power;
	}
	public int getProgress() {
		return progress;
	}
	public void setProgress(int progress) {
		this.progress = progress;
	}
	public STATUS getStatus() {
		return status;
	}
	public void setStatusNew() {
		status = STATUS.NEW;
	}
	public void setStatusDeleted() {
		status = STATUS.DELETED;
	}
	public void setStatusDefault() {
		status = STATUS.DEFAULT;
	}

}
