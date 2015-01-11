package crystalol.mining.champions.description.items;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

import crystalol.PropertyThread;
import crystalol.Utils;
import crystalol.mining.data.Item;
import crystalol.mining.data.LolItems;

public class ItemsSprite extends PropertyThread{
	protected static final int ICON_HEIGHT = 50;
	private static final int ICON_WIDTH = 50;

	private File imagesDir;

	@Override
	protected void process() throws Exception {
		LolItems itemsCore = Utils.readJson(getProperty("mining.items.file.description"), LolItems.class);	
		List<Item> items = itemsCore.getItems();	
		List<File> images = new ArrayList<File>();
		
		String iconFileName = getProperty("mining.items.file.icon");
		int index = 0;
		for(Item item:items){
			item.setSprite("-".concat(Integer.toString(ICON_HEIGHT*index)));
			index++;
			
			File imageDir = new File(getImagesDir(), item.getNameEncoded());
			File image = new File(imageDir, iconFileName);
			images.add(image);
		}

		itemsCore.setDate(Utils.getCurrentDateString());		
		String json = new Gson().toJson(itemsCore);
		File itemsSource = new File(getProperty("mining.items.file.description"));		
		FileUtils.writeStringToFile(itemsSource, json, Utils.ENCODING);		

		File sprite = Utils.createFile(getProperty("mining.items.file.sprite"));
    	Utils.createSprite(images, sprite, ICON_WIDTH, ICON_HEIGHT);
	}
	
	protected File getImagesDir() throws Exception{
		if(imagesDir == null){
			imagesDir = new File(getProperty("mining.items.folder.img64"));
		}
		return imagesDir;
	}

}
