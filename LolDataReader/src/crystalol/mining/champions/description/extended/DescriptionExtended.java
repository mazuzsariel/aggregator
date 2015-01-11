package crystalol.mining.champions.description.extended;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jodd.exception.UncheckedException;
import jodd.jerry.Jerry;
import jodd.jerry.JerryFunction;
import crystalol.Log;
import crystalol.PropertyThread;
import crystalol.Utils;
import crystalol.UtilsLol;
import crystalol.mining.data.Champion;
import crystalol.mining.data.Champions;

/**
 * Champions file and champions image directory are supposed to be before run mining extended description
 * @author kuan
 *
 */
public abstract class DescriptionExtended extends PropertyThread {
	private File imagesDir;

	abstract protected String getUrl();
	abstract protected String getSelectorChampionsLink(); 
	abstract protected String getSelectorChampionTitle(); 
	abstract protected String getSelectorChampionImg(); 
	abstract protected String getDomain();
	
	@Override
	protected void process() throws Exception {
		String source = Utils.getSource(getUrl());
		
		parse(source);
	}
	
	protected void parse(String source) throws Exception {
		Jerry doc = Jerry.jerry(source);
		
		Champions champions = UtilsLol.readChampionsData(this);
		final List<Champion> championsList = champions.getChampions();
		final Map<String, Champion> championsMap = new HashMap<String, Champion>();
		for(Champion champion:championsList){
			championsMap.put(champion.getNameEncoded(), champion);
		}
		
		final String SChampions = getSelectorChampionsLink();
		
    	doc.$(SChampions).each(new JerryFunction() {
	        public boolean onNode(Jerry $this, int index) {
	        	try{
		        	String name = Utils.trim($this.text());
		        	Log.log(Integer.toString(index).concat(" ").concat(name), 1);
		
		            String nameEncoded = Utils.encodeName(name);
		        	String championUrl = $this.attr("href");
		        	championUrl = getDomain().concat(championUrl);
		        	String championSource = Utils.getSource(championUrl);
		        	parseCahmpion(championsMap.remove(nameEncoded), championSource);	               
	        	}catch(Exception e){
	        		throw new UncheckedException(e);
	        	}
	            return true;
	        }

	    });
    	
    	
    	if(championsMap.size()>0){
        	for(Entry<String, Champion> entry:championsMap.entrySet()){
        		String nameEncoded = entry.getKey();
        		Log.log(nameEncoded, " was not found");
        	}

    		throw new Exception("not all chapions was found");
    	}
    	
    	UtilsLol.saveChampions(this, champions);    	
	}
	
	protected void parseCahmpion(Champion champion, String source) throws Exception {
		Jerry doc = Jerry.jerry(source);
		
		String title = doc.$(getSelectorChampionTitle()).text();
		title= Utils.trim(title);
		champion.setTitle(title);
		
		String imgUrl = doc.$(getSelectorChampionImg()).attr("src");
        imgUrl = getDomain().concat(imgUrl);
      
        downloadImage(champion, imgUrl);        	     		
	}
	
	private void downloadImage(Champion champion, String imgUrl)throws Exception {
		String imgName = getProperty("mining.champions.file.render");
        File imageDir = new File(getImagesDir(), champion.getNameEncoded());	            
        imageDir.mkdir();
        Utils.downloadImage(imageDir, imgName, imgUrl);
	}
	
	private File getImagesDir() throws Exception{
		if(imagesDir == null){
			imagesDir = new File(getProperty("mining.champions.folder.img.render"));
			imagesDir.mkdirs();
		}
		return imagesDir;
	}


}
