/*
 * This file is part of JBotSim.
 *
 *    JBotSim is free software: you can redistribute it and/or modify it
 *    under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Authors:
 *    Arnaud Casteigts        <arnaud.casteigts@labri.fr>
 */
package jbotsim;

import jbotsim.Link.Mode;
import jbotsim.Link.Type;
import jbotsim.event.*;

import java.util.*;

public class Topology extends _Properties implements ClockListener {
    public static final int DEFAULT_WIDTH = 600;
    public static final int DEFAULT_HEIGHT = 400;
    public static final double DEFAULT_COMMUNICATION_RANGE = 100;
    public static final double DEFAULT_SENSING_RANGE = 0;
    public ClockManager clockManager;
    List<ConnectivityListener> cxUndirectedListeners = new ArrayList<>();
    List<ConnectivityListener> cxDirectedListeners = new ArrayList<>();
    List<TopologyListener> topologyListeners = new ArrayList<>();
    List<MovementListener> movementListeners = new ArrayList<>();
    List<MessageListener> messageListeners = new ArrayList<>();
    List<SelectionListener> selectionListeners = new ArrayList<>();
    List<StartListener> startListeners = new ArrayList<>();
    MessageEngine messageEngine = null;
    Scheduler scheduler;
    List<Node> nodes = new ArrayList<>();
    List<Link> arcs = new ArrayList<>();
    List<Link> edges = new ArrayList<>();
    HashMap<String, Class<? extends Node>> nodeModels = new HashMap<String, Class<? extends Node>>();
    boolean isWirelessEnabled = true;
    double communicationRange = DEFAULT_COMMUNICATION_RANGE;
    double sensingRange = DEFAULT_SENSING_RANGE;
    int width;
    int height;
    LinkResolver linkResolver = new LinkResolver();
    Node selectedNode = null;
    ArrayList<Node> toBeUpdated = new ArrayList<>();
    private boolean step = false;
    private boolean isStarted = false;
    private int nextID = 0;

    public enum RefreshMode {CLOCKBASED, EVENTBASED}

    ;
    RefreshMode refreshMode = RefreshMode.EVENTBASED;

    /**
     * Creates a topology.
     */
    public Topology() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Creates a topology of given dimensions.
     */
    public Topology(int width, int height) {
        setMessageEngine(new MessageEngine());
        setScheduler(new DefaultScheduler());
        setDimensions(width, height);
        clockManager = new ClockManager(this);
    }

    /**
     * Returns the node class corresponding to that name.
     */
    public Class<? extends Node> getNodeModel(String modelName) {
        return nodeModels.get(modelName);
    }

    /**
     * Returns the default node model,
     * all properties assigned to this virtual node will be given to further nodes created
     * without explicit model name.
     */
    public Class<? extends Node> getDefaultNodeModel() {
        return getNodeModel("default");
    }

    /**
     * Adds the given node instance as a model.
     *
     * @param modelName
     * @param nodeClass
     */
    public void setNodeModel(String modelName, Class<? extends Node> nodeClass) {
        nodeModels.put(modelName, nodeClass);
    }

    /**
     * Sets the default node model to the given node instance.
     *
     * @param nodeClass
     */
    public void setDefaultNodeModel(Class<? extends Node> nodeClass) {
        setNodeModel("default", nodeClass);
    }

    /**
     * Returns the set registered node classes.
     */
    public Set<String> getModelsNames() {
        return nodeModels.keySet();
    }

    /**
     * Create a new instance of this type of node.
     *
     * @param modelName
     * @return a new instance of this type of node
     */
    public Node newInstanceOfModel(String modelName) {
        try {
            return getNodeModel(modelName).newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.err.println("(is your class of node public?)");
        } catch (NullPointerException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Node();
    }

    public boolean isStarted() { // FIXME Ambiguous for the user
        return isStarted;
    }

    /**
     * Sets the updates (links, sensed objects, etc.) to be instantaneous (EVENTBASED),
     * or periodic after each round (CLOCKBASED).
     */
    public void setRefreshMode(RefreshMode refreshMode) {
        this.refreshMode = refreshMode;
    }

    /**
     * Returns the current refresh mode (CLOCKBASED or EVENTBASED).
     */
    public RefreshMode getRefreshMode() {
        return refreshMode;
    }

    /**
     * Enables this node's wireless capabilities.
     */
    public void enableWireless() {
        setWirelessStatus(true);
    }

    /**
     * Disables this node's wireless capabilities.
     */
    public void disableWireless() {
        setWirelessStatus(false);
    }

    /**
     * Set wireless capabilities status
     */
    public void setWirelessStatus(boolean enabled) {
        if (enabled == isWirelessEnabled)
            return;
        isWirelessEnabled = enabled;
        for (Node node : nodes)
            node.setWirelessStatus(enabled);
    }

    /**
     * Returns true if wireless links are enabled.
     */
    public boolean getWirelessStatus() {
        return isWirelessEnabled;
    }
    /**
     * Returns the default communication range.
     *
     * @return the default communication range
     */
    public double getCommunicationRange() {
        return communicationRange;
    }

    /**
     * Sets the default communication range.
     * If the topology already has some nodes, their range is changed.
     *
     * @param communicationRange The communication range
     */
    public void setCommunicationRange(double communicationRange) {
        this.communicationRange = communicationRange;
        for (Node node : nodes)
            node.setCommunicationRange(communicationRange);
        setProperty("communicationRange", communicationRange); // for notification purpose
    }

    /**
     * Returns the default sensing range,
     *
     * @return the default sensing range
     */
    public double getSensingRange() {
        return sensingRange;
    }

    /**
     * Sets the default sensing range.
     * If the topology already has some nodes, their range is changed.
     *
     * @param sensingRange The sensing range
     */
    public void setSensingRange(double sensingRange) {
        this.sensingRange = sensingRange;
        for (Node node : nodes)
            node.setSensingRange(sensingRange);
        setProperty("sensingRange", sensingRange); // for notification purpose
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
        this.messageEngine = messageEngine;
        messageEngine.setTopology(this);
    }

    /**
     * Gets a reference on the associated JTopology (if any, null otherwise).
     *
     * @deprecated was indirectly a dependency to Swing (through JTopology);
     * The method still returns it, but only as an "Object" for this reason.
     */
    @Deprecated
    public Object getJTopology() {
        return getProperty("jtopology");
    }

    /**
     * Gets a reference on the scheduler.
     */
    public Scheduler getScheduler() {
        return scheduler;
    }

    /**
     * Sets the scheduler of this topology.
     */
    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * Returns the global duration of a round in this topology (in millisecond).
     *
     * @return The duration
     */
    public int getClockSpeed() {
        return clockManager.getTimeUnit();
    }

    /**
     * Sets the global duration of a round in this topology (in millisecond).
     *
     * @param period The desired duration
     */
    public void setClockSpeed(int period) {
        clockManager.setTimeUnit(period);
    }

    /**
     * Returns the clock model currently in use.
     */
    public Class<? extends Clock> getClockModel() {
        return clockManager.getClockModel();
    }

    /**
     * Sets the clock model (to be instantiated automatically).
     *
     * @param clockModel A class that extends JBotSim's abstract Clock
     */
    public void setClockModel(Class<? extends Clock> clockModel) {
        clockManager.setClockModel(clockModel);
    }

    /**
     * Returns the current time (current round number)
     */
    public int getTime() {
        return clockManager.currentTime();
    }

    /**
     * Indicates whether the internal clock is currently running or in pause.
     *
     * @return <tt>true</tt> if running, <tt>false</tt> if paused.
     */
    public boolean isRunning() {
        return clockManager.isRunning();
    }

    /**
     * Pauses the clock (or increments the pause counter).
     */
    public void pause() {
        clockManager.pause();
    }

    /**
     * Resumes the clock (or decrements the pause counter).
     */
    public void resume() {
        clockManager.resume();
    }

    /**
     * Reset the round number to 0.
     */
    public void resetTime() {
        clockManager.reset();
    }

    /**
     * Sets the topology dimensions as indicated.
     */
    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Returns the width of this topology.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of this topology.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Initializes the clock.
     */
    public void start() {
        clockManager.start();
        isStarted = true;
        restart();
    }

    /**
     * (Re)init the nodes through their onStart() method (and notifies StartListeners as well)
     */
    public void restart() {
        pause();
        resetTime();
        clearMessages();
        for (Node n : nodes)
            n.onStart();
        for (StartListener listener : startListeners)
            listener.onStart();
        resume();
    }

    /**
     * Removes all the nodes (and links) of this topology.
     */
    public void clear() {
        while (!nodes.isEmpty())
            removeNode(nodes.get(nodes.size() - 1));
        nextID = 0;
    }

    /**
     * Removes all the links of this topology.
     */
    public void clearLinks() {
        while (!edges.isEmpty())
            removeLink(edges.get(edges.size() - 1));
    }

    /**
     * Removes all the ongoing messages in this topology.
     */
    public void clearMessages() {
        for (Node n : nodes) {
            n.sendQueue.clear();
            n.mailBox.clear();
        }
    }

    /**
     * Performs a single round, then switch to pause state.
     */
    public void step() {
        resume();
        step = true;
    }

    /**
     * Adds the specified node to this topology. The location of the node
     * in the topology will be its current inherent location (or <tt>(0,0)</tt>
     * if no location was prealably given to it).
     *
     * @param n The node to be added.
     */
    public void addNode(Node n) {
        addNode(n.getX(), n.getY(), n);
    }

    /**
     * Adds a new node to this topology at the specified location.
     *
     * @param x The abscissa of the location.
     * @param y The ordinate of the location.
     */
    public void addNode(double x, double y) {
        addNode(x, y, newInstanceOfModel("default"));
    }

    /**
     * Adds the specified node to this topology at the specified location.
     *
     * @param x The abscissa of the location.
     * @param y The ordinate of the location.
     * @param n The node to be added.
     */
    public void addNode(double x, double y, Node n) {
        pause();
        if (x == -1)
            x = Math.random() * width;
        if (y == -1)
            y = Math.random() * height;
        if (n.getX() == 0 && n.getY() == 0)
            n.setLocation(x, y);

        if (n.communicationRange == null)
            n.setCommunicationRange(communicationRange);
        if (n.sensingRange == null)
            n.setSensingRange(sensingRange);
        if (isWirelessEnabled == false)
            n.disableWireless();
        if (n.getID() == -1)
            n.setID(nextID++);
        nodes.add(n);
        n.topo = this;
        notifyNodeAdded(n);
        if (isStarted)
            n.onStart();
        touch(n);
        resume();
    }

    /**
     * Removes the specified node from this topology. All adjacent links will
     * be automatically removed.
     *
     * @param n The node to be removed.
     */
    public void removeNode(Node n) {
        pause();
        n.onStop();
        for (Link l : n.getLinks(true))
            removeLink(l);
        notifyNodeRemoved(n);
        nodes.remove(n);
        for (Node n2 : nodes) {
            if (n2.sensedNodes.contains(n)) {
                n2.sensedNodes.remove(n);
                n2.onSensingOut(n);
            }
        }
        n.topo = null;
        resume();
    }

    public void selectNode(Node n) {
        selectedNode = n;
        n.onSelection();
        notifyNodeSelected(n);
    }

    /**
     * Adds the specified link to this topology. Calling this method makes
     * sense only for wired links, since wireless links are automatically
     * managed as per the nodes' communication ranges.
     *
     * @param l The link to be added.
     */
    public void addLink(Link l) {
        addLink(l, false);
    }

    /**
     * Adds the specified link to this topology without notifying the listeners
     * (if silent is true). Calling this method makes sense only for wired
     * links, since wireless links are automatically managed as per the nodes'
     * communication ranges.
     *
     * @param l The link to be added.
     */
    public void addLink(Link l, boolean silent) {
        if (l.type == Type.DIRECTED) {
            arcs.add(l);
            l.source.outLinks.put(l.destination, l);
            if (l.destination.outLinks.containsKey(l.source)) {
                Link edge = new Link(l.source, l.destination, Link.Type.UNDIRECTED, l.mode);
                edges.add(edge);
                if (!silent)
                    notifyLinkAdded(edge);
            }
        } else { // UNDIRECTED
            Link arc1 = l.source.outLinks.get(l.destination);
            Link arc2 = l.destination.outLinks.get(l.source);
            if (arc1 == null) {
                arc1 = new Link(l.source, l.destination, Link.Type.DIRECTED);
                arcs.add(arc1);
                arc1.source.outLinks.put(arc1.destination, arc1);
                if (!silent)
                    notifyLinkAdded(arc1);
            } else {
                arc1.mode = l.mode;
            }
            if (arc2 == null) {
                arc2 = new Link(l.destination, l.source, Link.Type.DIRECTED);
                arcs.add(arc2);
                arc2.source.outLinks.put(arc2.destination, arc2);
                if (!silent)
                    notifyLinkAdded(arc2);
            } else {
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
     *
     * @param l The link to be removed.
     */
    public void removeLink(Link l) {
        if (l.type == Type.DIRECTED) {
            arcs.remove(l);
            l.source.outLinks.remove(l.destination);
            Link edge = getLink(l.source, l.destination, false);
            if (edge != null) {
                edges.remove(edge);
                notifyLinkRemoved(edge);
            }
        } else {
            Link arc1 = getLink(l.source, l.destination, true);
            Link arc2 = getLink(l.destination, l.source, true);
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
    public boolean hasDirectedLinks() {
        return arcs.size() > 2 * edges.size();
    }

    /**
     * Returns a list containing all the nodes in this topology. The returned
     * ArrayList can be subsequently modified without effect on the topology.
     */
    public List<Node> getNodes() {
        return new ArrayList<>(nodes);
    }

    /**
     * Returns the first node found with this ID.
     */
    public Node findNodeById(int id) {
        for (Node node : nodes)
            if (node.getID() == id)
                return node;
        return null;
    }

    /**
     * Shuffles the IDs of the nodes in this topology.
     */
    public void shuffleNodeIds() {
        List<Integer> Ids = new ArrayList<>();
        for (Node node : nodes)
            Ids.add(node.getID());
        Collections.shuffle(Ids);
        for (int i = 0; i < nodes.size(); i++)
            nodes.get(i).setID(Ids.get(i));
    }

    /**
     * Returns a list containing all undirected links in this topology. The
     * returned ArrayList can be subsequently modified without effect on the
     * topology.
     */
    public List<Link> getLinks() {
        return getLinks(false);
    }

    /**
     * Returns a list containing all links of the specified type in this
     * topology. The returned ArrayList can be subsequently modified without
     * effect on the topology.
     *
     * @param directed <tt>true</tt> for directed links, <tt>false</tt> for
     *                 undirected links.
     */
    public List<Link> getLinks(boolean directed) {
        return new ArrayList<>(directed ? arcs : edges);
    }

    List<Link> getLinks(boolean directed, Node n, int pos) {
        List<Link> result = new ArrayList<>();
        List<Link> allLinks = (directed) ? arcs : edges;
        for (Link l : allLinks)
            switch (pos) {
                case 0:
                    if (l.source == n || l.destination == n)
                        result.add(l);
                    break;
                case 1:
                    if (l.source == n)
                        result.add(l);
                    break;
                case 2:
                    if (l.destination == n)
                        result.add(l);
                    break;
            }
        return result;
    }

    /**
     * Returns the undirected link shared the specified nodes, if any.
     *
     * @return The requested link, if such a link exists, <tt>null</tt>
     * otherwise.
     */
    public Link getLink(Node n1, Node n2) {
        return getLink(n1, n2, false);
    }

    /**
     * Returns the link of the specified type between the specified nodes, if
     * any.
     *
     * @return The requested link, if such a link exists, <tt>null</tt>
     * otherwise.
     */
    public Link getLink(Node from, Node to, boolean directed) {
        if (directed) {
            return from.outLinks.get(to);
            //Link l=new Link(from, to,Link.Type.DIRECTED);
            //int pos=arcs.indexOf(l);
            //return (pos != -1)?arcs.get(pos):null;
        } else {
            Link l = new Link(from, to, Link.Type.UNDIRECTED);
            int pos = edges.indexOf(l);
            return (pos != -1) ? edges.get(pos) : null;
        }
    }

    /**
     * Replaces the default Wireless Link Resolver by a custom one.
     *
     * @param linkResolver An object that implements LinkResolver.
     */
    public void setLinkResolver(LinkResolver linkResolver) {
        this.linkResolver = linkResolver;
    }

    /**
     * Return the current LinkResolver
     *
     */
    public LinkResolver getLinkResolver() {
        return linkResolver;
    }

    /**
     * Registers the specified topology listener to this topology. The listener
     * will be notified whenever an undirected link is added or removed.
     *
     * @param listener The listener to add.
     */
    public void addConnectivityListener(ConnectivityListener listener) {
        cxUndirectedListeners.add(listener);
    }

    /**
     * Registers the specified connectivity listener to this topology. The
     * listener will be notified whenever a link of the specified type is
     * added or removed.
     *
     * @param listener The listener to register.
     * @param directed The type of links to be listened (<tt>true</tt> for
     *                 directed, <tt>false</tt> for undirected).
     */
    public void addConnectivityListener(ConnectivityListener listener, boolean directed) {
        if (directed)
            cxDirectedListeners.add(listener);
        else
            cxUndirectedListeners.add(listener);
    }

    /**
     * Unregisters the specified connectivity listener from the 'undirected'
     * listeners.
     *
     * @param listener The listener to unregister.
     */
    public void removeConnectivityListener(ConnectivityListener listener) {
        cxUndirectedListeners.remove(listener);
    }

    /**
     * Unregisters the specified connectivity listener from the listeners
     * of the specified type.
     *
     * @param listener The listener to unregister.
     * @param directed The type of links that this listener was listening
     *                 (<tt>true</tt> for directed, <tt>false</tt> for undirected).
     */
    public void removeConnectivityListener(ConnectivityListener listener, boolean directed) {
        if (directed)
            cxDirectedListeners.remove(listener);
        else
            cxUndirectedListeners.remove(listener);
    }

    /**
     * Registers the specified movement listener to this topology. The
     * listener will be notified every time the location of a node changes.
     *
     * @param listener The movement listener.
     */
    public void addMovementListener(MovementListener listener) {
        movementListeners.add(listener);
    }

    /**
     * Unregisters the specified movement listener for this topology.
     *
     * @param listener The movement listener.
     */
    public void removeMovementListener(MovementListener listener) {
        movementListeners.remove(listener);
    }

    /**
     * Registers the specified topology listener to this topology. The listener
     * will be notified whenever a node is added or removed.
     *
     * @param listener The listener to register.
     */
    public void addTopologyListener(TopologyListener listener) {
        topologyListeners.add(listener);
    }

    /**
     * Unregisters the specified topology listener.
     *
     * @param listener The listener to unregister.
     */
    public void removeTopologyListener(TopologyListener listener) {
        topologyListeners.remove(listener);
    }

    /**
     * Registers the specified message listener to this topology. The listener
     * will be notified every time a message is received at any node.
     *
     * @param listener The message listener.
     */
    public void addMessageListener(MessageListener listener) {
        messageListeners.add(listener);
    }

    /**
     * Unregisters the specified message listener for this topology.
     *
     * @param listener The message listener.
     */
    public void removeMessageListener(MessageListener listener) {
        messageListeners.remove(listener);
    }

    /**
     * Registers the specified selection listener to this topology. The listener
     * will be notified every time a node is selected.
     *
     * @param listener The selection listener.
     */
    public void addSelectionListener(SelectionListener listener) {
        selectionListeners.add(listener);
    }

    /**
     * Unregisters the specified selection listener for this topology.
     *
     * @param listener The selection listener.
     */
    public void removeSelectionListener(SelectionListener listener) {
        selectionListeners.remove(listener);
    }

    /**
     * Registers the specified start listener to this topology. The listener
     * will be notified every time a (re)start is requested on the topology.
     *
     * @param listener The start listener.
     */
    public void addStartListener(StartListener listener) {
        startListeners.add(listener);
    }

    /**
     * Unregisters the specified selection listener for this topology.
     *
     * @param listener The start listener.
     */
    public void removeStartListener(StartListener listener) {
        startListeners.remove(listener);
    }

    /**
     * Registers the specified listener to the events of the clock.
     *
     * @param listener The listener to register.
     * @param period   The number of rounds between consecutive onClock() events,
     *                 in time units.
     */
    public void addClockListener(ClockListener listener, int period) {
        clockManager.addClockListener(listener, period);
    }

    /**
     * Registers the specified listener to the events of the clock.
     *
     * @param listener The listener to register.
     */
    public void addClockListener(ClockListener listener) {
        clockManager.addClockListener(listener);
    }

    /**
     * Unregisters the specified listener. (The <tt>onClock()</tt> method of this
     * listener will not longer be called.)
     *
     * @param listener The listener to unregister.
     */
    public void removeClockListener(ClockListener listener) {
        clockManager.removeClockListener(listener);
    }

    protected void notifyLinkAdded(Link l) {
        if (l.type == Type.DIRECTED) {
            l.endpoint(0).onDirectedLinkAdded(l);
            l.endpoint(1).onDirectedLinkAdded(l);
            for (ConnectivityListener cl : cxDirectedListeners)
                cl.onLinkAdded(l);
        } else {
            l.endpoint(0).onLinkAdded(l);
            l.endpoint(1).onLinkAdded(l);
            for (ConnectivityListener cl : cxUndirectedListeners)
                cl.onLinkAdded(l);
        }
    }

    protected void notifyLinkRemoved(Link l) {
        if (l.type == Type.DIRECTED) {
            l.endpoint(0).onDirectedLinkRemoved(l);
            l.endpoint(1).onDirectedLinkRemoved(l);
            for (ConnectivityListener cl : cxDirectedListeners)
                cl.onLinkRemoved(l);
        } else {
            l.endpoint(0).onLinkRemoved(l);
            l.endpoint(1).onLinkRemoved(l);
            for (ConnectivityListener cl : cxUndirectedListeners)
                cl.onLinkRemoved(l);
        }
    }

    protected void notifyNodeAdded(Node node) {
        for (TopologyListener tl : new ArrayList<>(topologyListeners))
            tl.onNodeAdded(node);
    }

    protected void notifyNodeRemoved(Node node) {
        for (TopologyListener tl : new ArrayList<>(topologyListeners))
            tl.onNodeRemoved(node);
    }

    protected void notifyNodeSelected(Node node) {
        for (SelectionListener tl : new ArrayList<>(selectionListeners))
            tl.onSelection(node);
    }

    @Override
    public void onClock() {
        if (step) {
            pause();
            step = false;
        }
        if (refreshMode == RefreshMode.CLOCKBASED) {
            for (Node node : toBeUpdated)
                update(node);
            toBeUpdated.clear();
        }
    }

    void touch(Node n) {
        if (refreshMode == RefreshMode.CLOCKBASED)
            toBeUpdated.add(n);
        else
            update(n);
    }

    void update(Node n) {
        for (Node n2 : nodes)
            if (n2 != n) {
                updateWirelessLink(n, n2);
                updateWirelessLink(n2, n);
            }
        for (Node n2 : new ArrayList<>(nodes)) {
            if (n2 != n) {
                updateSensedNodes(n, n2);
                updateSensedNodes(n2, n);
            }
        }
    }

    void updateWirelessLink(Node n1, Node n2) {
        Link l = n1.getOutLinkTo(n2);
        boolean linkExisted = (l == null) ? false : true;
        boolean linkExists = linkResolver.isHeardBy(n1, n2);
        if (!linkExisted && linkExists)
            addLink(new Link(n1, n2, Type.DIRECTED, Mode.WIRELESS));
        else if (linkExisted && l.isWireless() && !linkExists)
            removeLink(l);
    }

    void updateSensedNodes(Node from, Node to) {
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

    @Override
    public String toString() {
        System.err.println("Export and Import has been removed from Topology (see format extensions)");
        return "";
    }
}
