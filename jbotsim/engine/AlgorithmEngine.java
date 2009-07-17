/*******************************************************************************
 * This file is part of JBotSim.
 * 
 *     JBotSim is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     JBotSim is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 * 
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with JBotSim.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *     contributors:
 *     Arnaud Casteigts
 *******************************************************************************/
package jbotsim.engine;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Collections;

import jbotsim.core.Node;
import jbotsim.core.NodeAlgorithm;
import jbotsim.core.Topology;

public class AlgorithmEngine extends AbstractEngine{
	protected Hashtable<NodeAlgorithm,Integer> waitingTimes;
	
	public AlgorithmEngine(Topology topo, int clockRate){
		super(topo,clockRate);
		waitingTimes=new Hashtable<NodeAlgorithm,Integer>();
	}
	public void onClock() {
		Vector<Node> nodes=topo.getNodes();
		for (Node node : nodes){
			for (NodeAlgorithm algo : node.getAlgorithms()){
				if (!algo.isInitialized()){
					algo.init(node);
					algo.setInitialized(true);
					waitingTimes.put(algo, algo.getRate());
				}
			}
		}
		if (running){
			Collections.shuffle(nodes);
			for (Node node : nodes){
				for (NodeAlgorithm algo : node.getAlgorithms()){
					if (waitingTimes.containsKey(algo)){
						Integer remainingTime=waitingTimes.get(algo);
						if (remainingTime==0){
							algo.performStep(node);
							waitingTimes.put(algo, algo.getRate());
						}else{
							waitingTimes.put(algo, remainingTime-1);
						}
					}
				}
			}
		}
	}
}
