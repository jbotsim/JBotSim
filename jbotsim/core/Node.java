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

import java.awt.geom.Point2D;
import java.util.LinkedHashSet;
import java.util.Vector;

import jbotsim.core.event.NodeListener;

@SuppressWarnings("unchecked")
public class Node extends _Properties{
    protected boolean wireless=true;
    protected double communicationRange=100.0;
    protected double sensingRange=0.0;
    protected Point2D.Double coords=new Point2D.Double();
    protected double direction=Math.PI/2;
    protected Vector<NodeListener> directedListeners=new Vector<NodeListener>();
    protected Vector<NodeListener> undirectedListeners=new Vector<NodeListener>();
    protected Vector<NodeAlgorithm> algorithms=new Vector<NodeAlgorithm>();
    protected Topology topology;
	
    public Node() {
        this((Node)null);
    }
    public Node(Node model){
        super(model);
        if (model!=null){
            wireless=model.wireless;
            communicationRange=model.communicationRange;
            sensingRange=model.sensingRange;
            for (NodeAlgorithm tmp : model.getAlgorithms()){
                try{
                    addAlgorithm((NodeAlgorithm)tmp.getClass().newInstance(), tmp.getRate());
                }catch(Exception e){e.printStackTrace();}
            }
        }
        this.setProperty("mailBox", new Vector<Message>());
    }
    public Topology getTopology(){
        return topology;
    }
    public double getX(){
        return coords.x;
    }
    public double getY(){
        return coords.y;
    }
    public double getCommunicationRange() {
        return communicationRange;
    }
    public double getSensingRange() {
        return sensingRange;
    }
    public void setCommunicationRange(double range) {
        this.communicationRange = range;
        this.setProperty("communicationRange", range);
    }
    public void setSensingRange(double range) {
        this.sensingRange = range;
        this.setProperty("sensingRange", range);
    }
    public boolean isWireless() {
        return wireless;
    }
    public void setWireless(boolean wireless) {
        this.wireless = wireless;
        this.setProperty("wireless", wireless);
    }
    public String getColor(){
        return (String)this.getProperty("color");
    }
    /**
     * Sets the color of the node to the given color. The color must be given as a string containing
     * its <u>name</u>. Supported names are those offered as a static field by java.awt.{@link Color}.
     */
    public void setColor(String color){
        this.setProperty("color", color);
    }
    /**
     * Adds the given NodeAlgorithm in the list of algorithm executed by this node, the
     * second argument sets the rate at which {@link performStep} will be called (in millisec).
     */
    public void addAlgorithm(NodeAlgorithm algo, int rate){
        algorithms.add(algo);
        algo.setRate(rate);
    }
    public Vector<NodeAlgorithm> getAlgorithms(){
        return new Vector<NodeAlgorithm>(algorithms);
    }
    public Point2D.Double getLocation(){
    	return new Point2D.Double(coords.x, coords.y);
    }
    public void setLocation(double x, double y){
        this.setProperty("lastX", coords.x);
        this.setProperty("lastY", coords.y);
        this.coords.setLocation(x, y);
        this.notifyNodeMoved();
    }
    public void setLocation(Point2D.Double loc){
    	setLocation(loc.x, loc.y);
    }
    public void translate(double dx, double dy){
        this.setLocation(coords.x+dx, coords.y+dy);
    }
    public double getDirection(){
        return this.direction;
    }
    public void setDirection(double angle){
        this.direction=angle;
        this.notifyNodeMoved();
    }
    public void setDirection(double x, double y){
        this.setDirection(Math.atan2(x-coords.x, -(y-coords.y))-Math.PI/2);
    }
    public void move(double distance){
        this.translate(Math.cos(direction)*distance, Math.sin(direction)*distance);
    }
    public Link getIncomingLinkFrom(Node n){
        return topology.getLink(n, this, true);
    }
    public Link getOutgoingLinkTo(Node n){
        return topology.getLink(this, n, true);
    }
    public Link getCommonLinkWith(Node n){
        return topology.getLink(this, n);
    }
    public Vector<Link> getIncomingLinks(){
        return (Vector<Link>)topology.getElements(true,this,2);
    }
    public Vector<Link> getOutgoingLinks(){
        return (Vector<Link>)topology.getElements(true,this,1);	
    }
    public Vector<Link> getLinks(){
        return getLinks(false);
    }
    public Vector<Link> getLinks(boolean directed){
        return (Vector<Link>)topology.getElements(directed,this,0);
    }
    public Vector<Node> getIncomingNeighbors(){
        Vector<Node> neighbors=new Vector<Node>();
        for (Link l : getIncomingLinks())
            neighbors.add(l.getSourceNode());
        return neighbors;
    }
    public Vector<Node> getOutgoingNeighbors(){
        Vector<Node> neighbors=new Vector<Node>();
        for (Link l : getOutgoingLinks())
            neighbors.add(l.getDestinationNode());
        return neighbors;
    }
    public Vector<Node> getNeighbors(){
        return getNeighbors(false);
    }
    public Vector<Node> getNeighbors(boolean directed){
        LinkedHashSet<Node> neighbors=new LinkedHashSet<Node>();
        for (Link l : getLinks(directed))
            neighbors.add(l.getOtherEndpoint(this));
        return new Vector<Node>(neighbors);
    }
    public Vector<Message> getCopyOfReceivedMessages(){
        return new Vector<Message>((Vector<Message>) this.getProperty("mailBox"));
    }
    public void removeReceivedMessage(Message msg){
        ((Vector<Message>) this.getProperty("mailBox")).remove(msg);
    }
    public void send(Message msg){
        ((Vector<Message>)this.getProperty("sendQueue")).add(msg);
    }
    public void addNodeListener(NodeListener listener){
        addNodeListener(listener, false);
    }
    public void addNodeListener(NodeListener listener, boolean directed){
        if (directed)
            directedListeners.add(listener);
        else
            undirectedListeners.add(listener);
    }
    public void removeNodeListener(NodeListener listener){
        removeNodeListener(listener, false);
    }
    public void removeNodeListener(NodeListener listener, boolean directed){
        if (directed)
            directedListeners.remove(listener);
        else
            undirectedListeners.remove(listener);
    }
    public void setProperty(String key, Object value){
        super.setProperty(key, value);
        notifyNodeChanged(key);
    }
    public double distance(Node n){
        return coords.distance(n.coords);
    }
    public double distance(double x, double y){
        return coords.distance(x, y);
    }
    protected void notifyNodeMoved(){
        LinkedHashSet<NodeListener> union=new LinkedHashSet<NodeListener>(directedListeners);
        union.addAll(undirectedListeners);
        for (NodeListener nl : new Vector<NodeListener>(union))
            nl.nodeMoved(this);
    }
    protected void notifyNodeChanged(String key){
        LinkedHashSet<NodeListener> union=new LinkedHashSet<NodeListener>(directedListeners);
        union.addAll(undirectedListeners);
        for (NodeListener nl : new Vector<NodeListener>(union))
            nl.nodeChanged(this, key);
    }
    public String toString(){
        String s=(String)this.getProperty("id");
        return (s==null)?super.toString().substring(13):s;
    }
}
