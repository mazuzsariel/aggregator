package crystalol.mining.champions.description;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import jodd.exception.UncheckedException;
import jodd.io.FileNameUtil;
import crystalol.Utils;

public class DescriptionFromWiki extends Description {

	private File imagesDir120;
	private File imagesDirApp;

	@Override
	protected String getUrl() {
		return getProperty("mining.champions.url.champions.wiki");
	}

	@Override
	protected String getSelectorChampionsHolder() {
		return "div [style=\"text-align:center\"]";
	}

	@Override
	protected String getSelectorChampionsName() {
		return "[style=\"text-align:center; font-size:13px\"";
	}

	@Override
	protected String getSelectorChampionsImage() {
		return "img";
	}

	@Override
	protected String getUrlDomain() {
		return "";
		//return getProperty("mining.champions.url.champions.wiki.domain");
	}
	
	private File getImagesDir() throws Exception{
		if(imagesDir120 == null){
			imagesDir120 = new File(getProperty("mining.champions.folder.img120"));
			imagesDir120.mkdirs();
			FileUtils.cleanDirectory(imagesDir120);
		}
		return imagesDir120;
	}

	private File getImagesDirApp() throws Exception{
		if(imagesDirApp == null){
			imagesDirApp = new File(getProperty("mining.champions.folder.imgApp"));
			imagesDirApp.mkdirs();
			FileUtils.cleanDirectory(imagesDirApp);
		}
		return imagesDirApp;
	}
	
	@Override
	protected void downloadImage120(String imgUrl, String nameEncoded) {
		try{
			imgUrl = imgUrl.replace("/thumb/", "/");
			imgUrl = imgUrl.substring(0, imgUrl.indexOf(".png/")+4);
			
			String iconFileName = getProperty("mining.champions.file.icon");
            File imageDir = new File(getImagesDir(), nameEncoded);	            
            imageDir.mkdir();
            File img = Utils.downloadImage(imageDir, iconFileName, imgUrl, 120, 120);	   
            String appImgName = nameEncoded.concat(FilenameUtils.EXTENSION_SEPARATOR_STR).concat(FileNameUtil.getExtension(iconFileName));
            appImgName = getProperty("mining.champions.file.img.app.prefix").concat(appImgName);
            File appImg = new File(getImagesDirApp(), appImgName);
			FileUtils.copyFile(img, appImg);
        }catch(Exception e){
        	throw new UncheckedException(e);
        }
	}

}
