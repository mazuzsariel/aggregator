package crystalol;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;

import jodd.exception.UncheckedException;
import jodd.io.FileNameUtil;

import org.apache.commons.io.FilenameUtils;

import com.google.gson.Gson;

public class Utils {
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.97 Safari/537.11";
	public static final String ENCODING = "utf-8";
//	private static final Pattern CHARSET = Pattern.compile("text/html;\\s+charset=([^\\s]+)\\s*");
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	private static final int DAYS_AGO_DEFAULT = -1;
	
	public static int getDaysDiff(String from, String to) throws Exception{
		if( from == null || from.length() == 0 || to == null || to.length() == 0){
			return DAYS_AGO_DEFAULT;
		}

		Date startDate = dateFormat.parse(from);
		Date endDate = dateFormat.parse(to);
		long startTime = startDate.getTime();
		long endTime = endDate.getTime();
		long diffTime = endTime - startTime;
		long diffDays = diffTime / (1000 * 60 * 60 * 24);
		
		return (int) diffDays;		
	}
	
	public static String getCurrentDateString(){
		return dateFormat.format(new Date());
	}

	public static String getCurrentTimeString(){
		return timeFormat.format(new Date());
	}

	public static <T> T readJson(String path, Class<T> clazz) throws Exception{	
		FileReader reader = new FileReader(path);	
		return  new Gson().fromJson(reader, clazz);
	}

	public static File createFile(String path)throws Exception {
		String baseName = FileNameUtil.getName(path);
		String dirs = FilenameUtils.getFullPathNoEndSeparator(path);
		
		File file = new File(dirs);
		file.mkdirs() ;
		
		file = new File(dirs, baseName);
		file.createNewFile();
		return file;
	}

	public static File createFile(File dir, String name)throws Exception {
		File file = new File(dir, name);
		file.createNewFile();
		return file;
	}

	public static File downloadImage(File dir, String name, String url) throws Exception{
		return downloadImage(dir, name, url, -1, -1);
	}	
	
	public static File downloadImage(File dir, String name, String url, int width, int height) throws Exception{
		File file = Utils.createFile(dir, name);
		URLConnection con = openConnection(url);

		InputStream is = null;
		OutputStream os = null;
		try{
			is = con.getInputStream();
		 
			BufferedImage original = ImageIO.read(is);		
			int oWidth = original.getWidth();
			int oHeight = original.getHeight();
			if(width<0)width = oWidth;
			if(height<0)height = oHeight;
			
			if(oWidth!=width || oHeight!=height){		
				original = resizeImage(width, height, original);
			}

			String format = FilenameUtils.getExtension(name);		
			os = new FileOutputStream(file);		
			ImageIO.write(original, format, os);
		}finally{
			if(is!=null)is.close();
			if(os!=null)os.close();
		}
		
		return file;
	}

	public static  URLConnection openConnection(String stringUrl) throws Exception {
		URL url = new URL(stringUrl);	
		URLConnection con = url.openConnection();
		con.addRequestProperty("User-Agent", USER_AGENT);
		return con;
	}

	public static String getSource(String stringUrl) throws Exception{
		return getSource(stringUrl, 0);
	}
	
	public static String getSource(String stringUrl, int index) throws Exception{
		try{
			URLConnection con = Utils.openConnection(stringUrl);
		
			Reader r = new InputStreamReader(con.getInputStream(), ENCODING);
			StringBuilder buf = new StringBuilder();
			
			while (true) {
			  int ch = r.read();
			  if (ch < 0) break;
			  buf.append((char) ch);
			}
			
			return buf.toString();	
		}catch(Exception e){
			//some sites too slow and return 504 time to time
			//so we do few attempts to read data
			if(index<10)return getSource(stringUrl, ++index);
			throw e;
		}
	}
	
	public static String encodeName(String name){
        String nameEncoded = name.toLowerCase();
        nameEncoded = nameEncoded.replace(": ", "-");
        nameEncoded = nameEncoded.replace(" ", "-");
        nameEncoded = nameEncoded.replace(":", "-");
        nameEncoded = nameEncoded.replace("'", "");
        nameEncoded = nameEncoded.replace(".", "");
        return nameEncoded;
	}
	
	public static String trim(String string) {
		try{
			BufferedReader reader = new BufferedReader(new StringReader(string));
			StringBuffer out = new StringBuffer();
			String subString;
			while((subString = reader.readLine())!=null){
				if(subString.trim().length()>0) out.append(subString);
			}
			return out.toString().trim();
		}catch(Exception e){
			throw new UncheckedException(e);
		}
	}

	public static String concatDomain(String domain, String url){
		if(url.startsWith("http://")) return url;
		
		if(!url.startsWith("/")){
			url = "/".concat(url);
		}		
		return domain.concat(url);    	
	}
	
	
	public static void createSprite(List<File> images, File spriteFile, int imgWidth, int imgHeight) throws Exception {
		int rows = 0;
		
		List<BufferedImage> buffImages = new ArrayList<BufferedImage>();  
		for(File image:images){
			Log.log("read: ", image.getCanonicalPath());
			buffImages.add(ImageIO.read(image));
			rows++;
        }  		
		
		BufferedImage sprite = new BufferedImage(imgWidth, imgHeight*rows, BufferedImage.TYPE_INT_RGB);	
		Graphics2D g = sprite.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);			
		
		rows = 0;
        for(BufferedImage image:buffImages){        	
        	int oWidth = image.getWidth();
			int oHeight = image.getHeight();
			
			if(oWidth!=imgWidth || oHeight!=imgHeight){		
				image = resizeImage(imgWidth, imgHeight, image);
			}
        	
    		g.drawImage(image, 0, imgHeight * rows, Color.WHITE, null);
            rows++;
        }  
        
        String format = FilenameUtils.getExtension(spriteFile.getPath());
        ImageIO.write(sprite, format, spriteFile); 
	}

	private static BufferedImage resizeImage(int imgWidth, int imgHeight, BufferedImage image) {
		BufferedImage converted = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2= converted.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);			
		g2.drawImage(image, 0, 0, imgWidth, imgHeight, Color.WHITE, null);
		image = converted;
		return image;
	}
	

	public static Properties loadProperties(String name) throws Exception {
		Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(name);
        prop.load(fis);
        return prop;
	}
}