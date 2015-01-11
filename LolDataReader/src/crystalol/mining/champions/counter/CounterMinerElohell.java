package crystalol.mining.champions.counter;

import org.apache.commons.lang3.StringUtils;

public class CounterMinerElohell extends CounterMiner {

	@Override
	protected String getUrl() {
		return getProperty("mining.champions.url.elohell");
	}

	@Override
	protected String getChampionUrlDomain(){		
		return getProperty("mining.champions.url.elohell.domain");
	}
	
	@Override
	protected String getChampionUrlMask() {
		return getProperty("mining.champions.url.elohell.counter");		
	}

	@Override
	protected String detectSiteChamptionEncodedName(String url){
		return StringUtils.substringBetween(url, "-champions/", "-guide");
	}

	@Override
	protected String getSelectorChampionsHolder() {
		return "li.ch";
	}

	@Override
	protected String getSelectorChampionName() {
		return ".name";
	}

	@Override
	protected String getSelectorChampionLink() {
		return "a";
	}

	@Override
	protected String getSelectorChampionsWeak() {
		return ".strong li";
	}

	@Override
	protected String getSelectorChampionsStrong() {
		return ".weak li";
	}

	@Override
	protected String getSelectorChampionCounterName() {
		return ".name";
	}

	@Override
	protected String getSelectorChampionUp() {
		return ".won";
	}

	@Override
	protected String getSelectorChampionDown() {
		return ".lost";
	}

}
