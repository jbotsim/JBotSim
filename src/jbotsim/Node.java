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

import java.util.*;
import java.util.List;

import jbotsim.event.ClockListener;
import jbotsim.event.MovementListener;

public class Node extends _Properties implements ClockListener, Comparable<Node> {
    public static final Color DEFAULT_COLOR = null;
    public static final int DEFAULT_SIZE = 8;
    public static final double DEFAULT_DIRECTION =  Math.PI / 2;;
    List<Message> mailBox = new ArrayList<>();
    List<Message> sendQueue = new ArrayList<>();
    HashMap<Node, Link> outLinks = new HashMap<>();
    Point coords = new Point(0, 0, 0);
    double direction = DEFAULT_DIRECTION;
    Double communicationRange = null;
    Double sensingRange = null;
    List<Node> sensedNodes = new ArrayList<>();
    boolean isWirelessEnabled = true;
    Topology topo;
    Color color = null;
    Object label = null;
    Integer ID = -1;
    int size = DEFAULT_SIZE;
    static ArrayList<Color> basicColors = new ArrayList<>(Arrays.asList(
            Color.red, Color.green, Color.blue, Color.yellow,
            Color.pink, Color.black, Color.white, Color.gray,
            Color.orange, Color.cyan, Color.magenta));

    /**
     * Returns the identifier of this node.
     *
     * @return The identifier as an integer.
     */
    public int getID() {
        return ID;
    }

    /**
     * Sets the identifier of this node. Nodes have an identifier by default,
     * which is the smallest available integer.
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Returns the parent topology of this node, if any.
     *
     * @return The parent topology, or <tt>null</tt> if the node is orphan.
     */
    public Topology getTopology() {
        return topo;
    }

    /**
     * Called when this node is selected (e.g. middle click in the UI)
     */
    public void onSelection() {
    }

    /**
     * Override this method to re-initialise your node (e.g. your variables).
     * This method is also called when a node is added to the topology
     * (or when the topology starts, if not yet started).
     */
    public void onStart() {
    }

    /**
     * This method is called just before
     * the node is removed from the topology.
     */
    public void onStop() {
    }

    /**
     * Override this method to perform some action just before clock pulse.
     */
    public void onPreClock() {
    }

    /**
     * Override this method to perform some action upon clock pulse.
     */
    public void onClock() {
    }

    /**
     * Override this method to perform some action just after clock pulse.
     */
    public void onPostClock() {
    }

    /**
     * Override this method to perform some action when the node moves.
     */
    public void onMove() {
    }

    /**
     * Called when this node receives a message
     */
    public void onMessage(Message message) {
    }

    /**
     * Called when an adjacent undirected link is added
     */
    public void onLinkAdded(Link link) {
    }

    /**
     * Called when an adjacent undirected link is removed
     */
    public void onLinkRemoved(Link link) {
    }

    /**
     * Called when an adjacent directed link is added
     */
    public void onDirectedLinkAdded(Link link) {
    }

    /**
     * Called when an adjacent directed link is removed
     */
    public void onDirectedLinkRemoved(Link link) {
    }

    /**
     * Called when another node is sensed for the first time
     */
    public void onSensingIn(Node node) {
    }

    /**
     * Called when a sensed node is no more sensed
     */
    public void onSensingOut(Node node) {
    }

    /**
     * Returns the x-coordinate of this node.
     */
    public double getX() {
        return coords.getX();
    }

    /**
     * Returns the y-coordinate of this node.
     */
    public double getY() {
        return coords.getY();
    }

    /**
     * Returns the z-coordinate of this node.
     */
    public double getZ() {
        return coords.getZ();
    }

    /**
     * Returns the color of this node.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color of this node.
     */
    public void setColor(Color color) {
        this.color = color;
        setProperty("color", color); // Used for property notification
    }

    /**
     * Returns the color of this node.
     */
    public int getIntColor() {
        return basicColors.indexOf(color);
    }

    /**
     * Sets the color of this node.
     */
    public void setIntColor(Integer color) {
        Random r = new Random();
        while (basicColors.size() <= color)
            basicColors.add(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
        setColor(basicColors.get(color));
    }

    /**
     * Assign a random color to this node.
     */
    public void setRandomColor() {
        Random r = new Random();
        setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
    }

    public static List<Color> getBasicColors() {
        return basicColors;
    }

    /**
     * Sets the icon of this node. The argument must be an absolute path to
     * either a file in the file system, or a resource in the application.
     * Examples:
     * <pre>
     * {@code
     *     node.setIcon("/filesystem/path/to/image");
     *     node.setIcon("/package/path/to/image");
     * }
     * </pre>
     */
    public void setIcon(String fileName) {
        setProperty("icon", fileName);
    }

    /**
     * Returns the size of this node.
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the size of this node.
     */
    public void setSize(int size) {
        this.size = size;
        setProperty("size", size); // used for property notification
    }

    /**
     * Returns the label of this node.
     *
     * @deprecated Use setLabel() instead.
     */
    @Deprecated
    public Object getState() {
        return label;
    }

    /**
     * Sets the label of this node. This text will appear as a tooltip
     * when the mouse cursor is held some time over the node.
     *
     * @deprecated Use setLabel() instead.
     */
    @Deprecated
    public void setState(Object state) {
        this.label = state;
        setProperty("label", label); // Used for property notification
    }

    /**
     * Returns the label of this node.
     */
    public Object getLabel() {
        return label;
    }

    /**
     * Sets the label of this node. Default GUI shows it as tooltip
     * when the mouse cursor is held some time over the node.
     */
    public void setLabel(Object label) {
        this.label = label;
        setProperty("label", label); // Used for property notification
    }

    /**
     * Returns the communication range of this node (as a radius).
     */
    public double getCommunicationRange() {
        return communicationRange;
    }

    /**
     * Activates the wireless capabilities of this node and sets
     * its communication range to the specified radius. This
     * determines the distance up to which this node can <i>send</i> messages
     * to other nodes.
     */
    public void setCommunicationRange(double range) {
        communicationRange = range;
        if (topo != null)
            topo.touch(this);
    }

    /**
     * Indicates whether this node has wireless capabilities enabled.
     */
    public boolean isWirelessEnabled() {
        return isWirelessEnabled;
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
        if (topo != null)
            topo.touch(this);
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
        sensingRange = range;
        notifyNodeMoved(); // for GUI refresh FIXME
    }

    /**
     * Returns the location of this node.
     */
    public Point getLocation() {
        return new Point(coords);
    }

    /**
     * Changes this node's location to the specified coordinates.
     *
     * @param x The abscissa of the new location.
     * @param y The ordinate of the new location.
     */
    public void setLocation(double x, double y) {
        coords = new Point(x, y, 0);
        if (topo != null)
            topo.touch(this);
        notifyNodeMoved();
    }

    /**
     * Changes this node's location to the specified coordinates.
     *
     * @param x The abscissa of the new location.
     * @param y The ordinate of the new location.
     * @param z The ordinate of the new location.
     */
    public void setLocation(double x, double y, double z) {
        coords = new Point(x, y, z);
        if (topo != null)
            topo.touch(this);
        notifyNodeMoved();
    }

    /**
     * Changes this node's location to the specified 2D point.
     *
     * @param loc The new location point.
     */
/*    public void setLocation(Point loc) {
        setLocation(loc.getX(), loc.getY());
    }*/

    /**
     * Changes this node's location to the specified 2D point.
     *
     * @param loc The new location point.
     */
    public void setLocation(Point loc) {
        setLocation(loc.getX(), loc.getY(), loc.getZ());
    }

    /**
     * Changes this node's location modulo the size of topology.
     */
    public void wrapLocation() {
        setLocation((coords.getX() + topo.getWidth()) % topo.getWidth(), (coords.getY() + topo.getHeight()) % topo.getHeight());
    }

    /**
     * Translates the location of this node by the specified coordinates.
     *
     * @param dx The abscissa component.
     * @param dy The ordinate component.
     */
    public void translate(double dx, double dy) {
        setLocation(coords.getX() + dx, coords.getY() + dy);
    }

    /**
     * Translates the location of this node by the specified coordinates.
     *
     * @param dx The abscissa component.
     * @param dy The ordinate component.
     */
    public void translate(double dx, double dy, double dz) {
        setLocation(coords.getX() + dx, coords.getY() + dy, coords.getZ() + dz);
    }

    /**
     * Returns the current time (current round number)
     */
    public int getTime() {
        return topo.getTime();
    }

    /**
     * Returns the current direction angle of this node (in radians).
     */
    public double getDirection() {
        return direction;
    }

    /**
     * Sets the direction angle of this node (in radians).
     *
     * @param angle The angle in radians.
     */
    public void setDirection(double angle) {
        direction = angle;
        notifyNodeMoved();
    }

    /**
     * Sets the direction angle of this node using the specified reference
     * point. Only the resulting angle matters (not the particular location of
     * the reference point).
     *
     * @param p The reference point.
     */
    public void setDirection(Point p) {
        setDirection(Math.atan2(p.getY() - coords.getY(), (p.getX() - coords.getX())));
    }

    /**
     * Sets the direction angle of this node using the specified reference
     * point. Only the resulting angle matters (not the particular location of
     * the reference point).
     *
     * @param p The reference point.
     */
    public void setDirection(Point p, boolean opposite) {
        Point p2 = (Point) p.clone();
        if (opposite)
            p2.setLocation(2 * getX() - p.getX(), 2 * getY() - p.getY());
        setDirection(p2);
    }

    /**
     * Translates the location of this node by one unit towards
     * the node's current direction. The distance unit is the pixel.
     */
    public void move() {
        move(1);
    }

    /**
     * Translates the location of this node by the specified distance towards
     * the node's current direction. The distance unit is the pixel.
     */
    public void move(double distance) {
        translate(Math.cos(direction) * distance, Math.sin(direction) * distance);
    }

    /**
     * Returns the directed link whose destination is this node and sender is
     * the specified node, if any such link exists.
     *
     * @param n The sender node.
     * @return The requested link, or <tt>null</tt> if no such link is found.
     */
    public Link getInLinkFrom(Node n) {
        return topo.getLink(n, this, true);
    }

    /**
     * Returns the directed link whose sender is this node and destination is
     * the specified node, if any such link exists.
     *
     * @param n The destination node.
     * @return The requested link, or <tt>null</tt> if no such link is found.
     */
    public Link getOutLinkTo(Node n) {
        return outLinks.get(n);
    }

    /**
     * Returns the undirected link whose endpoints are this node and the
     * specified node, if any such link exists.
     *
     * @param n The node at the opposite endpoint.
     * @return The requested link, or <tt>null</tt> if no such link is found.
     */
    public Link getCommonLinkWith(Node n) {
        return topo.getLink(this, n);
    }

    /**
     * Returns a list containing all links for which this node is the
     * destination. The returned list can be subsequently modified without
     * effect on the topology.
     */
    public List<Link> getInLinks() {
        return topo.getLinks(true, this, 2);
    }

    /**
     * Returns a list containing all links for which this node is the
     * sender. The returned list can be subsequently modified without effect
     * on the topology.
     */
    public List<Link> getOutLinks() {
        return new ArrayList<Link>(outLinks.values());
    }

    /**
     * Returns a list containing all undirected links adjacent to this node.
     * The returned list can be subsequently modified without effect on the
     * topology.
     */
    public List<Link> getLinks() {
        return getLinks(false);
    }

    /**
     * Returns a list containing all adjacent links of the specified type.
     *
     * @param directed <tt>true</tt> for directed, <tt>false</tt> for
     *                 undirected. The returned list can be subsequently modified without
     *                 effect on the topology.
     */
    public List<Link> getLinks(boolean directed) {
        return topo.getLinks(directed, this, 0);
    }

    /**
     * Returns a list containing every node serving as source for an adjacent
     * directed link. The returned list can be subsequently modified
     * without effect on the topology.
     *
     * @return A list containing the neighbors, with possible duplicates
     * when several links come from a same neighbor.
     */
    public List<Node> getInNeighbors() {
        List<Node> neighbors = new ArrayList<>();
        for (Link l : getInLinks())
            neighbors.add(l.source);
        return neighbors;
    }

    /**
     * Returns a list containing every node serving as destination for an
     * adjacent directed link. The returned list can be subsequently
     * modified without effect on the topology.
     *
     * @return A list containing the neighbors, with possible duplicates
     * when several links go towards a same neighbor.
     */
    public List<Node> getOutNeighbors() {
        ArrayList<Node> neighbors = new ArrayList<>();
        for (Link l : getOutLinks())
            neighbors.add(l.destination);
        return neighbors;
    }

    /**
     * Returns a list containing every node located within the sensing range
     * The returned list can be modified without side effect.
     *
     * @return A list containing all nodes within sensing range
     */
    public List<Node> getSensedNodes() {
        ArrayList<Node> sensedNodes = new ArrayList<>();
        for (Node n : topo.getNodes())
            if (distance(n) < sensingRange && n != this)
                sensedNodes.add(n);
        return sensedNodes;
    }

    /**
     * Indicates whether this node has at least one neighbor (undirected)
     *
     * @return <tt>true</tt> if it does, <tt>false</tt> if it does not.
     */
    public boolean hasNeighbors() {
        return getLinks().size() > 0;
    }

    /**
     * Returns a list containing every node located at the opposite endpoint
     * of an adjacent undirected links. The returned list can be
     * subsequently modified without effect on the topology.
     *
     * @return A list containing the neighbors, with possible duplicates
     * when several links are shared with a same neighbor.
     */
    public List<Node> getNeighbors() {
        LinkedHashSet<Node> neighbors = new LinkedHashSet<>();
        for (Link l : getLinks())
            neighbors.add(l.getOtherEndpoint(this));
        return new ArrayList<>(neighbors);
    }

    /**
     * Returns a list of messages representing the mailbox of this node.
     * The mailbox can be useful to scrutinize new messages in a non-event,
     * round-based way (as opposed to the onMessage() method), or to clear previous
     * messages (since all received messages are retained in the mailbox). The
     * returned list must be considered as the original copy of the node's
     * mailbox.
     */
    public List<Message> getMailbox() {
        return mailBox;
    }

    /**
     * Returns a list of the messages that this node is about to send.
     */
    public List<Message> getOutbox() {
        return new ArrayList<>(sendQueue);
    }

    /**
     * Sends a message from this node to the specified destination node.
     * The content of the
     * message is specified as an object reference, to be passed 'as is' to the
     * destination(s). The effective transmission will occur at the
     * <tt>x<sup>th</sup></tt> following clock step, where <tt>x</tt> is the
     * message delay specified by the static method <tt>Message.setMessageDelay
     * </tt> (1 by default).
     *
     * @param destination The destination node.
     * @param message     The message to be sent.
     */
    public void send(Node destination, Message message) {
        Message m = new Message(this, destination, message);
        sendQueue.add(m);
    }

    /**
     * Same as <tt>send()</tt>, but the content is directly given as parameter
     * (a message will be created to contain it).
     *
     * @param destination The non-null destination.
     * @param content     The object to be sent.
     */
    public void send(Node destination, Object content) {
        send(destination, new Message(content));
    }

    /**
     * Same method as <tt>send()</tt>, but retries to send the message later
     * if the link to the destination disappeared during transmission.
     * (Does not work for <tt>null</tt> destinations.)
     *
     * @param destination The non-null destination.
     * @param message     The message.
     */
    public void sendRetry(Node destination, Message message) {
        assert (destination != null);
        Message m = new Message(message);
        m.retryMode = true;
        send(destination, m);
    }

    /**
     * Same as <tt>sendRetry()</tt>, but the content is directly given as parameter
     * (a message will be created to contain it).
     *
     * @param destination The non-null destination.
     * @param content     The object to be sent.
     */
    public void sendRetry(Node destination, Object content) {
        sendRetry(destination, new Message(content));
    }

    /**
     * Sends a message to all neighbors. The content of the
     * message is specified as an object reference, to be passed 'as is' to the
     * destination(s). The effective transmission will occur at the
     * <tt>x<sup>th</sup></tt> following clock step, where <tt>x</tt> is the
     * message delay specified by the static method <tt>Message.setMessageDelay
     * </tt> (1 by default).
     *
     * @param message The message to be sent.
     */
    public void sendAll(Message message) {
        send(null, message);
    }

    /**
     * Same as <tt>sendAll()</tt>, but the content is directly given as parameter
     * (a message will be created to contain it).
     *
     * @param content The object to be sent.
     */
    public void sendAll(Object content) {
        send(null, new Message(content));
    }

    /**
     * Returns the distance between this node and the specified node.
     *
     * @param n The other node.
     */
    public double distance(Node n) {
        return coords.distance(n.coords);
    }

    /**
     * Returns the distance between this node and the specified 2D location.
     *
     * @param p The location (as a point).
     */
/*    public double distance(Point p) {
        return coords.distance(p.getX(), p.getY(), 0);
    }*/

    /**
     * Returns the distance between this node and the specified 2D location.
     *
     * @param x The abscissa of the point to which the distance is measured.
     * @param y The ordinate of the point to which the distance is measured.
     */
    public double distance(double x, double y) {
        return coords.distance(x, y, 0);
    }

    /**
     * Returns the distance between this node and the specified 3D location.
     *
     * @param p The location (as a 3D point).
     */
    public double distance(Point p) {
        return coords.distance(p.getX(), p.getY(), p.getZ());
    }

    /**
     * Returns the distance between this node and the specified 3D location.
     *
     * @param x The abscissa of the point to which the distance is measured.
     * @param y The ordinate of the point to which the distance is measured.
     */
    public double distance(double x, double y, double z) {
        return coords.distance(x, y, z);
    }

    protected void notifyNodeMoved() {
        onMove();
        if (topo != null)
            for (MovementListener ml : new ArrayList<>(topo.movementListeners))
                ml.onMove(this);
    }

    public int compareTo(Node o) {
        return (toString().compareTo(o.toString()));
    }

    /**
     * Returns a string representation of this node.
     */
    public String toString() {
        if (label == null)
            return ID.toString();
        else
            return label.toString();
    }
}
