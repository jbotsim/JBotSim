/*
 * Copyright 2008 - 2019, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
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
package io.jbotsim.core;



import java.util.ArrayList;
import java.util.List;

/**
 * <p>The {@link Link} object encodes a communication link between two {@link Node}s.</p>
 *
 * <p>Links can either be {@link Orientation#DIRECTED} or {@link Orientation#UNDIRECTED}, and either
 * {@link Mode#WIRED} or {@link Mode#WIRELESS}.
 * The by default, a link will be {@link Orientation#UNDIRECTED} and {@link Mode#WIRELESS}.</p>
 */
public class Link extends Properties implements Comparable<Link> {
    public static final Color DEFAULT_COLOR = Color.darkGray;
    public static final int DEFAULT_WIDTH = 1;
    public static final Orientation DEFAULT_ORIENTATION = Orientation.UNDIRECTED;
    public static final Mode DEFAULT_MODE = Mode.WIRED;

    Integer width = DEFAULT_WIDTH;
    Color color = DEFAULT_COLOR;

    /**
     * Enumerates the two possible types of a link: <code>Orientation.DIRECTED</code> and
     * <code>Orientation.UNDIRECTED</code>.
     */
    public enum Orientation {
        DIRECTED, UNDIRECTED
    }

    /**
     * Enumerates the two possible modes of a link: <code>Mode.WIRED</code> and
     * <code>Mode.WIRELESS</code>.
     */
    public enum Mode {
        WIRED, WIRELESS
    }

    /**
     * The source node of this link (if directed),
     * the first endpoint otherwise.
     */
    public Node source;
    /**
     * The destination node of this link (if directed),
     * the second endpoint otherwise.
     */
    public Node destination;
    /**
     * The <code>Orientation</code> of this link (directed/undirected)
     */
    public Orientation orientation;
    /**
     * The <code>Mode</code> of this link (wired/wireless)
     */
    public Mode mode;

    /**
     * Creates an undirected wired link between the two specified nodes.
     *
     * @param n1 The source node.
     * @param n2 The destination node.
     */
    public Link(Node n1, Node n2) {
        this(n1, n2, DEFAULT_ORIENTATION, DEFAULT_MODE);
    }

    /**
     * Creates a wired link of the specified <code>orientation</code> between the nodes
     * <code>from</code> and <code>to</code>. The respective order of <code>from</code>
     * and <code>to</code> does not matter if the specified orientation is undirected.
     *
     * @param from The source node.
     * @param to   The destination node.
     * @param orientation The orientation of the link (<code>Orientation.DIRECTED</code> or
     *             <code>Orientatino.UNDIRECTED</code>).
     */
    public Link(Node from, Node to, Orientation orientation) {
        this(from, to, orientation, DEFAULT_MODE);
    }

    /**
     * Creates a link with the specified <code>mode</code> between the nodes
     * <code>from</code> and <code>to</code>. The created link is undirected by
     * default. The respective order of <code>from</code> and <code>to</code> does not
     * matter.
     *
     * @param from The source node.
     * @param to   The destination node.
     * @param mode The mode of the link (<code>Mode.WIRED</code> or
     *             <code>Mode.WIRELESS</code>).
     */
    public Link(Node from, Node to, Mode mode) {
        this(from, to, DEFAULT_ORIENTATION, mode);
    }

    /**
     * Creates a link of the specified <code>orientation</code> with the specified
     * <code>mode</code> between the nodes from and to. The created link is wired
     * by default. The respective order of <code>from</code> and <code>to</code> does
     * not matter if the orientation is undirected.
     *
     * @param from The source node.
     * @param to   The destination node.
     * @param orientation The orientation of the link (<code>Orientation.DIRECTED</code> or
     *             <code>Orientation.UNDIRECTED</code>).
     * @param mode The mode of the link (<code>Mode.WIRED</code> or
     *             <code>Mode.WIRELESS</code>).
     */
    public Link(Node from, Node to, Orientation orientation, Mode mode) {
        source = from;
        destination = to;
        this.orientation = orientation;
        this.mode = mode;
    }

    /**
     * Returns an list containing the two endpoint nodes of this link
     *
     * @return The endpoints.
     */
    public List<Node> endpoints() {
        List<Node> tmp = new ArrayList<>();
        tmp.add(source);
        tmp.add(destination);
        return tmp;
    }

    /**
     * Returns the requested endpoint node of this link
     *
     * @param index The endpoint index (0 or 1).
     * @return The endpoint.
     */
    public Node endpoint(int index) {
        assert (index == 0 || index == 1);
        return (index == 0) ? source : destination;
    }

    /**
     * Returns the node located at the opposite of the specified node
     * (reference node) on the underlying link.
     *
     * @param n The reference node.
     * @return The opposite node.
     */
    public Node getOtherEndpoint(Node n) {
        return (n == source) ? destination : source;
    }

    /**
     * Returns the parent topology of this link, if any.
     *
     * @return The parent topology, or <code>null</code> if the link has none.
     */
    public Topology getTopology() {
        return source.getTopology();
    }

    /**
     * Returns the color of this link.
     * @return the {@link Color} of the link.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color of this link
     * @param color the new {@link Color} of the link.
     */
    public void setColor(Color color) {
        this.color = (color != null) ? color : Color.darkGray;
        setProperty("color", color); // Used for property notification
    }

    /**
     * Returns the width of this link.
     * @return the width of this link.
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * Sets the width of this link.
     * @param width the width of this link.
     */
    public void setWidth(Integer width) {
        this.width = width;
        setProperty("width", width); // Used for property notification
    }

    /**
     * Returns the euclidean distance between this link endpoints.
     * @return the length between the endpoints, as a {@link Double}.
     */
    public Double getLength() {
        return source.distance(destination);
    }

    /**
     * Tests whether the <code>mode</code> is WIRELESS.
     * @return <code>true</code> if the link <code>mode</code> is WIRELESS,
     *         <code>false</code> otherwise.
     */
    public boolean isWireless() {
        return mode == Mode.WIRELESS;
    }

    /**
     * Tests whether the <code>orientation</code> is DIRECTED.
     * @return <code>true</code> if the link <code>orientation</code> is DIRECTED,
     *         <code>false</code> otherwise.
     */
    public boolean isDirected() {
        return orientation == Orientation.DIRECTED;
    }

    /**
     * Compares the specified object with this link for equality. Returns
     * <code>true</code> if both links have the same <code>orientation</code>
     * (directed/undirected) and the same endpoints (interchangeably if
     * undirected). The <code>mode</code> is not considered by the equality test.
     */
    public boolean equals(Object o) {
        if (o == null)
            return false;

        Link l = (Link) o;
        if (this.orientation != l.orientation)
            return false;
        else if (this.orientation == Orientation.DIRECTED)
            return (l.source == this.source && l.destination == this.destination);
        else
            return (l.source == this.source && l.destination == this.destination) ||
                    (l.source == this.destination && l.destination == this.source);
    }

    /**
     * Compares the specified link to this link in terms of length.
     */
    public int compareTo(Link l) {
        return getLength().compareTo(l.getLength());
    }

    /**
     * Returns a string representation of this link.
     */
    public String toString() {
        if (orientation == Orientation.DIRECTED)
            return (source + " --> " + destination);
        else
            return (source + " <--> " + destination);
    }
}
