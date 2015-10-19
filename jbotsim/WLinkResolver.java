package jbotsim;

public interface WLinkResolver {
	/** Returns true if node n1 can be heard by node n2 */
	boolean isHeardBy(Node n1, Node n2);
}
