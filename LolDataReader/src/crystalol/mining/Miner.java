package crystalol.mining;

import java.util.Properties;

import crystalol.Log;
import crystalol.PropertyThread;
import crystalol.Utils;
import crystalol.mining.champions.counter.Counter;
import crystalol.mining.champions.description.DescriptionFromLol;
import crystalol.mining.champions.description.DescriptionFromMoba;
import crystalol.mining.champions.description.DescriptionFromWiki;
import crystalol.mining.champions.description.extended.DescriprionExtendedFromLol;
import crystalol.mining.champions.description.items.ItemsDiff;
import crystalol.mining.champions.description.items.ItemsFromElohell;
import crystalol.mining.champions.description.items.ItemsFromMoba;
import crystalol.mining.champions.description.items.ItemsSprite;
import crystalol.mining.champions.news.News;
import crystalol.mining.manage.Manager;

public class Miner {
	private static enum ARG{
		description, counter, news, items,
		lol, moba, wiki, elohell,
		extended, diff, sprite, show};
	
	@SuppressWarnings("incomplete-switch")
	public static void main(String[] args) throws Exception {
		switch(ARG.valueOf(args[0])){
		case counter:			
			mineLogged(new Counter(), "Aggregation of counters");
			return;
		case description:			
			if(args.length<2){
				//default set
				mineLogged(new DescriptionFromWiki(), "Aggregation of champions from wiki");
				mineLogged(new DescriprionExtendedFromLol(), "Aggregation of champions descripton extended from lol");			
				return;
			}else{
				switch(ARG.valueOf(args[1])){
				case moba:
					mineLogged(new DescriptionFromMoba(), "Aggregation of champions from moba");
					return;
				case lol:
					mineLogged(new DescriptionFromLol(), "Aggregation of champions from lol");
				case wiki:
					mineLogged(new DescriptionFromWiki(), "Aggregation of champions from wiki");
					return;
				case extended:
					switch(ARG.valueOf(args[2])){
						case lol: 
							mineLogged(new DescriprionExtendedFromLol(), "Aggregation of champions descripton extended from lol");						
						return;
					}
				}		
			}
		case news:
			mineLogged(new News(), "Calculate news & progress");
			return;
		case items:	
			if(args.length<2){
				//default set
				mineLogged( new ItemsFromMoba(), "Aggregation of items from moba");
				mineLogged( new ItemsDiff(), "Calculate difference of base & aggregated items");
				return;
			}else{
				switch(ARG.valueOf(args[1])){
				case moba:
					mineLogged( new ItemsFromMoba(), "Aggregation of items from moba");
					return;
				case elohell:
					mineLogged( new ItemsFromElohell(), "Aggregation of items from elohell");
					return;
				case diff:
					mineLogged( new ItemsDiff(), "Calculate difference of base & aggregated items");
					return;
				case show:
					Manager.showItems();
					return;
				case sprite:
					mineLogged( new ItemsSprite(), "Create items sprite & update sprite data");
					return;
				}
			}
        }
		Log.log("Nothing to do");
	}
	
	private static void mineLogged(PropertyThread miner, String title) throws Exception {
		Log.log(title, " start");
		mine(miner);
		Log.log(title,  " done");
	}	
	
	private static void mine(PropertyThread miner) throws Exception {
		Properties prop = Utils.loadProperties("config/mining.properties");
		miner.setProperties(prop);
		miner.start();
		miner.join();
	}

}
