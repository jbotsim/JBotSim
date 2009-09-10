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
 *     Jeremie Albert
 *******************************************************************************/
package jbotsimx;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import jbotsim.core.Link;
import jbotsim.core.Node;
import jbotsim.core.Topology;

public class Connectivity {
    public static Vector<Node> getKHopNeighbors(Node n, int nbhops){
        Set<Node> neighbors=new HashSet<Node>();
        if (nbhops==1){
            neighbors.addAll(n.getNeighbors());
        }else{
            for(Node neigh : n.getNeighbors()){
                neighbors.add(neigh);
                neighbors.addAll(getKHopNeighbors(neigh, nbhops-1));
            }
        }
        neighbors.remove(n);
        return new Vector<Node>(neighbors);
    }
    public static Vector<Vector<Node>> splitIntoConnectedSets(Vector<Node> Vset){
        Vector<Vector<Node>> connectedSets=new Vector<Vector<Node>>();
        Vector<Node> Vinit=new Vector<Node>(Vset);
        while(!Vinit.isEmpty()){
            Vector<Node> nextConnectedSet=new Vector<Node>();
            nextConnectedSet.addAll(getConnectedSet(Vinit.elementAt(0), Vinit));
            Vinit.removeAll(nextConnectedSet);
            connectedSets.add(nextConnectedSet);
        }
        return connectedSets;
    }
    public static Vector<Node> getConnectedSet(Node node, Vector<Node> among) {
        Vector<Node> amongSet=new Vector<Node>(among);
        HashSet<Node> connectedSet = new HashSet<Node>();
        amongSet.remove(node);
        connectedSet.add(node);
        for (Node neigh : node.getNeighbors()){
            if (amongSet.contains(neigh)){
                amongSet.remove(neigh);
                connectedSet.addAll(getConnectedSet(neigh, amongSet));
            }
        }
        return new Vector<Node>(connectedSet);
    }
    public static boolean isBiconnected(Topology topo){
        if (!isConnected(topo))
            return false;
        for (Node n : topo.getNodes())
            if (isCritical(n))
                return false;
        return true;
    }
    public static boolean isCritical(Node n){
        return !isConnectedWithout(n.getTopology().getNodes(), n);
    }
    public static boolean isKHopCritical(Node n, int nbhops){
        Vector<Node> kneighbors=getKHopNeighbors(n, nbhops);
        if (kneighbors.size()<=1)
            return false;
        else
            return !isConnectedWithout(kneighbors, n);
    }
    public static boolean isConnectedWithout(Vector<Node> Vset, Node n){
        Set<Node> Vtmp=new HashSet<Node>(Vset);
        Vtmp.remove(n);
        return isConnected(new Vector<Node>(Vtmp));
    }
    public static boolean isConnected(Topology t){
        return isConnected(t.getNodes());
    }
    public static boolean isConnected(Vector<Node> Vset){
        if (Vset.size()<=1)
            return true;
        Vector<Node> vertices=new Vector<Node>(Vset);
        removeConnectedNodes(vertices,vertices.elementAt(0));
        return vertices.isEmpty();
    }
    private static void removeConnectedNodes(Vector<Node> Vtmp, Node v){
        Vtmp.remove(v);
        for(Link l : v.getLinks()){
            Node neighbor=l.getOtherEndpoint(v);
            if(Vtmp.contains(neighbor))
                removeConnectedNodes(Vtmp, neighbor);
        }
    }
    public static int getOptimalTopologySize(int nbNodes, double cRange, double ratio){
	double d = 0, pCo, pBico;
	do{ d+=0.000001;
	    pCo=1-(Math.exp(-d*Math.PI*Math.pow(cRange, 2)))*(1+d*Math.PI*Math.pow(cRange,2));
	    pBico=1-(Math.exp(-d*Math.PI*Math.pow(cRange, 2)))*(1+d*Math.PI*Math.pow(cRange,2)+Math.pow(d*Math.PI*cRange*cRange, 2)/2);
	} while (Math.pow(pCo/pBico,nbNodes)>ratio);
	if (d==0.000001){
	    System.err.println("In Connectivity.getOptimalSize : not enough precision!");
	    System.exit(1);
	}
	return (int)Math.round(Math.sqrt(nbNodes/d));
    }
    public static void addRandomConnectedNodes(Topology tp, int nbNodes){
    	double Cr=tp.getNodeModel("default").getCommunicationRange();
    	double Sr=tp.getNodeModel("default").getSensingRange();
    	int size = getOptimalTopologySize(nbNodes, Cr, 100);
    	int bordure = new Double(4*Sr).intValue();
    	Random rand = new Random();
    	Topology tmp=new Topology(tp);
    	tmp.getMessageEngine().setRunning(false);
    	tmp.getAlgorithmEngine().setRunning(false);
    	do{
    		tmp.clear();
    	    for (int i=0; i<nbNodes; i++)
    	    	tmp.addNode(rand.nextInt(size)+2*bordure, rand.nextInt(size)+1.5*bordure);
    	    // should update topology maintener here if not automatic (currently automatic)
    	} while (!Connectivity.isConnected(tmp) || Connectivity.isBiconnected(tmp));
    	for (Node n:tmp.getNodes())
    		tp.addNode(n.getX(), n.getY());
    }
    public static Topology createTopology(int nbNodes, double cRange, double sRange, double ratio){
	int size = getOptimalTopologySize(nbNodes, cRange, ratio);
	int bordure = new Double(4*sRange).intValue();
	int attempts = 0;
	Random rand = new Random();
	Topology topo = new Topology();
	topo.getNodeModel("default").setSensingRange(sRange);
	topo.getNodeModel("default").setCommunicationRange(cRange);
	topo.getAlgorithmEngine().setRunning(false);
	topo.getMessageEngine().setRunning(false);
	do{ attempts++;
	    topo.clear();
	    for (int i=0; i<nbNodes; i++)
		topo.addNode(rand.nextInt(size)+2*bordure, rand.nextInt(size)+1.5*bordure);
	} while (!Connectivity.isConnected(topo) || Connectivity.isBiconnected(topo));
	topo.getAlgorithmEngine().setRunning(true);
	topo.getMessageEngine().setRunning(true);
	topo.setProperty("attempts", attempts);
	return topo;
    }
}
