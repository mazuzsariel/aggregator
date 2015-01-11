package crystalol.mining.champions.description.items;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jodd.exception.UncheckedException;
import jodd.jerry.Jerry;
import jodd.jerry.JerryFunction;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

import crystalol.Log;
import crystalol.PropertyThread;
import crystalol.Utils;
import crystalol.mining.data.Item;
import crystalol.mining.data.LolItems;

public abstract class ItemsMiner extends PropertyThread {
	protected static final int ICON_HEIGHT = 64;
	private static final int ICON_WIDTH = 64;
	
	protected abstract String getUrl();
	protected abstract String getUrlDomain();		
	protected abstract String getSelectorItemsHolder();
	protected abstract String getSelectorItemName();
	protected abstract String getSelectorItemImage();
	
	private File imagesDir;
	
	@Override
	protected void process() throws Exception {
		String source = Utils.getSource(getUrl());
		parse(source);
	}

	protected void parse(String source) throws Exception {
		Jerry doc = Jerry.jerry(source);
		
		final List<Item> itemsList = new ArrayList<Item>();
		
		final String SItems = getSelectorItemsHolder();
		final String SName = getSelectorItemName();
		final String SImg = getSelectorItemImage();
		
		final String domain = getUrlDomain();

		doc.$(SItems).each(new JerryFunction() {
	        public boolean onNode(Jerry $this, int index) { 
	        	String name = Utils.trim($this.$(SName).text());
	        	Log.log(Integer.toString(index).concat(" ").concat(name), 1);
	        	
	            String imgUrl = $this.$(SImg).attr("src");
	            imgUrl = Utils.concatDomain(domain, imgUrl);
	            String nameEncoded = Utils.encodeName(name);
	          
	            Item item = new Item();
	            item.setName(name);
	            item.setNameEncoded(nameEncoded);
	            item.setSprite("-".concat(Integer.toString(ICON_HEIGHT*index)));
	      
	            itemsList.add(item);
	            
	            downloadImage(imgUrl, nameEncoded);
	         	return true;
	        }
		});
		
		final LolItems items = new LolItems();
		items.setItems(itemsList);						
		items.setDate(Utils.getCurrentDateString());
		
		String json = new Gson().toJson(items);
		File itemsSource = new File(getProperty("mining.items.file.description.raw"));		
		FileUtils.writeStringToFile(itemsSource, json, Utils.ENCODING);		
	}

	private File downloadImage(String imgUrl, String nameEncoded) {
		try{
			String iconFileName = getProperty("mining.items.file.icon");
            File imageDir = new File(getImagesDir(), nameEncoded);	            
            imageDir.mkdir();
            File image = Utils.downloadImage(imageDir, iconFileName, imgUrl, ICON_WIDTH, ICON_HEIGHT);	
            return image;
        }catch(Exception e){
        	throw new UncheckedException(e);
        }
	}
	
	protected File getImagesDir() throws Exception{
		if(imagesDir == null){
			imagesDir = new File(getProperty("mining.items.folder.img64.raw"));
			imagesDir.mkdirs();
			FileUtils.cleanDirectory(imagesDir);
		}
		return imagesDir;
	}
}
