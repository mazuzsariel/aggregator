package crystalol.mining.champions.description.items;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import crystalol.Log;
import crystalol.PropertyThread;
import crystalol.UtilsLol;
import crystalol.mining.data.Item;

public class ItemsDiff extends PropertyThread {

	@Override
	protected void process() throws Exception {
		List<Item> items = UtilsLol.readItems(this);
		Map<String, Item> itemsMap = wrapItems(items);
		
		List<Item> itemsRaw = UtilsLol.readItemsRaw(this);
		Map<String, Item> itemsRawMap = wrapItems(itemsRaw);
		
		Log.log("New: ");
		for(Item item:itemsRaw){
			if(!itemsMap.containsKey(item.getNameEncoded())){
				Log.log(item.getNameEncoded());
			}
		}
		
		Log.log("Removed: ");
		for(Item item:items){
			if(!itemsRawMap.containsKey(item.getNameEncoded())){
				Log.log(item.getNameEncoded());
			}
		}
	}

	private Map<String, Item> wrapItems(List<Item> items){
		Map<String, Item> itemsMap = new HashMap<String, Item>();
		for(Item item:items){
			itemsMap.put(item.getNameEncoded(), item);
		}
		return itemsMap;
	}
}
