package crystalol.mining.champions.news;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

import crystalol.Log;
import crystalol.PropertyThread;
import crystalol.Utils;
import crystalol.mining.data.ChampionCounters;
import crystalol.mining.data.CounterBrunches;
import crystalol.mining.data.CounterNews;

public class News extends PropertyThread {

	@Override
	protected void process() throws Exception {
		Log.log("Detect Changes ...");

		String pathFrom = getProperty("mining.champions.file.counters.from");
		CounterBrunches fromBrunches =  Utils.readJson(pathFrom, CounterBrunches.class);	

		String pathTo = getProperty("mining.champions.file.counters.to");
		CounterBrunches toBrunches =  Utils.readJson(pathTo, CounterBrunches.class);	
		
		int daysAgo = Utils.getDaysDiff(fromBrunches.getDate(), toBrunches.getDate());
		
		CounterNews news = new CounterNews();
		news.setCutoff(getPropertyInt("mining.champions.news.change.min"));
		news.setDatefrom(fromBrunches.getDate());
		news.setDateto(toBrunches.getDate());
		news.setDaysago(daysAgo);
		
		Map<String, ChampionCounters> countersFrom = fromBrunches.getCountersMap();
		List<ChampionCounters> counters = toBrunches.getCounters();
		for(ChampionCounters champion:counters){
			
			ChampionCounters championFrom = countersFrom.remove(champion.getNameEncoded());
			if(championFrom == null){
				news.addNewChampion(champion);
				champion.progressNew();				
			}else{
				champion.progressCalculate(championFrom, news);
			}
		}
		
		for(ChampionCounters championLost: countersFrom.values()){
			news.addLostChampion(championLost.getNameEncoded());
		}
		
		String json = new Gson().toJson(news);
		File source = new File(getProperty("mining.champions.file.news"));		
		FileUtils.writeStringToFile(source, json, Utils.ENCODING);		

		String jsonBrunch = new Gson().toJson(toBrunches);
		File sourceBrunch = new File(getProperty("mining.champions.file.news.progress"));		
		FileUtils.writeStringToFile(sourceBrunch, jsonBrunch, Utils.ENCODING);		
	}

}
