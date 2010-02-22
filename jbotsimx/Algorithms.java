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

import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;
import static java.lang.Math.*;

import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Topology;

public class Algorithms {
	public class LinkLengthComparator implements Comparator<Link>{
		public int compare(Link l1, Link l2){
			return (new Double(l1.source.distance(l1.destination)).compareTo(
					new Double(l2.source.distance(l2.destination))));
		}
	}
    public static Vector<Link> getMST(Topology t){
        return getMST(t.getNodes(), t.getLinks());
    }
    public static Vector<Link> getMST(Vector<Node> Vset){
        HashSet<Link> Eset=new HashSet<Link>();
        for (Node ntmp : Vset){
            for (Link ltmp : ntmp.getLinks()){
                if (Vset.contains(ltmp.getOtherEndpoint(ntmp)))
                    Eset.add(ltmp);
            }
        }
        return getMST(Vset, new Vector<Link>(Eset));
    }
    private static Vector<Link> getMST(Vector<Node> Vset, Vector<Link> Eset){
        if (!Connectivity.isConnected(Vset))
            return null;
        Collections.sort(Eset, new Algorithms().new LinkLengthComparator());
		
        Vector<Node> Vmst=new Vector<Node>();
        Vector<Link> Emst=new Vector<Link>();
		
        Vmst.add(Vset.elementAt(0));
		
        while(Vmst.size()!=Vset.size()){
            boolean added=false;
            Enumeration<Link> en=Eset.elements();
            while(!added){
                if(en.hasMoreElements()){
                    Link l=en.nextElement();
                    if (Vmst.contains(l.source) && !Vmst.contains(l.destination)){
                        Vmst.add(l.destination);
                        Emst.add(l);
                        added=true;
                    }else if (Vmst.contains(l.destination) && !Vmst.contains(l.destination)){
                        Vmst.add(l.source);
                        Emst.add(l);
                        added=true;
                    }
                }
            }
        }
        return Emst;
    }
    public static double getAngle(Node n1, Node n2){
        return -atan2(n2.getY()-n1.getY(), n2.getX()-n1.getX());
    }
    // Returns between 0 and 2*PI
    public static double getAngle(Node n1, Node n2, Node n3){
	    double slope1=getAngle(n2,n1);
	    double slope2=getAngle(n2,n3);
	    double angle=(slope2-slope1);
	    if (angle<0) angle+=PI*2.0;
	    return angle;
	}
    // Returns between 0 and PI (abs value of the shortest side)
	public static double getAngleAbs(Node n1, Node n2, Node n3){
        double slope1=getAngle(n1,n2);
        double slope2=getAngle(n3,n2);
        double angle=abs(slope1-slope2);
        if (angle>PI) angle=(2.0*PI-angle);
        return angle;
    }
    public static Vector<Node> orderByAngleToReferenceNode(Node refNode, Vector<Node> nodes){
        Hashtable<Double, Node> tmp=new Hashtable<Double, Node>();
        for (Node ng : nodes)
            tmp.put(getAngle(refNode,ng), ng);
        Vector<Double> angles=new Vector<Double>(tmp.keySet());
        Collections.sort(angles);
        Vector<Node> result=new Vector<Node>();
        for (Double angle : angles)
            result.add(tmp.get(angle));
        return result;
    }
    public static double getRelativeCoverage(Topology topo){
    	double maximum=0.0;
    	for (Node n : topo.getNodes())
    		maximum+=PI*pow(n.getSensingRange(), 2);
    	return getCoverage(topo)/maximum;
    }
    public static double getCoverage(Topology topo){
        int left=0, right=0, top=0, bottom=0, maxSR=0;
        for (Node n : topo.getNodes()){
        	int x=(int)n.getX(); int y=(int)n.getY();
        	left=min(left,x); top=min(top,y);
        	right=max(right,x); bottom=max(bottom,y);
        	maxSR=max(maxSR, (int)n.getSensingRange());
        }
        double coverage = 0;
        for (int i=left-maxSR; i<right+maxSR; i++){
            for (int j=top-maxSR; j<bottom+maxSR; j++){
                for (Node n : topo.getNodes()){
                    if (n.distance(i,j) <= n.getSensingRange()){
                        coverage++;
                        break;
                    }
                }
            }
        }
        return coverage;
    }
    /**
     * Returns the diameter of the topology (largest shortest path between any two nodes)
     */
    public static Integer getDiameter(Topology t) {
        if (!Connectivity.isConnected(t))
            return null;
        int max = 0;
        for (Node node : t.getNodes()){
            int d = maxDistance(t, node);
            max = d > max ? d : max;
        }
        return max;
    }
    /**
     * Returns the length of the shortest path from this node to the farthest node
     */
    private static int maxDistance(Topology t, Node node){
        Hashtable<Node,Integer> distance = new Hashtable<Node,Integer>();

        for (Node n : t.getNodes()){
            distance.put(n, new Integer(t.getNodes().size()));
        }

        distance.put(node, new Integer(0));
        int depth = 1;
        int cnt = 1;
        Vector<Node> distList = new Vector<Node>();
        distList.addElement(node);
        while (distList.size() > 0) {
            Vector<Node> nextList = new Vector<Node>();
            for (Enumeration<Node> e = distList.elements(); e.hasMoreElements();){
                Vector<Node> nbrs = e.nextElement().getNeighbors();
                for (Node n2 : nbrs){
                    if (distance.get(n2).equals(new Integer(t.getNodes().size()))){
                        cnt++;
                        distance.put(n2, new Integer(depth));
                        nextList.addElement(n2);
                    }
                }
            }
            distList = nextList;
            depth++;
        }
        return cnt == t.getNodes().size() ? depth-2 : t.getNodes().size();
    }
}
