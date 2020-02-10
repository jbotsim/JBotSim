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

public class ForceVector{
    protected double x, y, weight;
	public ForceVector(){
		this.setXYW(0,0,0);
	}
	public ForceVector(double angle, double magnitude, double weight){
		this.setPolar(angle, magnitude, weight);
	}
	public void add(ForceVector vec){
		if (this.weight==0 && vec.weight==0)
			return;
		double tmpx=x*weight+vec.x*vec.weight;
		double tmpy=y*weight+vec.y*vec.weight;
		double tmpweight=weight+vec.weight;
		ForceVector tmp=new ForceVector();tmp.setXYW(tmpx/tmpweight, tmpy/tmpweight, tmpweight);
		x=tmp.x;y=tmp.y; weight=tmp.weight;
	}
	public void setXYW(double x, double y, double weight){
		this.x=x; this.y=y; this.weight=weight;
	}
	public void setPolar(double angle, double magnitude, double weight){
		x=Math.cos(angle)*magnitude; y=-Math.sin(angle)*magnitude; this.weight=weight;
	}
	public double getMagnitude(){
		return Math.sqrt(x*x+y*y);
	}
	public double getDirection(){
		return -Math.atan2(y, x);
	}
	public String toString(){
		return "("+x+","+y+","+weight+")";
	}
}
