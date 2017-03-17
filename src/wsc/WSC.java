package wsc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ec.EvolutionState;
import ec.Individual;
import ec.Problem;
import ec.multiobjective.MultiObjectiveFitness;
import ec.simple.SimpleFitness;
import ec.simple.SimpleProblemForm;
import ec.util.Parameter;

public class WSC extends Problem implements SimpleProblemForm {

	private static final long serialVersionUID = 1L;

	public void setup(final EvolutionState state, final Parameter base) {
		// very important, remember this
		super.setup(state, base);
	}

	public void evaluate(final EvolutionState state, final Individual ind, final int subpopulation, final int threadnum) {
		if (!ind.evaluated) {
			WSCInitializer init = (WSCInitializer) state.initializer;
			WSCIndividual tree = (WSCIndividual) ind;

			double cost = 0.0;
			double availability = 1.0;
			double reliability = 1.0;

			Map<String, Double> timeMap = new HashMap<String, Double>();
	        double time = findLongestTime("end", tree, init, timeMap);

			for (String key : timeMap.keySet()) {
			    if (!key.equals( "start" ) && !key.equals( "end" )) {
    			    double[] qos = init.serviceMap.get( key ).getQos();
    			    cost += qos[WSCInitializer.COST];
    			    availability *= qos[WSCInitializer.AVAILABILITY];
    			    reliability *= qos[WSCInitializer.RELIABILITY];
			    }
			}

			double[] objectives = calculateFitness(availability, reliability, time, cost, init);
			tree.setAvailability(availability);
			tree.setReliability(reliability);
			tree.setTime(time);
			tree.setCost(cost);

			MultiObjectiveFitness f = ((MultiObjectiveFitness) ind.fitness);
			f.setObjectives(state, objectives);
			ind.evaluated = true;

//			// Find the unused fragments from the tree
//			Set<String> fragmentsToRemove = new HashSet<String>();
//			for (String s : tree.getPredecessorMap().keySet()) {
//			    if (!timeMap.containsKey(s))
//			        fragmentsToRemove.add(s);
//			}
//			// Clean up the unused fragments
//			for (String s : fragmentsToRemove)
//			    tree.getPredecessorMap().remove( s );

			// If performing encapsulation, add fragments to fragment count map
			if (WSCInitializer.countFragments) {
				for (Entry<String, Set<String>> e: tree.getPredecessorMap().entrySet()) {
					// Generate string representation for fragment
					String s = init.generateStringFromFragment(e.getKey(), e.getValue());
					// Update fragment count
					init.updateFragmentCount(s);
				}
			}
		}
	}

	private double findLongestTime(String select, WSCIndividual ind, WSCInitializer init, Map<String, Double> timeMap) {
	    if (!timeMap.containsKey( select )) {
	        double highestTime = 0.0;

	        for(String child: ind.getPredecessorMap().get( select )) {
	            double childValue;
	            if (timeMap.containsKey( child ))
	                childValue = timeMap.get( child );
	            else
	               childValue = findLongestTime(child, ind, init, timeMap);
                if (childValue > highestTime)
                    highestTime = childValue;
	        }

	        double serviceTime = 0.0;
	        if (!select.equals("start") && !select.equals("end"))
	            serviceTime = init.serviceMap.get(select).getQos()[WSCInitializer.TIME];
	        double overallTime = highestTime + serviceTime;
	        timeMap.put( select, overallTime );
	        return overallTime;
	    }
	    else {
	        return timeMap.get( select );
	    }
	}

	private double[] calculateFitness(double a, double r, double t, double c, WSCInitializer init) {
		a = normaliseAvailability(a, init);
		r = normaliseReliability(r, init);
		t = normaliseTime(t, init);
		c = normaliseCost(c, init);

        double[] objectives = new double[2];
        //objectives[GraphInitializer.AVAILABILITY] = a;
        //objectives[GraphInitializer.RELIABILITY] = r;
        //objectives[WSCInitializer.TIME] = t;
        //objectives[WSCInitializer.COST] = c;
        objectives[0] = t + c;
        objectives[1] = a + r;

        return objectives;
	}

	private double normaliseAvailability(double availability, WSCInitializer init) {
		if (init.maxAvailability - init.minAvailability == 0.0)
			return 1.0;
		else
			//return (availability - init.minAvailability)/(init.maxAvailability - init.minAvailability);
			return (init.maxAvailability - availability)/(init.maxAvailability - init.minAvailability);
	}

	private double normaliseReliability(double reliability, WSCInitializer init) {
		if (init.maxReliability - init.minReliability == 0.0)
			return 1.0;
		else
			//return (reliability - init.minReliability)/(init.maxReliability - init.minReliability);
			return (init.maxReliability - reliability)/(init.maxReliability - init.minReliability);
	}

	private double normaliseTime(double time, WSCInitializer init) {
		// If the time happens to go beyond the normalisation bound, set it to the normalisation bound
		if (time > init.maxTime)
			time = init.maxTime;

		if (init.maxTime - init.minTime == 0.0)
			return 1.0;
		else
			//return (init.maxTime - time)/(init.maxTime - init.minTime);
			return (time - init.minTime)/(init.maxTime - init.minTime);
	}

	private double normaliseCost(double cost, WSCInitializer init) {
		// If the cost happens to go beyond the normalisation bound, set it to the normalisation bound
		if (cost > init.maxCost)
			cost = init.maxCost;

		if (init.maxCost - init.minCost == 0.0)
			return 1.0;
		else
			//return (init.maxCost - cost)/(init.maxCost - init.minCost);
			return (cost - init.minCost)/(init.maxCost - init.minCost);
	}
}