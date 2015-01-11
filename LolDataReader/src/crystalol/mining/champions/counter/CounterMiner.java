package crystalol.mining.champions.counter;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import jodd.exception.UncheckedException;
import jodd.jerry.Jerry;
import jodd.jerry.JerryFunction;
import crystalol.Log;
import crystalol.PropertyReader;
import crystalol.Utils;
import crystalol.mining.data.CounterScales;

abstract public class CounterMiner implements PropertyReader{
	private static enum CounterStatus{WEAK, STRONG}

	protected static final String ITEM_UP = "up";
	protected static final String ITEM_DOWN = "down";;

	private Map<String, Map<String, CounterScales>> data;
	private PropertyReader properties;
	private boolean isCountersPresent = false;
	private boolean isChampionsPresent = false;
    private String championUrl;

	public void mineData(Map<String, Map<String, CounterScales>> data, PropertyReader propertyReader) throws Exception {
		properties = propertyReader;
		this.data = data;
		
		String source = Utils.getSource(getUrl());	
		parse(source);
		
		if(isRemoveDuplicates()){
			removeDuplicates();
		}
	}
	
	private void removeDuplicates() {
		for(Entry<String, Map<String, CounterScales>> champion:data.entrySet()){
			String championNameEncoded = champion.getKey();
			for(Entry<String, CounterScales> counter:champion.getValue().entrySet()){
				String counterNameEncoded = counter.getKey();
				data.get(counterNameEncoded).remove(championNameEncoded);
			}
		}
	}

	protected boolean isRemoveDuplicates() {
		return false;
	}

	protected String getChampionUrlDomain(){		
		return "";
	}

	protected void parseChampion(String nameEncoded, String source) throws Exception{
		parseChampionPage(nameEncoded, source);;
	}
	
	protected String getChampionUrlMask() {
		return null;		
	}

	protected String detectSiteChamptionEncodedName(String url){
		return null;
	}
	
	abstract protected String getUrl();

	abstract protected String getSelectorChampionsHolder();
	abstract protected String getSelectorChampionName();
	abstract protected String getSelectorChampionLink();
	abstract protected String getSelectorChampionsWeak();
	abstract protected String getSelectorChampionsStrong();
	abstract protected String getSelectorChampionCounterName();
	
	protected String getSelectorChampionUp() throws Exception {
		throw new Exception("unused");
	}

	protected String getSelectorChampionDown() throws Exception {
		throw new Exception("unused");
	}

	protected void parseChampionByUrl(String championUrl, String nameEncoded) throws Exception {
    	String championSource = Utils.getSource(championUrl); 	
    	parseChampion(nameEncoded, championSource);   
   	}
	
	protected void parse(String source) throws Exception {	
		Jerry doc = Jerry.jerry(source);

		final String SChampions = getSelectorChampionsHolder();
		final String SName = getSelectorChampionName();
		
		doc.$(SChampions).each(new JerryFunction() {
			public boolean onNode(Jerry $this, int index) {
	        	try{
		        	String name = (SName == null?$this.text():$this.$(SName).text());
		        	name = Utils.trim(name);
		        	
		        	String nameEncoded = Utils.encodeName(name);
		        	
		        	detectChampionUrl($this);
		        	
		    		Log.log(name.concat(" ..."), 1);		     
		        	parseChampionByUrl(getChampionUrl(), nameEncoded);
		         	Log.log("parsed", 2);

		        }catch(Exception e){
	        		throw new UncheckedException(e);
	        	}
	        	return true;
	        }
	    });		
		
		
		if(!isChampionsPresent){
			throw new Exception("Unable to find champions");
		}
	}

	private void detectChampionUrl(Jerry $this){
		final String SLink = getSelectorChampionLink();
		final String ADomain = getChampionUrlDomain();

		String championUrl = SLink == null ?
				$this.attr("href"):
				$this.$(SLink).attr("href");
				
    	championUrl = Utils.concatDomain(ADomain, championUrl);
    	String mask = getChampionUrlMask();
    	if(mask != null){
    		String encodedName = detectSiteChamptionEncodedName(championUrl);
    		championUrl = mask.replace("[name]", encodedName);
    	}
    	setChampionUrl(championUrl);
	}
	
	private void setChampionUrl(String url){
		championUrl = url;
	}
	
	protected String getChampionUrl(){
		return championUrl;
	}
	
	protected enum PARSE_SIDE{WEAK, STRONG, BOTH};
	protected void parseChampionPage(final String nameEncoded, String source, PARSE_SIDE parseSide) throws Exception{
		if(!isChampionsPresent)isChampionsPresent=true;
		
		Jerry doc = Jerry.jerry(source);
		
		if(PARSE_SIDE.WEAK.equals(parseSide) || PARSE_SIDE.BOTH.equals(parseSide)){
			final String SWeak = getSelectorChampionsWeak();
			doc.$(SWeak).each(new JerryFunction() {
			    public boolean onNode(Jerry $this, int index) {
			    	addCounter(nameEncoded, CounterStatus.WEAK, $this);
			    	return true;
			    }	
			});
		}

		if(PARSE_SIDE.STRONG.equals(parseSide) || PARSE_SIDE.BOTH.equals(parseSide)){
			final String SStrong = getSelectorChampionsStrong();
			doc.$(SStrong).each(new JerryFunction() {
			    public boolean onNode(Jerry $this, int index) {
			    	addCounter(nameEncoded, CounterStatus.STRONG, $this);
			    	return true;
			    }	
			});	
		}
		
		if(!isCountersPresent){
			throw new Exception("Unable to find counters: ".concat(nameEncoded));
		}		
	}
	
	protected void parseChampionPage(final String nameEncoded, String source) throws Exception{
		parseChampionPage(nameEncoded, source, PARSE_SIDE.BOTH);
	}
	
	private static String getVal(Jerry el){
		String val = el.text();
		if(val == null || val.length() == 0){
			val = el.attr("value");
		}
		return Utils.trim(val);
	}
	
	protected Map<String, String> getVotes(Jerry $this) throws Exception{
		String up = getVal($this.$(getSelectorChampionUp()));
		up = up.replace("+", "");
		up = up.replace(",", "");
		
		String down = getVal($this.$(getSelectorChampionDown()));			
		down = down.replace("-", "");
		down = down.replace(",", "");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(ITEM_UP, up);
		map.put(ITEM_DOWN, down);
		
		return map;
	}
	
	protected void addCounter(String nameEncoded, CounterStatus counterStatus, Jerry $this){
		try{
			String counterName = $this.$(getSelectorChampionCounterName()).text();
			if(counterName.contains("(")){
				counterName = counterName.substring(0, counterName.indexOf("("));
			}
			counterName = Utils.trim(counterName);
			//no champions found
			if(counterName.length() == 0) return;
			
			Map<String, String> votes = getVotes($this);
			String up = votes.get(ITEM_UP);
			String down = votes.get(ITEM_DOWN);
			
			if(counterStatus.equals(CounterStatus.WEAK)){
				String tmp = up;
				up = down;
				down = tmp;
			}

			String counterNameEncoded = Utils.encodeName(counterName);
			if(nameEncoded.equals(counterNameEncoded)) return;
			if(!isCountersPresent)isCountersPresent=true;
			
			Map<String, CounterScales> champion = getScales(nameEncoded);
			
			if(champion == null){
				//Log.log("Champion does not exist: " + nameEncoded);
				//champion = new HashMap<String, CounterScales>();
				//putScales(nameEncoded, champion);
				throw new Exception("Champion does not exist: " + nameEncoded);
				//Log.log("Champion does not exist: " + nameEncoded);
				//return;
			}
			
			if(getScales(counterNameEncoded) == null){
				//some sites have an empty slots with voices :)
				Log.log("Champion does not exist (Counter): " + counterNameEncoded);
				//throw new Exception("Champion does not exist (Counter): " + counterNameEncoded);
			}else{
				CounterScales counter = champion.get(counterNameEncoded);
				if(counter == null){
					counter = new CounterScales();
					champion.put(counterNameEncoded, counter);
				}
				counter.addUp(up);
				counter.addDown(down);
			}
			
		}catch(Exception e){
    		throw new UncheckedException(e);
    	}
	}

	public String getProperty(String key){
		return properties.getProperty(key);
	}
	
	public int getPropertyInt(String key){
		return properties.getPropertyInt(key);
	}
	
	public Map<String, CounterScales> getScales(String key){
		return data.get(key);
	}
	
	public void putScales(String key, Map<String, CounterScales> scales){
		data.put(key, scales);
	}	
}
