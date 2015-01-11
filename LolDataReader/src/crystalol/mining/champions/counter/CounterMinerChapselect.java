package crystalol.mining.champions.counter;

import crystalol.Utils;

public class CounterMinerChapselect extends CounterMiner{	
	@Override
	protected String getSelectorChampionsHolder(){
		return ".champions a";
	}

	@Override
	protected String getSelectorChampionName(){
		return ".champ-img";
	}

	@Override
	protected String getSelectorChampionLink(){
		return null;
	}
	
	@Override
	protected String getChampionUrlDomain(){		
		return getProperty("mining.champions.url.championselect.domain");
	}
	
	@Override
	protected String getSelectorChampionsWeak(){
		return ".weak-strong .champ-block";
	}

	@Override
	protected String getSelectorChampionsStrong(){
		return ".weak-strong .champ-block";
	}

	@Override
	protected String getSelectorChampionCounterName(){
		return ".name";
	}
	
	@Override
	protected String getSelectorChampionUp(){
		return ".uvote";
	}
	
	@Override
	protected String getSelectorChampionDown(){
		return ".dvote";
	}
	
	@Override
	protected String getUrl(){		
		return getProperty("mining.champions.url.championselect");
	}

	@Override
	protected void parseChampionByUrl(String championUrl, String nameEncoded) throws Exception {
    	String weakSource = Utils.getSource(championUrl.concat("/weak"));
    	parseChampionPage(nameEncoded, weakSource, PARSE_SIDE.WEAK);
    	
    	String strongSource = Utils.getSource(championUrl.concat("/strong"));
    	parseChampionPage(nameEncoded, strongSource, PARSE_SIDE.STRONG);
	}
}
