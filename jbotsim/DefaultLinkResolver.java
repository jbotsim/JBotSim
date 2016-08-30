package jbotsim;


public class DefaultLinkResolver implements LinkResolver {

	@Override
	public boolean isHeardBy(Node n1, Node n2) {
		return (n1.isWirelessEnabled() && n2.isWirelessEnabled()
				&& n1.distance(n2) < n1.getCommunicationRange());
	}
}
