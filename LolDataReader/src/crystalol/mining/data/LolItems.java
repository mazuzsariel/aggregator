package crystalol.mining.data;

import java.util.List;

public class LolItems {
	private String date;
	private List<Item> items;

	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
