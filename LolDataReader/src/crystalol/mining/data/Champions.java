package crystalol.mining.data;

import java.util.List;

public class Champions {
	private String date;
	private List<Champion> champions;

	public List<Champion> getChampions() {
		return champions;
	}
	public void setChampions(List<Champion> champions) {
		this.champions = champions;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
