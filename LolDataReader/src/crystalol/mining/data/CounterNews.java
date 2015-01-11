package crystalol.mining.data;

import java.util.ArrayList;
import java.util.List;

public class CounterNews {
	private String datefrom;
	private String dateto;
	private int cutoff;
	private int daysago;
	private List<ChampionCounters> newChampions;
	private List<String> lostChampions;
	private List<ChampionCounters> counters;

	public List<ChampionCounters> getNewChampions() {
		return newChampions;
	}

	public void addNewChampion(ChampionCounters champion) {
		if(newChampions == null) newChampions = new ArrayList<ChampionCounters>();
		newChampions.add(champion);
	} 

	public List<String> getLostChampions() {
		return lostChampions;
	}

	public void addLostChampion(String encodedName) {
		if(lostChampions == null) lostChampions = new ArrayList<String>();
		lostChampions.add(encodedName);
	}

	public List<ChampionCounters> getCounters() {
		return counters;
	}

	public void addCounter(ChampionCounters counter) {
		if(counters == null) counters = new ArrayList<ChampionCounters>();
		counters.add(counter);
	}

	public int getCutoff() {
		return cutoff;
	}

	public void setCutoff(int cutoff) {
		this.cutoff = cutoff;
	}

	public int getDaysago() {
		return daysago;
	}

	public void setDaysago(int daysago) {
		this.daysago = daysago;
	}

	public String getDatefrom() {
		return datefrom;
	}

	public void setDatefrom(String datefrom) {
		this.datefrom = datefrom;
	}

	public String getDateto() {
		return dateto;
	}

	public void setDateto(String dateto) {
		this.dateto = dateto;
	} 

	
}
