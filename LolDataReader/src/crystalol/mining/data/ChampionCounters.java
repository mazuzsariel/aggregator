package crystalol.mining.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChampionCounters {
	private String nameEncoded;
	private List<ChampionPower> stronger;
	private List<ChampionPower> weaker;
	
	public List<ChampionPower> getWeaker() {
		return weaker;
	}
	public void setWeaker(List<ChampionPower> weaker) {
		this.weaker = weaker;
	}
	public List<ChampionPower> getStronger() {
		return stronger;
	}
	public void setStronger(List<ChampionPower> stronger) {
		this.stronger = stronger;
	}
	public String getNameEncoded() {
		return nameEncoded;
	}
	public void setNameEncoded(String nameEncoded) {
		this.nameEncoded = nameEncoded;
	}

	public void addStronger(ChampionPower power){
		if(stronger == null) stronger = new ArrayList<ChampionPower>();
		stronger.add(power);
	}	

	public void addStronger(String name, int power){
		if(stronger == null) stronger = new ArrayList<ChampionPower>();
		add(stronger, name, power);
	}	
	
	public void addWeaker(String name, int power){
		if(weaker == null) weaker = new ArrayList<ChampionPower>();
		add(weaker, name, power);
	}	

	private static void add(List<ChampionPower> list, String name, int power){	
		ChampionPower champion = new ChampionPower();
		champion.setNameEncoded(name);
		champion.setPower(power);	
		list.add(champion);
	}	

	public void progressNew(){
		for(ChampionPower power:stronger){
			power.setProgress(power.getPower());
		}
		for(ChampionPower power:weaker){
			power.setProgress(power.getPower());
		}
	}

	public void progressCalculate(ChampionCounters championLast, CounterNews news){
		List<ChampionPower> newsPowerStronger = new ArrayList<ChampionPower>();
		List<ChampionPower> newsPowerWeaker = new ArrayList<ChampionPower>();
		
		progressCalculate(stronger, championLast.getStronger(), news.getCutoff(), newsPowerStronger);
		progressCalculate(weaker, championLast.getWeaker(), news.getCutoff(), newsPowerWeaker);
		
		if(newsPowerStronger.size()>0 || newsPowerWeaker.size()>0){
			ChampionCounters newsCounter = new ChampionCounters();
			newsCounter.setNameEncoded(getNameEncoded());
			if(newsPowerStronger.size()>0){
				newsCounter.setStronger(newsPowerStronger);
			}
			if(newsPowerWeaker.size()>0){
				newsCounter.setWeaker(newsPowerWeaker);
			}
			news.addCounter(newsCounter);
		}
	}
	
	private void progressCalculate(List<ChampionPower>powerList, List<ChampionPower> powerListLast, int cutoff, List<ChampionPower> newsPower) {
		Map<String, ChampionPower> powerMapLast = getPowerMap(powerListLast);
		for(ChampionPower power:powerList){
			ChampionPower powerLast = powerMapLast.remove(power.getNameEncoded());
			if(powerLast == null){
				power.setProgress(power.getPower());
				power.setStatusNew();
				newsPower.add(power);
			}else{
				int difference = power.getPower() - powerLast.getPower();
				power.setProgress(difference);
				power.setStatusDefault();
				if(Math.abs(difference)>=cutoff){
					newsPower.add(power);
				}
			}
		}
		
		for(ChampionPower powerLast: powerMapLast.values()){
			powerLast.setProgress(-powerLast.getPower());
			powerLast.setPower(0);
			powerLast.setStatusDeleted();
			newsPower.add(powerLast);
		}
	}

	private static Map<String, ChampionPower> getPowerMap(List<ChampionPower> powerList){
		Map<String, ChampionPower> map = new HashMap<String, ChampionPower>();
		
		for(ChampionPower champion:powerList){
			map.put(champion.getNameEncoded(), champion);
		}
		
		return map;
	}
	
	public void sort(){
		if(stronger == null){
			stronger = new ArrayList<ChampionPower>();
		}else{
			Collections.sort(stronger,new Comparator<ChampionPower>(){
	            public int compare(ChampionPower f1, ChampionPower f2){
	                return f2.getPower().compareTo(f1.getPower());
	            }        
	        });
		}

		if(weaker == null){
			weaker = new ArrayList<ChampionPower>();
		}else{
			Collections.sort(weaker,new Comparator<ChampionPower>(){
	            public int compare(ChampionPower f1, ChampionPower f2){
	                return f2.getPower().compareTo(f1.getPower());
	            }        
	        });
		}

	}
}
