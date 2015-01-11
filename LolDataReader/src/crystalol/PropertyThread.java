package crystalol;

import java.util.Properties;

public abstract class PropertyThread extends Thread implements PropertyReader {

	private Properties properties;
	
	public void setProperties(Properties properties){
		this.properties = properties;
	}

	public String getProperty(String key){
		return properties.getProperty(key);
	}
	
	public int getPropertyInt(String key){
		String property = properties.getProperty(key);
		return Integer.parseInt(property);
	}
	
	@Override
	public void run(){
		try{
			process();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	abstract protected void process() throws Exception;
}
