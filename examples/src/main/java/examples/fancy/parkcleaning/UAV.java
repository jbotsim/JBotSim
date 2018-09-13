package examples.fancy.parkcleaning;

import jbotsim.Node;

public class UAV extends Node{
	public UAV(){
		this.setCommunicationRange(100);
		this.setSensingRange(40);
		this.setIcon("/examples/n_parc/gmuav.png");
		this.setSize(20);
		this.setProperty("type", "rwp");
		this.setDirection(2);
	}

	@Override
	public void onSensingIn(Node node) {
		if (node instanceof Garbage && getColor()==null) {
			setProperty("examples/n_parc", node);
			setColor(node.getColor());
		}
		if (node instanceof Robot &&
				node.getColor()==this.getColor() && ((Robot)node).target==null){
			((Robot)node).target = ((Node)this.getProperty("examples/n_parc")).getLocation();
			setColor(null);
		}
	}

}
