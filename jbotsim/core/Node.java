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
package jbotsim;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Vector;

import jbotsim.event.MessageListener;
import jbotsim.event.NodeListener;

public class Node{
	static HashMap<String,Node> nodeModels=new HashMap<String,Node>();
    Vector<NodeListener> directedListeners=new Vector<NodeListener>();
    Vector<NodeListener> undirectedListeners=new Vector<NodeListener>();
    Vector<MessageListener> messageListeners=new Vector<MessageListener>();
    Vector<Message> mailBox=new Vector<Message>();
    Vector<Message> sendQueue=new Vector<Message>();
    HashMap<String,Object> properties;
    Point2D.Double coords=new Point2D.Double();
    double direction=Math.PI/2;
    double communicationRange=100;
    double sensingRange=0;
    Topology topo;
	
    /**
     * Creates a new node using the settings of a default model. FIXME
     */
    public Node() {
        this(Node.nodeModels.get("default"));
    }
    /**
     * FIXME
     * @param model
     */
    public Node(Node model){
    	this.properties=(model!=null)?new HashMap<String,Object>(model.properties):new HashMap<String,Object>();
        if (model!=null){
            this.communicationRange=model.communicationRange;
            this.sensingRange=model.sensingRange;
        }
    }
    /**
     * Returns the topology this node is member of, if any.
     * @return The parent topology, or <tt>null</tt> if the node has none.
     */
    public Topology getTopology(){
        return topo;
    }
    /**
     * Returns the abscissa of this node's location.
     */
    public double getX(){
        return coords.x;
    }
    /**
     * Returns the ordinate of this node's location.
     */
    public double getY(){
        return coords.y;
    }
    /**
     * Returns the communication range of this node (as a radius).
     */
    public double getCommunicationRange() {
        return communicationRange;
    }
    /**
     * Sets the communication range of this node to the specified radius. This
     * determines the distance up to which this node can <i>send</i> messages
     * to other nodes.
     */
    public void setCommunicationRange(double range) {
        this.communicationRange = range;
        if (topo!=null)
        	topo.updateWirelessLinksFor(this);
    }
    /**
     * Returns the sensing range of this node (as a radius).
     */
    public double getSensingRange() {
        return sensingRange;
    }
    /**
     * Sets the sensing range of this node to the specified radius.
     */
    public void setSensingRange(double range) {
        this.sensingRange = range;
    }
    /**
     * Returns the virtual node corresponding to the model whose name is given in argument,
     * all properties assigned to this virtual node will be given to further nodes created 
     * using this model name. If the requested model does not exist, it is created. FIXME
     */
    public static Node getModel(String modelName){
        if (!nodeModels.containsKey(modelName))
            nodeModels.put(modelName,new Node());
        return nodeModels.get(modelName);
    }
    /**
     * FIXME
     * @param id
     * @param n
     */
    public static void setModel(String id, Node n){
        nodeModels.put(id,n);
    }
    /**
     * Returns the list of all available model names. FIXME
     */
    public static Vector<String> getModelsNames(){
        return new Vector<String>(nodeModels.keySet());
    }
    /**
     * FIXME
     * @param modelName
     * @return pouet
     */
    public static Node newInstanceOfModel(String modelName){
    	try {
			return nodeModels.get(modelName).getClass().newInstance();
		} catch (Exception e) {return new Node();}
    }
    /**
     * Returns the location of this node (as a 2D point).
     */
    public Point2D.Double getLocation(){
    	return new Point2D.Double(coords.x, coords.y);
    }
    /**
     * Changes this node's location to the specified coordinates.
     * @param x The abscissa of the new location.
     * @param y The ordinate of the new location.
     */
    public void setLocation(double x, double y){
        this.coords.setLocation(x, y);
        if (topo!=null)
        	topo.updateWirelessLinksFor(this);
        this.notifyNodeMoved();
    }
    /**
     * Changes this node's location to the specified 2D point.
     * @param loc The new location point.
     */
    public void setLocation(Point2D.Double loc){
    	setLocation(loc.x, loc.y);
    }
    /**
     * Translates the location of this node by the specified coordinates.
     * @param dx The abscissa component.
     * @param dy The ordinate component.
     */
    public void translate(double dx, double dy){
        this.setLocation(coords.x+dx, coords.y+dy);
    }
    /**
     * Returns the current direction angle of this node (in radians).
     */
    public double getDirection(){
        return this.direction;
    }
    /**
     * Sets the direction angle of this node (in radians).
     * @param angle The angle in radians.
     */
    public void setDirection(double angle){
        this.direction=angle;
        this.notifyNodeMoved();
    }
    /**
     * Sets the direction angle of this node using the specified reference
     * point. Only the resulting angle matters (not the particular location of
     * the reference point).
     * @param x The abscissa of the reference point.
     * @param y The ordinate of the reference point.
     */
    public void setDirection(double x, double y){
        this.setDirection(Math.atan2(x-coords.x, -(y-coords.y))-Math.PI/2);
    }
    /**
     * Translates the location of this node of the specified distance towards 
     * the node's current direction.
     */
    public void move(double distance){
        this.translate(Math.cos(direction)*distance, Math.sin(direction)*distance);
    }
    /**
     * Returns the directed link whose destination is this node and source is
     * the specified node, if any such link exists.
     * @param n The source node.
     * @return The requested link, or <tt>null</tt> if no such link is found.
     */
    public Link getInLinkFrom(Node n){
        return topo.getLink(n, this, true);
    }
    /**
     * Returns the directed link whose source is this node and destination is
     * the specified node, if any such link exists.
     * @param n The destination node.
     * @return The requested link, or <tt>null</tt> if no such link is found.
     */
    public Link getOutLinkTo(Node n){
        return topo.getLink(this, n, true);
    }
    /**
     * Returns the undirected link whose endpoints are this node and the
     * specified node, if any such link exists.
     * @param n The node at the opposite endpoint.
     * @return The requested link, or <tt>null</tt> if no such link is found.
     */
    public Link getCommonLinkWith(Node n){
        return topo.getLink(this, n);
    }
    /**
     * Returns a vector containing all links for which this node is the
     * destination. The returned vector can be subsequently modified without
     * effect on the topology.
     */
    public Vector<Link> getInLinks(){
        return topo.getLinks(true,this,2);
    }
    /**
     * Returns a vector containing all links for which this node is the
     * source. The returned vector can be subsequently modified without effect
     * on the topology.
     */
    public Vector<Link> getOutLinks(){
        return topo.getLinks(true,this,1);	
    }
    /**
     * Returns a vector containing all undirected links adjacent to this node.
     * The returned vector can be subsequently modified without effect on the
     * topology.
     */
    public Vector<Link> getLinks(){
        return getLinks(false);
    }
    /**
     * Returns a vector containing all adjacent links of the specified type.
     * @param directed <tt>true</tt> for directed, <tt>false</tt> for 
     * undirected. The returned vector can be subsequently modified without 
     * effect on the topology.
     */
    public Vector<Link> getLinks(boolean directed){
        return topo.getLinks(directed,this,0);
    }
    /**
     * Returns a vector containing every node serving as source for an adjacent
     * <i>directed</i> link. The returned vector can be subsequently modified 
     * without effect on the topology.
     * @return A vector containing the neighbors, with possible duplicates
     * when several links come from a same neighbor.
     */
    public Vector<Node> getInNeighbors(){
        Vector<Node> neighbors=new Vector<Node>();
        for (Link l : getInLinks())
            neighbors.add(l.source);
        return neighbors;
    }
    /**
     * Returns a vector containing every node serving as destination for an 
     * adjacent <i>directed</i> link. The returned vector can be subsequently
     * modified without effect on the topology.
     * @return A vector containing the neighbors, with possible duplicates
     * when several links go towards a same neighbor.
     */
    public Vector<Node> getOutNeighbors(){
        Vector<Node> neighbors=new Vector<Node>();
        for (Link l : getOutLinks())
            neighbors.add(l.destination);
        return neighbors;
    }
    /**
     * Returns a vector containing every node located at the opposite endpoint
     * of adjacent <i>undirected</i> links. The returned vector can be
     * subsequently modified without effect on the topology.
     * @return A vector containing the neighbors, with possible duplicates
     * when several links are shared with a same neighbor.
     */
    public Vector<Node> getNeighbors(){
        LinkedHashSet<Node> neighbors=new LinkedHashSet<Node>();
        for (Link l : getLinks())
            neighbors.add(l.getOtherEndpoint(this));
        return new Vector<Node>(neighbors);
    }
    /**
     * Returns a vector of messages representing the mailbox of this node.
     * The mailbox can be usefull to scrutinize the arriving of new messages
     * without using the MessageListener interface, or to clear previous 
     * messages (since all received messages are retained in the mailbox). The
     * returned vector must be considered as the original copy of the node's
     * mailbox.
     */
    public Vector<Message> mailbox(){
        return mailBox;
    }
    /**
     * Sends a message from this node to the specified destination node. 
     * A <tt>null</tt> destination stands for the broadcast mode (that is, all
     * the neighbors of this node will receive the message). The content of the
     * message is specified as an object reference, to be passed 'as is' to the
     * destination(s). The effective transmission will occur at the
     * <tt>x<sup>th</sup></tt> following clock step, where <tt>x</tt> is the
     * message delay specified by the static method <tt>Message.setMessageDelay
     * </tt> (1 by default).
     * @param dest The destination node, or <tt>null</tt>.
     * @param content The object whose reference will be passed. 
     */
    public void send(Node dest, Object content){
        sendQueue.add(new Message(this, dest, content));
    }
    /**
     * Registers the specified node listener to this node. The listener
     * will be notified whenever the node changes a property or moves,
     * as well as when an <i>undirected</i> link incident to this node appears
     * or disappears. 
     * @param listener The node listener.
     */
    public void addNodeListener(NodeListener listener){
        addNodeListener(listener, false);
    }
    /**
     * Registers the specified node listener to this node. The listener
     * will be notified whenever the node changes a property or moves,
     * as well as when a link incident to this node appears or disappears. 
     * Depending on the <tt>directed</tt> parameter, the listener will be
     * notified only for directed or undirected links. Listening both types
     * of links thus implies two different registrations by this method. 
     * @param listener The node listener.
     * @param directed The type of link to be listened to.
     */
    public void addNodeListener(NodeListener listener, boolean directed){
        if (directed)
            directedListeners.add(listener);
        else
            undirectedListeners.add(listener);
    }
    /**
     * Unregisters the specified node listener from this node's listeners.
     * The listener will be removed from the list of 'undirected' listener. 
     * @param listener The node listener. 
     */
    public void removeNodeListener(NodeListener listener){
        removeNodeListener(listener, false);
    }
    /**
     * Unregisters the specified node listener from this node's listeners.
     * Depending on the parameter <tt>directed</tt>, the listener will be
     * removed from the list of 'directed' or 'undirected' listeners.
     * @param listener The node listener. 
     * @param directed The type of link that was listened to.
     */
    public void removeNodeListener(NodeListener listener, boolean directed){
        if (directed)
            directedListeners.remove(listener);
        else
            undirectedListeners.remove(listener);
    }
    /**
     * Registers the specified message listener to this node. The listener
     * will be notified every time a message is received at this node. 
     * @param listener The message listener.
     */
    public void addMessageListener(MessageListener listener){
    	messageListeners.add(listener);
    }
    /**
     * Unregisters the specified message listener from this node's listeners.
     * @param listener The message listener. 
     */
    public void removeMessageListener(MessageListener listener){
    	messageListeners.remove(listener);
    }
    /**
     * Returns the property stored under the specified name.
     * @param key The property name.
     */
    public Object getProperty(String key){
    	return properties.get(key);
    }
    /**
     * Stores the specified property (<tt>value</tt>) under the specified name
     * (<tt>key</tt>). 
     * @param key The property name.
     * @param value The property value.
     */
    public void setProperty(String key, Object value){
    	properties.put(key, value);
        notifyNodeChanged(key);
    }
    /**
     * Returns the distance between this node and the specified node.
     * @param n The other node.
     */
    public double distance(Node n){
        return coords.distance(n.coords);
    }
    /**
     * Returns the distance between this node and the point of specified 
     * coordinates.
     * @param x The abscissa of the point to which the distance is measured.
     * @param y The ordinate of the point to which the distance is measured.
     */
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
            nl.propertyChanged(this, key);
    }
    /**
     * Returns a string representation of this node.
     */
	public String toString(){
        String s=(String)this.getProperty("id");
        return (s==null)?super.toString().substring(13):s;
    }
}
