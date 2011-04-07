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

import java.util.Vector;

public class Link extends _Properties{
	/**
	 * Enumerates the two possible types of a link: <tt>Type.DIRECTED</tt> and
	 * <tt>Type.UNDIRECTED</tt>.
	 */
    public static enum Type{DIRECTED, UNDIRECTED};
	/**
	 * Enumerates the two possible modes of a link: <tt>Mode.WIRED</tt> and
	 * <tt>Mode.WIRELESS</tt>.
	 */
    public static enum Mode{WIRED, WIRELESS};
    /**
     * The source node of this link (if directed), 
     * the first endpoint otherwise.
     */
    public final Node source; 
    /**
     * The destination node of this link (if directed),
     * the second endpoint otherwise.
     */
    public final Node destination;
    /**
     * The <tt>Type</tt> of this link (directed/undirected)
     */
    public final Type type;
    /**
     * The <tt>Mode</tt> of this link (wired/wireless)
     */
    public final Mode mode;
    /**
     * Creates an undirected wired link between the two specified nodes.
     * @param n1 The source node.
     * @param n2 The destination node.
     */
    public Link(Node n1, Node n2) {
        this(n1, n2, Type.UNDIRECTED, Mode.WIRED);
    }
    /**
     * Creates a wired link of the specified <tt>type</tt> between the nodes 
     * <tt>from</tt> and <tt>to</tt>. The respective order of <tt>from</tt>
     * and <tt>to</tt> does not matter if the specified type is undirected.
     * @param from The source node.
     * @param to The destination node.
     * @param type The type of the link (<tt>Type.DIRECTED</tt> or 
     * <tt>Type.UNDIRECTED</tt>).
     */
    public Link(Node from, Node to, Type type) {
        this(from, to, type, Mode.WIRED);
    }
    /**
     * Creates a link with the specified <tt>mode</tt> between the nodes 
     * <tt>from</tt> and <tt>to</tt>. The created link is undirected by
     * default. The respective order of <tt>from</tt> and <tt>to</tt> does not
     * matter.
     * @param from The source node.
     * @param to The destination node.
     * @param mode The mode of the link (<tt>Mode.WIRED</tt> or
     * <tt>Mode.WIRELESS</tt>).
     */
    public Link(Node from, Node to, Mode mode) {
        this(from, to, Type.UNDIRECTED, mode);
    }
    /**
     * Creates a link of the specified <tt>type</tt> with the specified
     * <tt>mode</tt> between the nodes from and to. The created link is wired
     * by default. The respective order of <tt>from</tt> and <tt>to</tt> does
     * not matter if the type is undirected. 
     * @param from The source node.
     * @param to The destination node.
     * @param type The type of the link (<tt>Type.DIRECTED</tt> or 
     * <tt>Type.UNDIRECTED</tt>).
     * @param mode The mode of the link (<tt>Mode.WIRED</tt> or
     * <tt>Mode.WIRELESS</tt>).
     */
    public Link(Node from, Node to, Type type, Mode mode) {
        this.source = from;
        this.destination = to;
        this.type = type;
        this.mode = mode;
    }
    /**
     * Returns a vector containing the two endpoint nodes of this link
     * @return The endpoints.
     */
    public Vector<Node> endpoints(){
    	Vector<Node> tmp = new Vector<Node>();
    	tmp.add(source); tmp.add(destination);
    	return tmp;
    }
    /**
     * Returns the node located at the opposite of the specified node 
     * (reference node) on the underlying link.
     * @param n The reference node.
     * @return The opposite node.
     */
    public Node getOtherEndpoint(Node n){
        return (n==source)?destination:source;
    }
    /**
     * Returns <tt>true</tt> if the link <tt>mode</tt> is wireless, 
     * <tt>false</tt> otherwise.
     */
    public boolean isWireless() {
    	return mode==Mode.WIRELESS;
    }
    /**
     * Returns <tt>true</tt> if the link <tt>type</tt> is directed, 
     * <tt>false</tt> otherwise.
     */
    public boolean isDirected() {
        return type==Type.DIRECTED;
    }
    /**
     * Compares the specified object with this link for equality. Returns
     * <tt>true</tt> if both links have the same <tt>type</tt>
     * (directed/undirected) and the same endpoints (interchangeably if 
     * undirected). The <tt>mode</tt> is not considered by the equality test.
     */
    public boolean equals(Object o){
        Link l=(Link)o;
        return ((this.type == l.type) &&
                (l.source==this.source && l.destination==this.destination) ||
                (this.type==Type.UNDIRECTED && (l.source==this.destination && l.destination==this.source)));
    }
    /**
     * Returns a string representation of this link.
     */
    public String toString(){
        if (this.type==Type.DIRECTED)
            return (source+" --> "+destination);
        else
            return (source+" <--> "+destination);
    }
}
