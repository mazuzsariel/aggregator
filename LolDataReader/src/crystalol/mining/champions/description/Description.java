package crystalol.mining.champions.description;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jodd.exception.UncheckedException;
import jodd.jerry.Jerry;
import jodd.jerry.JerryFunction;

import org.apache.commons.io.FileUtils;

import crystalol.Log;
import crystalol.PropertyThread;
import crystalol.Utils;
import crystalol.UtilsLol;
import crystalol.mining.data.Champion;
import crystalol.mining.data.Champions;

abstract public class Description extends PropertyThread {
	protected static final int ICON_HEIGHT = 80;
	private static final int ICON_WIDTH = 80;
	private File imagesDir;
	
	abstract protected String getUrl();
	abstract protected String getSelectorChampionsHolder();
	abstract protected String getSelectorChampionsName();
	abstract protected String getSelectorChampionsImage();

	@Override
	protected void process() throws Exception{
		String source = Utils.getSource(getUrl());
		
		parse(source);
	}
	
	protected void parse(String source) throws Exception {
		Jerry doc = Jerry.jerry(source);
		
		final List<File> images = new ArrayList<File>();

		final List<Champion> championsList = new ArrayList<Champion>();
		
		final String SChampions = getSelectorChampionsHolder();
		final String SName = getSelectorChampionsName();
		final String SImg = getSelectorChampionsImage();
		final String SDomain = getUrlDomain();
		
    	doc.$(SChampions).each(new JerryFunction() {
	        public boolean onNode(Jerry $this, int index) { 
	        	String name = Utils.trim($this.$(SName).text());
	        	Log.log(Integer.toString(index).concat(" ").concat(name), 1);
	        	
	            String imgUrl = SDomain.concat($this.$(SImg).attr("src"));
	            String nameEncoded = Utils.encodeName(name);
	          
	            Champion champion = new Champion();
	            champion.setName(name);
	            champion.setNameEncoded(nameEncoded);
	            champion.setSprite("-".concat(Integer.toString(ICON_HEIGHT*index)));
	      
	            championsList.add(champion);
	            
	            File image = downloadImage(imgUrl, nameEncoded);
	            images.add(image);
	            
	            return true;
	        }

	    });
    	

		final Champions champions = new Champions();
		champions.setChampions(championsList);				
		UtilsLol.saveChampions(this, champions);
		
		File sprite = Utils.createFile(getProperty("mining.champions.file.sprite"));
    	Utils.createSprite(images, sprite, ICON_WIDTH, ICON_HEIGHT);
    	
    	FileUtils.deleteDirectory(imagesDir);
	}

	private File getImagesDir() throws Exception{
		if(imagesDir == null){
			imagesDir = new File(getProperty("mining.champions.folder.img80"));
			imagesDir.mkdirs();
			FileUtils.cleanDirectory(imagesDir);
		}
		return imagesDir;
	}
	
	private File downloadImage(String imgUrl, String nameEncoded) {
		try{
			String iconFileName = getProperty("mining.champions.file.icon");
            File imageDir = new File(getImagesDir(), nameEncoded);	            
            imageDir.mkdir();
            File image = Utils.downloadImage(imageDir, iconFileName, imgUrl, ICON_WIDTH, ICON_HEIGHT);	
            downloadImage120(imgUrl, nameEncoded);
            return image;
        }catch(Exception e){
        	throw new UncheckedException(e);
        }
	}

	protected void downloadImage120(String imgUrl, String nameEncoded) {
		Log.log("download 120*120 image does not supported");
	}
	
	protected String getUrlDomain() {
		return "";
	}
}
