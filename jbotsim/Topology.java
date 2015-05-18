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

import java.awt.*;
import java.util.*;
import java.util.List;

import jbotsim.Link.Mode;
import jbotsim.Link.Type;
import jbotsim.event.*;

public class Topology extends _Properties{
    Clock clock = new Clock();
    List<ConnectivityListener> cxUndirectedListeners=new ArrayList<ConnectivityListener>();
    List<ConnectivityListener> cxDirectedListeners=new ArrayList<ConnectivityListener>();
    List<TopologyListener> topologyListeners=new ArrayList<TopologyListener>();
    List<MovementListener> movementListeners=new ArrayList<MovementListener>();
    List<MessageListener> messageListeners=new ArrayList<MessageListener>();
    List<SelectionListener> selectionListeners=new ArrayList<SelectionListener>();
    List<ResetListener> resetListeners=new ArrayList<ResetListener>();
    MessageEngine messageEngine=null;
    List<Node> nodes=new ArrayList<Node>();
    List<Link> arcs=new ArrayList<Link>();
    List<Link> edges=new ArrayList<Link>();
    HashMap<String,Class<Node>> nodeModels=new HashMap<String,Class<Node>>();
    boolean isWirelessEnabled = true;
    double communicationRange = 100;
    double sensingRange = 0;
    Dimension dimensions;
    WLinkCalculator wlinkcalc = new BasicWLinkCalculator();
    Node selectedNode = null;
    boolean linkUpdate = true;

    /**
     * Creates a topology.
     */
    public Topology(){
        this(600, 400);
    }
    /**
     * Creates a topology of given dimensions.
     */
    public Topology(int width, int height){
        setMessageEngine(new MessageEngine());
        setDimensions(width, height);
    }
    /**
     * Returns the node class corresponding to that name.
     */
    public Class<Node> getNodeModel(String modelName){
        return nodeModels.get(modelName);
    }
    /**
     * Returns the default node model,
     * all properties assigned to this virtual node will be given to further nodes created
     * without explicit model name.
     */
    public Class<Node> getDefaultNodeModel(){
        return getNodeModel("default");
    }
    /**
     * Adds the given node instance as a model.
     * @param modelName
     * @param nodeClass
     */
    public void setNodeModel(String modelName, Class nodeClass){
        nodeModels.put(modelName, nodeClass);
    }
    /**
     * Sets the default node model to the given node instance.
     * @param nodeClass
     */
    public void setDefaultNodeModel(Class nodeClass){
        setNodeModel("default", nodeClass);
    }
    /**
     * Returns the set registered node classes.
     */
    public Set<String> getModelsNames(){
        return nodeModels.keySet();
    }

    /**
     * Create a new instance of this type of node.
     * @param modelName
     * @return a new instance of this type of node
     */
    public Node newInstanceOfModel(String modelName){
        try {
            return (Node) getNodeModel(modelName).newInstance();
        } catch (Exception e) {
            return new Node();
       }
    }

    /**
     * Enables this node's wireless capabilities.
     */
    public void enableWireless(){
        isWirelessEnabled = true;
        for (Node node : nodes)
            node.enableWireless();
    }
    /**
     * Disables this node's wireless capabilities.
     */
    public void disableWireless(){
        isWirelessEnabled = false;
        for (Node node : nodes)
            node.disableWireless();
    }
    /**
     * Returns the default communication range.
     * @return the default communication range
     */
    public double getCommunicationRange() {
        return communicationRange;
    }

    /**
     * Sets the default communication range.
     * If the topology already has some nodes, their range is changed.
     * @param communicationRange The communication range
     */
    public void setCommunicationRange(double communicationRange) {
        this.communicationRange = communicationRange;
        for (Node node : nodes)
            node.setCommunicationRange(communicationRange);
    }

    /**
     * Returns the default sensing range,
     * @return the default sensing range
     */
    public double getSensingRange() {
        return sensingRange;
    }

    /**
     * Sets the default sensing range.
     * If the topology already has some nodes, their range is changed.
     * @param sensingRange The sensing range
     */
    public void setSensingRange(double sensingRange) {
        this.sensingRange = sensingRange;
        for (Node node : nodes)
            node.setSensingRange(sensingRange);
    }

    /**
     * Gets a reference on the message engine of this topology.
     */
    public MessageEngine getMessageEngine() {
        return messageEngine;
    }
    /**
     * Sets the message engine of this topology.
     */
    public void setMessageEngine(MessageEngine messageEngine) {
        if (messageEngine != null)
            clock.removeClockListener(this.messageEngine);
        this.messageEngine = messageEngine;
        messageEngine.setTopology(this);
        clock.addClockListener(messageEngine, 1);
    }

    /**
     * Returns the global duration of a round in this topology (in millisecond).
     * @return The duration
     */
    public int getClockSpeed(){
        return clock.getTimeUnit();
    }

    /**
     * Sets the global duration of a round in this topology (in millisecond).
     * @param period The desired duration
     */
    public void setClockSpeed(int period){
        clock.setTimeUnit(period);
    }

    /**
     * Returns the current time (current round number)
     */
    public int getTime(){
        return clock.currentTime();
    }
    /**
     * Sets the topology dimensions as indicated.
     */

    /**
     * Indicates whether the internal clock is currently running or in pause.
     * @return <tt>true</tt> if running, <tt>false</tt> if paused.
     */
    public boolean isRunning(){
        return clock.isRunning();
    }

    /**
     * Pauses the clock (freezes time and stops to send onClock() events to
     * listeners).
     */
    public void pause(){
        clock.pause();
    }

    /**
     * Resumes the clock if it was paused.
     */
    public void resume(){
        clock.resume();
    }

    /**
     * Reset the round number to 0.
     */
    public void resetTime(){
        clock.reset();
    }

    public void setDimensions(int width, int height){
        dimensions = new Dimension(width,height);
    }
    /**
     * Returns the topology dimensions.
     */
    public Dimension getDimensions(){
        return new Dimension(dimensions);
    }
    /**
     * Reset the color and width of nodes and links, then calls the
     * onStart() method on each node.
     */
    public void reset(){
        clearMessages();
        for (Link link : edges) {
            link.setWidth(1);
            link.setColor(Color.black);
        }
        for (Node node : nodes)
            node.setColor(null);
        for (ResetListener listener : resetListeners)
            listener.onReset();
        for (Node n : nodes)
            n.onStart();
    }
    /**
     * Removes all the nodes (and links) of this topology.
     */
    public void clear(){
        for (Node n : new ArrayList<Node>(nodes))
            removeNode(n);
    }
    /**
     * Removes all the ongoing messages in this topology.
     */
    public void clearMessages(){
        for (Node n : nodes) {
            n.sendQueue.clear();
            n.mailBox.clear();
        }
    }
    /**
     * Adds the specified node to this topology. The location of the node
     * in the topology will be its current inherent location (or <tt>(0,0)</tt>
     * if no location was prealably given to it).
     * @param n The node to be added.
     */
    public void addNode(Node n){
        addNode(n.getX(), n.getY(), n);
    }
    /**
     * Adds a new node to this topology at the specified location. The node
     * will be created using default settings FIXME
     * @param x The abscissa of the location.
     * @param y The ordinate of the location.
     */
    public void addNode(double x, double y){
        addNode(x, y, newInstanceOfModel("default"));
    }
    /**
     * Adds the specified node to this topology at the specified location.
     * @param x The abscissa of the location.
     * @param y The ordinate of the location.
     * @param n The node to be added.
     */
    public void addNode(double x, double y, Node n){
        boolean wasRunning=false;
        if (clock.isRunning()){
            clock.pause();
            wasRunning=true;
        }
        clock.pause(); // to be removed.
        if (x == -1)
            x = (new Random()).nextDouble() * (dimensions.width - 12) + 6;
        if (y == -1)
            y = (new Random()).nextDouble() * (dimensions.height - 12) + 6;
        if (n.getX()==0 && n.getY()==0)
            n.setLocation(x, y);

        if (n.communicationRange == null)
            n.setCommunicationRange(communicationRange);
        if (n.sensingRange == null)
            n.setSensingRange(sensingRange);
        if (isWirelessEnabled == false)
            n.disableWireless();
        nodes.add(n);
        n.topo=this;
        n.onTopologyAttachment(this);
        notifyNodeAdded(n);
        clock.addClockListener(n, n.clockSpeed);
        n.onStart();
        updateWirelessLinksFor(n);
        if (wasRunning)
            clock.resume();
    }
    /**
     * Removes the specified node from this topology. All adjacent links will
     * be automatically removed.
     * @param n The node to be removed.
     */
    public void removeNode(Node n){
        boolean wasRunning=false;
        if (clock.isRunning()){
            clock.pause();
            wasRunning=true;
        }
        for (Link l : n.getLinks(true))
            removeLink(l);
        notifyNodeRemoved(n);
        removeClockListener(n);
        nodes.remove(n);
        for (Node n2 : nodes){
            if (n2.sensedNodes.contains(n)){
                n2.sensedNodes.remove(n);
                n2.onSensingOut(n);
            }
        }
        n.onTopologyDetachment(this);
        n.topo=null;
        if (wasRunning)
            clock.resume();
    }
    public void selectNode(Node n){
        boolean wasRunning=false;
        if (clock.isRunning()){
            clock.pause();
            wasRunning=true;
        }
        selectedNode = n;
        n.onSelection();
        notifyNodeSelected(n);
        if (wasRunning)
            clock.resume();
    }
    /**
     * Adds the specified link to this topology. Calling this method makes
     * sense only for wired links, since wireless links are automatically
     * managed as per the nodes' communication ranges.
     * @param l The link to be added.
     */
    public void addLink(Link l){
        addLink(l, false);
    }
    /**
     * Adds the specified link to this topology without notifying the listeners
     * (if silent is true). Calling this method makes sense only for wired 
     * links, since wireless links are automatically managed as per the nodes'
     * communication ranges.
     * @param l The link to be added.
     */
    public void addLink(Link l, boolean silent){
        if (l.type==Type.DIRECTED){
            arcs.add(l);
            l.source.outLinks.put(l.destination, l);
            if (l.destination.outLinks.containsKey(l.source)){
                Link edge=new Link(l.source,l.destination,Link.Type.UNDIRECTED,l.mode);
                edges.add(edge);
                if (!silent)
                    notifyLinkAdded(edge);
            }
        }else{ // UNDIRECTED
            Link arc1 = l.source.outLinks.get(l.destination);
            Link arc2 = l.destination.outLinks.get(l.source);
            if (arc1 == null){
                arc1 = new Link(l.source,l.destination,Link.Type.DIRECTED);
                arcs.add(arc1);
                arc1.source.outLinks.put(arc1.destination, arc1);
                if (!silent)
                    notifyLinkAdded(arc1);
            }else{
                arc1.mode = l.mode;
            }
            if (arc2 == null){
                arc2 = new Link(l.destination,l.source,Link.Type.DIRECTED);
                arcs.add(arc2);
                arc2.source.outLinks.put(arc2.destination, arc2);
                if (!silent)
                    notifyLinkAdded(arc2);
            }else{
                arc2.mode = l.mode;
            }
            edges.add(l);
        }
        if (!silent)
            notifyLinkAdded(l);
    }
    /**
     * Removes the specified link from this topology. Calling this method makes
     * sense only for wired links, since wireless links are automatically
     * managed as per the nodes' communication ranges.
     * @param l The link to be removed.
     */
    public void removeLink(Link l){
        if (l.type==Type.DIRECTED){
            arcs.remove(l);
            l.source.outLinks.remove(l.destination);
            Link edge=getLink(l.source, l.destination, false);
            if (edge!=null){
                edges.remove(edge);
                notifyLinkRemoved(edge);
            }
        }else{
            Link arc1=getLink(l.source, l.destination, true);
            Link arc2=getLink(l.destination, l.source, true);
            arcs.remove(arc1);
            arc1.source.outLinks.remove(arc1.destination);
            notifyLinkRemoved(arc1);
            arcs.remove(arc2);
            arc2.source.outLinks.remove(arc2.destination);
            notifyLinkRemoved(arc2);
            edges.remove(l);
        }
        notifyLinkRemoved(l);
    }
    /**
     * Returns true if this topology has at least one directed link.
     */
    public boolean hasDirectedLinks(){
        return arcs.size()>2*edges.size();
    }
    /**
     * Returns a list containing all the nodes in this topology. The returned
     * ArrayList can be subsequently modified without effect on the topology.
     */
    public List<Node> getNodes(){
        return new ArrayList<Node>(nodes);
    }
    /**
     * Returns a list containing all undirected links in this topology. The 
     * returned ArrayList can be subsequently modified without effect on the
     * topology.
     */
    public List<Link> getLinks(){
        return getLinks(false);
    }
    /**
     * Returns a list containing all links of the specified type in this
     * topology. The returned ArrayList can be subsequently modified without
     * effect on the topology.
     * @param directed <tt>true</tt> for directed links, <tt>false</tt> for
     * undirected links.
     */
    public List<Link> getLinks(boolean directed){
        return (directed)?new ArrayList<Link>(arcs):new ArrayList<Link>(edges);
    }
    List<Link> getLinks(boolean directed, Node n, int pos){
        List<Link> result=new ArrayList<Link>();
        List<Link> allLinks=(directed)?arcs:edges;
        for(Link l : allLinks)
            switch(pos){
                case 0:	if(l.source==n || l.destination==n)
                    result.add(l); break;
                case 1:	if(l.source==n)
                    result.add(l); break;
                case 2:	if(l.destination==n)
                    result.add(l); break;
            }
        return result;
    }
    /**
     * Returns the undirected link shared the specified nodes, if any.
     * @return The requested link, if such a link exists, <tt>null</tt> 
     * otherwise.
     */
    public Link getLink(Node n1, Node n2){
        return getLink(n1, n2, false);
    }
    /**
     * Returns the link of the specified type between the specified nodes, if
     * any.
     * @return The requested link, if such a link exists, <tt>null</tt> 
     * otherwise.
     */
    public Link getLink(Node from, Node to, boolean directed){
        if (directed){
            return from.outLinks.get(to);
            //Link l=new Link(from, to,Link.Type.DIRECTED);
            //int pos=arcs.indexOf(l);
            //return (pos != -1)?arcs.get(pos):null;
        }else{
            Link l=new Link(from, to, Link.Type.UNDIRECTED);
            int pos=edges.indexOf(l);
            return (pos != -1)?edges.get(pos):null;
        }
    }
    /**
     * Replaces the default Wireless Link Calculator by a custom one.
     * @param wlinkcalc An object that implements WLinkCalculator.
     */
    public void setWLinkCalculator(WLinkCalculator wlinkcalc){
        this.wlinkcalc = wlinkcalc;
    }
    /**
     * Registers the specified topology listener to this topology. The listener
     * will be notified whenever an undirected link is added or removed.
     * @param listener The listener to add.
     */
    public void addConnectivityListener(ConnectivityListener listener){
        cxUndirectedListeners.add(listener);
    }
    /**
     * Registers the specified connectivity listener to this topology. The 
     * listener will be notified whenever a link of the specified type is 
     * added or removed.
     * @param listener The listener to register.
     * @param directed The type of links to be listened (<tt>true</tt> for 
     * directed, <tt>false</tt> for undirected).
     */
    public void addConnectivityListener(ConnectivityListener listener, boolean directed){
        if (directed)
            cxDirectedListeners.add(listener);
        else
            cxUndirectedListeners.add(listener);
    }
    /**
     * Unregisters the specified connectivity listener from the 'undirected' 
     * listeners.
     * @param listener The listener to unregister.
     */
    public void removeConnectivityListener(ConnectivityListener listener){
        cxUndirectedListeners.remove(listener);
    }
    /**
     * Unregisters the specified connectivity listener from the listeners 
     * of the specified type.
     * @param listener The listener to unregister.
     * @param directed The type of links that this listener was listening 
     * (<tt>true</tt> for directed, <tt>false</tt> for undirected).
     */
    public void removeConnectivityListener(ConnectivityListener listener, boolean directed){
        if (directed)
            cxDirectedListeners.remove(listener);
        else
            cxUndirectedListeners.remove(listener);
    }
    /**
     * Registers the specified movement listener to this topology. The
     * listener will be notified every time the location of a node changes. 
     * @param listener The movement listener.
     */
    public void addMovementListener(MovementListener listener){
        movementListeners.add(listener);
    }
    /**
     * Unregisters the specified movement listener for this topology.
     * @param listener The movement listener. 
     */
    public void removeMovementListener(MovementListener listener){
        movementListeners.remove(listener);
    }
    /**
     * Registers the specified topology listener to this topology. The listener
     * will be notified whenever the a node is added or removed.
     * @param listener The listener to register.
     */
    public void addTopologyListener(TopologyListener listener){
        topologyListeners.add(listener);
    }
    /**
     * Unregisters the specified topology listener.
     * @param listener The listener to unregister.
     */
    public void removeTopologyListener(TopologyListener listener){
        topologyListeners.remove(listener);
    }
    /**
     * Registers the specified message listener to this topology. The listener
     * will be notified every time a message is received at any node. 
     * @param listener The message listener.
     */
    public void addMessageListener(MessageListener listener){
        messageListeners.add(listener);
    }
    /**
     * Unregisters the specified message listener for this topology.
     * @param listener The message listener. 
     */
    public void removeMessageListener(MessageListener listener){
        messageListeners.remove(listener);
    }
    /**
     * Registers the specified selection listener to this topology. The listener
     * will be notified every time a node is selected. 
     * @param listener The selection listener.
     */
    public void addSelectionListener(SelectionListener listener){
        selectionListeners.add(listener);
    }
    /**
     * Unregisters the specified selection listener for this topology.
     * @param listener The selection listener. 
     */
    public void removeSelectionListener(SelectionListener listener){
        selectionListeners.remove(listener);
    }
    /**
     * Registers the specified reset listener to this topology. The listener
     * will be notified every time a reset is requested on the topology.
     * @param listener The reset listener.
     */
    public void addResetListener(ResetListener listener){
        resetListeners.add(listener);
    }
    /**
     * Unregisters the specified selection listener for this topology.
     * @param listener The reset listener.
     */
    public void removeResetListener(ResetListener listener){
        resetListeners.remove(listener);
    }
    /**
     * Registers the specified listener to the events of the topology clock.
     * @param listener The listener to register.
     * @param period The number of rounds between consecutive onClock() events,
     * in time units.
     */
    public void addClockListener(ClockListener listener, int period){
        clock.addClockListener(listener, period);
    }
    /**
     * Registers the specified listener to the events of the topology clock.
     * @param listener The listener to register.
     */
    public void addClockListener(ClockListener listener){
        clock.addClockListener(listener);
    }
    /**
     * Unregisters the specified listener. (The <tt>onClock()</tt> method of this
     * listener will not longer be called.)
     * @param listener The listener to unregister.
     */
    public void removeClockListener(ClockListener listener){
        clock.removeClockListener(listener);
    }
    protected void notifyLinkAdded(Link l){
        if (l.type==Type.DIRECTED) {
            l.endpoint(0).onDirectedLinkAdded(l);
            l.endpoint(1).onDirectedLinkAdded(l);
            for (ConnectivityListener cl : cxDirectedListeners)
                cl.onLinkAdded(l);
        }else {
            l.endpoint(0).onLinkAdded(l);
            l.endpoint(1).onLinkAdded(l);
            for (ConnectivityListener cl : cxUndirectedListeners)
                cl.onLinkAdded(l);
        }
    }
    protected void notifyLinkRemoved(Link l){
        if (l.type==Type.DIRECTED) {
            l.endpoint(0).onDirectedLinkRemoved(l);
            l.endpoint(1).onDirectedLinkRemoved(l);
            for (ConnectivityListener cl : cxDirectedListeners)
                cl.onLinkRemoved(l);
        }else {
            l.endpoint(0).onLinkRemoved(l);
            l.endpoint(1).onLinkRemoved(l);
            for (ConnectivityListener cl : cxUndirectedListeners)
                cl.onLinkRemoved(l);
        }
    }
    protected void notifyNodeAdded(Node node){
        for (TopologyListener tl : new ArrayList<TopologyListener>(topologyListeners))
            tl.onNodeAdded(node);
    }
    protected void notifyNodeRemoved(Node node){
        for (TopologyListener tl : new ArrayList<TopologyListener>(topologyListeners))
            tl.onNodeRemoved(node);
    }
    protected void notifyNodeSelected(Node node){
        for (SelectionListener tl : new ArrayList<SelectionListener>(selectionListeners))
            tl.onSelection(node);
    }
    void updateWirelessLinksFor(Node n){
        for (Node n2 : nodes)
            if (n2!=n){
                updateWirelessLink(n, n2);
                updateWirelessLink(n2, n);
            }
        for (Node n2 : new ArrayList<Node>(nodes)) {
            if (n2 != n) {
                updateSensedNodes(n, n2);
                updateSensedNodes(n2, n);
            }
        }
    }
    void updateWirelessLink(Node n1, Node n2){
        Link l = n1.getOutLinkTo(n2);
        boolean linkExisted = (l==null)?false:true;
        boolean linkExists = wlinkcalc.isHeardBy(n1, n2);
        if (!linkExisted && linkExists)
            addLink(new Link(n1,n2,Type.DIRECTED,Mode.WIRELESS));
        else if (linkExisted && l.isWireless() && !linkExists)
            removeLink(l);
    }
    void updateSensedNodes(Node from, Node to){
        if (from.distance(to) < from.sensingRange) {
            if (!from.sensedNodes.contains(to)) {
                from.sensedNodes.add(to);
                from.onSensingIn(to);
            }
        } else if (from.sensedNodes.contains(to)) {
            from.sensedNodes.remove(to);
            from.onSensingOut(to);
        }
    }
    /**
     * Returns a string representation of this topology. The output of this
     * method can be subsequently used to reconstruct a topology with the 
     * <tt>fromString</tt> method. Only the nodes and wired links are exported
     * here (not the topology's properties).
     */
    public String toString(){
        StringBuffer res = new StringBuffer();
        for (Node n : nodes)
            res.append(n.toString() + " " + n.coords.toString().substring(14) + "\n");
        for (Link l : getLinks())
            if (!l.isWireless())
                res.append(l.toString()+ "\n");
        return res.toString();
    }
    /**
     * Imports nodes and wired links from the specified string representation of a 
     * topology.
     * @param s The string representation.
     */
    public void fromString(String s){
        HashMap<String,Node> nodeTable=new HashMap<String,Node>();
        while(s.indexOf("[")>0){
            Node n=new Node();
            String id=s.substring(0, s.indexOf(" "));
            n.setProperty("id", id);
            nodeTable.put(id, n);
            addNode(new Double(s.substring(s.indexOf("[")+1,s.indexOf(","))),new Double(s.substring(s.indexOf(",")+2,s.indexOf("]"))),n);
            s=s.substring(s.indexOf("\n")+1);
        }
        while(s.indexOf("--")>0){
            Node n1=nodeTable.get(s.substring(0,s.indexOf(" ")));
            Node n2=nodeTable.get(s.substring(s.indexOf(">")+2,s.indexOf("\n")));
            Type type=(s.indexOf("<")>0 && s.indexOf("<")<s.indexOf("\n"))?Type.UNDIRECTED:Type.DIRECTED;
            addLink(new Link(n1,n2,type,Link.Mode.WIRED));
            s=s.substring(s.indexOf("\n")+1);
        }
    }
}
