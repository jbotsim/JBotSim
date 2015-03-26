package jbotsim;


public class BasicWLinkCalculator implements WLinkCalculator{

	@Override
	public boolean isHeardBy(Node n1, Node n2) {
		return (n1.isWirelessEnabled() && n2.isWirelessEnabled()
				&& n1.distance(n2) < n1.getCommunicationRange());
	}
}
