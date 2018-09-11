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
package examples.fancy.angularforces;

import jbotsim.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import static java.lang.Math.*;

public class Angles {
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
    public static ArrayList<Node> orderByAngleToReferenceNode(Node refNode, ArrayList<Node> nodes){
        Hashtable<Double, Node> tmp=new Hashtable<Double, Node>();
        for (Node ng : nodes)
            tmp.put(getAngle(refNode,ng), ng);
        ArrayList<Double> angles=new ArrayList<Double>(tmp.keySet());
        Collections.sort(angles);
        ArrayList<Node> result=new ArrayList<Node>();
        for (Double angle : angles)
            result.add(tmp.get(angle));
        return result;
    }
}
