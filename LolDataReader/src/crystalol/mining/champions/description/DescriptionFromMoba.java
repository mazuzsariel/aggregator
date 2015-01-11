package crystalol.mining.champions.description;

public class DescriptionFromMoba extends Description {

	@Override
	protected String getUrl(){
		return getProperty("mining.champions.url.champions.moba");
	}

	@Override
	protected String getSelectorChampionsHolder() {
		return ".champ-box";
	}

	@Override
	protected String getSelectorChampionsName() {
		return ".champ-name";
	}	
	
	@Override
	protected String getSelectorChampionsImage() {
		return "img:first";
	}
	
	@Override
	protected String getUrlDomain() {
		return getProperty("mining.champions.url.champions.moba.domain");
	}

}
