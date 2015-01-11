package crystalol.mining.champions.description.extended;

public class DescriprionExtendedFromLol extends DescriptionExtended{
	
	@Override
	protected String getUrl(){
		return getProperty("mining.champions.url.champions.lol");
	}

	@Override
	protected String getDomain(){
		return getProperty("mining.champions.url.champions.lol.domain");
	}
	
	@Override
	protected String getSelectorChampionsLink(){
		return ".description span a";
	}
	
	@Override
	protected String getSelectorChampionTitle(){
		return ".champion_title";
	}

	@Override
	protected String getSelectorChampionImg(){
		return ".champion_render img";
	}

}
