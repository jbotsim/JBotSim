package examples.misc.dynamicgraphs;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

import io.jbotsim.core.Color;
import java.util.List;

public class TestSendRetry extends Node{
	@Override
	public void onSelection() {
        setColor(Color.black);
		List<Node> nodes = getTopology().getNodes();
		Node other = nodes.get((nodes.indexOf(this) + 1) % 2);
        other.setColor(Color.green);
        sendRetry (other, new Message());
	}

	public void onMessage(Message msg) {
		setColor(Color.red);
	}
	public static void main(String args[]){
		Topology tp = new Topology();
        tp.setDefaultNodeModel(TestSendRetry.class);
		new JViewer(tp);
		tp.start();
	}
}
