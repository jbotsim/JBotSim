package examples.fancy.angularforces;

import jbotsim.Node;
import jbotsimx.Algorithms;

import java.util.ArrayList;
import java.util.Hashtable;

@SuppressWarnings("unchecked")
public class Forces {
	public static double Dth;

	// SPRING FORCES (can use the real position of nodes (because 1-hop))
	public static ForceVector getSpringForce(Node from, Node to){
		double distance=from.distance(to);
		double direction=(distance>Dth)?Algorithms.getAngle(to, from):Algorithms.getAngle(from, to);
		double correction=(Math.abs(distance-Dth)/2.0)/Dth;
		return new ForceVector(direction, getSpringMagnitude(correction), getSpringWeight(correction));
	}
	private static double getSpringMagnitude(double correction){
		return Dth*correction;
	}
	private static double getSpringWeight(double correction){
		return Math.exp(correction);
	}

	// ANGULAR FORCES (use the previous position of nodes (2-hop))
	public static ForceVector getAngularForces(Node from, Node me){
		ArrayList<Node> selection=new ArrayList<Node>();
		for (Node s : (ArrayList<Node>)from.getProperty("selection")){
			// this loop is to create image nodes that have the previous coordinate
			// in order to behave as specified in the paper. Note that node "me" still
			// use its current position, as it knows it.
			if (s!=me){
				Node scopy=new Node();
				scopy.setLocation((jbotsim.Point)s.getProperty("oldpos"));
				selection.add(scopy);
			}else
				selection.add(s);
		}
		ArrayList<Node> anglesLefts=new ArrayList(selection);
		Hashtable<Node,ForceVector> results=new Hashtable<Node,ForceVector>();
		// eliminate the largest angle
		if (selection.size()<6){
			double largestAngle=0; Node largestLeft=null;
			for (Node ng1 : selection){
				Node ng2 = selection.get((selection.indexOf(ng1)+1)%selection.size());
				double angle=Algorithms.getAngle(ng1, from, ng2);
				if (angle>largestAngle){
					largestAngle=angle;
					largestLeft=ng1;
				}
			}
			anglesLefts.remove(largestLeft);
		}
		for (Node ng1 : anglesLefts){
			Node ng2 = selection.get((selection.indexOf(ng1)+1)%selection.size());
			double cor=getAngularCorrection(from, ng1, ng2);
			double dir1=Algorithms.getAngle(ng1, from)-(Math.PI)/2.0+0.45;
			double dir2=Algorithms.getAngle(ng2, from)+(Math.PI)/2.0-0.45;
			ForceVector v1=new ForceVector(dir1, getAngularMagnitude(cor), getAngularWeight(cor));
			ForceVector v2=new ForceVector(dir2, getAngularMagnitude(cor), getAngularWeight(cor));
			if (results.containsKey(ng1)) results.get(ng1).add(v1); else results.put(ng1, v1);
			if (results.containsKey(ng2)) results.get(ng2).add(v2); else results.put(ng2, v2);
		}
		return results.get(me);
	}
	private static double getAngularCorrection(Node master, Node slave1, Node slave2){
		double angle = Algorithms.getAngle(slave1, master, slave2);
		return (angle < Math.PI/3.0)?0:(angle-Math.PI/3.0)/2.0;
	}
	private static double getAngularMagnitude(double correction){
		return Dth*Math.tan(correction);
	}
	private static double getAngularWeight(double correction){
		return 1.15*Math.exp(-6.222*Math.sqrt(correction));
	}
}
