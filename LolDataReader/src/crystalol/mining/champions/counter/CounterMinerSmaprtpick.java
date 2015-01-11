package crystalol.mining.champions.counter;

import java.util.HashMap;
import java.util.Map;

import jodd.jerry.Jerry;
import crystalol.Utils;

public class CounterMinerSmaprtpick extends CounterMiner {

	@Override
	protected String getUrl() {
		return getProperty("mining.champions.url.smartpick");
	}

	@Override
	protected String getChampionUrlDomain(){		
		return getProperty("mining.champions.url.smartpick.domain");
	}
	
	@Override
	protected String getSelectorChampionsHolder() {
		return "div.tab-content div ul li a";
	}

	@Override
	protected String getSelectorChampionName() {
		return null;
	}

	@Override
	protected String getSelectorChampionLink() {
		return null;
	}

	@Override
	protected String getSelectorChampionsWeak() {
		return "#left_champion_container .counterpick_container";
	}

	@Override
	protected String getSelectorChampionsStrong() {
		return "#right_champion_container .counterpick_container";
	}

	@Override
	protected String getSelectorChampionCounterName() {
		return ".championName";
	}

	@Override
	protected Map<String, String> getVotes(Jerry $this){	
		String votesRaw = $this.$(".thumb_container span:first").attr("votes");
		
		String[] votes = votesRaw.split("\\|");
		String up = votes[0].trim();
		String down = votes[1].trim();
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(ITEM_UP, up);
		map.put(ITEM_DOWN, down);
		
		return map;
	}

	@Override
	protected void parseChampion(final String nameEncoded, String source) throws Exception{
		for(int i=0; i<5; i++){
			String suffix = "&role=".concat(Integer.toString(i));
			String championsSource = Utils.getSource(getChampionUrl().concat(suffix));
			parseChampionPage(nameEncoded, championsSource);
		}
	}
}
