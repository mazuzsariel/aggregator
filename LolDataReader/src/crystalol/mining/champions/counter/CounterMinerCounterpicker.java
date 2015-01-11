package crystalol.mining.champions.counter;

import java.util.HashMap;
import java.util.Map;

import jodd.jerry.Jerry;

public class CounterMinerCounterpicker extends CounterMiner {

	@Override
	protected String getUrl() {
		return getProperty("mining.champions.url.counterpicker");
	}

	@Override
	protected String getChampionUrlDomain(){		
		return getProperty("mining.champions.url.counterpicker.domain");
	}
	
	@Override
	protected String getSelectorChampionsHolder() {
		return "div#hrdina";
	}

	@Override
	protected String getSelectorChampionName() {
		return null;
	}

	@Override
	protected String getSelectorChampionLink() {
		return "a";
	}

	@Override
	protected String getSelectorChampionsWeak() {
		return "div#box_d div.pravy";
	}

	@Override
	protected String getSelectorChampionsStrong() {
		return "div#ram_obsah_maly div:eq(3) div.pravy";
	}

	@Override
	protected String getSelectorChampionCounterName() {
		return "a:eq(0)";
	}
	
	@Override
	protected Map<String, String> getVotes(Jerry $this){		
		String html = $this.html();
		html = html.replaceAll("<a.*?a>", "").replace("<br>", "");
		String[] votes = html.split(" ");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(ITEM_UP, votes[0]);
		map.put(ITEM_DOWN, votes[1]);
		
		return map;
	}
	
	@Override
	protected boolean isRemoveDuplicates() {
		return true;
	}



}
