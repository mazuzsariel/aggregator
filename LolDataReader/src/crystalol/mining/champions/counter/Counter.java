package crystalol.mining.champions.counter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

import crystalol.Log;
import crystalol.PropertyThread;
import crystalol.Utils;
import crystalol.UtilsLol;
import crystalol.mining.data.Champion;
import crystalol.mining.data.CounterBrunches;
import crystalol.mining.data.CounterScales;
import crystalol.mining.math.CounterGraph;

public class Counter extends PropertyThread {		
	@Override
	protected void process() throws Exception {		
		Map<String, Map<String, CounterScales>>  data = readChampions();
		
		mine(data);				
		save(data);
	}

	private void mine(Map<String, Map<String, CounterScales>> data)throws Exception {
		mine(new CounterMinerChapselect(), data, "mine chapionselect ...");
		mine(new CounterMinerElohell(), data, "mine elohell ...");
		mine(new CounterMinerCounterpicker(), data, "mine counterpicker ...");
	//	just few data
	//	mine(new CounterMinerSmaprtpick(), data, "mine smartpick ...");
	}

	private void mine(CounterMiner miner, Map<String, Map<String, CounterScales>> data, String msg) throws Exception{
		Log.log(msg);
		miner.mineData(data, this);
	}
	
	private void save(Map<String, Map<String, CounterScales>> data) throws Exception {
		CounterBrunches brunches = getBrunchers(data);
		Log.log("Saving ...");
		
		String date = Utils.getCurrentDateString();
		
		brunches.setDate(date);
		String json = new Gson().toJson(brunches);		
		File source = new File(getProperty("mining.champions.file.counters"));		
		FileUtils.writeStringToFile(source, json, Utils.ENCODING);		
	}

	private CounterBrunches getBrunchers(Map<String, Map<String, CounterScales>> data) throws Exception {
		CounterGraph graph = new CounterGraph();
		
		Log.log("Building graph ...");
		graph.build(data);
		
		int cutOffWeight = getPropertyInt("mining.champions.weight.min");
		int cutOffPower = getPropertyInt("mining.champions.power.min");

		Log.log("Normalizing graph ...");
		graph.normalize(cutOffWeight, cutOffPower);
		
		Log.log("Brunching graph ...");
		CounterBrunches brunches = graph.brunch();
		
		return brunches;		
	}

	private Map<String, Map<String, CounterScales>> readChampions() throws Exception {
		Map<String, Map<String, CounterScales>> data = new HashMap<String, Map<String,CounterScales>>();
		
		List<Champion> championsList = UtilsLol.readChampions(this);
		for(Champion champion: championsList){
			String nameEncoded = champion.getNameEncoded();
			data.put(nameEncoded, new HashMap<String, CounterScales>());
		}
		
		return data;
	}
}
