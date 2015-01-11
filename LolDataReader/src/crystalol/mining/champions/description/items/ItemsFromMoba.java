package crystalol.mining.champions.description.items;

public class ItemsFromMoba extends ItemsMiner{

	@Override
	protected String getUrl() {
		return getProperty("mining.items.url.moba");
	}

	@Override
	protected String getUrlDomain() {
		return getProperty("mining.items.url.moba.domain");
	}
	
	@Override
	protected String getSelectorItemsHolder() {
		return "#browse-items a.champ-box";
	}

	@Override
	protected String getSelectorItemName() {
		return ".champ-name";
	}

	@Override
	protected String getSelectorItemImage() {
		return "img:first";
	}

}
