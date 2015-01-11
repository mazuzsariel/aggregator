package crystalol.mining.champions.description.items;

public class ItemsFromElohell extends ItemsMiner {

	@Override
	protected String getUrl() {
		return getProperty("mining.items.url.elohell");
	}

	@Override
	protected String getUrlDomain() {
		return getProperty("mining.items.url.elohell.domain");
	}

	@Override
	protected String getSelectorItemsHolder() {
		return "ul.itemslist li";
	}

	@Override
	protected String getSelectorItemName() {
		return "p a";
	}

	@Override
	protected String getSelectorItemImage() {
		return "a img";
	}

}
