package crystalol.mining.champions.description;


public class DescriptionFromLol extends Description{
	@Override
	protected String getUrl(){
		return getProperty("mining.champions.url.champions.lol");
	}

	@Override
	protected String getSelectorChampionsHolder() {
		return ".champion_item";
	}

	@Override
	protected String getSelectorChampionsName() {
		return ".description span a";
	}

	@Override
	protected String getSelectorChampionsImage() {
		return ".champion img";
	}	
	
}
