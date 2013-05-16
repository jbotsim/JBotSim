/*
 * This file is part of JBotSim.
 * 
 *    JBotSim is free software: you can redistribute it and/or modify it
 *    under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *  
 *    Authors:
 *    Arnaud Casteigts		<casteig@site.uottawa.ca>
 */
package jbotsim;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

import jbotsim.event.ConnectivityListener;
import jbotsim.event.MessageListener;
import jbotsim.event.MovementListener;

public class Node extends _Properties{
	static HashMap<String,Node> nodeModels=new HashMap<String,Node>();
    List<ConnectivityListener> cxDirectedListeners=new ArrayList<ConnectivityListener>();
    List<ConnectivityListener> cxUndirectedListeners=new ArrayList<ConnectivityListener>();
    List<MovementListener> movementListeners=new ArrayList<MovementListener>();
    List<MessageListener> messageListeners=new ArrayList<MessageListener>();
    List<Message> mailBox=new ArrayList<Message>();
    List<Message> sendQueue=new ArrayList<Message>();
    HashMap<Node,Link> outLinks=new HashMap<Node,Link>();
    Point2D.Double coords=new Point2D.Double();
    double direction=Math.PI/2;
    double communicationRange=100;
    double sensingRange=0;
    Topology topo;
    String color="none";
	
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
        if (model!=null){
        	this.properties=new HashMap<String,Object>(model.properties);
            this.communicationRange=model.communicationRange;
            this.sensingRange=model.sensingRange;
            this.color=model.color;
        }
    }
    /**
     * Returns the parent topology of this node, if any.
     * @return The parent topology, or <tt>null</tt> if the node is orphan.
     */
    public Topology getTopology(){
        return topo;
    }
    /**
     * Called once this node has been added to a topology, but before this
     * topology listeners are notified. This method is to be overwritten in the 
     * node class to perform topology related initialization. 
     */
    public void onTopologyAttachment(Topology tp){
    }
    /**
     * Called once this node has been removed from a topology, right after this
     * topology listeners are notified. This method is to be overwritten in the 
     * node class to perform topology related clean up. 
     */
    public void onTopologyDetachment(Topology tp){
    }
   /**
     * Returns the abscissa of this node.
     */
    public double getX(){
        return coords.x;
    }
    /**
     * Returns the ordinate of this node.
     */
    public double getY(){
        return coords.y;
    }
    /**
     * Returns the color of this node as a string.
     */
    public String getColor(){
    	return color;
    }
    /**
     * Sets the color of this node as a string.
     */
    public void setColor(String color){
    	String[] colors={"black","blue","cyan","darkGray","gray","green",
    			"lightGray","magenta","orange","pink","red","white","yellow"};
    	if (color.equals("random"))
    		this.color=colors[(new Random()).nextInt(colors.length)];
    	else{
    		this.color=(color==null)?"none":color;
    	}
		this.setProperty("color", color); // Used for property notification
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
     * Returns the model node corresponding to that name,
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
    public static List<String> getModelsNames(){
        return new ArrayList<String>(nodeModels.keySet());
    }
    /**
     * Create a new Node based on the specified model.
     * @param modelName
     * @return pouet
     */
    @SuppressWarnings("unchecked")
	public static Node newInstanceOfModel(String modelName){
    	Node model=getModel(modelName);
		Class modelClass=model.getClass();
    	try {
    		Node node = (Node) modelClass.getConstructor().newInstance();
    		node.properties=new HashMap<String,Object>(model.properties);
    		node.communicationRange=model.communicationRange;
    		node.sensingRange=model.sensingRange;
    		if (node.color=="none")
    			node.color=model.color;
    		return node;		
		} catch (Exception e) {
			System.err.println("Problem of model instantiation.."); return new Node();
		}
    }
    /**
     * Returns the location of this node (as a 2D point).
     */
    public Point2D getLocation(){
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
    public void setLocation(Point2D loc){
    	setLocation(loc.getX(), loc.getY());
    }
    /**
     * Changes this node's location modulo the size of topology.
     */
    public void wrapLocation(){
    	Dimension dim = this.topo.dimensions;
    	setLocation((coords.x + dim.width) % dim.width, (coords.y + dim.height) % dim.height);
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
     * @param p The reference point.
     */
    public void setDirection(Point2D p){
        this.setDirection(Math.atan2(p.getX()-coords.x, -(p.getY()-coords.y))-Math.PI/2);
    }
    /**
     * Translates the location of this node of the specified distance towards 
     * the node's current direction. The distance unit is the pixel.
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
    	return outLinks.get(n);
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
     * Returns a list containing all links for which this node is the
     * destination. The returned list can be subsequently modified without
     * effect on the topology.
     */
    public List<Link> getInLinks(){
        return topo.getLinks(true,this,2);
    }
    /**
     * Returns a list containing all links for which this node is the
     * source. The returned list can be subsequently modified without effect
     * on the topology.
     */
    public List<Link> getOutLinks(){
        return new ArrayList<Link>(outLinks.values()); 	
    }
    /**
     * Returns a list containing all undirected links adjacent to this node.
     * The returned list can be subsequently modified without effect on the
     * topology.
     */
    public List<Link> getLinks(){
        return getLinks(false);
    }
    /**
     * Returns a list containing all adjacent links of the specified type.
     * @param directed <tt>true</tt> for directed, <tt>false</tt> for 
     * undirected. The returned list can be subsequently modified without 
     * effect on the topology.
     */
    public List<Link> getLinks(boolean directed){
        return topo.getLinks(directed,this,0);
    }
    /**
     * Returns a list containing every node serving as source for an adjacent
     * directed link. The returned list can be subsequently modified 
     * without effect on the topology.
     * @return A list containing the neighbors, with possible duplicates
     * when several links come from a same neighbor.
     */
    public List<Node> getInNeighbors(){
        ArrayList<Node> neighbors=new ArrayList<Node>();
        for (Link l : getInLinks())
            neighbors.add(l.source);
        return neighbors;
    }
    /**
     * Returns a list containing every node serving as destination for an 
     * adjacent directed link. The returned list can be subsequently
     * modified without effect on the topology.
     * @return A list containing the neighbors, with possible duplicates
     * when several links go towards a same neighbor.
     */
    public List<Node> getOutNeighbors(){
        ArrayList<Node> neighbors=new ArrayList<Node>();
        for (Link l : getOutLinks())
            neighbors.add(l.destination);
        return neighbors;
    }
    /**
     * Returns a list containing every node located within the sensing range
     * The returned list can be modified without side effect.
     * @return A list containing all nodes within sensing range
     */
    public List<Node> getSensedObjects(){
        ArrayList<Node> sensedNodes=new ArrayList<Node>();
        for (Node n : this.topo.getNodes())
        	if (distance(n) < sensingRange && n!=this)
        		sensedNodes.add(n);
        return sensedNodes;
    }
    /**
     * Indicates whether this node has at least one neighbor (undirected)
     * @return <tt>true</tt> if it does, <tt>false</tt> if it does not.
     */
    public boolean hasNeighbors(){
    	return this.getLinks().size()>0;
    }
    /**
     * Returns a list containing every node located at the opposite endpoint
     * of an adjacent undirected links. The returned list can be
     * subsequently modified without effect on the topology.
     * @return A list containing the neighbors, with possible duplicates
     * when several links are shared with a same neighbor.
     */
    public List<Node> getNeighbors(){
        LinkedHashSet<Node> neighbors=new LinkedHashSet<Node>();
        for (Link l : this.getLinks())
            neighbors.add(l.getOtherEndpoint(this));
        return new ArrayList<Node>(neighbors);
    }
    /**
     * Returns a list of messages representing the mailbox of this node.
     * The mailbox can be usefull to scrutinize the arriving of new messages
     * without using the MessageListener interface, or to clear previous 
     * messages (since all received messages are retained in the mailbox). The
     * returned list must be considered as the original copy of the node's
     * mailbox.
     */
    public List<Message> mailbox(){
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
     * Same method as <tt>send()</tt>, but retries to send the message later
     * if the link to the destination disappeared during transmission. 
     * (Does not work for <tt>null</tt> destinations.)
     * @param dest The non-null destination.
     * @param content The message.
     */
    public void sendRetry(Node dest, Object content){
    	assert(dest!=null);
        sendQueue.add(new Message(this, dest, content, true));
    }
    /**
     * Registers the specified node listener to this node. The listener
     * will be notified whenever an undirected link incident to this node 
     * appears or disappears.
     * @param listener The connectivity listener.
     */
    public void addConnectivityListener(ConnectivityListener listener){
        addConnectivityListener(listener, false);
    }
    /**
     * Registers the specified connectivity listener to this node. The listener
     * will be notified whenever a link incident to this node appears or 
     * disappears. Depending on the <tt>directed</tt> parameter, the listener
     * will be notified only for directed or undirected links. Listening both
     * types of links thus implies two different registrations by this method. 
     * @param listener The connectivity listener.
     * @param directed The type of link to be listened to.
     */
    public void addConnectivityListener(ConnectivityListener listener, boolean directed){
        if (directed)
            cxDirectedListeners.add(listener);
        else
            cxUndirectedListeners.add(listener);
    }
    /**
     * Unregisters the specified node listener for this node.
     * The listener will be removed from the list of 'undirected' listener. 
     * @param listener The connectivity listener. 
     */
    public void removeConnectivityListener(ConnectivityListener listener){
        removeConnectivityListener(listener, false);
    }
    /**
     * Unregisters the specified connectivity listener for this node.
     * Depending on the parameter <tt>directed</tt>, the listener will be
     * removed from the list of 'directed' or 'undirected' listeners.
     * @param listener The connectivity listener. 
     * @param directed The type of link that was listened to.
     */
    public void removeConnectivityListener(ConnectivityListener listener, boolean directed){
        if (directed)
            cxDirectedListeners.remove(listener);
        else
            cxUndirectedListeners.remove(listener);
    }
    /**
     * Registers the specified movement listener to this node. The listener
     * will be notified every time the location of this node changes. 
     * @param listener The movement listener.
     */
    public void addMovementListener(MovementListener listener){
        movementListeners.add(listener);
    }
    /**
     * Unregisters the specified movement listener for this node.
     * @param listener The movement listener. 
     */
    public void removeMovementListener(MovementListener listener){
        movementListeners.remove(listener);
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
     * Unregisters the specified message listener for this node.
     * @param listener The message listener. 
     */
    public void removeMessageListener(MessageListener listener){
    	messageListeners.remove(listener);
    }
    /**
     * Returns the distance between this node and the specified node.
     * @param n The other node.
     */
    public double distance(Node n){
        return coords.distance(n.coords);
    }
    /**
     * Returns the distance between this node and the specified location.
     * @param p The location (as a point).
     */
    public double distance(Point2D p){
        return coords.distance(p);
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
    	LinkedHashSet<MovementListener> union=new LinkedHashSet<MovementListener>(movementListeners);
    	if (topo!=null)
    		union.addAll(topo.movementListeners);
        for (MovementListener ml : union)
            ml.nodeMoved(this);
    }
    /**
     * Returns a string representation of this node.
     */
	public String toString(){
        String s=(String)super.getProperty("id");
        return (s==null)?super.toString():s;
    }
}
