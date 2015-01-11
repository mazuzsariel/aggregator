package crystalol.mining.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CounterBrunches {
	private String date;
	private List<ChampionCounters> counters;
	
	public List<ChampionCounters> getCounters() {
		return counters;
	}
	public void setCounters(List<ChampionCounters> counters) {
		this.counters = counters;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Map<String, ChampionCounters> getCountersMap(){
		Map<String, ChampionCounters> countersMap = new HashMap<String, ChampionCounters>();
		
		for(ChampionCounters champion:counters){
			countersMap.put(champion.getNameEncoded(), champion);
		}
		
		return countersMap;
	}
}
