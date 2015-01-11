package crystalol.mining.manage;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Manager {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		showItems();
	}
	
	public static void showItems() throws Exception{
		JFrame.setDefaultLookAndFeelDecorated(true);
	    JFrame frame = new JFrame();
	    frame.setTitle("Crystalol Data Manager");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   
	    ItemsManager items = new ItemsManager();
		JPanel panel = new JPanel();		
		frame.add(panel);

		items.fillItems(panel);
		//items.fillItemsRaw(panel);

	    panel.setSize(200, 200);
		panel.setBackground(Color.GRAY);
		JScrollPane scroller = new JScrollPane(panel);  
		frame.add(scroller, BorderLayout.CENTER);

	    frame.pack();
	    frame.setSize(400, 800);
	    frame.setVisible(true);
	}
}
