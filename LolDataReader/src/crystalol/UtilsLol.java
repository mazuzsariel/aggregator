package crystalol;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

import crystalol.mining.data.Champion;
import crystalol.mining.data.Champions;
import crystalol.mining.data.Item;
import crystalol.mining.data.LolItems;

public class UtilsLol {

	public static Champions readChampionsData(PropertyReader properties) throws Exception{
		String path = properties.getProperty("mining.champions.file.description");
		Champions champions =  Utils.readJson(path, Champions.class);	
		return champions;
	}
	
	public static List<Champion> readChampions(PropertyReader properties) throws Exception{
		String path = properties.getProperty("mining.champions.file.description");
		Champions champions =  Utils.readJson(path, Champions.class);		

		List<Champion> championsList = champions.getChampions();
		return championsList;
	}

	public static List<Item> readItemsRaw(PropertyReader properties) throws Exception{
		String path = properties.getProperty("mining.items.file.description.raw");
		return readItems(path);
	}

	public static List<Item> readItems(PropertyReader properties) throws Exception{
		String path = properties.getProperty("mining.items.file.description");
		return readItems(path);
	}
	
	public static List<Item> readItems(String path) throws Exception{
		LolItems items =  Utils.readJson(path, LolItems.class);		

		List<Item> itemsList = items.getItems();
		return itemsList;
	}

	public static void saveChampions(PropertyReader properties, Champions champions) throws Exception{
		String date = Utils.getCurrentDateString();	
		champions.setDate(date);
		
		String json = new Gson().toJson(champions);
		
		File championsSource = new File(properties.getProperty("mining.champions.file.description"));		
		FileUtils.writeStringToFile(championsSource, json, Utils.ENCODING);
	}
}
