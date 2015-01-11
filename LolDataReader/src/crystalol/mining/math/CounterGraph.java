package crystalol.mining.math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import crystalol.mining.data.ChampionArc;
import crystalol.mining.data.ChampionCounters;
import crystalol.mining.data.CounterBrunches;
import crystalol.mining.data.CounterScales;

public class CounterGraph {
	private List<ChampionArc> arcs;
	
	private static final String ARC_DELIMETER = "|";
	private Map<String, ChampionArc> arcLinks; 
	
	public void build(Map<String, Map<String, CounterScales>> countersRaw)throws Exception {
		arcs = new ArrayList<ChampionArc>();
		arcLinks = new HashMap<String, ChampionArc>();
		
		for(Entry<String, Map<String, CounterScales>> entry:countersRaw.entrySet()){
			buildArc(entry.getKey(), entry.getValue());
		}
	}

	private void buildArc(String championEncodedName, Map<String, CounterScales> championCounters) throws Exception{
		for(Entry<String, CounterScales> entry: championCounters.entrySet()){
			String championCounterEncodedName = entry.getKey();
			CounterScales scale = entry.getValue();
			int up = scale.getUp();
			int down = scale.getDown();
		
			addWeight(championEncodedName, up, championCounterEncodedName, down);
		}
	}	

	private void addWeight(String champion1, int weight1, String champion2, int weight2) throws Exception{
		String leftToRight = champion1.concat(ARC_DELIMETER).concat(champion2);
		ChampionArc arc = arcLinks.get(leftToRight);
		if(arc!=null){
			arc.addWeightLeft(weight1);
			arc.addWeightRight(weight2);	
			return;
		}
		
		String rightToLeft = champion2.concat(ARC_DELIMETER).concat(champion1);
		arc = arcLinks.get(rightToLeft);
		if(arc!=null){
			arc.addWeightLeft(weight2);
			arc.addWeightRight(weight1);	
			return;
		}
		
		arc = new ChampionArc();
		
		arc.setNameLeft(champion1);
		arc.addWeightLeft(weight1);
		
		arc.setNameRight(champion2);
		arc.addWeightRight(weight2);	
		
		arcs.add(arc);
		arcLinks.put(leftToRight, arc);		
	}
	
	public void normalize(int weight, int power){
		for (Iterator<ChampionArc> it = arcs.iterator(); it.hasNext();){
	        ChampionArc arc = it.next();
	        if(arc.getWeightLeft() + arc.getWeightRight() < weight){  
	        	//delete counter with few votes
	        	it.remove();
	        }else{
		        arc.normalize();
		        //delete counter that almost equal
		        if(arc.getWeightLeft() == arc.getWeightRight() 
		        		|| arc.getWeightLeft() < power 
		        		|| arc.getWeightRight() < power){    
		        	it.remove();
		        }
	        }
		}
	}
	
	public CounterBrunches brunch() throws Exception{
		Map<String, ChampionCounters> counters = new HashMap<String, ChampionCounters>();
		
		for(ChampionArc arc:arcs){
			String nameLeft = arc.getNameLeft();
			ChampionCounters championLeft = counters.get(nameLeft);
			if(championLeft == null){
				championLeft = new ChampionCounters();
				championLeft.setNameEncoded(nameLeft);
				counters.put(nameLeft, championLeft);
			}
			
			String nameRight = arc.getNameRight();
			ChampionCounters championRight = counters.get(nameRight);
			if(championRight == null){
				championRight = new ChampionCounters();
				championRight.setNameEncoded(nameRight);
				counters.put(nameRight, championRight);
			}
			
			int weightLeft = arc.getWeightLeft();
			int weightRight = arc.getWeightRight();
			
			if(weightLeft == 100 && weightRight == 100){
				championLeft.addWeaker(nameRight, 0);
				championRight.addStronger(nameLeft, 0);			
			}else if(weightLeft == 100){
				championLeft.addWeaker(nameRight, weightRight);
				championRight.addStronger(nameLeft, weightRight);
			}else if(weightRight == 100){
				championRight.addWeaker(nameLeft, weightLeft);
				championLeft.addStronger(nameRight, weightLeft);				
			}else{
				throw new Exception("Wrong graph: " + nameRight + ":" + weightRight + ", " + nameLeft+weightLeft);
			}

		}
		

		return sort(counters);
	}

	private CounterBrunches sort(Map<String, ChampionCounters> countersMap) {
		List<ChampionCounters> counters = new ArrayList<ChampionCounters>(countersMap.values());
		Collections.sort(counters,new Comparator<ChampionCounters>(){
            public int compare(ChampionCounters f1, ChampionCounters f2){
                return f1.getNameEncoded().compareTo(f2.getNameEncoded());
            }        
        });
		
		for(ChampionCounters champion:counters){
			champion.sort();
		}
		
		CounterBrunches brunches = new CounterBrunches();
		brunches.setCounters(counters);
		return brunches;
	}
}
