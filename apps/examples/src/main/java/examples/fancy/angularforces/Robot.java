/*
 * Copyright 2008 - 2020, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
 *
 *
 * This file is part of JBotSim.
 *
 * JBotSim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JBotSim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JBotSim.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package examples.fancy.angularforces;

import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

import io.jbotsim.core.Point;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("unchecked")
public class Robot extends Node{
	public static double epsilon = 0.0025; // don't move under this value
	public static double dmax = 1; // Vmax / round-duration
	
	public Robot() {
		setIcon("/examples/fancy/angularforces/robot.png");
		setIconSize(18);
		setProperty("oldpos", getLocation());
	}
	public void onClock() {
		ArrayList<Node> neighbors=new ArrayList<Node>();
		for (Message msg : getMailbox()){
			neighbors.add(msg.getSender());
			msg.getSender().setProperty("position", ((HashMap<String, Object>)msg.getContent()).get("pos"));
			msg.getSender().setProperty("selection", ((HashMap<String, Object>)msg.getContent()).get("selec"));
		}
		getMailbox().clear();
		setProperty("selection", select(this, neighbors));
		Point nextPos=computeNextPosition(this);
		HashMap<String, Object> msgBody=new HashMap<String, Object>();
		msgBody.put("pos", nextPos);
		msgBody.put("selec", getProperty("selection"));
		sendAll(new Message(msgBody));
		setProperty("oldpos", getLocation());
        setLocation(nextPos);
	}
	public static ArrayList<Node> select(Node node, ArrayList<Node> neighbors){
		// Thanks to the sending of nextPos to all neighbors, the selection is made 
		// with real current positions.. so we use them directly for simplicity
		ArrayList<Node> res = new ArrayList<Node>();
		for (Node ng : neighbors){
			boolean selected = true;
			for (Node ng2 : neighbors)
				if (Angles.getAngleAbs(ng, node, ng2) < 3.0*Math.PI/10.0
						&& ng2!=ng && node.distance(ng2) < node.distance(ng))
					selected = false;
			if (selected) 
				res.add(ng);
		}
		return Angles.orderByAngleToReferenceNode(node, res);
	}
	protected static Point computeNextPosition(Node node){
		ArrayList<ForceVector> List=new ArrayList<ForceVector>();
		for (Node ng : (ArrayList<Node>)node.getProperty("selection")){
            if (((ArrayList<Node>)ng.getProperty("selection")).contains(node)){
				List.add(Forces.getSpringForce(ng, node));
				List.add(Forces.getAngularForces(ng, node));
			}
		}
		ForceVector Fres=getWeightedAverage(List);
		if (Fres.getMagnitude()>dmax)
			Fres=new ForceVector(Fres.getDirection(),dmax,0);
		if (Fres.getMagnitude()<epsilon)
			Fres=new ForceVector(0,0,0);
		return new Point(node.getX()+Fres.x, node.getY()+Fres.y);
	}
	protected static ForceVector getWeightedAverage(ArrayList<ForceVector> List){
		ForceVector Fres=new ForceVector();
		for (ForceVector F : List)
			Fres.add(F);
		return Fres;
	}
}
