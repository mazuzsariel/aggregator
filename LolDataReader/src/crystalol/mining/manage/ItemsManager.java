package crystalol.mining.manage;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import crystalol.PropertyReader;
import crystalol.Utils;
import crystalol.UtilsLol;
import crystalol.mining.data.Item;

public class ItemsManager implements PropertyReader{
	private Properties properties;

	/**
	 * @param args
	 * @throws Exception 
	 */
	public ItemsManager() throws Exception {
		properties = Utils.loadProperties("config/mining.properties");
	}
	
	public String getProperty(String key){
		return properties.getProperty(key);
	}
	
	public int getPropertyInt(String key){
		String property = properties.getProperty(key);
		return Integer.parseInt(property);
	}

	public void fillItemsRaw(JPanel rootPanel) throws Exception {
		String itemsFileName = getProperty("mining.items.file.description.raw");
		String imgDirName = getProperty("mining.items.folder.img64.raw");
		fillItems(rootPanel, "Raw", itemsFileName, imgDirName);
	}

	public void fillItems(JPanel rootPanel) throws Exception {
		String itemsFileName = getProperty("mining.items.file.description");
		String imgDirName = getProperty("mining.items.folder.img64");
		fillItems(rootPanel, "Base", itemsFileName, imgDirName);
	}

	private void fillItems(JPanel rootPanel, String title, String itemsFileName, String imgDirName) throws Exception {
		JPanel panel = new JPanel();		

		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints constraintsImage = new GridBagConstraints(
			    0, GridBagConstraints.RELATIVE,    // x = 0, y = below previous element
			    1, 1,           // cell width = 1, cell height = 1
			    1.0, 1.0,        // how to distribute space: weightx = 0.0, weighty = 0,0 
			    GridBagConstraints.WEST,  // anchor
			    GridBagConstraints.WEST,    // fill
			    new Insets(10, 10, 0, 0),     // cell insets
			    0, 0);          // internal padding
		
		rootPanel.add(panel);
		
		JLabel label = new JLabel(title);
		JPanel tPanel = new JPanel();		
		tPanel.add(label);
		tPanel.setBackground(Color.WHITE);
		panel.add(tPanel);			

		File folder = new File(imgDirName);
		String fileName = getProperty("mining.items.file.icon");		
		List<Item> itemsList = UtilsLol.readItems(itemsFileName);
		for(Item item:itemsList){
			File img = new File(new File(folder, item.getNameEncoded()),fileName);
			BufferedImage myPicture = ImageIO.read(img);
			JLabel picLabel = new JLabel(item.getName(), new ImageIcon( myPicture ), JLabel.CENTER);
			panel.add(picLabel, constraintsImage);			
		}	
		
		panel.setBackground(Color.WHITE);
		
	}


}
