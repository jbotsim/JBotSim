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
package jbotsim.core;

import java.util.Vector;

public class Link extends _Properties{
    public enum Type{DIRECTED, UNDIRECTED};
    public enum Mode{WIRED, WIRELESS};
    protected Node from;
    protected Node to;
    protected boolean directed;
    protected boolean wireless;
    public Link(Node from, Node to) {
        this(from, to, Type.DIRECTED, Mode.WIRED);
    }
    public Link(Node from, Node to, Type type) {
        this(from, to, type, Mode.WIRED);
    }
    public Link(Node from, Node to, Mode mode) {
        this(from, to, Type.UNDIRECTED, mode);
    }
    public Link(Node from, Node to, Type type, Mode mode) {
        this.from = from;
        this.to = to;
        this.directed = (type==Type.DIRECTED)?true:false;
        this.wireless = (mode==Mode.WIRELESS)?true:false;
    }
    public double getLength(){
        return from.distance(to);
    }
    public int getWidth(){
        Integer width=(Integer)this.getProperty("lineWidth");
        return width!=null?width:1;
    }
    public void setWidth(int width){
        this.setProperty("lineWidth",new Integer(width));
    }
    public Node getSourceNode(){
        return from;
    }
    public Node getDestinationNode(){
        return to;
    }
    public Node getEndpoint(int num){
        return (num==1)?from:to;
    }
    public Vector<Node> getEndpoints(){
        Vector<Node> vec=new Vector<Node>();
        vec.add(from); vec.add(to);
        return vec;
    }
    public Node getOtherEndpoint(Node n){
        return (n==from)?to:from;
    }
    public boolean isWireless() {
        return wireless;
    }
    public boolean isDirected() {
        return directed;
    }
    public boolean equals(Object o){
        Link l=(Link)o;
        return ((this.directed == l.directed) &&
                (l.from==this.from && l.to==this.to) ||
                (!directed && (l.from==this.to && l.to==this.from)));
    }
    public String toString(){
        if (directed)
            return (from+" --> "+to);
        else
            return (from+" <--> "+to);
    }
}
